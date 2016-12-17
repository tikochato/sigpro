package shiro.utilities;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.Session;
import dao.UsuarioDAO;
import pojo.Usuario;
import utilities.CHibernateSession;

public class CustomRealm extends JdbcRealm {
	
	@Override
	  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	    
		UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
	    final String username = userPassToken.getUsername();
	    if (username == null) 
	      return null;
	     
	    Session session = CHibernateSession.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			//UserDAO userDAO = new UserDAO(session);
			//UsuarioDAO usuarioDAO = new UsuarioDAO();
			final Usuario user = UsuarioDAO.getUsuario(username);

			if (user == null) {
				System.out.println("No account found for user [" + username + "]");
				return null;
			}

			// return salted credentials
			SaltedAuthenticationInfo info = new CustomSaltedAuthenticationInfo(
					username, user.getPassword(), user.getSalt());

			return info;
		} finally {
			session.getTransaction().commit();
			if (session.isOpen()) session.close();
		}
	  }
	
	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		return UsuarioDAO.tienePermiso(principals.getPrimaryPrincipal().toString(), permission);
	}
}
