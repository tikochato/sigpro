package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class stentidad {
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
	CellStyle cs_bold_border;
	CellStyle cs_normal;
	CellStyle cs_normal_border;
	CellStyle cs_min;
	Font font;
	Font font_bold;
	Font font_min;
	DataFormat dataformat;
	CellStyle cs_currency_bold;
	CellStyle cs_percent_bold;
	CellStyle cs_estiloVariable;
	boolean HasGroup;
	CGraficaExcel stgrafica;
		
	public CExcel(String sheet_name, boolean hasGroup, CGraficaExcel grafica) {
		 
		HasGroup = hasGroup;
		workbook = new HSSFWorkbook();
		
		if(grafica != null){
			try {
				stgrafica = grafica;
				FileInputStream fileInputStream;
				switch(stgrafica.tipo){
					case CGraficaExcel.EXCEL_CHART_BAR: 
						fileInputStream = new FileInputStream(CGraficaExcel.EXCEL_CHART_BAR_PATH);
						break;
					case CGraficaExcel.EXCEL_CHART_PIE: 
						fileInputStream = new FileInputStream(CGraficaExcel.EXCEL_CHART_PIE_PATH);
						break;
					case CGraficaExcel.EXCEL_CHART_AREA: 
						fileInputStream = new FileInputStream(CGraficaExcel.EXCEL_CHART_AREA_PATH);
						break;
					default: 
						fileInputStream = new FileInputStream(CGraficaExcel.EXCEL_CHART_BAR_PATH);
				}
				workbook = new HSSFWorkbook(fileInputStream);
				sheet = workbook.getSheetAt(0);
			} catch (Exception e) {
				CLogger.write("1", CExcel.class, e);
			}
		}else{
			sheet = workbook.createSheet();
		}
		
		if(sheet_name != null && !sheet_name.isEmpty()){
			workbook.setSheetName(0, sheet_name);
		}
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);

		font_bold = workbook.createFont();
		font_bold.setBold(true);
		font_bold.setFontHeightInPoints((short) 11);
		
		font_min = workbook.createFont();
		font_min.setBold(false);
		font_min.setFontHeightInPoints((short) 8);
		
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
		
		cs_bold_border = workbook.createCellStyle();
		cs_bold_border.setFont(font_bold);
		cs_bold_border.setBorderTop(BorderStyle.THIN);
		cs_bold_border.setBorderLeft(BorderStyle.THIN);
		cs_bold_border.setBorderRight(BorderStyle.THIN);
		cs_bold_border.setBorderBottom(BorderStyle.THIN);

		cs_normal = workbook.createCellStyle();
		cs_normal.setFont(font);
		
		cs_normal_border = workbook.createCellStyle();
		cs_normal_border.setFont(font);
		cs_normal_border.setBorderTop(BorderStyle.THIN);
		cs_normal_border.setBorderLeft(BorderStyle.THIN);
		cs_normal_border.setBorderRight(BorderStyle.THIN);
		cs_normal_border.setBorderBottom(BorderStyle.THIN);
		
		cs_min = workbook.createCellStyle();
		cs_min.setFont(font_min);

		cs_estiloVariable = workbook.createCellStyle();
		
		estilo0();
		estilo1();
		estilo2();
		estilo3();

	}

	public void setCellValueCurrency(double value, int irow, int icell, boolean bold, boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		cs_estiloVariable.cloneStyleFrom(bold ? cs_currency_bold : cs_currency);
		if(borde){
			cs_estiloVariable.setBorderTop(BorderStyle.THIN);
			cs_estiloVariable.setBorderLeft(BorderStyle.THIN);
			cs_estiloVariable.setBorderRight(BorderStyle.THIN);
			cs_estiloVariable.setBorderBottom(BorderStyle.THIN);
		}
		cell.setCellStyle(cs_estiloVariable);
	}

	public void setCellValueDouble(double value, int irow, int icell, boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
				CellStyle estiloCelda =borde ? cs_normal_border : cs_normal;
				cell.setCellStyle(estiloCelda);
		cell.setCellValue(value);
	}

	public void setCellValuePercent(double value, int irow, int icell, boolean bold, boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value / 100.00);
		cs_estiloVariable.cloneStyleFrom(bold ? cs_percent_bold : cs_percent);
		if(borde){
			cs_estiloVariable.setBorderTop(BorderStyle.THIN);
			cs_estiloVariable.setBorderLeft(BorderStyle.THIN);
			cs_estiloVariable.setBorderRight(BorderStyle.THIN);
			cs_estiloVariable.setBorderBottom(BorderStyle.THIN);
		}
		cell.setCellStyle(cs_estiloVariable);
	}

	public void setCellValueInt(int value, int irow, int icell, boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		CellStyle estiloCelda =borde ? cs_normal_border : cs_normal;
		cell.setCellStyle(estiloCelda);
	}

	public void setCellValueString(String value, int irow, int icell, boolean bold, boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		CellStyle estiloCelda =(bold) ? cs_bold : cs_normal;
		if(borde){
			estiloCelda =(bold) ? cs_bold_border : cs_normal_border;
		}
		cell.setCellStyle(estiloCelda);
	}
	
	public void setCellValueString(String value, int irow, int icell, boolean bold, boolean letraMin, boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		cs_estiloVariable.cloneStyleFrom(letraMin ? cs_min : cs_normal);
		if(borde){
			cs_estiloVariable.setBorderTop(BorderStyle.THIN);
			cs_estiloVariable.setBorderLeft(BorderStyle.THIN);
			cs_estiloVariable.setBorderRight(BorderStyle.THIN);
			cs_estiloVariable.setBorderBottom(BorderStyle.THIN);
		}
		cell.setCellStyle(cs_estiloVariable);
	}
	
	public void setCellValueDate(Date value, int irow, int icell, Boolean borde) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellValue(value);
		cs_estiloVariable.cloneStyleFrom(cs_normal);
		if(borde){
			cs_estiloVariable.setBorderTop(BorderStyle.THIN);
			cs_estiloVariable.setBorderLeft(BorderStyle.THIN);
			cs_estiloVariable.setBorderRight(BorderStyle.THIN);
			cs_estiloVariable.setBorderBottom(BorderStyle.THIN);
		}
		cell.setCellStyle(cs_estiloVariable);
	}

	public void setCellFormula(String formula, int irow, int icell, String style, boolean bold) {
		cell = sheet.getRow(irow) != null ? (sheet.getRow(irow).getCell(icell) != null
				? sheet.getRow(irow).getCell(icell) : sheet.getRow(irow).createCell(icell))
				: sheet.createRow(irow).createCell(icell);
		cell.setCellFormula(formula);
		switch (style) {
		case "currency":
			cell.setCellStyle(bold ? cs_currency_bold : cs_currency);
			break;
		case "percent":
			cell.setCellStyle(bold ? cs_percent_bold : cs_percent);
			break;
		default:
			break;
		}
	}
	
	private void Header(String report_name, int celdasCombinadas) {
		setCellValueString("Ministerio de Finanzas Públicas", 0, 0, true, false);
		setCellValueString("SIPRO", 1, 0, true, false);
		setCellValueString(report_name, 4, 0, true, false);
		combineCells(4, 4, 0, celdasCombinadas,false,false);
		combineCells(0, 0, 0, 3,false,false);
		combineCells(1, 1, 0, 3,false,false);
	}
	
	private void Footer(int line, DateTime now, String usuario) {
		line = line + 2;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("d/M/yyyy h:mm a");
		setCellValueString("Fecha de Generación:", line, 0, false, true, false);
		setCellValueString(fmt.print(now),line,1,false, true, false);
		line++;
		setCellValueString("Usuario:", line, 0, false, true, false);
		setCellValueString(usuario,line,1,false, true, false);
	}
	
	private void combineCells(int inicioFila, int finFila, int inicioColumna, int finColumna, boolean border, boolean centrado) {
		if(border || centrado){
			for(int i=inicioFila; i<=finFila;i++){
				for(int j=inicioColumna; j<=finColumna; j++){
					cell = sheet.getRow(i) != null ? (sheet.getRow(i).getCell(j) != null
							? sheet.getRow(i).getCell(j) : sheet.getRow(i).createCell(j))
							: sheet.createRow(i).createCell(j);
					CellStyle estiloCelda = workbook.createCellStyle();
					estiloCelda.setFont(font_bold);
					if(border){
						estiloCelda.setBorderTop(BorderStyle.THIN);
						estiloCelda.setBorderLeft(BorderStyle.THIN);
						estiloCelda.setBorderRight(BorderStyle.THIN);
						estiloCelda.setBorderBottom(BorderStyle.THIN);
					}
					if(centrado){
						estiloCelda.setAlignment(HorizontalAlignment.CENTER);
						estiloCelda.setVerticalAlignment(VerticalAlignment.CENTER);
					}
					cell.setCellStyle(estiloCelda);
				}
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(inicioFila, finFila, inicioColumna, finColumna));
	}

	public Workbook generateExcel(ArrayList<?> data, String report_name, String[][] headers, String[][] extra_lines, String usuario) {
		int line = 5;
		if (extra_lines != null) {
			for (int i = 0; i < extra_lines.length; i++) {
				setCellValueString(extra_lines[i][0], line, 0, true, false);
				setCellValueString(extra_lines[i][1], line, 1, false, false);
				line++;
			}
		}
		line++;
		if (headers.length > 0) {
			for (int i = 0; i < headers[0].length; i++)
				setCellValueString(headers[0][i].toString(), line, i, true, false);
			line++;
			int first_data_line = line + 1;
			int last_data_line = line + 1;
			ArrayList<String> lineas = new ArrayList<String>();
			for (int i = 0; i < data.size(); i++) {			
				Class<?> c = data.get(i).getClass();
				Field f = null;
				try {
					for (int j = 0; j < headers[1].length; j++) {
						f = c.getDeclaredField(headers[1][j].toString());
						f.setAccessible(true);
						switch (headers[2][j]) {
						case "int":
							setCellValueInt(Integer.class.cast(f.get(data.get(i))), line, j, false);
							break;
						case "double":
							setCellValueDouble(Double.parseDouble((f.get(data.get(i))).toString()), line, j, false);
							break;	
						case "currency":
							setCellValueCurrency(Double.parseDouble((f.get(data.get(i))).toString()), line, j, false, false);
							break;
						case "string":
							setCellValueString(String.class.cast(f.get(data.get(i))), line, j, false, false);
							break;
						case "percent":
							setCellValuePercent(Double.parseDouble((f.get(data.get(i))).toString()), line, j, false, false);
						}
					}
					f = c.getDeclaredField("parent");
					f.setAccessible(true);
					if (HasGroup && f.get(data.get(i)) == null) {
						lineas.add((line + 1) + "");
					}
				} catch (Exception e) {
					CLogger.write("7", CExcel.class, e);
				}
				line++;
			}			
			line = setOperations(headers, lineas, line, first_data_line, last_data_line);
		}
		Header(report_name, headers[0].length);
		Footer(line++, DateTime.now(), usuario);
		return workbook;
	}
	
	public Workbook generateExcelOfData(String[][] data, String report_name, String[][] headers, String[][] extra_lines, boolean borde, String usuario) {
		int line = 5;
		DateTime fechaActual = DateTime.now();
		try {
			if(stgrafica != null){
				line = generateChart(report_name, fechaActual, usuario);
			}
			
			if (extra_lines != null) {
				for (int i = 0; i < extra_lines.length; i++) {
					setCellValueString(extra_lines[i][0], line, 0, true, borde);
					
					setCellValueString(extra_lines[i][1], line, 1, false, borde);
					line++;
				}
			}
			line++;
			if (headers.length > 0) {
				for (int i = 0; i < headers[0].length; i++){
					if(headers[0][i].toString().isEmpty()){
						combineCells(line, line, i-1, i, borde,true);
					}else{
						setCellValueString(headers[0][i].toString(), line, i, true, borde);
					}
				}
				if(headers[5] != null){ 
					line++;
					for (int i = 0; i < headers[5].length; i++){
						if(headers[5][i].toString().isEmpty()){
							combineCells(line-1, line, i, i, borde,true);
						}else{
							setCellValueString(headers[5][i].toString(), line, i, true, borde);
						}
					}
				}
				line++;
				int first_data_line = line + 1;
				int last_data_line = line + 1;
				ArrayList<String> lineas = new ArrayList<String>();
				for (int i = 0; i < data.length; i++) {	
						for (int j = 0; j < data[i].length; j++) {
							if(data[i][j]!=null) {
								switch (headers[2][j]) {
								case "int":
										setCellValueInt(Integer.parseInt(data[i][j]), line, j, borde);
									break;
								case "double":
									if(!data[i][j].isEmpty()){
										setCellValueDouble(Double.parseDouble(data[i][j]), line, j, borde);
									}else{
										setCellValueString("", line, j, false, borde);
									}
									break;	
								case "currency":
									setCellValueCurrency(Double.parseDouble(data[i][j]), line, j, false, borde);
									break;
								case "string":
									setCellValueString(String.class.cast(data[i][j]), line, j, false, borde);
									break;
								case "percent":
									if(!data[i][j].isEmpty()){
										setCellValuePercent(Double.parseDouble(data[i][j]), line, j, false, borde);
									}
									break;
								case "date":
									if(!data[i][j].isEmpty()){
										DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
										Date date = format.parse(data[i][j]);
										setCellValueDate(date, line, j, borde);
									}else{
										setCellValueString("", line, j, false, borde);
									}
								}
							}else{
								setCellValueString("", line, j, false, borde);
							}
							if(headers.length>7 && headers[6]!=null && !headers[6][j].isEmpty()){
								setRowOperation(data[i], headers[7][j], headers[6][j], "double", line, j);
							}
						}
						if (HasGroup && data[i] == null) {
							lineas.add((line + 1) + "");
						}
					
					line++;
				}
				line = setOperations(headers, lineas, line, first_data_line, last_data_line);
			}
			Header(report_name, headers[0].length);
			Footer(line++, fechaActual, usuario);
		} catch (Exception e) {
		    CLogger.write("1", CExcel.class, e);
		}
		
		return workbook;
	}
	
	private int setOperations(String[][] headers, ArrayList<String> lineas, int line, int first_data_line, int last_data_line){
		last_data_line = (line > first_data_line) ? line : last_data_line;
		String colname = "";
		if(headers[3]!=null){
			for (int i = 0; i < headers[3].length; i++) {
				switch (headers[3][i]) {
				case "":
					break;
				case "sum":
					colname = CellReference.convertNumToColString(i);
					if (HasGroup)
						setCellFormula(getSumCells(colname, lineas), line, i, headers[2][i], true);
					else
						setCellFormula("sum(" + colname + first_data_line + ":" + colname + last_data_line + ")", line,
								i, headers[2][i], true);
					break;
				case "sub":
					colname = CellReference.convertNumToColString(i);
						setCellFormula("(" + colname + first_data_line + "-" + colname + last_data_line + ")", line,
								i, headers[2][i], true);
					break;
				case "avg":
					colname = CellReference.convertNumToColString(i);
					if (HasGroup)
						setCellFormula("(" + getSumCells(colname, lineas) + ")/" + lineas.size(), line, i,
								headers[2][i], true);
					else
						setCellFormula("average(" + colname + first_data_line + ":" + colname + last_data_line + ")",
								line, i, headers[2][i], true);
					break;
				case "div":
					String[] operandos = headers[4][i].split(",");
					setCellFormula(CellReference.convertNumToColString(getColumnbyName(operandos[0], headers))
							+ (line + 1) + "/"
							+ CellReference.convertNumToColString(getColumnbyName(operandos[1], headers)) + (line + 1),
							line, i, headers[2][i], true);
					break;
				}
			}
			line++;
			for (int colnum = 0; colnum <= sheet.getLastRowNum(); colnum++)
				sheet.autoSizeColumn(colnum);
		}
		return line;
	}
	
	private int setRowOperation(String[] fila, String columnas, String operacion, String style, int line, int row){
		String signoOperacion="";
		String finOperacion="";
		String formula="";
		String[] columna = columnas.split(",");
		switch (operacion) {
		case "":
			break;
		case "sum":
			signoOperacion = "+";
			break;
		case "avg":
			signoOperacion = "+";
			finOperacion = "/"+columna.length;
			break;
		}
		for (int i = 0; i < columna.length; i++) {
			if (!columna[i].isEmpty() && fila[row] != null){
				if(formula.isEmpty()){
					formula = CellReference.convertNumToColString(Integer.parseInt(columna[i])) + (line+1);
				}else{
					formula += signoOperacion + CellReference.convertNumToColString(Integer.parseInt(columna[i])) + (line+1);
				}
			}
		}
		if(!formula.isEmpty()){
			formula=formula+finOperacion;
			setCellFormula(formula, line, row, style, true);
		}
		return line;
	}

	int getColumnbyName(String field, String[][] headers) {
		int ret = 0;
		for (int i = 0; i < headers[1].length; i++) {
			if (headers[1][i].compareTo(field) == 0)
				ret = i;
		}
		return ret;
	}

	String getSumCells(String column, ArrayList<String> lineas) {
		String ret = "";
		for (int i = 0; i < lineas.size(); i++) {
			ret += "+" + column + "" + lineas.get(i);
		}
		return ret.substring(1);
	}
	
	public int generateChart(String report_name, DateTime fechaActual, String usuario){ 
		setCellValueString(stgrafica.leyendaX, 6, 0, true, true);
		setCellValueString(stgrafica.leyendaY, 7, 0, true, true);

		int linea = 6;
		int columna = 1;
		for(int i=0; i<stgrafica.data.length; i++){
			for(int j=0; j<stgrafica.data[i].length; j++){
				switch (stgrafica.tipoData[i]) {
				case "int":
						setCellValueInt(Integer.parseInt(stgrafica.data[i][j]), linea, columna+j, true);
					break;
				case "double":
					setCellValueDouble(Double.parseDouble(stgrafica.data[i][j]), linea, columna+j, true);
					break;	
				case "currency":
					setCellValueCurrency(Double.parseDouble(stgrafica.data[i][j]), linea, columna+j, false, true);
					break;
				case "string":
					setCellValueString(String.class.cast(stgrafica.data[i][j]), linea, columna+j, false, true);
					break;
				case "percent":
					setCellValuePercent(Double.parseDouble(stgrafica.data[i][j]), linea, columna+j, false, true);
				}
				if(stgrafica.igualarCeldas != null && stgrafica.igualarCeldas[i][j]!= null && !stgrafica.igualarCeldas[i][j].isEmpty()){
					String ah = stgrafica.igualarCeldas[i][j];
					String[] celda = ah.split("\\.");
					String formula = CellReference.convertNumToColString(Integer.parseInt(celda[0])) + celda[1];
					setCellFormula(formula, linea, columna+j, stgrafica.tipoData[i], false);
				}
			}
			linea++;
		}

		setCellValueString(report_name, 26, 0, true, false);
		linea = 27;
		
		return linea;
	}

	// nuevo
	HSSFWorkbook workbook_ = new HSSFWorkbook();
	HSSFSheet sheet_;
	CellStyle estilo_0; // normal;
	CellStyle estilo_1; // negrita;
	CellStyle estilo_2; // titulo centrado
	CellStyle estilo_3; // pie
	int rownum;

	public String ExportarExcel2(Map<String, Object[]> datos, String titulo, String usuario,
			String[] encabezadosCombinados, int inicio) {

		sheet_ = workbook_.createSheet(titulo);
		String path = "";

		int contador = inicio;

		Row row = sheet_.createRow(6);

		for (String tamano : encabezadosCombinados) {
			String[] valor = tamano.split(",");
			crearCelda2(valor[0], workbook_, row, contador, 1);
			combinarCeldas(6, 6, contador, contador + (Utils.String2Int(valor[1]) - 1));
			contador += Utils.String2Int(valor[1]);
			sheet_.autoSizeColumn(contador);
			sheet_.autoSizeColumn(contador + 1);
		}

		rownum = 7;
		int columnas = 0;

		for (int i = 0; i < datos.size(); i++) {
			row = sheet_.createRow(rownum++);
			Object[] objArr = datos.get(i + "");
			int cellnum = 0;
			for (Object obj : objArr) {
				crearCelda(obj, workbook_, row, cellnum++, rownum == 8 ? 1 : 0);
			}
			columnas = objArr.length > columnas ? objArr.length : columnas;
		}
		setEncabezado(titulo, columnas);
		piePagina(usuario);
		sheet_.autoSizeColumn(0);
		sheet_.autoSizeColumn(1);
		sheet_.autoSizeColumn(2);
		sheet_.autoSizeColumn(3);

		try {
			path = String.join("", "/archivos/temporales/temp_", ((Long) new Date().getTime()).toString(), ".xls");
			FileOutputStream out = new FileOutputStream(new File(path));
			workbook_.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			CLogger.write("2", CExcel.class, e);
		} catch (Exception e) {
			CLogger.write("3", CExcel.class, e);
		}
		return path;
	}

	public String ExportarExcel(Map<String, Object[]> datos, String titulo, String usuario) {

		sheet_ = workbook_.createSheet(titulo);
		String path = "";

		rownum = 6;
		int columnas = 0;
		
		for (int i = 0; i < datos.size(); i++) {
			Row row = sheet_.createRow(rownum++);
			Object[] objArr = datos.get(i + "");
			int cellnum = 0;
			for (Object obj : objArr) {
				crearCelda(obj, workbook_, row, cellnum++, rownum == 7 ? 1 : 0);
			}
			columnas = objArr.length > columnas ? objArr.length : columnas;
		}
				
		setEncabezado(titulo, columnas);
		piePagina(usuario);
		sheet_.autoSizeColumn(0);
		sheet_.autoSizeColumn(1);
		sheet_.autoSizeColumn(2);
		sheet_.autoSizeColumn(3);
		
		try {
			path = String.join("", "/archivos/temporales/temp_", ((Long) new Date().getTime()).toString(), ".xls");
			FileOutputStream out = new FileOutputStream(new File(path));
			workbook_.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			CLogger.write("4", CExcel.class, e);
		} catch (Exception e) {
			CLogger.write("5", CExcel.class, e);
		}
		return path;
	}

	private Cell crearCelda2(Object value, HSSFWorkbook workbook_, Row row, int cellnum, int estilo) {

		Cell cell = row.createCell(cellnum);
		if (value instanceof String)
			cell.setCellValue((String) value);
		else if (value instanceof Integer)
			cell.setCellValue((Integer) value);

		if (estilo > 0)
			cell.setCellStyle(obtenerEstilo(estilo));

		return cell;
	}

	private Cell crearCelda(Object obj, HSSFWorkbook workbook_, Row row, int cellnum, int estilo) {

		Cell cell = row.createCell(cellnum);

		if (obj instanceof Date) {
			cell.setCellValue((Date) obj);
			HSSFCreationHelper createHelper = workbook_.getCreationHelper();
			HSSFCellStyle cellStyle = workbook_.createCellStyle();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			cell.setCellStyle(cellStyle);

		} else if (obj instanceof Boolean)
			cell.setCellValue((Boolean) obj);
		else if (obj instanceof String)
			cell.setCellValue((String) obj);
		else if (obj instanceof Double)
			cell.setCellValue((Double) obj);
		else if (obj instanceof Integer)
			cell.setCellValue((Integer) obj);

		if (estilo > 0)
			cell.setCellStyle(obtenerEstilo(estilo));

		return cell;
	}

	private CellStyle obtenerEstilo(int estilo) {
		switch (estilo) {
		case 1:
			return estilo_1;
		case 2:
			return estilo_2;
		case 3:
			return estilo_3;
		default:
			return estilo_0;
		}
	}

	private void setEncabezado(String titulo, int celdasCombinadas) {
		setCeldaString("Ministerio de Finanzas Publicas", 0, 0, true, 1);
		setCeldaString("Proyecto SIPRO", 1, 0, true, 1);
		setCeldaString(titulo, 4, 0, true, 2);
		combinarCeldas(4, 4, 0, celdasCombinadas);
		combinarCeldas(0, 0, 0, 3);
		combinarCeldas(1, 1, 0, 3);
	}

	public void setCeldaString(String value, int rownum, int cellnum, boolean negrita, int estilo) {
		Row row = sheet_.createRow(rownum);
		Cell cell = row.createCell(cellnum);

		cell = sheet_.getRow(rownum) != null ? (sheet_.getRow(rownum).getCell(cellnum) != null
				? sheet_.getRow(rownum).getCell(cellnum) : sheet_.getRow(rownum).createCell(cellnum))
				: sheet_.createRow(rownum).createCell(cellnum);
		cell.setCellValue(value);
		cell.setCellStyle(obtenerEstilo(estilo));
	}

	private void estilo0() {
		estilo_0 = workbook_.createCellStyle();

	}

	private void estilo1() {
		estilo_1 = workbook_.createCellStyle();
		HSSFFont font = workbook_.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 11);
		estilo_1.setFont(font);
	}

	private void estilo2() {
		estilo_2 = workbook_.createCellStyle();
		HSSFFont font = workbook_.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		estilo_2.setFont(font);

	}

	private void estilo3() {
		estilo_3 = workbook_.createCellStyle();
		HSSFFont font = workbook_.createFont();
		font.setBold(false);
		font.setFontHeightInPoints((short) 8);
		estilo_3.setFont(font);
	}

	public void combinarCeldas(int inicioFila, int finFila, int inicioColumna, int finColumna) {
		sheet_.addMergedRegion(new CellRangeAddress(inicioFila, finFila, inicioColumna, finColumna));
	}
	
	private void piePagina(String usuario) {
		rownum = rownum + 2;
		DateTime now = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("d/M/yyyy h:mm a");
		setCeldaString("Fecha de Generación: " + fmt.print(now), rownum, 0, true, 3);
		combinarCeldas(rownum, rownum, 0, 3);
		rownum++;
		setCeldaString("Usuario: " + usuario, rownum, 0, true, 4);
		combinarCeldas(rownum, rownum, 0, 3);
	}
}
