package utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
		cell.setCellType(Cell.CELL_TYPE_FORMULA);
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
}
