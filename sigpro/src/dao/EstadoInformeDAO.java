package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.EstadoInforme;
import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

public class EstadoInformeDAO {
	
	public static List<EstadoInforme> getEstadoInforme(int prestamoId){
		List<EstadoInforme> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<EstadoInforme> criteria = session.createQuery("FROM EstadoInforme ei where ei.proyecto.id=:prestamoId and ei.estado = 1", EstadoInforme.class);
			criteria.setParameter("prestamoId", prestamoId);
			result = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", EstadoInformeDAO.class, e);
		}
		finally{
			session.close();
		}
		return result;
	}
	
	public static boolean crearInforme(EstadoInforme informe){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(informe);
			session.getTransaction().commit();
		}
		catch(Throwable e){
			CLogger.write("2", EstadoInformeDAO.class, e);
			return false;
		}
		finally{
			session.close();
		}
		return true;
	}
	
	public static int getCantidadInformesPrestamo(int idPrestamo){
		List<Object> result = null;
		int cantidad = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Object> criteria = session.createQuery("select count(ei) FROM EstadoInforme ei where ei.proyecto.id=:idPrestamo and ei.estado=1",Object.class);
			criteria.setParameter("idPrestamo", idPrestamo);
			result = criteria.getResultList();
			
			for(Object obj: result){
				cantidad = Utils.String2Int(obj.toString());
			}
			//cantidad = result[0];
		}
		catch(Throwable e){
			CLogger.write("3", EstadoInformeDAO.class, e);
		}
		finally{
			session.close();
		}
		return cantidad;
	}
}
