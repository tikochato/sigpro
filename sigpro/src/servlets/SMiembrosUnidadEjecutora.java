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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import dao.ColaboradorDAO;
import dao.ProyectoDAO;
import dao.ProyectoRolColaboradorDAO;
import dao.RolUnidadEjecutoraDAO;
import pojo.Colaborador;
import pojo.Proyecto;
import pojo.ProyectoRolColaborador;
import pojo.RolUnidadEjecutora;
import pojo.Usuario;
import utilities.Utils;


@WebServlet("/SMiembrosUnidadEjecutora")
public class SMiembrosUnidadEjecutora extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stmiembro {
		Integer id;
		Integer colaboradorId;
		String primerNombre;
		String segundoNombre;
		String primerApellido;
		String segundoApellido;
		Integer proyectoId;
		Integer estado;
		Integer rolUnidadEjecutoraId;
		String rolUnidadEjecotoraNombre;
		String email;
		boolean guardado = true;
	}
	
	class stcolaborador {
		Integer id;
		String nombre;
		String email;
	}
	
	class strol {
		Integer id;
		String nombre;
		Integer predeterminado;
	}
       
    
    public SMiembrosUnidadEjecutora() {
        super();
    
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
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
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";

		if (accion.equals("getMiembros")) {
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"),null);
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			List<ProyectoRolColaborador> colaboradores = ProyectoRolColaboradorDAO.getMiembrosPorProyecto(proyectoId);
			
			List<RolUnidadEjecutora> roles = RolUnidadEjecutoraDAO.getRoles();
			List<strol> listaRoles = new ArrayList<strol>();
			for (RolUnidadEjecutora rol : roles){
				strol temp = new strol();
				temp.id = rol.getId();
				temp.nombre = rol.getNombre();
				temp.predeterminado = rol.getRolPredeterminado();
				listaRoles.add(temp);
			}
			
			List<stmiembro> miembros = new ArrayList<stmiembro>();
			for(ProyectoRolColaborador colaborador : colaboradores){
				stmiembro temp = new stmiembro();
				temp.colaboradorId = colaborador.getColaborador() != null ? colaborador.getColaborador().getId() : null;
				if (colaborador.getColaborador()!=null){
					temp.primerNombre = colaborador.getColaborador().getPnombre();
					temp.segundoNombre = colaborador.getColaborador().getSnombre() != null ? colaborador.getColaborador().getSnombre() :null;
					temp.primerApellido = colaborador.getColaborador().getPapellido();
					temp.segundoApellido = colaborador.getColaborador().getSapellido() != null ? colaborador.getColaborador().getSapellido() : null;
				}
				temp.estado = colaborador.getEstado();
				temp.id = colaborador.getId();
				temp.proyectoId = colaborador.getProyecto() != null ?  colaborador.getProyecto().getId() : null;
				temp.rolUnidadEjecotoraNombre = colaborador.getRolUnidadEjecutora().getNombre();
				temp.rolUnidadEjecutoraId = colaborador.getRolUnidadEjecutora().getId();
				temp.guardado = temp.colaboradorId != null;
				Usuario u = colaborador.getColaborador()!= null ? colaborador.getColaborador().getUsuario() : null;
				if (u!= null)
					temp.email = u.getEmail();
				miembros.add(temp);	
			}
			
			List<Colaborador> colaboradoresDisponibles = ColaboradorDAO.getColaboradorPorUnidadEjecutora(proyecto.getUnidadEjecutora().getId().getEjercicio(),
					proyecto.getUnidadEjecutora().getId().getUnidadEjecutora(),
					proyecto.getUnidadEjecutora().getId().getEntidadentidad());
			
			List<stcolaborador> stcolaboradores = new ArrayList<stcolaborador>();
			for (Colaborador colaborador : colaboradoresDisponibles){
				stcolaborador temp = new stcolaborador();
				Usuario u = colaborador.getUsuario();
				if (u!= null)
					temp.email = u.getEmail();
				
				temp.nombre = String.join(" ", colaborador.getPnombre(), 
						colaborador.getSnombre() != null ? colaborador.getSnombre() : "",
						colaborador.getPapellido(),
						colaborador.getSapellido() != null ? colaborador.getSapellido() : "");
				temp.id = colaborador.getId();
				stcolaboradores.add(temp);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(miembros);
			String response_text_colaborador = new GsonBuilder().serializeNulls().create().toJson(stcolaboradores);
			String response_text_roles = new GsonBuilder().serializeNulls().create().toJson(listaRoles);
			response_text = String.join("", "\"miembros\":", 
					response_text_colaborador != null ? response_text : null,
					",\"colaboradores\":",response_text_colaborador != null ? response_text_colaborador : null ,
					",\"roles\":",response_text_roles != null ? response_text_roles : null);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if (accion.equals("guardarMiembros")) {
			boolean result = true;
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"),null);
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			
			String miembros = map.get("miembros");
			JsonParser parser = new JsonParser();
			JsonArray miembrosArreglo = parser.parse(miembros).getAsJsonArray();
			
			
			List<ProyectoRolColaborador> colaboradores = ProyectoRolColaboradorDAO.getMiembrosPorProyecto(proyectoId);
			for (ProyectoRolColaborador colaborador : colaboradores){
				result = result && ProyectoRolColaboradorDAO.eliminarTotalMiembro(colaborador);
			}
			
			
			for(int i=0; i<miembrosArreglo.size(); i++){
				JsonObject objeto = miembrosArreglo.get(i).getAsJsonObject();
				
				Colaborador colaborador;
				if(!objeto.get("colaboradorId").isJsonNull()){
					colaborador = new Colaborador();
					colaborador.setId(Utils.String2Int(objeto.get("colaboradorId").getAsString(),0));
				}else{
					colaborador = ColaboradorDAO.getColaboradorByNombre(
						objeto.get("primerNombre").getAsString(), 
						objeto.get("segundoNombre").isJsonNull() ? null:   objeto.get("segundoNombre").getAsString() , 
						objeto.get("primerApellido").getAsString(),
						objeto.get("segundoApellido").isJsonNull() ? null : objeto.get("segundoApellido").getAsString()
					);
					
					if (colaborador == null){
						colaborador = new Colaborador(proyecto.getUnidadEjecutora(), null, 
								objeto.get("primerNombre").getAsString(), 
								objeto.get("segundoNombre").isJsonNull() ? null:   objeto.get("segundoNombre").getAsString(), 
								objeto.get("primerApellido").getAsString(), 
								objeto.get("segundoApellido").isJsonNull() ? null : objeto.get("segundoApellido").getAsString(), 
								0 , 1, usuario, null, new Date(), null, null, null, null, null, null);
						ColaboradorDAO.guardarColaborador(colaborador);
					}
				}
				
				RolUnidadEjecutora rol = new RolUnidadEjecutora();
				rol.setId(objeto.get("rolUnidadEjecutoraId") != null ? Integer.parseInt(objeto.get("rolUnidadEjecutoraId").getAsString()) : 0);
				
				ProyectoRolColaborador proyCol = new ProyectoRolColaborador();
				proyCol.setColaborador(colaborador);
				proyCol.setEstado(1);
				proyCol.setFechaCreacion(new Date());
				proyCol.setProyecto(proyecto);
				proyCol.setRolUnidadEjecutora(rol);
				proyCol.setUsuarioCreo(usuario);
				
				result = result && ProyectoRolColaboradorDAO.guardarMiembro(proyCol);
			}
			response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
		}
		else
			response_text = "{ \"success\": false }";
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
