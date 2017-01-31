package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SProyectoPropiedadTest {
	String direccionServlet = "http://localhost:8080/SProyectoPropiedad";
	@Test
	public void getProyectoPropiedadPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectoPropiedadPagina\", \"pagina\":\"0\", \"idProyectoTipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void getProyectoPropiedadesTotalDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectoPropiedadesTotalDisponibles\", \"pagina\":\"0\", \"idspropiedades\":\"0\", \"numeroproyectopropiedad\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void getProyectoPropiedadPorTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getProyectoPropiedadPorTipo\", \"idProyecto\":\"0\", \"idProyectoTipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void numeroProyectoPropiedadesDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroProyectoPropiedadesDisponibles\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}

}
