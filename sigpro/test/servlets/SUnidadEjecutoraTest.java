package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SUnidadEjecutoraTest {

	String direccionServlet = "http://localhost:8080/SUnidadEjecutora";
	@Test
	public void cargarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar\", \"pagina\":\"0\", \"registros\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void crearTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"crear\", \"codigo\":\"0\", \"nombre\":\"\", \"entidad\":\"0\"}");
		assertEquals(respuesta, "");		
	}
	
	@Test
	public void actualizarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"actualizar\", \"codigo\":\"0\", \"nombre\":\"unit_test\", \"entidad\":\"0\"}");
		assertEquals(respuesta, "");		
	}
	
	@Test
	public void totalElementosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"totalElementos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
}
