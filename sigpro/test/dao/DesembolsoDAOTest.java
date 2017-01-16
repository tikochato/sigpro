package dao;
import static org.junit.Assert.*;

import org.junit.Test;

import pojo.Desembolso;
public class DesembolsoDAOTest {
	
	@Test
	public void getDesembolsosTest(){
		assertNotNull(DesembolsoDAO.getDesembolsos());
	}
	
	@Test
	public void getDesembolsoPorIdTest(){
		assertEquals(DesembolsoDAO.getDesembolsoPorId(0), null);
	}
	
	@Test
	public void guardarDesembolsoTest(){
		assertEquals(DesembolsoDAO.guardarDesembolso(new Desembolso()), false);
	}
	
	@Test
	public void eliminarDesembolsoTest(){
		assertEquals(DesembolsoDAO.eliminarDesembolso(new Desembolso()),false);
	}
	
	@Test
	public void eliminarTotalDesembolsoTest(){
		assertNotNull(DesembolsoDAO.eliminarTotalDesembolso(new Desembolso()));
	}
	
	@Test
	public void getDesembolsosPaginaTest(){
		assertNotNull(DesembolsoDAO.getDesembolsosPagina(1, 1));
	}
	
	@Test
	public void getTotalDesembolsosTest(){
		assertEquals(DesembolsoDAO.getTotalDesembolsos().getClass(),Long.class);
	}

}
