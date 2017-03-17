package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SProyectoTest {
	String direccionServlet = "http://localhost:8080/SProyecto";
	@Test
	public void getProyectoPaginTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectoPagina\", \"filtro_nombre\":\"test\", \"numeroproyecto\":\"0\", \"pagina\":\"0\", \"filtro_usuario_creo\":\"test\", \"filtro_fecha_creacion\":\"test\", \"columna_ordenada\":\"test\", \"orden_direccion\":\"test\"  }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardar\" , \"esnuevo\":\"false\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void getProyectosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getProyectoPaginaDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectoPaginaDisponibles\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	
	@Test
	public void numeroProyectosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroProyectos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
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
	
	@Test
	public void numeroProyectosDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroProyectosDisponibles\", \"id\":\"0\"}");
		
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
}
