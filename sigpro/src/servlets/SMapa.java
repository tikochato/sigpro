package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

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
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.Utils;


@WebServlet("/SMapa")
public class SMapa extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
	class stobjeto {
		Integer id;
		String nombreOjetoTipo;
		String nombre;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
		String title;
		
	}

    public SMapa() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
		String response_text = "";
		if(accion.equals("getMarcasProyecto")){
			int id = 0;
			String marcas = "";
			List<Proyecto> proyectos = ProyectoDAO.getProyectos(usuario);
			for (Proyecto proyecto : proyectos){
				if (proyecto.getLongitud()!=null && proyecto.getLatitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
					getMarca(id,proyecto.getId(),1, proyecto.getNombre(),proyecto.getLatitud(), proyecto.getLongitud()));
				}
			}
			
			response_text = String.join("","{\"marcas\" : [",marcas,"]}" );
			
		}else if(accion.equals("getMarcas")){
			int id = 0;
			String marcas = "";
			List<Proyecto> proyectos = ProyectoDAO.getProyectos(usuario);
			for (Proyecto proyecto : proyectos){
				if (proyecto.getLongitud()!=null && proyecto.getLatitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
					getMarca(id,proyecto.getId(),1, proyecto.getNombre(),proyecto.getLatitud(), proyecto.getLongitud()));
				}
			}
			
			List<Componente> componentes = ComponenteDAO.getComponentes(usuario);
			for (Componente componente : componentes){
				if (componente.getLatitud()!=null && componente.getLongitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
							getMarca(id,componente.getId(),2, componente.getNombre(),componente.getLatitud(), componente.getLongitud()));
				}
			}
			
			List<Producto> productos = ProductoDAO.getProductos(usuario);
			for (Producto producto :productos){
				if (producto.getLatitud()!=null && producto.getLongitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
							getMarca(id,producto.getId(),3, producto.getNombre(), producto.getLatitud(), producto.getLongitud()));
				}
			}
			
			List<Subproducto> subproductos = SubproductoDAO.getSubproductos(usuario);
			for (Subproducto subproducto :subproductos){
				if (subproducto.getLatitud()!=null && subproducto.getLongitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
							getMarca(id,subproducto.getId(),4, subproducto.getNombre(), subproducto.getLatitud(), subproducto.getLongitud()));
				}
			}
			
			List<Actividad> actividades = ActividadDAO.getActividads(usuario);
			for (Actividad actividad :actividades){
				if (actividad.getLatitud()!=null && actividad.getLongitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
							getMarca(id, actividad.getId(),5, actividad.getNombre(),actividad.getLatitud(), actividad.getLongitud()));
				}
			}
			
			response_text = String.join("","{\"marcas\" : [",marcas,"]}" );
			
		}else if (accion.equals("getMarcasPorProyecto")){
			
			int id = 0;
			String marcas = "";
			int proyectoId = Utils.String2Int(map.get("proyectoId"),0);
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
			
			if (proyecto.getLongitud()!=null && proyecto.getLatitud()!=null){
				id++;
				marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
				getMarca(id,proyecto.getId(),1, proyecto.getNombre(),proyecto.getLatitud(), proyecto.getLongitud()));
			}
				
				
				
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyecto.getId(),
					null, null, null,null,null, usuario);
			for (Componente componente : componentes){
				if (componente.getLatitud()!=null && componente.getLongitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
							getMarca(id,componente.getId(),OBJETO_ID_COMPONENTE, componente.getNombre(),componente.getLatitud(), componente.getLongitud()));
				}
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				for (Producto producto :productos){
					if (producto.getLatitud()!=null && producto.getLongitud()!=null){
						id++;
						marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
								getMarca(id,producto.getId(),OBJETO_ID_PRODUCTO, producto.getNombre(), producto.getLatitud(), producto.getLongitud()));
					}
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					for (Subproducto subproducto :subproductos){
						if (subproducto.getLatitud()!=null && subproducto.getLongitud()!=null){
							id++;
							marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
									getMarca(id,subproducto.getId(),OBJETO_ID_SUBPRODUCTO, subproducto.getNombre(), subproducto.getLatitud(), subproducto.getLongitud()));
						}
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO
								, null, null, null, null, null, usuario);
						for (Actividad actividad :actividades){
							if (actividad.getLatitud()!=null && actividad.getLongitud()!=null){
								id++;
								marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
										getMarca(id, actividad.getId(),OBJETO_ID_ACTIVIDAD, actividad.getNombre(),actividad.getLatitud(), actividad.getLongitud()));
							}
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO
							, null, null, null, null, null, usuario);
					for (Actividad actividad :actividades){
						if (actividad.getLatitud()!=null && actividad.getLongitud()!=null){
							id++;
							marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
									getMarca(id, actividad.getId(),5, actividad.getNombre(),actividad.getLatitud(), actividad.getLongitud()));
						}
					}
					
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE
						, null, null, null, null, null, usuario);
				for (Actividad actividad :actividades){
					if (actividad.getLatitud()!=null && actividad.getLongitud()!=null){
						id++;
						marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
								getMarca(id, actividad.getId(),OBJETO_ID_ACTIVIDAD, actividad.getNombre(),actividad.getLatitud(), actividad.getLongitud()));
					}
				}	
			}
			
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyecto.getId(), OBJETO_ID_PROYECTO
					, null, null, null, null, null, usuario);
			for (Actividad actividad :actividades){
				if (actividad.getLatitud()!=null && actividad.getLongitud()!=null){
					id++;
					marcas = String.join(marcas.length() > 0 ? "," : "", marcas,
							getMarca(id, actividad.getId(),OBJETO_ID_ACTIVIDAD, actividad.getNombre(),actividad.getLatitud(), actividad.getLongitud()));
				}
			}
			
			
			response_text = String.join("","{\"marcas\" : [",marcas,"]}" );
			
		}
		else if(accion.equals("getObjeto")){
			int objetoId = Utils.String2Int(map.get("objetoId"), 0);
			int objetoTipo = Utils.String2Int(map.get("objetoTipo"), 0);
			stobjeto objeto = new stobjeto();
			switch (objetoTipo){
			case 1: 
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(objetoId, usuario);
				objeto.nombreOjetoTipo = "Proyecto";
				objeto.id = proyecto.getId();
				objeto.nombre = proyecto.getNombre();
				objeto.fechaCreacion = Utils.formatDate(proyecto.getFechaCreacion());
				objeto.usuarioCreo = proyecto.getUsuarioCreo();
				objeto.fechaactualizacion = Utils.formatDate(proyecto.getFechaActualizacion());
				objeto.usuarioactualizo = proyecto.getUsuarioActualizo();
				
				break;
			case 2:
				Componente componente = ComponenteDAO.getComponentePorId(objetoId, usuario);
				objeto.nombreOjetoTipo = "Componente";
				objeto.id = componente.getId();
				objeto.nombre = componente.getNombre();
				objeto.fechaCreacion = Utils.formatDate(componente.getFechaCreacion());
				objeto.usuarioCreo = componente.getUsuarioCreo();
				objeto.fechaactualizacion = Utils.formatDate(componente.getFechaActualizacion());
				objeto.usuarioactualizo = componente.getUsuarioActualizo();
				break;
			case 3:
				Producto prodcuto = ProductoDAO.getProductoPorId(objetoId);
				objeto.nombreOjetoTipo = "Producto";
				objeto.id = prodcuto.getId();
				objeto.nombre = prodcuto.getNombre();
				objeto.fechaCreacion = Utils.formatDate(prodcuto.getFechaCreacion());
				objeto.usuarioCreo = prodcuto.getUsuarioCreo();
				objeto.fechaactualizacion = Utils.formatDate(prodcuto.getFechaActualizacion());
				objeto.usuarioactualizo = prodcuto.getUsuarioActualizo();
				break;
			case 4:
				Subproducto subprodcuto = SubproductoDAO.getSubproductoPorId(objetoId);
				objeto.nombreOjetoTipo = "Subproducto";
				objeto.id = subprodcuto.getId();
				objeto.nombre = subprodcuto.getNombre();
				objeto.fechaCreacion = Utils.formatDate(subprodcuto.getFechaCreacion());
				objeto.usuarioCreo = subprodcuto.getUsuarioCreo();
				objeto.fechaactualizacion = Utils.formatDate(subprodcuto.getFechaActualizacion());
				objeto.usuarioactualizo = subprodcuto.getUsuarioActualizo();
				break;
			case 5:
				Actividad actividad = ActividadDAO.getActividadPorId(objetoId, usuario);
				objeto.nombreOjetoTipo = "Actividad";
				objeto.id = actividad.getId();
				objeto.nombre = actividad.getNombre();
				objeto.fechaCreacion = Utils.formatDate(actividad.getFechaCreacion());
				objeto.usuarioCreo = actividad.getUsuarioCreo();
				objeto.fechaactualizacion = Utils.formatDate(actividad.getFechaActualizacion());
				objeto.usuarioactualizo = actividad.getUsuarioActualizo();
				break;
				
			}
					
			response_text=new GsonBuilder().serializeNulls().create().toJson(objeto);
			response_text = String.join("", "\"objeto\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
					
			
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
	
	private String getMarca(Integer id, Integer objetoId,Integer objetoTipoId,  String nombre, String latitud, String longitud){
		return  String.join("", "{\"id\": ", id.toString(),",",
				"\"objetoId\": ", objetoId.toString(),",",
				"\"objetoTipoId\": ", objetoTipoId.toString(), ",",
				"\"nombre\": \"", nombre,"\",",
				"\"posicion\" : {"
				,"\"latitude\": \"",latitud,"\","
				,"\"longitude\": \"",longitud,"\"}, "
				, "\"icon\": { \"url\": \"/assets/img/marcas/marca_",objetoTipoId.toString()
				,".png\", \"scaledSize\": { \"width\": 32, \"height\": 32 }}}"
				);
	}

}
