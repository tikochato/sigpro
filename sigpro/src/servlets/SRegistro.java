package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.UsuarioDAO;

/**
 * Servlet implementation class SRegistro
 */
@WebServlet("/SRegistro")
public class SRegistro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SRegistro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String nuevousuario = map.get("usuario").toLowerCase();
		String nuevopassword = map.get("password");
		String nuevomail = map.get("mail").toLowerCase();
		HttpSession sesionweb = request.getSession();
		if(nuevousuario!=null && nuevopassword!=null && nuevomail != null){
			if(nuevousuario.compareTo("")!=0 && nuevopassword.compareTo("")!=0 && nuevomail.compareTo("")!=0){
				boolean registro = UsuarioDAO.registroUsuario(nuevousuario, nuevomail, nuevopassword,sesionweb.getAttribute("usuario").toString(), 3);
				if(registro){
					response.getWriter().write("{ \"success\": true, \"message\":\"Usuario registrado exitosamente\" }");
				}else{
					response.getWriter().write("{ \"success\": false, \"error\":\"Error al registrar nuevo usuario\" }");
				}
			}else{
				response.getWriter().write("{ \"success\": false, \"error\":\"Parametros vacios\" }");
			}
		}else{
			response.getWriter().write("{ \"success\": false, \"error\":\"No se enviaron los parametros deseados\" }");
		}
		response.getWriter().flush();
		response.getWriter().close();
		
	}
	


}
