package dao;

import static org.junit.Assert.*;

import org.junit.Test;
import pojo.EstadoTabla;
import pojo.EstadoTablaId;

public class EstadoTablaDAOTest {

	@Test
	public void testSaveEstadoTabla() {
		EstadoTablaId estadotablaid = new EstadoTablaId("admin","unit_test");
		EstadoTabla estadotabla = new EstadoTabla();
		estadotabla.setId(estadotablaid);
		estadotabla.setValores("unit_test");
		assertEquals(EstadoTablaDAO.saveEstadoTabla(estadotabla), true );
	}

	@Test
	public void testGetStadoTabla() {
		String resultado = EstadoTablaDAO.getStadoTabla("admin", "unit_test");
		assertEquals(resultado,"unit_test");
	}

}
