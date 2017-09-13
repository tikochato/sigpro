package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnidadEjecutoraDAOTest {
	
	@Test
	public void getUnidadEjecutoraTest(){
		assertEquals(UnidadEjecutoraDAO.getUnidadEjecutora(2017,0,0),null);
	}
	
	@Test
	public void guardarTest(){
		assertEquals(UnidadEjecutoraDAO.guardar(0,2017,0, "admin"),false);
	}
	
	@Test
	public void 	actualizarTest(){
		assertEquals(UnidadEjecutoraDAO.actualizar(0,2017,0, "admin"),false);
	}
	
	
	@Test
	public void getPaginaTest(){
		assertNotNull(UnidadEjecutoraDAO.getPagina(2017,0,1, 1));
	}
	
	@Test
	public void getJsonTest(){
		assertEquals(UnidadEjecutoraDAO.getJson(2017,0,1, 1).getClass(),String.class);
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(UnidadEjecutoraDAO.getTotal(2017,0).getClass(),Long.class);
	}
	
	@Test
	public void getUnidadEjecutorasTest(){
		assertNotNull(UnidadEjecutoraDAO.getUnidadEjecutoras(2017,0));
	}
}
