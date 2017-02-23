package dao;

import static org.junit.Assert.*;

import org.junit.Test;
import pojo.MetaTipo;

public class MetaTipoDAOTest {

	
	@Test
	public void getMetaTiposTest(){
		assertNotNull(MetaTipoDAO.getMetaTipos());
	}
	@Test
	public void getMetaTipoPorIdTest(){
		assertEquals(MetaTipoDAO.getMetaTipoPorId(1),null);
	}
	@Test
	public void guardarMetaTipoTest(){
		assertEquals(MetaTipoDAO.guardarMetaTipo(new MetaTipo()),false);
	}
	@Test
	public void eliminarMetaTipoTest(){
		assertEquals(MetaTipoDAO.eliminarMetaTipo(new MetaTipo()),false);
	}
	
	@Test
	public void eliminarTotalMetaTipoTest(){
		assertNotNull(MetaTipoDAO.eliminarTotalMetaTipo(new MetaTipo()));
	}
	
	@Test
	public void getMetaTiposPaginaTest(){
		assertNotNull(MetaTipoDAO.getMetaTiposPagina(1, 1, "", "", "", "", ""));
	}
	
	@Test
	public void getTotalMetaTiposTest(){
		assertEquals(MetaTipoDAO.getTotalMetaTipos("","","").getClass(),Long.class);
	}
	
	
}
