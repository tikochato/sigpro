package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;

public class SEstadoTablaTest {
	String direccionServlet = "http://localhost:8080/SEstadoTabla";
	@Test
	public void getDesembolsosPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"action\":\"guardaEstado\" ,\"grid\":\"\", \"estado\":\"0\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void getEstadoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"action\":\"getEstado\" ,\"grid\":\"\", \"usuario\":\"\"}");
		assertEquals(respuesta, "");	
		
	}

}
