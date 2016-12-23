package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		if (parametro.get("accion").compareTo("cargar") == 0) {
			listar(parametro, response);
		} else if (parametro.get("accion").compareTo("crear") == 0) {
			crear(parametro, response);
		} else if (parametro.get("accion").compareTo("actualizar") == 0) {
			actualizar(parametro, response);
		} else if (parametro.get("accion").compareTo("totalElementos") == 0) {
			total(response);
		} else if (parametro.get("accion").compareTo("validarUsuario") == 0) {
			validar(parametro, response);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);

		String resultadoJson = "";

		resultadoJson = ColaboradorDAO.getJson(pagina, registros);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void crear(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		String primerNombre = parametro.get("primerNombre");
		String segundoNombre = parametro.get("segundoNombre");
		String primerApellido = parametro.get("primerApellido");
		String segundoApellido = parametro.get("segundoApellido");
		Long cui = Utils.String2Long(parametro.get("cui"));
		Integer codigoUnidadEjecutora = Utils.String2Int(parametro.get("unidadEjecutora"), -1);
		String usuario = parametro.get("usuario");

		boolean creado = ColaboradorDAO.guardar(-1, primerNombre, segundoNombre, null, primerApellido, segundoApellido,
				null, cui, codigoUnidadEjecutora, usuario);

		if (creado) {
			listar(parametro, response);
		}
	}

	private void actualizar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		String primerNombre = parametro.get("primerNombre");
		String segundoNombre = parametro.get("segundoNombre");
		String primerApellido = parametro.get("primerApellido");
		String segundoApellido = parametro.get("segundoApellido");
		Long cui = Utils.String2Long(parametro.get("cui"));
		Integer codigoUnidadEjecutora = Utils.String2Int(parametro.get("unidadEjecutora"), -1);
		String usuario = parametro.get("usuario");

		boolean actualizado = ColaboradorDAO.actualizar(codigo, primerNombre, segundoNombre, null, primerApellido,
				segundoApellido, null, cui, codigoUnidadEjecutora, usuario);

		if (actualizado) {
			listar(parametro, response);
		}
	}

	private void total(HttpServletResponse response) throws IOException {
		Long total = ColaboradorDAO.getTotal();

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
