package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.AsignacionRaciDAO;
import dao.ComponenteDAO;
import dao.HitoDAO;
import dao.HitoResultadoDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.AsignacionRaci;
import pojo.Componente;
import pojo.Hito;
import pojo.HitoResultado;
import pojo.Producto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SAvanceActividades")
public class SAvanceActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_SUBACTIVIDAD = 5;
    
	class stActividad{
		int id;
		int porcentajeAvance;
		String nombre;
		String responsable;
		String fechaInicio;
		String fechaFin;
	}
	
	class stCantidad{
		double sinIniciar;
		double proceso;
		double completadas;
		double retrasadas;
	}
	
	class stAvance{
		Integer objetoId;
		Integer objetoTipo;
		String nombre;
		double sinIniciar;
		double proceso;
		double completadas;
		double retrasadas;
		int tipo;
	}
	
	class stelementosActividadesAvance{
		Integer id;
		String nombre;
		String fechaInicial;
		String fechaFinal;
		Integer avance;
		String responsable;
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
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
		String fechaCorte = map.get("fechaCorte");
		if (accion.equals("getAvance")){
			
			try{
				stElementoResult avanceActividades = getAvanceActividades(idPrestamo, fechaCorte, usuario);
					
				if(avanceActividades != null){
					response_text = new GsonBuilder().serializeNulls().create().toJson(avanceActividades.listaResult);
					response_text = String.join("", ",\"actividades\":",response_text);
					String cantidades = new GsonBuilder().serializeNulls().create().toJson(avanceActividades.listaResultCantidad);
					response_text += String.join("", ",\"cantidadesActividades\":",cantidades);
					response_text += String.join("", ",\"totalActividades\":" + avanceActividades.total,response_text);
				}
				
				stElementoResult avanceHitos = getAvanceHitos(idPrestamo, fechaCorte, usuario);
				
				if(avanceHitos != null){
					String cantidades = new GsonBuilder().serializeNulls().create().toJson(avanceHitos.listaResultCantidad);
					response_text += String.join("", ",\"cantidadHitos\":",cantidades);
					String response_hitos = new GsonBuilder().serializeNulls().create().toJson(avanceHitos.listaResult);
					response_text += String.join("", ",\"hitos\":",response_hitos);
					response_text = String.join("", ",\"totalHitos\":" + avanceHitos.total,response_text);
				}
				
				stElementoResult avanceProductos = getAvanceProductos(idPrestamo, fechaCorte, usuario);
				
				if(avanceProductos != null){
					String response_productos = new GsonBuilder().serializeNulls().create().toJson(avanceProductos.listaResult);
					response_text += String.join("", ",\"productos\":",response_productos);
					response_text = String.join("", ",\"totalProductos\":" + avanceProductos.total,response_text);
				}
			}catch (Throwable e) {
				e.printStackTrace();
		    }
			
			response_text = String.join("", "{\"success\":true ", response_text, "}");
		}else if(accion.equals("getActividadesProyecto")){
			List<stActividad> actividades = getActividadesProyecto(idPrestamo, usuario);
			List<stelementosActividadesAvance> lstElementosActividadesAvance = new ArrayList<stelementosActividadesAvance>();
			for(stActividad actividad : actividades){
				stelementosActividadesAvance temp = new stelementosActividadesAvance();
				temp.id = actividad.id;
				temp.nombre = actividad.nombre;
				temp.fechaInicial = actividad.fechaInicio;
				temp.fechaFinal = actividad.fechaFin;
				temp.avance = actividad.porcentajeAvance;
				temp.responsable = actividad.responsable;
				lstElementosActividadesAvance.add(temp);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(lstElementosActividadesAvance);
			response_text = String.join("", ",\"items\":",response_text);
			response_text = String.join("", "{\"success\":true ", response_text, "}");
		}else if(accion.equals("getHitos")){
			Date Corte = new Date();
			List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, idPrestamo, null, null, null, null, null);
			List<stelementosActividadesAvance> lstElementosActividadesAvance = new ArrayList<stelementosActividadesAvance>();
			
			for(Hito hito : hitos){
				stelementosActividadesAvance temp = null;
				HitoResultado hitoResultado = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				Date fechaHito = new Date();
				try{	
					fechaHito = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
							.parse(Utils.formatDate(hito.getFecha()));
					if(hitoResultado != null){					
							if((Corte.before(fechaHito) && hitoResultado.getValorEntero() == 0) || (Corte.after(fechaHito) && hitoResultado.getValorEntero() == 0)){
								temp = new stelementosActividadesAvance();
								temp.id = hito.getId();
								temp.nombre = hito.getNombre();
								temp.avance = 0;
								temp.fechaInicial = Utils.formatDate(hito.getFecha());
								lstElementosActividadesAvance.add(temp);
							}else if(Corte.after(fechaHito) && hitoResultado.getValorEntero() == 1){
								temp = new stelementosActividadesAvance();
								temp.id = hito.getId();
								temp.nombre = hito.getNombre();
								temp.avance = 100;
								temp.fechaFinal = Utils.formatDate(hito.getFecha());
								lstElementosActividadesAvance.add(temp);
							}
					}else{
						if (Corte.after(fechaHito)){
							temp = new stelementosActividadesAvance();
							temp.id = hito.getId();
							temp.nombre = hito.getNombre();
							temp.avance = 0;
							temp.fechaInicial = Utils.formatDate(hito.getFecha());
							lstElementosActividadesAvance.add(temp);
						}
					}
				}catch (Throwable e) {
					e.printStackTrace();
			    }
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(lstElementosActividadesAvance);
			response_text = String.join("", ",\"items\":",response_text);
			response_text = String.join("", "{\"success\":true ", response_text, "}");
		}else if(accion.equals("getActividadesProducto")){
			Integer productoId = Utils.String2Int(map.get("productoId"));
			Producto producto = ProductoDAO.getProductoPorId(productoId);
			List<stActividad> actividades = getActividadesProducto(producto, usuario);
			List<stelementosActividadesAvance> lstElementosActividadesAvance = new ArrayList<stelementosActividadesAvance>();
			for(stActividad actividad : actividades){
				stelementosActividadesAvance temp = new stelementosActividadesAvance();
				temp.id = actividad.id;
				temp.nombre = actividad.nombre;
				temp.fechaInicial = actividad.fechaInicio;
				temp.fechaFinal = actividad.fechaFin;
				temp.avance = actividad.porcentajeAvance;
				temp.responsable = actividad.responsable;
				lstElementosActividadesAvance.add(temp);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(lstElementosActividadesAvance);
			response_text = String.join("", ",\"items\":",response_text);
			response_text = String.join("", "{\"success\":true ", response_text, "}");
		}else if (accion.equals("exportarExcel")){
			try{
		        byte [] outArray = exportarExcel(idPrestamo, fechaCorte, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; ReporteAvances_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SAvanceActividades.class, e);
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
	
	private stElementoResult getAvanceActividades(Integer idPrestamo, String fechaCorte, String usuario){
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
		List<stCantidad> listaResultCantidad = new ArrayList<stCantidad>();
		stAvance temp = new stAvance();
		stCantidad tCantidad = new stCantidad();
		
		DecimalFormat df2 = new DecimalFormat("###.##");
		
		try{
			List<stActividad> actividades = getActividadesProyecto(idPrestamo, usuario);
			if (actividades != null){

				totalActividades = actividades.size();
			
				for (stActividad actividad : actividades){
					
					inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(actividad.fechaInicio);
					
					fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(actividad.fechaFin);
					
					Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
		                    .parse(fechaCorte);
					
					if (actividad.porcentajeAvance == 0){
						totalSinIniciar++;
					}
					
					if(Corte.after(inicio) && Corte.before(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100){
						totalEnProceso++;
					}
					
					if (Corte.after(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100 ){
						totalRetrasadas++;
					}
					
					if(actividad.porcentajeAvance == 100){
						totalCompletadas++;
					}
				}
				
				tCantidad.sinIniciar = totalSinIniciar;
				tCantidad.proceso = totalEnProceso;
				tCantidad.completadas = totalCompletadas;
				tCantidad.retrasadas = totalRetrasadas;
				
				listaResultCantidad.add(tCantidad);

				totalSinIniciar = (totalSinIniciar/totalActividades)*100;
				totalEnProceso = (totalEnProceso/totalActividades)*100;
				totalCompletadas = (totalCompletadas/totalActividades)*100;
				totalRetrasadas = (totalRetrasadas/totalActividades)*100;
				
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
				
				temp = new stAvance();
				temp.objetoId = idPrestamo;
				temp.objetoTipo = 1;
				temp.nombre = "Total de actividades";
				temp.sinIniciar = Double.valueOf(df2.format(totalSinIniciar));
				temp.proceso = Double.valueOf(df2.format(totalEnProceso));
				temp.completadas = Double.valueOf(df2.format(totalCompletadas));
				temp.retrasadas = Double.valueOf(df2.format(totalRetrasadas));
				temp.tipo = 2;
				
				listaResult.add(temp);
				
				resultado = new stElementoResult();
				resultado.listaResult = listaResult;
				resultado.listaResultCantidad = listaResultCantidad;
				resultado.total = totalActividades;
			}
		}catch(Exception e){
			CLogger.write("2", SAvanceActividades.class, e);
		}
		return resultado;
	}
	
	private stElementoResult getAvanceHitos(Integer idPrestamo, String fechaCorte, String usuario){
		stElementoResult resultado = null;
		double  totalSinIniciar = 0;
		double  totalCompletadas = 0;
		double  totalRetrasadas = 0;
		Date Corte = new Date();
		List<stAvance> listaResult = new ArrayList<stAvance>();
		List<stCantidad> listaResultCantidad = new ArrayList<stCantidad>();
		stAvance temp = new stAvance();
		stCantidad tCantidad = new stCantidad();
		
		DecimalFormat df2 = new DecimalFormat("###.##");
		
		try{
		List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, idPrestamo, null, null, null, null, null);
		totalSinIniciar = 0;
		totalRetrasadas = 0;
		totalCompletadas = 0;
		
		if(hitos != null && hitos.size() > 0){
		
			listaResult = new ArrayList<stAvance>();
			double  totalHitos = 0;
			
			Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
					.parse(fechaCorte);
			
			Date fechaHito = new Date();
			
			for(Hito hito : hitos){
				fechaHito = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
						.parse(Utils.formatDate(hito.getFecha()));
				
				HitoResultado hitoResultado = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				
				if(hitoResultado != null){
					if(Corte.before(fechaHito) && hitoResultado.getValorEntero() == 0){
						totalSinIniciar++;
						totalHitos++;
					}
					
					if(Corte.after(fechaHito) && hitoResultado.getValorEntero() == 0){
						totalRetrasadas++;
						totalHitos++;
					}
					
					if(Corte.after(fechaHito) && hitoResultado.getValorEntero() == 1){
						totalCompletadas++;
						totalHitos++;
					}
				}else{
					if (Corte.before(fechaHito)){
						totalSinIniciar++;
						totalHitos++;
					}
					
					if (Corte.after(fechaHito)){
						totalRetrasadas++;
						totalHitos++;
					}
				}
			}
			
			listaResultCantidad = new ArrayList<stCantidad>();
			tCantidad = new stCantidad();
			
			tCantidad.sinIniciar = totalSinIniciar;
			tCantidad.retrasadas = totalRetrasadas;
			tCantidad.completadas = totalCompletadas;
			
			listaResultCantidad.add(tCantidad);
			
			totalSinIniciar = (totalSinIniciar/totalHitos)*100;
			totalCompletadas = (totalCompletadas/totalHitos)*100;
			totalRetrasadas = (totalRetrasadas/totalHitos)*100;
			
			temp = new stAvance();
			temp.objetoId = idPrestamo;
			temp.objetoTipo = 10;
			temp.nombre = "Total de hitos";
			temp.sinIniciar = Double.valueOf(df2.format(totalSinIniciar));
			temp.completadas = Double.valueOf(df2.format(totalCompletadas));
			temp.retrasadas = Double.valueOf(df2.format(totalRetrasadas));
			temp.tipo = 2;
			
			listaResult.add(temp);
			
			resultado = new stElementoResult();
			resultado.listaResultCantidad = listaResultCantidad;
			resultado.listaResult = listaResult;
			resultado.total = totalHitos;
		}
		}catch(Exception e){
			CLogger.write("3", SAvanceActividades.class, e);
		}
		return resultado;
	}
	
	private stElementoResult getAvanceProductos(Integer idPrestamo, String fechaCorte, String usuario){
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
			List<Producto> productos = ProductoDAO.getProductosPorProyecto(idPrestamo, usuario);
			List<stActividad> actividades = getActividadesProyecto(idPrestamo, usuario);
			if(productos != null  && productos.size() > 0){
				listaResult = new ArrayList<stAvance>();
				int totalProductos = productos.size();
				
				for(Producto producto : productos){
					totalSinIniciar = 0;
					totalEnProceso = 0;
					totalRetrasadas = 0;
					totalCompletadas = 0;
					
					actividades = getActividadesProducto(producto, usuario);
					
					if(actividades != null && actividades.size() != 0){
						totalActividades = actividades.size();
						
						for (stActividad actividad : actividades){
							
							inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
									.parse(actividad.fechaInicio);
							
							fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
									.parse(actividad.fechaFin);
							
							Corte = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
									.parse(fechaCorte);
							
							if (actividad.porcentajeAvance == 0){
								totalSinIniciar++;
							}
							
							if(Corte.after(inicio) && Corte.before(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100){
								totalEnProceso++;
							}
							
							if (Corte.after(fin) && actividad.porcentajeAvance > 0 && actividad.porcentajeAvance < 100 ){
								totalRetrasadas++;
							}
							
							if(actividad.porcentajeAvance == 100){
								totalCompletadas++;
							}
						}
						
						totalSinIniciar = (totalSinIniciar/totalActividades)*100;
						totalEnProceso = (totalEnProceso/totalActividades)*100;
						totalCompletadas = (totalCompletadas/totalActividades)*100;
						totalRetrasadas = (totalRetrasadas/totalActividades)*100;
						
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
						
						temp = new stAvance();
						temp.objetoId = producto.getId();
						temp.objetoTipo = 3;
						temp.nombre = producto.getNombre();
						temp.sinIniciar = Double.valueOf(df2.format(totalSinIniciar));
						temp.proceso = Double.valueOf(df2.format(totalEnProceso));
						temp.completadas = Double.valueOf(df2.format(totalCompletadas));
						temp.retrasadas = Double.valueOf(df2.format(totalRetrasadas));
						temp.tipo = 2;
						
						listaResult.add(temp);
					}else
						totalProductos = 0;
				}
				
				resultado = new stElementoResult();
				resultado.listaResult = listaResult;
				resultado.total = totalProductos;
			}
		}catch(Exception e){
			CLogger.write("4", SAvanceActividades.class, e);
		}
		return resultado;
	}
	
	private List<stActividad> getActividadesProducto(Producto producto, String usuario){
		List<stActividad> result = new ArrayList<stActividad>();
		try{
			List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
					null, null, null, null, null, usuario);
			for (Subproducto subproducto : subproductos){						
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					result = ObtenerActividades(actividad,usuario, result);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){						
				result = ObtenerActividades(actividad,usuario, result);
			}
			return result;
		}catch(Throwable e){
			CLogger.write("1", SReporte.class, e);
			return null;
		}
	}
	
	
	private List<stActividad> getActividadesProyecto(int idPrestamo, String usuario){
		List<stActividad> result = new ArrayList<stActividad>();
		
		try{
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, idPrestamo,
					null, null, null, null, null, usuario);
			for (Componente componente : componentes){
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				for (Producto producto : productos){
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					for (Subproducto subproducto : subproductos){						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null, null, null, null, null, usuario);
						for (Actividad actividad : actividades ){
							result = ObtenerActividades(actividad,usuario, result);
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){						
						result = ObtenerActividades(actividad,usuario, result);
					}
		
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					result = ObtenerActividades(actividad,usuario, result);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, idPrestamo, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){				
				result = ObtenerActividades(actividad,usuario, result);
			}
			
			return result;
		}
		catch(Throwable e){
			CLogger.write("1", SReporte.class, e);
			return null;
		}
	}
	
	private List<stActividad> ObtenerActividades(Actividad actividad, String usuario, List<stActividad> items){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_SUBACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		stActividad temp = new stActividad();
		temp.id = actividad.getId();
		temp.nombre = actividad.getNombre();
		AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(actividad.getId(), 5, "R"); 
		temp.responsable = asignacion.getColaborador().getPnombre() + " " + asignacion.getColaborador().getPapellido();
		temp.porcentajeAvance = actividad.getPorcentajeAvance();
		String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad, usuario).split(";");
		temp.fechaInicio = fechaInicioFin[0];
		temp.fechaFin = fechaInicioFin[1];
		
		items.add(temp);
		
		for(Actividad subActividad : actividades){
			items = ObtenerActividades(subActividad, usuario, items);
		}
		
		return items;
	}
	

	private byte[] exportarExcel(Integer idPrestamo, String fechaCorte, String usuario) {
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamo, fechaCorte, usuario);
			excel = new CExcel("Reporte de Avance", false, null);
			wb=excel.generateExcelOfData(datos, "Reporte de Avance", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("4", SAvanceActividades.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Estado", "Completadas", "Sin Iniciar", "En Proceso", "Retrasadas"},  //titulos
			null, //mapeo
			{"string", "string", "string", "string", "string", "string"}, //tipo dato
			{"", "", "", "", "", ""}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatos(Integer idPrestamo, String fechaCorte, String usuario){
		String[][] datos = null;
		Integer totalActividades = 0;
		Integer totalHitos = 0;
		Integer totalProductos = 0;
		
		stElementoResult avanceActividades = getAvanceActividades(idPrestamo, fechaCorte, usuario);
		stElementoResult avanceHitos = getAvanceHitos(idPrestamo, fechaCorte, usuario);
		stElementoResult avanceProductos = getAvanceProductos(idPrestamo, fechaCorte, usuario);
		if(avanceActividades!=null){
			totalActividades = avanceActividades.listaResult.size();
		}
		if(avanceHitos!=null){
			totalHitos = avanceHitos.listaResult.size();
		}
		if(avanceProductos!=null){
			totalProductos = avanceProductos.listaResult.size();
		}
		
		datos = new String[totalActividades+totalHitos+totalProductos+6+2][6];

		Integer fila = 1;
		
		datos[0][0] = "Actividades";
		datos[0][1] = datos[0][2] = datos[0][3] = datos[0][4] = datos[0][5] = "";
		for(int i=0; i<totalActividades;i++){
			datos[fila][0] = "    "+avanceActividades.listaResult.get(i).nombre;
			datos[fila][1] = "";
			datos[fila][2] = String.valueOf(avanceActividades.listaResult.get(i).completadas)+"%";
			datos[fila][3] = String.valueOf(avanceActividades.listaResult.get(i).sinIniciar)+"%";
			datos[fila][4] = String.valueOf(avanceActividades.listaResult.get(i).proceso)+"%";
			datos[fila][5] = String.valueOf(avanceActividades.listaResult.get(i).retrasadas)+"%";
			fila++;
		}
		datos[fila][0]="Total de Actividades: "+totalActividades;
		datos[fila][1]="";
		datos[fila][2] = avanceActividades!=null ? String.valueOf(avanceActividades.listaResultCantidad.get(0).completadas) : "0";
		datos[fila][3] = avanceActividades!=null ? String.valueOf(avanceActividades.listaResultCantidad.get(0).sinIniciar) : "0";
		datos[fila][4] = avanceActividades!=null ? String.valueOf(avanceActividades.listaResultCantidad.get(0).proceso) : "0";
		datos[fila][5] = avanceActividades!=null ? String.valueOf(avanceActividades.listaResultCantidad.get(0).retrasadas) : "0";
		fila++;
		fila++;
		
		datos[fila][0] = "Hitos";
		datos[fila][1] = datos[fila][2] = datos[fila][3] = datos[fila][4] = datos[fila][5] = "";
		fila++;
		for(int i=0; i<totalHitos;i++){
			datos[fila][0] = "    "+avanceHitos.listaResult.get(i).nombre;
			datos[fila][1] = "";
			datos[fila][2] = String.valueOf(avanceHitos.listaResult.get(i).completadas)+"%";
			datos[fila][3] = String.valueOf(avanceHitos.listaResult.get(i).sinIniciar)+"%";
			datos[fila][4] = "";
			datos[fila][5] = String.valueOf(avanceHitos.listaResult.get(i).retrasadas)+"%";
			fila++;
		}
		datos[fila][0]="Total de Hitos: "+totalHitos;
		datos[fila][1]="";
		datos[fila][2] = avanceHitos!=null ? String.valueOf(avanceHitos.listaResultCantidad.get(0).completadas) : "0";
		datos[fila][3] = avanceHitos!=null ? String.valueOf(avanceHitos.listaResultCantidad.get(0).sinIniciar) : "0";
		datos[fila][4] = String.valueOf(0);
		datos[fila][5] = avanceHitos!=null ? String.valueOf(avanceHitos.listaResultCantidad.get(0).retrasadas) : "0";
		fila++;
		fila++;
		
		datos[fila][0] = "Productos";
		datos[fila][1] = datos[fila][2] = datos[fila][3] = datos[fila][4] = datos[fila][5] = "";
		fila++;
		for(int i=0; i<totalProductos;i++){
			datos[fila][0] = "    "+avanceProductos.listaResult.get(i).nombre;
			datos[fila][1] = "";
			datos[fila][2] = String.valueOf(avanceProductos.listaResult.get(i).completadas)+"%";
			datos[fila][3] = String.valueOf(avanceProductos.listaResult.get(i).sinIniciar)+"%";
			datos[fila][4] = String.valueOf(avanceProductos.listaResult.get(i).proceso)+"%";
			datos[fila][5] = String.valueOf(avanceProductos.listaResult.get(i).retrasadas)+"%";
			fila++;
		}
		datos[fila][0]="Total de Productos: "+totalProductos;
		datos[fila][1]="";
		datos[fila][2] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).completadas) : "0";
		datos[fila][3] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).sinIniciar) : "0";
		datos[fila][4] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).proceso) : "0";
		datos[fila][5] = (avanceProductos!=null && avanceProductos.listaResultCantidad!=null) ? String.valueOf(avanceProductos.listaResultCantidad.get(0).retrasadas) : "0";
		fila++;
		
		return datos;
	}
}
