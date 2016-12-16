package dao;



import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.Session;

import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pojo.Usuario;
import pojo.UsuarioPermiso;
import pojo.UsuarioPermisoId;
import pojo.Permiso;
import pojo.Usuariolog;
import utilities.CHibernateSession;
import utilities.CLogger;

public class UsuarioDAO {

	public static Usuario getUsuario(String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Usuario ret = null;
		try{
			ret = (Usuario) session.get(Usuario.class,usuario);
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
		Usuariolog usuariolog = new Usuariolog(usuario,new Date());
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(usuariolog);
			session.getTransaction().commit();
		}catch(Throwable e){
			CLogger.write("3", EstadoTablaDAO.class, e);
		}
		finally{
			session.close();
		}
		
	}

	public static boolean tienePermiso(String usuario, String permisonombre) {
		boolean ret=false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder(); 
			CriteriaQuery<Permiso> criteria = builder.createQuery(Permiso.class);
			Root<Permiso> root = criteria.from(Permiso.class);
			criteria.select( root );
			criteria.where( builder.equal( root.get("nombre"), permisonombre ) );
			Permiso permiso = session.createQuery( criteria ).getSingleResult();
			if(permiso !=null){
				UsuarioPermisoId usuariopermisoid = new UsuarioPermisoId(usuario,permiso.getId());
				UsuarioPermiso usuariopermiso = (UsuarioPermiso) session.get(UsuarioPermiso.class, usuariopermisoid);
				if(usuariopermiso!=null){
					ret = true;
				}
			}
			
		}
		catch(Throwable e){
			CLogger.write("4", UsuarioDAO.class, e);
		}
		return ret;
	}
	
	public static boolean registroUsuario(String cadenausuario, String email, String passwordTextoPlano){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Usuario usuario = new Usuario();
			usuario.setUsuario(cadenausuario);
			usuario.setEmail(email);
			RandomNumberGenerator rng = new SecureRandomNumberGenerator();
			Object salt = rng.nextBytes();
			String hashedPasswordBase64 = new Sha256Hash(passwordTextoPlano, salt,1024).toBase64();
			usuario.setPassword(hashedPasswordBase64);
			usuario.setSalt(salt.toString());
			usuario.setFechaCreacion(new Date());
			usuario.setEstado(1);
			session.save(usuario);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("1", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;		
	}
	
}
