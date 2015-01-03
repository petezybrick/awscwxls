package com.ipcglobal.awscwxls.cw;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MetricSet contains all of the Metric|Statistic values for a given date/time.
 * Each MetricSet corresponds to a single output row in the XLS
 */
public class MetricSet implements Comparable<MetricSet> {
	
	/** The date. */
	public Date date;
	
	/** The metric statistic values. */
	public HashMap<String, Double> metricStatisticValues = new HashMap<String, Double>();


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MetricSet compare) {
		return (int) (date.compareTo(compare.date));
	}

	/**
	 * Sets the metric statistic value.
	 *
	 * @param metricNameStatName the metric name stat name
	 * @param value the value
	 */
	public void setMetricStatisticValue(String metricNameStatName, Double value) {
		metricStatisticValues.put(metricNameStatName, value);
	}


	/**
	 * Gets the metric statistic values.
	 *
	 * @return the metric statistic values
	 */
	public Map<String, Double> getMetricStatisticValues() {
		return metricStatisticValues;
	}

	/**
	 * Gets the metric statistic value.
	 *
	 * @param metricNameStatisticName the metric name statistic name
	 * @return the metric statistic value
	 */
	public Double getMetricStatisticValue(String metricNameStatisticName) {
		return metricStatisticValues.get(metricNameStatisticName);
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

}