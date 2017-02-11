package dao;


import static org.junit.Assert.*;

import org.junit.Test;
public class ProductoPropiedadValorDAOTest {
	
	@Test
	public void getProductoPropiedadValorTest(){
		assertNotNull(ProductoPropiedadValorDAO.getProductoPropiedadValor(0));
	}
	
	@Test
	public void guardarTest(){
		assertEquals(ProductoPropiedadValorDAO.guardar(0, 0, null, 0, "unit_test", null, "admin"),false);
	}
	
	@Test
	public void actualizarTest(){
		assertEquals(ProductoPropiedadValorDAO.actualizar(0, 0, null, null, "unit_test", null, "admin"),false);
	}
	
	@Test
	public void eliminarTest(){
		assertEquals(ProductoPropiedadValorDAO.eliminar(0, 0, "admin"),false);
	}
	
	@Test
	public void getPaginaTest(){
		assertNotNull(ProductoPropiedadValorDAO.getPagina(1, 1, 1));
	}
	
	@Test
	public void getJsonTest(){
		assertNotNull(ProductoPropiedadValorDAO.getJson(1));
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(ProductoPropiedadValorDAO.getTotal().getClass(),Long.class);
	}
	
	@Test
	public void getProductoPropiedadValorTes(){
		assertNotNull(ProductoPropiedadValorDAO.getProductoPropiedadValor(0));
	}
	
	@Test
	public void getJsonTest1(){
		assertEquals(ProductoPropiedadValorDAO.getJson(0).getClass(),String.class);
	}
	
	@Test
	public void persistirValoresTest(){
		//assertEquals(ProductoPropiedadValorDAO.persistirValores(null, null, null),true	);
	}
	

}
