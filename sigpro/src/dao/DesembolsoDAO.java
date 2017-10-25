package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Desembolso;
import utilities.CHibernateSession;
import utilities.CLogger;

public class DesembolsoDAO {
	
	public static List<Desembolso> getDesembolsos(){
		List<Desembolso> ret = new ArrayList<Desembolso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Desembolso> criteria = builder.createQuery(Desembolso.class);
			Root<Desembolso> root = criteria.from(Desembolso.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Desembolso getDesembolsoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Desembolso ret = null;
		List<Desembolso> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Desembolso> criteria = builder.createQuery(Desembolso.class);
			Root<Desembolso> root = criteria.from(Desembolso.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarDesembolso(Desembolso desembolso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(desembolso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarDesembolso(Desembolso desembolso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			desembolso.setEstado(0);
			session.beginTransaction();
			session.update(desembolso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalDesembolso(Desembolso desembolso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(desembolso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Desembolso> getDesembolsosPagina(int pagina, int numerodesembolsos){
		List<Desembolso> ret = new ArrayList<Desembolso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Desembolso> criteria = session.createQuery("SELECT d FROM Desembolso d WHERE estado = 1",Desembolso.class);
			criteria.setFirstResult(((pagina-1)*(numerodesembolsos)));
			criteria.setMaxResults(numerodesembolsos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalDesembolsos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(d.id) FROM Desembolso d WHERE d.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Desembolso> getDesembolsosPorProyecto(int proyectoId){
		List<Desembolso> ret = new ArrayList<Desembolso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT d FROM Desembolso d WHERE estado = 1 AND d.proyecto.id = :proyId ORDER BY fecha";
			Query<Desembolso> criteria = session.createQuery(query,Desembolso.class);
			criteria.setParameter("proyId", proyectoId);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalDesembolsosPorProyecto(int proyectoId,String filtro_fecha, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(d.id) FROM Desembolso d WHERE d.estado=1 AND d.proyecto.id = :proyId ";
			String query_a="";
			if(filtro_fecha!=null && filtro_fecha.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(d.fecha,'%d/%m/%YYYY')) LIKE '%", filtro_fecha,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " d.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(d.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("proyId", proyectoId);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<?> getDesembolsosPorEjercicio(Integer idProyecto, int anio_inicial, int anio_final){
		List<?> ret= null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select year (fecha) anio ,month(fecha) mes ,SUM(monto)  monto",
				"from desembolso where proyectoid = ?1",
				"and estado  = 1", 
				"and  year(fecha) between ?2 and ?3",
				"GROUP BY year (fecha),month(fecha) order by year(fecha),month (fecha) asc");
			Query<?>  desembolsos = session.createNativeQuery(query);
			desembolsos.setParameter(1, idProyecto);
			desembolsos.setParameter(2, anio_inicial);
			desembolsos.setParameter(3, anio_final);
			ret = desembolsos.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<?> getDesembolsosEntreFechas(Integer idProyecto, Date fechaInicio, Date fechaFin){
		java.sql.Date fechaInicial = new java.sql.Date(fechaInicio.getTime());
		java.sql.Date fechaFinal = new java.sql.Date(fechaFin.getTime());
		List<?> ret= null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select year (fecha) anio ,month(fecha) mes ,SUM(monto)  monto",
				"from desembolso where proyectoid = ?1",
				"and estado  = 1", 
				"and  fecha between ?2 and ?3",
				"GROUP BY year (fecha),month(fecha) order by year(fecha),month (fecha) asc");
			Query<?>  desembolsos = session.createNativeQuery(query);
			desembolsos.setParameter(1, idProyecto);
			desembolsos.setParameter(2, fechaInicial);
			desembolsos.setParameter(3, fechaFinal);
			ret = desembolsos.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ArrayList<DesembolsoReal> getDesembolsosReales(long codigoPresupuestario, Integer mesInicial, Integer anioInicial, Integer mesFinal, Integer anioFinal, Connection conn){
		ArrayList<DesembolsoReal> ret= null;
		
		try{
			if(!conn.isClosed() ){
				String str_Query = String.join(" ", "select ejercicio_fiscal, mes_desembolso, SUM(desembolsos_mes_gtq) desembolso_gtq ",
						"from dtm_avance_fisfinan_det_dti", 
						"where codigo_presupuestario = ? ",
						"and ejercicio_fiscal between ? and ?",
						"and mes_desembolso between ? and ?",
						"group by ejercicio_fiscal, mes_desembolso",
						"order by ejercicio_fiscal, mes_desembolso asc");
				PreparedStatement pstm  = conn.prepareStatement(str_Query);
				pstm.setFetchSize(1000);
                pstm.setLong(1, codigoPresupuestario);
                pstm.setInt(2, anioInicial);
                pstm.setInt(3, anioFinal);
                pstm.setInt(4, mesInicial);
                pstm.setInt(5, mesFinal);
                ResultSet rs = pstm.executeQuery();
                        
                ret = new ArrayList<DesembolsoReal>();
                while(rs!=null && rs.next()){
                	Integer ejercicioFiscal = rs.getInt("ejercicio_fiscal");
                	Integer mes = rs.getInt("mes_desembolso");
                	BigDecimal desembolsoGTQ = rs.getBigDecimal("desembolso_gtq");
                	
                    DesembolsoReal desembolso = new DesembolsoReal(ejercicioFiscal, mes, codigoPresupuestario, null, null, null, null, null, null, null, desembolsoGTQ);
                    ret.add(desembolso);
                }
                
                rs.close();
                pstm.close();
			}
		}
		catch(Throwable e){
			CLogger.write("10", DesembolsoDAO.class, e);
		}
		return ret;
	}
	
	public static List<?> getCostosPorEjercicio(Integer idProyecto, int anio_inicial, int anio_final){
		List<?> ret= null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select year (a.fecha_inicio), month (a.fecha_inicio), a.costo",
						"from actividad a",
						"where a.estado=1 and ((a.proyecto_base= ?1)",
						"OR (a.componente_base in (select id from componente where proyectoid= ?1))",
						"OR (a.producto_base in (select p.id from producto p, componente c where p.componenteid=c.id and c.proyectoid= ?1))",
						")",
						"and  year (a.fecha_inicio) between ?2 and ?3",
						"GROUP BY year (a.fecha_inicio), month (a.fecha_inicio)",
						"order by year (a.fecha_inicio), month (a.fecha_inicio) asc");
			Query<?>  desembolsos = session.createNativeQuery(query);
			desembolsos.setParameter(1, idProyecto);
			desembolsos.setParameter(2, anio_inicial);
			desembolsos.setParameter(3, anio_final);
			ret = desembolsos.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static BigDecimal getTotalDesembolsosFuturos(int proyectoId, Date fechaActual){
		BigDecimal ret= new BigDecimal("0");
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<BigDecimal> conteo = 
					session.createQuery("select sum(d.monto) from Desembolso d where d.proyecto.id = ?1 and d.fecha > ?2",BigDecimal.class);
			conteo.setParameter(1, proyectoId);
			conteo.setParameter(2, fechaActual);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
