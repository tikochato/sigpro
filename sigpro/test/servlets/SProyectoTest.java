package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SProyectoTest {
	String direccionServlet = "http://localhost:8080/SProyecto";
	@Test
	public void getProyectoPaginTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectoPagin\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardar\" , \"esnuevo\":\"false\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void cargar_cooperantesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar_cooperantes\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void cargar_proyectotiposTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar_proyectotipos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	
	@Test
	public void cargar_unidadesejecturoasTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar_unidadesejecturoasTest\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void borrarProyectoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarProyecto\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void obtenerProyectoPorIdTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"obtenerProyectoPorId\", \"id\":\"0\"}");
		
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
}
