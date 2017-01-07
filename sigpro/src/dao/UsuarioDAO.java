package dao;



import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pojo.Usuario;
import pojo.UsuarioLog;
import pojo.UsuarioLogId;
import pojo.UsuarioPermiso;
import pojo.UsuarioPermisoId;
import pojo.Permiso;

import java.util.ArrayList;
import java.util.List;
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
		UsuarioLogId usuariologid = new UsuarioLogId(usuario, new DateTime().toDate());
		UsuarioLog usuariolog = new UsuarioLog(usuariologid);
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
					if(usuariopermiso.getEstado()==1){
						ret = true;
					}
				}
			}
			
		}
		catch(Throwable e){
			CLogger.write("4", UsuarioDAO.class, e);
		}
		return ret;
	}
	
	public static boolean registroUsuario(String cadenausuario, String email, String passwordTextoPlano, String usuarioCreo){
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
			usuario.setFechaCreacion(new DateTime().toDate());
			usuario.setUsuarioCreo(usuarioCreo);
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
	public static boolean asignarPermisosUsuario(String usuario, List <Integer> permisos,String usuarioTexto){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i =0; i<permisos.size();i++){
				UsuarioPermisoId usuariopermisoid = new UsuarioPermisoId(usuario, permisos.get(i));
				UsuarioPermiso usuariopermiso = new UsuarioPermiso();
				usuariopermiso.setId(usuariopermisoid);
				usuariopermiso.setUsuarioCreo(usuarioTexto);
				usuariopermiso.setEstado(1);
				usuariopermiso.setFechaCreacion(new DateTime().toDate());
				session.save(usuariopermiso);
				if( i % 20 == 0 ){
					session.flush();
		            session.clear();
		        }
			}
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("2", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean desactivarPermisosUsuario(String usuario, List <Integer> permisos, String usuarioTexto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i =0; i<permisos.size();i++){
				UsuarioPermisoId usuariopermisoid = new UsuarioPermisoId(usuario, permisos.get(i));
				UsuarioPermiso usuariopermiso= (UsuarioPermiso)session.get(UsuarioPermiso.class, usuariopermisoid);
				usuariopermiso.setUsuarioActualizo(usuarioTexto);
				usuariopermiso.setEstado(0);
				usuariopermiso.setFechaActualizacion(new DateTime().toDate());
				session.saveOrUpdate(usuariopermiso);
				if( i % 20 == 0 ){
					session.flush();
		            session.clear();
		        }
			}
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("3", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean existeUsuario(String usuario){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			if (session.get(Usuario.class,usuario)!=null){
				ret=true;
			}
		}
		catch(Throwable e){
			CLogger.write("5", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean desactivarUsuario(String usuario, String usuarioActualizo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Usuario usuarioDesactivado = (Usuario) session.get(Usuario.class,usuario);
			usuarioDesactivado.setEstado(0);
			usuarioDesactivado.setUsuarioActualizo(usuarioActualizo);
			usuarioDesactivado.setFechaActualizacion(new DateTime().toDate());
			session.saveOrUpdate(usuarioDesactivado);
			session.getTransaction().commit();
			ret=true;
		}
		catch(Throwable e){
			CLogger.write("5", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static List <UsuarioPermiso> getPermisosActivosUsuario(String usuario){
		List <UsuarioPermiso> ret = new ArrayList <UsuarioPermiso> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<UsuarioPermiso> criteria = session.createQuery("FROM UsuarioPermiso where usuario=:usuario", UsuarioPermiso.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("6", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static List <Usuario> getUsuarios(int pagina, int numeroUsuarios){
		List <Usuario> ret = new ArrayList<Usuario> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query <Usuario> criteria = session.createQuery("FROM Usuario where estado =:estado", Usuario.class);
			criteria.setParameter("estado",1);
			criteria.setFirstResult(((pagina-1)*(numeroUsuarios)));
			criteria.setMaxResults(numeroUsuarios);
			ret =criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("7", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalUsuarios(){
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(u.usuario) FROM Usuario u WHERE u.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}catch(Throwable e){
			CLogger.write("8", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;		
	}
	
}
