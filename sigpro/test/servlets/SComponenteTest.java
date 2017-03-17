package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SComponenteTest {
	String direccionServlet = "http://localhost:8080/SComponente";
	@Test
	public void getComponentesPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentesPagina\" ,\"pagina\":\"1\", \"numerocomponentes\":\"1\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getComponentesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentes\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
	
	@Test
	public void guardarComponenteTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarComponente\" , \"esnuevo\":\"false\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");
	}
	
	@Test
	public void borrarComponenteTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarComponente\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");
	}
	
	@Test
	public void numeroComponentesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroComponentes\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
	
	@Test
	public void numeroComponentesPorProyectoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroComponentesPorProyecto\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
	
	@Test
	public void getComponentesPaginaPorProyectoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentesPaginaPorProyecto\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
	
	@Test
	public void obtenerComponentePorIdTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getComponentesPaginaPorProyecto\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
	
	
}
