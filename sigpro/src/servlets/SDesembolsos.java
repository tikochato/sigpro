package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.DataSigadeDAO;
import dao.DesembolsoDAO;
import dao.ObjetoCosto;
import dao.ObjetoDAO;
import dao.ProyectoDAO;
import pojo.Prestamo;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;


@WebServlet("/SDesembolsos")
public class SDesembolsos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;
	
	class stanio{
		Integer anio;
		BigDecimal[] mesPlanificado = new BigDecimal[12];
		BigDecimal[] mesReal = new BigDecimal[12];
		BigDecimal totalPlanificado;
		BigDecimal totalReal;
	}
	
	class stdedembolsos{
		
	}
	
    public SDesembolsos() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{\"success\": false }").append(request.getContextPath());
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
		if(accion.equals("getDesembolsos")){
			Integer anio_inicial = Utils.String2Int(map.get("anio_inicial"));
			Integer anio_final = Utils.String2Int(map.get("anio_final"));
			String lineaBase = map.get("lineaBase");
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"));
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			Prestamo prestamo = proyecto.getPrestamo();
			
			if (prestamo!=null){
			
				List<?> objDesembolso =DesembolsoDAO.getDesembolsosPorEjercicio(proyectoId,anio_inicial,anio_final,lineaBase);
				
				String planificado="";
				
				
				for (int i = anio_inicial ; i<=anio_final ; i++){
					for (int j = 1; j<=12 ; j++){
						Integer anio = objDesembolso.size() > 0 ?  (Integer) ((Object[]) objDesembolso.get(0))[0] : 0;
						Integer mes = objDesembolso.size() > 0 ?  (Integer) ((Object[]) objDesembolso.get(0))[1] : 0;
						if (anio.compareTo(i) == 0 && mes.compareTo(j)==0){
							BigDecimal valor = (BigDecimal) ((Object[]) objDesembolso.get(0))[2];
							objDesembolso.remove(objDesembolso.get(0));
							planificado = planificado + (planificado.length()>0 ? "," :"") +  
									 valor.toString();
						}else{
							planificado = planificado + (planificado.length()>0 ? "," :"") +  
									 "0";
						}
					}
				}
				
				List<?> dtmAvance = DataSigadeDAO.getAVANCE_FISFINAN_DET_DTIRango( 
						prestamo.getCodigoPresupuestario()+"",anio_inicial,anio_final,1, proyecto.getUnidadEjecutora().getId().getEntidadentidad(), proyecto.getUnidadEjecutora().getId().getUnidadEjecutora());
				
				String realQ="";
				for (int i = anio_inicial ; i<=anio_final ; i++){
					for (int j = 1; j<=12 ; j++){
						Integer anio = dtmAvance.size() > 0 ?  (  ((Long) ((Object[]) dtmAvance.get(0))[0]).intValue()) : 0;
						Integer mes = dtmAvance.size() > 0 ?  Integer.parseInt((String) ((Object[]) dtmAvance.get(0))[1]) : 0;
						if (anio.compareTo(i) == 0 && mes.compareTo(j)==0){
							BigDecimal valor = (BigDecimal) ((Object[]) dtmAvance.get(0))[2];
							dtmAvance.remove(dtmAvance.get(0));
							realQ = realQ + (realQ.length()>0 ? "," :"") +  
									 valor.toString();
						}else{
							realQ = realQ + (realQ.length()>0 ? "," :"") +  
									 "0";
						}
					}
				}
				
				List<?> dtmAvanceD = DataSigadeDAO.getAVANCE_FISFINAN_DET_DTIRango( 
						prestamo.getCodigoPresupuestario()+"",anio_inicial,anio_final,2, proyecto.getUnidadEjecutora().getId().getEntidadentidad(), proyecto.getUnidadEjecutora().getId().getUnidadEjecutora());
				
				String realD="";
				for (int i = anio_inicial ; i<=anio_final ; i++){
					for (int j = 1; j<=12 ; j++){
						Integer anio = dtmAvanceD.size() > 0 ?  (  ((Long) ((Object[]) dtmAvanceD.get(0))[0]).intValue()) : 0;
						Integer mes = dtmAvanceD.size() > 0 ?  Integer.parseInt((String) ((Object[]) dtmAvanceD.get(0))[1]) : 0;
						if (anio.compareTo(i) == 0 && mes.compareTo(j)==0){
							BigDecimal valor = (BigDecimal) ((Object[]) dtmAvanceD.get(0))[2];
							dtmAvanceD.remove(dtmAvanceD.get(0));
							realD = realD + (realD.length()>0 ? "," :"") +  
									 valor.toString();
						}else{
							realD = realD + (realD.length()>0 ? "," :"") +  
									 "0";
						}
					}
				}
				List<ObjetoCosto> costos = new ArrayList<>();
				
				try {
					costos = ObjetoDAO.getEstructuraConCosto(proyectoId, anio_inicial, anio_final,true, false, false, lineaBase, usuario);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				 
				String lista_costo="";
				int contanio = 0;
				for (int i = anio_inicial ; i<=anio_final ; i++){
					if(costos!=null){
						dao.ObjetoCosto.stanio[] stanio_= costos.get(0).getAnios();
						for (int j = 0; j<12 ; j++){
							    BigDecimal valor = stanio_[contanio].mes[j].planificado;
								lista_costo = lista_costo + (lista_costo.length()>0 ? "," :"") +  
										 (valor!= null ? valor.toString() : "0");
						}
						contanio++;
					}else{
						lista_costo = lista_costo + (lista_costo.length()>0 ? "," :"") +  
								 "0,0,0,0,0,0,0,0,0,0,0,0";
					}
				}
				
				planificado = String.join("", "[",planificado,"]");    
				lista_costo = String.join("", "[",lista_costo,"]");
				realQ = String.join("", "[",realQ,"]");
				realD = String.join("", "[",realD,"]");
				
			    response_text = String.join("","{ \"success\": true, \"planificado\": ",planificado,
			    		","," \"real\":",realQ,
			    		","," \"costos\":",lista_costo,
			    		","," \"reald\":",realD,"}");
			    
			}else{
				response_text = "{ \"success\": false }";
			}
		}else if (accion.equals("exportarExcel")){
			
			Integer agrupacion = Utils.String2Int(map.get("agrupacion"));
			String real = map.get("real");
			String realDolares = map.get("realdolares");
			String variacion = map.get("variacion"); 
			String porcentaje = map.get("porcentaje"); 
			String headers_ = map.get("headers");
			String planificado = map.get("planificado");
			String costo = map.get("costo");
			
			
			try{
		        byte [] outArray = exportarExcel(costo, planificado, real, realDolares, variacion, porcentaje, headers_, agrupacion, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Desembolsos.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SDesembolsos.class, e);
			}
		}else if(accion.equals("exportarPdf")){
			
			String real = map.get("real");
			String realDolares = map.get("realdolares");
			String variacion = map.get("variacion"); 
			String porcentaje = map.get("porcentaje"); 
			String headers_ = map.get("headers");
			String planificado = map.get("planificado");
			String costo = map.get("costo");
			
			CPdf archivo = new CPdf("Desembolsos");
			String headers[][];
			String datos[][];
			headers = generarHeaders(headers_);
			datos = generarDatos(costo, planificado, real, realDolares, variacion, porcentaje, headers, usuario);
			String path = archivo.exportarDesembolsos(headers, datos,usuario);
			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
					CLogger.write("5", SDesembolsos.class, e);
		        }
		        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		        
		        int readByte = 0;
		        byte[] buffer = new byte[2024];

                while(true)
                {
                    readByte = is.read(buffer);
                    if(readByte == -1)
                    {
                        break;
                    }
                    outByteStream.write(buffer);
                }
                
                file.delete();
                
                is.close();
                outByteStream.flush();
                outByteStream.close();
                
		        byte [] outArray = Base64.encode(outByteStream.toByteArray());
				response.setContentType("application/pdf");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "in-line; 'Desembolsos.pdf'");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		else{
			response_text = "{ \"success\": false }";
		}
		
		if(!accion.equals("exportarExcel") && !accion.equals("exportarPdf")){
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}
	}
		
	private byte[] exportarExcel(String costo, String planificado, String real, String realDolares, String variacion,
			String porcentaje, String headers_, int agrupacion, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders(headers_);
			datos = generarDatos(costo, planificado, real, realDolares, variacion, porcentaje, headers, usuario);
			
			CGraficaExcel grafica = generarGrafica(datos, headers);
			excel = new CExcel("Desembolsos", false, grafica);
			wb=excel.generateExcelOfData(datos, "Desembolsos", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("2", SDesembolsos.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(String headers_){
		String headers[][];
		
		String titulos[] = headers_.replace("[", "").
				replace("]", "").replace("\"", "").split(",");
		
		
		 
		int columnasTotal = titulos.length;
				
		
				
		String titulo[] = new String[columnasTotal];
		String tipo[] = new String[columnasTotal];
		String columnasOperacion[] = new String[columnasTotal];
		String filasOperacion[] = new String[columnasTotal];
		String filasOperadores[]= new String[columnasTotal];
		titulo[0]="Tipo";
		tipo[0]="string";
		columnasOperacion[0]="";
		filasOperacion[0]="";
		filasOperadores[0]="";
		String filasOperadas = "";
		for (int i = 1; i< columnasTotal ; i++){
			tipo[i] = "double" ;
			columnasOperacion[i]="";
			
			if (i != (columnasTotal -1)){
				filasOperacion[i]="";
				filasOperadores[i]="";
			}else{
				filasOperacion[i]="";
				filasOperadores[i]="";
			}
			filasOperadas = filasOperadas + (filasOperadas.length()> 0 ? "," : "") + i;
		}
		
		
		
		
		
		headers = new String[][]{
			titulos,  //titulos
			null, //mapeo
			tipo, //tipo dato
			columnasOperacion, //operaciones columnas
			null, //operaciones div
			null,
			filasOperacion,
			filasOperadores
			};
			
		return headers;
	}
	
	public String[][] generarDatos(String costo, String planificado, String real, String realDolares, 
			String variacion, String porcentaje, String[][] headers, String usuario){
		
		String[][] datos = new String[6][headers[0].length];
		
		for (int i=0; i<6; i++){
			for (int j=0; j<headers[0].length; j++){
				datos[i][j] = "0";
			}
		}
		
		datos[0] = costo.replace("[","").replace("]", "").replace("\"", "").split(",");
		datos[1] = planificado.replace("[","").replace("]", "").replace("\"", "").split(",");
		datos[2] = real.replace("[","").replace("]", "").replace("\"", "").split(",");
		datos[3] = realDolares.replace("[","").replace("]", "").replace("\"", "").split(",");
		datos[4] = variacion.replace("[","").replace("]", "").replace("\"", "").split(",");
		datos[5] = porcentaje.replace("[","").replace("]", "").replace("\"", "").replace("%", "").split(",");
			
		       
		return datos;
	}
	
	public CGraficaExcel generarGrafica(String[][] datosTabla, String[][] headers){
		
		String[][] datos = new String[3][datosTabla[0].length-2];
		String[][] datosIgualar = new String[3][datosTabla[0].length-2];
		String[] tipoData = new String[]{"string","currency","currency"};
				
		for(int c=1; c<datosTabla[0].length-1; c++){
			datos[0][c-1] = headers[0][c];
			datos[1][c-1] = datosTabla[0][c];
			datos[2][c-1] = datosTabla[1][c];
			datosIgualar[0][c-1]="";
			datosIgualar[1][c-1]=(c)+"."+(datosTabla.length-2+27);
			datosIgualar[2][c-1]=(c)+"."+(datosTabla.length-1+27);
			
		}
		
		CGraficaExcel grafica = new CGraficaExcel("Desembolsos", CGraficaExcel.EXCEL_CHART_AREA, "Meses", "Planificado", datos, tipoData, datosIgualar);
	
		return grafica;
	}

}
