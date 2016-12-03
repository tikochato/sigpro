package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import shiro.utilities.CShiro;
 
/**
 * Servlet implementation class SSignout
 */
@WebServlet(name = "SLogout", urlPatterns = {"/SLogout"})
public class SLogout extends HttpServlet {
       
    private static final long serialVersionUID = -7865046670351807851L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SLogout() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	} 

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Subject currentUser = CShiro.getSubject();
		if(currentUser!=null){
			currentUser.logout();
			//response.getWriter().write("{ \"success\": true }");
		}
		response.sendRedirect("/");
		response.getWriter().flush();
		response.getWriter().close();
	}

}
