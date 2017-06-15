package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DatoTipoDAO;
import pojo.DatoTipo;
import utilities.Utils;

@WebServlet("/SDatoTipo")
public class SDatoTipo extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;

	public SDatoTipo() {
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
			listar(response);
		} else if (parametro.get("accion").compareTo("cargarCombo") == 0) {
			listarCombo(response);
		} else if (parametro.get("accion").compareTo("getDatoTipoPorId") == 0) {
			Integer id = parametro.get("id")!=null ? Integer.parseInt(parametro.get("id")) : 0;
			getDatoTipoPorId(response, id);
		}

	}

	private void listar(HttpServletResponse response) throws IOException {
		String resultadoJson = "";

		resultadoJson = DatoTipoDAO.getJson();

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarCombo(HttpServletResponse response) throws IOException {
		String resultadoJson = "";

		resultadoJson = DatoTipoDAO.getJsonCombo();

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void getDatoTipoPorId(HttpServletResponse response, int id) throws IOException {
		String resultadoJson = "";

		DatoTipo datotipo= DatoTipoDAO.getDatoTipo(id);
		resultadoJson = String.join("","{ \"success\": ",(datotipo!=null && datotipo.getId()!=null ? "true" : "false"),", "
				+ "\"id\": " + (datotipo!=null ? datotipo.getId():"0") +", "
				+ "\"nombre\": \"" + (datotipo!=null ? datotipo.getNombre():"") +"\" }");
		
		Utils.writeJSon(response, resultadoJson);
	}
}
