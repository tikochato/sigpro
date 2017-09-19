package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.RiesgoPropiedadValor;
import pojo.RiesgoPropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RiesgoPropiedadValorDAO {
	public static RiesgoPropiedadValor getValorPorRiesgoYPropiedad(int idPropiedad,int idRiesgo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RiesgoPropiedadValor ret = null;
		try {
			List<RiesgoPropiedadValor> listRet = null;
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RiesgoPropiedadValor> criteria = builder.createQuery(RiesgoPropiedadValor.class);
			Root<RiesgoPropiedadValor> root = criteria.from(RiesgoPropiedadValor.class);
			criteria.select(root);
			
			criteria.where(builder.equal(root.get("id"), new RiesgoPropiedadValorId(idRiesgo, idPropiedad)),builder.equal( root.get("estado"), 1));
			listRet =session.createQuery(criteria).getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("1", RiesgoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRiesgoPropiedadValor(RiesgoPropiedadValor riesgoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(riesgoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", RiesgoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRiesgoPropiedadValor(RiesgoPropiedadValor riesgoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			riesgoPropiedadValor.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(riesgoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RiesgoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRiesgoPropiedadValor(RiesgoPropiedadValor riesgoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(riesgoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RiesgoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RiesgoPropiedadValor> getRiesgoPropiedadadesValoresPorRiesgo(int idRiesgo){
		List<RiesgoPropiedadValor> ret = new ArrayList<RiesgoPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RiesgoPropiedadValor> criteria = session.createNativeQuery(" select * "
					+ "from riesgo_propiedad_valor rpv "
					+ "join riesgo r on r.id = rpv.riesgoid "
					+ "where r.id = :idRies "
					+ "and rpv.estado = 1 ",RiesgoPropiedadValor.class);
			
			criteria.setParameter("idRies", idRiesgo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
