package com.ipcglobal.awscwxls.cw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.ipcglobal.awscwxls.main.ExtractItem;
import com.ipcglobal.awscwxls.util.Utils;


/**
 * ExtractMetrics calls CloudWatch to extract the Metric|Statistic combinations (i.e. CPUUtilization|Average) by 
 * Namespace (i.e. AWS/EC2) and date/time range
 */
public class ExtractMetrics {
	
	/** The log. */
	private static Log log = LogFactory.getLog(ExtractMetrics.class);
	
	/** The credentials provider. */
	private AWSCredentialsProvider credentialsProvider;
	
	/** The CloudWatch client. */
	private AmazonCloudWatchClient cwClient;
	
	/** The metric names. */
	private List<String> metricNames;
	
	/** The dimension statistics by metric. */
	private Map<String,List<String>> dimensionStatisticsByMetric;
	
	/** The properties. */
	private Properties properties;
	
	
	/**
	 * Instantiates a new extract metrics.
	 *dimensionValues.
	 * @param properties the properties
	 * @throws Exception the exception
	 */
	public ExtractMetrics( Properties properties ) throws Exception {
		this.properties = properties;
		String credentialsProfileName = properties.getProperty("credentialsProfileName").trim();
		String credentialsRegion = properties.getProperty("credentialsRegion").trim();
		
		if( credentialsProfileName == null ) this.credentialsProvider = Utils.initCredentials();
		else this.credentialsProvider = Utils.initProfileCredentialsProvider( credentialsProfileName );

		this.cwClient = new AmazonCloudWatchClient(credentialsProvider);
		if( credentialsRegion != null ) {
			Regions regions = Regions.fromName( credentialsRegion );
			overrideRegion( regions );
		}
	}
	
	
	/**
	 * Override region.
	 *
	 * @param regions the regions
	 * @throws Exception the exception
	 */
	public void overrideRegion( Regions regions ) throws Exception {
		this.cwClient.setRegion(Region.getRegion( regions ));
	}


	/**
	 * Extract metrics by dimension.
	 *
	 * @param extractItem the extract item
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<DimensionMetric> extractMetricsByDimension( ExtractItem extractItem ) throws Exception {
		initStatisticsByMetric( extractItem.getDimensionStatisticNames() );
		List<DimensionMetric> dimensionMetrics = new ArrayList<DimensionMetric>();
		GetMetricStatisticsRequest getMetricRequest = new GetMetricStatisticsRequest();
		getMetricRequest.setNamespace( extractItem.getNamespace() );
		getMetricRequest.setPeriod( extractItem.getPeriodMinutes() * 60 );

		final SimpleDateFormat dfYyyyMmDdHhMmSs = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		getMetricRequest.setStartTime( dfYyyyMmDdHhMmSs.parse( properties.getProperty("startTime" ).trim() ) );
		getMetricRequest.setEndTime( dfYyyyMmDdHhMmSs.parse( properties.getProperty("endTime").trim() ) );

		// Process each DimensionValue (from property dimentionValue.n), i.e. i-1a2b3c4d i-5e6f7g8h for EC2
		for( String dimensionValue : extractItem.getDimensionValues() ) {
			HashMap<Long, MetricSet> metricSets = new HashMap<Long, MetricSet>();
			
			DimensionMetric dimensionMetric = new DimensionMetric().withDimensionValue( dimensionValue ); 
			dimensionMetrics.add( dimensionMetric );
			ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
			// Create the CloudWatch dimension based on the property dimensionName.n i.e. InstanceId for EC2
			dimensions.add(new Dimension().withName( extractItem.getDimensionName() ).withValue( dimensionValue ));
			getMetricRequest.setDimensions(dimensions);
	
			for (String metricName : metricNames ) {
				log.info( extractItem.getNamespace() + ": dimensionName=" + extractItem.getDimensionName()  + ", dimensionValue=" + dimensionValue + ", metric="+metricName );
				List<String> statistics = dimensionStatisticsByMetric.get(metricName);
				for( String statistic : statistics ) {
					String metricNameStatisticName = new StringBuilder(metricName).append("|").append(statistic).toString();
					dimensionMetric.putDistinctMetricNameStatisticName( metricNameStatisticName );
				}
				getMetricRequest.setStatistics(statistics);
				getMetricRequest.setMetricName(metricName);
				GetMetricStatisticsResult getMetricStatisticsResult = cwClient.getMetricStatistics(getMetricRequest);
				List<Datapoint> datapoints = getMetricStatisticsResult.getDatapoints();
				for (Datapoint datapoint : datapoints) {
					// MetricSet's are keyed by DateTime so that all values for a given DateTime can be output to the
					// XLS in the same row
					MetricSet metricSet = metricSets.get(datapoint.getTimestamp().getTime());
					if (metricSet == null) {
						metricSet = new MetricSet();
						metricSet.date = datapoint.getTimestamp();
						metricSets.put(datapoint.getTimestamp().getTime(), metricSet);
					}
					// Set each Metric|Statistic in the MetricSet
					for( String statistic : statistics ) {
						String metricNameStatisticName = new StringBuilder(metricName).append("|").append(statistic).toString();
						Double value = findStatisticValue( statistic, datapoint);
						metricSet.setMetricStatisticValue( metricNameStatisticName, value);
					}
				}
			}
			
			// Sort the MetricSet to ensure the columns will be output consistently in the XLS
			ArrayList<MetricSet> sortedMetricSets = new ArrayList<MetricSet>( metricSets.values() );
			Collections.sort( sortedMetricSets );
			dimensionMetric.setMetricSets( sortedMetricSets );
		}
		return dimensionMetrics;
	}
	
	
	/**
	 * Inits the statistics by metric.
	 *
	 * @param dimensionStatisticNames the dimension statistic names
	 * @throws Exception the exception
	 */
	private void initStatisticsByMetric( List<String> dimensionStatisticNames ) throws Exception {
		dimensionStatisticsByMetric = new HashMap<String,List<String>>();
		for( String metricStatisticName : dimensionStatisticNames ) {
			String[] temp = metricStatisticName.split("[|]");
			String metricName = temp[0];
			String statisticName = temp[1];
			List<String> statisticNames = dimensionStatisticsByMetric.get( metricName );
			if( statisticNames == null ) {
				statisticNames = new ArrayList<String>();
				dimensionStatisticsByMetric.put( metricName, statisticNames );
			}
			statisticNames.add( statisticName );
		}
		metricNames = new ArrayList<String>( dimensionStatisticsByMetric.keySet() );
		Collections.sort( metricNames );
	}

	
	/**
	 * Find statistic value.
	 *
	 * @param statistic the statistic
	 * @param datapoint the datapoint
	 * @return the double
	 * @throws Exception the exception
	 */
	private Double findStatisticValue( String statistic, Datapoint datapoint) throws Exception {
		if( "Average".equals(statistic) ) return datapoint.getAverage();
		else if( "Maximum".equals(statistic) ) return datapoint.getMaximum();
		else if( "Minimum".equals(statistic) ) return datapoint.getMinimum();
		else if( "Sum".equals(statistic) ) return datapoint.getSum();
		else if( "SampleCount".equals(statistic) ) return datapoint.getSampleCount();
		else throw new Exception("Invalid statistic: " + statistic);
	}
	
}
