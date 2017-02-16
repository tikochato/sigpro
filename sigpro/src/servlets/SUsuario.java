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

import dao.ColaboradorDAO;
import dao.UsuarioDAO;
import pojo.Colaborador;
import pojo.Permiso;
import pojo.Usuario;
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
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
	class stusuario{
		String usuario;
		String email;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		String password;
		String colaborador;
		String pnombre;
		String snombre;
		String papellido;
		String sapellido;
		Long cui;
		String unidad_ejecutora;
		int id;
	
	}
	
	class stpermiso{
		Integer id;
		String nombre;
		String descripcion;
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
		accion = map.get("accion");
		if(accion!=null){
			 if(accion.compareTo("actualizarPermisos")==0){
				String usuario = map.get("usuario");
				String permisosNuevos = map.get("permisosNuevos");
				String permisosEliminados = map.get("permisosEliminados");
				HttpSession sesionweb = request.getSession();
				if(usuario!=null && permisosNuevos!=null && permisosEliminados!=null){
					boolean asignacion=false;
					boolean eliminacion=false;
					Gson entradaJson = new Gson();
					Type tipo = new TypeToken<List<Integer>>() {}.getType();
					String usuarioActualizo ="";
					if(sesionweb.getAttribute("usuario")!=null){
						usuarioActualizo=sesionweb.getAttribute("usuario").toString();
					}
					
					if(permisosNuevos.compareTo("[]")!=0){
						List<Integer> permisos = entradaJson.fromJson(permisosNuevos, tipo);
						asignacion = UsuarioDAO.asignarPermisosUsuario(usuario, permisos, usuarioActualizo);
					}
					if(permisosEliminados.compareTo("[]")!=0){
						List<Integer> permisos = entradaJson.fromJson(permisosEliminados, tipo);
						eliminacion = UsuarioDAO.desactivarPermisosUsuario(usuario, permisos, usuarioActualizo);
					}
					if(asignacion || eliminacion){
						response_text = String.join("","{ \"success\": true, \"mensaje\":\"actualización de permisos exitosa\" }");
					}else{
						response_text = String.join("","{ \"success\": false, \"error\":\"no se actualizaron los permisos\" }");
					}
				}
			}else if(accion.compareTo("eliminarUsuario")==0){
				String usuario= map.get("usuario");				
				if(usuario!=null){
					HttpSession sesionweb = request.getSession();
					String usuarioActualizo ="";
					if(sesionweb.getAttribute("usuario")!=null){
						usuarioActualizo=sesionweb.getAttribute("usuario").toString();
					}
					List <UsuarioPermiso> listaPermiso =new ArrayList <UsuarioPermiso>();
					listaPermiso = UsuarioDAO.getPermisosActivosUsuario(usuario);
					List <Integer>permisos = new ArrayList <Integer>();
					for(int i=0;i<listaPermiso.size();i++){
						permisos.add(listaPermiso.get(i).getId().getPermisoid());
					}
					boolean eliminarPermisos = UsuarioDAO.desactivarPermisosUsuario(usuario, permisos, usuarioActualizo);
					if(eliminarPermisos){
						response_text = String.join("","{ \"success\": ",( UsuarioDAO.desactivarUsuario(usuario, usuarioActualizo) ? "true" : "false")," }");
					}else{
						response_text = String.join("","{ \"success\": false, \"error\":\"no se pudo eliminar el usuario\" }");
					}
				}else{
					response_text = String.join("","{ \"success\": false, \"error\":\"no se pudo eliminar el usuario\" }");
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
							usuariopermiso.nombre=usuarioPermiso.getPermiso().getNombre();
							usuariopermiso.descripcion=usuarioPermiso.getPermiso().getDescripcion();
							usuariopermiso.usuarioCreo=usuarioPermiso.getUsuarioCreo();
							usuariopermiso.usuarioActualizo=usuarioPermiso.getUsuarioActualizo();
							usuariopermiso.fechaCreacion=Utils.formatDate(usuarioPermiso.getFechaCreacion());
							usuariopermiso.fechaActualizacion=Utils.formatDate(usuarioPermiso.getFechaActualizacion());
							usuariopermiso.estado= usuarioPermiso.getEstado();
							stpermisos.add(usuariopermiso);
						}
					}
					String respuesta = new GsonBuilder().serializeNulls().create().toJson(stpermisos);
					response_text  = String.join("", "\"permisos\":",respuesta);
					response_text = String.join("", "{\"success\":true,", response_text ,"}");
				}
			}else if(accion.compareTo("getUsuarios")==0){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroUsuarios = map.get("numeroUsuarios")!=null  ? Integer.parseInt(map.get("numeroUsuarios")) : 0;
				String filtro_email = map.get("filtro_email");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				String filtro_usuario= map.get("filtro_usuario");
				List <Usuario>  usuarios = UsuarioDAO.getUsuarios(pagina, numeroUsuarios, filtro_usuario,filtro_email,filtro_usuario_creo,filtro_fecha_creacion);
				List <stusuario> stusuarios = new ArrayList <stusuario>();
				for(Usuario usuario: usuarios){
					stusuario usuariotmp =new  stusuario();
					usuariotmp.usuario =usuario.getUsuario();
					usuariotmp.email = usuario.getEmail();
					usuariotmp.usuarioCreo=usuario.getUsuarioCreo();
					usuariotmp.usuarioActualizo= usuario.getUsuarioActualizo();
					usuariotmp.fechaCreacion=Utils.formatDate(usuario.getFechaCreacion());
					usuariotmp.fechaActualizacion=Utils.formatDate(usuario.getFechaActualizacion());
					usuariotmp.password=usuario.getPassword();
					Colaborador colaborador_tmp=UsuarioDAO.getColaborador(usuariotmp.usuario);
					if(colaborador_tmp!=null){
						usuariotmp.colaborador=colaborador_tmp.getPapellido()+", "+colaborador_tmp.getPnombre();
					}				
					stusuarios.add(usuariotmp);
				}
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(stusuarios);
				response_text = String.join("", "\"usuarios\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.compareTo("getTotalUsuarios")==0){
				String filtro_email = map.get("filtro_email");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				String filtro_usuario= map.get("filtro_usuario");
				response_text=String.join("","{ \"success\": true, \"totalUsuarios\":",UsuarioDAO.getTotalUsuarios(filtro_usuario,filtro_email,filtro_usuario_creo,filtro_fecha_creacion).toString()," }") ;
			}else if(accion.compareTo("getPermisosDisponibles")==0){
				String usuario = map.get("usuario");
				if(usuario !=null){
					List <Permiso> permisos = UsuarioDAO.getPermisosDisponibles(usuario);
					List <stpermiso> stpermisos = new ArrayList <stpermiso>();
					for(Permiso permiso: permisos){
						stpermiso tmp = new stpermiso();
						tmp.id = permiso.getId();
						tmp.nombre=permiso.getNombre();
						tmp.descripcion = permiso.getDescripcion();
						stpermisos.add(tmp);
					}
					String respuesta = new GsonBuilder().serializeNulls().create().toJson(stpermisos);
					response_text = String.join("", "\"permisos\": ",respuesta);
					response_text = String.join("", "{\"success\":true,", response_text,"}");
				}
			}else if(accion.compareTo("cambiarPassword")==0){
				String usuario = map.get("usuario");
				String nuevoPassword = map.get("password");
				if(usuario!=null && nuevoPassword != null){
					HttpSession sesionweb = request.getSession();
					String usuarioActualizo ="";
					if(sesionweb.getAttribute("usuario")!=null){
						usuarioActualizo=sesionweb.getAttribute("usuario").toString();
					}
					
					if(UsuarioDAO.cambiarPassword(usuario, nuevoPassword,usuarioActualizo)){
						response_text = String.join("","{ \"success\": true, \"mensaje\":\"cambio de password exitoso.\" }");
					}else{
						response_text = String.join("","{ \"success\": false, \"error\":\"no se pudo cambiar el password.\" }");
					}
					
				}else{
					response_text = String.join("","{ \"success\": false, \"error\":\"no se enviaron los parámetros deseados.\" }");
				}
			}else if(accion.compareTo("usuarioActual")==0){
				HttpSession sesionweb = request.getSession();
				String usuario_texto = sesionweb.getAttribute("usuario")!=null? sesionweb.getAttribute("usuario").toString(): "";
				Usuario usuarioActual = UsuarioDAO.getUsuario(usuario_texto);
				Colaborador  colaboradorActual = UsuarioDAO.getColaborador(usuario_texto);
				stusuario usuarioStr = new stusuario();
				usuarioStr.email=usuarioActual.getEmail();
				usuarioStr.usuario=usuarioActual.getUsuario();
				usuarioStr.password=usuarioActual.getPassword();
				if(colaboradorActual!=null){
					usuarioStr.cui=colaboradorActual.getCui();
					usuarioStr.pnombre=colaboradorActual.getPnombre();
					usuarioStr.snombre=colaboradorActual.getSnombre();
					usuarioStr.papellido=colaboradorActual.getPapellido();
					usuarioStr.sapellido=colaboradorActual.getSapellido();
					usuarioStr.unidad_ejecutora=String.valueOf(colaboradorActual.getUnidadEjecutora().getUnidadEjecutora());
					usuarioStr.id=colaboradorActual.getId();
				}
				
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(usuarioStr);
				response_text = String.join("", "\"usuario\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.compareTo("guardarUsuario")==0){
				String usuario =map.get("usuario");
				String esnuevo = map.get("esnuevo");
				if(usuario!=null && esnuevo!=null){
					if(esnuevo.compareTo("true")==0){
						String nuevousuario = map.get("usuario").toLowerCase();
						String nuevopassword = map.get("password");
						String nuevomail = map.get("email").toLowerCase();
						String permisosAsignados = map.get("permisos");
						HttpSession sesionweb = request.getSession();
						if(nuevousuario!=null && nuevopassword!=null && nuevomail != null && permisosAsignados!=null){
							if(!UsuarioDAO.existeUsuario(nuevousuario)){
								if(nuevousuario.compareTo("")!=0 && nuevopassword.compareTo("")!=0 && nuevomail.compareTo("")!=0){
									String usuarioCreo =sesionweb.getAttribute("usuario").toString();
									boolean registro = UsuarioDAO.registroUsuario(nuevousuario, nuevomail, nuevopassword,usuarioCreo);
									if(registro){
										if(permisosAsignados.compareTo("[]")!=0){
											Gson entradaJson = new Gson();
											Type tipo = new TypeToken<List<Integer>>() {}.getType();
											List<Integer> permisos = entradaJson.fromJson(permisosAsignados, tipo);
											response_text = String.join("","{ \"success\": ",(UsuarioDAO.asignarPermisosUsuario(nuevousuario, permisos, usuarioCreo) ? "true ,  \"message\":\"Usuario creado y asignación de permisos exitosa\" " : "true, \"message\":\"Usuario creado, asignación de permisos no exitosa\""),"}");
										}else{
											response_text = String.join("", "{\"success\":true, \"message\":\"usuario creado exitosamente\" }");
										}
										
									}else{
										response_text = String.join("", "{ \"success\": false, \"error\":\"Error al registrar nuevo usuario\" }");
									}
								}else{
									response_text = String.join("", "{ \"success\": false, \"error\":\"Parametros vacios\" }");
								}
							}else{
								response_text = String.join("", "{ \"success\": false, \"error\":\"Ya existe ese usuario\" }");
							}
							
						}else{
							response_text = String.join("", "{ \"success\": false, \"error\":\"No se enviaron los parametros deseados\" }");
						}
					}else{
						Usuario usuarioEdicion = UsuarioDAO.getUsuario(usuario);
						if(usuarioEdicion!=null){
							String email = map.get("email");
							if(email!=null){
								usuarioEdicion.setEmail(email);
								HttpSession sesionweb = request.getSession();
								if(UsuarioDAO.editarUsuario(usuarioEdicion, sesionweb.getAttribute("usuario").toString())){
									response_text = String.join("","{ \"success\": true, \"mensaje\":\"actualización de usuario exitosa.\" }");
								}else{
									response_text = String.join("","{ \"success\": false, \"error\":\"no se pudo actualizar al usuario. \" }");
								}
								
							}else{
								response_text = String.join("","{ \"success\": false, \"error\":\"no se encontró al usuario. \" }");
							}
						}else{
							response_text = String.join("","{ \"success\": false, \"error\":\"no se enviaron los parámetros correctos \" }");
						}
						
					}
				}else{
					response_text = String.join("","{ \"success\": false, \"error\":\"no se enviaron los parámetros correctos \" }");
				}
			}else if(accion.compareTo("getColaboradores")==0){
				String resultadoJson = "";

				resultadoJson = ColaboradorDAO.getJson2();

				if (Utils.isNullOrEmpty(resultadoJson)) {
					response_text = "{\"success\":false}";
				} else {
					response_text = "{\"success\":true," + resultadoJson + "}";
				}
			}else if(accion.compareTo("getUsuariosDisponibles")==0){
				List <Usuario>  usuarios = UsuarioDAO.getUsuariosDisponibles();
				List <stusuario> stusuarios = new ArrayList <stusuario>();
				for(Usuario usuario: usuarios){
					if(UsuarioDAO.getColaborador(usuario.getUsuario())==null){
						stusuario usuariotmp =new  stusuario();
						usuariotmp.usuario =usuario.getUsuario();
						usuariotmp.email = usuario.getEmail();
						usuariotmp.usuarioCreo=usuario.getUsuarioCreo();
						usuariotmp.usuarioActualizo= usuario.getUsuarioActualizo();
						usuariotmp.fechaCreacion=Utils.formatDate(usuario.getFechaCreacion());
						usuariotmp.fechaActualizacion=Utils.formatDate(usuario.getFechaActualizacion());
						stusuarios.add(usuariotmp);
					}
							
					
				}
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(stusuarios);
				response_text = String.join("", "\"usuarios\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
		}else{
			response_text = String.join("","{ \"success\": false, \"error\":\"No se enviaron los parametros deseados\" }");
		}
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
