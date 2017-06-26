package dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import pojo.Configuraciones;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ConfiguracionesDAO {
	public static String getValorConfiguracionByNombre (String nombre){
		String result = "";
		Session session = CHibernateSession.getSessionFactory().openSession();
		Configuraciones config = null;
		try{
			Query<Configuraciones> criteria = session.createQuery("FROM Configuraciones c where c.nombre=:nombre and c.estado=1)", Configuraciones.class);
			criteria.setParameter("nombre", nombre);
			config = criteria.getSingleResult();
			result = config.getValor();
		}catch (Throwable e) {
			CLogger.write("1", ConfiguracionesDAO.class, e);
		} finally {
			session.close();
		}
		return result;
		
	}
}
