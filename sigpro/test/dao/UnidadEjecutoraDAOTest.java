package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnidadEjecutoraDAOTest {
	
	@Test
	public void getUnidadEjecutoraTest(){
		assertEquals(UnidadEjecutoraDAO.getUnidadEjecutora(0),null);
	}
	
	@Test
	public void guardarTest(){
		assertEquals(UnidadEjecutoraDAO.guardar(0, "admin", 0),false);
	}
	
	@Test
	public void 	actualizarTest(){
		assertEquals(UnidadEjecutoraDAO.actualizar(0, "admin", 0),false);
	}
	
	
	@Test
	public void getPaginaTest(){
		assertNotNull(UnidadEjecutoraDAO.getPagina(1, 1));
	}
	
	@Test
	public void getJsonTest(){
		assertEquals(UnidadEjecutoraDAO.getJson(1, 1).getClass(),String.class);
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(UnidadEjecutoraDAO.getTotal().getClass(),Long.class);
	}
	
	@Test
	public void getUnidadEjecutorasTest(){
		assertNotNull(UnidadEjecutoraDAO.getUnidadEjecutoras());
	}
}
