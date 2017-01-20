package dao;
import static org.junit.Assert.*;

import org.junit.Test;
import pojo.CtipoPropiedad;

public class CTipoPropiedadDAOTest {
	
	@Test
	public void eliminarCtipoPropiedadTest(){
		assertEquals(CtipoPropiedadDAO.eliminarCtipoPropiedad(new CtipoPropiedad()), false);
	}
	
	@Test
	public void eliminarTotalCtipoPropiedad(){
		assertNotNull(CtipoPropiedadDAO.eliminarTotalCtipoPropiedad(new CtipoPropiedad()));
	}
}
