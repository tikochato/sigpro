package servlets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import java.util.Map;
import java.util.zip.GZIPOutputStream;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CooperanteDAO;
import dao.DataSigadeDAO;
import dao.TipoMonedaDAO;
import pojo.Cooperante;
import pojo.TipoMoneda;
import pojoSigade.DtmAvanceFisfinanDti;
import utilities.Utils;

@WebServlet("/SDataSigade")
public class SDataSigade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stprestamo{
		String fechaCorte;
		String codigoPresupuestario;
		String numeroPrestamo; 
		String destino;
		String sectorEconomico;
		int unidadEjecutora; 
		String unidadEjecutoraNombre;
		String fechaFirma;
		Integer tipoAutorizacionId;
		String tipoAutorizacionNombre;
		String numeroAutorizacion;
		String fechaAutorizacion;
		Integer aniosPlazo;
		Integer aniosGracia;
		String fechaFinEjecucion;
		Integer periodoEjecucion;
		Integer tipoInteresId;
		String tipoInteresNombre;
		BigDecimal porcentajeInteres; 
		BigDecimal porcentajeComisionCompra;
		int tipoMonedaId;
		String tipoMonedaNombre;
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
		BigDecimal presupuestoAsignadoInversion;
		BigDecimal presupuestoModificadoFun;
		BigDecimal presupuestoModificadoInv;
		BigDecimal presupuestoVigenteFun;
		BigDecimal presupuestoVigenteInv;
		BigDecimal presupuestoDevengadoFun;
		BigDecimal presupuestoDevengadoInv;
		BigDecimal presupuestoPagadoFun;
		BigDecimal presupuestoPagadoInv;
		BigDecimal saldoCuentas;
		BigDecimal desembolsoReal;
		Integer ejecucionEstadoId;
		String ejecucionEstadoNombre;
		String  proyectoPrograma;
		String fechaDecreto;
		String fechaSuscripcion;
		String fechaElegibilidadUe;
		String fechaCierreOrigianlUe;
		String fechaCierreActualUe;
		int mesesProrrogaUe;
		int plazoEjecucionUe;  
		BigDecimal montoAsignadoUe;
		BigDecimal desembolsoAFechaUe;
		BigDecimal montoPorDesembolsarUe;
		String fechaVigencia;
		BigDecimal montoContratadoUsd;
		BigDecimal montoContratadoQtz;
		BigDecimal desembolsoAFechaUsd;
		BigDecimal montoPorDesembolsarUsd;
		BigDecimal montoAsignadoUeUsd;
		BigDecimal montoAsignadoUeQtz;
		BigDecimal desembolsoAFechaUeUsd;
		BigDecimal montoPorDesembolsarUeUsd;
		int cooperanteid;
		String cooperantenombre;
	}
       
    public SDataSigade() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
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

		if (accion.equals("getdatos")) {
			String noPrestamo = map.get("noPrestamo");
			String codigoPresupuestario = map.get("codigoPresupuestario");
			DtmAvanceFisfinanDti inf = DataSigadeDAO.getavanceFisFinanDMS1(noPrestamo, codigoPresupuestario);
			
			stprestamo temp = new stprestamo();
			if (inf !=null){
			
			temp.codigoPresupuestario = inf.getId().getCodigoPresupuestario();
			temp.numeroPrestamo = inf.getId().getNoPrestamo();
			temp.proyectoPrograma = inf.getId().getNombrePrograma();
			Cooperante cooperante = CooperanteDAO.getCooperantePorCodigo(inf.getId().getCodigoOrganismoFinan());
			temp.cooperanteid = cooperante.getId();
			temp.cooperantenombre = cooperante.getNombre();
			
			temp.fechaDecreto = Utils.formatDate(inf.getId().getFechaDecreto());
			temp.fechaSuscripcion = Utils.formatDate(inf.getId().getFechaSuscripcion());
			temp.fechaVigencia = Utils.formatDate(inf.getId().getFechaVigencia());
			TipoMoneda moneda = TipoMonedaDAO.getTipoMonedaPorSimbolo(inf.getId().getMonedaPrestamo());
			temp.tipoMonedaNombre = String.join("",moneda.getNombre()," (" + moneda.getSimbolo() + ")");
			temp.tipoMonedaId = moneda.getId();
			temp.montoContratado = inf.getId().getMontoContratado();
			temp.montoContratadoUsd = inf.getId().getMontoContratadoUsd();
			temp.montoContratadoQtz = inf.getId().getMontoContratadoGtq();
			temp.desembolsoAFechaUsd = inf.getId().getDesembolsosUsd();
			temp.montoPorDesembolsarUsd = inf.getId().getPorDesembolsarUsd();
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

			}else{
				
		        response_text =  "{\"success\":false}";
			}
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
