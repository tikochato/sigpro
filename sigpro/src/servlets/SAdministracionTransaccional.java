package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.AdministracionTransaccionalDAO;
import dao.ColaboradorDAO;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SAdministracionTransaccional")
public class SAdministracionTransaccional extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class sttransaccion{
		Integer objeto_id;
		String nombre;
		Integer objeto_tipo;
		String nombre_objeto_tipo;
		String usuario_creo;
		String usuario_actualizo;
		String fecha_creacion;
		String fecha_modificacion;
		String estado;
	}
	
	class stusuario{
		String usuario;
		BigDecimal creados;
		BigDecimal actualizados;
		BigDecimal eliminados;
	}
	
	class stusuariodetalle{
		String usuario;
		String objetoNombre;
		String objetoTipo;
		String estado;
		String fecha;
	}
	
	class stanio{
		BigDecimal[] mes = new BigDecimal[12];
		Integer anio;
		
	}
	
	class stagrupacion{
		stanio[] anios;
	}

	Integer totalCreados = 0;
	Integer totalActualizados = 0;
	Integer totalEliminados = 0;
       
    public SAdministracionTransaccional() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		if(accion.equals("getTransacciones")){
			Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
			Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
			
			List<stusuario> lstusuarios = getAdministracionTransaccional(fechaInicio, fechaFin);
			
			List<stagrupacion> lstcreados = getTransaccionesCreadas(fechaInicio, fechaFin);
			List<stagrupacion> lstactualizados = getTransaccionesActualizadas(fechaInicio, fechaFin);
			List<stagrupacion> lsteliminados = getTransaccionesEliminadas(fechaInicio, fechaFin);
			
			response_text= new GsonBuilder().serializeNulls().create().toJson(lstusuarios);
	        response_text = String.join("", "\"usuarios\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text);
	        
	        String transacciones_text = new GsonBuilder().serializeNulls().create().toJson(lstcreados);
	        transacciones_text = String.join("", ",\"creados\":", transacciones_text);
	        response_text = String.join("",response_text, transacciones_text);
	        
	        transacciones_text = new GsonBuilder().serializeNulls().create().toJson(lstactualizados);
	        transacciones_text = String.join("", ",\"actualizados\":",transacciones_text);
	        response_text = String.join("",response_text, transacciones_text);
	        
	        transacciones_text = new GsonBuilder().serializeNulls().create().toJson(lsteliminados);
	        transacciones_text = String.join("", ",\"eliminados\":",transacciones_text);
	        response_text = String.join("",response_text, transacciones_text, "}");
	        
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}else if(accion.equals("exportarExcelDetalle")){
			String usuarioDetalle = map.get("usuarioDetalle");
			Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
			Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
			
			byte[] outArray = exportarExcelDetalle(usuarioDetalle, fechaInicio, fechaFin);
			
			response.setContentType("application/ms-excel");
			response.setContentLength(outArray.length);
			response.setHeader("Cache-Control", "no-cache"); 
			response.setHeader("Content-Disposition", "attachment; Administracion_Transaccional.xls");
			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
		}else if (accion.equals("exportarExcel")){
			try{
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
		        byte [] outArray = exportarExcel(usuario, fechaInicio, fechaFin);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Administracion_Transaccional.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SAdministracionTransaccional.class, e);
			}
		}else if(accion.equals("exportarPdfDetalle")){
			try{
				String usuarioDetalle = map.get("usuarioDetalle");
				String colaborador = ColaboradorDAO.getColaboradorByUsuario(usuarioDetalle);
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
				CPdf archivo = new CPdf("Detalle Administración Transaccional del " + (!colaborador.equals("") ? "colaborador " : "usuario ") + colaborador + "("+usuarioDetalle+")" + "\"");
				String headers[][];
				String datos[][];
				headers = generarHeadersDetalle();
				datos = generarDatosDetalle(usuarioDetalle, fechaInicio, fechaFin);
				String path = archivo.ExportarPdfAdministracionTransaccionalDetalle(headers, datos,usuario);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("5", SAdministracionTransaccional.class, e);
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
					response.setHeader("Content-Disposition", "in-line; 'AdministracionTransaccional.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
			}catch(Exception e){
				CLogger.write("2", SAdministracionTransaccional.class, e);
			}
		}else if(accion.equals("exportarPdf")){
			Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
			Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
			CPdf archivo = new CPdf("Administracion Transaccional");
			String headers[][];
			String datos[][];
			headers = generarHeaders();
			datos = generarDatos(fechaInicio, fechaFin);
			String path = archivo.ExportarPdfAdministracionTransaccional(headers, datos,usuario);
			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
					CLogger.write("5", SAdministracionTransaccional.class, e);
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
				response.setHeader("Content-Disposition", "in-line; 'AdministracionTransaccional.pdf'");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		else{
			response_text = "{ \"success\": false }";
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}		
	}
	
	private List<stusuario> getAdministracionTransaccional(Date fechaInicio, Date fechaFin){
		List<stusuario> lstusuarios = new ArrayList<stusuario>();
		try {
			List<?> usuarios = AdministracionTransaccionalDAO.obtenerTotalesPorUsuarios(fechaInicio, fechaFin);
			
			stusuario temp = null;
			for(Object user : usuarios){
				Object[] obj = (Object[]) user;
				temp = new stusuario();
				temp.usuario = (String)obj[0];
				temp.creados = (BigDecimal)obj[1];
				temp.actualizados = (BigDecimal)obj[2];
				temp.eliminados = (BigDecimal)obj[3];
				lstusuarios.add(temp);
			}
		} catch (Exception e) {
			CLogger.write("1", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private List<stagrupacion> getTransaccionesEliminadas(Date fechaInicio, Date fechaFin){
		List<stagrupacion> lstusuarios = new ArrayList<stagrupacion>();
		try {
			List<?> usuarios = AdministracionTransaccionalDAO.obtenerTransaccionesEliminadas(fechaInicio, fechaFin);
			
			DateTime time = new DateTime(fechaInicio);
			Integer anoInicial = time.getYear();
			time = new DateTime(fechaFin);
			Integer anoFinal = time.getYear();
			stanio[] tempAnio = inicializarStanio(anoInicial, anoFinal);
			
			stagrupacion temp = new stagrupacion();
			for(Object user : usuarios){
				Object[] obj = (Object[]) user;
				for(int i= 0; i<tempAnio.length;i++){
					if(tempAnio[i].anio.equals((Integer)obj[2])){
						tempAnio[i].mes[(Integer)obj[1] -1] = (BigDecimal)obj[0];
					}
				}
			}
			temp.anios = tempAnio;
			lstusuarios.add(temp);
		} catch (Exception e) {
			CLogger.write("1", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private List<stagrupacion> getTransaccionesActualizadas(Date fechaInicio, Date fechaFin){
		List<stagrupacion> lstusuarios = new ArrayList<stagrupacion>();
		try {
			List<?> usuarios = AdministracionTransaccionalDAO.obtenerTransaccionesActualizadas(fechaInicio, fechaFin);
			DateTime time = new DateTime(fechaInicio);
			Integer anoInicial = time.getYear();
			time = new DateTime(fechaFin);
			Integer anoFinal = time.getYear();
			stanio[] tempAnio = inicializarStanio(anoInicial, anoFinal);
			
			stagrupacion temp = new stagrupacion();
			for(Object user : usuarios){
				Object[] obj = (Object[]) user;
				for(int i= 0; i<tempAnio.length;i++){
					if(tempAnio[i].anio.equals((Integer)obj[2])){
						tempAnio[i].mes[(Integer)obj[1] -1] = (BigDecimal)obj[0];
					}
				}
			}
			temp.anios = tempAnio;
			lstusuarios.add(temp);
		} catch (Exception e) {
			CLogger.write("1", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private List<stagrupacion> getTransaccionesCreadas(Date fechaInicio, Date fechaFin){
		List<stagrupacion> lstusuarios = new ArrayList<stagrupacion>();
		try {
			List<?> usuarios = AdministracionTransaccionalDAO.obtenerTransaccionesCreadas(fechaInicio, fechaFin);
			
			DateTime time = new DateTime(fechaInicio);
			Integer anoInicial = time.getYear();
			time = new DateTime(fechaFin);
			Integer anoFinal = time.getYear();
			stanio[] tempAnio = inicializarStanio(anoInicial, anoFinal);
			
			stagrupacion temp = new stagrupacion();
			for(Object user : usuarios){
				Object[] obj = (Object[]) user;
				for(int i= 0; i<tempAnio.length;i++){
					if(tempAnio[i].anio.equals((Integer)obj[2])){
						tempAnio[i].mes[(Integer)obj[1] -1] = (BigDecimal)obj[0];
					}
				}
			}
			temp.anios = tempAnio;
			lstusuarios.add(temp);
		} catch (Exception e) {
			CLogger.write("1", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private stanio[] inicializarStanio (Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			for(int m=0; m<12; m++){
				temp.mes[m] = new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
	
	private List<stusuariodetalle> getAdministracionTransaccionalDetalle(String usuarioDetalle , Date fechaInicio, Date fechaFin){
		List<stusuariodetalle> lstusuarios = new ArrayList<stusuariodetalle>();
		try {
			List<?> usuarios = AdministracionTransaccionalDAO.obtenerTransaccionesPorUsuario(usuarioDetalle, fechaInicio, fechaFin);
			stusuariodetalle temp = null;
			for(Object user : usuarios){
				Object[] obj = (Object[]) user;
				 temp = new stusuariodetalle();
				 temp.usuario = (String)obj[0];
				 temp.objetoNombre = (String)obj[1];
				 temp.objetoTipo = (String)obj[2];
				 temp.estado = (String)obj[3];
				 temp.fecha = Utils.formatDateHour((Date)obj[4]);
				 lstusuarios.add(temp);
			}
		} catch (Exception e) {
			CLogger.write("1", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private byte[] exportarExcel(String usuario, Date fechaInicio, Date fechaFin){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(fechaInicio, fechaFin);
			CGraficaExcel grafica = generarGrafica(datos);
			excel = new CExcel("Administración Transaccional", false, grafica);
			wb=excel.generateExcelOfData(datos, "Administración Transaccional", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("2", SAdministracionTransaccional.class, e);
		}
		return outArray;
	}
	
	private byte[] exportarExcelDetalle(String usuario, Date fechaInicio, Date fechaFin){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeadersDetalle();
			datos = generarDatosDetalle(usuario, fechaInicio, fechaFin);
			String colaborador = ColaboradorDAO.getColaboradorByUsuario(usuario);
			excel = new CExcel("Detalle Administración Transaccional del " + (!colaborador.equals("") ? "colaborador " : "usuario ") + colaborador + "("+ usuario + ")", false, null);
			wb=excel.generateExcelOfData(datos, "Detalle Administración Transaccional del " + (!colaborador.equals("") ? "colaborador " : "usuario ") +  colaborador + "("+ usuario + ")", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("2", SAdministracionTransaccional.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Usuario", "Creados", "Actualizados", "Eliminados"},  //titulos
			null, //mapeo
			{"string", "int", "int", "int"}, //tipo dato
			{"", "sum", "sum", "sum"}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	private String[][] generarHeadersDetalle(){
		String headers[][];
		
		headers = new String[][]{
			{"Usuario", "Nombre", "Tipo", "Estado", "Fecha"},  //titulos
			null, //mapeo
			{"string", "string", "string", "string", "string"}, //tipo dato
			{"", "", "", "", ""}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatosDetalle(String usuario, Date fechaInicio, Date fechaFin){
		String[][] datos = null;
		try{
			List<stusuariodetalle> detalleUsuario = getAdministracionTransaccionalDetalle(usuario, fechaInicio, fechaFin);
			
			if (detalleUsuario != null && !detalleUsuario.isEmpty()){
				datos = new String[detalleUsuario.size()][5];
				for(int i=0; i < detalleUsuario.size(); i++){
					stusuariodetalle temp = detalleUsuario.get(i);
					datos[i][0] = temp.usuario;
					datos[i][1] = temp.objetoNombre;
					datos[i][2] = temp.objetoTipo;
					datos[i][3] = temp.estado;
					datos[i][4] = temp.fecha;
				}
			}
		}catch(Exception e){
			CLogger.write("3", SAdministracionTransaccional.class, e);
		}
		
		return datos;
	}
	
	public String[][] generarDatos(Date fechaInicio, Date fechaFin){
		List<stusuario> lstusuarios = getAdministracionTransaccional(fechaInicio, fechaFin);
		String[][] datos = null;
		totalCreados = 0;
		totalActualizados = 0;
		totalEliminados = 0;
		if (lstusuarios != null && !lstusuarios.isEmpty()){ 
			datos = new String[lstusuarios.size()][4];
			for (int i=0; i<lstusuarios.size(); i++){
				datos[i][0]=lstusuarios.get(i).usuario;
				datos[i][1]=lstusuarios.get(i).creados.toString();
				datos[i][2]=lstusuarios.get(i).actualizados.toString();
				datos[i][3]=lstusuarios.get(i).eliminados.toString();
				totalCreados += lstusuarios.get(i).creados.intValue();
				totalActualizados += lstusuarios.get(i).actualizados.intValue();
				totalEliminados += lstusuarios.get(i).eliminados.intValue();
			}
		}
			
		return datos;
	}
	
	public CGraficaExcel generarGrafica(String[][] datosTabla){
		
		String[][] datos = new String[][]{
			{"Creados","Actualizados","Eliminados"},
			{totalCreados.toString(),totalActualizados.toString(),totalEliminados.toString()}
		};
		String[][] datosIgualar= new String[][]{
			{"","",""},
			{"1."+(datosTabla.length+30),"2."+(datosTabla.length+30),"3."+(datosTabla.length+30)}
		};
		
		String[] tipoData = new String[]{"string","int"};
		CGraficaExcel grafica = new CGraficaExcel("Administración Transaccional", CGraficaExcel.EXCEL_CHART_BAR, "Cantidad", "Estados", datos, tipoData, datosIgualar);
	
		return grafica;
	}
} 
