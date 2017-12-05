package dao;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojoSigade.DtmAvanceFisfinanDetDti;
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
			String query =String.join(" ", "select d from DtmAvanceFisfinanDti d",		
					"where d.id.codigoPresupuestario = :codigo_presupuestario");
			Query<DtmAvanceFisfinanDti> criteria = session.createQuery(query,DtmAvanceFisfinanDti.class);
			
			criteria.setParameter("codigo_presupuestario", codigoPresupuestario);
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
			String query =String.join(" ", "select d.id.ejercicioFiscal,d.id.mesDesembolso,sum(d.id.desembolsosMesGtq) ",
					"from DtmAvanceFisfinanDetDti d",
					"where d.id.codigoPresupuestario = ?2",
					"group by d.id.ejercicioFiscal, d.id.mesDesembolso",
					"order by d.id.ejercicioFiscal, d.id.mesDesembolso asc");
			Query<?> criteria = session.createQuery(query);
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
	
	public static List<?> getAVANCE_FISFINAN_DET_DTIRango(String codigoPresupeustario,int anio_inicio, int anio_fin, int tipoMoneda, int entidad, int unidadEjecutora){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", "select d.id.ejercicioFiscal,d.id.mesDesembolso,sum(",
					tipoMoneda == 1 ? "d.id.desembolsosMesGtq" : "d.id.desembolsosMesUsd",
					") ",
					"from DtmAvanceFisfinanDetDti d",
					"where d.id.codigoPresupuestario = ?1",
					"and d.id.ejercicioFiscal between ?2 and ?3",
					"and d.id.entidadSicoin= ?4",
					"and d.id.unidadEjecutoraSicoin= ?5",
					"group by d.id.ejercicioFiscal,d.id.mesDesembolso",
					"order by d.id.ejercicioFiscal,d.id.mesDesembolso asc");
			Query<?> criteria = session.createQuery(query);
			criteria.setParameter(1, codigoPresupeustario);
			criteria.setParameter(2, new Long(anio_inicio));
			criteria.setParameter(3, new Long(anio_fin));
			criteria.setParameter(4, entidad);
			criteria.setParameter(5, unidadEjecutora);
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
	
	public static  BigDecimal totalDesembolsadoAFechaReal (String codigo_presupuestario, Long anio, int mes){
		BigDecimal ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", 
									"select  sum (d.id.desembolsosMesGtq)",
									"from DtmAvanceFisfinanDetDti d",
									"where d.id.codigoPresupuestario = ?1",
									"and (d.id.ejercicioFiscal < ?2 ",
									"or (d.id.ejercicioFiscal = ?2 and (cast(d.id.mesDesembolso as integer)) < ?3))");
			Query<?> criteria = session.createQuery(query);
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
	
	public static List<?> getComponentes(String codigo_presupuestario){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query ="select * from sipro_analytic.dtm_avance_fisfinan_cmp " +
						"where codigo_presupuestario = ?1 " +
						"order by numero_componente asc";
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", codigo_presupuestario);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<?> getUnidadesEjecutoras(String codigo_presupuestario,int ejercicio){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query ="select * from sipro_analytic.dtm_avance_fisfinan_enp " +
						"where codigo_presupuestario = ?1 "
						+ "and ejercicio_fiscal = ?2 ";
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", codigo_presupuestario);
			criteria.setParameter("2", ejercicio);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<DtmAvanceFisfinanDetDti> getInfPorUnidadEjecutora(String codigoPresupuestario, Integer ejercicio, Integer entidad, Integer UE){
		List<DtmAvanceFisfinanDetDti> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query =String.join("", "SELECT * FROM sipro_analytic.dtm_avance_fisfinan_det_dti d ",
					"where d.codigo_presupuestario = ?1 ",
					"and d.ejercicio_fiscal = ?2 ",
					"and d.entidad_sicoin = ?3 ",
					"and d.unidad_ejecutora_sicoin= ?4 ");
			Query<DtmAvanceFisfinanDetDti> criteria = session.createNativeQuery(query, DtmAvanceFisfinanDetDti.class);
			criteria.setParameter("1", codigoPresupuestario);
			criteria.setParameter("2", ejercicio);
			criteria.setParameter("3", entidad);
			criteria.setParameter("4", UE);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("7", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<DtmAvanceFisfinanDetDti> getInfPorUnidadEjecutoraALaFecha(String codigoPresupuestario, Integer entidad, Integer UE){
		List<DtmAvanceFisfinanDetDti> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query =String.join("", "SELECT * FROM sipro_analytic.dtm_avance_fisfinan_det_dti d ",
					"where d.codigo_presupuestario = ?1 ",
					"and d.entidad_sicoin = ?2 ",
					"and d.unidad_ejecutora_sicoin= ?3 ",
					"order by d.ejercicio_fiscal, d.mes_desembolso asc");
			Query<DtmAvanceFisfinanDetDti> criteria = session.createNativeQuery(query, DtmAvanceFisfinanDetDti.class);
			criteria.setParameter("1", codigoPresupuestario);
			criteria.setParameter("2", entidad);
			criteria.setParameter("3", UE);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Integer getDiferenciaMontos(String codigo_presupuestario){
		Integer ret = null; 
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query ="select sum(cmp.monto_componente-cs.monto_componente) diferencia " +
						"from sipro_analytic.dtm_avance_fisfinan_cmp cmp,sipro.componente_sigade cs " +
						"where cmp.codigo_presupuestario = cs.codigo_presupuestario " +
						"and cmp.numero_componente = cs.numero_componente " + 
						"and cmp.codigo_presupuestario = ?1 " +
						"and cs.estado = 1";
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", codigo_presupuestario);
			Object total =  criteria.getSingleResult();
			
			ret = ((BigDecimal ) total).intValue();
		}
		catch(Throwable e){
			CLogger.write("9", DataSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static  BigDecimal totalDesembolsadoAFechaRealDolaresPorEntidad (String codigo_presupuestario,
				Long anio, int mes,Integer entidadSicoin, Integer unidadEjecutoraSicoin){
		BigDecimal ret = new BigDecimal(0);
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ", 
									"select  sum (d.id.desembolsosMesUsd)",
									"from DtmAvanceFisfinanDetDti d",
									"where d.id.codigoPresupuestario = ?1",
									"and (d.id.ejercicioFiscal < ?2 ",
									"or (d.id.ejercicioFiscal = ?2 and (cast(d.id.mesDesembolso as integer)) < ?3))",
									"and d.id.entidadSicoin = ?4 ",
									"and d.id.unidadEjecutoraSicoin = ?5");
			Query<?> criteria = session.createQuery(query);
			criteria.setParameter(1, codigo_presupuestario);
			criteria.setParameter(2, anio);
			criteria.setParameter(3, mes);
			criteria.setParameter(4, entidadSicoin);
			criteria.setParameter(5, unidadEjecutoraSicoin);
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
