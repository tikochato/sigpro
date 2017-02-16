package dao;

import static org.junit.Assert.*;

import org.junit.Test;
public class UsuarioDAOTest {
	
	@Test
	public void getUsuarioTest(){
		assertEquals(UsuarioDAO.getUsuario("test test test"),null);
	}
	
	@Test
	public void tienePermisoTest(){
		assertEquals(UsuarioDAO.tienePermiso("unit_test", "unit_test"),false);
	}
	
	@Test
	public void 	registroUsuarioTest(){
		assertEquals(UsuarioDAO.registroUsuario(null, null, null, null),false);
	}
	
	@Test
	public void cambiarPasswordTest(){
		assertEquals(UsuarioDAO.cambiarPassword(null, null, null),false);
	}
	
	@Test
	public void asignarPermisosUsuarioTest(){
		assertEquals(UsuarioDAO.asignarPermisosUsuario(null, null, null),false);
	}
	
	@Test
	public void desactivarPermisosUsuarioTest(){
		assertEquals(UsuarioDAO.desactivarPermisosUsuario(null, null, null),false);
	}
	
	@Test
	public void existeUsuarioTest(){
		assertEquals(UsuarioDAO.existeUsuario(null),false);
	}
	
	@Test
	public void desactivarUsuarioTest(){
		assertEquals(UsuarioDAO.desactivarUsuario(null, null),false);
	}
	
	@Test
	public void editarUsuarioTest(){
		assertEquals(UsuarioDAO.editarUsuario(null, null),false);
	}
	
	@Test
	public void getPermisosActivosUsuarioTest(){
		assertNotNull(UsuarioDAO.getPermisosActivosUsuario(null));
	}
	
	@Test
	public void getPermisosDisponiblesTest(){
		assertNotNull(UsuarioDAO.getPermisosDisponibles(null));
	}
	
	@Test
	public void getUsuariosTest(){
		assertNotNull(UsuarioDAO.getUsuarios(1, 1, "","","",""));
	}
	
	@Test
	public void getTotalUsuariosTest(){
		assertEquals(UsuarioDAO.getTotalUsuarios("","","","").getClass(),Long.class);
	}
	
	

}
