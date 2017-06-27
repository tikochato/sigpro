package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Subproducto;
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
		
		if (accion.equals("getAvance")){
			List<Actividad> actividades = getActividadesProyecto(idPrestamo, usuario);
			
			for (Actividad actividad : actividades){
				
			}
		}
	}
	
	private List<Actividad> getActividadesProyecto(int idPrestamo, String usuario){
		List<Actividad> result = new ArrayList<Actividad>();
		
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
			
			//items = String.join("","{\"items\" : [", items,"]}");
			
			return result;
		}
		catch(Throwable e){
			CLogger.write("1", SReporte.class, e);
			return null;
		}
	}
	
	private List<Actividad> ObtenerActividades(Actividad actividad, String usuario, List<Actividad> items){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_SUBACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		for(Actividad subActividad : actividades){
			items = ObtenerActividades(subActividad, usuario, items);
		}
		
		return items;
	}
}
