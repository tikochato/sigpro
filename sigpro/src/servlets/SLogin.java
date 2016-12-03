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

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.UsuarioDAO;
import pojo.Usuario;
import shiro.utilities.CShiro;

/**
 * Servlet implementation class SSLogin
 */
@WebServlet("/SLogin")
public class SLogin extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8130109518967011067L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SLogin() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		Subject currentUser = CShiro.getSubject();
		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(map.get("username").toLowerCase(), map.get("password").toString());
			token.setRememberMe(false);

			try {
				currentUser.login(token);
				Usuario user=UsuarioDAO.getUsusuario(map.get("username").toLowerCase());
				CShiro.setAttribute("username", user.getUsuario());
				CShiro.setAttribute("user",user);
				response.getWriter().write("{ \"success\": true }");
				UsuarioDAO.userLoginHistory(user.getUsuario());
			} catch (UnknownAccountException uae) {
				response.getWriter().write("{ \"success\": false, \"error\":\"Unknow Account\" }");
			} catch (IncorrectCredentialsException ice) {
				response.getWriter().write("{ \"success\": false, \"error\": \"Incorrect Credential\" }");
			} catch (LockedAccountException lae) {
				response.getWriter().write("{ \"success\": false,\"error\": \"Locked Account Exception\" }");
			} catch (Exception e) {
				response.getWriter().write("{ \"success\": false,\"error\": \"Unknow Exception\" }");
			}
		} else {
			response.getWriter().write("{ \"success\": true }");
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

}
