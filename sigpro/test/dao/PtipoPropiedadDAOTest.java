package dao;

import static org.junit.Assert.*;

import org.junit.Test;

import pojo.PtipoPropiedad;
public class PtipoPropiedadDAOTest {

	
	@Test
	public void eliminarPtipoPropiedadTest(){
		assertEquals(PtipoPropiedadDAO.eliminarPtipoPropiedad(new PtipoPropiedad()),false);
	}
	
	@Test
	public void eliminarTotalPtipoPropiedadTest(){
		assertNotNull(PtipoPropiedadDAO.eliminarTotalPtipoPropiedad(new PtipoPropiedad()));
	}
}
