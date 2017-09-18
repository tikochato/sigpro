package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProgramaPropiedadValor;
import pojo.ProgramaPropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProgramaPropiedadValorDAO {
	public static ProgramaPropiedadValor getValorPorProgramaYPropiedad(int idPropiedad,int idPrograma){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProgramaPropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProgramaPropiedadValor> criteria = builder.createQuery(ProgramaPropiedadValor.class);
			Root<ProgramaPropiedadValor> root = criteria.from(ProgramaPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ProgramaPropiedadValorId(idPropiedad,idPrograma)),builder.equal(root.get("estado"), 1));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (NoResultException e){
		} catch (Throwable e) {
			CLogger.write("1", ProgramaPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}	
	
	public static boolean guardarProgramaPropiedadValor(ProgramaPropiedadValor programaPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(programaPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", ProgramaPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProgramaPropiedadValor(ProgramaPropiedadValor programaPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			programaPropiedadValor.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(programaPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProgramaPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProgramaPropiedadValor(ProgramaPropiedadValor programaPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(programaPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProgramaPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProgramaPropiedadValor> getProgramaPropiedadadesValoresPorPrograma(int idPrograma){
		List<ProgramaPropiedadValor> ret = new ArrayList<ProgramaPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProgramaPropiedadValor> criteria = session.createNativeQuery(" select * "
					+ "from programa_propiedad_valor ppv "
					+ "join programa p on p.id = ppv.programaid "
					+ "where p.id = :idProg "
					+ "and ppv.estado = 1 ",ProgramaPropiedadValor.class);
			
			criteria.setParameter("idProg", idPrograma);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", ProgramaPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
