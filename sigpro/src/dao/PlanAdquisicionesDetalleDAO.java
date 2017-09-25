package dao;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisicionesDetalle;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionesDetalleDAO {
	public static boolean guardarPlanAdquisicion(PlanAdquisicionesDetalle planAdquisicionDetalle){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicionDetalle);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionesDetalleDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisicionesDetalle getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
		PlanAdquisicionesDetalle ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicionesDetalle where objetoId=:objetoId and objetoTipo=:objetoTipo";
			Query<PlanAdquisicionesDetalle> criteria = session.createQuery(query, PlanAdquisicionesDetalle.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getSingleResult();
		}
		catch(NoResultException e){
			
		}
		catch(Throwable e){
			CLogger.write("2", PlanAdquisicionesDetalleDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalAdquisicionesPorObjeto(Integer objetoId, Integer objetoTipo, String filtro_unidad_medida, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(pad.id) FROM PlanAdquisicionesDetalle pad WHERE pad.estado=1 and pad.objetoId=:objetoId and pad.objetoTipo=:objetoTipo ";
			String query_a="";
			if(filtro_unidad_medida!=null && filtro_unidad_medida.trim().length()>0)
				query_a = String.join("",query_a, " pad.unidadMedida LIKE '%",filtro_unidad_medida,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " pad.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(pad.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("3", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
