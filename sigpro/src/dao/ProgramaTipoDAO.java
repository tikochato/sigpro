package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProgramaTipo;
import pojo.ProgtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProgramaTipoDAO {
	public static List<ProgramaTipo> getProgramaTipos(){
		List<ProgramaTipo> ret = new ArrayList<ProgramaTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProgramaTipo> criteria = builder.createQuery(ProgramaTipo.class);
			Root<ProgramaTipo> root = criteria.from(ProgramaTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ProgramaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static ProgramaTipo getProgramaTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProgramaTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProgramaTipo> criteria = builder.createQuery(ProgramaTipo.class);
			Root<ProgramaTipo> root = criteria.from(ProgramaTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			List<ProgramaTipo> lista = session.createQuery(criteria).getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		}catch(Throwable e){
			CLogger.write("2", ProgramaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarProgramaTipo(ProgramaTipo programatipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(programatipo);
			session.flush();

			if (programatipo.getProgtipoPropiedads()!=null && programatipo.getProgtipoPropiedads().size()>0){
				for (ProgtipoPropiedad propiedad : programatipo.getProgtipoPropiedads()){
					session.saveOrUpdate(propiedad);
				}
			}
			session.flush();

			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProgramaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<ProgramaTipo> getProgramaTipoPagina(int pagina, int numeroprogramatipos,
			String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ProgramaTipo> ret = new ArrayList<ProgramaTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT p FROM ProgramaTipo p WHERE estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<ProgramaTipo> criteria = session.createQuery(query,ProgramaTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroprogramatipos)));
			criteria.setMaxResults(numeroprogramatipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ProgramaTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalProgramaTipos(String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM ProgramaTipo p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));			
			
			Query<Long> conteo = session.createQuery(query,Long.class);

			List<Long> lista = conteo.getResultList();
			ret = !lista.isEmpty() ? lista.get(0) : null;
		}catch(Throwable e){
			CLogger.write("5", ProgramaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarProgramaTipo(ProgramaTipo programaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{

			programaTipo.setEstado(0);
			session.beginTransaction();
			session.update(programaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ProgramaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
