package dao;

import static org.junit.Assert.*;


import org.junit.Test;

import pojo.RiesgoPropiedad;

public class RiesgoPropiedadDAOTest {
	
	@Test
	public void getRiesgoPropiedadesPorTipoRiesgoPaginaTest(){
		assertNotNull(RiesgoPropiedadDAO.getRiesgoPropiedadesPorTipoRiesgoPagina(1, 1));
	}
	
	@Test
	public void getTotalRiesgoPropiedadesTest(){
		assertEquals(RiesgoPropiedadDAO.getTotalRiesgoPropiedades().getClass(),Long.class);
	}
	
	@Test
	public void getRiesgoPropiedadPaginaTotalDisponiblesTest(){
		assertNotNull(RiesgoPropiedadDAO.getRiesgoPropiedadPaginaTotalDisponibles(0, 0, "{}"));
	}
	
	@Test
	public void getRiesgoPropiedadPorIdTest(){
		assertEquals(RiesgoPropiedadDAO.getRiesgoPropiedadPorId(0),null);
	}
	
	@Test
	public void eliminarRiesgoPropiedadTest(){
		assertEquals(RiesgoPropiedadDAO.eliminarRiesgoPropiedad(new RiesgoPropiedad()),false);
	}
	
	@Test
	public void eliminarTotalRiesgoPropiedadTest(){
		assertNotNull(RiesgoPropiedadDAO.eliminarTotalRiesgoPropiedad(null));
	}
	
	@Test
	public void getRiesgoPropiedadesPaginaTest(){
		assertNotNull(RiesgoPropiedadDAO.getRiesgoPropiedadesPagina(0, 0,"","","","",""));
	}
	
	@Test
	public void getTotalRiesgoPropiedadTest(){
		assertEquals(RiesgoPropiedadDAO.getTotalRiesgoPropiedad("","","").getClass(),Long.class);
	}
	
}
