package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.SubcomponentePropiedad;
public class SubComponentePropiedadDAOTest {
	
	@Test
	public void getSubComponentePropiedadesPorTipoSubComponentePaginaTest(){
		assertNotNull(SubComponentePropiedadDAO.getSubComponentePropiedadesPorTipoSubComponentePagina(1, 1));
	}
	
	@Test
	public void getTotalSubComponentePropiedadesTest(){
		assertEquals(SubComponentePropiedadDAO.getTotalSubComponentePropiedades().getClass(),Long.class);
	}
	
	@Test
	public void getSubComponentePropiedadPaginaTotalDisponiblesTest(){
		assertNotNull(SubComponentePropiedadDAO.getSubComponentePropiedadPaginaTotalDisponibles(1, 1,"unit_test"));
	}
	
	@Test
	public void getSubComponentePropiedadPorIdTest(){
		assertNull(SubComponentePropiedadDAO.getSubComponentePropiedadPorId(0));
	}
	
	@Test
	public void guardarSubComponentePropiedadTest(){
		assertEquals(SubComponentePropiedadDAO.guardarSubComponentePropiedad(new SubcomponentePropiedad()), false);
	}
	
	@Test
	public void eliminarSubComponentePropiedadTest(){
		assertEquals(SubComponentePropiedadDAO.eliminarSubComponentePropiedad(new SubcomponentePropiedad()),false);
	}
	
	@Test
	public void eliminarTotalSubComponentePropiedadTest(){
		assertNotNull(SubComponentePropiedadDAO.eliminarTotalSubComponentePropiedad(new SubcomponentePropiedad()));
	}
	
	@Test
	public void getSubComponentePropiedadesPaginaTest(){
		assertNotNull(SubComponentePropiedadDAO.getSubComponentePropiedadesPagina(1, 1, "","","","",""));
	}
	
	@Test
	public void getTotalSubComponentePropiedad(){
		assertEquals(SubComponentePropiedadDAO.getTotalSubComponentePropiedad("","","").getClass(),Long.class);
	}

}
