package com.ipcglobal.awscwxls.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.ipcglobal.awscwxls.cw.DimensionMetric;
import com.ipcglobal.awscwxls.cw.MetricSet;
import com.ipcglobal.awscwxls.main.ExtractItem;


/**
 * MetricSpreadsheet uses POI to generate the spreadsheet
 * 
 * One sheet is created per set of property values, i.e. if namespace.0=AWS/EC2 and namespace.1=AWS/ELB,
 * then two sheets will be created: AWS-EC2-0 and AWS-ELB-1.
 */
public class MetricSpreadsheet extends BaseXls {
	
	/** The log. */
	private static Log log = LogFactory.getLog(MetricSpreadsheet.class);
	
	/** The properties. */
	private Properties properties;
	
	
	/**
	 * Instantiates a new metric spreadsheet.
	 *
	 * @param properties the properties
	 * @throws Exception the exception
	 */
	public MetricSpreadsheet( Properties properties ) throws Exception {
		super();
		this.properties = properties;
	}

	
	/**
	 * Creates the sheet.
	 *
	 * @param dimensionMetrics the dimension metrics
	 * @param extractItem the extract item
	 * @throws Exception the exception
	 */
	public void createSheet( List<DimensionMetric> dimensionMetrics, ExtractItem extractItem ) throws Exception {
		List<String> sortedListDistinctMetricNameStatisticNames = dimensionMetrics.get(0).createSortedListDistinctMetricNameStatisticNames();
		// sheetname can't contain a backslash
		String sheetName = extractItem.getNamespace().replace("/", "-") + "-" + extractItem.getOffset();
		Sheet sheet = wb.createSheet( sheetName );
		processColumnWidths(sheet, sortedListDistinctMetricNameStatisticNames.size() );
		sheet.createFreezePane(0, 1, 0, 1); // freeze top row
		sheet.getPrintSetup().setLandscape(true);
		sheet.setAutobreaks(true);
		sheet.getPrintSetup().setFitWidth((short) 1);
		sheet.getPrintSetup().setFitHeight((short) 1);

		int rowCnt = 0;

		// Header
		int colCnt = 0;
		Row rowHdr = sheet.createRow(rowCnt);
		List<String> hdrNames = new ArrayList<String>(Arrays.asList(
				extractItem.getDimensionName(), "Date"));
		hdrNames.addAll(sortedListDistinctMetricNameStatisticNames);

		for (int i = 0; i < hdrNames.size(); i++) {
			Cell cellHdr = rowHdr.createCell(colCnt, Cell.CELL_TYPE_STRING);
			CellStyle style = findCellStyle("Arial", HSSFColor.WHITE.index,	(short) 11, XSSFFont.BOLDWEIGHT_BOLD, cellStyleFromHdrAlign(HdrAlign.Center), XSSFCellStyle.VERTICAL_TOP, HSSFColor.LIGHT_BLUE.index, CellBorder.All_Thin, formatGeneral);
			style.setWrapText(true);
			cellHdr.setCellStyle(style);
			cellHdr.setCellValue(hdrNames.get(i));
			colCnt++;
		}
		rowCnt++;
		
		// Data
		for( DimensionMetric dimensionMetric : dimensionMetrics )  {

			for( MetricSet metricSet : dimensionMetric.getMetricSets() ) { 
				Row rowData = sheet.createRow(rowCnt);
				// DimensionValue (i.e. the instance id) and Date are always the first two columns
				populateCell( rowData, 0, DataType.Text, dimensionMetric.getDimensionValue() );
				populateCell( rowData, 1, DataType.Date, metricSet.getDate() );
				colCnt = 2;
				for( String metricNameStatisticName : sortedListDistinctMetricNameStatisticNames ) {
					Double value = metricSet.getMetricStatisticValue(metricNameStatisticName);
					populateCell( rowData, colCnt, findDataTypeFromMetricNameStatisticName( metricNameStatisticName ), value );
					colCnt++;
				}
				rowCnt++;
			}
		}
	}


	/**
	 * Find data type from metric name statistic name.
	 *
	 * @param metricNameStatisticName the metric name statistic name
	 * @return the data type
	 */
	private DataType findDataTypeFromMetricNameStatisticName( String metricNameStatisticName ) {
		String[] temp = metricNameStatisticName.split("[|]");
		if( "Average".equals(temp[1]) || "Minimum".equals(temp[1]) || "Maximum".equals(temp[1]) )
			return DataType.NumericDec2;
		else return DataType.Numeric;
	}
	
	
	/**
	 * Populate cell.
	 *
	 * @param rowData the row data
	 * @param colCnt the col cnt
	 * @param dataType the data type
	 * @param obj the obj
	 * @throws Exception the exception
	 */
	private void populateCell( Row rowData, int colCnt, DataType dataType, Object obj ) throws Exception {
		int cellType = 0;
		
		if (dataType == DataType.Numeric)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		else if (dataType == DataType.NumericDec2)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		else if (dataType == DataType.Text)
			cellType = XSSFCell.CELL_TYPE_STRING;
		else if (dataType == DataType.Date)
			cellType = XSSFCell.CELL_TYPE_STRING;
		else if (dataType == DataType.Accounting)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		else if (dataType == DataType.Percent)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		Cell cellData = rowData.createCell(colCnt, cellType);

		short findFormat = -1;
		if (dataType == DataType.Date)
			findFormat = formatMmDdYyyy;
		else if (dataType == DataType.Percent)
			findFormat = formatPercent;
		else if (dataType == DataType.Accounting)
			findFormat = formatAccounting;
		else if (dataType == DataType.Numeric)
			findFormat = formatNumeric;
		else if (dataType == DataType.NumericDec2)
			findFormat = formatNumericDec2;
		else
			findFormat = formatGeneral;
		CellStyle style = findCellStyle("Arial", HSSFColor.BLACK.index,
				(short) 11, XSSFFont.BOLDWEIGHT_NORMAL,
				cellStyleFromDataAlign(findAlignByDataType(dataType)),
				XSSFCellStyle.VERTICAL_TOP, BG_COLOR_NONE,
				CellBorder.All_Thin, findFormat );
		cellData.setCellStyle(style);

		if (dataType == DataType.Numeric || dataType == DataType.NumericDec2 
				|| dataType == DataType.Accounting || dataType == DataType.Percent) {
			if (obj == null)
				; // leave the cell empty
			else if (obj instanceof BigDecimal) {
				BigDecimal value = (BigDecimal) obj;
				if (value != null)
					cellData.setCellValue(value.doubleValue());
			} else if (obj instanceof Integer) {
				Integer value = (Integer) obj;
				if (value != null)
					cellData.setCellValue(value.intValue());
			} else if (obj instanceof Long) {
				Long value = (Long) obj;
				if (value != null)
					cellData.setCellValue(value.longValue());
			} else if (obj instanceof Double) {
				Double value = (Double) obj;
				if (value != null)
					cellData.setCellValue(value.doubleValue());
			} else if (obj instanceof Short) {
				Short value = (Short) obj;
				if (value != null)
					cellData.setCellValue(value.shortValue());
			} else
				throw new Exception("Unsupported numeric type: "
						+ obj.getClass().getSimpleName());
		} else if (dataType == DataType.Date) {
			Date date = (Date)obj;
			if (date != null)
				cellData.setCellValue(date);
		} else {
			cellData.setCellValue((String) obj );
		}
	}	
	
	
	/**
	 * Write workbook.
	 *
	 * @throws Exception the exception
	 */
	public void writeWorkbook( ) throws Exception {
		String xlsPathNamePrefix = properties.getProperty("xlsPathNamePrefix");
		final SimpleDateFormat dfYyyyMmDdHhMmSs = new SimpleDateFormat( "yyyyMMdd-HHmmss" );
		String xlsPathNameExt = xlsPathNamePrefix + "_" + dfYyyyMmDdHhMmSs.format(new Date())+ ".xlsx";
		log.info("Writing workbook: " + xlsPathNameExt );
		new File(xlsPathNameExt).delete();
		FileOutputStream fos = new FileOutputStream(xlsPathNameExt);
		wb.write( fos );
		fos.close();
	}

	/**
	 * Process column widths.
	 *
	 * @param sheet the sheet
	 * @param numMetricColumns the num metric columns
	 * @throws Exception the exception
	 */
	private void processColumnWidths(Sheet sheet, int numMetricColumns )
			throws Exception {
		final Integer widthDimensionValue = 16;
		final Integer widthDate = 20;
		final Integer widthMetricColumn = 20;
		List<Integer> widths = new ArrayList<Integer>();
		widths.add( widthDimensionValue );
		widths.add( widthDate );
		for( int i=0 ; i<numMetricColumns ; i++ ) widths.add( widthMetricColumn );
		int colNum = 0;
		for (int width : widths) {
			sheet.setColumnWidth(colNum++, width * COLUMN_WIDTH_FACTOR);
		}
	}

}
