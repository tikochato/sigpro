package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Meta;
import pojo.MetaAvance;
import pojo.MetaPlanificado;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MetaDAO {
	
	public static List<Meta> getMetas(){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Meta> criteria = builder.createQuery(Meta.class);
			Root<Meta> root = criteria.from(Meta.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Meta getMetaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Meta ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Meta> criteria = builder.createQuery(Meta.class);
			Root<Meta> root = criteria.from(Meta.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			List<Meta> lista = session.createQuery( criteria ).getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("2", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(Meta);
			session.getTransaction().commit();
			
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		
	public static boolean eliminarMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Meta.setEstado(0);
			session.beginTransaction();
			session.update(Meta);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Meta);
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Meta> getMetasPagina(int pagina, int numeroMetas, int id, int tipo,
			String filtro_nombre, int filtro_metatipo, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT m FROM Meta m WHERE m.estado = 1 and m.objetoId=:objetoId AND m.objetoTipo=:objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " m.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_metatipo>0)
				query_a = String.join("",query_a, " m.metaTipo = ",String.valueOf(filtro_metatipo)," ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " m.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(m.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join(""," AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Meta> criteria = session.createQuery(query,Meta.class);
			criteria.setParameter("objetoId", id);
			criteria.setParameter("objetoTipo", tipo);
			if (numeroMetas > 0){
				criteria.setFirstResult(((pagina-1)*(numeroMetas)));
				criteria.setMaxResults(numeroMetas);
			}
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Meta> getMetasObjeto(int id, int tipo){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT m FROM Meta m WHERE m.estado = 1 and m.objetoId=:objetoId AND m.objetoTipo=:objetoTipo and m.objetoId > 0 ";
			
			Query<Meta> criteria = session.createQuery(query,Meta.class);
			criteria.setParameter("objetoId", id);
			criteria.setParameter("objetoTipo", tipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("7", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalMetas(Integer id, Integer tipo,String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(m.id) FROM Meta m WHERE m.estado=1 AND m.objetoId = :objetoId AND m.objetoTipo = :objetotipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " m.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " m.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(m.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("objetoId", id);
			conteo.setParameter("objetotipo", tipo);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("8", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean borrarPlanificadoAvanceMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Set<MetaPlanificado> metaPlanificados = Meta.getMetaPlanificados();
			if(metaPlanificados!=null){
				for(MetaPlanificado planificado : metaPlanificados){
					session.beginTransaction();
					session.delete(planificado);
					session.getTransaction().commit();
				}
			}
			Set<MetaAvance> metaAvances = Meta.getMetaAvances();
			if(metaAvances!=null){
				for(MetaAvance avance : metaAvances){
					session.beginTransaction();
					session.delete(avance);
					session.getTransaction().commit();
				}
			}
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("9", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean agregarMetaAvance(MetaAvance metaAvance){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(metaAvance);
			session.getTransaction().commit();
		}
		catch(Throwable e){
			CLogger.write("10", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean agregarMetaPlanificado(MetaPlanificado metaPlanificado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(metaPlanificado);
			session.getTransaction().commit();
		}
		catch(Throwable e){
			CLogger.write("11", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Meta> getMetasPorObjeto(Integer objetoId, Integer objetoTipo,String lineaBase){
		List<Meta> ret=null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select  *",
				"from sipro_history.meta m",
				"where m.objeto_id = ?1",
				"and m.objeto_tipo = ?2",
				"and m.dato_tipoid in (2,3)",
				lineaBase != null ? "and m.linea_base = ?3" : "and m.actual = 1");
			
			Query<Meta> metavalor = session.createNativeQuery(query,Meta.class);
			metavalor.setParameter("1", objetoId);
			metavalor.setParameter("2", objetoTipo);
			if (lineaBase != null)
				metavalor.setParameter("3", lineaBase);
			
			ret =  metavalor.getResultList();
		}
		catch(Throwable e){
			CLogger.write("12", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<MetaPlanificado> getMetasPlanificadas(Integer metaId,String lineaBase){
		List<MetaPlanificado> ret=null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select *",
					"from sipro_history.meta_planificado mp",
					"where mp.metaid = ?1",
					"and mp.estado = 1",
					lineaBase != null ? "and mp.linea_base = ?2": "and mp.actual = 1");
			
			Query<MetaPlanificado> metavalor = session.createNativeQuery(query,MetaPlanificado.class);
			metavalor.setParameter("1", metaId);
			if (lineaBase != null)
				metavalor.setParameter("2", lineaBase);
			ret =  metavalor.getResultList();
		}
		catch(Throwable e){
			CLogger.write("13", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Object[]> getMetaAvancesPorMes(Integer metaId){
		List<Object[]> ret=null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select year(ma.id.fecha), month(ma.id.fecha), sum(ma.valorEntero), sum(ma.valorDecimal)",
					"from MetaAvance ma",
					"where ma.id.metaid = ?1 GROUP BY year(ma.id.fecha), month(ma.id.fecha) ORDER BY year(ma.id.fecha), month(ma.id.fecha)");
			Query<Object[]> metavalor = session.createQuery(query,Object[].class);
			metavalor.setParameter("1", metaId);
			ret =  metavalor.getResultList();
		}
		catch(Throwable e){
			CLogger.write("14", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Meta> getMetasObjetoLineaBase(int id, int tipo, String lineaBase){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT id, nombre, descripcion, usuario_creo, usuario_actualizo, fecha_creacion, fecha_actualizacion, "
					+ "estado, meta_unidad_medidaid, dato_tipoid, objeto_id, objeto_tipo, meta_final_entero, meta_final_string, "
					+ "meta_final_decimal, meta_final_fecha "
					+ "FROM sipro_history.meta m "
					+ "WHERE m.estado = 1 and m.objeto_id=?1 "
					+ "AND m.objeto_tipo=?2 "
					+" and m.objeto_id > 0 ";
			
			query += lineaBase!=null ? " and m.linea_base like '%"+lineaBase+"%' " : " and m.actual = 1 ";
			
			Query<Meta> metavalor = session.createNativeQuery(query,Meta.class);
			metavalor.setParameter("1", id);
			metavalor.setParameter("2", tipo);
			
			ret =  metavalor.getResultList();
		}
		catch(Throwable e){
			CLogger.write("15", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
