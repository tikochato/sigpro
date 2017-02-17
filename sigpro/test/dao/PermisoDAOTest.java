package dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import pojo.Permiso;
import org.joda.time.DateTime;


public class PermisoDAOTest {
    private static Permiso permisoPrueba;
	@Before
	public void before(){
		 permisoPrueba  = new Permiso("test","unit_test", "admin", new DateTime().toDate(),1);
	}
	@Test 
	public void getPermisosTest(){
		assertNotNull(PermisoDAO.getPermisos());
	}
	
	@Test
	public void guardarPermisoTest(){
		assertEquals(PermisoDAO.guardarPermiso(permisoPrueba), false);
	}
	
	@Test
	public void getPermisoTest(){
		Permiso permisoTmp = PermisoDAO.getPermiso("");
		assertEquals(permisoTmp,null);
	}
	
	@Test
	public void getPermisoByIdTest(){
		assertEquals(PermisoDAO.getPermisoById(1),null);
	}
	
	@Test
	public void getPermisoPagina(){
		assertNotNull(PermisoDAO.getPermisosPagina(1, 1,"","","",""));
	}
	
	@Test
	public void getTotalPermiso(){
		assertNotNull(PermisoDAO.getTotalPermisos("","","",""));
	}
	
	@Test
	public void eliminarPermisoTest(){
		Permiso permisoTmp = new Permiso("","", "", new DateTime().toDate(),1);
		PermisoDAO.guardarPermiso(permisoTmp);
		assertEquals(PermisoDAO.eliminarPermiso(permisoTmp),false);
	}
	
}
