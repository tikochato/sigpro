package shiro.utilities;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;

import pojo.Usuario;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

public class CShiro {
	private static Factory<SecurityManager> factory;
	private static SecurityManager securityManager;
	
	static {
		try {
			factory = new IniSecurityManagerFactory();
			securityManager = factory.getInstance();
		    
			SecurityUtils.setSecurityManager(securityManager);
		} catch (Throwable ex) {
			
		}
	}
	
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	public static void setAttribute(String name, Object value){
		SecurityUtils.getSubject().getSession(false).setAttribute(name, value);
	}
	
	public static Object getAttribute(String name){
		return SecurityUtils.getSubject().getSession(false).getAttribute(name);
	}
	
	public static boolean hasPermission(String permission){
		return getSubject().isPermitted(permission);
	}
	
	public static boolean[] hasPermissions(String[] permissions){
		return getSubject().isPermitted(permissions);
	}
	
	public static boolean atLeastOnePermssion(String[] permissions){
		boolean[] bpermissions = getSubject().isPermitted(permissions);
		boolean ret = false;
		for(boolean perm : bpermissions){
			if(perm)
				ret = true;
		}
		return ret;
	}
	
	public static String getIdUser(){
		Usuario user = (Usuario)getAttribute("user");
		return user.getUsuario();
	}

}
