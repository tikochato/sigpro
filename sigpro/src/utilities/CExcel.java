package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class stentidad{
	Integer parent;
	int entidad;
	String nombre;
	double ano1;
	double ano2;
	double ano3;
	double ano4;
	double ano5;
	double solicitado;
	double solicitado_acumulado;
	double ejecutado;
	double ejecutado_acumulado;
	double vigente;
	double ejecucion_anual;
	double aprobacion_anual;
	int icono_ejecucion_anual;
}

public class CExcel {	
	Workbook workbook;
	Sheet sheet;
	Row row;
	Cell cell;
	CellStyle cs_currency;
	CellStyle cs_percent;
	CellStyle cs_bold;
	CellStyle cs_normal;
	Font font;
	Font font_bold;
	DataFormat dataformat;
	CellStyle cs_currency_bold;
	CellStyle cs_percent_bold;
	boolean HasGroup;
	
	public CExcel(String sheet_name,boolean hasGroup){
		HasGroup = hasGroup;
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet();
		if(sheet_name!=null)
			workbook.setSheetName(0, sheet_name);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		
		font_bold = workbook.createFont();
		font_bold.setBold(true);
		font_bold.setFontHeightInPoints((short)12);
		
		cs_currency = workbook.createCellStyle();
		cs_currency.setFont(font);
		cs_percent = workbook.createCellStyle();
		cs_percent.setFont(font);
		dataformat = workbook.createDataFormat();
		cs_currency.setDataFormat(dataformat.getFormat("Q #,##0.00"));
		cs_percent.setDataFormat(dataformat.getFormat("0.00%"));
		
		cs_currency_bold = workbook.createCellStyle();
		cs_currency_bold.setFont(font_bold);
		cs_percent_bold = workbook.createCellStyle();
		cs_percent_bold.setFont(font_bold);
		cs_currency_bold.setDataFormat(dataformat.getFormat("Q #,##0.00"));
		cs_percent_bold.setDataFormat(dataformat.getFormat("0.00%"));
		
		cs_bold = workbook.createCellStyle();
		cs_bold.setFont(font_bold);
		
		cs_normal = workbook.createCellStyle();
		cs_normal.setFont(font);
		
		estilo0();
		estilo1();
		estilo2();
		estilo3();
		
	}
	
	public void setCellValueCurrency(double value, int irow, int icell, boolean bold){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		cell.setCellStyle(bold ? cs_currency_bold : cs_currency);
	}
	
	public void setCellValueDouble(double value, int irow, int icell){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
	}
	
	public void setCellValuePercent(double value, int irow, int icell, boolean bold){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value/100.00);
		cell.setCellStyle(bold ? cs_percent_bold: cs_percent);
	}
	
	public void setCellValueInt(int value, int irow, int icell){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		cell.setCellStyle(cs_normal);
	}
	
	public void setCellValueString(String value, int irow, int icell, boolean bold){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		cell.setCellStyle((bold) ? cs_bold : cs_normal);
	}
	
	public void setCellValueDate(Date value, int irow, int icell){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
	}
	
	public void setCellFormula(String formula, int irow, int icell,String style, boolean bold){
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell)!=null ? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell)) : sheet.createRow(irow).createCell(icell);
		//cell.setCellType(Cell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
		switch(style){
			case "currency": cell.setCellStyle(bold ?  cs_currency_bold : cs_currency); break;
			case "percent": cell.setCellStyle(bold ? cs_percent_bold : cs_percent); break;
			default: cell.setCellStyle(cs_normal);
		}
	}
	
	private void Header(String report_name){
		setCellValueString("Ministerio de Finanzas Públicas", 0, 0, true);
		setCellValueString("SIAF",1,0, true);
		setCellValueString("Tableros de Información",2,0, true);
		setCellValueString(report_name, 4,0, true);
	}
	
	private void Footer(int line,DateTime now){
		DateTimeFormatter fmt = DateTimeFormat.forPattern("d/M/yyyy h:mm a");
		setCellValueString("Fecha de Generación",line+1,0,true);
		setCellValueString(fmt.print(now),line+1,1,false);
	}
	
	public Workbook generateExcel(ArrayList<?> data, String report_name, String[][] headers,int ejercicio, String[][] extra_lines){
		int line = 5;
		if(extra_lines!=null){
			for(int i=0; i<extra_lines.length; i++){
				setCellValueString(extra_lines[i][0],line,0, true);
				setCellValueString(extra_lines[i][1],line,1,false);
				line++;
			}
		}
		line++;
		if(headers.length>0){
			for(int i=0;i<headers[0].length; i++)
				setCellValueString(headers[0][i].toString(),line,i, true);
			line++;
			int first_data_line=line+1;
			int last_data_line=line+1;
			ArrayList<String> lineas = new ArrayList<String>();			
				for(int i=0; i<data.size(); i++){
					Class<?> c = data.get(i).getClass();
					Field f = null;
					try {
						for(int j=0; j<headers[1].length; j++){
						    f = c.getDeclaredField(headers[1][j].toString());
							f.setAccessible(true);
							switch(headers[2][j]){
								case "int": setCellValueInt(Integer.class.cast(f.get(data.get(i))), line, j); break;
								case "currency": setCellValueCurrency(Double.class.cast(f.get(data.get(i))), line, j, false); break;
								case "string": setCellValueString(String.class.cast(f.get(data.get(i))), line, j, false); break;
								case "percent": setCellValuePercent(Double.class.cast(f.get(data.get(i))), line, j, false); break;
							}						
							
						}
						f=c.getDeclaredField("parent");
						f.setAccessible(true);
						if (HasGroup && f.get(data.get(i))==null){
							lineas.add((line+1)+"");
						}
					} catch (Exception e) {
					}
					line++;
				}
			
			last_data_line=(line>first_data_line) ? line : last_data_line;
			String colname="";
			for(int i=0; i<headers[3].length;i++){
				switch(headers[3][i]){
					case "": break;
					case "sum": 
						colname = CellReference.convertNumToColString(i);
						if (HasGroup)
							setCellFormula(getSumCells(colname, lineas), line, i, headers[2][i], true); 
						else
							setCellFormula("sum("+colname+first_data_line+":"+colname+last_data_line+")", line, i, headers[2][i], true); 
						break;
					case "avg":
						colname = CellReference.convertNumToColString(i);
						if (HasGroup)
							setCellFormula("("+getSumCells(colname, lineas)+")/"+lineas.size(), line, i, headers[2][i], true); 
						else
							setCellFormula("average("+colname+first_data_line+":"+colname+last_data_line+")", line, i, headers[2][i], true); 
						break;	
					case "div":
						String[] operandos = headers[4][i].split(",");
						setCellFormula(
								CellReference.convertNumToColString(getColumnbyName(operandos[0], headers)) + (line+1) + "/" +
								CellReference.convertNumToColString(getColumnbyName(operandos[1], headers)) + (line+1)
										, line, i, headers[2][i], true);
						break;
				}
			}
			line++;
			for (int colnum = 0; colnum <= sheet.getLastRowNum(); colnum++)
				sheet.autoSizeColumn(colnum);
		}
		Header(report_name);
		Footer(line++,DateTime.now());
		return workbook;
	}
	
	int getColumnbyName(String field,String[][] headers){
		int ret = 0;
		for (int i=0; i < headers[1].length; i++){
			if (headers[1][i].compareTo(field)==0)
				ret=i;
		}
		return ret;
	}
	
	String getSumCells(String column,ArrayList<String> lineas){
		String ret="";
		for (int i = 0; i<lineas.size(); i++){
			ret +="+" +column + "" + lineas.get(i) ; 
		}
		return ret.substring(1);
	}
	//nuevo
	HSSFWorkbook workbook_ = new HSSFWorkbook();
	HSSFSheet sheet_;
	CellStyle estilo_0; //normal;
	CellStyle estilo_1; //negrita;
	CellStyle estilo_2; // titulo centrado
	CellStyle estilo_3; // pie
	int rownum;
	
	public String ExportarExcel(Map<String,Object[]> datos,String titulo){
		
		sheet_ = workbook_.createSheet(titulo);
		String path="";
		
		rownum = 6;
		int columnas=0;
		
		for (int i = 0 ;i< datos.size(); i++ ) {
			Row row = sheet_.createRow(rownum++);
			Object [] objArr = datos.get(i+"");
			int cellnum = 0;
			for (Object obj : objArr) {
				crearCelda(obj, workbook_, row, cellnum++, rownum==7?1:0);
			}
			columnas = objArr.length > columnas? objArr.length : columnas;
		}
		setEncabezado(titulo,columnas);
		piePagina();
		sheet_.autoSizeColumn(0);
		sheet_.autoSizeColumn(1);
		sheet_.autoSizeColumn(2);
		sheet_.autoSizeColumn(3);

		try {
			path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".xls");
			FileOutputStream out = 
					new FileOutputStream(new File(path));
			workbook_.write(out);
			out.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	
	private Cell crearCelda(Object obj, HSSFWorkbook workbook_,Row row,int cellnum,int estilo){
		
		Cell cell = row.createCell(cellnum);
		
		if(obj instanceof Date) {
			cell.setCellValue((Date)obj);
			HSSFCreationHelper createHelper = workbook_.getCreationHelper();
			HSSFCellStyle cellStyle         = workbook_.createCellStyle();
			cellStyle.setDataFormat(
			createHelper.createDataFormat().getFormat("dd/MM/yyyy")); 
			cell.setCellStyle(cellStyle);

		}
		else if(obj instanceof Boolean)
			cell.setCellValue((Boolean)obj);
		else if(obj instanceof String)
			cell.setCellValue((String)obj);
		else if(obj instanceof Double)
			cell.setCellValue((Double)obj);
		else if (obj instanceof Integer ) 
			cell.setCellValue((Integer) obj);
		
		
		if (estilo>0)
			cell.setCellStyle(obtenerEstilo( estilo));
		
		
		return cell;
	}
	
	private CellStyle obtenerEstilo(int estilo){
		switch (estilo){
			case 1: return estilo_1; 
			case 2: return estilo_2;
			case 3: return estilo_3;
			default: return estilo_0;
		}
	}

	
	private void setEncabezado(String titulo,int celdasCombinadas){
		setCeldaString( "Ministerio de Finanzas Publicas", 0, 0,  true,1);
		setCeldaString( "Proyecto SIPRO", 1, 0,  true,1);
		setCeldaString(titulo, 4,0, true,2);
		conbinarCeldas(4, 4, 0, celdasCombinadas);
		conbinarCeldas(0, 0, 0, 3);
		conbinarCeldas(1, 1, 0, 3);
		
		
	}
	public void setCeldaString(String value, int rownum, int cellnum,
			boolean negrita,int estilo){
		Row row = sheet_.createRow(rownum);
		Cell cell = row.createCell(cellnum);
		
		cell = sheet_.getRow(rownum) != null ? (sheet_.getRow(rownum).getCell(cellnum)!=null ? 
				sheet_.getRow(rownum).getCell(cellnum) : sheet_.getRow(rownum).createCell(cellnum)) : sheet_.createRow(rownum).createCell(cellnum);
		cell.setCellValue(value);
		cell.setCellStyle(obtenerEstilo(estilo));
	}
	
	
	private void estilo0(){
		estilo_0 =workbook_.createCellStyle();
		
	}
	
	private void estilo1(){
		estilo_1 =workbook_.createCellStyle();
		HSSFFont font = workbook_.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)11);
		estilo_1.setFont(font);
	}
	
	private void estilo2(){
		estilo_2 =workbook_.createCellStyle();
		HSSFFont font = workbook_.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)12);
		estilo_2.setFont(font);
		
	}
	
	private void estilo3(){
		estilo_3 =workbook_.createCellStyle();
		HSSFFont font = workbook_.createFont();
		font.setBold(false);
		font.setFontHeightInPoints((short)8);
		estilo_3.setFont(font);
	}
	
	public void conbinarCeldas(int inicioFila,int finFila, int inicioColumna,int finColumna){
		sheet_.addMergedRegion(new CellRangeAddress(inicioFila,finFila,inicioColumna,finColumna));
	}
	
	private void piePagina(){
		rownum=rownum+2;
		DateTime now = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("d/M/yyyy h:mm a");
		setCeldaString("Fecha de Generación: " +fmt.print(now) ,rownum,0,true,3);
		conbinarCeldas(rownum, rownum, 0, 3);
		rownum++;
		setCeldaString("Usuario: " +"admin" ,rownum,0,true,4);
		conbinarCeldas(rownum, rownum, 0, 3);	
	}
}
