package com.ipcglobal.awscwxls.xls;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * BaseXls encapsulates the much of the complexity of generating XLS's using POI.
 * 
 * To minimize memory usage, Fonts and CellStyles are managed in Maps.  For example,
 * a given Font or CellStyle exists only once in the map and is reused across the document.
 */
public class BaseXls {
	
	/** The log. */
	private static Log log = LogFactory.getLog(BaseXls.class);
	
	/**
	 * The Enum HdrAlign.
	 */
	public enum HdrAlign { 
 /** The Left. */
 Left, 
 /** The Center. */
 Center, 
 /** The Right. */
 Right };
	
	/**
	 * The Enum DataAlign.
	 */
	public enum DataAlign { 
 /** The Left. */
 Left, 
 /** The Center. */
 Center, 
 /** The Right. */
 Right };
	
	/**
	 * The Enum DataType.
	 */
	public enum DataType { 
 /** The Text. */
 Text, 
 /** The Date. */
 Date, 
 /** The Numeric. */
 Numeric, 
 /** The Numeric dec2. */
 NumericDec2, 
 /** The Accounting. */
 Accounting, 
 /** The Formula. */
 Formula, 
 /** The Percent. */
 Percent };
	
	/** The Constant COLUMN_WIDTH_FACTOR. */
	public static final int COLUMN_WIDTH_FACTOR = 278;
	
	/** The wb. */
	protected Workbook wb;
	
	/** The format general. */
	protected Short formatGeneral;
	
	/** The format numeric. */
	protected Short formatNumeric;
	
	/** The format numeric dec2. */
	protected Short formatNumericDec2;
	
	/** The format mm dd yyyy. */
	protected Short formatMmDdYyyy;
	
	/** The format accounting. */
	protected Short formatAccounting;
	
	/** The format percent. */
	protected Short formatPercent;

	
	/** The fonts. */
	protected Map<String,Font> fonts = new HashMap<String,Font>();
	
	/** The cell styles. */
	protected Map<String,CellStyle> cellStyles = new HashMap<String,CellStyle>();
	
	/**
	 * The Enum CellBorder.
	 */
	public enum CellBorder { 
 /** The None. */
 None,
		
		/** The All_ thin. */
		All_Thin, 
 /** The Top_ thin. */
 Top_Thin, 
 /** The Bottom_ thin. */
 Bottom_Thin, 
 /** The Right_ thin. */
 Right_Thin, 
 /** The Left_ thin. */
 Left_Thin,
		
		/** The All_ medium. */
		All_Medium, 
 /** The Top_ medium. */
 Top_Medium, 
 /** The Bottom_ medium. */
 Bottom_Medium, 
 /** The Right_ medium. */
 Right_Medium, 
 /** The Left_ medium. */
 Left_Medium,
		
		/** The All_ thick. */
		All_Thick, 
 /** The Top_ thick. */
 Top_Thick, 
 /** The Bottom_ thick. */
 Bottom_Thick, 
 /** The Right_ thick. */
 Right_Thick, 
 /** The Left_ thick. */
 Left_Thick
		};
	
	/** The bg color none. */
	public short BG_COLOR_NONE = -1;
	
	/**
	 * Instantiates a new base xls.
	 */
	public BaseXls() {
		this.wb = new XSSFWorkbook();
		CreationHelper creationHelper = wb.getCreationHelper();
		this.formatGeneral = creationHelper.createDataFormat().getFormat("General");
		this.formatNumeric = creationHelper.createDataFormat().getFormat("#,##0");
		this.formatNumericDec2 = creationHelper.createDataFormat().getFormat("#,##0.00");
		this.formatMmDdYyyy = creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss");
		this.formatAccounting = creationHelper.createDataFormat().getFormat("_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)");
		this.formatPercent = creationHelper.createDataFormat().getFormat("0.00%");

	}

	/**
	 * Find cell style.
	 *
	 * @param fontName the font name
	 * @param fontColor the font color
	 * @param fontHeight the font height
	 * @param fontWeight the font weight
	 * @return the cell style
	 * @throws Exception the exception
	 */
	public CellStyle findCellStyle( String fontName, short fontColor, short fontHeight, 
			short fontWeight ) throws Exception {
		return findCellStyle( fontName, fontColor, fontHeight, 
				fontWeight, (short)-1, (short)-1, BG_COLOR_NONE, CellBorder.None, (short)-1 );
	}

	/**
	 * Find cell style.
	 *
	 * @param fontName the font name
	 * @param fontColor the font color
	 * @param fontHeight the font height
	 * @param fontWeight the font weight
	 * @param alignHorz the align horz
	 * @param alignVert the align vert
	 * @param bgColor the bg color
	 * @param cellBorder the cell border
	 * @return the cell style
	 * @throws Exception the exception
	 */
	public CellStyle findCellStyle( String fontName, short fontColor, short fontHeight, 
			short fontWeight, short alignHorz, short alignVert, 
			short bgColor, CellBorder cellBorder ) throws Exception {
		return findCellStyle( fontName, fontColor, fontHeight, 
				fontWeight, alignHorz, alignVert, bgColor, cellBorder, (short)-1 );
	}
	
	/**
	 * Find cell style.
	 *
	 * @param fontName the font name
	 * @param fontColor the font color
	 * @param fontHeight the font height
	 * @param fontWeight the font weight
	 * @param alignHorz the align horz
	 * @param alignVert the align vert
	 * @param bgColor the bg color
	 * @param cellBorder the cell border
	 * @param dataFormat the data format
	 * @return the cell style
	 * @throws Exception the exception
	 */
	public CellStyle findCellStyle( String fontName, short fontColor, short fontHeight, 
			short fontWeight, short alignHorz, short alignVert, 
			short bgColor, CellBorder cellBorder, 
			short dataFormat ) throws Exception {
		String keyStyle = new StringBuffer()
			.append( fontName ).append("|")
			.append( fontColor ).append("|")
			.append( fontHeight ).append("|")
			.append( fontWeight ).append("|")
			.append( alignHorz ).append("|")
			.append( alignVert ).append("|")
			.append( bgColor ).append("|")
			.append( cellBorder ).append("|")
			.append( dataFormat ).append("|")
			.toString();
		CellStyle cellStyle = cellStyles.get( keyStyle );
		if( cellStyle == null ) {
			String keyFont = new StringBuffer()
				.append( fontName ).append("|")
				.append( fontColor ).append("|")
				.append( fontHeight ).append("|")
				.append( fontWeight ).append("|")
				.toString();
			Font font = fonts.get( keyFont );
			if( font == null ) {
				font = wb.createFont();
				fonts.put( keyFont, font );
				font.setFontName(fontName);
				font.setFontHeightInPoints( fontHeight );
				font.setBoldweight( fontWeight );
				font.setColor( fontColor );
			}
			cellStyle = wb.createCellStyle();
			cellStyles.put( keyStyle, cellStyle );
			cellStyle.setWrapText(true);
			cellStyle.setFont( font );
			if( bgColor != BG_COLOR_NONE ) {
				cellStyle.setFillForegroundColor( bgColor );
				cellStyle.setFillPattern( CellStyle.SOLID_FOREGROUND);
			}
			if( alignHorz != -1 ) 
				cellStyle.setAlignment( alignHorz );
			if( alignVert != -1 )
				cellStyle.setVerticalAlignment( alignVert );
			if( dataFormat != -1 ) {
				cellStyle.setDataFormat( dataFormat );
			}
			if( cellBorder != null ) 
				addBorderToStyle( cellStyle, cellBorder );
		}
		
		return cellStyle;
	}

	
	/**
	 * Adds the border to style.
	 *
	 * @param style the style
	 * @param cellBorder the cell border
	 * @throws Exception the exception
	 */
	protected void addBorderToStyle( CellStyle style, CellBorder cellBorder ) throws Exception {
	    if( cellBorder == CellBorder.All_Thin || cellBorder == CellBorder.Bottom_Thin ) {
			style.setBorderBottom(CellStyle.BORDER_THIN);
		    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Thin || cellBorder == CellBorder.Left_Thin ) {
		    style.setBorderLeft(CellStyle.BORDER_THIN);
		    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Thin || cellBorder == CellBorder.Right_Thin ) {
		    style.setBorderRight(CellStyle.BORDER_THIN);
		    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Thin || cellBorder == CellBorder.Top_Thin ) {
		    style.setBorderTop(CellStyle.BORDER_THIN);
		    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    
	    if( cellBorder == CellBorder.All_Medium || cellBorder == CellBorder.Bottom_Medium ) {
			style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Medium || cellBorder == CellBorder.Left_Medium ) {
		    style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Medium || cellBorder == CellBorder.Right_Medium ) {
		    style.setBorderRight(CellStyle.BORDER_MEDIUM);
		    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Medium || cellBorder == CellBorder.Top_Medium ) {
		    style.setBorderTop(CellStyle.BORDER_MEDIUM);
		    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    
	    if( cellBorder == CellBorder.All_Thick || cellBorder == CellBorder.Bottom_Thick ) {
			style.setBorderBottom(CellStyle.BORDER_THICK);
		    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Thick || cellBorder == CellBorder.Left_Thick ) {
		    style.setBorderLeft(CellStyle.BORDER_THICK);
		    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Thick || cellBorder == CellBorder.Right_Thick ) {
		    style.setBorderRight(CellStyle.BORDER_THICK);
		    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    if( cellBorder == CellBorder.All_Thick || cellBorder == CellBorder.Top_Thick ) {
		    style.setBorderTop(CellStyle.BORDER_THICK);
		    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    }
	    
	}


	/**
	 * Find align by data type.
	 *
	 * @param dataType the data type
	 * @return the data align
	 */
	protected DataAlign findAlignByDataType(DataType dataType) {
		if (dataType == DataType.Text)
			return DataAlign.Left;
		else if (dataType == DataType.Numeric
				|| dataType == DataType.NumericDec2 
				|| dataType == DataType.Accounting 
				|| dataType == DataType.Percent )
			return DataAlign.Right;
		else if (dataType == DataType.Date)
			return DataAlign.Center;
		else
			return DataAlign.Left;
	}

	
	/**
	 * Convert sql to xls data type.
	 *
	 * @param sqlType the sql type
	 * @return the data type
	 */
	protected DataType convertSqlToXlsDataType(int sqlType) {
		DataType dataType = DataType.Text;
		switch (sqlType) {
		case java.sql.Types.BIGINT:
		case java.sql.Types.DOUBLE:
		case java.sql.Types.INTEGER:
		case java.sql.Types.NUMERIC:
		case java.sql.Types.REAL:
		case java.sql.Types.SMALLINT:
		case java.sql.Types.TINYINT:
			dataType = DataType.Numeric;
			break;

		case java.sql.Types.DECIMAL:
		case java.sql.Types.FLOAT:
			dataType = DataType.Accounting;
			break;

		case java.sql.Types.CHAR:
		case java.sql.Types.LONGVARCHAR:
		case java.sql.Types.VARCHAR:
			dataType = DataType.Text;
			break;

		case java.sql.Types.DATE:
		case java.sql.Types.TIME:
		case java.sql.Types.TIMESTAMP:
			dataType = DataType.Date;
			break;
		}
		return dataType;
	}

	/**
	 * Cell style from hdr align.
	 *
	 * @param dataAlign the data align
	 * @return the short
	 */
	protected short cellStyleFromHdrAlign(HdrAlign dataAlign) {
		if (dataAlign == HdrAlign.Left)
			return XSSFCellStyle.ALIGN_LEFT;
		else if (dataAlign == HdrAlign.Center)
			return XSSFCellStyle.ALIGN_CENTER;
		else if (dataAlign == HdrAlign.Right)
			return XSSFCellStyle.ALIGN_RIGHT;
		else
			return XSSFCellStyle.ALIGN_LEFT;
	}

	/**
	 * Cell style from data align.
	 *
	 * @param dataAlign the data align
	 * @return the short
	 */
	protected short cellStyleFromDataAlign(DataAlign dataAlign) {
		if (dataAlign == DataAlign.Left)
			return XSSFCellStyle.ALIGN_LEFT;
		else if (dataAlign == DataAlign.Center)
			return XSSFCellStyle.ALIGN_CENTER;
		else if (dataAlign == DataAlign.Right)
			return XSSFCellStyle.ALIGN_RIGHT;
		else
			return XSSFCellStyle.ALIGN_LEFT;
	}

	/**
	 * Gets the Workbook.
	 *
	 * @return the wb
	 */
	public Workbook getWb() {
		return wb;
	}

}
