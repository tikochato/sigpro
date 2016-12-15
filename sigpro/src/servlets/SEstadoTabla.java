package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.EstadoTablaDAO;
import pojo.Estadotabla;
import pojo.EstadotablaId;

/**
 * Servlet implementation class SEstadoTabla
 */
@WebServlet("/SEstadoTabla")
public class SEstadoTabla extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEstadoTabla() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";


		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };


	    Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = "";
		action = map.get("action");
		if(action!=null){
			if(action.compareTo("guardaEstado")==0){
				String usuariotexto="";
				usuariotexto="visita";
				HttpSession sesionweb = request.getSession();
				if(sesionweb.getAttribute("usuario")!=null){
					usuariotexto=sesionweb.getAttribute("usuario").toString();
				}
				String tablatexto="";
				tablatexto=map.get("grid");
				String estadotexto = "";
				estadotexto = map.get("estado");
				if(usuariotexto!=null&&tablatexto!=null&&estadotexto!=null){
					EstadotablaId Estadotablaid = new EstadotablaId(usuariotexto, tablatexto);
					Estadotabla Estadotabla = new Estadotabla(Estadotablaid,estadotexto);
					EstadoTablaDAO.saveEstadoTabla(Estadotabla);
				}else{
					response_text = String.join("", "{\"success\":false, \"error\":\"uno o mas parametros vacios\" }");
				}

			}
			else if(action.compareTo("getEstado")==0){
				String usuariotexto="";
				usuariotexto = "visita";
				String tablatexto="";
				tablatexto=map.get("grid");
				if(usuariotexto!=null&& tablatexto!=null){
					String estado =EstadoTablaDAO.getStadoTabla(usuariotexto, tablatexto);
					response_text = String.join("", "{\"success\":true, \"estado\":", estado.length()>0 ?  estado : "\"\""," }");
				}else{
					response_text = String.join("", "{\"success\":false, \"error\":\"uno o mas parametros vacios\" }");
				}
			}else{
				response_text = String.join("", "{\"success\":false, \"error\":\"parametro de accion no valido\" }");
			}

        }else{
        	response_text = String.join("", "{\"success\":false, \"error\":\"fatal parametro de accion\" }");
        }
		gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
