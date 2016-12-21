package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CooperanteDAO;
import dao.ProyectoDAO;
import dao.ProyectoTipoDAO;
import dao.UnidadEjecutoraDAO;
import pojo.Cooperante;
import pojo.Proyecto;
import pojo.ProyectoTipo;
import pojo.UnidadEjecutora;
import utilities.Utils;

/**
 * Servlet implementation class SProyecto
 */
@WebServlet("/SProyecto")
public class SProyecto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class datos {
		int id;
		String nombre;
		String descripcion;
		int snip;
		int proyectotipoid;
		String proyectotipo;
		String unidadejecutora;
		int unidadejecutoraid;
		String cooperante;
		int cooperanteid;
		String fechacrea;
		String usuariocrea;
		String fechaactualizacion;
		String usuarioactualizo;
		
	};
	
	class listas{
		int id;
		String nombre;
	}
       
    
    public SProyecto() {
        super();
        
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		if (accion.equals("getProyectoPagin")) {
			List<Proyecto> proyectos = ProyectoDAO.getProyectos();
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			
			List <datos> datos_ = new ArrayList<datos>();
			for (Proyecto proyecto : proyectos){
				datos dato = new datos();
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getId();
				dato.unidadejecutora = proyecto.getUnidadEjecutora().getNombre();
				dato.unidadejecutoraid = proyecto.getId();
				dato.cooperante = proyecto.getCooperante().getNombre();
				dato.cooperanteid = proyecto.getId();
				dato.fechacrea = Utils.formatDate( proyecto.getFechaCreacion());
				dato.usuariocrea = proyecto.getUsuarioCreo();
				dato.fechaactualizacion = Utils.formatDate( proyecto.getFechaActualizacion());
				dato.usuarioactualizo = proyecto.getUsuarioActualizo();
				datos_.add(dato);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(datos_);
			
			
			//response_text = new GsonBuilder().serializeNulls().create().toJson(proyectos);
			response_text = String.join("", "\"entidades\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
			
			OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
			gz.write(response_text.getBytes("UTF-8"));
			gz.close();
			output.close();
			
		}else if (accion.equals("guardar")){
			boolean esnuevo = map.get("esnuevo").equals("true");
			
			Map<String, String> datos = gson.fromJson(map.get("datos"), type);
			Proyecto proyecto;
			if (datos!=null && (datos.get("id") !=null && Integer.parseInt(datos.get("id"))>0) || esnuevo){
				if(esnuevo){
					proyecto = new Proyecto();
					proyecto.setNombre(datos.get("nombre"));
					proyecto.setDescripcion(datos.get("descripcion"));
					proyecto.setSnip(Integer.parseInt(datos.get("snip")));
					proyecto.setEstado(1);
					
					ProyectoTipo proyectoTipo = new ProyectoTipo();
					proyectoTipo.setId(Integer.parseInt(datos.get("proyectotipoid")));
					
					UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
					unidadEjecutora.setUnidadEjecutora(Integer.parseInt(datos.get("unidadejecutoraid")));
					
					Cooperante cooperante = new Cooperante();
					cooperante.setId(Integer.parseInt(datos.get("cooperanteid")));
					
					proyecto.setProyectoTipo(proyectoTipo);
					proyecto.setUnidadEjecutora(unidadEjecutora);
					proyecto.setCooperante(cooperante);
					
					proyecto.setUsuarioCreo("admin");
					proyecto.setFechaCreacion(new Date());
					
					
					
				}else{
					proyecto = ProyectoDAO.getProyectoPorId(Integer.parseInt(datos.get("id")));
					proyecto.setNombre(datos.get("nombre"));
					proyecto.setDescripcion(datos.get("descripcion"));
					proyecto.setSnip(Integer.parseInt(datos.get("snip")));
					proyecto.setEstado(1);
					
					ProyectoTipo proyectoTipo = new ProyectoTipo();
					proyectoTipo.setId(Integer.parseInt(datos.get("proyectotipoid")));
					
					UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
					unidadEjecutora.setUnidadEjecutora(Integer.parseInt(datos.get("unidadejecutoraid")));
					
					Cooperante cooperante = new Cooperante();
					cooperante.setId(Integer.parseInt(datos.get("cooperanteid")));
					
					proyecto.setProyectoTipo(proyectoTipo);
					proyecto.setUnidadEjecutora(unidadEjecutora);
					proyecto.setCooperante(cooperante);
					
					proyecto.setUsuarioActualizo("admin");
					proyecto.setFechaActualizacion(new Date());
				}
				boolean result = ProyectoDAO.guardarProyecto(proyecto);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}else
				response_text = "{ \"success\": false }";
			
			response.getWriter().write(response_text);

		} else if (accion.equals("cargar_cooperantes")){
			List<Cooperante> cooperantes = CooperanteDAO.getCooperantes();
			List<listas> listas_ = new ArrayList<listas>();
			for (Cooperante cooperante : cooperantes){
				listas lista = new listas();
				lista.id = cooperante.getId();
				lista.nombre = cooperante.getNombre();
				listas_.add(lista);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(listas_);
			response_text = String.join("", "\"entidades\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
			response.getWriter().write(response_text);
		}
		else if (accion.equals("cargar_proyectotipos")){
			List<ProyectoTipo> proyectotipos = ProyectoTipoDAO.getProyectoTipos();
			List<listas> listas_ = new ArrayList<listas>();
			for (ProyectoTipo proyectoTipo : proyectotipos){
				listas lista = new listas();
				lista.id =  proyectoTipo.getId();
				lista.nombre = proyectoTipo.getNombre();
				listas_.add(lista);
			} 
						
			response_text = new GsonBuilder().serializeNulls().create().toJson(listas_);
			response_text = String.join("", "\"entidades\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
			response.getWriter().write(response_text);
		}
		else if (accion.equals("cargar_unidadesejecturoas")){
			List<UnidadEjecutora> UnidadesEjecutoras = UnidadEjecutoraDAO.getUnidadEjecutoras();
			List<listas> listas_ = new ArrayList<>();
			for (UnidadEjecutora unidadEjecutora : UnidadesEjecutoras){
				listas lista = new listas();
				lista.id =  unidadEjecutora.getUnidadEjecutora();
				lista.nombre = unidadEjecutora.getNombre();
				listas_.add(lista);
			} 
						
			response_text = new GsonBuilder().serializeNulls().create().toJson(listas_);
			response_text = String.join("", "\"entidades\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
			response.getWriter().write(response_text);
		}
		
		
		
		
		else if(accion.equals("borrarProyecto")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(id);
				response_text = String.join("","{ \"success\": ",(ProyectoDAO.eliminarProyecto(proyecto) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
			response.getWriter().write(response_text);
			
		}
		else if(accion.equals("numeroProyectos")){
			response_text = String.join("","{ \"success\": true, \"totalproyectos\":",ProyectoDAO.getTotalProyectos().toString()," }");
			response.getWriter().write(response_text);
		
		}
		

		
	}

}
