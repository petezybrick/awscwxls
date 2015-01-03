package com.ipcglobal.awscwxls.main;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ipcglobal.awscwxls.cw.DimensionMetric;
import com.ipcglobal.awscwxls.cw.ExtractMetrics;
import com.ipcglobal.awscwxls.util.LogTool;
import com.ipcglobal.awscwxls.xls.MetricSpreadsheet;

/**
 * ExtractSpreadsheet is the main entry point, it is passed a properties file to determine the execution path.
 */
public class ExtractSpreadsheet {
	
	/** The log. */
	private static Log log = LogFactory.getLog(ExtractSpreadsheet.class);

	/**
	 * The main method.
	 * A single argument is required - the path/name.ext of the properties file.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		if( args.length == 0 ) throw new Exception("Properties file path/name.ext must be passed");
		LogTool.initConsole();
		Properties properties = new Properties( );
		properties.load( new FileInputStream(args[0]));
		ExtractMetrics extractMetrics = new ExtractMetrics( properties );
		MetricSpreadsheet metricSpreadsheet = new MetricSpreadsheet( properties );
		boolean isSpreadsheetCreated = false;
		
		for( int offset=0 ; ; offset++ ) {
			String namespace = properties.getProperty("namespace."+offset);
			if( namespace == null || "".equals(namespace)) break;
			
			ExtractItem extractItem = new ExtractItem( properties, offset );
			List<DimensionMetric> dimensionMetrics = extractMetrics.extractMetricsByDimension(extractItem);
			metricSpreadsheet.createSheet( dimensionMetrics, extractItem );
			isSpreadsheetCreated = true;
		}

		if( isSpreadsheetCreated ) metricSpreadsheet.writeWorkbook( );
		else log.error("Invalid Properties file - must contain at least one resource, i.e. one EC2 instance, one ELB name, etc.");

	}

}
