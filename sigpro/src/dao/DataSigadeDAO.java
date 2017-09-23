package dao;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojoSigade.DtmAvanceFisfinanDti;
import utilities.CHibernateSession;
import utilities.CLogger;

public class DataSigadeDAO {
	
	
	public static List<DtmAvanceFisfinanDti> getInf(){
		List<DtmAvanceFisfinanDti> ret = new ArrayList<DtmAvanceFisfinanDti>();
		Session session = CHibernateSession.getSessionFactory().openSession();
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
		Session session = CHibernateSession.getSessionFactory().openSession();
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
	
	public static DtmAvanceFisfinanDti getavanceFisFinanDMS1(String codigoPresupuestario){
		DtmAvanceFisfinanDti ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select * from dtm_avance_fisfinan_dti fis",		
					"where fis.CODIGO_PRESUPUESTARIO = ?1");
			Query<DtmAvanceFisfinanDti> criteria = session.createNativeQuery(query,DtmAvanceFisfinanDti.class);
			
			criteria.setParameter("1", codigoPresupuestario);
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
	
	public static List<?> getAVANCE_FISFINAN_DET_DTI(String codigoPresupeustario){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select ejercicio_fiscal,mes_desembolso,sum(desembolsos_mes_gtq) ",
					"from dtm_avance_fisfinan_det_dti",
					"where codigo_presupuestario = ?2",
					"group by ejercicio_fiscal,mes_desembolso",
					"order by ejercicio_fiscal,mes_desembolso asc");
			Query<?> criteria = session.createNativeQuery(query);
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
	
	public static List<DtmAvanceFisfinanDti> getCodigos(){
		List<DtmAvanceFisfinanDti> ret = new ArrayList<>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select d from DtmAvanceFisfinanDti d");
			Query<DtmAvanceFisfinanDti> criteria = session.createQuery(query,DtmAvanceFisfinanDti.class);

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
	
	public static  BigDecimal totalDesembolsadoAFechaReal (String codigo_presupuestario, int anio, int mes){
		BigDecimal ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", 
									"select  sum(distinct desembolsos_mes_gtq)",
									"from dtm_avance_fisfinan_det_dti",
									"where codigo_presupuestario = ?1",
									"and (ejercicio_fiscal < ?2 ",
									"or (ejercicio_fiscal = ?2 and (cast(mes_desembolso as integer)) < ?3))");
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter(1, codigo_presupuestario);
			criteria.setParameter(2, anio);
			criteria.setParameter(3, mes);
			Object object =  criteria.getSingleResult();
			ret =(BigDecimal) object;
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
