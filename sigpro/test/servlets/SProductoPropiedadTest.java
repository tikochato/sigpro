package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SProductoPropiedadTest {
	String direccionServlet = "http://localhost:8080/SProductoPropiedad";
	@Test
	public void cargarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar\", \"pagina\":\"1\", \"registros\":\"25\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void crearTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"crear\", \"nombre\":\"\", \"descripcion\":\"\", \"descripcion\":\"\",  \"usuario\":\"\" }");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void actualizarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"actualizar\", \"codigo\":\"0\", \"nombre\":\"\", \"descripcion\":\"\", \"usuario\":\"\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void borrarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrar\", \"usuario\":\"\", \"codigo\":\"0\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void totalElementosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"totalElementos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}

}
