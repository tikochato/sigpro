package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Subproducto;
import utilities.Utils;


@WebServlet("/SPorcentajeActividades")
public class SPorcentajeActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_SUBACTIVIDAD = 5;
	
       
    
    public SPorcentajeActividades() {
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
		
		String items = "";
		
		if(accion.equals("getKanban")){
			items = "";
			Integer proyectoId = Utils.String2Int(map.get("proyecto_id"),0);
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
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
							items = ObtenerActividades(actividad,usuario, items);
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){						
						items = ObtenerActividades(actividad,usuario, items);
					}

				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					items = ObtenerActividades(actividad,usuario, items);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){				
				items = ObtenerActividades(actividad,usuario, items);
			}
			
			items = String.join("","{\"items\" : [", items,"]}");
		}else if(accion.equals("getKanbanComponente")){
			items = "";
			Integer componenteId = Utils.String2Int(map.get("componente_id"),0);
			List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componenteId,
					null, null, null, null, null, usuario);
			for (Producto producto : productos){
				List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
						null, null, null, null, null, usuario);
				for (Subproducto subproducto : subproductos){
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){
						items = ObtenerActividades(actividad,usuario, items);
					}
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					items = ObtenerActividades(actividad,usuario, items);
				}

			}
			
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componenteId, OBJETO_ID_COMPONENTE,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				items = ObtenerActividades(actividad,usuario, items);
			}
			
			items = String.join("","{\"items\" : [", items,"]}");
		}else if(accion.equals("getKanbanProducto")){
			Integer productoId = Utils.String2Int(map.get("producto_id"),0);
			List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, productoId,
					null, null, null, null, null, usuario);
			for (Subproducto subproducto : subproductos){
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					items = ObtenerActividades(actividad,usuario, items);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, productoId, OBJETO_ID_PRODUCTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				items = ObtenerActividades(actividad,usuario, items);
			}
			items = String.join("","{\"items\" : [", items,"]}");
		}

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");


        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(items.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private String ObtenerActividades(Actividad actividad, String usuario, String items){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_SUBACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		for(Actividad subActividad : actividades){
			items = ObtenerActividades(subActividad, usuario, items);
		}
		
		return String.join(items.length()>0 ? "," : "", items,
					construirItemKanban("(" + actividad.getId() + ") "+ actividad.getNombre(), actividad.getPorcentajeAvance(), actividad.getFechaFin()));
	}

	private String construirItemKanban(String nombre, Integer porcentaje,Date fechaFin){
		Integer estado = 0;
		Date hoy = new Date();
		if (fechaFin.after(hoy) ){
			if (porcentaje == 0){
				estado = 0;
			}else if (porcentaje >0 && porcentaje < 100){
				estado = 1;
			}else if (porcentaje == 100){
				estado = 2;
			}
		}else{
			if (porcentaje == 100){
				estado = 2;
			}else{
				estado = 3;
			}
		}
		
		return String.join("", "{\"name\": \"", nombre,"\", \"estadoId\" : " ,estado.toString(),
				",\"percentageValue\": \"",porcentaje.toString(),"%\"",
				"}");
	
	}

}
