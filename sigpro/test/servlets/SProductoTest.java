package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SProductoTest {
	String direccionServlet = "http://localhost:8080/SProducto";
	@Test
	public void cargarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar\", \"pagina\":\"1\", \"registros\":\"25\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void crearTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"crear\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void actualizarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"actualizar\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void eliminarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"eliminar\"}");
		assertEquals(respuesta, "");	
	}
	
	/*@Test
	public void listarTiposTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"listarTipos\"}");
		assertEquals(respuesta, "");	
	}*/
	
	@Test
	public void totalElementosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"totalElementos\",  \"pagina\":\"1\", \"registros\":\"25\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void listarProductosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"listarProductos\",  \"pagina\":\"1\", \"registros\":\"25\"}");
		assertEquals(respuesta, "");	
	}
}
