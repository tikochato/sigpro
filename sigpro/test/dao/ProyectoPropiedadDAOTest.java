package dao;



import static org.junit.Assert.*;

import org.junit.Test;
public class ProyectoPropiedadDAOTest {
	
	@Test
	public void getProyectoPropiedadesPorTipoProyectoPaginaTest(){
		assertNotNull(ProyectoPropiedadDAO.getProyectoPropiedadesPorTipoProyectoPagina(0, 0));
	}
	
	@Test
	public void getTotalProyectoPropiedadesTest(){
		assertEquals(ProyectoPropiedadDAO.getTotalProyectoPropiedades().getClass(),Long.class);
	}
	
	@Test
	public void getProyectoPropiedadPaginaTotalDisponiblesTest(){
		assertNotNull(ProyectoPropiedadDAO.getProyectoPropiedadPaginaTotalDisponibles(0, 0, "{}"));
	}


}
