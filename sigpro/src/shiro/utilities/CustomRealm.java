package shiro.utilities;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.mariadb.jdbc.MariaDbDataSource;

import dao.UsuarioDAO;
import pojo.Usuario;
import utilities.CLogger;
import utilities.CProperties;

public class CustomRealm extends JdbcRealm {
	
	public CustomRealm() {
		super();
		MariaDbDataSource mariadbDS = new MariaDbDataSource();
		try {
			mariadbDS.setUrl(String.join("", "jdbc:mysql://",CProperties.getmemsql_host(),":", String.valueOf(CProperties.getmemsql_port()), "/", CProperties.getmemsql_schema()));
			mariadbDS.setUser(CProperties.getmemsql_user());
			mariadbDS.setPassword(CProperties.getmemsql_password());
			this.setDataSource(mariadbDS);
		} catch (Exception e) {
			CLogger.write("1", CustomRealm.class, e);
		}
	}
	
	@Override
	  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	    
		UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
	    final String username = userPassToken.getUsername();
	    if (username == null) 
	      return null;
	     
	    Usuario user = UsuarioDAO.getUsusuario(username);
	    if (user == null) 
	      return null;
	    
	    SaltedAuthenticationInfo info = new CustomSaltedAuthenticationInfo(username, user.getPassword(), user.getSalt());
	 
	    return info;
	  }
	
	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		return UsuarioDAO.tienePermiso(principals.getPrimaryPrincipal().toString(), permission);
	}
}
