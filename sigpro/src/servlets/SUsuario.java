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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ColaboradorDAO;
import dao.EtiquetaDAO;
import dao.ProyectoDAO;
import dao.RolDAO;
import dao.UsuarioDAO;
import pojo.Colaborador;
import pojo.Etiqueta;
import pojo.Permiso;
import pojo.Usuario;
import pojo.UsuarioPermiso;
import pojo.Proyecto;
import pojo.ProyectoUsuario;
import pojo.Rol;
import pojo.RolUsuarioProyecto;
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
		int rol;
		String cooperante;
		Long cui;
		String unidad_ejecutora;
		int id;
		int sistemaUsuario;
	}
	
	class stpermiso{
		Integer id;
		String nombre;
		String descripcion;
	}
	class stproyecto{
		Integer id;
		String nombre;		
	}
	class stproyecto_usuario{
		Integer id;
		String nombre;
		String usuario;
	}
	
	class stetiqueta{
		Integer id;
		String claseNombre;
		String proyecto;
		String colorPrincipal;
	}
       
    public SUsuario() {
        super();
    }

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
					boolean asigna_estructuras=false;
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
					String estructura_permisos = map.get("estructuraAsignada");
					String[] estructuras = estructura_permisos.split("\\|");
					int id=0;
					int objeto_tipo=0;
					if(estructuras!=null){
						UsuarioDAO.desasignarEstructurasPermisos(usuario);
						for(int i=0; i<estructuras.length; i++){
							String[] permiso = estructuras[i].split(",");
							if(permiso!=null && permiso.length==2){
								objeto_tipo = Integer.parseInt(permiso[0]);
								id = Integer.parseInt(permiso[1]);
								List<Integer> ids_estructuras = new ArrayList<Integer>();
								ids_estructuras.add(id);
								switch(objeto_tipo){
									case 0: UsuarioDAO.asignarPrestamos(usuario, ids_estructuras, usuarioActualizo); break;
									case 1: UsuarioDAO.asignarComponentes(usuario, ids_estructuras, usuarioActualizo); break;
									case 2: UsuarioDAO.asignarSubComponentes(usuario, ids_estructuras, usuarioActualizo); break;
									case 3: UsuarioDAO.asignarProductos(usuario, ids_estructuras, usuarioActualizo); break;
									
								}
							}
						}
						asigna_estructuras=true;
					}
					if(asignacion || eliminacion || asigna_estructuras){
						response_text = String.join("","{ \"success\": true, \"mensaje\":\"Actualización de permisos exitosa\" }");
					}else{
						response_text = String.join("","{ \"success\": false, \"error\":\"No se actualizaron los permisos\" }");
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
						response_text = String.join("","{ \"success\": false, \"error\":\"No se pudo eliminar el usuario\" }");
					}
				}else{
					response_text = String.join("","{ \"success\": false, \"error\":\"No se pudo eliminar el usuario\" }");
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
					List <ProyectoUsuario>proyectos = new ArrayList <ProyectoUsuario>();
					List <stproyecto_usuario>stproyectos = new ArrayList <stproyecto_usuario>();
					proyectos = UsuarioDAO.getPrestamosAsignadosPorUsuario(usuario);
					if(proyectos !=null && proyectos.size()>0){
						for(ProyectoUsuario proyectoUsuario : proyectos){
							stproyecto_usuario proyecto_usuario= new stproyecto_usuario();
							proyecto_usuario.id=proyectoUsuario.getId().getProyectoid();
							proyecto_usuario.nombre=ProyectoDAO.getProyecto(proyecto_usuario.id).getNombre();
							proyecto_usuario.usuario=proyectoUsuario.getId().getUsuario();
							stproyectos.add(proyecto_usuario);
						}
					}
					Integer rol = UsuarioDAO.getRolPorUsuario(usuario);
					Integer unidadEjecutora =0;
					if(rol>0)
						unidadEjecutora = UsuarioDAO.getUnidadEjecutora(usuario).getId().getUnidadEjecutora();
					//Integer cooperante = UsuarioDAO.getCooperantePorUsuario(usuario).getId();
					String respuesta = new GsonBuilder().serializeNulls().create().toJson(stpermisos);
					String proyectos_usuarios= new GsonBuilder().serializeNulls().create().toJson(stproyectos);
					response_text  = String.join("", "\"permisos\":",respuesta);
					response_text = String.join("", "{\"success\":true,", response_text ,",\"proyectos\": "+proyectos_usuarios+"," +"\"rol\": "+rol+",\"unidadEjecutora\": "+unidadEjecutora +"}");
					//response_text = String.join("", "{\"success\":true,", response_text ,",\"proyectos\": "+proyectos_usuarios+"," +"\"rol\": "+rol+",\"unidadEjecutora\": "+unidadEjecutora+"," +"\"cooperante\": "+cooperante+"}");
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
					RolUsuarioProyecto tmp= RolDAO.getRolUser(usuariotmp.usuario);
					if(tmp.getId()!=null){
						int rolid= tmp.getId().getRol();
						usuariotmp.rol=rolid==0? 1: rolid;
					}else{
						usuariotmp.rol=1;
					}
					if(usuariotmp.rol!=0 &&usuariotmp.rol==6){
						usuariotmp.cooperante= RolDAO.getCooperante(usuariotmp.usuario).getNombre();
					}
					usuariotmp.email = usuario.getEmail();
					usuariotmp.usuarioCreo=usuario.getUsuarioCreo();
					usuariotmp.usuarioActualizo= usuario.getUsuarioActualizo();
					usuariotmp.fechaCreacion=Utils.formatDate(usuario.getFechaCreacion());
					usuariotmp.fechaActualizacion=Utils.formatDate(usuario.getFechaActualizacion());
					usuariotmp.password="";
					usuariotmp.sistemaUsuario = usuario.getSistemaUsuario();
					Colaborador colaborador_tmp=UsuarioDAO.getColaborador(usuariotmp.usuario);
					if(colaborador_tmp!=null){
						usuariotmp.colaborador=colaborador_tmp.getPapellido()+", "+colaborador_tmp.getPnombre();
						usuariotmp.unidad_ejecutora=colaborador_tmp.getUnidadEjecutora().getNombre();
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
						response_text = String.join("","{ \"success\": true, \"mensaje\":\"Cambio de password exitoso.\" }");
					}else{
						response_text = String.join("","{ \"success\": false, \"error\":\"No se pudo cambiar el password.\" }");
					}
					
				}else{
					response_text = String.join("","{ \"success\": false, \"error\":\"No se enviaron los parámetros deseados.\" }");
				}
			}else if(accion.compareTo("usuarioActual")==0){
				HttpSession sesionweb = request.getSession();
				String usuario_texto = sesionweb.getAttribute("usuario")!=null? sesionweb.getAttribute("usuario").toString(): "";
				Usuario usuarioActual = UsuarioDAO.getUsuario(usuario_texto);
				Colaborador  colaboradorActual = UsuarioDAO.getColaborador(usuario_texto);
				stusuario usuarioStr = new stusuario();
				usuarioStr.email=usuarioActual.getEmail();
				usuarioStr.usuario=usuarioActual.getUsuario();
				usuarioStr.password="";
				usuarioStr.sistemaUsuario = usuarioActual.getSistemaUsuario();
				if(colaboradorActual!=null){
					usuarioStr.cui=colaboradorActual.getCui();
					usuarioStr.pnombre=colaboradorActual.getPnombre();
					usuarioStr.snombre=colaboradorActual.getSnombre();
					usuarioStr.papellido=colaboradorActual.getPapellido();
					usuarioStr.sapellido=colaboradorActual.getSapellido();
					usuarioStr.unidad_ejecutora=String.valueOf(colaboradorActual.getUnidadEjecutora().getId().getUnidadEjecutora());
					usuarioStr.id=colaboradorActual.getId();
				}
				
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(usuarioStr);
				response_text = String.join("", "\"usuario\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.compareTo("guardarUsuario")==0){
				String usuario =map.get("usuario");
				String esnuevo = map.get("esnuevo");
				HttpSession sesionweb = request.getSession();
				String usuario_texto = sesionweb.getAttribute("usuario")!=null? sesionweb.getAttribute("usuario").toString(): "";
				if(usuario!=null && esnuevo!=null){
					if(esnuevo.compareTo("true")==0){
						String nuevousuario = map.get("usuario").toLowerCase();
						String nuevopassword = map.get("password");
						String nuevomail = map.get("email").toLowerCase();
						String permisosAsignados = map.get("permisos");
//						String prestamosAsignados=map.get("prestamos");
						Integer sistemaUsuario= Utils.String2Int(map.get("sistemaUsuario"), 1);
						String rol =map.get("rol");
						if(nuevousuario!=null && nuevopassword!=null && nuevomail != null && permisosAsignados!=null && rol!=null){
							if(!UsuarioDAO.existeUsuario(nuevousuario)){
								if(nuevousuario.compareTo("")!=0 && nuevopassword.compareTo("")!=0 && nuevomail.compareTo("")!=0){
									boolean registro = UsuarioDAO.registroUsuario(nuevousuario, nuevomail, nuevopassword,usuario_texto, sistemaUsuario);
									if(registro){
										String estructura_permisos = map.get("estructuraAsignada");
										String[] estructuras = estructura_permisos.split("\\|");
										int id=0;
										int objeto_tipo=0;
										if(estructuras!=null){
											UsuarioDAO.desasignarEstructurasPermisos(usuario);
											for(int i=0; i<estructuras.length; i++){
												String[] permiso = estructuras[i].split(",");
												if(permiso!=null && permiso.length==2){
													objeto_tipo = Integer.parseInt(permiso[0]);
													id = Integer.parseInt(permiso[1]);
													List<Integer> ids_estructuras = new ArrayList<Integer>();
													ids_estructuras.add(id);
													switch(objeto_tipo){
														case -1: UsuarioDAO.asignarPrestamos(usuario, ids_estructuras, usuario_texto); break;
														case 0: UsuarioDAO.asignarProyectos(usuario, ids_estructuras, usuario_texto); break;
														case 1: UsuarioDAO.asignarComponentes(usuario, ids_estructuras, usuario_texto); break;
														case 2: UsuarioDAO.asignarSubComponentes(usuario, ids_estructuras, usuario_texto); break;
														case 3: UsuarioDAO.asignarProductos(usuario, ids_estructuras, usuario_texto); break;
														
													}
												}
											}
										}
//										if(prestamosAsignados!=null && prestamosAsignados.compareTo("[]")!=0){
//											Gson entradaJson = new Gson();
//											Type tipo = new TypeToken<List<Integer>>() {}.getType();
//											List<Integer> prestamos = entradaJson.fromJson(prestamosAsignados, tipo);
//											UsuarioDAO.asignarPrestamos(usuario, prestamos,usuario_texto);
//											UsuarioDAO.asignarPrestamoRol(usuario, prestamos, Integer.parseInt(rol));
//										}
										if(permisosAsignados!=null && permisosAsignados.compareTo("[]")!=0){
											Gson entradaJson = new Gson();
											Type tipo = new TypeToken<List<Integer>>() {}.getType();
											List<Integer> permisos = entradaJson.fromJson(permisosAsignados, tipo);
											response_text = String.join("","{ \"success\": ",(UsuarioDAO.asignarPermisosUsuario(nuevousuario, permisos, usuario_texto) ? "true ,  \"mensaje\":\"Usuario creado y asignación de permisos exitosa\" " : "true, \"mensaje\":\"Usuario creado, asignación de permisos no exitosa\""),"}");
										}else{
											response_text = String.join("", "{\"success\":true, \"mensaje\":\"Usuario creado exitosamente\" }");
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
							String password = map.get("password");
							if(password!=null && password.length()>0 && !password.equals(usuarioEdicion.getPassword())){
								usuarioEdicion = UsuarioDAO.setNuevoPassword(usuarioEdicion, password);
							}
							String email = map.get("email");
							String permisosAsignados = map.get("permisos");
							Integer sistemaUsuario = Utils.String2Int(map.get("sistemaUsuario"), 1);
							usuarioEdicion.setSistemaUsuario(sistemaUsuario);
							if(email!=null){
								usuarioEdicion.setEmail(email);
								String estructura_permisos = map.get("estructuraAsignada");
								String[] estructuras = estructura_permisos.split("\\|");
								int id=0;
								int objeto_tipo=0;
								if(estructuras!=null){
									UsuarioDAO.desasignarEstructurasPermisos(usuario);
									for(int i=0; i<estructuras.length; i++){
										String[] permiso = estructuras[i].split(",");
										if(permiso!=null && permiso.length==2){
											objeto_tipo = Integer.parseInt(permiso[0]);
											id = Integer.parseInt(permiso[1]);
											List<Integer> ids_estructuras = new ArrayList<Integer>();
											ids_estructuras.add(id);
											switch(objeto_tipo){
												case -1: UsuarioDAO.asignarPrestamos(usuario, ids_estructuras, usuario_texto); break;
												case 0: UsuarioDAO.asignarProyectos(usuario, ids_estructuras, usuario_texto); break;
												case 1: UsuarioDAO.asignarComponentes(usuario, ids_estructuras, usuario_texto); break;
												case 2: UsuarioDAO.asignarSubComponentes(usuario, ids_estructuras, usuario_texto); break;
												case 3: UsuarioDAO.asignarProductos(usuario, ids_estructuras, usuario_texto); break;
												
											}
										}
									}
								}
								
								UsuarioDAO.desasignarPermisos(usuario);
								if(permisosAsignados!=null && permisosAsignados.compareTo("[]")!=0){
									Gson entradaJson = new Gson();
									Type tipo = new TypeToken<List<Integer>>() {}.getType();
									List<Integer> permisos = entradaJson.fromJson(permisosAsignados, tipo);
									UsuarioDAO.asignarPermisosUsuario(usuario, permisos, usuario_texto);
								}
								if(UsuarioDAO.editarUsuario(usuarioEdicion, sesionweb.getAttribute("usuario").toString())){
									response_text = String.join("","{ \"success\": true, \"mensaje\":\"Actualización de usuario exitosa.\" }");
								}else{
									response_text = String.join("","{ \"success\": false, \"error\":\"No se pudo actualizar al usuario. \" }");
								}
								
							}else{
								response_text = String.join("","{ \"success\": false, \"error\":\"No se encontró al usuario. \" }");
							}
						}else{
							response_text = String.join("","{ \"success\": false, \"error\":\"No se enviaron los parámetros correctos \" }");
						}
						
					}
				}else{
					response_text = String.join("","{ \"success\": false, \"error\":\"No se enviaron los parámetros correctos \" }");
				}
			}
			else if(accion.compareTo("getColaboradores")==0){
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
						usuariotmp.sistemaUsuario = usuario.getSistemaUsuario();
						stusuarios.add(usuariotmp);
					}
							
					
				}
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(stusuarios);
				response_text = String.join("", "\"usuarios\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.compareTo("getPrestamosPorElemento")==0){
				String elemento = map.get("tipo");
				String id_elemento = map.get("id");
				if(elemento!=null && id_elemento!=null){
					HttpSession sesionweb = request.getSession();
					String usuario_consulta =sesionweb.getAttribute("usuario").toString();
					List <Proyecto> proyectos = UsuarioDAO.getPrestamosPorElemento(Integer.parseInt(elemento),Integer.parseInt(id_elemento),usuario_consulta);
					List <stproyecto>stproyectos = new  ArrayList<stproyecto>();
					for(Proyecto proyecto : proyectos){
						stproyecto tmpProyecto = new stproyecto();
						tmpProyecto.id = proyecto.getId();
						tmpProyecto.nombre= proyecto.getNombre();
						stproyectos.add(tmpProyecto);
					}
					String respuesta = new GsonBuilder().serializeNulls().create().toJson(stproyectos);
					response_text = String.join("", "\"prestamos\": ",respuesta);
					response_text = String.join("", "{\"success\":true,", response_text,"}");
				}
			}else if(accion.compareTo("getUsuarioPorPrestamo")==0){
				String proyecto = map.get("proyecto");
				if(proyecto!=null){
					List <RolUsuarioProyecto> rolUsuarios = UsuarioDAO.getUsuariosPorPrestamo(Integer.parseInt(proyecto));
					List <stproyecto_usuario>usuarios = new  ArrayList<stproyecto_usuario>();
					for(RolUsuarioProyecto rolusuario : rolUsuarios){
						stproyecto_usuario tmp = new stproyecto_usuario();
						tmp.id=rolusuario.getId().getRol();
						Rol tmpRol = new Rol();
						tmpRol =RolDAO.getRol(rolusuario.getId().getRol());
						tmp.nombre=tmpRol.getNombre();
						Usuario us_tmp= new Usuario();
						us_tmp=UsuarioDAO.getUsuario(rolusuario.getId().getUsuario());
						String nombre_col = "";
						if(us_tmp.getColaboradors()!=null){
							Set <Colaborador> colaboradores= new HashSet <Colaborador>();
							colaboradores = us_tmp.getColaboradors();							
							for (Colaborador col :colaboradores){
								nombre_col = col.getPnombre()+ " "+ col.getPapellido();
							}
						}
						tmp.usuario=nombre_col+" "+rolusuario.getId().getUsuario()+" - "+us_tmp.getEmail();
						usuarios.add(tmp);
					}
					String respuesta = new GsonBuilder().serializeNulls().create().toJson(usuarios);
					response_text = String.join("", "\"usuarios\": ",respuesta);
					response_text = String.join("", "{\"success\":true,", response_text,"}");
				}
			}else if(accion.compareTo("getEtiquetasSistemaUsuario")==0){
				HttpSession sesionweb = request.getSession();
				Integer sistemaUsuario = Utils.String2Int(sesionweb.getAttribute("sistemausuario").toString());
				Etiqueta etiquetaUsuario = EtiquetaDAO.getEtiquetaPorId(sistemaUsuario);
				if(etiquetaUsuario==null){
					etiquetaUsuario = EtiquetaDAO.getEtiquetaPorId(1);
				}
				stetiqueta etiqueta = new stetiqueta();
				etiqueta.id = etiquetaUsuario.getId();
				etiqueta.claseNombre = etiquetaUsuario.getNombre();
				etiqueta.proyecto = etiquetaUsuario.getProyecto();
				etiqueta.colorPrincipal = etiquetaUsuario.getColorPrincipal();
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(etiqueta);
				response_text = String.join("", "\"etiquetas\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.compareTo("getSistemasUsuario")==0){
				List<Etiqueta> etiquetasUsuario = EtiquetaDAO.getEtiquetas();
				List<stetiqueta> etiquetas = new ArrayList<stetiqueta>();
				for(int i=0; i<etiquetasUsuario.size(); i++){
					stetiqueta etiqueta = new stetiqueta();
					etiqueta.id = etiquetasUsuario.get(i).getId();
					etiqueta.claseNombre = etiquetasUsuario.get(i).getNombre();
					etiqueta.proyecto = etiquetasUsuario.get(i).getProyecto();
					etiqueta.colorPrincipal = etiquetasUsuario.get(i).getColorPrincipal();
					etiquetas.add(etiqueta);
				}
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(etiquetas);
				response_text = String.join("", "\"etiquetas\": ",respuesta);
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
