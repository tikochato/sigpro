package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import dao.AsignacionRaciDAO;
import dao.EstructuraProyectoDAO;
import dao.HitoDAO;
import dao.HitoResultadoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.AsignacionRaci;
import pojo.Hito;
import pojo.HitoResultado;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CPdf;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SAvanceActividades")
public class SAvanceActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stActividad{
		int id;
		int porcentajeAvance;
		String nombre;
		String responsable;
		String fechaInicio;
		String fechaFin;
		String fechaInicialReal;
		String fechaFinalReal;
		String descripcion;
	}
	
	class stCantidad{
		double sinIniciar;
		double proceso;
		double completadas;
		double retrasadas;
		double esperadasfinanio;
		double aniosiguientes;
	}
	
	class stAvance{
		Integer objetoId;
		Integer objetoTipo;
		String nombre;
		double sinIniciar;
		double proceso;
		double completadas;
		double retrasadas;
		double esperadasfinanio;
		double aniosSiguientes;
		int tipo;
	}
	
	class stelementosActividadesAvance{
		Integer id;
		String nombre;
		String fechaInicial;
		String fechaFinal;
		Integer avance;
		String responsable;
		String estado;
		String fechaInicialReal;
		String fechaFinalReal;
		String descripcion;
	}
	
	class stElementoResult{
		List<stAvance> listaResult;
		List<stCantidad> listaResultCantidad;
		double total;
	}
	
    public SAvanceActividades() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{		
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
			Map<String, String> map = gson.fromJson(sb.toString(), type);
			String accion = map.get("accion")!=null ? map.get("accion") : "";
			
			String response_text = "";
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			String fechaCorte = map.get("fechaCorte");
			String lineaBase = map.get("lineaBase");
						
			if (accion.equals("getAvance")){
				try{
					stElementoResult avanceActividades = getAvanceActividadesPrestamo(idPrestamo, fechaCorte, usuario,lineaBase);
						
					if(avanceActividades != null){
						response_text = new GsonBuilder().serializeNulls().create().toJson(avanceActividades.listaResult);
						response_text = String.join("", ",\"actividades\":",response_text);
						String cantidades = new GsonBuilder().serializeNulls().create().toJson(avanceActividades.listaResultCantidad);
						response_text += String.join("", ",\"cantidadesActividades\":",cantidades);
						response_text += String.join("", ",\"totalActividades\":" + avanceActividades.total,response_text);
					}
					
					stElementoResult avanceHitos = getAvanceHitos(idPrestamo, fechaCorte, usuario,lineaBase);
					
					if(avanceHitos != null){
						String cantidades = new GsonBuilder().serializeNulls().create().toJson(avanceHitos.listaResultCantidad);
						response_text += String.join("", ",\"cantidadHitos\":",cantidades);
						String response_hitos = new GsonBuilder().serializeNulls().create().toJson(avanceHitos.listaResult);
						response_text += String.join("", ",\"hitos\":",response_hitos);
						response_text = String.join("", ",\"totalHitos\":" + avanceHitos.total,response_text);
					}
					
					stElementoResult avanceProductos = getAvanceProductos(idPrestamo, fechaCorte, usuario,lineaBase);
					
					if(avanceProductos != null){
						String response_productos = new GsonBuilder().serializeNulls().create().toJson(avanceProductos.listaResult);
						response_text += String.join("", ",\"productos\":",response_productos);
						response_text = String.join("", ",\"totalProductos\":" + avanceProductos.total,response_text);
					}
				}catch (Throwable e) {
				    CLogger.write("1", SAvanceActividades.class, e);					
			    }
				
				response_text = String.join("", "{\"success\":true ", response_text, "}");
			}else if(accion.equals("getActividadesProyecto")){
				List<?> actividades = EstructuraProyectoDAO.getActividadesProyecto(idPrestamo, lineaBase);
				Date Corte = new Date();
				Date inicio = new Date();
				Date fin = new Date();
				
				if(actividades != null && !actividades.isEmpty()){
					List<stActividad> lstActividadesProyecto = new ArrayList<stActividad>();
					stActividad tempActividad = null;
					for (Object actividad : actividades){
						
						tempActividad = new stActividad();
						Object[] obj = (Object[])actividad;
						tempActividad.id = (Integer)obj[0];
						tempActividad.nombre = (String)obj[1];
						tempActividad.fechaInicio = Utils.formatDate((Date)obj[5]);
						tempActividad.fechaFin = Utils.formatDate((Date)obj[6]);
						tempActividad.porcentajeAvance = (Integer)obj[12];
						tempActividad.fechaInicialReal = Utils.formatDate((Date)obj[13]);
						tempActividad.fechaFinalReal = Utils.formatDate((Date)obj[14]);
						tempActividad.descripcion = obj[15] != null ? (String)obj[15] : "";
						
						AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(tempActividad.id, 5, "R", lineaBase); 
						tempActividad.responsable = asignacion != null ? ((asignacion != null ? asignacion.getColaborador().getPnombre() : null) + " " + (asignacion != null ? asignacion.getColaborador().getPapellido() : null)) : null;
						lstActividadesProyecto.add(tempActividad);
					}
					
					List<stelementosActividadesAvance> lstElementosActividadesAvance = new ArrayList<stelementosActividadesAvance>();
					for(stActividad actividad : lstActividadesProyecto){
						stelementosActividadesAvance temp = new stelementosActividadesAvance();
						temp.id = actividad.id;
						temp.nombre = actividad.nombre;
						temp.fechaInicial = actividad.fechaInicio;
						temp.fechaFinal = actividad.fechaFin;
						temp.avance = actividad.porcentajeAvance;
						temp.responsable = actividad.responsable;
						temp.fechaInicialReal = actividad.fechaInicialReal;
						temp.fechaFinalReal = actividad.fechaFinalReal;
						temp.descripcion = actividad.descripcion;
						
						inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(actividad.fechaInicio);
						
						fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(actividad.fechaFin);
						
						DateTime nuevaFechaFin = new DateTime(fin);
						nuevaFechaFin = nuevaFechaFin.plusHours(23);
						nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
						nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
						fin = nuevaFechaFin.toDate();
						
						Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(fechaCorte);
						
						DateTime nuevaFechaCorte = new DateTime(Corte);
						nuevaFechaFin = nuevaFechaFin.plusHours(23);
						nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
						nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
						Corte = nuevaFechaCorte.toDate();
						
						if (actividad.porcentajeAvance == 0 && (!Corte.after(fin))){
							temp.estado = "Sin iniciar";
						}else if(Corte.after(inicio) && Corte.before(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100){
							temp.estado = "En proceso";
						}else if (Corte.after(fin) && actividad.porcentajeAvance >= 0 && actividad.porcentajeAvance < 100 ){
							temp.estado = "Retrasado";
						}else if(actividad.porcentajeAvance == 100){
							temp.estado = "Completada";
						}
						
						lstElementosActividadesAvance.add(temp);
					}
					
					response_text = new GsonBuilder().serializeNulls().create().toJson(lstElementosActividadesAvance);
					response_text = String.join("", ",\"items\":",response_text);
					response_text = String.join("", "{\"success\":true ", response_text, "}");
				}
			}else if(accion.equals("getHitos")){
				Date Corte = new Date();
				List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, idPrestamo, null, null, null, null, null);
				List<stelementosActividadesAvance> lstElementosActividadesAvance = new ArrayList<stelementosActividadesAvance>();
				
				for(Hito hito : hitos){
					stelementosActividadesAvance temp = null;
					HitoResultado hitoResultado = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
					Date fechaHito = new Date();
					try{	
						fechaHito = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(Utils.formatDate(hito.getFecha()));
						
						DateTime nuevaFechaHito = new DateTime(fechaHito);
						nuevaFechaHito = nuevaFechaHito.plusHours(23);
						nuevaFechaHito = nuevaFechaHito.plusMinutes(59);
						nuevaFechaHito = nuevaFechaHito.plusSeconds(59);
						fechaHito = nuevaFechaHito.toDate();						
						
						Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(fechaCorte);
						
						DateTime nuevaFechaCorte = new DateTime(Corte);
						nuevaFechaCorte = nuevaFechaCorte.plusHours(23);
						nuevaFechaCorte = nuevaFechaCorte.plusMinutes(59);
						nuevaFechaCorte = nuevaFechaCorte.plusSeconds(59);
						Corte = nuevaFechaCorte.toDate();
						
						if(hitoResultado != null){
							temp = new stelementosActividadesAvance();
							temp.id = hito.getId();
							temp.nombre = hito.getNombre();
							temp.avance = 100;
							temp.estado = "Completado";
							temp.fechaInicial = Utils.formatDate(hito.getFecha());
							lstElementosActividadesAvance.add(temp);							
						}else{							
							if (Corte.before(fechaHito) || Corte.equals(fechaHito)){
								temp = new stelementosActividadesAvance();
								temp.id = hito.getId();
								temp.nombre = hito.getNombre();
								temp.avance = 0;
								temp.estado = "Sin iniciar";
								temp.fechaInicial = Utils.formatDate(hito.getFecha());
								lstElementosActividadesAvance.add(temp);
							} else if (Corte.after(fechaHito)){
								temp = new stelementosActividadesAvance();
								temp.id = hito.getId();
								temp.nombre = hito.getNombre();
								temp.avance = 0;
								temp.estado = "Retrasado";
								temp.fechaInicial = Utils.formatDate(hito.getFecha());
								lstElementosActividadesAvance.add(temp);
							}
						}
					}catch (Throwable e) {
					    CLogger.write("2", SAvanceActividades.class, e);
				    }
				}
				
				response_text = new GsonBuilder().serializeNulls().create().toJson(lstElementosActividadesAvance);
				response_text = String.join("", ",\"items\":",response_text);
				response_text = String.join("", "{\"success\":true ", response_text, "}");
			}else if(accion.equals("getActividadesProducto")){
				Integer productoId = Utils.String2Int(map.get("productoId"));
				Producto producto = ProductoDAO.getProductoPorId(productoId);
				Date Corte = new Date();
				Date inicio = new Date();
				Date fin = new Date();
				
				List<?> lstActividadesProducto = EstructuraProyectoDAO.getActividadesByTreePath(producto.getTreePath(), 
						producto.getComponente() != null ? producto.getComponente().getProyecto().getId() : producto.getSubcomponente().getComponente().getProyecto().getId(),
								lineaBase);
				List<stActividad> actividades = new ArrayList<stActividad>();
				if (lstActividadesProducto != null){
					stActividad tempActividad = null;
					for (Object actividad : lstActividadesProducto){
						tempActividad = new stActividad();
						Object[] obj = (Object[])actividad;
						tempActividad.id = (Integer)obj[0];
						tempActividad.nombre = (String)obj[1];
						tempActividad.fechaInicio = Utils.formatDate((Date)obj[2]);
						tempActividad.fechaFin = Utils.formatDate((Date)obj[3]);
						tempActividad.porcentajeAvance = (Integer)obj[4];
						tempActividad.fechaInicialReal = Utils.formatDate((Date)obj[5]);
						tempActividad.fechaFinalReal = Utils.formatDate((Date)obj[6]);
						tempActividad.descripcion = obj[7] != null ? (String)obj[7] : "";
						
						AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(tempActividad.id, 5, "R", lineaBase); 
						tempActividad.responsable = asignacion != null ? ((asignacion != null ? asignacion.getColaborador().getPnombre() : null) + " " + (asignacion != null ? asignacion.getColaborador().getPapellido() : null)) : null;
						actividades.add(tempActividad);
					}
				}
				
				List<stelementosActividadesAvance> lstElementosActividadesAvance = new ArrayList<stelementosActividadesAvance>();
				for(stActividad actividad : actividades){
					stelementosActividadesAvance temp = new stelementosActividadesAvance();
					temp.id = actividad.id;
					temp.nombre = actividad.nombre;
					temp.fechaInicial = actividad.fechaInicio;
					temp.fechaFinal = actividad.fechaFin;
					temp.avance = actividad.porcentajeAvance;
					temp.responsable = actividad.responsable;
					temp.fechaInicialReal = actividad.fechaInicialReal;
					temp.fechaFinalReal = actividad.fechaFinalReal;
					temp.descripcion = actividad.descripcion;
					
					inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(actividad.fechaInicio);
					
					fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(actividad.fechaFin);
					
					DateTime nuevaFechaFin = new DateTime(fin);
					nuevaFechaFin = nuevaFechaFin.plusHours(23);
					nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
					nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
					fin = nuevaFechaFin.toDate();
					
					Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(fechaCorte);
					
					DateTime nuevaFechaCorte = new DateTime(Corte);
					nuevaFechaCorte = nuevaFechaCorte.plusHours(23);
					nuevaFechaCorte = nuevaFechaCorte.plusMinutes(59);
					nuevaFechaCorte = nuevaFechaCorte.plusSeconds(59);
					Corte = nuevaFechaCorte.toDate();
					
					if (actividad.porcentajeAvance == 0 && (!Corte.after(fin))){
						temp.estado = "Sin iniciar";
					}else if(Corte.after(inicio) && Corte.before(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100){
						temp.estado = "En proceso";
					}else if (Corte.after(fin) && actividad.porcentajeAvance >= 0 && actividad.porcentajeAvance < 100 ){
						temp.estado = "Retrasado";
					}else if(actividad.porcentajeAvance == 100){
						temp.estado = "Complentada";
					}
					lstElementosActividadesAvance.add(temp);
				}
				
				response_text = new GsonBuilder().serializeNulls().create().toJson(lstElementosActividadesAvance);
				response_text = String.join("", ",\"items\":",response_text);
				response_text = String.join("", "{\"success\":true ", response_text, "}");
			}else if (accion.equals("exportarExcel")){
				try{
			        byte [] outArray = exportarExcel(idPrestamo, fechaCorte, usuario,lineaBase);
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);					
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "attachment; Reporte_Avances_de_Actividades_e_Hitos.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}catch(Exception e){
				    CLogger.write("3", SAvanceActividades.class, e);
				}
			}else if(accion.equals("exportarPdf")){
				CPdf archivo = new CPdf("Reporte de Avance");
				String headers[][];
				String datos[][];
				headers = generarHeaders();
				datos = generarDatos(idPrestamo, fechaCorte, usuario,lineaBase);
				String path = archivo.ExportarPdfAvanceActividades(headers, datos,usuario);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("5", SInformacionPresupuestaria.class, e);
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
					response.setHeader("Content-Disposition", "in-line; 'AvanceActividadeseHitos.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
				
			}else if(accion.equals("exportarDetalleExcel")){
				try{
					Integer id = Utils.String2Int(map.get("id"));
					Integer tipo = Utils.String2Int(map.get("objetoTipo"));
					
			        byte [] outArray = exportarDetalleExcel(id, tipo, fechaCorte, usuario, lineaBase);
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);					
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "attachment; Reporte_Avances_de_Actividades_e_Hitos.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}catch(Exception e){
				    CLogger.write("3", SAvanceActividades.class, e);
				}
			}
			else{
				response_text = "{ \"success\": false }";
			}
			
			if(!accion.equals("exportarExcel") && !accion.equals("exportarPdf") && !accion.equals("exportarDetalleExcel")){
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");

		        OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
		        gz.write(response_text.getBytes("UTF-8"));
		        gz.close();
		        output.close();
			}
		}catch(Exception e){
		    CLogger.write("4", SAvanceActividades.class, e);
		}
		
	}
	
	private stElementoResult getAvanceActividadesPrestamo(Integer idPrestamo, String fechaCorte, String usuario, String lineaBase){
		stElementoResult resultado = null;
		double  totalActividades = 0;
		double  totalSinIniciar = 0;
		double  totalEnProceso = 0;
		double  totalCompletadas = 0;
		double  totalRetrasadas = 0;
		double totalEsperadasAnio = 0;
		double totalAniosSiguientes = 0;
		Date inicio = new Date();
		Date fin = new Date();
		Date Corte = new Date();
		DateTime anioCorte = new DateTime();
		
		List<stAvance> listaResult = new ArrayList<stAvance>();
		List<stCantidad> listaResultCantidad = new ArrayList<stCantidad>();
		stAvance temp = new stAvance();
		stCantidad tCantidad = new stCantidad();
		
		DecimalFormat df2 = new DecimalFormat("###.##");
		
		try{
			List<?> actividades = EstructuraProyectoDAO.getActividadesProyecto(idPrestamo, lineaBase);
			if (actividades != null){
				if(actividades.size() > 0){
					totalActividades = actividades.size();
					
					List<stActividad> lstActividadesProyecto = new ArrayList<stActividad>();
					stActividad tempActividad = null;
					for (Object actividad : actividades){
						tempActividad = new stActividad();
						Object[] obj = (Object[])actividad;
						tempActividad.id = (Integer)obj[0];
						tempActividad.nombre = (String)obj[1];
						tempActividad.fechaInicio = Utils.formatDate((Date)obj[5]);
						tempActividad.fechaFin = Utils.formatDate((Date)obj[6]);
						tempActividad.porcentajeAvance = (Integer)obj[12];
						
						AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(tempActividad.id, 5, "R", lineaBase); 
						tempActividad.responsable = asignacion != null ? ((asignacion != null ? asignacion.getColaborador().getPnombre() : null) + " " + (asignacion != null ? asignacion.getColaborador().getPapellido() : null)) : null;
						lstActividadesProyecto.add(tempActividad);
					}
						
					for(stActividad actividad : lstActividadesProyecto){
						inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(actividad.fechaInicio);
						
						fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(actividad.fechaFin);
						
						DateTime nuevaFechaFin = new DateTime(fin);
						nuevaFechaFin = nuevaFechaFin.plusHours(23);
						nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
						nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
						fin = nuevaFechaFin.toDate();
						
						Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(fechaCorte);
						
						DateTime nuevaFechaCorte = new DateTime(Corte);
						nuevaFechaCorte = nuevaFechaCorte.plusHours(23);
						nuevaFechaCorte = nuevaFechaCorte.plusMinutes(59);
						nuevaFechaCorte = nuevaFechaCorte.plusSeconds(59);
						Corte = nuevaFechaCorte.toDate();
						
						anioCorte = new DateTime(Corte);
						Integer anio = anioCorte.getYear();
						
						DateTime anioInicio = new DateTime(anio,1,1,0,0,0);
						DateTime anioFin = new DateTime(anio,12,31,23,59,59);
						
						if(actividad.porcentajeAvance >= 0 && actividad.porcentajeAvance < 100 && inicio.after(anioInicio.toDate()) && inicio.before(anioFin.toDate())){
							totalEsperadasAnio++;
						}
						
						if (actividad.porcentajeAvance == 0 && (!Corte.after(fin))){
							totalSinIniciar++;
						}else if(Corte.after(inicio) && Corte.before(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100){
							totalEnProceso++;
						}else if (Corte.after(fin) && actividad.porcentajeAvance >= 0 && actividad.porcentajeAvance < 100 ){
							totalRetrasadas++;
						}else if(actividad.porcentajeAvance == 100){
							totalCompletadas++;
						}
						
						if(inicio.after(anioFin.toDate())){
							totalAniosSiguientes++;
						}
					}
					
					tCantidad.sinIniciar = totalSinIniciar;
					tCantidad.proceso = totalEnProceso;
					tCantidad.completadas = totalCompletadas;
					tCantidad.retrasadas = totalRetrasadas;
					tCantidad.esperadasfinanio = totalEsperadasAnio;
					tCantidad.aniosiguientes = totalAniosSiguientes;
					listaResultCantidad.add(tCantidad);
					totalSinIniciar = (totalSinIniciar/totalActividades)*100;
					totalEnProceso = (totalEnProceso/totalActividades)*100;
					totalCompletadas = (totalCompletadas/totalActividades)*100;
					totalRetrasadas = (totalRetrasadas/totalActividades)*100;
					totalEsperadasAnio = (totalEsperadasAnio/totalActividades)*100;
					totalAniosSiguientes = (totalAniosSiguientes/totalActividades)*100;
					
					if (Double.isNaN(totalSinIniciar)){
						totalSinIniciar = 0;
					}
					
					if (Double.isNaN(totalEnProceso)){
						totalEnProceso = 0;
					}
					
					if (Double.isNaN(totalCompletadas)){
						totalCompletadas = 0;
					}
					
					if (Double.isNaN(totalRetrasadas)){
						totalRetrasadas = 0;
					}
					
					if(Double.isNaN(totalEsperadasAnio)){
						totalEsperadasAnio = 0;
					}
					
					if(Double.isNaN(totalActividades)){
						totalActividades = 0;
					}
					
					if(Double.isNaN(totalAniosSiguientes)){
						totalAniosSiguientes = 0;
					}
					
					temp = new stAvance();
					temp.objetoId = idPrestamo;
					temp.objetoTipo = 1;
					temp.nombre = "Total de actividades";
					temp.sinIniciar = Double.valueOf(df2.format(totalSinIniciar));
					temp.proceso = Double.valueOf(df2.format(totalEnProceso));
					temp.completadas = Double.valueOf(df2.format(totalCompletadas));
					temp.retrasadas = Double.valueOf(df2.format(totalRetrasadas));
					temp.esperadasfinanio = Double.valueOf(df2.format(totalEsperadasAnio));
					temp.aniosSiguientes = Double.valueOf(df2.format(totalAniosSiguientes));
					temp.tipo = 2;
					
					listaResult.add(temp);
					
					resultado = new stElementoResult();
					resultado.listaResult = listaResult;
					resultado.listaResultCantidad = listaResultCantidad;
					resultado.total = totalActividades;
				}
			}
		}catch(Exception e){
		    CLogger.write("5", SAvanceActividades.class, e);
		}
		return resultado;
	}
	
	private stElementoResult getAvanceHitos(Integer idPrestamo, String fechaCorte, String usuario,String lineaBase){
		stElementoResult resultado = null;
		double  totalSinIniciar = 0;
		double  totalCompletadas = 0;
		double  totalRetrasadas = 0;
		double totalEsperadasAnio = 0;
		double totalAniosSiguientes = 0;
		Date Corte = new Date();
		
		List<stAvance> listaResult = new ArrayList<stAvance>();
		List<stCantidad> listaResultCantidad = new ArrayList<stCantidad>();
		stAvance temp = new stAvance();
		stCantidad tCantidad = new stCantidad();
		
		DecimalFormat df2 = new DecimalFormat("###.##");
		
		try{
		List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, idPrestamo, null, null, null, null, null);
		DateTime anioCorte = new DateTime();
		
		if(hitos != null && hitos.size() > 0){
		
			listaResult = new ArrayList<stAvance>();
			double  totalHitos = 0;
			
			Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    .parse(fechaCorte);
			
			DateTime nuevaFechaCorte = new DateTime(Corte);
			nuevaFechaCorte = nuevaFechaCorte.plusHours(23);
			nuevaFechaCorte = nuevaFechaCorte.plusMinutes(59);
			nuevaFechaCorte = nuevaFechaCorte.plusSeconds(59);
			Corte = nuevaFechaCorte.toDate();
			
			Date fechaHito = new Date();
			
			anioCorte = new DateTime(Corte);
			Integer anio = anioCorte.getYear();
			
			DateTime anioInicio = new DateTime(anio, 1,1,0,0,0);
			DateTime anioFin = new DateTime(anio,12,31,23,59,59);
			
			for(Hito hito : hitos){
				fechaHito = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
						.parse(Utils.formatDate(hito.getFecha()));
				
				HitoResultado hitoResultado = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				
				if(hitoResultado != null){
						totalCompletadas++;
					totalHitos++;
				}else{						
					if(fechaHito.after(anioInicio.toDate()) && fechaHito.before(anioFin.toDate())){
						totalEsperadasAnio++;
					}else if(fechaHito.after(anioFin.toDate())){
						totalAniosSiguientes++;
					}
					
					if (Corte.before(fechaHito) || Corte.equals(fechaHito)){
						totalSinIniciar++;
					}else if (Corte.after(fechaHito)){
						totalRetrasadas++;
					}
					totalHitos++;
				}
			}
			
			listaResultCantidad = new ArrayList<stCantidad>();
			tCantidad = new stCantidad();
			
			tCantidad.sinIniciar = totalSinIniciar;
			tCantidad.retrasadas = totalRetrasadas;
			tCantidad.completadas = totalCompletadas;
			tCantidad.esperadasfinanio = totalEsperadasAnio;
			tCantidad.aniosiguientes = totalAniosSiguientes;
			
			listaResultCantidad.add(tCantidad);
			
			totalSinIniciar = (totalSinIniciar/totalHitos)*100;
			totalCompletadas = (totalCompletadas/totalHitos)*100;
			totalRetrasadas = (totalRetrasadas/totalHitos)*100;
			totalEsperadasAnio = (totalEsperadasAnio/totalHitos)*100;
			totalAniosSiguientes = (totalAniosSiguientes/totalHitos)*100;
			
			if (Double.isNaN(totalSinIniciar)){
				totalSinIniciar = 0;
			}
			
			if (Double.isNaN(totalCompletadas)){
				totalCompletadas = 0;
			}
			
			if (Double.isNaN(totalRetrasadas)){
				totalRetrasadas = 0;
			}
			
			if(Double.isNaN(totalEsperadasAnio)){
				totalEsperadasAnio = 0;
			}
			
			if(Double.isNaN(totalAniosSiguientes)){
				totalAniosSiguientes = 0;
			}
			
			temp = new stAvance();
			temp.objetoId = idPrestamo;
			temp.objetoTipo = 10;
			temp.nombre = "Total de hitos";
			temp.sinIniciar = Double.valueOf(df2.format(totalSinIniciar));
			temp.completadas = Double.valueOf(df2.format(totalCompletadas));
			temp.retrasadas = Double.valueOf(df2.format(totalRetrasadas));
			temp.esperadasfinanio = Double.valueOf(df2.format(totalEsperadasAnio));
			temp.aniosSiguientes = Double.valueOf(df2.format(totalAniosSiguientes));
			temp.tipo = 2;
			
			listaResult.add(temp);
			
			resultado = new stElementoResult();
			resultado.listaResultCantidad = listaResultCantidad;
			resultado.listaResult = listaResult;
			resultado.total = totalHitos;
		}
		}catch(Exception e){
		    CLogger.write("6", SAvanceActividades.class, e);
		}
		return resultado;
	}
	
	private stElementoResult getAvanceProductos(Integer idPrestamo, String fechaCorte, String usuario,String lineaBase){
		stElementoResult resultado = null;
		double  totalActividades = 0;
		double  totalSinIniciar = 0;
		double  totalEnProceso = 0;
		double  totalCompletadas = 0;
		double  totalRetrasadas = 0;
		Date inicio = new Date();
		Date fin = new Date();
		Date Corte = new Date();
		List<stAvance> listaResult = new ArrayList<stAvance>();
		stAvance temp = new stAvance();
		
		DecimalFormat df2 = new DecimalFormat("###.##");
		
		try{			
			List<Producto> productos = ProductoDAO.getProductosPorProyecto(idPrestamo, null, lineaBase);
			double totalEsperadasAnio = 0;
			double totalAniosSiguientes = 0;
			DateTime anioCorte = new DateTime();
			
			if(productos != null  && productos.size() > 0){
				listaResult = new ArrayList<stAvance>();
				
				for(Producto producto : productos){
					totalSinIniciar = 0;
					totalEnProceso = 0;
					totalRetrasadas = 0;
					totalCompletadas = 0;
					totalEsperadasAnio = 0;
					totalAniosSiguientes = 0;
					
					List<?> lstActividadesProducto = EstructuraProyectoDAO.getActividadesByTreePath(producto.getTreePath(), idPrestamo, lineaBase);
					List<stActividad> actividades = new ArrayList<stActividad>();
					if (lstActividadesProducto != null){
						stActividad tempActividad = null;
						for (Object actividad : lstActividadesProducto){
							tempActividad = new stActividad();
							Object[] obj = (Object[])actividad;
							tempActividad.id = (Integer)obj[0];
							tempActividad.nombre = (String)obj[1];
							tempActividad.fechaInicio = Utils.formatDate((Date)obj[2]);
							tempActividad.fechaFin = Utils.formatDate((Date)obj[3]);
							tempActividad.porcentajeAvance = (Integer)obj[4];
							
							AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(tempActividad.id, 5, "R", lineaBase); 
							tempActividad.responsable = asignacion != null ? ((asignacion != null ? asignacion.getColaborador().getPnombre() : null) + " " + (asignacion != null ? asignacion.getColaborador().getPapellido() : null)) : null;
							actividades.add(tempActividad);
						}
					}
					
					if(actividades != null && actividades.size() != 0){
						totalActividades = actividades.size();
						
						for (stActividad actividad : actividades){
							
							inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
									.parse(actividad.fechaInicio);
							
							fin =  new SimpleDateFormat("dd/MM/yyyy", Locale.US)
									.parse(actividad.fechaFin);
							
							DateTime nuevaFechaFin = new DateTime(fin);
							nuevaFechaFin = nuevaFechaFin.plusHours(23);
							nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
							nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
							fin = nuevaFechaFin.toDate();
							
							Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
				                    .parse(fechaCorte);
							
							DateTime nuevaFechaCorte = new DateTime(Corte);
							nuevaFechaCorte = nuevaFechaCorte.plusHours(23);
							nuevaFechaCorte = nuevaFechaCorte.plusMinutes(59);
							nuevaFechaCorte = nuevaFechaCorte.plusSeconds(59);
							Corte = nuevaFechaCorte.toDate();
							
							anioCorte = new DateTime(Corte);
							Integer anio = anioCorte.getYear();
							
							DateTime anioInicio = new DateTime(anio,1,1,0,0,0);
							DateTime anioFin = new DateTime(anio,12,31,23,59,59);
							
							if(actividad.porcentajeAvance >= 0 && actividad.porcentajeAvance < 100 && inicio.after(anioInicio.toDate()) && inicio.before(anioFin.toDate())){
								totalEsperadasAnio++;
							}
							
							if (actividad.porcentajeAvance == 0 && (!Corte.after(fin))){
								totalSinIniciar++;
							}else if(Corte.after(inicio) && Corte.before(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100){
								totalEnProceso++;
							}else if (Corte.after(fin) && actividad.porcentajeAvance >= 0 && actividad.porcentajeAvance < 100 ){
								totalRetrasadas++;
							}else if(actividad.porcentajeAvance == 100){
								totalCompletadas++;
							}
							
							if(inicio.after(anioFin.toDate())){
								totalAniosSiguientes++;
							}
						}
												
						if (Double.isNaN(totalSinIniciar)){
							totalSinIniciar = 0;
						}
						
						if (Double.isNaN(totalEnProceso)){
							totalEnProceso = 0;
						}
						
						if (Double.isNaN(totalCompletadas)){
							totalCompletadas = 0;
						}
						
						if (Double.isNaN(totalRetrasadas)){
							totalRetrasadas = 0;
						}
						
						if (Double.isNaN(totalEsperadasAnio)){
							totalEsperadasAnio = 0;
						}
						
						if (Double.isNaN(totalActividades)){
							totalActividades = 0;
						}
						
						if(Double.isNaN(totalEsperadasAnio)){
							totalEsperadasAnio = 0;
						}
						
						if(Double.isNaN(totalActividades)){
							totalActividades = 0;
						}
						
						if(Double.isNaN(totalAniosSiguientes)){
							totalAniosSiguientes = 0;
						}
						
						temp = new stAvance();
						temp.objetoId = producto.getId();
						temp.objetoTipo = 3;
						temp.nombre = producto.getNombre();
						temp.sinIniciar = Double.valueOf(df2.format(totalSinIniciar));
						temp.proceso = Double.valueOf(df2.format(totalEnProceso));
						temp.completadas = Double.valueOf(df2.format(totalCompletadas));
						temp.retrasadas = Double.valueOf(df2.format(totalRetrasadas));
						temp.esperadasfinanio = Double.valueOf(df2.format(totalEsperadasAnio));
						temp.aniosSiguientes = Double.valueOf(df2.format(totalAniosSiguientes));
						
						temp.tipo = 2;
						
						listaResult.add(temp);
					}
				}
				
				resultado = new stElementoResult();
				resultado.listaResult = listaResult;
				resultado.total = listaResult.size();
			}
		}catch(Exception e){
		    CLogger.write("7", SAvanceActividades.class, e);
		}
		return resultado;
	}
	
	private byte[] exportarExcel(Integer idPrestamo, String fechaCorte, String usuario,String lineaBase) {
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamo, fechaCorte, usuario,lineaBase);
			excel = new CExcel("Reporte de Avance", false, null);
			Proyecto proyecto = ProyectoDAO.getProyecto(idPrestamo);
			wb=excel.generateExcelOfData(datos, "Reporte de Avance de Actividades e Hitos - "+proyecto.getNombre(), headers, null, true, usuario);
			wb.write(outByteStream);
			outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
		    CLogger.write("10", SAvanceActividades.class, e);
		}
		return outArray;
	}
	
	private byte[] exportarDetalleExcel(Integer id, Integer tipo, String fechaCorte, String usuario, String lineaBase) {
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeadersDetalle();
			datos = generarDatosDetalle(id, tipo, fechaCorte, usuario, lineaBase);
			excel = new CExcel(tipo == 1 ? "Detalle Avance de Actividades de pep" : "Detalle Avance de Actividades de Producto", false, null);
			if(tipo==1){
				Proyecto proyecto = ProyectoDAO.getProyecto(id);
				wb=excel.generateExcelOfData(datos, tipo == 1 ? "Detalle Avance de Actividades de pep - " : "Detalle Avance de Actividades de Producto - " + proyecto.getNombre(), headers, null, true, usuario);
			}else if(tipo == 3){
				Producto producto = ProductoDAO.getProductoPorId(id);
				String nombrePep = producto.getComponente() != null ? producto.getComponente().getProyecto().getNombre() : producto.getSubcomponente().getComponente().getProyecto().getNombre();
				wb=excel.generateExcelOfData(datos, tipo == 1 ? "Detalle Avance de Actividades de pep - " : "Detalle Avance de Actividades de Producto - " + nombrePep, headers, null, true, usuario);
			}
			
			
			wb.write(outByteStream);
			outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
		    CLogger.write("10", SAvanceActividades.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Estado", "Completadas", "Sin Iniciar", "En Proceso", "Retrasadas","Esperadas fin de año", "Años siguientes"},  //titulos
			null, //mapeo
			{"string", "string", "string", "string", "string", "string", "string", "string"}, //tipo dato
			{"", "", "", "", "", "", "", ""}, //operaciones columnas
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
			{"Nombre", "Fecha Inicial", "Fecha Final", "Fecha Inicial Real", "Fecha Fin Real", "Avance %","Estado", "Responsable" , "Descripción"},  //titulos
			null, //mapeo
			{"string", "string", "string", "string", "string", "int", "string", "string", "string"}, //tipo dato
			{"", "", "", "", "", "", "", "", ""}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatos(Integer idPrestamo, String fechaCorte, String usuario,String lineaBase){
		String[][] datos = null;
		Integer totalesdeActividades = 0;
		Integer totalActividades = 0;
		Integer totalesHitos = 0;
		Integer totalHitos = 0;
		Integer totalProductos = 0;
		
		stElementoResult avanceActividades = getAvanceActividadesPrestamo(idPrestamo, fechaCorte, usuario,lineaBase);
		stElementoResult avanceHitos = getAvanceHitos(idPrestamo, fechaCorte, usuario,lineaBase);
		stElementoResult avanceProductos = getAvanceProductos(idPrestamo, fechaCorte, usuario,lineaBase);
		if(avanceActividades!=null){
			totalesdeActividades = (int)avanceActividades.total;
			totalActividades = avanceActividades.listaResult.size();
		}
		if(avanceHitos!=null){
			totalesHitos = (int)avanceHitos.total;
			totalHitos = avanceHitos.listaResult.size();
		}
		if(avanceProductos!=null){
			totalProductos = avanceProductos.listaResult.size();
		}
		
		datos = new String[totalActividades+totalHitos+totalProductos+6+2][8];

		Integer fila = 1;
		
		datos[0][0] = "Actividades";
		datos[0][1] = datos[0][2] = datos[0][3] = datos[0][4] = datos[0][5] = datos[0][6] = datos[0][7] = "";
		for(int i=0; i<totalActividades;i++){
			datos[fila][0] = "    "+avanceActividades.listaResult.get(i).nombre;
			datos[fila][1] = "";
			datos[fila][2] = String.valueOf(avanceActividades.listaResult.get(i).completadas)+"%";
			datos[fila][3] = String.valueOf(avanceActividades.listaResult.get(i).sinIniciar)+"%";
			datos[fila][4] = String.valueOf(avanceActividades.listaResult.get(i).proceso)+"%";
			datos[fila][5] = String.valueOf(avanceActividades.listaResult.get(i).retrasadas)+"%";
			datos[fila][6] = String.valueOf(avanceActividades.listaResult.get(i).esperadasfinanio)+"%";
			datos[fila][7] = String.valueOf(avanceActividades.listaResult.get(i).aniosSiguientes)+"%";
			fila++;
		}
		datos[fila][0]="Total de Actividades: "+totalesdeActividades;
		datos[fila][1]="";
		datos[fila][2] = avanceActividades!=null ? String.valueOf((int)avanceActividades.listaResultCantidad.get(0).completadas) : "0";
		datos[fila][3] = avanceActividades!=null ? String.valueOf((int)avanceActividades.listaResultCantidad.get(0).sinIniciar) : "0";
		datos[fila][4] = avanceActividades!=null ? String.valueOf((int)avanceActividades.listaResultCantidad.get(0).proceso) : "0";
		datos[fila][5] = avanceActividades!=null ? String.valueOf((int)avanceActividades.listaResultCantidad.get(0).retrasadas) : "0";
		datos[fila][6] = avanceActividades!=null ? String.valueOf((int)avanceActividades.listaResultCantidad.get(0).esperadasfinanio) : "0";
		datos[fila][7] = avanceActividades!=null ? String.valueOf((int)avanceActividades.listaResultCantidad.get(0).aniosiguientes) : "0";
		fila++;
		fila++;
		
		datos[fila][0] = "Hitos";
		datos[fila][1] = datos[fila][2] = datos[fila][3] = datos[fila][4] = datos[fila][5] = datos[fila][7] = datos[fila][7] = "";
		fila++;
		for(int i=0; i<totalHitos;i++){
			datos[fila][0] = "    "+avanceHitos.listaResult.get(i).nombre;
			datos[fila][1] = "";
			datos[fila][2] = String.valueOf(avanceHitos.listaResult.get(i).completadas)+"%";
			datos[fila][3] = String.valueOf(avanceHitos.listaResult.get(i).sinIniciar)+"%";
			datos[fila][4] = "";
			datos[fila][5] = String.valueOf(avanceHitos.listaResult.get(i).retrasadas)+"%";
			datos[fila][6] = String.valueOf(avanceHitos.listaResult.get(i).esperadasfinanio)+"%";
			datos[fila][7] = String.valueOf(avanceHitos.listaResult.get(i).aniosSiguientes)+"%";
			fila++;
		}
		datos[fila][0]="Total de Hitos: "+totalesHitos;
		datos[fila][1]="";
		datos[fila][2] = avanceHitos!=null ? String.valueOf((int)avanceHitos.listaResultCantidad.get(0).completadas) : "0";
		datos[fila][3] = avanceHitos!=null ? String.valueOf((int)avanceHitos.listaResultCantidad.get(0).sinIniciar) : "0";
		datos[fila][4] = String.valueOf(0);
		datos[fila][5] = avanceHitos!=null ? String.valueOf((int)avanceHitos.listaResultCantidad.get(0).retrasadas) : "0";
		datos[fila][6] = avanceHitos!=null ? String.valueOf((int)avanceHitos.listaResultCantidad.get(0).esperadasfinanio) : "0";
		datos[fila][7] = avanceHitos!=null ? String.valueOf((int)avanceHitos.listaResultCantidad.get(0).esperadasfinanio) : "0";
		fila++;
		fila++;
		
		datos[fila][0] = "Productos";
		datos[fila][1] = datos[fila][2] = datos[fila][3] = datos[fila][4] = datos[fila][5] = datos[fila][6] = datos[fila][7] = "";
		fila++;
		for(int i=0; i<totalProductos;i++){
			datos[fila][0] = "    "+avanceProductos.listaResult.get(i).nombre;
			datos[fila][1] = "";
			datos[fila][2] = String.valueOf((int)avanceProductos.listaResult.get(i).completadas);
			datos[fila][3] = String.valueOf((int)avanceProductos.listaResult.get(i).sinIniciar);
			datos[fila][4] = String.valueOf((int)avanceProductos.listaResult.get(i).proceso);
			datos[fila][5] = String.valueOf((int)avanceProductos.listaResult.get(i).retrasadas);
			datos[fila][6] = String.valueOf((int)avanceProductos.listaResult.get(i).esperadasfinanio);
			datos[fila][7] = String.valueOf((int)avanceProductos.listaResult.get(i).aniosSiguientes);
			fila++;
		}
		datos[fila][0]="Total de Productos: "+totalProductos;
		datos[fila][1]="";
		datos[fila][2] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).completadas) : "0";
		datos[fila][3] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).sinIniciar) : "0";
		datos[fila][4] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).proceso) : "0";
		datos[fila][5] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).retrasadas) : "0";
		datos[fila][6] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).esperadasfinanio) : "0";
		datos[fila][7] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).aniosiguientes) : "0";
		fila++;
		
		return datos;
	}
	
	public String[][] generarDatosDetalle(Integer id, Integer tipo, String fechaCorte, String usuario, String lineaBase){
		String[][] datos = null;
		
		try{
			List<?> actividades = null;
			
			if(tipo == 1){
				actividades = EstructuraProyectoDAO.getActividadesProyecto(id, lineaBase);
			}else if(tipo == 3){
				Producto producto = ProductoDAO.getProductoPorId(id);
				actividades = EstructuraProyectoDAO.getActividadesByTreePath(producto.getTreePath(), producto.getComponente() != null ? producto.getComponente().getProyecto().getId() : producto.getSubcomponente().getComponente().getProyecto().getId(), lineaBase);
			}
			
			if (actividades != null && !actividades.isEmpty()){
				datos = new String[actividades.size()][9];
				for(int i=0; i < actividades.size(); i++){
					Object[] objActividad = (Object[])actividades.get(i);
					datos[i][0] = (String)objActividad[1];					
					datos[i][1] = Utils.formatDate(tipo == 1 ? (Date)objActividad[5] : (Date)objActividad[2]);
					datos[i][2] = Utils.formatDate(tipo == 1 ? (Date)objActividad[6] : (Date)objActividad[3]);
					datos[i][3] = Utils.formatDate(tipo == 1 ? (Date)objActividad[13] : (Date)objActividad[5]);
					datos[i][4] = Utils.formatDate(tipo == 1 ? (Date)objActividad[14] : (Date)objActividad[6]);
					datos[i][5] = (tipo == 1 ? ((Integer)objActividad[12]).toString() : ((Integer)objActividad[4]).toString());
					
					Date inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(Utils.formatDate(tipo == 1 ? (Date)objActividad[5] : (Date)objActividad[2]));
					
					Date fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(Utils.formatDate(tipo == 1 ? (Date)objActividad[6] : (Date)objActividad[3]));
					
					DateTime nuevaFechaFin = new DateTime(fin);
					nuevaFechaFin = nuevaFechaFin.plusHours(23);
					nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
					nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
					fin = nuevaFechaFin.toDate();
					
					Date Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(fechaCorte);
					
					DateTime nuevaFechaCorte = new DateTime(Corte);
					nuevaFechaFin = nuevaFechaFin.plusHours(23);
					nuevaFechaFin = nuevaFechaFin.plusMinutes(59);
					nuevaFechaFin = nuevaFechaFin.plusSeconds(59);
					Corte = nuevaFechaCorte.toDate();
					
					if ((tipo== 1 ? (Integer)objActividad[12] : (Integer)objActividad[4]) == 0 && (!Corte.after(fin))){
						datos[i][6] = "Sin iniciar";
					}else if(Corte.after(inicio) && Corte.before(fin) && (tipo == 1 ? (Integer)objActividad[12] : (Integer)objActividad[4]) > 0 && (tipo == 1 ? (Integer)objActividad[12] : (Integer)objActividad[4]) < 100){
						datos[i][6] = "En proceso";
					}else if (Corte.after(fin) && (tipo == 1 ? (Integer)objActividad[12] : (Integer)objActividad[4]) >= 0 && (tipo == 1 ? (Integer)objActividad[12] : (Integer)objActividad[4]) < 100 ){
						datos[i][6] = "Retrasado";
					}else if((tipo == 1 ? (Integer)objActividad[12] : (Integer)objActividad[4]) == 100){
						datos[i][6] = "Completada";
					}

					AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea((Integer)objActividad[0], 5, "R", lineaBase); 
					datos[i][7] = asignacion != null ? ((asignacion != null ? asignacion.getColaborador().getPnombre() : null) + " " + (asignacion != null ? asignacion.getColaborador().getPapellido() : null)) : null;
					datos[i][8] = tipo == 1 ? (String)objActividad[15] : objActividad[7] != null ? (String)objActividad[7] : "";
				}
			}
		}catch(Exception e){
			
		}
		
		return datos;
	}
}
