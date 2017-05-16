package dao;

import java.util.List;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojoSigade.DtmAvanceFisfinanDti;
import utilities.CHibernateSessionSIGADE;
import utilities.CLogger;

public class DataSigadeDAO {
	
	
	public static List<DtmAvanceFisfinanDti> getInf(){
		List<DtmAvanceFisfinanDti> ret = new ArrayList<DtmAvanceFisfinanDti>();
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		try{
			Query<DtmAvanceFisfinanDti> criteria = session.createQuery("FROM dtm_avance_fisfinan_dti p ", DtmAvanceFisfinanDti.class);
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
	
	public static DtmAvanceFisfinanDti getInfPorId(String noPrestamo,String codigoPresupuestario){
		DtmAvanceFisfinanDti ret = null;
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		try{
			String query =String.join("", "SELECT i FROM dtm_avance_fisfinan_dti i ",
					"where i.id.codigoPresupuestario = :codPre ",
					"and i.id.noPrestamo = noPre ");
			Query<DtmAvanceFisfinanDti> criteria = session.createQuery(query, DtmAvanceFisfinanDti.class);
			criteria.setParameter("codPre", codigoPresupuestario);
			criteria.setParameter("noPre", noPrestamo);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static DtmAvanceFisfinanDti getavanceFisFinanDMS1(String noPrestamo,String codigoPresupuestario){
		DtmAvanceFisfinanDti ret = null;
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select * from dtm_avance_fisfinan_dti fis",
					"where fis.NO_PRESTAMO = ?1",
					"and fis.CODIGO_PRESUPUESTARIO = ?2");
			Query<DtmAvanceFisfinanDti> criteria = session.createNativeQuery(query,DtmAvanceFisfinanDti.class);
			criteria.setParameter("1", noPrestamo);
			criteria.setParameter("2", codigoPresupuestario);
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
