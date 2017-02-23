package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.ComponentePropiedad;
public class ComponentePropiedadDAOTest {
	
	@Test
	public void getComponentePropiedadesPorTipoComponentePaginaTest(){
		assertNotNull(ComponentePropiedadDAO.getComponentePropiedadesPorTipoComponentePagina(1, 1));
	}
	
	@Test
	public void getTotalComponentePropiedadesTest(){
		assertEquals(ComponentePropiedadDAO.getTotalComponentePropiedades().getClass(),Long.class);
	}
	
	@Test
	public void getComponentePropiedadPaginaTotalDisponiblesTest(){
		assertNotNull(ComponentePropiedadDAO.getComponentePropiedadPaginaTotalDisponibles(1, 1,"unit_test"));
	}
	
	@Test
	public void getComponentePropiedadPorIdTest(){
		assertNull(ComponentePropiedadDAO.getComponentePropiedadPorId(0));
	}
	
	@Test
	public void guardarComponentePropiedadTest(){
		assertEquals(ComponentePropiedadDAO.guardarComponentePropiedad(new ComponentePropiedad()), false);
	}
	
	@Test
	public void eliminarComponentePropiedadTest(){
		assertEquals(ComponentePropiedadDAO.eliminarComponentePropiedad(new ComponentePropiedad()),false);
	}
	
	@Test
	public void eliminarTotalComponentePropiedadTest(){
		assertNotNull(ComponentePropiedadDAO.eliminarTotalComponentePropiedad(new ComponentePropiedad()));
	}
	
	@Test
	public void getComponentePropiedadesPaginaTest(){
		assertNotNull(ComponentePropiedadDAO.getComponentePropiedadesPagina(1, 1, "","","","",""));
	}
	
	@Test
	public void getTotalComponentePropiedad(){
		assertEquals(ComponentePropiedadDAO.getTotalComponentePropiedad("","","").getClass(),Long.class);
	}

}
