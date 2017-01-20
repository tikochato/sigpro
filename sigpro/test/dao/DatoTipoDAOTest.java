package dao;

import static org.junit.Assert.*;


import org.junit.Test;


public class DatoTipoDAOTest {

	
	
	@Test
	public void getDatoTipoTest(){
		assertNotNull(DatoTipoDAO.getDatoTipo(1).getClass());
	}
	
	@Test
	public void guardarTest(){
		assertEquals(DatoTipoDAO.guardar(0, null, null),false);
	}
	
	
	@Test
	public void getJsonComboTest(){
		assertNotNull(DatoTipoDAO.getJsonCombo());
	}
	
	@Test
	public void getJsonTest(){
		assertNotNull(DatoTipoDAO.getJson());
	}
	
	@Test
	public void getDatoTiposTest(){
		assertNotNull(DatoTipoDAO.getDatoTipos());
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(DatoTipoDAO.getTotal().getClass(),Long.class);
	}
	@Test
	public void getPaginaTest(){
		assertNotNull(DatoTipoDAO.getPagina(1, 1));
	}
	
	@Test
	public void actualizarTest(){
		assertEquals(DatoTipoDAO.actualizar(0, null,""), false);
	}
	
	
	
	
}
