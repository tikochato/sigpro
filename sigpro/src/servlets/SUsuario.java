package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.UsuarioDAO;
import pojo.UsuarioPermiso;
import utilities.Utils;

/**
 * Servlet implementation class SUsuario
 */
@WebServlet("/SUsuario")
public class SUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stusuarioPermiso{
		Integer idPermiso;
		String nombrePermiso;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };


	    Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = "";
		accion = map.get("action");
		if(accion!=null){
			if(accion.compareTo("registroUsuario")==0){
				String nuevousuario = map.get("usuario").toLowerCase();
				String nuevopassword = map.get("password");
				String nuevomail = map.get("mail").toLowerCase();
				HttpSession sesionweb = request.getSession();
				if(nuevousuario!=null && nuevopassword!=null && nuevomail != null){
					if(!UsuarioDAO.existeUsuario(nuevousuario)){
						if(nuevousuario.compareTo("")!=0 && nuevopassword.compareTo("")!=0 && nuevomail.compareTo("")!=0){
							String usuarioCreo =sesionweb.getAttribute("usuario").toString();
							boolean registro = UsuarioDAO.registroUsuario(nuevousuario, nuevomail, nuevopassword,usuarioCreo);
							if(registro){
								String permisosAsignados = map.get("permisos");
								if(permisosAsignados.compareTo("[]")!=0){
									Gson entradaJson = new Gson();
									Type tipo = new TypeToken<List<Integer>>() {}.getType();
									List<Integer> permisos = entradaJson.fromJson(permisosAsignados, tipo);
									boolean asignacion = UsuarioDAO.asignarPermisosUsuario(nuevousuario, permisos, usuarioCreo);
									if(asignacion){
										response.getWriter().write("{ \"success\": true, \"message\":\"Usuario creado y asignación de permisos exitosa\" }");
									}else{
										response.getWriter().write("{ \"success\": true, \"message\":\"Usuario creado, asignación de permisos no exitosa\" }");
									}
								}else{
									response.getWriter().write("{ \"success\": true, \"message\":\"Usuario creado\" }");
								}
								
							}else{
								response.getWriter().write("{ \"success\": false, \"error\":\"Error al registrar nuevo usuario\" }");
							}
						}else{
							response.getWriter().write("{ \"success\": false, \"error\":\"Parametros vacios\" }");
						}
					}else{
						response.getWriter().write("{ \"success\": false, \"error\":\"Ya existe ese usuario\" }");
					}
					
				}else{
					response.getWriter().write("{ \"success\": false, \"error\":\"No se enviaron los parametros deseados\" }");
				}
				
			}else if(accion.compareTo("actualizarPermisos")==0){
				String usuario = map.get("usuario");
				String permisosNuevos = map.get("permisosNuevos");
				String permisosEliminados = map.get("permisosEliminados");
				HttpSession sesionweb = request.getSession();
				if(usuario!=null && permisosNuevos!=null && permisosEliminados!=null){
					boolean asignacion=false;
					boolean eliminacion=false;
					Gson entradaJson = new Gson();
					Type tipo = new TypeToken<List<Integer>>() {}.getType();
					String usuarioActualizo =sesionweb.getAttribute("usuario").toString();
					if(permisosNuevos.compareTo("[]")!=0){
						List<Integer> permisos = entradaJson.fromJson(permisosNuevos, tipo);
						asignacion = UsuarioDAO.asignarPermisosUsuario(usuario, permisos, usuarioActualizo);
					}
					if(permisosEliminados.compareTo("[]")!=0){
						List<Integer> permisos = entradaJson.fromJson(permisosEliminados, tipo);
						eliminacion = UsuarioDAO.desactivarPermisosUsuario(usuario, permisos, usuarioActualizo);
					}
					if(asignacion || eliminacion){
						response.getWriter().write("{ \"success\": true, \"mensaje\":\"asginación de permisos exitosa\" }");
					}else{
						response.getWriter().write("{ \"success\": false, \"error\":\"no se actualizaron los permisos\" }");
					}
				}
			}else if(accion.compareTo("eliminarUsuario")==0){
				String usuario= map.get("usuario");				
				if(usuario!=null){
					HttpSession sesionweb = request.getSession();
					String usuarioActualizo =sesionweb.getAttribute("usuario").toString();
					List <UsuarioPermiso> listaPermiso =new ArrayList <UsuarioPermiso>();
					listaPermiso = UsuarioDAO.getPermisosActivosUsuario(usuario);
					List <Integer>permisos = new ArrayList <Integer>();
					for(int i=0;i<listaPermiso.size();i++){
						permisos.add(listaPermiso.get(i).getId().getPermisoid());
					}
					boolean eliminarPermisos = UsuarioDAO.desactivarPermisosUsuario(usuario, permisos, usuarioActualizo);
					if(eliminarPermisos){
						boolean eliminarUsuario = UsuarioDAO.desactivarUsuario(usuario, usuarioActualizo);
						if(eliminarUsuario){
							response.getWriter().write("{ \"success\": true, \"mensaje\":\"eliminación exitosa\" }");
						}else{
							response.getWriter().write("{ \"success\": false, \"error\":\"error en la eliminación\" }");
						}
					}else{
						response.getWriter().write("{ \"success\": true, \"mensaje\":\"asginación de permisos exitosa\" }");
					}
				}
			}else if(accion.compareTo("obtenerPermisos")==0){
				String usuario = map.get("usuario");
				if(usuario!=null){
					List <UsuarioPermiso>permisos = new ArrayList <UsuarioPermiso>();
					List <stusuarioPermiso> stpermisos = new ArrayList <stusuarioPermiso>();
					permisos = UsuarioDAO.getPermisosActivosUsuario(usuario);
					if(permisos!=null && permisos.size() > 0){
						for(UsuarioPermiso usuarioPermiso : permisos){
							stusuarioPermiso usuariopermiso = new stusuarioPermiso();
							usuariopermiso.idPermiso=usuarioPermiso.getId().getPermisoid();
							usuariopermiso.nombrePermiso=usuarioPermiso.getPermiso().getNombre();
							usuariopermiso.descripcion=usuarioPermiso.getPermiso().getDescripcion();
							usuariopermiso.usuarioCreo=usuarioPermiso.getUsuarioCreo();
							usuariopermiso.usuarioActualizo=usuarioPermiso.getUsuarioActualizo();
							usuariopermiso.fechaCreacion=Utils.formatDate(usuarioPermiso.getFechaCreacion());
							usuariopermiso.fechaActualizacion=Utils.formatDate(usuarioPermiso.getFechaActualizacion());
							stpermisos.add(usuariopermiso);
						}
					}
					String respuesta = new GsonBuilder().serializeNulls().create().toJson(stpermisos);
					respuesta = String.join("", "\"permisos\":",respuesta);
					respuesta = String.join("", "{\"success\":true,", respuesta,"}");
					response.getWriter().write(respuesta);
				}
			}
		}else{
			response.getWriter().write("{ \"success\": false, \"error\":\"No se enviaron los parametros deseados\" }");
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

}
