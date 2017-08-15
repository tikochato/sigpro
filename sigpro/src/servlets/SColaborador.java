package servlets;

import java.io.IOException;
import java.io.OutputStream;
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

import com.google.gson.GsonBuilder;

import dao.AsignacionRaciDAO;
import dao.ColaboradorDAO;
import pojo.AsignacionRaci;
import pojo.Colaborador;
import utilities.Utils;

@WebServlet("/SColaborador")
public class SColaborador extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;
	
	static class stcolaborador {
		Integer id;
		String primerNombre;
		String segundoNombre;
		String otrosNombres;
		String primerApellido;
		String segundoApellido;
		String otrosApellidos;
		Long cui;
		Integer unidadEjecutora;
		String nombreUnidadEjecutora;
		String usuario;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		String nombreCompleto;
	}

	public SColaborador() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Map<String, String> parametro = Utils.getParams(request);
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String accion = parametro.get("accion");
		String response_text="";
		
		if (accion.equals("cargar")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);
			String filtro_pnombre = parametro.get("filtro_pnombre");
			String filtro_snombre = parametro.get("filtro_snombre");
			String filtro_papellido = parametro.get("filtro_papellido");
			String filtro_sapellido = parametro.get("filtro_sapellido");
			String filtro_cui = parametro.get("filtro_cui");
			String filtro_unidad_ejecutora = parametro.get("filtro_unidad_ejecutora");
			String columna_ordenada  = parametro.get("columna_ordenada");
			String orden_direccion = parametro.get("orden_direccion");
			String excluir = parametro.get("idResponsables");
			
			List<Colaborador> colaboradores = ColaboradorDAO.getPagina(pagina, registros, filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido,
						filtro_cui, filtro_unidad_ejecutora, columna_ordenada, orden_direccion,excluir);

			List<stcolaborador> listaColaborador = new ArrayList<stcolaborador>();

			for (Colaborador colaborador : colaboradores) {
				stcolaborador temp = new stcolaborador();
				temp.id = colaborador.getId();
				temp.primerNombre = colaborador.getPnombre();
				temp.segundoNombre = colaborador.getSnombre();
				temp.primerApellido = colaborador.getPapellido();
				temp.segundoApellido = colaborador.getSapellido();
				temp.cui = colaborador.getCui();

				temp.usuario = colaborador.getUsuario().getUsuario();
				temp.unidadEjecutora = colaborador.getUnidadEjecutora().getUnidadEjecutora();
				temp.nombreUnidadEjecutora = colaborador.getUnidadEjecutora().getNombre();
				
				temp.usuarioCreo = colaborador.getUsuarioCreo();
				temp.usuarioActualizo = colaborador.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(colaborador.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(colaborador.getFechaActualizacion());
				temp.nombreCompleto = String.join(" ", temp.primerNombre,
						temp.segundoNombre!=null ? temp.segundoNombre : "" ,
						temp.primerApellido !=null ? temp.primerApellido : "" ,
						temp.segundoApellido !=null ? temp.segundoApellido : "");
				
				listaColaborador.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(listaColaborador);
	        response_text = String.join("", "\"colaboradores\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");	
		
		} else if (accion.equals("crear")) {
			String primerNombre = parametro.get("primerNombre");
			String segundoNombre = parametro.get("segundoNombre");
			String primerApellido = parametro.get("primerApellido");
			String segundoApellido = parametro.get("segundoApellido");
			Long cui = Utils.String2Long(parametro.get("cui"));
			Integer codigoUnidadEjecutora = Utils.String2Int(parametro.get("unidadEjecutora"), -1);
			String usuarioParametro = parametro.get("usuario");

			boolean creado = ColaboradorDAO.guardar(-1, primerNombre, segundoNombre, null, primerApellido, segundoApellido,
					null, cui, codigoUnidadEjecutora, usuarioParametro, usuario, new Date());

			if (creado) {
				int pagina = Utils.String2Int(parametro.get("pagina"), 1);
				int registros = Utils.String2Int(parametro.get("registros"), 20);
				String filtro_pnombre = parametro.get("filtro_pnombre");
				String filtro_snombre = parametro.get("filtro_snombre");
				String filtro_papellido = parametro.get("filtro_papellido");
				String filtro_sapellido = parametro.get("filtro_sapellido");
				String filtro_cui = parametro.get("filtro_cui");
				String filtro_unidad_ejecutora = parametro.get("filtro_unidad_ejecutora");
				String columna_ordenada  = parametro.get("columna_ordenada");
				String orden_direccion = parametro.get("orden_direccion");
				
				List<Colaborador> colaboradores = ColaboradorDAO.getPagina(pagina, registros, filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido,
							filtro_cui, filtro_unidad_ejecutora, columna_ordenada, orden_direccion,null);

				List<stcolaborador> listaColaborador = new ArrayList<stcolaborador>();

				for (Colaborador colaborador : colaboradores) {
					stcolaborador temp = new stcolaborador();
					temp.id = colaborador.getId();
					temp.primerNombre = colaborador.getPnombre();
					temp.segundoNombre = colaborador.getSnombre();
					temp.primerApellido = colaborador.getPapellido();
					temp.segundoApellido = colaborador.getSapellido();
					temp.cui = colaborador.getCui();

					temp.usuario = colaborador.getUsuario().getUsuario();
					temp.unidadEjecutora = colaborador.getUnidadEjecutora().getUnidadEjecutora();
					temp.nombreUnidadEjecutora = colaborador.getUnidadEjecutora().getNombre();
					
					temp.usuarioCreo = colaborador.getUsuarioCreo();
					temp.usuarioActualizo = colaborador.getUsuarioActualizo();
					temp.fechaCreacion = Utils.formatDateHour(colaborador.getFechaCreacion());
					temp.fechaActualizacion = Utils.formatDateHour(colaborador.getFechaActualizacion());
					temp.nombreCompleto = String.join(" ", temp.primerNombre,
							temp.segundoNombre!=null ? temp.segundoNombre : "" ,
							temp.primerApellido !=null ? temp.primerApellido : "" ,
							temp.segundoApellido !=null ? temp.segundoApellido : "");
			
					listaColaborador.add(temp);
				}
				stcolaborador stColaborador = listaColaborador.get(listaColaborador.size()-1);
				String output_colaborador= new GsonBuilder().serializeNulls().create().toJson(stColaborador);
				response_text=new GsonBuilder().serializeNulls().create().toJson(listaColaborador);
		        response_text = String.join("", "\"colaboradores\":",response_text ,",\"colaborador\":",output_colaborador);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");	
			}
			
		} else if (accion.equals("actualizar")) {
			int id = Utils.String2Int(parametro.get("id"), -1);
			String primerNombre = parametro.get("primerNombre");
			String segundoNombre = parametro.get("segundoNombre");
			String primerApellido = parametro.get("primerApellido");
			String segundoApellido = parametro.get("segundoApellido");
			Long cui = Utils.String2Long(parametro.get("cui"));
			Integer codigoUnidadEjecutora = Utils.String2Int(parametro.get("unidadEjecutora"), -1);
			String usuarioParametro = parametro.get("usuario");

			boolean actualizado = ColaboradorDAO.actualizar(id, primerNombre, segundoNombre, primerApellido,
					segundoApellido, cui, codigoUnidadEjecutora, usuarioParametro, usuario);

			if (actualizado) {
				int pagina = Utils.String2Int(parametro.get("pagina"), 1);
				int registros = Utils.String2Int(parametro.get("registros"), 20);
				String filtro_pnombre = parametro.get("filtro_pnombre");
				String filtro_snombre = parametro.get("filtro_snombre");
				String filtro_papellido = parametro.get("filtro_papellido");
				String filtro_sapellido = parametro.get("filtro_sapellido");
				String filtro_cui = parametro.get("filtro_cui");
				String filtro_unidad_ejecutora = parametro.get("filtro_unidad_ejecutora");
				String columna_ordenada  = parametro.get("columna_ordenada");
				String orden_direccion = parametro.get("orden_direccion");
			
				List<Colaborador> colaboradores = ColaboradorDAO.getPagina(pagina, registros, filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido,
							filtro_cui, filtro_unidad_ejecutora, columna_ordenada, orden_direccion,null);

				List<stcolaborador> listaColaborador = new ArrayList<stcolaborador>();

				for (Colaborador colaborador : colaboradores) {
					stcolaborador temp = new stcolaborador();
					temp.id = colaborador.getId();
					temp.primerNombre = colaborador.getPnombre();
					temp.segundoNombre = colaborador.getSnombre();
					temp.primerApellido = colaborador.getPapellido();
					temp.segundoApellido = colaborador.getSapellido();
					temp.cui = colaborador.getCui();

					temp.usuario = colaborador.getUsuario().getUsuario();
					temp.unidadEjecutora = colaborador.getUnidadEjecutora().getUnidadEjecutora();
					temp.nombreUnidadEjecutora = colaborador.getUnidadEjecutora().getNombre();
					
					temp.usuarioCreo = colaborador.getUsuarioCreo();
					temp.usuarioActualizo = colaborador.getUsuarioActualizo();
					temp.fechaCreacion = Utils.formatDateHour(colaborador.getFechaCreacion());
					temp.fechaActualizacion = Utils.formatDateHour(colaborador.getFechaActualizacion());
					temp.nombreCompleto = String.join(" ", temp.primerNombre,
							temp.segundoNombre!=null ? temp.segundoNombre : "" ,
							temp.primerApellido !=null ? temp.primerApellido : "" ,
							temp.segundoApellido !=null ? temp.segundoApellido : "");
			
					listaColaborador.add(temp);
				}
				stcolaborador stColaborador = listaColaborador.get(listaColaborador.size()-1);
				String output_colaborador= new GsonBuilder().serializeNulls().create().toJson(stColaborador);
				response_text=new GsonBuilder().serializeNulls().create().toJson(listaColaborador);
				response_text = String.join("", "\"colaboradores\":",response_text ,",\"colaborador\":",output_colaborador);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");	
			
			}
			
		} else if (accion.equals("borrar")) {
			int id = Utils.String2Int(parametro.get("id"), -1);
			boolean borrado = ColaboradorDAO.borrar(id, usuario);
			if (borrado) 
				Utils.writeJSon(response, "{\"success\":true }");
			else
				Utils.writeJSon(response, "{\"success\":false }");
			
		}else if (accion.equals("totalElementos")) {
			String filtro_pnombre = parametro.get("filtro_pnombre");
			String filtro_snombre = parametro.get("filtro_snombre");
			String filtro_papellido = parametro.get("filtro_papellido");
			String filtro_sapellido = parametro.get("filtro_sapellido");
			String filtro_cui = parametro.get("filtro_cui");
			String filtro_unidad_ejecutora = parametro.get("filtro_unidad_ejecutora");
			Long total = ColaboradorDAO.getTotal(filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido, filtro_cui, filtro_unidad_ejecutora);

			response_text = "{\"success\":true, \"total\":" + total + "}";

		} else if (accion.equals("validarUsuario")) {
			String usuarioParametro = parametro.get("usuario");
			boolean valido = ColaboradorDAO.validarUsuario(usuarioParametro);

			response_text = "{\"success\":false}";;
			
			if(valido){
				response_text = "{\"success\":true}";
			}
			
		} else if (accion.equals("obtenerPorId")){
			
			
			Integer id = Utils.String2Int(parametro.get("id"), -1);
			AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(id, 5, "r");
			
			if (asignacion != null && asignacion.getColaborador() != null){
				stcolaborador temp = new stcolaborador();
				Colaborador colaborador = asignacion.getColaborador();
				temp.id = colaborador.getId();
				temp.primerNombre = colaborador.getPnombre();
				temp.segundoNombre = colaborador.getSnombre();
				temp.primerApellido = colaborador.getPapellido();
				temp.segundoApellido = colaborador.getSapellido();
				temp.cui = colaborador.getCui();

				temp.usuario = colaborador.getUsuario().getUsuario();
				temp.unidadEjecutora = colaborador.getUnidadEjecutora().getUnidadEjecutora();
				temp.nombreUnidadEjecutora = colaborador.getUnidadEjecutora().getNombre();
				
				temp.usuarioCreo = colaborador.getUsuarioCreo();
				temp.usuarioActualizo = colaborador.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(colaborador.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(colaborador.getFechaActualizacion());
				temp.nombreCompleto = String.join(" ", temp.primerNombre,
						temp.segundoNombre!=null ? temp.segundoNombre : "" ,
						temp.primerApellido !=null ? temp.primerApellido : "" ,
						temp.segundoApellido !=null ? temp.segundoApellido : "");
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
		        response_text = String.join("", "\"colaborador\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else{
				 response_text = String.join("", "{\"success\":false}");
			}
			
			
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
