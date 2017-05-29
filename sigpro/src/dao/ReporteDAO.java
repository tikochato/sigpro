package dao;

import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ReporteDAO {
	public static List getCargaTrabajo(int atrasados, int objetoTipo, int idPrestamo, int idComponente, int idProducto, int idSubProducto){
		List result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			Query query = session.createSQLQuery("CALL carga_trabajo(:atrasados, :objetoTipo, :idPrestamo, :idComponente, :idProducto, :idSubproducto, :fecha)")
			.setParameter("atrasados", atrasados)
			.setParameter("objetoTipo", objetoTipo)
			.setParameter("idPrestamo", idPrestamo)
			.setParameter("idComponente", idComponente)
			.setParameter("idProducto", idProducto)
			.setParameter("idSubproducto", idSubProducto)
			.setParameter("fecha", new DateTime().toDate());
			result = query.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
}