package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.ComponentePropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponentePropiedadDAO {
	
	public static List<ComponentePropiedad> getComponentePropiedadesPorTipoComponentePagina(int pagina,int idTipoComponente){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("select p from ComponentePropiedad p " 
					+ "inner join p.ctipoPropiedads ptp " 
					+ "inner join ptp.componenteTipo pt  "
					+ "where pt.id =  " + idTipoComponente + " ",ComponentePropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalComponentePropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM CompoentePropiedad p ",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<ComponentePropiedad> getComponentePropiedadPaginaTotalDisponibles(int pagina, int numerocomponentepropiedades, String idPropiedades){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("select p from ComponentePropiedad p  "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " where p.id not in ("+ idPropiedades + ")" : "") 
					,ComponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numerocomponentepropiedades)));
			criteria.setMaxResults(numerocomponentepropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
