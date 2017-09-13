package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UnidadEjecutoraDAO;
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
		    total(parametro,response);
		} else if (parametro.get("accion").compareTo("cargarPorEntidad") == 0){
			listarPorEntidad(parametro, response);
		}
    }

    private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		int entidadId = Utils.String2Int(parametro.get("entidad"), 0);
		int ejercicio = Utils.String2Int(parametro.get("ejercicio"), 0);
		String resultadoJson = "";
	
		resultadoJson = UnidadEjecutoraDAO.getJson(pagina, registros, ejercicio, entidadId);
	
		if (Utils.isNullOrEmpty(resultadoJson)) {
		    resultadoJson = "{\"success\":false}";
		} else {
		    resultadoJson = "{\"success\":true,"
		                    + resultadoJson
		                    + "}";
		}
	
		Utils.writeJSon(response, resultadoJson);
    }
    
    private void listarPorEntidad(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		int entidadId = Utils.String2Int(parametro.get("entidadId"), 0);
		int ejercicio = Utils.String2Int(parametro.get("ejercicio"), 0);
		String resultadoJson = "";
	
		resultadoJson = UnidadEjecutoraDAO.getJsonPorEntidad(pagina, registros, entidadId, ejercicio);
	
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
    	try{
    		int codigo = Utils.String2Int(parametro.get("codigo"));
    		String nombre = parametro.get("nombre");
    		int codigoEntidad = Utils.String2Int(parametro.get("entidad"));
    		Integer ejercicio = Utils.String2Int(parametro.get("ejercicio"),-1);
    	   		
    		boolean creado = UnidadEjecutoraDAO.guardar(codigoEntidad, ejercicio, codigo, nombre);
    		if (creado) {
    		    listar(parametro, response);
    		}
    		
    	}catch(Throwable e){
    		e.printStackTrace();
    	}
		
    }

    private void actualizar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"));
		String nombre = parametro.get("nombre");
		int codigoEntidad = Utils.String2Int(parametro.get("entidad"));
		Integer ejercicio = Utils.String2Int(parametro.get("ejercicio"),-1);
	
		boolean actualizado = UnidadEjecutoraDAO.actualizar(codigoEntidad, ejercicio, codigo, nombre);
	
		if (actualizado) {
		    listar(parametro, response);
		}
    }

    private void total(Map<String, String> parametro,HttpServletResponse response) throws IOException {
		int codigoEntidad = Utils.String2Int(parametro.get("entidad"));
		Integer ejercicio = Utils.String2Int(parametro.get("ejercicio"),-1);
		Long total = UnidadEjecutoraDAO.getTotal(ejercicio, codigoEntidad);
		
		String resultadoJson = "{\"success\":true, \"total\":"
		                       + total
		                       + "}";
	
		Utils.writeJSon(response, resultadoJson);
    }

}
