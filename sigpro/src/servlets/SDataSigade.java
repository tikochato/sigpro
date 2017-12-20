package servlets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import pojoSigade.DtmAvanceFisfinanDetDti;
import pojoSigade.DtmAvanceFisfinanDti;
import utilities.Utils;

@WebServlet("/SDataSigade")
public class SDataSigade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stcodigopresupuestario{
		String codigopresupuestario;
		String numeroprestamo;
	}
	
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
		String objetivo;
	}
	
	class stdesembolsos{
		Long ejercicioFiscal;
		Integer mesDesembolso;
		String codigoPresupuestario;
		Integer entidadSicoin;
		Integer unidadEjecutoraSicoin;
		String monedaDesembolso;
		BigDecimal mesDesembolsoMoneda;
		BigDecimal tipoCambioUSD;
		BigDecimal mesDesembolsoMonedaUSD;
		BigDecimal tipoCambioGTQ;
		BigDecimal mesDesembolsoMonedaGTQ;
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
			String codigoPresupuestario = map.get("codigoPresupuestario");
			DtmAvanceFisfinanDti inf = DataSigadeDAO.getavanceFisFinanDMS1(codigoPresupuestario);
			
			stprestamo temp = new stprestamo();
			if (inf !=null){
			
			temp.codigoPresupuestario = inf.getId().getCodigoPresupuestario();
			temp.numeroPrestamo = inf.getId().getNoPrestamo();
			temp.proyectoPrograma = inf.getId().getNombrePrograma();
			Cooperante cooperante = CooperanteDAO.getCooperantePorCodigo(inf.getId().getCodigoOrganismoFinan());
			if(cooperante!=null){
				temp.cooperanteid = cooperante.getId();
				temp.cooperantenombre = cooperante.getNombre();
			}
			
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
			temp.objetivo = inf.getId().getObjetivo();
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

			}else{
				
		        response_text =  "{\"success\":false}";
			}
		}else if (accion.equals("getcodigos")) {
			List<DtmAvanceFisfinanDti> prestamos = DataSigadeDAO.getCodigos();
			List<stcodigopresupuestario> codigos = new ArrayList<>();
			for (DtmAvanceFisfinanDti prestamo : prestamos){
				stcodigopresupuestario temp = new stcodigopresupuestario();
				temp.codigopresupuestario = prestamo.getId().getCodigoPresupuestario();
				temp.numeroprestamo = prestamo.getId().getNoPrestamo();
				codigos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(codigos);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("getDesembolsosUE")){
			String codPrep = map.get("codPrep");
			Integer ejercicio = Utils.String2Int(map.get("ejercicio"));
			Integer entidad = Utils.String2Int(map.get("entidad"));
			Integer ue = Utils.String2Int(map.get("ue"));
			List<DtmAvanceFisfinanDetDti> lstDesembolsos = DataSigadeDAO.getInfPorUnidadEjecutora(codPrep,ejercicio,entidad, ue);
			List<stdesembolsos> lstDesembolsosUE = new ArrayList<stdesembolsos>();
			for(DtmAvanceFisfinanDetDti desembolso : lstDesembolsos){
				stdesembolsos temp = new stdesembolsos();
				temp.codigoPresupuestario = desembolso.getId().getCodigoPresupuestario();
				temp.ejercicioFiscal = desembolso.getId().getEjercicioFiscal();
				temp.entidadSicoin = desembolso.getId().getEntidadSicoin();
				temp.mesDesembolso = Utils.String2Int(desembolso.getId().getMesDesembolso());
				temp.mesDesembolsoMoneda = desembolso.getId().getDesembolsosMesMoneda();
				temp.mesDesembolsoMonedaGTQ = desembolso.getId().getDesembolsosMesGtq();
				temp.mesDesembolsoMonedaUSD = desembolso.getId().getDesembolsosMesUsd();
				temp.monedaDesembolso = desembolso.getId().getMonedaDesembolso();
				temp.tipoCambioUSD = desembolso.getId().getTcMonUsd();
				temp.tipoCambioGTQ = desembolso.getId().getTcUsdGtq();
				
				lstDesembolsosUE.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstDesembolsosUE);
	        response_text = String.join("", "\"desembolsosUE\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("getMontoDesembolsosUE")){
			String codPrep = map.get("codPrep");
			Integer ejercicio = Utils.String2Int(map.get("ejercicio"));
			Integer entidad = Utils.String2Int(map.get("entidad"));
			Integer ue = Utils.String2Int(map.get("ue"));
			List<DtmAvanceFisfinanDetDti> lstDesembolsos = DataSigadeDAO.getInfPorUnidadEjecutora(codPrep,ejercicio,entidad, ue);
			
			BigDecimal montoDesembolsado = new BigDecimal(0);
			
			for(DtmAvanceFisfinanDetDti desembolso : lstDesembolsos){
				montoDesembolsado = montoDesembolsado.add(desembolso.getId().getDesembolsosMesUsd());				
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(montoDesembolsado);
	        response_text = String.join("", "\"montoDesembolsadoUE\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("getMontoDesembolsosUEALaFecha")){
			String codPrep = map.get("codPrep");
			Integer entidad = Utils.String2Int(map.get("entidad"));
//			Integer ue = Utils.String2Int(map.get("ue"));
			List<DtmAvanceFisfinanDetDti> lstDesembolsos = DataSigadeDAO.getInfPorUnidadEjecutoraALaFecha(codPrep, entidad);
			
			BigDecimal montoDesembolsado = new BigDecimal(0);
			
			for(DtmAvanceFisfinanDetDti desembolso : lstDesembolsos){
				montoDesembolsado = montoDesembolsado.add(desembolso.getId().getDesembolsosMesUsd());				
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(montoDesembolsado);
	        response_text = String.join("", "\"montoDesembolsadoUEALaFecha\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else{
			
	        response_text =  "{\"success\":false}";
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
