package servlets;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ColaboradorDAO;
import utilities.Utils;

@WebServlet("/SColaborador")
public class SColaborador extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;

	public SColaborador() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> parametro = Utils.getParams(request);
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;

		if (parametro.get("accion").compareTo("cargar") == 0) {
			listar(parametro, response);
		} else if (parametro.get("accion").compareTo("crear") == 0) {
			crear(parametro, response, usuario);
		} else if (parametro.get("accion").compareTo("actualizar") == 0) {
			actualizar(parametro, response, usuario);
		} else if (parametro.get("accion").compareTo("borrar") == 0) {
			borrar(parametro, response, usuario);
		}else if (parametro.get("accion").compareTo("totalElementos") == 0) {
			total(parametro,response);
		} else if (parametro.get("accion").compareTo("validarUsuario") == 0) {
			validar(parametro, response);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
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
		String resultadoJson = "";

		resultadoJson = ColaboradorDAO.getJson(pagina, registros, filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido, filtro_cui, filtro_unidad_ejecutora,
				columna_ordenada, orden_direccion);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void crear(Map<String, String> parametro, HttpServletResponse response, String usuarioc) throws IOException {
		String primerNombre = parametro.get("primerNombre");
		String segundoNombre = parametro.get("segundoNombre");
		String primerApellido = parametro.get("primerApellido");
		String segundoApellido = parametro.get("segundoApellido");
		Long cui = Utils.String2Long(parametro.get("cui"));
		Integer codigoUnidadEjecutora = Utils.String2Int(parametro.get("unidadEjecutora"), -1);
		String usuario = parametro.get("usuario");

		boolean creado = ColaboradorDAO.guardar(-1, primerNombre, segundoNombre, null, primerApellido, segundoApellido,
				null, cui, codigoUnidadEjecutora, usuario, usuarioc, new Date());

		if (creado) {
			listar(parametro, response);
		}
	}

	private void actualizar(Map<String, String> parametro, HttpServletResponse response, String usuarioc) throws IOException {
		int id = Utils.String2Int(parametro.get("id"), -1);
		String primerNombre = parametro.get("primerNombre");
		String segundoNombre = parametro.get("segundoNombre");
		String primerApellido = parametro.get("primerApellido");
		String segundoApellido = parametro.get("segundoApellido");
		Long cui = Utils.String2Long(parametro.get("cui"));
		Integer codigoUnidadEjecutora = Utils.String2Int(parametro.get("unidadEjecutora"), -1);
		String usuario = parametro.get("usuario");

		boolean actualizado = ColaboradorDAO.actualizar(id, primerNombre, segundoNombre, primerApellido,
				segundoApellido, cui, codigoUnidadEjecutora, usuario, usuarioc);

		if (actualizado) {
			listar(parametro, response);
		}
	}
	
	private void borrar(Map<String, String> parametro, HttpServletResponse response, String usuarioc) throws IOException {
		int id = Utils.String2Int(parametro.get("id"), -1);
		boolean borrado = ColaboradorDAO.borrar(id, usuarioc);
		if (borrado) 
			Utils.writeJSon(response, "{\"success\":true }");
		else
			Utils.writeJSon(response, "{\"success\":false }");
	}

	private void total(Map<String, String> parametro,HttpServletResponse response) throws IOException {
		String filtro_pnombre = parametro.get("filtro_pnombre");
		String filtro_snombre = parametro.get("filtro_snombre");
		String filtro_papellido = parametro.get("filtro_papellido");
		String filtro_sapellido = parametro.get("filtro_sapellido");
		String filtro_cui = parametro.get("filtro_cui");
		String filtro_unidad_ejecutora = parametro.get("filtro_unidad_ejecutora");
		Long total = ColaboradorDAO.getTotal(filtro_pnombre, filtro_snombre, filtro_papellido, filtro_sapellido, filtro_cui, filtro_unidad_ejecutora);

		String resultadoJson = "{\"success\":true, \"total\":" + total + "}";

		Utils.writeJSon(response, resultadoJson);
	}

	private void validar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		String usuario = parametro.get("usuario");
		boolean valido = ColaboradorDAO.validarUsuario(usuario);

		String resultadoJson = "{\"success\":false}";;
		
		if(valido)
			resultadoJson = "{\"success\":true}";

		Utils.writeJSon(response, resultadoJson);
	}

}
