package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SMetaTipoTest {
	String direccionServlet = "http://localhost:8080/SMetaTipo";
	@Test
	public void getMetaTiposPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getMetaTiposPagina\" ,\"pagina\":\"0\", \"numerometatipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getMetaTiposTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getMetaTipos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarMetaTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarMetaTipo\" , \"esnueva\":\"false\" , \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void borrarMetaTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarMetaTipo\" , \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void numeroMetaTiposTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroMetaTipos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	

}
