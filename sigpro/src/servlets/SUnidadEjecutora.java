package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EntidadDAO;
import dao.UnidadEjecutoraDAO;
import pojo.UnidadEjecutora;
import utilities.Utils;

@WebServlet("/SUnidadEjecutora")
public class SUnidadEjecutora extends HttpServlet {
    private static final long serialVersionUID = -6537014370076177564L;

    public SUnidadEjecutora() {
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
	}
    }

    private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
	
		String resultadoJson = "";
	
		resultadoJson = UnidadEjecutoraDAO.getJson(pagina, registros);
	
		if (Utils.isNullOrEmpty(resultadoJson)) {
		    resultadoJson = "{\"success\":false}";
		} else {
		    resultadoJson = "{\"success\":true,"
		                    + resultadoJson
		                    + "}";
		}
	
		Utils.writeJSon(response, resultadoJson);
    }

    private void crear(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"));
		String nombre = parametro.get("nombre");
		int codigoEntidad = Utils.String2Int(parametro.get("entidad"));
	
		//boolean creado = UnidadEjecutoraDAO.guardar(codigo, nombre, codigoEntidad);
		UnidadEjecutora pojo = UnidadEjecutoraDAO.getUnidadEjecutora(codigo);
		pojo.setUnidadEjecutora(codigo);
		pojo.setNombre(nombre);
		pojo.setEntidad(EntidadDAO.getEntidad(codigoEntidad));
		
		boolean creado = UnidadEjecutoraDAO.guardarUnidadEjecutora(pojo);
		if (creado) {
		    listar(parametro, response);
		}
    }

    private void actualizar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"));
		String nombre = parametro.get("nombre");
		int codigoEntidad = Utils.String2Int(parametro.get("entidad"));
	
		boolean actualizado = UnidadEjecutoraDAO.actualizar(codigo, nombre, codigoEntidad);
	
		if (actualizado) {
		    listar(parametro, response);
		}
    }

    private void total(HttpServletResponse response) throws IOException {
		Long total = UnidadEjecutoraDAO.getTotal();
	
		String resultadoJson = "{\"success\":true, \"total\":"
		                       + total
		                       + "}";
	
		Utils.writeJSon(response, resultadoJson);
    }

}
