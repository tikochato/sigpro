package dao;

import static org.junit.Assert.*;

import org.junit.Test;

import pojo.SubcomponenteTipo;

public class SubComponenteTipoDAOTest {
	

	
	@Test
	public void getSubComponenteTiposTest(){
		assertNotNull(SubComponenteTipoDAO.getSubComponenteTipos());
	}
	
	@Test
	public void getSubComponenteTipoPorIdTest(){
		assertEquals(SubComponenteTipoDAO.getSubComponenteTipoPorId(1),null);
	}
	
	@Test
	public void guardarSubComponenteTipoTest(){
		assertEquals(SubComponenteTipoDAO.guardarSubComponenteTipo(new SubcomponenteTipo()),false);
	}
	
	@Test
	public void eliminarSubComponenteTipoTest(){
		assertEquals(SubComponenteTipoDAO.eliminarSubComponenteTipo(new SubcomponenteTipo()),false);
	}
	
	@Test
	public void eliminarTotalSubComponenteTipo(){
		assertNotNull(SubComponenteTipoDAO.eliminarTotalSubComponenteTipo(new SubcomponenteTipo()));
	}
	
	@Test
	public void getSubComponenteTiposPagina(){
		assertNotNull(SubComponenteTipoDAO.getSubComponenteTiposPagina(1, 1,"","","","",""));
	}
	
	@Test
	public void getTotalSubComponenteTipo(){
		assertNotNull(SubComponenteTipoDAO.getTotalSubComponenteTipo("","",""));
	}

}
