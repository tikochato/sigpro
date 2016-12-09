package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

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
}
