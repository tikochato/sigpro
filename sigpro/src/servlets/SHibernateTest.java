package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import dao.CooperanteDAO;
import pojo.Cooperante;

/**
 * Servlet implementation class SHibernateTest
 */
@WebServlet("/SHibernateTest")
public class SHibernateTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SHibernateTest() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateTime date = new DateTime();
		int id = 0;
		Cooperante cooperante = new Cooperante("A1", "Prueba", "admin", date.toDate(), 1);
		if(CooperanteDAO.guardarCooperante(cooperante)){
			response.getWriter().write("Cooperante creado con éxito, nuevo id: "+cooperante.getId()+", código: A1\r\n");
			id = cooperante.getId();
			cooperante.setCodigo("B1");
			if(CooperanteDAO.guardarCooperante(cooperante)){
				Cooperante guardado = CooperanteDAO.getCooperantePorId(cooperante.getId());
				response.getWriter().write("Cooperante guardado con éxito, id: "+guardado.getId()+", código: "+guardado.getCodigo()+ "\r\n");
				if(CooperanteDAO.eliminarCooperante(guardado)){
					Cooperante eliminado = CooperanteDAO.getCooperantePorId(guardado.getId());
					response.getWriter().write("Cooperante eliminado con éxito, id: "+eliminado.getId()+", nuevo estado: "+eliminado.getEstado()+"\r\n");
					CooperanteDAO.eliminarTotalCooperante(eliminado);
					Cooperante test = CooperanteDAO.getCooperantePorId(id);
					if(test==null)
						response.getWriter().write("Cooperante eliminado definitivamente con éxito");
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
