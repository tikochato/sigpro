package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ObjetoResponsableRol;
import pojo.ResponsableRol;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ObjetoResponsableDAO {
	public static List<ResponsableRol> getResponsableRolPorObjetoId(Integer objetoId, Integer objetoTipo) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		List<ResponsableRol> ret = new ArrayList<ResponsableRol>();
		
		try{
			Query<ResponsableRol> criteria = session.createQuery("select rr FROM ResponsableRol rr " 
					+ "inner join rr.objetoResponsableRols orr where " 
					+ "orr.objetoId=:objetoId AND orr.objetoTipo=:objetoTipo", ResponsableRol.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getResultList();
		}
		catch (Throwable e) {
			e.printStackTrace();
			CLogger.write("1", ObjetoResponsableDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarResponsableRol(ObjetoResponsableRol objetoResponsableRol){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(objetoResponsableRol);
			session.getTransaction().commit();
			ret = true;
		}catch (Throwable e) {
			CLogger.write("2", ObjetoResponsableDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static ObjetoResponsableRol getResponsableRolPorId(int objetoId, int objetoTipo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ObjetoResponsableRol ret = null;
		try{
			Query<ObjetoResponsableRol> criteria = session.createQuery("select p FROM ObjetoResponsableRol p "
					+ " where p.objetoId = :objetoId "
					+ " and p.objetoTipo = :objetoTipo "
					+ " and p.estado = 1 ", ObjetoResponsableRol.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", ObjetoResponsableRol.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
