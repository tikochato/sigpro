package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EntidadDAO;
import utilities.Utils;

@WebServlet("/SEntidad")
public class SEntidad extends HttpServlet {
    private static final long serialVersionUID = 7415363423617383848L;

    public SEntidad() {
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
	    listarEntidades(parametro, response);
	} else if (parametro.get("accion").compareTo("crear") == 0) {
	    crearEntidad(parametro, response);
	} else if (parametro.get("accion").compareTo("actualizar") == 0) {
	    actualizarEntidad(parametro, response);
	} else if (parametro.get("accion").compareTo("totalEntidades") == 0) {
	    totalEntidades(response);
	}

    }

    private void listarEntidades(Map<String, String> parametro, HttpServletResponse response) throws IOException {
	int pagina = Utils.String2Int(parametro.get("pagina"), 1);
	int registros = Utils.String2Int(parametro.get("registros"), 20);

	String jsonEntidades = "";

	jsonEntidades = EntidadDAO.getJsonEntidades(pagina, registros);

	if (Utils.isNullOrEmpty(jsonEntidades)) {
	    jsonEntidades = "{\"success\":false}";
	} else {
	    jsonEntidades = "{\"success\":true,"
	                    + jsonEntidades
	                    + "}";
	}

	Utils.writeJSon(response, jsonEntidades);
    }

    private void crearEntidad(Map<String, String> parametro, HttpServletResponse response) throws IOException {
	Integer entidad = Utils.String2Int(parametro.get("entidad"), -1);
	String nombre = parametro.get("nombre");
	String abreviatura = parametro.get("abreviatura");

	boolean creado = EntidadDAO.guardarEntidad(entidad, nombre, abreviatura);

	if (creado) {
	    listarEntidades(parametro, response);
	} else {

	}
    }

    private void actualizarEntidad(Map<String, String> parametro, HttpServletResponse response) throws IOException {
	Integer entidad = Utils.String2Int(parametro.get("entidad"), -1);
	String abreviatura = parametro.get("abreviatura");

	boolean actualizado = EntidadDAO.actualizarEntidad(entidad, abreviatura);

	if (actualizado) {
	    listarEntidades(parametro, response);
	}
    }

    private void totalEntidades(HttpServletResponse response) throws IOException {
	Long total = EntidadDAO.getTotalEntidades();

	String jsonEntidades = "{\"success\":true, \"total\":"
	                       + total
	                       + "}";

	Utils.writeJSon(response, jsonEntidades);

    }

}
