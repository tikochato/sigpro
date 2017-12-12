package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ObjetoRiesgo;
import pojo.ObjetoRiesgoId;
import pojo.Riesgo;
import utilities.CHibernateSession;
import utilities.CHistoria;
import utilities.CLogger;


public class RiesgoDAO {
	
	public static List<Riesgo> getRiesgos(){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Riesgo> criteria = builder.createQuery(Riesgo.class);
			Root<Riesgo> root = criteria.from(Riesgo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Riesgo getRiesgoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Riesgo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Riesgo> criteria = builder.createQuery(Riesgo.class);
			Root<Riesgo> root = criteria.from(Riesgo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			List<Riesgo> listRet = null;
			listRet =session.createQuery( criteria ).getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("2", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRiesgo(Riesgo riesgo,Integer objetoId, Integer objetoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(riesgo);
			ObjetoRiesgoId objetoRiesgoId = new ObjetoRiesgoId(riesgo.getId(), objetoId, objetoTipo);
			ObjetoRiesgo objetoRiesgo = new ObjetoRiesgo(objetoRiesgoId, riesgo, riesgo.getUsuarioCreo(), 
					riesgo.getUsuarioActualizo(), riesgo.getFechaCreacion(), riesgo.getFechaActualizacion());
			session.saveOrUpdate(objetoRiesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			riesgo.setEstado(0);
			session.beginTransaction();
			session.update(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgos(String filtro_nombre, String filtro_usuario_creo,String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(r.id) FROM Riesgo r WHERE r.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("6", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Riesgo> getRiesgosPorObjeto (int objetoId, int objetoTipo){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT r FROM Riesgo as r inner join r.objetoRiesgos o "
					+ "WHERE r.estado = 1 "
					+ "and o.id.objetoId = :objid "
					+ "and o.id.objetoTipo = :objetoTipo "
					+ "and o.id.objetoId > 0 ";
			
			Query<Riesgo> criteria = session.createQuery(query,Riesgo.class);
			criteria.setParameter("objid", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("7", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Riesgo> getMatrizRiesgo (int proyectoId){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_id = :idP and obr.objeto_tipo = 0 ", 
					"UNION ",
					"select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_tipo = 1 and obr.objeto_id in ", 
					"(select c.id from proyecto p join componente c on c.proyectoid = p.id where c.estado  = 1 and p.id = :idP ) ",
					"UNION ",
					"select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_tipo = 2 and obr.objeto_id in ", 
					"(select sc.id from proyecto p join componente c on c.proyectoid = p.id join subcomponente sc on c.id = sc.componenteid where sc.estado=1 and c.estado  = 1 and p.id = :idP ) ",
					"UNION ",
					"select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_tipo = 3 and obr.objeto_id in ", 
					"(select pr.id from proyecto p join componente c on c.proyectoid = p.id join producto pr ON pr.componenteid = c.id ", 
					"where c.estado  = 1 and pr.estado =1 and pr.estado = 1 and p.id = :idP UNION ", 
					"select pr.id from proyecto p join componente c on c.proyectoid = p.id join subcomponente sc on c.id = sc.componenteid ",
					"join producto pr ON pr.subcomponenteid = sc.id where sc.estado=1 and c.estado  = 1 and pr.estado =1 and pr.estado = 1 and p.id = :idP) ", 
					"UNION ",
					"select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_tipo = 4 and obr.objeto_id in ",  
					"(select spr.id from proyecto p join componente c on c.proyectoid = p.id join producto pr ON pr.componenteid = c.id ", 
					"join subproducto spr ON spr.productoid = pr.id where c.estado  = 1 and pr.estado = 1 and spr.estado = 1 and p.id = :idP UNION ", 
					"select spr.id from proyecto p join componente c on c.proyectoid = p.id join subcomponente sc on sc.componenteid = c.id join producto pr ON pr.subcomponenteid = sc.id ", 
					"join subproducto spr ON spr.productoid = pr.id where c.estado  = 1 and sc.estado=1 and pr.estado = 1 and spr.estado = 1 and p.id = :idP ) ", 
					"UNION ",
					"select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_tipo = 5 and obr.objeto_id in ",   
					"(select a.id from actividad a where a.estado = 1 and a.objeto_id = :idP and a.objeto_tipo = 0) ", 
					"UNION ",
					"select r.* from riesgo r join objeto_riesgo obr on obr.riesgoid = r.id where r.estado = 1 and obr.objeto_tipo = 5 and obr.objeto_id in ( ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_id = :idP and a.objeto_tipo = 0 UNION ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_tipo = 1 and a.objeto_id in ( ",
					"select c.id from proyecto p join componente c on c.proyectoid = p.id where c.estado  = 1 and p.id = :idP ) UNION ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_tipo = 2 and a.objeto_id in ( ",
					"select sc.id from proyecto p join componente c on c.proyectoid = p.id join subcomponente sc on sc.componenteid = c.id ", 
					"where c.estado  = 1 and sc.estado=1 and p.id = :idP )  UNION ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_tipo = 3 and a.objeto_id in ( ",
					"select pr.id from proyecto p join componente c on c.proyectoid = p.id ", 
					"join producto pr ON pr.componenteid = c.id where c.estado  = 1 and pr.estado =1 and pr.estado = 1 and p.id = :idP ) UNION ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_tipo = 3 and a.objeto_id in ( ",
					"select pr.id from proyecto p join componente c on c.proyectoid = p.id join subcomponente sc on sc.componenteid = c.id ", 
					"join producto pr ON pr.subcomponenteid = sc.id where c.estado  = 1 and sc.estado=1 and pr.estado = 1 and p.id = :idP ) UNION ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_tipo = 4 and a.objeto_id in ( ",
					"select spr.id from proyecto p join componente c on c.proyectoid = p.id join producto pr ON pr.componenteid = c.id join subproducto spr ON spr.productoid = pr.id ", 
					"where c.estado  = 1 and pr.estado = 1 and spr.estado = 1 and pr.estado =1 and pr.estado = 1 and p.id = :idP ) UNION ",
					"select a.id from actividad a where a.estado = 1 and a.objeto_tipo = 4 and a.objeto_id in ( ",
					"select spr.id from proyecto p join componente c on c.proyectoid = p.id join subcomponente sc ON sc.componenteid = c.id join producto pr ON pr.subcomponenteid = sc.id ", 
					"join subproducto spr ON spr.productoid = pr.id ", 
					"where c.estado  = 1 and sc.estado=1 and pr.estado = 1 and spr.estado = 1 and pr.estado =1 and pr.estado = 1 and p.id = :idP )) ");
			Query<Riesgo> criteria = session.createNativeQuery(query,Riesgo.class);
			criteria.setParameter("idP", proyectoId);
			
			
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ObjetoRiesgo getObjetoRiesgo (int idRiesgo){
		ObjetoRiesgo ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			List<ObjetoRiesgo> listRet = null;
			String query = String .join(" ", "select o.* ",
							"from riesgo r",
							"join objeto_riesgo o on o.riesgoid = r.id",
							"where r.estado = 1",
							"and r.id = :objid",
							"order by o.fecha_creacion desc limit 1 ");
			Query<ObjetoRiesgo> criteria = session.createNativeQuery(query,ObjetoRiesgo.class);
			criteria.setParameter("objid", idRiesgo);
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("9", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;	
	}
	
	
	public static List<Riesgo> getRiesgosNotIn(Integer objetoId, Integer objetoTipo,List<Integer> riesgos){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT r FROM Riesgo as r inner join r.objetoRiesgos o "
					+ "WHERE r.estado = 1 "
					+ "and o.id.objetoId = :objid "
					+ "and o.id.objetoTipo = :objetoTipo "
					+ "and r.id NOT IN (:ids)";
			
			Query<Riesgo> criteria = session.createQuery(query,Riesgo.class);
			criteria.setParameter("objid", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameterList("ids", riesgos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("10", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static void eliminarTodosRiesgos(Integer objetoId, Integer objetoTipo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT r FROM Riesgo as r inner join r.objetoRiesgos o "
					+ "WHERE r.estado = 1 "
					+ "and o.id.objetoId = :objid "
					+ "and o.id.objetoTipo = :objetoTipo ";
			
			Query<Riesgo> criteria = session.createQuery(query,Riesgo.class);
			criteria.setParameter("objid", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			List<Riesgo> ret = criteria.getResultList();
			session.close();
			for(Riesgo riesgo:ret)
				eliminarRiesgo(riesgo);
		}
		catch(Throwable e){
			CLogger.write("11", RiesgoDAO.class, e);
		}
		finally{
			if(session.isOpen())
				session.close();
		}
	}
	
	public static String getVersiones (Integer objetoId, Integer objetoTipo){
		String resultado = "";
		String query = "SELECT DISTINCT(r.version) "
			+ "FROM sipro_history.riesgo r, sipro_history.objeto_riesgo ori "
			+ "WHERE r.id=ori.riesgoid "
			+ "AND ori.objeto_id="+ objetoId
			+ " AND ori.objeto_tipo="+ objetoTipo;
		List<?> versiones = CHistoria.getVersiones(query);
		if(versiones!=null){
			for(int i=0; i<versiones.size(); i++){
				if(!resultado.isEmpty()){
					resultado+=",";
				}
				resultado+=(Integer)versiones.get(i);
			}
		}
		return resultado;
	}
	
	public static String getHistoria (Integer objetoId, Integer objetoTipo, Integer version){
		String resultado = "";
		String query = "SELECT DISTINCT(r.version), r.nombre, r.descripcion, rt.nombre as tipo_riesgo, r.impacto, r.probabilidad, "
			+ "r.impacto_monto, r.impacto_tiempo, r.gatillo, r.consecuencia, r.solucion, r.riesgo_segundarios, " 
			+ "CASE r.ejecutado "
			+ "WHEN 0 THEN 'No' "
			+ "WHEN 1 THEN 'Si' "
			+ "END as ejecutado, "
			+ "r.fecha_ejecucion, "
			+ "r.resultado, "
			+ "r.observaciones, "
			+ "CONCAT(IFNULL(c.pnombre, ''), ' ', IFNULL(c.snombre,''), ' ', IFNULL(c.papellido,''), ' ', IFNULL(c.sapellido,'')) as colaborador, "
			+ "r.usuario_creo, r.usuario_actualizo, r.fecha_creacion, r.fecha_actualizacion, " 
			+ "CASE r.estado "
			+ "WHEN 0 THEN 'Inactivo' "
			+ "WHEN 1 THEN 'Activo' "
			+ "END as estado "
			+ "FROM sipro_history.objeto_riesgo ori, sipro_history.riesgo r, sipro_history.riesgo_tipo rt, sipro_history.colaborador c "
			+ "WHERE r.id=ori.riesgoid "
			+ "AND rt.id=r.riesgo_tipoid "
			+ "AND c.id=r.colaboradorid "
			+ "AND ori.objeto_id="+objetoId
			+ " AND ori.objeto_tipo="+ objetoTipo 
			+ " AND r.version="+ version;
		
		String [] campos = {"Version", "Nombre", "Descripción", "Tipo de Riesgo", "Impacto", "Probabilidad", 
				"Impacto Monto(Q)", "Impacto Tiempo(días)", "Evento iniciador del riesgo", "Consecuencia", "Solución", "Riesgos secundarios", 
				"Ejecutado", "Fecha de Ejecución", "Resultado", "Observaciones", "Colaborador",
				"Fecha Creación", "Usuario que creo", "Fecha Actualización", "Usuario que actualizó", 
				"Estado"};
		resultado = CHistoria.getHistoria(query, campos);
		return resultado;
	}
}
