package dao;

import java.util.List;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojoSigade.Cierre;
import pojoSigade.Inf;
import utilities.CHibernateSessionSIGADE;
import utilities.CLogger;

public class DataSigadeDAO {
	public static List<Cierre> getCierre(){
		List<Cierre> ret = new ArrayList<Cierre>();
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		try{
			Query<Cierre> criteria = session.createQuery("FROM Cierre p ", Cierre.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Inf> getInf(){
		List<Inf> ret = new ArrayList<Inf>();
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		try{
			Query<Inf> criteria = session.createQuery("FROM Inf p ", Inf.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Inf getInfPorId(String noPrestamo,String codigoPresupuestario){
		Inf ret = null;
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		try{
			String query =String.join("", "SELECT i FROM Inf i ",
					"where i.id.codigoPresupuestario = :codPre ",
					"and i.id.noPrestamo = noPre ");
			Query<Inf> criteria = session.createQuery(query, Inf.class);
			criteria.setParameter("codPre", codigoPresupuestario);
			criteria.setParameter("noPre", noPrestamo);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Inf getavanceFisFinan(String noPrestamo,String codigoPresupuestario){
		Inf ret = null;
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		try{
			String query =String.join(" ", "select * from dtm_avance_fisfinan_dti@bd_datamart fis",
							"where fis.NO_PRESTAMO = ?1");
			Query<Inf> criteria = session.createNativeQuery(query,Inf.class);
			//criteria.setParameter("codPre", codigoPresupuestario);
			criteria.setParameter("1", noPrestamo);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	

}
