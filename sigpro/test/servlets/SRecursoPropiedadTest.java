package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SRecursoPropiedadTest {
	String direccionServlet = "http://localhost:8080/SRecursoPropiedad";
	@Test
	public void getRecursoPropiedadPaginaPorTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRecursoPropiedadPaginaPorTipo\", \"pagina\":\"0\", \"idRecursoTipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void getRecursoPropiedadPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRecursoPropiedadPagina\", \"pagina\":\"0\", \"numerorecursopropiedad\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void getRecursoPropiedadesTotalDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRecursoPropiedadesTotalDisponibles\", \"pagina\":\"0\", \"idspropiedades\":\"0\", \"numerorecursopropiedad\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void numeroRecursoPropiedadesDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRecursoPropiedadesDisponibles\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void numeroRecursoPropiedades(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRecursoPropiedades\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void guardarRecursoPropiedadTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarRecursoPropiedad\", \"esnuevo\":\"false\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
	
	@Test
	public void borrarRecursoPropiedadTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarRecursoPropiedad\",\"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
}
