package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SRecursoUnidadMedidaTest {
	
	String direccionServlet = "http://localhost:8080/SRecursoUnidadMedida";
	@Test
	public void getRecursoPropiedadPaginaPorTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRecursoUnidadMedidasPagina\", \"pagina\":\"0\", \"numerorecursounidadmedidas\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void getRecursoUnidadMedidasTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRecursoUnidadMedidas\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	

	@Test
	public void guardarRecursoUnidadMedidaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarRecursoUnidadMedida\", \"esnueva\":\"false\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
	
	@Test
	public void borrarRecursoUnidadMedidaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarRecursoUnidadMedida\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
	
	@Test
	public void numeroRecursoUnidadMedidasTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRecursoUnidadMedidas\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}

}
