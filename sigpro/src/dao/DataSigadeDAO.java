package dao;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojoSigade.DtmAvanceFisfinanDetDti;
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
	
	public static List<Object> getAVANCE_FISFINAN_DET_DTI(String codigoPresupeustario){
		List<Object> ret = null;
		Session session = CHibernateSessionSIGADE.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select ejercicio_fiscal,mes_desembolso,sum(desembolsos_mes_gtq) ",
					"from DTM_AVANCE_FISFINAN_DET_DTI",
					"where codigo_presupuestario = ?2",
					"group by ejercicio_fiscal,mes_desembolso",
					"order by ejercicio_fiscal,mes_desembolso asc");
			Query criteria = session.createNativeQuery(query);
			criteria.setParameter("2", codigoPresupeustario);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	

}
