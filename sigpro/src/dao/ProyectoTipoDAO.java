package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Cooperante;
import pojo.ProyectoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoTipoDAO {

	public static List<ProyectoTipo> getProyectoTipos(){
		List<ProyectoTipo> ret = new ArrayList<ProyectoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProyectoTipo> criteria = builder.createQuery(ProyectoTipo.class);
			Root<ProyectoTipo> root = criteria.from(ProyectoTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoTipo> getProyectosTipoPagina(int pagina, int numeroproyectotipos){
		List<ProyectoTipo> ret = new ArrayList<ProyectoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoTipo> criteria = session.createQuery("SELECT p FROM ProyectoTipo p WHERE estado = 1",ProyectoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectotipos)));
			criteria.setMaxResults(numeroproyectotipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectoTipos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM Proyectotipo p WHERE p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
