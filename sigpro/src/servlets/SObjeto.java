package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

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
import dao.SubComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subcomponente;
import pojo.Subproducto;
import utilities.Utils;

/**
 * Servlet implementation class SActividad
 */
@WebServlet("/SObjeto")
public class SObjeto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stactividad{
		Integer id;
		String nombre;
		String tiponombre;
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SObjeto() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
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
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		if(accion.equals("getObjetoPorId")){
			Integer objetoId= Utils.getParameterInteger(map, "id");
			Integer objetoTipo= Utils.getParameterInteger(map, "tipo");
			String nombre="";
			String tiponombre = "";
			String fechaInicio = "";
			if(objetoTipo!=null){
				switch(objetoTipo){
					case 0: //Proyecto;
						tiponombre = "Proyecto";
						Proyecto proyecto = ProyectoDAO.getProyectoPorId(objetoId,usuario);
						nombre = (proyecto!=null) ? proyecto.getNombre() : "";
						fechaInicio = (proyecto!=null) ? Utils.formatDate(proyecto.getFechaInicio()) : "";
						break;
					case 1: //Componente;
						tiponombre = "Componente";
						Componente componente = ComponenteDAO.getComponentePorId(objetoId,usuario);
						nombre = (componente!=null) ? componente.getNombre() : "";
						fechaInicio = (componente!=null) ? Utils.formatDate(componente.getFechaInicio()) : "";
						break;
					case 2: //Subcomponente;
						tiponombre = "Subcomponente";
						Subcomponente subcomponente = SubComponenteDAO.getSubComponentePorId(objetoId,usuario);
						nombre = (subcomponente!=null) ? subcomponente.getNombre() : "";
						fechaInicio = (subcomponente!=null) ? Utils.formatDate(subcomponente.getFechaInicio()) : "";
						break;
					case 3: //Producto
						tiponombre = "Producto";
						Producto producto = ProductoDAO.getProductoPorId(objetoId,usuario);
						nombre = (producto!=null) ? producto.getNombre() : "";
						fechaInicio = (producto!=null) ? Utils.formatDate(producto.getFechaInicio()) : "";
						break;
					case 4: //Subproducto
						tiponombre = "Subproducto";
						Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId,usuario);
						nombre = (subproducto!=null) ? subproducto.getNombre() : "";
						fechaInicio = (subproducto!=null) ? Utils.formatDate(subproducto.getFechaInicio()) : "";
						break;
					case 5: //Actividad
						tiponombre = "Actividad";
						Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
						nombre = (actividad!=null) ? actividad.getNombre() : "";
						fechaInicio = (actividad!=null) ? Utils.formatDate(actividad.getFechaInicio()) : "";
						break;
				}
			}
			response_text = String.join("", "{\"success\":true, \"nombre\":\"", nombre ,"\", \"tiponombre\":\"",tiponombre,"\" , \"fechaInicio\" : \"", fechaInicio, "\"}");
		}
		else{
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
}
