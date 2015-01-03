# Generic AWS CloudWatch to Spreadsheet Exporter

CloudWatch doesn't provide an Export utility - this does.  awscwxls creates spreadsheets based on generic sets of Namespace/Dimension/Metric/Statistic specifications.
As long as AWS continues to follow the Namespace/Dimension/Metric/Statistic pattern, awscwxls should work for existing and future Namespaces (Services). Each set of specifications is stored in a properties file, so each properties file can be configured for a specific set of AWS Services and resources.

To run:
* Prereqs
	* Java 1.7
	* AWS account, .aws/credentials file containing your keys
	* AWS account must have CloudWatch IAM permission 
* Copy the run/ directory to the local file system
* Review properties/first.properties.  This is a very simple properties file that will extract EC2 statistics for a single instance
* Update properties/first.properties based on the TODO comments in the file
* Run
	* Linux: 	./awscwxls properties/first.properties
	* Windows:	awscwxls properties/first.properties
* Review the spreadsheet
* Advanced
	* Review properties/template.properties.  This is an example of collecting stats from multiple Namespace/Dimension/Metric/Statistic combinations
 
Java Project Overview
 * Eclipse project
 * Uses Apache POI to create the spreadsheets.  Take a look at BaseXls.java, encapsulates most of the POI complexity, MetricSpreadsheet.java creates the xls.
 * Entry point: ExtractSpreadsheet.main()
 * ExtractMetrics.java does most of the heavy lifting - connecting to CW and getting the metrics/statistics
 

