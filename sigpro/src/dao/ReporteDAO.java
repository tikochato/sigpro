package dao;

import java.util.List;


import org.hibernate.query.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import pojo.InformePresupuesto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ReporteDAO {
	public static List<Object> getCargaTrabajo(int atrasados, int objetoTipo, int idPrestamo, int idComponente, int idProducto, int idSubProducto){
		List<Object> result = null;
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
			CLogger.write("1", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	public static List<Object> getAdquisiciones(int anio, int idPrestamo){
		List<Object> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
		}
		catch(Throwable e){
			CLogger.write("2", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	public static List<InformePresupuesto> existeInformeBase(int idPrestamo, int tipoInforme, String anio){
		List<InformePresupuesto> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<InformePresupuesto> criteria = session.createQuery("FROM InformePresupuesto ip where ip.idPrestamo=:idPrestamo and ip.estadoInforme.id=:tipoInforme and date_format(ip.anio,'%d/%m/%Y')=:anio", InformePresupuesto.class);
			criteria.setParameter("idPrestamo", idPrestamo);
			criteria.setParameter("tipoInforme", tipoInforme);
			criteria.setParameter("anio", anio);
			result = criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("3", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		return result;
	}
	
	public static boolean agregarRowInformePresupuesto(InformePresupuesto informe){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(informe);
			session.getTransaction().commit();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("4", ReporteDAO.class, e);
			return false;
		}
		finally{
			session.close();
		}
		return true;
	}
	
	public static List<InformePresupuesto> getrowInformebyId(int idrow){
		List<InformePresupuesto> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<InformePresupuesto> criteria = session.createQuery("FROM InformePresupuesto ip where ip.id=:idrow", InformePresupuesto.class);
			criteria.setParameter("idrow", idrow);
			result = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		return result;
	}
}