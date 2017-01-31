package dao;
import static org.junit.Assert.*;
import org.junit.Test;

import pojo.MetaUnidadMedida;


public class MetaUnidadMedidaDAOTest {
	
	@Test
	public void getMetaUnidadMedidasTest(){
		assertNotNull(MetaUnidadMedidaDAO.getMetaUnidadMedidas());
	}
	
	@Test
	public void getMetaUnidadMedidaPorIdTest(){
		assertEquals(MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(0),null);
	}
	
	@Test
	public void guardarMetaUnidadMedidaTest(){
		assertEquals(MetaUnidadMedidaDAO.guardarMetaUnidadMedida(new MetaUnidadMedida()),false);
	}
	
	@Test
	public void eliminarMetaUnidadMedidaTest(){
		assertEquals(MetaUnidadMedidaDAO.eliminarMetaUnidadMedida(new MetaUnidadMedida()),false);
	}
	
	@Test
	public void eliminarTotalMetaUnidadMedidaTest(){
		assertNotNull(MetaUnidadMedidaDAO.eliminarTotalMetaUnidadMedida(new MetaUnidadMedida()));
	}
	
	@Test
	public void getMetaUnidadMedidasPaginaTest(){
		assertNotNull(MetaUnidadMedidaDAO.getMetaUnidadMedidasPagina(1, 1, "", "", "", "", ""));
	}
	
	@Test
	public void getTotalMetaUnidadMedidasTest(){
		assertEquals(MetaUnidadMedidaDAO.getTotalMetaUnidadMedidas( "", "", "").getClass(),Long.class);
	}
}
