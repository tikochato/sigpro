package dao;
import static org.junit.Assert.*;
import org.junit.Test;


public class ProdTipoPropiedadDAOTest {

	
	@Test
	public void getProdtipoPropiedadTest(){
		assertEquals(ProdTipoPropiedadDAO.getProdtipoPropiedad(0, 0),null);
	}
	
	@Test
	public void guardarTest(){
		assertEquals(ProdTipoPropiedadDAO.guardar(0, 9, "admin"),false);
	}
	
	@Test
	public void actualizarTest(){
		assertEquals(ProdTipoPropiedadDAO.actualizar(null, null, null, null, null),false);
	}
	
	@Test
	public void eliminarTest(){
		assertEquals(ProdTipoPropiedadDAO.eliminar(null, null, null),false);
	}
	
	@Test
	public void getPaginaTest(){
		assertNotNull(ProdTipoPropiedadDAO.getPagina(1, 1, 1));
	}
	
	@Test
	public void getJsonTest(){
		assertEquals(ProdTipoPropiedadDAO.getJson(1).getClass(),String.class);
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(ProdTipoPropiedadDAO.getTotal().getClass(),Long.class);
	}
	
	@Test
	public void getTipoPropiedades(){
		assertNotNull(ProdTipoPropiedadDAO.getTipoPropiedades(1));
	}
	
	@Test
	public void getJsonTest1(){
		assertNotNull(ProdTipoPropiedadDAO.getJson(1, 1, 1));
	}
	
	@Test
	public void persistirPropiedadesTest(){
		assertEquals(ProdTipoPropiedadDAO.persistirPropiedades(1, "[{idTipo:0,tipo:'s', idPropiedad:0, propiedad:'s', idPropiedadTipo:0, propiedadTipo:'s', estado:'s'}]","a"),false);
	}
}
