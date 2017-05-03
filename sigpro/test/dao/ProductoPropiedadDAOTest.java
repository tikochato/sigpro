package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProductoPropiedadDAOTest {
	
	@Test
	public void getProductoPropiedadTest(){
		assertEquals(ProductoPropiedadDAO.getProductoPropiedad(0),null);
	}
	
	@Test
	public void guardarTest(){
		assertEquals(ProductoPropiedadDAO.guardar(0, "unit_test", "unit_test", "admin", 0),false);
	}
	
	@Test
	public void actualizarTest(){
		assertEquals(ProductoPropiedadDAO.actualizar(0, "unit_test", "unit_test", "admin", 0),false);
	}
	
	@Test
	public void eliminarTest(){
		assertEquals(ProductoPropiedadDAO.eliminar(0, "admin"),false);
	}
	
	
	@Test
	public void getPaginaTest(){
		assertNotNull (ProductoPropiedadDAO.getPagina(1, 1,"","","","",""));
	}
	
	@Test
	public void getJsonTest(){
		//assertEquals(ProductoPropiedadDAO.getJson(1, 1,"","","","","").getClass(), String.class);
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(ProductoPropiedadDAO.getTotal("","","").getClass(),Long.class);
	}
}
