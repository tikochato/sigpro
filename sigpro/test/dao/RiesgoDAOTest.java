package dao;


import static org.junit.Assert.*;

import org.junit.Test;

import pojo.Riesgo;
public class RiesgoDAOTest {
	@Test
	public void getRiesgosTest(){
		assertNotNull(RiesgoDAO.getRiesgos());
	}
	
	@Test
	public void getRiesgoPorIdTest(){
		assertEquals(RiesgoDAO.getRiesgoPorId(0),null);
	}
	
	@Test
	public void guardarRiesgoTest(){
		assertEquals(RiesgoDAO.guardarRiesgo(new Riesgo()),false);
	}
	
	@Test
	public void eliminarRiesgoTest(){
		assertEquals(RiesgoDAO.eliminarRiesgo(new Riesgo()),false);
	}
	
	@Test
	public void eliminarTotalRiesgoTest(){
		assertNotNull(RiesgoDAO.eliminarTotalRiesgo(new Riesgo()));
	}
	
	@Test
	public void getRiesgosPaginaTest(){
		assertNotNull(RiesgoDAO.getRiesgosPagina(1, 1));
	}
	
	
	@Test
	public void getTotalRiesgosTest(){
		assertEquals(RiesgoDAO.getTotalRiesgos().getClass(),Long.class);
	}
	
	@Test
	public void getRiesgosPaginaPorProyectoTest(){
		assertNotNull(RiesgoDAO.getRiesgosPaginaPorProyecto(0,0, 0));
	}
	
	@Test
	public void getTotalRiesgosPorProyectoTest(){
		assertEquals(RiesgoDAO.getTotalRiesgosPorProyecto(0).getClass(),Long.class);
	}
	
	
}
