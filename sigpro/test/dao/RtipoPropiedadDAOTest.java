package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.RtipoPropiedad;
public class RtipoPropiedadDAOTest {
	
	@Test
	public void eliminarRtipoPropiedadTest(){
		assertEquals(RtipoPropiedadDAO.eliminarRtipoPropiedad(new RtipoPropiedad()),false);
	}
	
	@Test
	public void eliminarTotalRtipoPropiedadTest(){
		assertNotNull(RtipoPropiedadDAO.eliminarTotalRtipoPropiedad(new RtipoPropiedad()));
	}


}
