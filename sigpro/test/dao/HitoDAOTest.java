package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.Hito;

public class HitoDAOTest {
	
	@Test
	public void getHitosTest(){
		assertNotNull(HitoDAO.getHitos());
	}
	
	@Test
	public void getHitoPorIdTest(){
		assertEquals(HitoDAO.getHitoPorId(0),null);
	}
	
	@Test
	public void guardarHitoTest(){
		assertEquals(HitoDAO.guardarHito(new Hito()),false);
	}
	
	@Test
	public void eliminarHitoTest(){
		assertEquals(HitoDAO.eliminarHito(new Hito()),false);
	}
	
	@Test
	public void eliminarTotalHitoTest(){
		assertNotNull(HitoDAO.eliminarTotalHito(new Hito()));
	}
	
	@Test
	public void getHitosPaginaTest(){
		assertNotNull(HitoDAO.getHitosPagina(1, 1));
	}
	
	@Test
	public void getTotalHitosTest(){
		assertEquals(HitoDAO.getTotalHitos().getClass(),Long.class);
	}
	
	@Test
	public void getHitosPaginaPorProyectoTest(){
		assertNotNull(HitoDAO.getHitosPaginaPorProyecto(1, 1, 0,"","","","",""));
	}
	
	@Test
	public void getTotalHitosPorProyectoTest(){
		assertEquals(HitoDAO.getTotalHitosPorProyecto(0,"","","").getClass(),Long.class);
	}

}
