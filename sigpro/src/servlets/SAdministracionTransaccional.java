package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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
import dao.ProyectoDAO;
import dao.UsuarioDAO;
import pojo.Proyecto;
import pojo.Usuario;
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
		Integer nivel;
		stusuario transacciones = new stusuario();
	}
	
	class stusuario{
		String usuario;
		Integer creados;
		Integer actualizados;
		Integer eliminados;
	}
	
	class stusuariodetalle{
		String usuario;
		String objetoNombre;
		Integer objetoTipo;
		String objetoTipoDesc;
		String estado;
		String fecha;
	}
	
	class stanio{
		Integer[] mes = new Integer[12];
		Integer anio;
		
	}
	
	class stagrupacion{
		stanio[] anios;
	}

	Integer totalCreados = 0;
	Integer totalActualizados = 0;
	Integer totalEliminados = 0;
	
	String[] meses = new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
       
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
			try{
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
				
				DateTime nuevaFechaFin = new DateTime(fechaFin);
				nuevaFechaFin = nuevaFechaFin.plusHours(23);
				nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
				nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
				
				List<Proyecto> lstprestamos = ProyectoDAO.getTodosProyectos();
				List<Usuario> lstUsuarios = UsuarioDAO.getUsuariosDisponibles();
				
				List<sttransaccion> lstcounttransacciones = getAdministracionTransaccional(fechaInicio, nuevaFechaFin.toDate(), lstprestamos, lstUsuarios);
				
				List<stagrupacion> lstcreados = getTransaccionesCreadas(fechaInicio, nuevaFechaFin.toDate(), lstprestamos, lstUsuarios);
				List<stagrupacion> lstactualizados = getTransaccionesActualizadas(fechaInicio, nuevaFechaFin.toDate(), lstprestamos, lstUsuarios);
				List<stagrupacion> lsteliminados = getTransaccionesEliminadas(fechaInicio, nuevaFechaFin.toDate(), lstprestamos, lstUsuarios);
				
				response_text= new GsonBuilder().serializeNulls().create().toJson(lstcounttransacciones);
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
			}catch(Exception e){
				CLogger.write("1", SAdministracionTransaccional.class, e);
			}
		}else if(accion.equals("exportarExcelDetalle")){
			try{
				Integer proyectoId = Utils.String2Int(map.get("proyectoId"));
				String proyectoNombre = map.get("proyectoNombre");
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
				
				DateTime nuevaFechaFin = new DateTime(fechaFin);
				nuevaFechaFin = nuevaFechaFin.plusHours(23);
				nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
				nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
				
				byte[] outArray = exportarExcelDetalle(proyectoId, proyectoNombre, fechaInicio, nuevaFechaFin.toDate(), usuario);
				
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Administración_Transaccional.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("2", SAdministracionTransaccional.class, e);
			}
		}else if (accion.equals("exportarExcel")){
			try{
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
				
				DateTime nuevaFechaFin = new DateTime(fechaFin);
				nuevaFechaFin = nuevaFechaFin.plusHours(23);
				nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
				nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
				
		        byte [] outArray = exportarExcel(usuario, fechaInicio, nuevaFechaFin.toDate());
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Administración_Transaccional.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("3", SAdministracionTransaccional.class, e);
			}
		}else if(accion.equals("exportarPdfDetalle")){
			try{
				Integer proyectoId = Utils.String2Int(map.get("proyectoId"));
				String proyectoNombre = map.get("proyectoNombre");
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
				
				DateTime nuevaFechaFin = new DateTime(fechaFin);
				nuevaFechaFin = nuevaFechaFin.plusHours(23);
				nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
				nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
				
				CPdf archivo = new CPdf("Detalle Administración Transaccional del Prestamo " + proyectoNombre);
				String headers[][];
				String datos[][];
				headers = generarHeadersDetalle();
				datos = generarDatosDetalle(proyectoId, fechaInicio, nuevaFechaFin.toDate());
				String path = archivo.ExportarPdfAdministracionTransaccionalDetalle(headers, datos,usuario);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("4", SAdministracionTransaccional.class, e);
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
					response.setHeader("Content-Disposition", "in-line; 'AdministraciónTransaccional.pdf'");
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
			
			DateTime nuevaFechaFin = new DateTime(fechaFin);
			nuevaFechaFin = nuevaFechaFin.plusHours(23);
			nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
			nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
			
			CPdf archivo = new CPdf("Administración Transaccional");
			String headers[][];
			String datos[][];
			headers = generarHeaders();
			
			List<Proyecto> lstprestamos = ProyectoDAO.getTodosProyectos();
			List<Usuario> lstUsuarios = UsuarioDAO.getUsuariosDisponibles();
			
			datos = generarDatos(fechaInicio, nuevaFechaFin.toDate(),lstprestamos,lstUsuarios);
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
				response.setHeader("Content-Disposition", "in-line; 'AdministraciónTransaccional.pdf'");
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
	
	private List<sttransaccion> getAdministracionTransaccional(Date fechaInicio, Date fechaFin, List<Proyecto> lstprestamos, List<Usuario> lstUsuarios){
		List<sttransaccion> lsttransaccion = new ArrayList<sttransaccion>();
		try {
			sttransaccion temp = new sttransaccion();
			for(Proyecto proyecto : lstprestamos){
				temp.nombre = proyecto.getNombre();
				temp.objeto_id = proyecto.getId();
				temp.objeto_tipo = 0;
				temp.nivel = 0;
				lsttransaccion.add(temp);
				
				for(Usuario usuario : lstUsuarios){
					temp =  new sttransaccion();
					
					stusuario userRes = new stusuario();
					Integer creadas = AdministracionTransaccionalDAO.obtenerCreadosProyectoUsuario(fechaInicio, fechaFin, proyecto.getId(), usuario.getUsuario());
					Integer actualizadas = AdministracionTransaccionalDAO.obtenerActualizadosProyectoUsuario(fechaInicio, fechaFin, proyecto.getId(), usuario.getUsuario());
					Integer eliminadas = AdministracionTransaccionalDAO.obtenerEliminadosProyectoUsuario(fechaInicio, fechaFin, proyecto.getId(), usuario.getUsuario());
					
					if(creadas == 0 && actualizadas == 0 && eliminadas == 0){
						
					}else{
						temp.nombre = usuario.getUsuario();
						temp.nivel = 1;
						userRes.creados = creadas;
						userRes.actualizados = actualizadas;
						userRes.eliminados = eliminadas;
						
						temp.transacciones = userRes;
						
						lsttransaccion.add(temp);
					}				
				}
			}			
		} catch (Exception e) {
			CLogger.write("6", SAdministracionTransaccional.class, e);
		}
		return lsttransaccion;
	}
	
	private List<stagrupacion> getTransaccionesEliminadas(Date fechaInicio, Date fechaFin, List<Proyecto> lstprestamos, List<Usuario> lstUsuarios){
		List<stagrupacion> lstusuarios = new ArrayList<stagrupacion>();
		try {
			DateTime time = new DateTime(fechaInicio);
			Integer anoInicial = time.getYear();
			time = new DateTime(fechaFin);
			Integer anoFinal = time.getYear();
			stanio[] tempAnio = inicializarStanio(anoInicial, anoFinal);
			stagrupacion temp = new stagrupacion();
			for(Proyecto proyecto : lstprestamos){
				for(Usuario usuario : lstUsuarios){
					List<?> creadas = AdministracionTransaccionalDAO.obtenerTransaccionesEliminadasProyectoUsuario(fechaInicio, fechaFin, proyecto.getId(), usuario.getUsuario());
					for(int i = 0; i<tempAnio.length;i++){
						for(int j = 0; j< creadas.size(); j++){
							Object[] obj = (Object[])creadas.get(j);
							if(tempAnio[i].anio.equals((Integer)obj[1])){
								tempAnio[i].mes[(Integer)obj[0] -1] += 1;
							}
						}
					}
				}
			}
			
			temp.anios = tempAnio;
			lstusuarios.add(temp);
		} catch (Exception e) {
			CLogger.write("7", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private List<stagrupacion> getTransaccionesActualizadas(Date fechaInicio, Date fechaFin, List<Proyecto> lstprestamos, List<Usuario> lstUsuarios){
		List<stagrupacion> lstusuarios = new ArrayList<stagrupacion>();
		try {
			DateTime time = new DateTime(fechaInicio);
			Integer anoInicial = time.getYear();
			time = new DateTime(fechaFin);
			Integer anoFinal = time.getYear();
			stanio[] tempAnio = inicializarStanio(anoInicial, anoFinal);
			stagrupacion temp = new stagrupacion();
			for(Proyecto proyecto : lstprestamos){
				for(Usuario usuario : lstUsuarios){
					List<?> creadas = AdministracionTransaccionalDAO.obtenerTransaccionesActualizadosProyectoUsuario(fechaInicio, fechaFin, proyecto.getId(), usuario.getUsuario());
					for(int i = 0; i<tempAnio.length;i++){
						for(int j = 0; j< creadas.size(); j++){
							Object[] obj = (Object[])creadas.get(j);
							if(tempAnio[i].anio.equals((Integer)obj[1])){
								tempAnio[i].mes[(Integer)obj[0] -1] += 1;
							}
						}
					}
				}
			}
			
			temp.anios = tempAnio;
			lstusuarios.add(temp);
		} catch (Exception e) {
			CLogger.write("8", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private List<stagrupacion> getTransaccionesCreadas(Date fechaInicio, Date fechaFin, List<Proyecto> lstprestamos, List<Usuario> lstUsuarios){
		List<stagrupacion> lstusuarios = new ArrayList<stagrupacion>();
		try {			
			DateTime time = new DateTime(fechaInicio);
			Integer anoInicial = time.getYear();
			time = new DateTime(fechaFin);
			Integer anoFinal = time.getYear();
			stanio[] tempAnio = inicializarStanio(anoInicial, anoFinal);
			stagrupacion temp = new stagrupacion();
			for(Proyecto proyecto : lstprestamos){
				for(Usuario usuario : lstUsuarios){
					List<?> creadas = AdministracionTransaccionalDAO.obtenerTransaccionesCreadosProyectoUsuario(fechaInicio, fechaFin, proyecto.getId(), usuario.getUsuario());
					for(int i = 0; i<tempAnio.length;i++){
						for(int j = 0; j< creadas.size(); j++){
							Object[] obj = (Object[])creadas.get(j);
							if(tempAnio[i].anio.equals((Integer)obj[1])){
								tempAnio[i].mes[(Integer)obj[0] -1] += 1;
							}
						}
					}
				}
			}
			
			temp.anios = tempAnio;
			lstusuarios.add(temp);
		} catch (Exception e) {
			CLogger.write("9", SAdministracionTransaccional.class, e);
		}
		return lstusuarios;
	}
	
	private stanio[] inicializarStanio (Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			for(int m=0; m<12; m++){
				temp.mes[m] = 0;
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
	
	private List<stusuariodetalle> getAdministracionTransaccionalDetalle(Integer proyectoId, Date fechaInicio, Date fechaFin){
		List<stusuariodetalle> lstusuarios = new ArrayList<stusuariodetalle>();
		try {
			List<?> creadas = AdministracionTransaccionalDAO.obtenerTransaccionesCreadosProyectoUsuario(fechaInicio, fechaFin, proyectoId, null);
			List<?> actualizadas = AdministracionTransaccionalDAO.obtenerTransaccionesActualizadosProyectoUsuario(fechaInicio, fechaFin, proyectoId, null);
			List<?> eliminadas = AdministracionTransaccionalDAO.obtenerTransaccionesEliminadasProyectoUsuario(fechaInicio, fechaFin, proyectoId, null);
			
			List<Object> lstPrestamo = new ArrayList<Object>();
			lstPrestamo.addAll(creadas);
			lstPrestamo.addAll(actualizadas);
			lstPrestamo.addAll(eliminadas);
			
			stusuariodetalle temp = null;
			for(Object user : lstPrestamo){
				Object[] obj = (Object[]) user;
				 temp = new stusuariodetalle();
				 temp.objetoNombre = (String)obj[2];
				 temp.objetoTipo = (Integer)obj[3];
				 temp.usuario = (String)obj[4];
				 temp.objetoTipoDesc = (String)obj[5];
				 temp.estado = (String)obj[6];
				 temp.fecha = Utils.formatDateHour((Date)obj[7]);
				 lstusuarios.add(temp);
			}
		} catch (Exception e) {
			CLogger.write("10", SAdministracionTransaccional.class, e);
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
			
			List<Proyecto> lstprestamos = ProyectoDAO.getTodosProyectos();
			List<Usuario> lstUsuarios = UsuarioDAO.getUsuariosDisponibles();
			
			datos = generarDatos(fechaInicio, fechaFin, lstprestamos, lstUsuarios);
			CGraficaExcel grafica = generarGrafica(datos, fechaInicio, fechaFin, lstprestamos, lstUsuarios);
			excel = new CExcel("Administración Transaccional", false, grafica);
			wb=excel.generateExcelOfData(datos, "Administración Transaccional", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("11", SAdministracionTransaccional.class, e);
		}
		return outArray;
	}
	
	private byte[] exportarExcelDetalle(Integer proyectoId, String proyectoNombre, Date fechaInicio, Date fechaFin, String usuario){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeadersDetalle();
			datos = generarDatosDetalle(proyectoId, fechaInicio, fechaFin);
			excel = new CExcel("Detalle Administración Transaccional del Préstamo " + proyectoNombre, false, null);
			wb=excel.generateExcelOfData(datos, "Detalle Administración Transaccional del Prestamo " + proyectoNombre, headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("12", SAdministracionTransaccional.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Creados", "Actualizados", "Eliminados"},  //titulos
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
			{"Nombre", "Tipo", "Estado", "Fecha", "Usuario"},  //titulos
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
	
	public String[][] generarDatosDetalle(Integer proyectoId, Date fechaInicio, Date fechaFin){
		String[][] datos = null;
		try{
			List<stusuariodetalle> detalleUsuario = getAdministracionTransaccionalDetalle(proyectoId, fechaInicio, fechaFin);
			
			if (detalleUsuario != null && !detalleUsuario.isEmpty()){
				datos = new String[detalleUsuario.size()][5];
				for(int i=0; i < detalleUsuario.size(); i++){
					stusuariodetalle temp = detalleUsuario.get(i);
					if(temp.objetoTipo == null)
						datos[i][0] = "		" + temp.objetoNombre;	
					else
						datos[i][0] = temp.objetoNombre;					
					datos[i][1] = temp.objetoTipoDesc;
					datos[i][2] = temp.estado;
					datos[i][3] = temp.fecha;
					datos[i][4] = temp.usuario;

				}
			}
		}catch(Exception e){
			CLogger.write("13", SAdministracionTransaccional.class, e);
		}
		
		return datos;
	}
	
	public String[][] generarDatos(Date fechaInicio, Date fechaFin, List<Proyecto> lstprestamos, List<Usuario> lstUsuarios){		
		List<sttransaccion> transacciones = getAdministracionTransaccional(fechaInicio, fechaFin, lstprestamos, lstUsuarios);
		
		String[][] datos = null;
		totalCreados = 0;
		totalActualizados = 0;
		totalEliminados = 0;
		
		if(transacciones != null && !transacciones.isEmpty()){
			datos = new String[transacciones.size()][4];
			for(int i=0; i<transacciones.size(); i++){
				if(transacciones.get(i).objeto_tipo == null)
					datos[i][0] = "      " + transacciones.get(i).nombre;	
				else
					datos[i][0] = transacciones.get(i).nombre;
				
				datos[i][1]=transacciones.get(i).transacciones.creados != null ? transacciones.get(i).transacciones.creados.toString() : null;
				datos[i][2]=transacciones.get(i).transacciones.actualizados != null ? transacciones.get(i).transacciones.actualizados.toString() : null;
				datos[i][3]=transacciones.get(i).transacciones.eliminados != null ? transacciones.get(i).transacciones.eliminados.toString() : null;
				totalCreados += transacciones.get(i).transacciones.creados != null ? transacciones.get(i).transacciones.creados : 0;
				totalActualizados += transacciones.get(i).transacciones.actualizados != null ? transacciones.get(i).transacciones.actualizados : 0;
				totalEliminados += transacciones.get(i).transacciones.eliminados != null ? transacciones.get(i).transacciones.eliminados : 0;
			}
		}
		
		return datos;
	}
	
	public CGraficaExcel generarGrafica(String[][] datosTabla, Date fechaInicio, Date fechaFin, List<Proyecto> lstprestamos, List<Usuario> lstUsuarios){
		List<stagrupacion> creados = getTransaccionesCreadas(fechaInicio, fechaFin, lstprestamos, lstUsuarios);
		List<stagrupacion> actualizados = getTransaccionesActualizadas(fechaInicio, fechaFin, lstprestamos, lstUsuarios);
		List<stagrupacion> eliminados = getTransaccionesEliminadas(fechaInicio, fechaFin, lstprestamos, lstUsuarios);
		DateTime inicio = new DateTime(fechaInicio);
		DateTime fin = new DateTime(fechaFin);
		int total = (fin.getYear() - inicio.getYear() + 1) * 12;
		int cont = 0;
		int contanios = 0;
		String[][] datos = new String[4][total];
		for(int j=0; j < total; j++){
			if(cont == 12)
			{
				cont = 0;
				contanios++;
			}
			datos[0][j] = meses[cont] + "-" + (inicio.getYear() + contanios);
			cont++;
		}
		
		contanios = 0;
		cont = 0;
		for(int j=0; j < total; j++){
			if(cont == 12){
				cont = 0;
				contanios++;
			}
			datos[1][j] = creados.get(0).anios[contanios].mes[cont].toString();
			datos[2][j] = actualizados.get(0).anios[contanios].mes[cont].toString();
			datos[3][j] = eliminados.get(0).anios[contanios].mes[cont].toString();
			cont++;
		}
		
		String[] tipoData = new String[total+1];
		for(int i=0; i<total; i++){
			if(i==0)
				tipoData[i] = "string";
			else
				tipoData[i] = "int";
		}
		
		CGraficaExcel grafica = new CGraficaExcel("Administración Transaccional", CGraficaExcel.EXCEL_CHART_AREA2, "Meses", "Creados", datos, tipoData, null);
	
		return grafica;
	}
} 
