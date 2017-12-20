package utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.apache.shiro.codec.Base64;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class CJasperReport {
	
	public static final String PLANTILLA_PLANANUAL = "/SIPRO/archivos/plantillas/PlanAnualEjecucion.jasper";
	public static final String PLANTILLA_INFORMACIONGENERAL = "/SIPRO/archivos/plantillas/InformacionGeneral.jasper";
	public static final String PLANTILLA_INFORMACIONGENERALCUADRO2 = "/SIPRO/archivos/plantillas/InformacionGeneral_Cuadro2.jasper";
	public static final String PLANTILLA_METAS = "/SIPRO/archivos/plantillas/Metas.jasper";
	public static final String PLANTILLA_DESEMBOLSOS = "/SIPRO/archivos/plantillas/Desembolsos.jasper";
	public static final String PLANTILLA_PLANADQUISICIONES = "/SIPRO/archivos/plantillas/PlanAdquisiciones.jasper";
	public static final String PLANTILLA_EJECUCIONFINANCIERA = "/SIPRO/archivos/plantillas/EjecucionFinanciera.jasper";

	public CJasperReport(){
		
	}
	
	public static byte[] reporteJasper(String pathPlantilla, Map<String, Object> parameters) throws JRException, SQLException{
		byte[] outArray = null;
		File reportFile = new File(pathPlantilla);
		if (!reportFile.exists()){
			CLogger.write_simple("1", CJasperReport.class, "Error al abrir el archivo "+pathPlantilla);
		}
		
		//Map<String, Object> parameters = new HashMap<String, Object>();
	
		if(CMariaDB.connect()){
			Connection conn = CMariaDB.getConnection();
		    JasperPrint jasperPrint = JasperFillManager.fillReport(pathPlantilla, parameters, conn);
		    //JasperExportManager.exportReportToPdfFile(jasperPrint, "/logs/salida.pdf");
		    outArray = Base64.encode(JasperExportManager.exportReportToPdf(jasperPrint));
		    conn.close();
		}
		return outArray;
	}
	
	public static JasperPrint reporteJasperPrint(String pathPlantilla, Map<String, Object> parameters) throws JRException, SQLException{
		JasperPrint jasperPrint = null;
		File reportFile = new File(pathPlantilla);
		if (!reportFile.exists()){
			CLogger.write_simple("2", CJasperReport.class, "Error al abrir el archivo "+pathPlantilla);
		}
			
		if(CMariaDB.connect()){
			Connection conn = CMariaDB.getConnection();
		    jasperPrint = JasperFillManager.fillReport(pathPlantilla, parameters, conn);
		    JasperExportManager.exportReportToPdfFile(jasperPrint, "/logs/salida.pdf");
		    conn.close();
		}
		return jasperPrint;
	}
	
}
