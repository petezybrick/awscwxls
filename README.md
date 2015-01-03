# Generic AWS CloudWatch to Spreadsheet Exporter

CloudWatch doesn't provide an Export utility - this does.  awscwxls creates spreadsheets based on generic sets of Namespace/Dimension/Metric/Statistic specifications.
As long as AWS continues to follow the Namespace/Dimension/Metric/Statistic pattern, awscwxls should work for existing and future Namespaces (Services). Each set of specifications is stored in a properties file, so each properties file can be configured for a specific set of AWS Services and resources.
  

