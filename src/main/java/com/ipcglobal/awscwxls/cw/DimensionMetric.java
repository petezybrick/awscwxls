package com.ipcglobal.awscwxls.cw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DimensionMetric contains all the Metrics and Statistics for a given Dimension
 * 
 * A DimensionMetric exists for each blank-delimited value in the property dimensionValues.n 
 * For example, if 
 * 		dimensionValues.0 = i-1a2b3c4d i-5e6f7g8h
 * then there will be 2 DimensionMetric instances, one with a dimensionValue of i-1a2b3c4d, 
 * the other with a dimensionValue of i-5e6f7g8h 
 */
public class DimensionMetric {
	
	/** The dimension value. This corresponds each blank-delimited value in the property dimensionValues.*/
	private String dimensionValue;
	
	/** The metric sets. Each metric set contains all Metric|Statistic values for a given date/time */
	private List<MetricSet> metricSets;
	
	/** The distinct metric name by statistic names.  Used to create sorted list so that the Metric|Statistic columns
	 *  will be output in the spreadsheet in a consistent manner
	*/
	private Map<String,Object> distinctMetricNameStatisticNames = new HashMap<String,Object>();

	/**
	 * Instantiates a new dimension metric.
	 */
	public DimensionMetric() {
		super();
	}
	
	/**
	 * Creates the sorted list distinct metric name statistic names.
	 *
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<String> createSortedListDistinctMetricNameStatisticNames() throws Exception {
		List<String> sortedListDistinctMetricNameStatisticNames = new ArrayList<String>( distinctMetricNameStatisticNames.keySet() );
		Collections.sort( sortedListDistinctMetricNameStatisticNames );
		return sortedListDistinctMetricNameStatisticNames;
	}
	
	/**
	 * Put distinct metric name statistic name.
	 *
	 * @param metricNameStatisticName the metric name statistic name
	 */
	public void putDistinctMetricNameStatisticName( String metricNameStatisticName ) {
		distinctMetricNameStatisticNames.put( metricNameStatisticName, null );
	}

	/**
	 * Gets the dimension value.
	 *
	 * @return the dimension value
	 */
	public String getDimensionValue() {
		return dimensionValue;
	}

	/**
	 * Sets the dimension value.
	 *
	 * @param dimensionValue the new dimension value
	 */
	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}

	/**
	 * Gets the metric sets.
	 *
	 * @return the metric sets
	 */
	public List<MetricSet> getMetricSets() {
		return metricSets;
	}

	/**
	 * Sets the metric sets.
	 *
	 * @param metricSets the new metric sets
	 */
	public void setMetricSets(List<MetricSet> metricSets) {
		this.metricSets = metricSets;
	}
	
	/**
	 * With dimension value.
	 *
	 * @param dimensionValue the dimension value
	 * @return the dimension metric
	 */
	public DimensionMetric withDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
		return this;
	}

	/**
	 * With metric sets.
	 *
	 * @param metricSets the metric sets
	 * @return the dimension metric
	 */
	public DimensionMetric withMetricSets(List<MetricSet> metricSets) {
		this.metricSets = metricSets;
		return this;
	}

	/**
	 * Gets the distinct metric name statistic names.
	 *
	 * @return the distinct metric name statistic names
	 */
	public Map<String, Object> getDistinctMetricNameStatisticNames() {
		return distinctMetricNameStatisticNames;
	}

	/**
	 * Sets the distinct metric name statistic names.
	 *
	 * @param distinctMetricNameStatisticNames the distinct metric name statistic names
	 */
	public void setDistinctMetricNameStatisticNames(
			Map<String, Object> distinctMetricNameStatisticNames) {
		this.distinctMetricNameStatisticNames = distinctMetricNameStatisticNames;
	}
}

