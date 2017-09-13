package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class ColaboradorDAOTest {
	
	@Test
	public void getColaboradorTest(){
		assertNull(ColaboradorDAO.getColaborador(0));
	}
	
	@Test
	public void guardarTest(){
		assertNotNull(ColaboradorDAO.guardar(1, "unit_test", "unit_test", "", "unit_Test", "unit_test", "unit_test", 0L, 2017,1,1, "admin", "",null));
	}
	


	
	@Test
	public void getPaginaTest(){
		assertNotNull(ColaboradorDAO.getPagina(1, 1,"","","","","","","","",""));
	}
	
	@Test
	public void getJsonTest(){
		assertEquals(ColaboradorDAO.getJson(1, 1,"","","","","","","","").getClass(),String.class);
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(ColaboradorDAO.getTotal("","","","","","").getClass(), Long.class);
	}
	
	@Test
	public void validarUsuarioTest(){
		assertNotNull(ColaboradorDAO.validarUsuario("admin"));
	}
}
