package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.PrestamoDAO;
import pojo.Prestamo;
import utilities.Utils;


@WebServlet("/SPrestamo")
public class SPrestamo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stprestamo{
		Date fechaCorte;
		int codigoPresupuestario;
		String numeroPrestamo; 
		String destino;
		String sectorEconomico;
		int unidadEjecutora; 
		Date fechaFirma;
		int tipoAutorizacionId;
		String numeroAutorizacion;
		Date fechaAutorizacion;
		int aniosPlazo;
		int aniosGracia;
		Date fechaFinEjecucion;
		int peridoEjecucion;
		int tipoInteresId;
		BigDecimal porcentajeInteres; 
		BigDecimal porcentajeComisionCompra;
		int tipoMonedaId;
		BigDecimal montoContratado;
		BigDecimal amortizado;
		BigDecimal porAmortizar;
		BigDecimal principalAnio;
		BigDecimal interesesAnio;
		BigDecimal comisionCompromisoAnio;
		BigDecimal otrosGastos;
		BigDecimal principalAcumulado;
		BigDecimal interesesAcumulados;
		BigDecimal comisionCompromisoAcumulado;
		BigDecimal otrosCargosAcumulados;
		BigDecimal presupuestoAsignadoFuncionamiento;
		BigDecimal prespupuestoAsignadoInversion;
		BigDecimal presupuestoModificadoFun;
		BigDecimal presupuestoModificadoInv;
		BigDecimal presupuestoVigenteFun;
		BigDecimal presupuestoVigenteInv;
		BigDecimal prespupuestoDevengadoFun;
		BigDecimal presupuestoDevengadoInv;
		BigDecimal presupuestoPagadoFun;
		BigDecimal presupuestoPagadoInv;
		BigDecimal saldoCuentas;
		BigDecimal desembolsadoReal;
		int ejecucionEstadoId;
		String  proyectoPrograma;
		Date fechaDecreto;
		Date fechaSuscripcion;
		Date fechaElegibilidadUe;
		Date fechaCierreOrigianlUe;
		Date fechaCierreActualUe;
		int mesesProrrogaUe;
		int plazoEjecucionUe;  
		BigDecimal montoAsignadoUe;
		BigDecimal desembolsoAFechaUe;
		BigDecimal montoPorDesembolsarUe;
		
	}
       
    
    public SPrestamo() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";

		if (accion.equals("getPrestamo")) {
			int objetoId = Utils.String2Int(map.get("objetoId"));
			int objetoTipo = Utils.String2Int(map.get("objetoTipo"));
			
			
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(objetoId, objetoTipo);
			//stprestamo temp =  new stprestamo();
			
		

			response_text=new GsonBuilder().serializeNulls().create().toJson(prestamo);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
			
		}
	}

}
