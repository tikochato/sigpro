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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SInformePresupuesto")
public class SInformePresupuesto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
	
	class stInformePresupuesto{
		int id;
		int idPrestamo;
		int objetoTipo;
		int idObjetoTipo;
		String nombre;
		int posicionArbol;
		int objetoTipoPredecesor;
		int idPredecesor;
		String[] hijo;
		BigDecimal Costo;
		BigDecimal CostoReal;
		String fechaInicio;
		String fechaFin;
		int acumulacionCostos;
		String columnas;
	}

       
    public SInformePresupuesto() {
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

		if(accion.equals("getAdquisicionesPrestamo")){
			List<stInformePresupuesto> prestamo = obtenerProyecto(idPrestamo,usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(prestamo);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("generarInforme")){
			Boolean informeCompleto = Boolean.valueOf(map.get("informeCompleto"));
			String[] columnaNames = map.get("columnaNames").split(",");
				List<stInformePresupuesto> prestamo = obtenerProyecto(idPrestamo,usuario);
				
				List<Map<String, Object>> resultPrestamo = new ArrayList<Map<String, Object>>();
				for (stInformePresupuesto p : prestamo ){
					Map<String, Object> data = new HashMap<String, Object>();
					
					if(informeCompleto){
						data.put("id", p.id);
						data.put("idPrestamo", idPrestamo);
						data.put("objetoTipo", p.objetoTipo);
						data.put("posicionArbol", p.posicionArbol);
						data.put("$$treeLevel", p.posicionArbol -1);
						data.put("idObjetoTipo", p.idObjetoTipo);
						data.put("nombre", p.nombre);
						data.put("idPredecesor", p.idPredecesor);
						data.put("objetoTipoPredecesor", p.objetoTipoPredecesor);
						data.put("Costo", p.Costo);
						data.put("CostoReal", p.CostoReal);
						data.put("fechaInicio", p.fechaInicio);
						data.put("fechaFin", p.fechaFin);
						data.put("hijo", p.hijo);
						data.put("acumulacionCostos", p.objetoTipo == 5 && p.hijo.length == 0 ? 3 : 0);
						data.put("columnas", null);
						data.put("Total", 0);
						
						for (String columna: columnaNames){
							data.put(columna, 0);
							data.put("total" + columna, 0);
						}
						
						resultPrestamo.add(data);
					}else{ 
						
						if(p.objetoTipo != 5){
							data.put("id", p.id);
							data.put("idPrestamo", idPrestamo);
							data.put("objetoTipo", p.objetoTipo);
							data.put("posicionArbol", p.posicionArbol);
							data.put("$$treeLevel", p.posicionArbol -1);
							data.put("idObjetoTipo", p.idObjetoTipo);
							data.put("nombre", p.nombre);
							data.put("idPredecesor", p.idPredecesor);
							data.put("objetoTipoPredecesor", p.objetoTipoPredecesor);
							data.put("Costo", p.Costo);
							data.put("CostoReal", p.CostoReal);
							data.put("fechaInicio", p.fechaInicio);
							data.put("fechaFin", p.fechaFin);
							data.put("hijo", p.hijo);
							data.put("acumulacionCostos", 0);
							data.put("columnas", null);
							data.put("Total", 0);
							
							for (String columna: columnaNames){
								data.put(columna, 0);
								data.put("total" + columna, 0);
							}
							
							resultPrestamo.add(data);
						}else if (p.Costo.compareTo(BigDecimal.ZERO) != 0 || p.CostoReal.compareTo(BigDecimal.ZERO) != 0){
							data.put("id", p.id);
							data.put("idPrestamo", idPrestamo);
							data.put("objetoTipo", p.objetoTipo);
							data.put("posicionArbol", p.posicionArbol);
							data.put("$$treeLevel", p.posicionArbol -1);
							data.put("idObjetoTipo", p.idObjetoTipo);
							data.put("nombre", p.nombre);
							data.put("idPredecesor", p.idPredecesor);
							data.put("objetoTipoPredecesor", p.objetoTipoPredecesor);
							data.put("Costo", p.Costo);
							data.put("CostoReal", p.CostoReal);
							data.put("fechaInicio", p.fechaInicio);
							data.put("fechaFin", p.fechaFin);
							data.put("hijo", p.hijo);
							data.put("acumulacionCostos", p.objetoTipo == 5 && p.hijo.length == 0 ? 3 : 0);
							data.put("columnas", null);
							data.put("Total", 0);
							
							for (String columna: columnaNames){
								data.put(columna, 0);
								data.put("total" + columna, 0);
							}
							
							resultPrestamo.add(data);
						}
					}	
				}

				response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
		        response_text = String.join("", "\"prestamo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("exportarExcel")){
			String data = map.get("data");
			String columnas = map.get("columnas");
			String cabeceras = map.get("cabeceras");
			String[] col = cabeceras.split(",");
			Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
			List<Map<String, String>> datos = gson.fromJson(data, listType);

		    
			String nombreInforme = "Informe Ejecuci�n";
			Map<String,Object[]> reporte = new HashMap<>();
			Object[] obj = new Object[col.length];
			
			for(int i=0; i< col.length;i++){
				obj[i] = col[i];
			}
			
			reporte.put("0", obj);
			
			col = columnas.split(",");
			
			obj = new Object[col.length];
			int fila = 1;
			for(Map<String, String> d : datos){
				for(int i=0; i< col.length;i++){
					if(!col[i].equals("nombre"))
						obj[i] = new BigDecimal(d.get(col[i])).doubleValue();
					else
						obj[i] = d.get(col[i]);
				}
				reporte.put(fila+"",obj);
				fila++;
				obj = new Object[col.length];
			}
			
			exportarExcel(reporte,nombreInforme,usuario,response);
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}	
	
	private void exportarExcel(Map<String,Object[]> datos, String nombreInforme, String usuario, HttpServletResponse response){
		try{
			CExcel excel = new CExcel("Reporte",false);
			String path = excel.ExportarExcel(datos, nombreInforme, usuario);
			File file=new File(path);
			if(file.exists()){
				FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
		        	
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
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; Informe_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		catch(Throwable e){
			CLogger.write("2", SReporte.class, e);
		}
	}

	private List<stInformePresupuesto> obtenerProyecto(int proyectoId, String usuario){
		String[] hijos = null;
		int contadorHijos =0;
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		List<stInformePresupuesto> lstdataEjecutado = new ArrayList<>();
		if (proyecto!=null){
			stInformePresupuesto dataEjecutado = new stInformePresupuesto();
			dataEjecutado.objetoTipo = OBJETO_ID_PROYECTO;
			dataEjecutado.posicionArbol = 1;
			dataEjecutado.idObjetoTipo = proyecto.getId();
			dataEjecutado.nombre = proyecto.getNombre();
			dataEjecutado.idPredecesor = 0;
			dataEjecutado.objetoTipoPredecesor = 0;
			

			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);
			
			hijos = new String[componentes.size()];
			for (Componente componente : componentes){
				hijos[contadorHijos] = componente.getId().toString() + ",2";
				contadorHijos++;
			}
			dataEjecutado.hijo = hijos;
			lstdataEjecutado.add(dataEjecutado);
						
			for (Componente componente : componentes){
				dataEjecutado = new stInformePresupuesto();
				dataEjecutado.objetoTipo = OBJETO_ID_COMPONENTE;
				dataEjecutado.posicionArbol = 2;
				dataEjecutado.idObjetoTipo = componente.getId();
				dataEjecutado.nombre = componente.getNombre();
				dataEjecutado.idPredecesor = proyecto.getId();
				dataEjecutado.objetoTipoPredecesor = 1;
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				
				hijos = new String[productos.size()];
				contadorHijos = 0;
				for (Producto producto : productos){
					hijos[contadorHijos] = producto.getId().toString() + ",3";
					contadorHijos++;
				}
				dataEjecutado.hijo = hijos;
				lstdataEjecutado.add(dataEjecutado);
				
				for (Producto producto : productos){
					dataEjecutado = new stInformePresupuesto();
					dataEjecutado.objetoTipo = OBJETO_ID_PRODUCTO;
					dataEjecutado.posicionArbol = 3;
					dataEjecutado.nombre = producto.getNombre();
					dataEjecutado.idObjetoTipo = producto.getId();
					dataEjecutado.idPredecesor = componente.getId();
					dataEjecutado.objetoTipoPredecesor = 2;
					
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					
					hijos = new String[subproductos.size()];
					contadorHijos = 0;
					for (Subproducto subproducto : subproductos){
						hijos[contadorHijos] = subproducto.getId().toString() + ",4";
						contadorHijos++;
					}
					dataEjecutado.hijo = hijos;
					lstdataEjecutado.add(dataEjecutado);
					
					
					for (Subproducto subproducto : subproductos){
						dataEjecutado = new stInformePresupuesto();
						dataEjecutado.objetoTipo = OBJETO_ID_SUBPRODUCTO;
						dataEjecutado.posicionArbol = 4;
						dataEjecutado.idObjetoTipo = subproducto.getId();
						dataEjecutado.nombre =   subproducto.getNombre();
						dataEjecutado.idPredecesor = producto.getId();
						dataEjecutado.objetoTipoPredecesor = 3;
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null, null, null, null, null, usuario);
						
						
						hijos = new String[actividades.size()];
						contadorHijos = 0;
						for (Actividad actividad : actividades){
							if(actividad.getCosto().compareTo(BigDecimal.ZERO) != 0 && actividad.getCostoReal().compareTo(BigDecimal.ZERO) != 0){
								hijos[contadorHijos] = actividad.getId().toString() + ",5";
								contadorHijos++;
							}
						}
						dataEjecutado.hijo = hijos;
						lstdataEjecutado.add(dataEjecutado);
						
						for (Actividad actividad : actividades ){
							lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado, OBJETO_ID_ACTIVIDAD,subproducto.getId(), OBJETO_ID_SUBPRODUCTO);
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){
						lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado,OBJETO_ID_SUBPRODUCTO,producto.getId(), OBJETO_ID_PRODUCTO);
					}
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado,OBJETO_ID_PRODUCTO, componente.getId(),OBJETO_ID_COMPONENTE);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado,OBJETO_ID_COMPONENTE, proyecto.getId(), OBJETO_ID_PROYECTO);
			}
		}
		
		return lstdataEjecutado;
	}
	
	private List<stInformePresupuesto> ObtenerActividades(Actividad actividad, String usuario, List<stInformePresupuesto> lstdataEjecutado, int posicionArbol, 
			int idPredecesor, int objetoTipoPredecesor){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		if (actividad.getCosto().compareTo(BigDecimal.ZERO) != 0 && actividad.getCostoReal().compareTo(BigDecimal.ZERO) != 0){
			stInformePresupuesto dataEjecutado = new stInformePresupuesto();
			dataEjecutado = new stInformePresupuesto();
			dataEjecutado.objetoTipo = OBJETO_ID_ACTIVIDAD;
			dataEjecutado.posicionArbol = posicionArbol;
			dataEjecutado.idObjetoTipo = actividad.getId();
			dataEjecutado.nombre =   actividad.getNombre();
			dataEjecutado.idPredecesor = idPredecesor;
			dataEjecutado.objetoTipoPredecesor = objetoTipoPredecesor;
			dataEjecutado.Costo = actividad.getCosto() == null ? new BigDecimal(0) : actividad.getCosto();
			dataEjecutado.CostoReal = actividad.getCostoReal() == null ? new BigDecimal(0) : actividad.getCostoReal();
			
			String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad, usuario).split(";");
			dataEjecutado.fechaInicio = fechaInicioFin[0];
			dataEjecutado.fechaFin = fechaInicioFin[1];			
			
			String[] hijos = new String[actividades.size()];
			int contadorHijos = 0;
			for(Actividad subActividad : actividades){
					hijos[contadorHijos] = subActividad.getId().toString() + ",5";
					contadorHijos++;
			}
			dataEjecutado.hijo = hijos;
			lstdataEjecutado.add(dataEjecutado);

			for(Actividad subActividad : actividades){
				lstdataEjecutado = ObtenerActividades(subActividad, usuario, lstdataEjecutado, posicionArbol + 1, actividad.getId(), OBJETO_ID_ACTIVIDAD);
			}
		}
		return lstdataEjecutado;
	}
}