package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.RecursoDAO;
import dao.RecursoTipoDAO;
import dao.RecursoUnidadMedidaDAO;
import pojo.Recurso;
import pojo.RecursoTipo;
import pojo.RecursoUnidadMedida;
import utilities.CLogger;
import utilities.Utils;

/**
 * Servlet implementation class SRecurso
 */

@WebServlet("/SRecurso")
public class SRecurso extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class strecurso{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		Integer recursotipoid;
		String recursotiponombre;
		Integer medidaid;
		String medidanombre;
		int estado;
	}

	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SRecurso() {
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
		if(accion.equals("getRecursosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRecursos = map.get("numerorecursos")!=null  ? Integer.parseInt(map.get("numerorecursos")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Recurso> recursos = RecursoDAO.getRecursosPagina(pagina, numeroRecursos, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,
					columna_ordenada, orden_direccion);
			List<strecurso> strecursos=new ArrayList<strecurso>();
			for(Recurso recurso:recursos){
				strecurso temp =new strecurso();
				temp.descripcion = recurso.getDescripcion();
				temp.estado = recurso.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(recurso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(recurso.getFechaCreacion());
				temp.id = recurso.getId();
				temp.nombre = recurso.getNombre();
				temp.recursotipoid = recurso.getRecursoTipo().getId();
				temp.recursotiponombre = recurso.getRecursoTipo().getNombre();
				temp.medidaid = recurso.getRecursoUnidadMedida().getId();
				temp.medidanombre = recurso.getRecursoUnidadMedida().getNombre();
				temp.usuarioActualizo = recurso.getUsuarioActualizo();
				temp.usuarioCreo = recurso.getUsuarioCreo();
				temp.recursotipoid = recurso.getRecursoTipo().getId();
				temp.recursotiponombre = recurso.getRecursoTipo().getNombre();
				strecursos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursos);
	        response_text = String.join("", "\"recursos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRecursos")){
			List<Recurso> recursos = RecursoDAO.getRecursos();
			List<strecurso> strecursos=new ArrayList<strecurso>();
			for(Recurso recurso:recursos){
				strecurso temp =new strecurso();

				temp.descripcion = recurso.getDescripcion();
				temp.estado = recurso.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(recurso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(recurso.getFechaCreacion());
				temp.id = recurso.getId();
				temp.nombre = recurso.getNombre();
				temp.recursotipoid = recurso.getRecursoTipo().getId();
				temp.recursotiponombre = recurso.getRecursoTipo().getNombre();
				temp.medidaid = recurso.getRecursoUnidadMedida().getId();
				temp.medidanombre = recurso.getRecursoUnidadMedida().getNombre();
				temp.usuarioActualizo = recurso.getUsuarioActualizo();
				temp.usuarioCreo = recurso.getUsuarioCreo();
				temp.recursotipoid = recurso.getRecursoTipo().getId();
				temp.recursotiponombre = recurso.getRecursoTipo().getNombre();
				strecursos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursos);
	        response_text = String.join("", "\"recursos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarRecurso")){
			try{
				boolean result = false;
				boolean esnuevo = map.get("esnuevo").equals("true");
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				if(id>0 || esnuevo){
					String nombre = map.get("nombre");
					String descripcion = map.get("descripcion");
					int recursotipoid = map.get("recursotipoid")!=null ? Integer.parseInt(map.get("recursotipoid")) : 0;
					int unidadmedida = map.get("unidadmedida")!=null ? Integer.parseInt(map.get("unidadmedida")) : 0;
					
					RecursoTipo recursoTipo= RecursoTipoDAO.getRecursoTipoPorId(recursotipoid);
					RecursoUnidadMedida unidadMedida = RecursoUnidadMedidaDAO.getRecursoUnidadMedidaPorId(unidadmedida);
					
					

					type = new TypeToken<List<stdatadinamico>>() {
					}.getType();

					Recurso recurso;
					if(esnuevo){
						recurso = new Recurso(recursoTipo, unidadMedida, nombre, descripcion, usuario,null, new DateTime().toDate(), null,1, null);
					}
					else{
						recurso = RecursoDAO.getRecursoPorId(id);
						recurso.setNombre(nombre);
						recurso.setDescripcion(descripcion);
						recurso.setRecursoUnidadMedida(unidadMedida);
						recurso.setUsuarioActualizo(usuario);
						recurso.setFechaActualizacion(new DateTime().toDate());
					}
					result = RecursoDAO.guardarRecurso(recurso);

					response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
							, "\"id\": " , recurso.getId().toString() , ","
							, "\"usuarioCreo\": \"" , recurso.getUsuarioCreo(),"\","
							, "\"fechaCreacion\":\" " , Utils.formatDateHour(recurso.getFechaCreacion()),"\","
							, "\"usuarioactualizo\": \"" , recurso.getUsuarioActualizo() != null ? recurso.getUsuarioActualizo() : "","\","
							, "\"fechaactualizacion\": \"" , Utils.formatDateHour(recurso.getFechaActualizacion()),"\""
							," }");
				}
				else
					response_text = "{ \"success\": false }";
			}
			catch (Throwable e){
				CLogger.write("1", SRecurso.class, e);
				response_text = "{ \"success\": false }";
			}
		}
		else if(accion.equals("borrarRecurso")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Recurso recurso = RecursoDAO.getRecursoPorId(id);
				recurso.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(RecursoDAO.eliminarRecurso(recurso) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroRecursos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalrecursos\":",RecursoDAO.getTotalRecursos(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("numeroRecursosPorObjeto")){
			int objetoId = map.get("objetoid")!=null  ? Integer.parseInt(map.get("objetoid")) : 0;
			int tipoObjeto = map.get("tipoobjeto")!=null  ? Integer.parseInt(map.get("tipoobjeto")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalrecursos\":",RecursoDAO.getTotalRecursosPorObjeto(objetoId,tipoObjeto).toString()," }");
		}
		else if(accion.equals("getRecursosPaginaPorObjeto")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int objetoId = map.get("objetoid")!=null  ? Integer.parseInt(map.get("objetoid")) : 0;
			int tipoObjeto = map.get("tipoobjeto")!=null  ? Integer.parseInt(map.get("tipoobjeto")) : 0;
			int numeroRecursos = map.get("numerorecursos")!=null  ? Integer.parseInt(map.get("numerorecursos")) : 0;
			List<Recurso> recursos = RecursoDAO.getRecursosPaginaPorObjeto(pagina, numeroRecursos,objetoId, tipoObjeto);
			List<strecurso> strecursos=new ArrayList<strecurso>();
			for(Recurso recurso:recursos){
				strecurso temp =new strecurso();
				temp.descripcion = recurso.getDescripcion();
				temp.estado = recurso.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(recurso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(recurso.getFechaCreacion());
				temp.id = recurso.getId();
				temp.nombre = recurso.getNombre();
				temp.recursotipoid = recurso.getRecursoTipo().getId();
				temp.recursotiponombre = recurso.getRecursoTipo().getNombre();
				temp.medidaid = recurso.getRecursoUnidadMedida().getId();
				temp.medidanombre = recurso.getRecursoUnidadMedida().getNombre();
				temp.usuarioActualizo = recurso.getUsuarioActualizo();
				temp.usuarioCreo = recurso.getUsuarioCreo();
				temp.recursotipoid = recurso.getRecursoTipo().getId();
				temp.recursotiponombre = recurso.getRecursoTipo().getNombre();
				strecursos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursos);
	        response_text = String.join("", "\"recursos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
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
