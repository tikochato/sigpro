package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.ProyectoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoPropiedadDAO {
	
	
	public static List<ProyectoPropiedad> getProyectoPropiedadesPorTipoProyectoPagina(int pagina,int idTipoProyecto){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedad> criteria = session.createQuery("select p from ProyectoPropiedad p "
					+ "inner join p.ptipoPropiedads ptp "
					+ "inner join ptp.proyectoTipo pt "
					+ "where pt.id =  " + idTipoProyecto + " "
					+ "and p.estado = 1",ProyectoPropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalProyectoPropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM ProyectoPropiedad p WHERE p.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<ProyectoPropiedad> getProyectoPropiedadPaginaTotalDisponibles(int pagina, int numeroproyectopropiedades, String idPropiedades){
		List<ProyectoPropiedad> ret = new ArrayList<ProyectoPropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedad> criteria = session.createQuery("select p from ProyectoPropiedad p  WHERE p.estado = 1 "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " and p.id not in ("+ idPropiedades + ")" : "") 
					,ProyectoPropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectopropiedades)));
			criteria.setMaxResults(numeroproyectopropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoPropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
