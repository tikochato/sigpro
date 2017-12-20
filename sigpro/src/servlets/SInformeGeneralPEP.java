package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.ComponenteDAO;
import dao.DataSigadeDAO;
import dao.PlanEjecucionDAO;
import dao.PrestamoDAO;
import dao.ProyectoDAO;
import pojo.Componente;
import pojo.Prestamo;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.Utils;


@WebServlet("/SInformeGeneralPEP")
public class SInformeGeneralPEP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SInformeGeneralPEP() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		Date fecha_actual = new Date();
		if(accion.equals("getDatosPlan")){
			String lineaBase = map.get("lineaBase");
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
			
			List<?> datosPlan = PlanEjecucionDAO.getDatosPlan(proyectoId,lineaBase);
			BigDecimal plazoEjecucionPlan = new BigDecimal(0);
			BigDecimal plazoEjecucionReal = new BigDecimal(0);
			BigDecimal ejecucionFinancieraReal = new BigDecimal(0);
			BigDecimal ejecucionFinancieraPlan = new BigDecimal(0);;
			BigDecimal ejecucionFisicaReal = new BigDecimal(0);
			BigDecimal ejecucionFisicaPlan = new BigDecimal(0);
			
			for(Object eje : datosPlan){
				Object[] temp = (Object[])eje;
				
				if (((String) temp[0]).equals("Plazo Ejecucion")){
					plazoEjecucionPlan = (BigDecimal) temp[1];
					plazoEjecucionReal = (BigDecimal) temp[2];
				}else if (((String) temp[0]).equals("Ejecucion Financiera")){
					ejecucionFinancieraPlan = (BigDecimal) temp[1];
					ejecucionFinancieraReal = (BigDecimal) temp[2];
				}else if (((String) temp[0]).equals("Ejecucion Fisica")){
					ejecucionFisicaPlan = (BigDecimal) temp[1];
					ejecucionFisicaReal = (BigDecimal) temp[2];
				}
			}
			    
			response_text = String.join("", "{ \"success\": true ,",
					"\"ejecucionFisicaR\": \"" , ejecucionFisicaReal + "" , "\",",
					"\"ejecucionFisicaP\": \"" , ejecucionFisicaPlan + "" , "\",",
					"\"ejecucionFinancieraR\": \"" , ejecucionFinancieraReal + "" , "\",",
					"\"ejecucionFinancieraP\": \"" , ejecucionFinancieraPlan + "" , "\",",
					"\"plazoEjecucionP\": \"" , plazoEjecucionReal + "" , "\",",
					"\"plazoEjecucionR\": \"" , plazoEjecucionPlan + "" , "\",",
					"\"fecha\": \"" , Utils.formatDate(fecha_actual), "\"",
					"}");

				
		}else if(accion.equals("exportarExcel")){
			try{
				int proyectoId = Utils.String2Int(map.get("id"),0);
				Double plazoEjecucionReal = Double.parseDouble(map.get("plazoEjecucionReal") != null ? map.get("plazoEjecucionReal") : "0");
				Double ejecucionFinancieraReal =  Double.parseDouble(map.get("ejecucionFinancieraReal") != null ? map.get("ejecucionFinancieraReal") : "0");
				Double ejecucionFisicaReal =  Double.parseDouble(map.get("ejecucionFisicaReal") != null ? map.get("ejecucionFisicaReal") : "0");
				Double plazoEjecucionPlan = Double.parseDouble(map.get("plazoEjecucionPlan") != null ? map.get("plazoEjecucionPlan") : "0");
				Double ejecucionFinancieraPlan =  Double.parseDouble(map.get("ejecucionFinancieraPlan") != null ? map.get("ejecucionFinancieraPlan") : "0");
				Double ejecucionFisicaPlan =  Double.parseDouble(map.get("ejecucionFisicaPlan") != null ? map.get("ejecucionFisicaPlan") : "0");
				
		        byte [] outArray = exportarExcel(proyectoId, usuario, plazoEjecucionReal,ejecucionFinancieraReal,ejecucionFisicaReal
		        		,plazoEjecucionPlan,ejecucionFinancieraPlan,ejecucionFisicaPlan);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Plan_de_Ejecución.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
			    CLogger.write("1", SPlanEjecucion.class, e);
			}
			
		}else{
			response_text = "{ \"success\": false }";
		}

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
		
	private byte[] exportarExcel(int idPrestamo, String usuario, Double plazoEjecucionReal, Double ejecucionFinancieraReal,
			Double ejecucionFisicaReal ,Double plazoEjecucionPlan, Double ejecucionFinancieraPlan, Double ejecucionFisicaPlan
			) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamo, usuario,plazoEjecucionReal);
			CGraficaExcel grafica = generarGrafica(plazoEjecucionReal,ejecucionFinancieraReal,ejecucionFisicaReal
	        		,plazoEjecucionPlan,ejecucionFinancieraPlan,ejecucionFisicaPlan);
			excel = new CExcel("Plan de Ejecucion", false, grafica);
			wb=excel.generateExcelOfData(datos, "Plan de Ejecución", headers, null, false, usuario);
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
		    CLogger.write("1", SCargaTrabajo.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{" ", " ", " ", " "},  //titulos
			null, //mapeo
			{"stringsinformat", "stringsinformat", "stringsinformat", "stringsinformat"}, //tipo dato
			{"", "", "", ""}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}


	public String[][] generarDatos(int idProyecto, String usuario,Double plazoEjecucion){
		Proyecto proyecto = ProyectoDAO.getProyectoHistory(idProyecto,null);
		Prestamo prestamo = PrestamoDAO.getPrestamoByIdHistory(proyecto.getId(), null);

		Date fecha_actual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String[][] datos = new String[20][4];
		for (int i=0; i<14; i++){
			for (int j=0; j<4; j++){
				datos[i][j] = "";
			}
		}
		
		datos[0][1] = "Mes Reportado";
		datos[0][2] = obtenerMes(Integer.parseInt(sdf.format(fecha_actual)));
		datos[1][1] = "Año Fiscal";
		sdf = new SimpleDateFormat("YYYY");
		datos [1][2] = sdf.format(fecha_actual);
		datos[2][1] = "Proyecto/Programa";
		datos[2][2] = prestamo.getProyectoPrograma();
		datos[3][1] = "Organismo Ejecutor";
		datos[3][2] = proyecto.getUnidadEjecutora()!=null ? proyecto.getUnidadEjecutora().getEntidad().getNombre(): "";
		
		datos[5][0] = "Número del PEP";
		datos[5][1]= prestamo.getNumeroPrestamo();
		datos[5][2] = "Fecha ulimta actualización";
		datos[5][3] = Utils.formatDate(proyecto.getFechaActualizacion());
		
		datos[6][0] = "Código Presupuestario";
		datos[6][1]= prestamo.getCodigoPresupuestario() + "";
		datos[6][2] = "Fecha del decreto";
		datos[6][3] = Utils.formatDate(prestamo.getFechaDecreto());
		
		datos[7][0] = "Organismo Financiero";
		datos[7][1]= prestamo.getCooperante().getNombre();
		datos[7][2] = "Fecha del suscripción";
		datos[7][3] = Utils.formatDate(prestamo.getFechaSuscripcion());
		
		datos[8][0] = "Moneda de préstamo";
		datos[8][1]= prestamo.getTipoMoneda().getNombre();
		datos[8][2] = "Fecha de vigencia";
		datos[8][3] = Utils.formatDate(prestamo.getFechaVigencia());
		
		BigDecimal montoContratadoEntidadUsd = new BigDecimal(0);
		ArrayList<Componente> componentes = (ArrayList<Componente>) ComponenteDAO.getComponentesPorProyectoHistory(proyecto.getId(),null);
		if (componentes != null){
			for (Componente c : componentes)
				montoContratadoEntidadUsd = montoContratadoEntidadUsd.add(c.getFuentePrestamo()!= null ? c.getFuentePrestamo() : new BigDecimal(0));
		}
		
		datos[9][0] = "Monto contratado";
		datos[9][1]=  "$" + montoContratadoEntidadUsd.toString();
		datos[9][2] = "Fecha de elegibilidad";
		datos[9][3] = Utils.formatDate(prestamo.getFechaElegibilidadUe());
		
		
		Date fechaActual = new Date();
		sdf = new SimpleDateFormat("YYYY");
		Long anio = Long.parseLong(sdf.format(fechaActual));
		sdf = new SimpleDateFormat("MM");
		Integer mes = Integer.parseInt(sdf.format(fechaActual));
		
		BigDecimal desembolsadoAFecha = DataSigadeDAO.totalDesembolsadoAFechaRealDolaresPorEntidad(prestamo.getCodigoPresupuestario() + "", 
				anio, mes,proyecto.getUnidadEjecutora().getId().getEntidadentidad());
		
		datos[10][0] = "Desembolsos realizados a la fecha";
		datos[10][1] = desembolsadoAFecha.toString();
		datos[10][2] = "Meses de prorroga";
		datos[10][3] = prestamo.getMesesProrrogaUe()+ "";
		
		Long tiempo1 = 0L;
		sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			tiempo1 = sdf.parse(sdf.format(fechaActual)).getTime();
		} catch (ParseException e) {
			
		}
		Double f1 = (((new Date().getTime()  * 1.0) - tiempo1) - proyecto.getFechaInicio().getTime()) / 86400000;
		Double f2 = (proyecto.getFechaFin().getTime() * 1.0-proyecto.getFechaInicio().getTime()) / 86400000;
		Double plazoEjecucionPEP =  (f1*1.0/f2 * 100);
		
		datos[11][0] = "Monto por desembolsar";
		datos[11][1] = montoContratadoEntidadUsd.subtract(desembolsadoAFecha).toString();
		datos[11][2] = "Plazo ejecución";
		datos[11][3] = String.format("%.2f", plazoEjecucionPEP);	
		return datos;
	}
	
	public String obtenerMes(int mes){
		switch (mes){
			case 1: return "Enero";
			case 2: return "Febrero";
			case 3: return "Marzo";
			case 4: return "Abril";
			case 5: return "Mayo";
			case 6: return "Junio";
			case 7: return "Julio";
			case 8: return "Agosto";
			case 9: return "Septiembre";
			case 10: return "Ocutubre";
			case 11: return "Noviembre";
			case 12: return "Diciembre";
			
		}
		return "";
	}
	
	public CGraficaExcel generarGrafica(Double plazoEjecucionReal, Double ejecucionFinancieraReal,
			Double ejecucionFisicaReal ,Double plazoEjecucionPlan, Double ejecucionFinancieraPlan, Double ejecucionFisicaPlan){
		
		String[][] datos = new String[4][2];
		String[][] datosIgualar = new String[4][2];
		String[] tipoData = new String[]{"String","double","double","double"};
				
		
			datos[0][0] = "Planificada";
			datos[0][1] = "Real";
			
			datos[1][0] = ejecucionFisicaPlan.toString();
			datos[1][1] = ejecucionFisicaReal.toString();
			
			datos[2][0] = plazoEjecucionPlan.toString();
			datos[2][1] = plazoEjecucionReal.toString();
			
			datos[3][0] = ejecucionFinancieraPlan.toString();
			datos[3][1] = ejecucionFinancieraReal.toString();
			
		
		CGraficaExcel grafica = new CGraficaExcel("Plan de Ejecución", CGraficaExcel.EXCEL_CHART_RADAR, " ", "Ejecución Física", datos, tipoData, datosIgualar);
	
		return grafica;
	}

}
