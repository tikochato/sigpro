package dao;

import static org.junit.Assert.*;

import org.junit.Test;
import pojo.EstadoTabla;

public class EstadoTablaDAOTest {

	@Test
	public void testSaveEstadoTabla() {
		assertEquals(EstadoTablaDAO.saveEstadoTabla(new EstadoTabla()), false );
	}

	@Test
	public void testGetStadoTabla() {
		assertNotNull(EstadoTablaDAO.getStadoTabla("admin", "unit_test"));
	}

}
