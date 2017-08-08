package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.AdministracionTransaccionalDAO;
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Subproducto;
import utilities.CMariaDB;
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
		Integer creados;
		Integer actualizados;
		Integer eliminados;
	}
       
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
		if(accion.equals("getComponentes")){
			//List<sttransaccion> lstransaccion = ObtenerProyecto(proyectoId, usuario);
			List<stusuario> lstusuarios = new ArrayList<stusuario>();
			if(CMariaDB.connect()){
				Connection conn = CMariaDB.getConnection();
				List<List<String>> usuarios = AdministracionTransaccionalDAO.obtenerUsuarios(conn);
				for(List<String> user : usuarios){
					stusuario temp = new stusuario();
					temp.usuario = user.get(0);
					temp.creados = Utils.String2Int(user.get(1));
					temp.actualizados = Utils.String2Int(user.get(2));
					temp.eliminados = Utils.String2Int(user.get(3));
					lstusuarios.add(temp);
				}
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstusuarios);
	        response_text = String.join("", "\"usuarios\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");


        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private List<sttransaccion> ObtenerProyecto(Integer idPrestamo, String usuario){
		List<sttransaccion> lstransaccion = new ArrayList<sttransaccion>();
		sttransaccion tempPrestamo = new sttransaccion();
		if(CMariaDB.connect()){
			Connection conn = CMariaDB.getConnection();
			ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
			
			for(Integer componente:componentes){
				Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
				tempPrestamo.objeto_id = objComponente.getId();
				tempPrestamo.nombre = objComponente.getNombre();
				tempPrestamo.objeto_tipo = 2;
				tempPrestamo.nombre_objeto_tipo = "Componente";
				tempPrestamo.usuario_creo = objComponente.getUsuarioCreo();
				tempPrestamo.usuario_actualizo = objComponente.getUsuarioActualizo();
				tempPrestamo.fecha_creacion = Utils.formatDate(objComponente.getFechaCreacion());
				tempPrestamo.fecha_modificacion = Utils.formatDate(objComponente.getFechaActualizacion());
				tempPrestamo.estado = objComponente.getEstado() == 1 ? "Activo" : "Inactivo";
				
				lstransaccion.add(tempPrestamo);							
				ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
				for(Integer producto: productos){
					tempPrestamo = new sttransaccion();
					Producto objProducto = ProductoDAO.getProductoPorId(producto);
					tempPrestamo.objeto_id = objProducto.getId();
					tempPrestamo.nombre = objProducto.getNombre();
					tempPrestamo.objeto_tipo = 3;
					tempPrestamo.nombre_objeto_tipo = "Producto";
					tempPrestamo.usuario_creo = objProducto.getUsuarioCreo();
					tempPrestamo.usuario_actualizo = objProducto.getUsuarioActualizo();
					tempPrestamo.fecha_creacion = Utils.formatDate(objProducto.getFechaCreacion());
					tempPrestamo.fecha_modificacion = Utils.formatDate(objProducto.getFechaActualizacion());
					tempPrestamo.estado = objProducto.getEstado() == 1 ? "Activo" : "Inactivo";
					
					lstransaccion.add(tempPrestamo);
					ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
					for(Integer subproducto: subproductos){
						tempPrestamo = new sttransaccion();
						Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
						tempPrestamo.objeto_id = objSubProducto.getId();
						tempPrestamo.nombre = objSubProducto.getNombre();
						tempPrestamo.objeto_tipo = 4;
						tempPrestamo.nombre_objeto_tipo = "Sub Producto";
						tempPrestamo.usuario_creo = objSubProducto.getUsuarioCreo();
						tempPrestamo.usuario_actualizo = objSubProducto.getUsuarioActualizo();
						tempPrestamo.fecha_creacion = Utils.formatDate(objSubProducto.getFechaCreacion());
						tempPrestamo.fecha_modificacion = Utils.formatDate(objSubProducto.getFechaActualizacion());
						tempPrestamo.estado = objSubProducto.getEstado() == 1 ? "Activo" : "Inactivo";
						
						lstransaccion.add(tempPrestamo);
						//actividades sub producto
						ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),objSubProducto.getId(), conn);
						for(ArrayList<Integer> actividad : actividades){
							tempPrestamo = new sttransaccion();
							Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
							tempPrestamo.objeto_id = objActividad.getId();
							tempPrestamo.nombre = objActividad.getNombre();
							tempPrestamo.objeto_tipo = 5;
							tempPrestamo.nombre_objeto_tipo = "Actividad";
							tempPrestamo.usuario_creo = objActividad.getUsuarioCreo();
							tempPrestamo.usuario_actualizo = objActividad.getUsuarioActualizo();
							tempPrestamo.fecha_creacion = Utils.formatDate(objActividad.getFechaCreacion());
							tempPrestamo.fecha_modificacion = Utils.formatDate(objActividad.getFechaActualizacion());
							tempPrestamo.estado = objActividad.getEstado() == 1 ? "Activo" : "Inactivo";
							
							lstransaccion.add(tempPrestamo);
						}
					}
					
					//actividades producto
					ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
					for(ArrayList<Integer> actividad : actividades){
						tempPrestamo = new sttransaccion();
						Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
						tempPrestamo.objeto_id = objActividad.getId();
						tempPrestamo.nombre = objActividad.getNombre();
						tempPrestamo.objeto_tipo = 5;
						tempPrestamo.nombre_objeto_tipo = "Actividad";
						tempPrestamo.usuario_creo = objActividad.getUsuarioCreo();
						tempPrestamo.usuario_actualizo = objActividad.getUsuarioActualizo();
						tempPrestamo.fecha_creacion = Utils.formatDate(objActividad.getFechaCreacion());
						tempPrestamo.fecha_modificacion = Utils.formatDate(objActividad.getFechaActualizacion());
						tempPrestamo.estado = objActividad.getEstado() == 1 ? "Activo" : "Inactivo";
						
						lstransaccion.add(tempPrestamo);						
					}  
				} 
				//actividades componente
				ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);							
				for(ArrayList<Integer> actividad : actividades){
					tempPrestamo = new sttransaccion();
					Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
					tempPrestamo.objeto_id = objActividad.getId();
					tempPrestamo.nombre = objActividad.getNombre();
					tempPrestamo.objeto_tipo = 5;
					tempPrestamo.nombre_objeto_tipo = "Actividad";
					tempPrestamo.usuario_creo = objActividad.getUsuarioCreo();
					tempPrestamo.usuario_actualizo = objActividad.getUsuarioActualizo();
					tempPrestamo.fecha_creacion = Utils.formatDate(objActividad.getFechaCreacion());
					tempPrestamo.fecha_modificacion = Utils.formatDate(objActividad.getFechaActualizacion());
					tempPrestamo.estado = objActividad.getEstado() == 1 ? "Activo" : "Inactivo";
					
					lstransaccion.add(tempPrestamo);
				} 
			}
			
			
			//actividades prestamo
			ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
			
			for(ArrayList<Integer> actividad : actividades){
				tempPrestamo = new sttransaccion();
				Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
				tempPrestamo.objeto_id = objActividad.getId();
				tempPrestamo.nombre = objActividad.getNombre();
				tempPrestamo.objeto_tipo = 5;
				tempPrestamo.nombre_objeto_tipo = "Actividad";
				tempPrestamo.usuario_creo = objActividad.getUsuarioCreo();
				tempPrestamo.usuario_actualizo = objActividad.getUsuarioActualizo();
				tempPrestamo.fecha_creacion = Utils.formatDate(objActividad.getFechaCreacion());
				tempPrestamo.fecha_modificacion = Utils.formatDate(objActividad.getFechaActualizacion());
				tempPrestamo.estado = objActividad.getEstado() == 1 ? "Activo" : "Inactivo";
				
				lstransaccion.add(tempPrestamo);
			}
			
			CMariaDB.close();
			
		}
		return lstransaccion;
	}

} 
