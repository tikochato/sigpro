package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Programa;
import pojo.ProgramaProyecto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProgramaDAO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static List<Programa> getProgramas(){
		List<Programa> ret = new ArrayList<Programa>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Programa> criteria = session.createQuery("FROM Programa p where p.estado = 1", Programa.class);
			
			ret =   (List<Programa>)criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", Programa.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarPrograma(Programa programa){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(programa);
			session.flush();
			
			if (programa.getProgramaProyectos()!=null){
				for (ProgramaProyecto programaProyecto : programa.getProgramaProyectos()){
					session.saveOrUpdate(programaProyecto);
				}
			}
			session.flush();
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProgramaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Programa getProgramaPorId(int id){

		Session session = CHibernateSession.getSessionFactory().openSession();
		Programa ret = null;
		try{
			Query<Programa> criteria = session.createQuery("FROM Programa where id=:id", Programa.class);
			criteria.setParameter("id", id);
			ret = (Programa) criteria.getSingleResult();;
		}
		catch(Throwable e){
			CLogger.write("3", ProgramaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarPrograma(Programa programa){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			programa.setEstado(0);
			session.beginTransaction();
			session.update(programa);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProgramaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalProgramas(String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM Programa p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", ProgramaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Programa> getProgramaPagina(int pagina, int numeroprograma,
			String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){
		List<Programa> ret = new ArrayList<Programa>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Programa p WHERE p.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Programa> criteria = session.createQuery(query,Programa.class);
			criteria.setFirstResult(((pagina-1)*(numeroprograma)));
			criteria.setMaxResults(numeroprograma);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", Programa.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
