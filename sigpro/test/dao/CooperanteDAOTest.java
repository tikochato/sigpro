package dao;
import static org.junit.Assert.*;

import org.junit.Test;

import pojo.Cooperante;

public class CooperanteDAOTest {

	
	@Test
	public void getCooperantesTest(){
		assertNotNull(CooperanteDAO.getCooperantes());	
	}
	
	@Test
	public void getCooperantePorIdTest(){
		assertEquals(CooperanteDAO.getCooperantePorId(1000000),null);
	}
	
	@Test
	public void guardarCooperanteTest(){
		assertEquals(CooperanteDAO.guardarCooperante(new Cooperante()), false);
	}
	
	@Test
	public void eliminarCooperanteTest(){
		assertEquals(CooperanteDAO.eliminarCooperante(new Cooperante()),false);
	}
	
	@Test
	public void eliminarTotalCooperanteTest(){
		assertNotNull(CooperanteDAO.eliminarTotalCooperante(new Cooperante()));
	}
	
	@Test
	public void getCooperantesPaginaTest(){
		assertNotNull(CooperanteDAO.getCooperantesPagina(1, 1, "", "", "", "", "",""));
	}
	
	@Test
	public void getTotalCooperantesTest(){
		assertEquals(CooperanteDAO.getTotalCooperantes("","","","").getClass(),Long.class);
	}

}
