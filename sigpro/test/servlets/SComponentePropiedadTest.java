package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SComponentePropiedadTest {
	String direccionServlet = "http://localhost:8080/SComponentePropiedad";
	@Test
	public void getComponentePropiedadPaginaPorTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentePropiedadPaginaPorTipo\" ,\"pagina\":\"0\", \"idComponenteTipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getComponentePropiedadPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentePropiedadPagina\" ,\"pagina\":\"0\", \"numerocomponentepropiedad\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	@Test
	public void getComponentePropiedadesTotalDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentePropiedadesTotalDisponibles\" ,\"pagina\":\"0\", \"idspropiedades\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	@Test
	public void numeroComponentePropiedadesDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroComponentePropiedadesDisponibles\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	@Test
	public void numeroComponentePropiedadesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroComponentePropiedades\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarComponentePropiedadTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarComponentePropiedad\", \"esnuevo\":\"false\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	@Test
	public void borrarComponentePropiedadTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarComponentePropiedad\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	
}
