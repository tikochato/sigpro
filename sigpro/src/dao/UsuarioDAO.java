package dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.Usuario;
import utilities.CHibernateSession;
import utilities.CLogger;

public class UsuarioDAO {

	public static Usuario getUsuario(String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Usuario ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
			Root<Usuario> root = criteria.from(Usuario.class);
			criteria.select( root );
			criteria.where( builder.equal( root.get("usuario"), usuario ) );
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static void userLoginHistory(String usuario) {
		// TODO Auto-generated method stub
		
	}

	public static boolean tienePermiso(String string, String permission) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
