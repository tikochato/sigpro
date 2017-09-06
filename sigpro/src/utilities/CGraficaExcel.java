package utilities;

public class CGraficaExcel {
	String titulo;
	int tipo;
	String leyendaX;
	String leyendaY;
	String[][] data;
	String [] tipoData;
	String[][] igualarCeldas;
		
	public static final String EXCEL_CHART_BAR_PATH = "/archivos/plantillas/TemplateChartBar.xls";
	public static final String EXCEL_CHART_PIE_PATH = "/archivos/plantillas/TemplateChartPie.xls";
	public static final String EXCEL_CHART_AREA_PATH = "/archivos/plantillas/TemplateChartArea.xls";
	public static final int EXCEL_CHART_BAR = 1;
	public static final int EXCEL_CHART_PIE = 2;
	public static final int EXCEL_CHART_AREA = 3;
	public static final String EXCEL_SHEET_BAR= "Reporte";
	public static final String EXCEL_SHEET_PIE = "GraficoPie";
	public static final String EXCEL_SHEET_AREA = "GraficoArea";
	
	public CGraficaExcel(String titulo, int tipo, String leyendaX, String leyendaY, String[][] data, String[] tipoData, String[][] igualarCeldas) {
		this.titulo = titulo;
		this.tipo = tipo;
		this.leyendaX = leyendaX;
		this.leyendaY = leyendaY;
		this.data = data;
		this.tipoData = tipoData;
		this.igualarCeldas = igualarCeldas;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getLeyendaX() {
		return leyendaX;
	}
	public void setLeyendaX(String leyendaX) {
		this.leyendaX = leyendaX;
	}
	public String getLeyendaY() {
		return leyendaY;
	}
	public void setLeyendaY(String leyendaY) {
		this.leyendaY = leyendaY;
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = data;
	}
	public String[] getTipoData() {
		return tipoData;
	}
	public void setTipoData(String[] tipoData) {
		this.tipoData = tipoData;
	}
	public String[][] getIgualarCeldas() {
		return igualarCeldas;
	}
	public void setIgualarCeldas(String[][] igualarCeldas) {
		this.igualarCeldas = igualarCeldas;
	}
	
}
