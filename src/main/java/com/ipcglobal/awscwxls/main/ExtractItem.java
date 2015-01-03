package com.ipcglobal.awscwxls.main;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * ExtractItem contains values from the Properties file necessary to perform an Extract. 
 * All of these values correspond to a name.offset in the properties file, i.e. namespace.0, periodMinutes.0, etc.
 */
public class ExtractItem {
	
	/** The namespace. */
	private String namespace;
	
	/** The period minutes. */
	private int periodMinutes;
	
	/** The dimension name. */
	private String dimensionName;
	
	/** The dimension values. */
	private List<String> dimensionValues;
	
	/** The dimension statistic names. */
	private List<String> dimensionStatisticNames;
	
	/** The offset. */
	private int offset;
	
	/**
	 * Instantiates a new extract item.
	 *
	 * @param properties the properties
	 * @param offset the offset
	 */
	public ExtractItem( Properties properties, int offset ) {
		this.namespace = properties.getProperty("namespace."+offset).trim();
		this.periodMinutes = Integer.parseInt( properties.getProperty("periodMinutes."+offset) );
		this.dimensionName = properties.getProperty("dimensionName."+offset).trim();
		this.dimensionValues = Arrays.asList( properties.getProperty("dimensionValues."+offset).split("[ ]") );
		this.dimensionStatisticNames = Arrays.asList( properties.getProperty("dimensionStatisticNames."+offset).split("[ ]") );
		this.offset = offset;
	}

	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 *
	 * @param namespace the new namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Gets the dimension name.
	 *
	 * @return the dimension name
	 */
	public String getDimensionName() {
		return dimensionName;
	}

	/**
	 * Sets the dimension name.
	 *
	 * @param dimensionName the new dimension name
	 */
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}

	/**
	 * Gets the dimension values.
	 *
	 * @return the dimension values
	 */
	public List<String> getDimensionValues() {
		return dimensionValues;
	}

	/**
	 * Sets the dimension values.
	 *
	 * @param dimensionValues the new dimension values
	 */
	public void setDimensionValues(List<String> dimensionValues) {
		this.dimensionValues = dimensionValues;
	}

	/**
	 * Gets the dimension statistic names.
	 *
	 * @return the dimension statistic names
	 */
	public List<String> getDimensionStatisticNames() {
		return dimensionStatisticNames;
	}

	/**
	 * Sets the dimension statistic names.
	 *
	 * @param dimensionStatisticNames the new dimension statistic names
	 */
	public void setDimensionStatisticNames(List<String> dimensionStatisticNames) {
		this.dimensionStatisticNames = dimensionStatisticNames;
	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the offset.
	 *
	 * @param offset the new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Gets the period minutes.
	 *
	 * @return the period minutes
	 */
	public int getPeriodMinutes() {
		return periodMinutes;
	}

	/**
	 * Sets the period minutes.
	 *
	 * @param periodMinutes the new period minutes
	 */
	public void setPeriodMinutes(int periodMinutes) {
		this.periodMinutes = periodMinutes;
	}

}
