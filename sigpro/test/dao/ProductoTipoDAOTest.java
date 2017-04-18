package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProductoTipoDAOTest {

	
	@Test
	public void guardarTest(){
		assertEquals(ProductoTipoDAO.guardar(0, "unit_test", "unit_test", "unit_test", "admin"),null);
	}
	
	@Test
	public void getProductoTipoTest(){
		assertEquals(ProductoTipoDAO.getProductoTipo(2),null);
	}
	
	@Test
	public void actualizarTest(){
		assertEquals(ProductoTipoDAO.actualizar(0, "unit_test", "unit_test1", "unit_test", "admin"),false);
	}
	
	
	@Test
	public void  getPaginaTest(){
		assertNotNull(ProductoTipoDAO.getPagina(1, 1,"","","","",""));
	}
	
	@Test
	public void eliminarTest(){
		assertNotNull(ProductoTipoDAO.eliminar(0, "admin"));
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(ProductoTipoDAO.getTotal("","","").getClass(),Long.class);
	}
	
}
