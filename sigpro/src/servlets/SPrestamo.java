package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import dao.AcumulacionCostoDAO;
import dao.ComponenteDAO;
import dao.ComponenteSigadeDAO;
import dao.ComponenteTipoDAO;
import dao.DataSigadeDAO;
import dao.LineaBaseDAO;
import dao.ObjetoDAO;
import dao.PrestamoDAO;
import dao.PrestamoTipoDAO;
import dao.ProyectoDAO;
import dao.ProyectoTipoDAO;
import dao.UnidadEjecutoraDAO;
import pojo.AcumulacionCosto;
import pojo.AutorizacionTipo;
import pojo.Componente;
import pojo.ComponenteSigade;
import pojo.ComponenteTipo;
import pojo.Cooperante;
import pojo.EjecucionEstado;
import pojo.Etiqueta;
import pojo.InteresTipo;
import pojo.LineaBase;
import pojo.Prestamo;
import pojo.PrestamoTipoPrestamo;
import pojo.Proyecto;
import pojo.ProyectoTipo;
import pojo.TipoMoneda;
import pojo.UnidadEjecutora;
import utilities.Utils;


@WebServlet("/SPrestamo")
public class SPrestamo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stprestamo{
		Integer id;
		String fechaCorte;
		Long codigoPresupuestario;
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
		String nombreEntidadEjecutora;
		int cooperanteid;
		String cooperantenombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		String objetivo;
		String objetivoEspecifico;
		BigDecimal montoContratadoEntidadUsd;
		BigDecimal desembolsadoAFecha;
		Double plazoEjecucionPEP;
		Integer ejecucionFisicaRealPEP;
		Integer porcentajeAvance;
	}
	
	class sttiposprestamo{
		Integer id;
		String nombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
	}
       
    public class stcomponentessigade{
    	public Integer id;
    	public String nombre;
    	public String tipoMoneda;
    	public BigDecimal techo;
    	public int orden;
    	public String descripcion;
    	public List<stunidadejecutora> unidadesEjecutoras;
    	public long fechaActualizacion;
    }
    
    public class stunidadejecutora{
    	public Integer id;
    	public String nombre;
    	public String entidad;
    	public Integer entidadId;
    	public Integer ejercicio;
    	public Double prestamo;
    	public Double donacion;
    	public Double nacional;
    	public int esCoordinador;
    	public String fechaElegibilidad;
    	public String fechaCierre;
    	public BigDecimal techo;
    	public Integer prestamoId;
    	public Integer componenteId;
    	public Integer componenteSigadeId;
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

		if (accion.equals("getPrestamos")) {
			try{
				List<Prestamo> prestamos = PrestamoDAO.getPrestamos(usuario);
				if (prestamos !=null && prestamos.size() > 0){
					List<stprestamo> lstprestamo = new ArrayList<stprestamo>();
					
					for(Prestamo prestamo : prestamos){
						stprestamo temp =  new stprestamo();
						
						temp.id = prestamo.getId();
						temp.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
						temp.codigoPresupuestario = prestamo.getCodigoPresupuestario();
						temp.numeroPrestamo = prestamo.getNumeroPrestamo(); 
						temp.destino = prestamo.getDestino();
						temp.sectorEconomico = prestamo.getSectorEconomico();
						temp.fechaFirma = (prestamo.getFechaFirma() == null ? null : Utils.formatDate(prestamo.getFechaFirma()));
						temp.tipoAutorizacionId = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getId());
						temp.tipoAutorizacionNombre = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getNombre());
						temp.numeroAutorizacion = (prestamo.getNumeroAutorizacion() == null ? null: prestamo.getNumeroAutorizacion());
						temp.fechaAutorizacion = prestamo.getFechaAutorizacion() == null ? null : Utils.formatDate(prestamo.getFechaAutorizacion());
						temp.aniosPlazo = (prestamo.getAniosPlazo() == null ? null : prestamo.getAniosPlazo()); 
						temp.aniosGracia = (prestamo.getAniosGracia() == null ? null : prestamo.getAniosGracia());  
						temp.fechaFinEjecucion = prestamo.getFechaFinEjecucion() == null ? null : Utils.formatDate(prestamo.getFechaFinEjecucion());
						temp.periodoEjecucion = (prestamo.getPeridoEjecucion() == null ? null :prestamo.getPeridoEjecucion()); 
						temp.tipoInteresId = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getId());
						temp.tipoInteresNombre = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getNombre());
						temp.porcentajeInteres = prestamo.getPorcentajeInteres(); 
						temp.porcentajeComisionCompra = prestamo.getPorcentajeComisionCompra();
						temp.tipoMonedaId = prestamo.getTipoMoneda().getId();
						temp.tipoMonedaNombre = prestamo.getTipoMoneda().getNombre();
						temp.montoContratado = prestamo.getMontoContratado();
						temp.amortizado = prestamo.getAmortizado();
						temp.porAmortizar = prestamo.getPorAmortizar();
						temp.principalAnio = prestamo.getPrincipalAnio();
						temp.interesesAnio = prestamo.getInteresesAnio();
						temp.comisionCompromisoAnio = prestamo.getComisionCompromisoAnio();
						temp.otrosGastos = prestamo.getOtrosGastos();
						temp.principalAcumulado = prestamo.getPrincipalAcumulado();
						temp.interesesAcumulados = prestamo.getInteresesAcumulados();
						temp.comisionCompromisoAcumulado = prestamo.getComisionCompromisoAcumulado();
						temp.otrosCargosAcumulados = prestamo.getOtrosCargosAcumulados();
						temp.presupuestoAsignadoFuncionamiento = prestamo.getPresupuestoAsignadoFuncionamiento();
						temp.presupuestoAsignadoInversion = prestamo.getPrespupuestoAsignadoInversion();
						temp.presupuestoModificadoFun = prestamo.getPresupuestoModificadoFuncionamiento();
						temp.presupuestoModificadoInv = prestamo.getPresupuestoModificadoInversion();
						temp.presupuestoVigenteFun = prestamo.getPresupuestoVigenteFuncionamiento();
						temp.presupuestoVigenteInv = prestamo.getPresupuestoVigenteInversion();
						temp.presupuestoDevengadoFun = prestamo.getPrespupuestoDevengadoFuncionamiento();
						temp.presupuestoDevengadoInv = prestamo.getPresupuestoDevengadoInversion();
						temp.presupuestoPagadoFun = prestamo.getPresupuestoPagadoFuncionamiento();
						temp.presupuestoPagadoInv = prestamo.getPresupuestoPagadoInversion();
						temp.saldoCuentas = prestamo.getSaldoCuentas();
						temp.desembolsoReal = prestamo.getSaldoCuentas();
						temp.ejecucionEstadoId = (prestamo.getEjecucionEstado() == null ? null :prestamo.getEjecucionEstado().getId()); 
						temp.ejecucionEstadoNombre = (prestamo.getEjecucionEstado() == null ? null : prestamo.getEjecucionEstado().getNombre());
						temp.proyectoPrograma = prestamo.getProyectoPrograma();
						temp.fechaDecreto = Utils.formatDate(prestamo.getFechaDecreto());
						temp.fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
						temp.fechaElegibilidadUe = Utils.formatDate(prestamo.getFechaElegibilidadUe());
						temp.fechaCierreOrigianlUe = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
						temp.fechaCierreActualUe = Utils.formatDate(prestamo.getFechaCierreActualUe());
						temp.mesesProrrogaUe = prestamo.getMesesProrrogaUe();
						temp.montoAsignadoUe = prestamo.getMontoAsignadoUe();
						temp.desembolsoAFechaUe = prestamo.getDesembolsoAFechaUe();
						temp.montoPorDesembolsarUe = prestamo.getMontoPorDesembolsarUe();
						temp.fechaVigencia = Utils.formatDate(prestamo.getFechaVigencia());
						temp.montoContratadoUsd = prestamo.getMontoContratadoUsd();
						temp.montoContratadoQtz = prestamo.getMontoContratadoQtz();
						temp.desembolsoAFechaUsd = prestamo.getDesembolsoAFechaUsd();
						temp.montoPorDesembolsarUsd = prestamo.getMontoPorDesembolsarUsd();
						temp.montoAsignadoUeUsd = prestamo.getMontoAsignadoUeUsd();
						temp.montoAsignadoUeQtz = prestamo.getMontoAsignadoUeQtz();
						temp.desembolsoAFechaUeUsd = prestamo.getDesembolsoAFechaUeUsd();
						temp.montoPorDesembolsarUeUsd = prestamo.getMontoPorDesembolsarUeUsd();
						temp.cooperanteid = prestamo.getCooperante().getId();
						temp.cooperantenombre =  (prestamo.getCooperante().getSiglas()!=null ? 
								prestamo.getCooperante().getSiglas() + " - " : "") + prestamo.getCooperante().getNombre();
						
						if (prestamo.getUnidadEjecutora()!=null){
							temp.unidadEjecutora = prestamo.getUnidadEjecutora().getId().getUnidadEjecutora();
							temp.unidadEjecutoraNombre = prestamo.getUnidadEjecutora().getNombre();
							temp.nombreEntidadEjecutora = prestamo.getUnidadEjecutora().getEntidad().getNombre();
						}
						
						temp.usuarioCreo = prestamo.getUsuarioCreo();
						temp.usuarioActualizo = prestamo.getUsuarioActualizo();
						temp.fechaCreacion = Utils.formatDateHour(prestamo.getFechaCreacion());
						temp.fechaActualizacion = Utils.formatDateHour(prestamo.getFechaActualizacion());
						temp.objetivo = prestamo.getObjetivo();
						temp.objetivoEspecifico = prestamo.getObjetivoEspecifico();
						temp.porcentajeAvance = prestamo.getPorcentajeAvance();
						lstprestamo.add(temp);
					}
					
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			        response_text = String.join("", "\"prestamos\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
				}else{
			        response_text = String.join("", "\"prestamo\":null");
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
				}
			}
			catch(Throwable e){
				response_text = "{ \"success\": false }";
			}	
		}else if (accion.equals("guardarPrestamo")) {
			boolean result = false;
			boolean esNuevo = map.get("esNuevo") == "true" ? true : false;
			Integer prestamoId = Utils.String2Int(map.get("prestamoId"), null);
			Long codigoPresupuestario = Utils.String2Long(map.get("codigoPresupuestario"));
			String numeroPrestamo =  map.get("numeroPrestamo"); 
			String  proyectoPrograma = map.get("proyetoPrograma");
			
			int ejercicio = Utils.String2Int(map.get("ejercicio"), 0);
			int entidad = Utils.String2Int(map.get("entidad"), 0);
			int unidadEjecutoraPrestamo = Utils.String2Int(map.get("unidadEjecutora"), 0);
			
			UnidadEjecutora unidadEjecutora_ = UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidad, unidadEjecutoraPrestamo);
			
			Cooperante cooperanteUe = new Cooperante();
			cooperanteUe.setId(map.get("cooperanteUeId")!=null ? Integer.parseInt(map.get("cooperanteUeId")): null);
			
			Date fechaDecreto = Utils.dateFromString(map.get("fechaDecreto"));
			Date fechaSuscripcion = Utils.dateFromString(map.get("fechaSuscripcion"));
			Date fechaVigencia = Utils.dateFromString(map.get("fechaVigencia"));
			
			int tipoMonedaId = Utils.String2Int(map.get("tipoMonedaId"));
			TipoMoneda tipoMoneda = new TipoMoneda();
			tipoMoneda.setId(tipoMonedaId);
			
			BigDecimal montoContratado = Utils.String2BigDecimal(map.get("montoContratado"), null);
			BigDecimal montoContratadoUsd = Utils.String2BigDecimal(map.get("montoContratadoUsd"), null);
			BigDecimal montoContratadoQtz = Utils.String2BigDecimal(map.get("montoContratadoQtz"), null);
			BigDecimal desembolsoAFechaUsd = Utils.String2BigDecimal(map.get("desembolsoAFechaUsd"), null);
			BigDecimal montoPorDesembolsarUsd = Utils.String2BigDecimal(map.get("montoPorDesembolsarUsd"), null);
			Date fechaElegibilidadUe = Utils.dateFromString(map.get("fechaElegibilidad"));
			Date fechaCierreOrigianlUe = Utils.dateFromString(map.get("fechaCierreOriginal"));
			Date fechaCierreActualUe = Utils.dateFromString(map.get("fechaCierreActual"));
			int mesesProrrogaUe = Utils.String2Int(map.get("mesesProrroga"), 0);
			BigDecimal montoAsignadoUe = Utils.String2BigDecimal(map.get("montoAisignadoUe"), null);
			BigDecimal desembolsoAFechaUe = Utils.String2BigDecimal(map.get("desembolsoAFechaUe"), null);
			BigDecimal montoPorDesembolsarUe = Utils.String2BigDecimal(map.get("montoPorDesembolsarUe"), null);
			BigDecimal montoAsignadoUeUsd = Utils.String2BigDecimal(map.get("montoAsignadoUeUsd"), null);
			BigDecimal montoAsignadoUeQtz = Utils.String2BigDecimal(map.get("montoAsignadoUeQtz"), null);
			BigDecimal desembolsoAFechaUeUsd = Utils.String2BigDecimal(map.get("desembolsoAFechaUeUsd"), null);
			BigDecimal montoPorDesembolsarUeUsd = Utils.String2BigDecimal(map.get("montoPorDesembolsarUeUsd"), null);
			Integer porcentajeAvance = Utils.String2Int(map.get("porcentajeAvance"));
			
			// prestamo campos adicionales
			Date fechaCorte = map.get("fechaCorte") == null ? null : Utils.dateFromString(map.get("fechaCorte"));
			String destino = map.get("destino");
			String sectorEconomico = map.get("sectorEconomico");
			Date fechaFirma = map.get("fechaFimra") == null ? null : Utils.dateFromString(map.get("fechaFimra"));
			Integer tipoAutorizacionId = Utils.String2Int(map.get("tipoAutorizacionId"),null);
			String numeroAutorizacion = map.get("numeroAutorizacion");
			Date fechaAutorizacion = map.get("fechaAutorizacion") == null ? null : Utils.dateFromString(map.get("fechaAutorizacion"));
			Integer aniosPlazo = Utils.String2Int(map.get("aniosPlazo"), null);
			Integer aniosGracia = Utils.String2Int(map.get("aniosGracia"), null);
			Date fechaFinEjecucion = map.get("fechaFinEjecucion") == null ? null : Utils.dateFromString(map.get("fechaFinEjecucion"));
			Integer peridoEjecucion = Utils.String2Int(map.get("periodoEjecucion"), null);
			Integer tipoInteresId = Utils.String2Int(map.get("tipoInteresId"), null);
			BigDecimal porcentajeInteres = Utils.String2BigDecimal(map.get("porcentajeInteres"), null); 
			BigDecimal porcentajeComisionCompra = Utils.String2BigDecimal(map.get("porcentajeComisionCompra"), null);
			BigDecimal amortizado = Utils.String2BigDecimal(map.get("amortizado"), null);
			BigDecimal porAmortizar = Utils.String2BigDecimal(map.get("porAmortizar"), null);
			BigDecimal principalAnio = Utils.String2BigDecimal(map.get("principalAnio"), null);
			BigDecimal interesesAnio = Utils.String2BigDecimal(map.get("interesesAnio"), null);
			BigDecimal comisionCompromisoAnio= Utils.String2BigDecimal(map.get("comisionCompromisoAnio"), null);
			BigDecimal otrosGastos = Utils.String2BigDecimal(map.get("otrosGastos"), null);
			BigDecimal principalAcumulado = Utils.String2BigDecimal(map.get("principalAcumulado"), null);
			BigDecimal interesesAcumulados = Utils.String2BigDecimal(map.get("interesesAcumulados"), null);
			BigDecimal comisionCompromisoAcumulado = Utils.String2BigDecimal(map.get("comisionCompromisoAcumulado"), null);
			BigDecimal otrosCargosAcumulados = Utils.String2BigDecimal(map.get("otrosCargosAcumulados"), null);
			BigDecimal presupuestoAsignadoFuncionamiento = Utils.String2BigDecimal(map.get("presupuestoAsignadoFuncionamiento"), null);
			BigDecimal prespupuestoAsignadoInversion = Utils.String2BigDecimal(map.get("presupuestoAsignadoInversion"), null);
			BigDecimal presupuestoModificadoFun = Utils.String2BigDecimal(map.get("presupuestoModificadoFuncionamiento"), null);
			BigDecimal presupuestoModificadoInv = Utils.String2BigDecimal(map.get("presupuestoModificadoInversion"), null);
			BigDecimal presupuestoVigenteFun = Utils.String2BigDecimal(map.get("presupuestoVigenteFuncionamiento"), null);
			BigDecimal presupuestoVigenteInv = Utils.String2BigDecimal(map.get("presupuestoVigenteInversion"), null);
			BigDecimal prespupuestoDevengadoFun = Utils.String2BigDecimal(map.get("presupuestoDevengadoFunconamiento"), null);
			BigDecimal presupuestoDevengadoInv = Utils.String2BigDecimal(map.get("presupuestoDevengadoInversion"), null);
			BigDecimal presupuestoPagadoFun = Utils.String2BigDecimal(map.get("presupuestoPagadoFuncionamiento"), null);
			BigDecimal presupuestoPagadoInv = Utils.String2BigDecimal(map.get("presupuestoPagadoInversion"), null);
			BigDecimal saldoCuentas = Utils.String2BigDecimal(map.get("saldoCuentas"), null);
			BigDecimal desembolsadoReal = Utils.String2BigDecimal(map.get("desembolsoReal"), null);
			String objetivo = map.get("objetivo");
			String objetivoEspecifico = map.get("objetivoEspecifico");
			
			
			
			AutorizacionTipo autorizacionTipo = null;
			
			if (tipoAutorizacionId != null){
				autorizacionTipo = new AutorizacionTipo();
				autorizacionTipo.setId(tipoAutorizacionId);
			}
			
			Integer ejecucionEstadoId = Utils.String2Int(map.get("ejecucionEstadoId"), null);
			EjecucionEstado ejecucionEstado = null;
			if (ejecucionEstadoId != null){
				ejecucionEstado = new EjecucionEstado();
				ejecucionEstado.setId(ejecucionEstadoId);
			}
			
			InteresTipo interesTipo = null;
			if (tipoInteresId != null){
				interesTipo = new InteresTipo();
				interesTipo.setId(tipoInteresId);
			}
			
			Prestamo prestamo = PrestamoDAO.getPrestamoById(prestamoId);
			
			if (esNuevo==true){
				// revisar esta variable plazoEjecucionUe que deberia ir en el penultimo null
				prestamo = new Prestamo(autorizacionTipo, cooperanteUe, ejecucionEstado, interesTipo, tipoMoneda, 
						unidadEjecutora_, fechaCorte, codigoPresupuestario, numeroPrestamo, destino, sectorEconomico, 
						fechaFirma, numeroAutorizacion, fechaAutorizacion, aniosPlazo, aniosGracia,
						fechaFinEjecucion, peridoEjecucion, porcentajeInteres, porcentajeComisionCompra, 
						montoContratado, amortizado, porAmortizar, principalAnio, interesesAnio, comisionCompromisoAnio, 
						otrosGastos, principalAcumulado, interesesAcumulados, comisionCompromisoAcumulado, 
						otrosCargosAcumulados, presupuestoAsignadoFuncionamiento, prespupuestoAsignadoInversion,
						presupuestoModificadoFun, presupuestoModificadoInv, presupuestoVigenteFun,
						presupuestoVigenteInv, prespupuestoDevengadoFun, presupuestoDevengadoInv,
						presupuestoPagadoFun, presupuestoPagadoInv, saldoCuentas, desembolsadoReal, 
						usuario, null, new Date(), null, 1, proyectoPrograma, fechaDecreto, 
						fechaSuscripcion, fechaElegibilidadUe, fechaCierreOrigianlUe, fechaCierreActualUe, mesesProrrogaUe,
						null, montoAsignadoUe, desembolsoAFechaUe, montoPorDesembolsarUe, fechaVigencia, 
						montoContratadoUsd, montoContratadoQtz, desembolsoAFechaUsd, montoPorDesembolsarUsd, montoAsignadoUeUsd, 
						montoAsignadoUeQtz, desembolsoAFechaUeUsd, montoPorDesembolsarUeUsd,objetivo,objetivoEspecifico,porcentajeAvance, null,null,null,null);
				result = PrestamoDAO.guardarPrestamo(prestamo);
				
			}else{
				prestamo.setAmortizado(amortizado);
				prestamo.setAniosGracia(aniosGracia);
				prestamo.setAniosPlazo(aniosPlazo);
				prestamo.setAutorizacionTipo(autorizacionTipo);
				prestamo.setCodigoPresupuestario(codigoPresupuestario);
				prestamo.setComisionCompromisoAcumulado(comisionCompromisoAcumulado);
				prestamo.setComisionCompromisoAnio(comisionCompromisoAnio);
				prestamo.setDesembolsadoReal(desembolsadoReal);
				prestamo.setDesembolsoAFechaUe(desembolsoAFechaUe);
				prestamo.setDestino(destino);
				prestamo.setEjecucionEstado(ejecucionEstado);
				prestamo.setEstado(1);
				prestamo.setFechaActualizacion(new Date());
				prestamo.setFechaAutorizacion(fechaAutorizacion);
				prestamo.setFechaCierreActualUe(fechaCierreActualUe);
				prestamo.setFechaCierreOrigianlUe(fechaCierreOrigianlUe);
				prestamo.setFechaCorte(fechaCorte);
				prestamo.setFechaDecreto(fechaDecreto);
				prestamo.setFechaElegibilidadUe(fechaElegibilidadUe);
				prestamo.setFechaFinEjecucion(fechaFinEjecucion);
				prestamo.setFechaFirma(fechaFirma);
				prestamo.setFechaSuscripcion(fechaSuscripcion);
				prestamo.setInteresesAcumulados(interesesAcumulados);
				prestamo.setInteresesAnio(interesesAnio);
				prestamo.setInteresTipo(interesTipo);
				prestamo.setMesesProrrogaUe(mesesProrrogaUe);
				prestamo.setMontoAsignadoUe(montoAsignadoUe);
				prestamo.setMontoContratado(montoContratado);
				prestamo.setMontoPorDesembolsarUe(montoPorDesembolsarUe);
				prestamo.setNumeroAutorizacion(numeroAutorizacion);
				prestamo.setNumeroPrestamo(numeroPrestamo);
				prestamo.setOtrosCargosAcumulados(otrosCargosAcumulados);
				prestamo.setOtrosGastos(otrosGastos);
				prestamo.setPeridoEjecucion(peridoEjecucion);
				prestamo.setPorAmortizar(porAmortizar);
				prestamo.setPorcentajeComisionCompra(porcentajeComisionCompra);
				prestamo.setPorcentajeInteres(porcentajeInteres);
				prestamo.setPrespupuestoAsignadoInversion(prespupuestoAsignadoInversion);
				prestamo.setPrespupuestoDevengadoFuncionamiento(prespupuestoDevengadoFun);
				prestamo.setPresupuestoAsignadoFuncionamiento(presupuestoAsignadoFuncionamiento);
				prestamo.setPresupuestoDevengadoInversion(presupuestoDevengadoInv);
				prestamo.setPresupuestoModificadoFuncionamiento(presupuestoModificadoFun);
				prestamo.setPresupuestoModificadoInversion(presupuestoModificadoInv);
				prestamo.setPresupuestoPagadoFuncionamiento(presupuestoPagadoFun);
				prestamo.setPresupuestoPagadoInversion(presupuestoPagadoInv);
				prestamo.setPresupuestoVigenteFuncionamiento(presupuestoVigenteFun);
				prestamo.setPresupuestoVigenteInversion(presupuestoVigenteInv);
				prestamo.setPrincipalAcumulado(principalAcumulado);
				prestamo.setPrincipalAnio(principalAnio);
				prestamo.setProyectoPrograma(proyectoPrograma);
				prestamo.setSaldoCuentas(saldoCuentas);
				prestamo.setSectorEconomico(sectorEconomico);
				prestamo.setTipoMoneda(tipoMoneda);
				prestamo.setUnidadEjecutora(unidadEjecutora_);
				prestamo.setUsuarioActualizo(usuario);
				prestamo.setFechaVigencia(fechaVigencia);
				prestamo.setMontoContratadoUsd(montoContratadoUsd);
				prestamo.setMontoContratadoQtz(montoContratadoQtz);
				prestamo.setDesembolsoAFechaUsd(desembolsoAFechaUsd);
				prestamo.setMontoPorDesembolsarUsd(montoPorDesembolsarUsd);
				prestamo.setMontoAsignadoUeUsd(montoAsignadoUeUsd);
				prestamo.setMontoAsignadoUeQtz(montoAsignadoUeQtz);
				prestamo.setDesembolsoAFechaUeUsd(desembolsoAFechaUeUsd);
				prestamo.setMontoPorDesembolsarUeUsd(montoPorDesembolsarUeUsd);
				prestamo.setCooperante(cooperanteUe);
				prestamo.setObjetivo(objetivo);
				prestamo.setObjetivoEspecifico(objetivoEspecifico);
				prestamo.setPorcentajeAvance(porcentajeAvance);
				result = PrestamoDAO.guardarPrestamo(prestamo);
				
				
			}
			
			if(result){
				PrestamoTipoDAO.desasignarTiposAPrestamo(prestamo.getId());
				String idPrestamoTipos = map.get("idPrestamoTipos");
				if(idPrestamoTipos != null && idPrestamoTipos.length() > 0){
					String[] prestamoTipos = idPrestamoTipos.split(",");
					ArrayList<Integer> tipos = new ArrayList<Integer>();
					for(String tipo : prestamoTipos){
						tipos.add(Utils.String2Int(tipo));
					}
					result = PrestamoTipoDAO.asignarTiposAPrestamo(tipos, prestamo, usuario);
				}	
			}
			response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
					+ "\"id\": " + prestamo.getId() ,","
					, "\"usuarioCreo\": \"" , prestamo.getUsuarioCreo(),"\","
					, "\"fechaCreacion\":\" " , Utils.formatDateHour(prestamo.getFechaCreacion()),"\","
					, "\"usuarioActualizo\": \"" , prestamo.getUsuarioActualizo() != null ? prestamo.getUsuarioActualizo() : "","\","
					, "\"fechaActualizacion\": \"" , Utils.formatDateHour(prestamo.getFechaActualizacion()),"\""+
					" }");
		}else if(accion.equals("getPrestamosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int elementosPorPagina = map.get("elementosPorPagina")!=null  ? Integer.parseInt(map.get("elementosPorPagina")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			Long filtro_codigo_presupuestario = Utils.String2Long(map.get("filtro_codigo_presupuestario")) != 0 ? Utils.String2Long(map.get("filtro_codigo_presupuestario")) : null;
			String filtro_numero_prestamo = map.get("filtro_numero_prestamo");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			
			List<Prestamo> lstprestamos = PrestamoDAO.getPrestamosPagina(pagina, elementosPorPagina,filtro_nombre, filtro_codigo_presupuestario, filtro_numero_prestamo, 
					filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion, usuario);
			
			List<stprestamo> lstprestamo = new ArrayList<stprestamo>();
			stprestamo temp = null;
			for(Prestamo prestamo : lstprestamos){
				temp = new stprestamo();
				temp.id = prestamo.getId();
				temp.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
				temp.codigoPresupuestario = prestamo.getCodigoPresupuestario();
				temp.numeroPrestamo = prestamo.getNumeroPrestamo(); 
				temp.destino = prestamo.getDestino();
				temp.sectorEconomico = prestamo.getSectorEconomico();
				temp.fechaFirma = (prestamo.getFechaFirma() == null ? null : Utils.formatDate(prestamo.getFechaFirma()));
				temp.tipoAutorizacionId = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getId());
				temp.tipoAutorizacionNombre = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getNombre());
				temp.numeroAutorizacion = (prestamo.getNumeroAutorizacion() == null ? null: prestamo.getNumeroAutorizacion());
				temp.fechaAutorizacion = prestamo.getFechaAutorizacion() == null ? null : Utils.formatDate(prestamo.getFechaAutorizacion());
				temp.aniosPlazo = (prestamo.getAniosPlazo() == null ? null : prestamo.getAniosPlazo()); 
				temp.aniosGracia = (prestamo.getAniosGracia() == null ? null : prestamo.getAniosGracia());  
				temp.fechaFinEjecucion = prestamo.getFechaFinEjecucion() == null ? null : Utils.formatDate(prestamo.getFechaFinEjecucion());
				temp.periodoEjecucion = (prestamo.getPeridoEjecucion() == null ? null :prestamo.getPeridoEjecucion()); 
				temp.tipoInteresId = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getId());
				temp.tipoInteresNombre = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getNombre());
				temp.porcentajeInteres = prestamo.getPorcentajeInteres(); 
				temp.porcentajeComisionCompra = prestamo.getPorcentajeComisionCompra();
				temp.tipoMonedaId = prestamo.getTipoMoneda().getId();
				temp.tipoMonedaNombre = prestamo.getTipoMoneda().getNombre();
				temp.montoContratado = prestamo.getMontoContratado();
				temp.amortizado = prestamo.getAmortizado();
				temp.porAmortizar = prestamo.getPorAmortizar();
				temp.principalAnio = prestamo.getPrincipalAnio();
				temp.interesesAnio = prestamo.getInteresesAnio();
				temp.comisionCompromisoAnio = prestamo.getComisionCompromisoAnio();
				temp.otrosGastos = prestamo.getOtrosGastos();
				temp.principalAcumulado = prestamo.getPrincipalAcumulado();
				temp.interesesAcumulados = prestamo.getInteresesAcumulados();
				temp.comisionCompromisoAcumulado = prestamo.getComisionCompromisoAcumulado();
				temp.otrosCargosAcumulados = prestamo.getOtrosCargosAcumulados();
				temp.presupuestoAsignadoFuncionamiento = prestamo.getPresupuestoAsignadoFuncionamiento();
				temp.presupuestoAsignadoInversion = prestamo.getPrespupuestoAsignadoInversion();
				temp.presupuestoModificadoFun = prestamo.getPresupuestoModificadoFuncionamiento();
				temp.presupuestoModificadoInv = prestamo.getPresupuestoModificadoInversion();
				temp.presupuestoVigenteFun = prestamo.getPresupuestoVigenteFuncionamiento();
				temp.presupuestoVigenteInv = prestamo.getPresupuestoVigenteInversion();
				temp.presupuestoDevengadoFun = prestamo.getPrespupuestoDevengadoFuncionamiento();
				temp.presupuestoDevengadoInv = prestamo.getPresupuestoDevengadoInversion();
				temp.presupuestoPagadoFun = prestamo.getPresupuestoPagadoFuncionamiento();
				temp.presupuestoPagadoInv = prestamo.getPresupuestoPagadoInversion();
				temp.saldoCuentas = prestamo.getSaldoCuentas();
				temp.desembolsoReal = prestamo.getSaldoCuentas();
				temp.ejecucionEstadoId = (prestamo.getEjecucionEstado() == null ? null :prestamo.getEjecucionEstado().getId()); 
				temp.ejecucionEstadoNombre = (prestamo.getEjecucionEstado() == null ? null : prestamo.getEjecucionEstado().getNombre());
				temp.proyectoPrograma = prestamo.getProyectoPrograma();
				temp.fechaDecreto = Utils.formatDate(prestamo.getFechaDecreto());
				temp.fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
				temp.fechaElegibilidadUe = Utils.formatDate(prestamo.getFechaElegibilidadUe());
				temp.fechaCierreOrigianlUe = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
				temp.fechaCierreActualUe = Utils.formatDate(prestamo.getFechaCierreActualUe());
				temp.mesesProrrogaUe = prestamo.getMesesProrrogaUe();
				temp.montoAsignadoUe = prestamo.getMontoAsignadoUe();
				temp.desembolsoAFechaUe = prestamo.getDesembolsoAFechaUe();
				temp.montoPorDesembolsarUe = prestamo.getMontoPorDesembolsarUe();
				temp.fechaVigencia = Utils.formatDate(prestamo.getFechaVigencia());
				temp.montoContratadoUsd = prestamo.getMontoContratadoUsd();
				temp.montoContratadoQtz = prestamo.getMontoContratadoQtz();
				temp.desembolsoAFechaUsd = prestamo.getDesembolsoAFechaUsd();
				temp.montoPorDesembolsarUsd = prestamo.getMontoPorDesembolsarUsd();
				temp.montoAsignadoUeUsd = prestamo.getMontoAsignadoUeUsd();
				temp.montoAsignadoUeQtz = prestamo.getMontoAsignadoUeQtz();
				temp.desembolsoAFechaUeUsd = prestamo.getDesembolsoAFechaUeUsd();
				temp.montoPorDesembolsarUeUsd = prestamo.getMontoPorDesembolsarUeUsd();
				temp.cooperanteid = prestamo.getCooperante().getId();
				temp.cooperantenombre =  (prestamo.getCooperante().getSiglas()!=null ? 
						prestamo.getCooperante().getSiglas() + " - " : "") + prestamo.getCooperante().getNombre();
				
				if (prestamo.getUnidadEjecutora()!=null){
					temp.unidadEjecutora = prestamo.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.unidadEjecutoraNombre = prestamo.getUnidadEjecutora().getNombre();
					temp.nombreEntidadEjecutora = prestamo.getUnidadEjecutora().getEntidad().getNombre();
				}
				
				temp.usuarioCreo = prestamo.getUsuarioCreo();
				temp.usuarioActualizo = prestamo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(prestamo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(prestamo.getFechaActualizacion());
				temp.objetivo = prestamo.getObjetivo();
				temp.objetivoEspecifico = prestamo.getObjetivoEspecifico();
				temp.porcentajeAvance = prestamo.getPorcentajeAvance();
				lstprestamo.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			response_text = String.join("", "\"prestamos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("numeroPrestamos")){
			String filtro_nombre = map.get("filtro_nombre");
			Long filtro_codigo_presupuestario = Utils.String2Long(map.get("filtro_codigo_presupuestario")) != 0 ? Utils.String2Long(map.get("filtro_codigo_presupuestario")) : null;
			String filtro_numero_prestamo = map.get("filtro_numero_prestamo");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			
			response_text = String.join("","{ \"success\": true, \"totalprestamos\":",PrestamoDAO.getTotalPrestamos(filtro_nombre, filtro_codigo_presupuestario, filtro_numero_prestamo, filtro_usuario_creo, filtro_fecha_creacion, usuario).toString()," }");
		}else if(accion.equals("borrarPrestamo")){
			Integer prestamoId = Utils.String2Int(map.get("prestamoId"));
			Prestamo prestamo = PrestamoDAO.getPrestamoById(prestamoId);
			prestamo.setUsuarioActualizo(usuario);
			prestamo.setFechaActualizacion(new Date());
			
			boolean eliminado = PrestamoDAO.borrarPrestamo(prestamo);
			response_text = String.join("","{ \"success\": ", eliminado == true ? "true" : "false"," }");			
		}else if(accion.equals("getComponentesSigade")){
			String codigoPresupuestario = map.get("codigoPresupuestario");
			List<?> componentesSigade = DataSigadeDAO.getComponentes(codigoPresupuestario);
			List<stcomponentessigade> lstcomponentes = new ArrayList<stcomponentessigade>();
			stcomponentessigade temp = null;
			for(Object objComponente : componentesSigade){
				Object[] componente = (Object[])objComponente;
				temp = new stcomponentessigade();
				temp.id = (Integer)componente[1];
				temp.nombre = (String)componente[2];
				temp.tipoMoneda = (String)componente[3];
				temp.techo = (BigDecimal)componente[4];
				lstcomponentes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstcomponentes);
			response_text = String.join("", "\"componentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("getUnidadesEjecutoras")){
			String codigoPresupuestario = map.get("codigoPresupuestario");
			Integer prestamoId = Utils.String2Int(map.get("proyectoId"), 0);
			int ejercicio = Year.now().getValue();
			
			List<?> unidadesEjecutoras = DataSigadeDAO.getUnidadesEjecutoras(codigoPresupuestario, ejercicio);
			List<stunidadejecutora> lstunidadesejecutoras = new ArrayList<stunidadejecutora>();
			stunidadejecutora temp = null;
			for(Object unidadEjecutora : unidadesEjecutoras){
				temp = new stunidadejecutora();
				Object[] objEU = (Object[])unidadEjecutora;
				
				UnidadEjecutora EU = UnidadEjecutoraDAO.getUnidadEjecutora((Integer)objEU[1], (Integer)objEU[2], (Integer)objEU[3]);
				if(EU != null){
					temp.id = EU.getId().getUnidadEjecutora();
					//TODO: quitar id Unidad Ejecutora quemado
					if(prestamoId == 10){
						temp.id = 205;
					}
					temp.ejercicio = EU.getId().getEjercicio();
					temp.entidad = EU.getEntidad().getNombre();
					temp.entidadId = EU.getEntidad().getId().getEntidad();
					temp.nombre = EU.getNombre();

					if(prestamoId>0){
						Proyecto proyecto = ProyectoDAO.getProyectoPorUnidadEjecutora(temp.id, prestamoId, temp.entidadId);
						if(proyecto!=null){
							temp.esCoordinador = proyecto.getCoordinador()!=null ? proyecto.getCoordinador() : 0;
							temp.fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
							temp.fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
						}
					}
					
					lstunidadesejecutoras.add(temp);
				}
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstunidadesejecutoras);
			response_text = String.join("", "\"unidadesEjecutoras\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("obtenerMatriz")){
			String codigoPresupuestario = map.get("codigoPresupuestario");
			Integer prestamoId = Utils.String2Int(map.get("prestamoId"),0);
			boolean existenDatos = false;
			int anio_actual = Year.now().getValue();
			List<?> unidadesEjecutorasSigade = DataSigadeDAO.getUnidadesEjecutoras(codigoPresupuestario,anio_actual);
			List<stunidadejecutora> unidadesEjecutroas =  new ArrayList<stunidadejecutora>();
			

			List<?> componentesSigade = DataSigadeDAO.getComponentes(codigoPresupuestario);
			List<stcomponentessigade> stcomponentes = new ArrayList<stcomponentessigade>();
			if(componentesSigade!=null && componentesSigade.size()>0){
				for(int i=0; i<componentesSigade.size(); i++){
					Object[] componenteSigade = (Object[]) componentesSigade.get(i);
					stcomponentessigade temp = new stcomponentessigade();
					temp.nombre = (String) componenteSigade[2];
					temp.techo = (BigDecimal) componenteSigade[4];
					temp.orden = (Integer) componenteSigade[1];
					unidadesEjecutroas =  new ArrayList<stunidadejecutora>();
					if (unidadesEjecutorasSigade!=null && unidadesEjecutorasSigade.size()>0){
						for(int j=0; j<unidadesEjecutorasSigade.size(); j++){
							Object[] unidadEjecutora = (Object[]) unidadesEjecutorasSigade.get(j);
							UnidadEjecutora unidade = UnidadEjecutoraDAO.getUnidadEjecutora((Integer) unidadEjecutora[1],(Integer) unidadEjecutora[2],
									(Integer) unidadEjecutora[3]);
							stunidadejecutora temp_ = new stunidadejecutora();
							temp_.id = unidade.getId().getUnidadEjecutora();
							temp_.entidad = unidade.getId().getEntidadentidad() + "";
							temp_.entidadId = unidade.getId().getEntidadentidad();
							temp_.ejercicio = unidade.getId().getEjercicio();
							temp_.nombre = unidade.getNombre();
							temp_.techo = temp.techo;
							temp_.prestamoId=prestamoId;
														
							Componente compTemp = ComponenteDAO.obtenerComponentePorEntidad(codigoPresupuestario,temp_.ejercicio,
									Integer.valueOf(temp_.entidad),temp_.id,temp.orden,prestamoId);
							if (compTemp != null){
								temp_.componenteId = compTemp.getId();
								temp_.componenteSigadeId = compTemp.getComponenteSigade()!=null?compTemp.getComponenteSigade().getId():null;
								temp_.prestamo = compTemp.getFuentePrestamo().doubleValue();
								temp_.donacion = compTemp.getFuenteDonacion().doubleValue();
								temp_.nacional = compTemp.getFuenteNacional().doubleValue();
								temp.descripcion = compTemp.getDescripcion();
								existenDatos = true;
							}
								
							unidadesEjecutroas.add(temp_);
						}
					}
					
					temp.unidadesEjecutoras = unidadesEjecutroas;
					stcomponentes.add(temp);
				}
			}
			
			Integer diferencia = DataSigadeDAO.getDiferenciaMontos(codigoPresupuestario);

			String unidades_text=new GsonBuilder().serializeNulls().create().toJson(unidadesEjecutroas);
			String componentes_text = new GsonBuilder().serializeNulls().create().toJson(stcomponentes);
	        response_text = String.join("", ",\"unidadesEjecutoras\":",unidades_text);
	        response_text = String.join("", "\"componentes\":",componentes_text,response_text);
	        response_text = String.join("", "\"diferencia\":",diferencia + ",",response_text);
	        response_text = String.join("", "\"existenDatos\":",existenDatos + ",",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		} else if(accion.equals("guardarMatriz")){
			Integer prestamoId = Utils.String2Int(map.get("prestamoId"));
			Prestamo prestamo = PrestamoDAO.getPrestamoById(prestamoId);
			String data = map.get("estructura");
			String unidadesEjecutoras = map.get("unidadesEjecutoras");
			int existenDatos = Utils.String2Boolean(map.get("existenDatos"), 0);
			
			
			boolean ret = false;
			
				guardarComponentesSigade(prestamo.getCodigoPresupuestario() + "", usuario,existenDatos);
				Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
				List<Map<String, Object>> estructuras = gson.fromJson(data, listType);
				
				JsonParser parser = new JsonParser();
				JsonArray estructuras_ = parser.parse(data).getAsJsonArray();
				JsonArray est_unidadesEjecutoras_ = parser.parse(unidadesEjecutoras).getAsJsonArray();
				
				ArrayList<Proyecto> proyectos = new ArrayList<Proyecto>();
				ArrayList<stunidadejecutora> unidadesEjecutorasMatriz = new ArrayList<stunidadejecutora>();
				int k = 0;
				for (Map<String, Object> estructura : estructuras){
					JsonObject estructura_ = estructuras_.get(k).getAsJsonObject();
					k++;
					parser = new JsonParser();
					JsonArray unidades = estructura_.get("unidadesEjecutoras").getAsJsonArray() ;
					listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
					
					for(int j=0; j<unidades.size(); j++){
						JsonObject unidad = unidades.get(j).getAsJsonObject();
						int posicion = existeUnidad(proyectos,unidad);
						
						BigDecimal fuentePrestamo =!unidad.get("prestamo").isJsonNull() ?   Utils.String2BigDecimal(unidad.get("prestamo").getAsString(), new BigDecimal(0)) : new BigDecimal(0);
						BigDecimal fuenteDonacion = !unidad.get("donacion").isJsonNull() ?  Utils.String2BigDecimal(unidad.get("donacion").getAsString(), new BigDecimal(0)): new BigDecimal(0);
						BigDecimal fuenteNacional = !unidad.get("nacional").isJsonNull() ? Utils.String2BigDecimal(unidad.get("nacional").getAsString(), new BigDecimal(0)): new BigDecimal(0); 
						if(posicion== -1){
							proyectos.add(crearEditarProyecto(unidad,prestamo,usuario,est_unidadesEjecutoras_,existenDatos));
							ret = proyectos.size()>0;
							if (fuentePrestamo.compareTo(BigDecimal.ZERO) > 0 || 
									fuenteDonacion.compareTo(BigDecimal.ZERO) > 0 || 
									fuenteNacional.compareTo(BigDecimal.ZERO)  > 0){
								ret = ret && crearEditarComponente(proyectos.get(proyectos.size() -1),(String) estructura.get("nombre"), (String) estructura.get("descripcion"),
										fuentePrestamo, fuenteDonacion, fuenteNacional,usuario,
										prestamo.getCodigoPresupuestario(),((Double) estructura.get("orden")).intValue());
							}
						}else{
							if (fuentePrestamo.compareTo(BigDecimal.ZERO) > 0 || 
									fuenteDonacion.compareTo(BigDecimal.ZERO) > 0 || 
									fuenteNacional.compareTo(BigDecimal.ZERO)  > 0){
								ret = ret && crearEditarComponente(proyectos.get(posicion),(String) estructura.get("nombre"), (String) estructura.get("descripcion"),
										fuentePrestamo, fuenteDonacion, fuenteNacional,usuario,
										prestamo.getCodigoPresupuestario(),((Double) estructura.get("orden")).intValue());
							}
						}
						ComponenteSigade componenteSigade = ComponenteSigadeDAO.getComponenteSigadePorCodigoNumero(prestamo.getCodigoPresupuestario() + "", ((Double) estructura.get("orden")).intValue());
						stunidadejecutora unidadMatriz = new stunidadejecutora();
						unidadMatriz.componenteSigadeId = componenteSigade.getId();						
						unidadMatriz.id = Utils.String2Int(unidad.get("id").getAsString());
						unidadMatriz.prestamoId=prestamo.getId();
						unidadMatriz.entidadId=Utils.String2Int(unidad.get("entidad").getAsString());
						unidadMatriz.ejercicio=Utils.String2Int(unidad.get("ejercicio").getAsString());
						unidadMatriz.prestamo=fuentePrestamo.doubleValue();
						unidadMatriz.donacion=fuenteDonacion.doubleValue();
						unidadMatriz.nacional=fuenteNacional.doubleValue();
						unidadMatriz.techo = estructura_.get("techo").getAsBigDecimal();
						unidadesEjecutorasMatriz.add(unidadMatriz);
					}
				}
				PrestamoDAO.actualizarMatriz(prestamo.getId(), unidadesEjecutorasMatriz);
				Integer diferencia = DataSigadeDAO.getDiferenciaMontos(prestamo.getCodigoPresupuestario()+"");
			
				response_text = String.join("", "{\"success\":",ret ? "true" : "false", response_text,
					 ",\"diferencia\":",diferencia + "",
					 "}");
			
		} else if(accion.equals("crearComponentesSigade")){
			
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"));
			Integer prestamoId = Utils.String2Int(map.get("prestamoId"));
			Prestamo prestamo = PrestamoDAO.getPrestamoById(prestamoId);
			
			boolean ret = guardarComponentes(prestamo.getCodigoPresupuestario() + "", proyectoId, usuario, prestamo.getFechaSuscripcion());
			response_text = String.join("", "{\"success\":,",ret ? "true" : "false", response_text,"}");
		}else if(accion.equals("obtenerPrestamoPorId")){
            Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
            Prestamo prestamo = PrestamoDAO.getPrestamoById(id);
            response_text = String.join("","{ \"success\": ",(prestamo!=null && prestamo.getId()!=null ? "true" : "false"),", "
                    + "\"id\": " + (prestamo!=null ? prestamo.getId():"0") +", "
            		+ "\"codigoPresupuestario\": " + (prestamo!=null ? prestamo.getCodigoPresupuestario():" ") +", "
            		+ "\"montoPorDesembolsar\": " + (prestamo!=null ? prestamo.getMontoPorDesembolsarUsd():"0") +", "
    				+ "\"desembolsoAFechaUsd\": \"" + (prestamo!=null ? prestamo.getDesembolsoAFechaUsd():" ") +"\", "
					+ "\"fechaEligibilidadUe\": \"" + (prestamo!=null ? Utils.formatDate(prestamo.getFechaElegibilidadUe()):" ") +"\", "
    				+ "\"fechaCierreActualUe\": \"" + (prestamo!=null ? Utils.formatDate(prestamo.getFechaCierreActualUe()):" ") +"\", "					
                    + "\"nombre\": \"" + (prestamo!=null ? prestamo.getProyectoPrograma():"") +"\" }");
        }else if(accion.equals("obtenerTipos")){
        	Integer prestamoId = Utils.String2Int(map.get("prestamoId"));
        	List<PrestamoTipoPrestamo> tipos = PrestamoTipoDAO.getPrestamoTiposPrestamo(prestamoId);
        	List<sttiposprestamo> lsttipos = new ArrayList<sttiposprestamo>();
        	if(tipos != null && tipos.size() > 0){
        		for(PrestamoTipoPrestamo tipo : tipos){
	        		sttiposprestamo temp = new sttiposprestamo();
	        		temp.id = tipo.getPrestamoTipo().getId();
	        		temp.nombre = tipo.getPrestamoTipo().getNombre();
	        		temp.usuarioCreo = tipo.getUsuarioCreo();
	        		temp.usuarioActualizo = tipo.getUsuarioActualizo();
	        		temp.fechaCreacion = Utils.formatDate(tipo.getFechaCreacion());
	        		temp.fechaActualizacion = Utils.formatDate(tipo.getFechaActualizacion());
	        		lsttipos.add(temp);
        		}
        	}
        	
        	String componentes_text = new GsonBuilder().serializeNulls().create().toJson(lsttipos);
	        response_text = String.join("", "\"prestamoTipos\":",componentes_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
        } else if(accion.equals("getPrestamo")){
        	Integer idPrestamo = Utils.String2Int(map.get("prestamoId"));
        	Prestamo prestamo = PrestamoDAO.getPrestamoById(idPrestamo);
        	
        	if(prestamo != null){
        		stprestamo temp = new stprestamo();
				temp.id = prestamo.getId();
				temp.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
				temp.codigoPresupuestario = prestamo.getCodigoPresupuestario();
				temp.numeroPrestamo = prestamo.getNumeroPrestamo(); 
				temp.destino = prestamo.getDestino();
				temp.sectorEconomico = prestamo.getSectorEconomico();
				temp.fechaFirma = (prestamo.getFechaFirma() == null ? null : Utils.formatDate(prestamo.getFechaFirma()));
				temp.tipoAutorizacionId = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getId());
				temp.tipoAutorizacionNombre = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getNombre());
				temp.numeroAutorizacion = (prestamo.getNumeroAutorizacion() == null ? null: prestamo.getNumeroAutorizacion());
				temp.fechaAutorizacion = prestamo.getFechaAutorizacion() == null ? null : Utils.formatDate(prestamo.getFechaAutorizacion());
				temp.aniosPlazo = (prestamo.getAniosPlazo() == null ? null : prestamo.getAniosPlazo()); 
				temp.aniosGracia = (prestamo.getAniosGracia() == null ? null : prestamo.getAniosGracia());  
				temp.fechaFinEjecucion = prestamo.getFechaFinEjecucion() == null ? null : Utils.formatDate(prestamo.getFechaFinEjecucion());
				temp.periodoEjecucion = (prestamo.getPeridoEjecucion() == null ? null :prestamo.getPeridoEjecucion()); 
				temp.tipoInteresId = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getId());
				temp.tipoInteresNombre = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getNombre());
				temp.porcentajeInteres = prestamo.getPorcentajeInteres(); 
				temp.porcentajeComisionCompra = prestamo.getPorcentajeComisionCompra();
				temp.tipoMonedaId = prestamo.getTipoMoneda().getId();
				temp.tipoMonedaNombre = prestamo.getTipoMoneda().getNombre();
				temp.montoContratado = prestamo.getMontoContratado();
				temp.amortizado = prestamo.getAmortizado();
				temp.porAmortizar = prestamo.getPorAmortizar();
				temp.principalAnio = prestamo.getPrincipalAnio();
				temp.interesesAnio = prestamo.getInteresesAnio();
				temp.comisionCompromisoAnio = prestamo.getComisionCompromisoAnio();
				temp.otrosGastos = prestamo.getOtrosGastos();
				temp.principalAcumulado = prestamo.getPrincipalAcumulado();
				temp.interesesAcumulados = prestamo.getInteresesAcumulados();
				temp.comisionCompromisoAcumulado = prestamo.getComisionCompromisoAcumulado();
				temp.otrosCargosAcumulados = prestamo.getOtrosCargosAcumulados();
				temp.presupuestoAsignadoFuncionamiento = prestamo.getPresupuestoAsignadoFuncionamiento();
				temp.presupuestoAsignadoInversion = prestamo.getPrespupuestoAsignadoInversion();
				temp.presupuestoModificadoFun = prestamo.getPresupuestoModificadoFuncionamiento();
				temp.presupuestoModificadoInv = prestamo.getPresupuestoModificadoInversion();
				temp.presupuestoVigenteFun = prestamo.getPresupuestoVigenteFuncionamiento();
				temp.presupuestoVigenteInv = prestamo.getPresupuestoVigenteInversion();
				temp.presupuestoDevengadoFun = prestamo.getPrespupuestoDevengadoFuncionamiento();
				temp.presupuestoDevengadoInv = prestamo.getPresupuestoDevengadoInversion();
				temp.presupuestoPagadoFun = prestamo.getPresupuestoPagadoFuncionamiento();
				temp.presupuestoPagadoInv = prestamo.getPresupuestoPagadoInversion();
				temp.saldoCuentas = prestamo.getSaldoCuentas();
				temp.desembolsoReal = prestamo.getSaldoCuentas();
				temp.ejecucionEstadoId = (prestamo.getEjecucionEstado() == null ? null :prestamo.getEjecucionEstado().getId()); 
				temp.ejecucionEstadoNombre = (prestamo.getEjecucionEstado() == null ? null : prestamo.getEjecucionEstado().getNombre());
				temp.proyectoPrograma = prestamo.getProyectoPrograma();
				temp.fechaDecreto = Utils.formatDate(prestamo.getFechaDecreto());
				temp.fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
				temp.fechaElegibilidadUe = Utils.formatDate(prestamo.getFechaElegibilidadUe());
				temp.fechaCierreOrigianlUe = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
				temp.fechaCierreActualUe = Utils.formatDate(prestamo.getFechaCierreActualUe());
				temp.mesesProrrogaUe = prestamo.getMesesProrrogaUe();
				temp.montoAsignadoUe = prestamo.getMontoAsignadoUe();
				temp.desembolsoAFechaUe = prestamo.getDesembolsoAFechaUe();
				temp.montoPorDesembolsarUe = prestamo.getMontoPorDesembolsarUe();
				temp.fechaVigencia = Utils.formatDate(prestamo.getFechaVigencia());
				temp.montoContratadoUsd = prestamo.getMontoContratadoUsd();
				temp.montoContratadoQtz = prestamo.getMontoContratadoQtz();
				temp.desembolsoAFechaUsd = prestamo.getDesembolsoAFechaUsd();
				temp.montoPorDesembolsarUsd = prestamo.getMontoPorDesembolsarUsd();
				temp.montoAsignadoUeUsd = prestamo.getMontoAsignadoUeUsd();
				temp.montoAsignadoUeQtz = prestamo.getMontoAsignadoUeQtz();
				temp.desembolsoAFechaUeUsd = prestamo.getDesembolsoAFechaUeUsd();
				temp.montoPorDesembolsarUeUsd = prestamo.getMontoPorDesembolsarUeUsd();
				temp.cooperanteid = prestamo.getCooperante().getId();
				temp.cooperantenombre =  (prestamo.getCooperante().getSiglas()!=null ? 
						prestamo.getCooperante().getSiglas() + " - " : "") + prestamo.getCooperante().getNombre();
				
				if (prestamo.getUnidadEjecutora()!=null){
					temp.unidadEjecutora = prestamo.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.unidadEjecutoraNombre = prestamo.getUnidadEjecutora().getNombre();
					temp.nombreEntidadEjecutora = prestamo.getUnidadEjecutora().getEntidad().getNombre();
				}
				
				temp.usuarioCreo = prestamo.getUsuarioCreo();
				temp.usuarioActualizo = prestamo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(prestamo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(prestamo.getFechaActualizacion());
				temp.objetivo = prestamo.getObjetivo();
				temp.objetivoEspecifico = prestamo.getObjetivoEspecifico();
				temp.porcentajeAvance = prestamo.getPorcentajeAvance();
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
		        response_text = String.join("", "\"prestamo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
        	}
        }else if(accion.equals("getPrestamoPorPEP")){
        	String lineaBase = map.get("lineaBase");
        	Integer proyectoId = Utils.String2Int(map.get("proyectoId"));
        	Proyecto proyecto = ProyectoDAO.getProyectoHistory(proyectoId, lineaBase);
        	if (proyecto != null ){
	        	Prestamo prestamo = proyecto.getPrestamo();
	        	if(prestamo != null){
	        		stprestamo temp = new stprestamo();
					temp.id = prestamo.getId();
					temp.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
					temp.codigoPresupuestario = prestamo.getCodigoPresupuestario();
					temp.numeroPrestamo = prestamo.getNumeroPrestamo(); 
					temp.destino = prestamo.getDestino();
					temp.sectorEconomico = prestamo.getSectorEconomico();
					temp.fechaFirma = (prestamo.getFechaFirma() == null ? null : Utils.formatDate(prestamo.getFechaFirma()));
					temp.tipoAutorizacionId = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getId());
					temp.tipoAutorizacionNombre = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getNombre());
					temp.numeroAutorizacion = (prestamo.getNumeroAutorizacion() == null ? null: prestamo.getNumeroAutorizacion());
					temp.fechaAutorizacion = prestamo.getFechaAutorizacion() == null ? null : Utils.formatDate(prestamo.getFechaAutorizacion());
					temp.aniosPlazo = (prestamo.getAniosPlazo() == null ? null : prestamo.getAniosPlazo()); 
					temp.aniosGracia = (prestamo.getAniosGracia() == null ? null : prestamo.getAniosGracia());  
					temp.fechaFinEjecucion = prestamo.getFechaFinEjecucion() == null ? null : Utils.formatDate(prestamo.getFechaFinEjecucion());
					temp.periodoEjecucion = (prestamo.getPeridoEjecucion() == null ? null :prestamo.getPeridoEjecucion()); 
					temp.tipoInteresId = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getId());
					temp.tipoInteresNombre = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getNombre());
					temp.porcentajeInteres = prestamo.getPorcentajeInteres(); 
					temp.porcentajeComisionCompra = prestamo.getPorcentajeComisionCompra();
					temp.tipoMonedaId = prestamo.getTipoMoneda().getId();
					temp.tipoMonedaNombre = prestamo.getTipoMoneda().getNombre();
					temp.montoContratado = prestamo.getMontoContratado();
					temp.amortizado = prestamo.getAmortizado();
					temp.porAmortizar = prestamo.getPorAmortizar();
					temp.principalAnio = prestamo.getPrincipalAnio();
					temp.interesesAnio = prestamo.getInteresesAnio();
					temp.comisionCompromisoAnio = prestamo.getComisionCompromisoAnio();
					temp.otrosGastos = prestamo.getOtrosGastos();
					temp.principalAcumulado = prestamo.getPrincipalAcumulado();
					temp.interesesAcumulados = prestamo.getInteresesAcumulados();
					temp.comisionCompromisoAcumulado = prestamo.getComisionCompromisoAcumulado();
					temp.otrosCargosAcumulados = prestamo.getOtrosCargosAcumulados();
					temp.presupuestoAsignadoFuncionamiento = prestamo.getPresupuestoAsignadoFuncionamiento();
					temp.presupuestoAsignadoInversion = prestamo.getPrespupuestoAsignadoInversion();
					temp.presupuestoModificadoFun = prestamo.getPresupuestoModificadoFuncionamiento();
					temp.presupuestoModificadoInv = prestamo.getPresupuestoModificadoInversion();
					temp.presupuestoVigenteFun = prestamo.getPresupuestoVigenteFuncionamiento();
					temp.presupuestoVigenteInv = prestamo.getPresupuestoVigenteInversion();
					temp.presupuestoDevengadoFun = prestamo.getPrespupuestoDevengadoFuncionamiento();
					temp.presupuestoDevengadoInv = prestamo.getPresupuestoDevengadoInversion();
					temp.presupuestoPagadoFun = prestamo.getPresupuestoPagadoFuncionamiento();
					temp.presupuestoPagadoInv = prestamo.getPresupuestoPagadoInversion();
					temp.saldoCuentas = prestamo.getSaldoCuentas();
					temp.desembolsoReal = prestamo.getSaldoCuentas();
					temp.ejecucionEstadoId = (prestamo.getEjecucionEstado() == null ? null :prestamo.getEjecucionEstado().getId()); 
					temp.ejecucionEstadoNombre = (prestamo.getEjecucionEstado() == null ? null : prestamo.getEjecucionEstado().getNombre());
					temp.proyectoPrograma = prestamo.getProyectoPrograma();
					temp.fechaDecreto = Utils.formatDate(prestamo.getFechaDecreto());
					temp.fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
					temp.fechaElegibilidadUe = Utils.formatDate(prestamo.getFechaElegibilidadUe());
					temp.fechaCierreOrigianlUe = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
					temp.fechaCierreActualUe = Utils.formatDate(prestamo.getFechaCierreActualUe());
					temp.mesesProrrogaUe = prestamo.getMesesProrrogaUe();
					temp.montoAsignadoUe = prestamo.getMontoAsignadoUe();
					temp.desembolsoAFechaUe = prestamo.getDesembolsoAFechaUe();
					temp.montoPorDesembolsarUe = prestamo.getMontoPorDesembolsarUe();
					temp.fechaVigencia = Utils.formatDate(prestamo.getFechaVigencia());
					temp.montoContratadoUsd = prestamo.getMontoContratadoUsd();
					temp.montoContratadoQtz = prestamo.getMontoContratadoQtz();
					temp.desembolsoAFechaUsd = prestamo.getDesembolsoAFechaUsd();
					temp.montoPorDesembolsarUsd = prestamo.getMontoPorDesembolsarUsd();
					temp.montoAsignadoUeUsd = prestamo.getMontoAsignadoUeUsd();
					temp.montoAsignadoUeQtz = prestamo.getMontoAsignadoUeQtz();
					temp.desembolsoAFechaUeUsd = prestamo.getDesembolsoAFechaUeUsd();
					temp.montoPorDesembolsarUeUsd = prestamo.getMontoPorDesembolsarUeUsd();
					temp.cooperanteid = prestamo.getCooperante().getId();
					temp.cooperantenombre =  (prestamo.getCooperante().getSiglas()!=null ? 
							prestamo.getCooperante().getSiglas() + " - " : "") + prestamo.getCooperante().getNombre();
					proyecto.getUnidadEjecutora();
					if (proyecto.getUnidadEjecutora()!=null){
						temp.unidadEjecutora = proyecto.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.unidadEjecutoraNombre = proyecto.getUnidadEjecutora() != null ? proyecto.getUnidadEjecutora().getNombre() : null;
						temp.nombreEntidadEjecutora = proyecto.getUnidadEjecutora() != null ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : null;
					}
					
					temp.montoContratadoEntidadUsd = new BigDecimal(0);
					ArrayList<Componente> componentes = (ArrayList<Componente>) ComponenteDAO.getComponentesPorProyectoHistory(proyecto.getId(),null);
					if (componentes != null){
						for (Componente c : componentes)
							temp.montoContratadoEntidadUsd = temp.montoContratadoEntidadUsd.add(c.getFuentePrestamo()!= null ? c.getFuentePrestamo() : new BigDecimal(0));
					}
					
					Date fechaActual = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
					Long anio = Long.parseLong(sdf.format(fechaActual));
					sdf = new SimpleDateFormat("MM");
					Integer mes = Integer.parseInt(sdf.format(fechaActual));
					
					
					temp.desembolsadoAFecha = DataSigadeDAO.totalDesembolsadoAFechaRealDolaresPorEntidad(prestamo.getCodigoPresupuestario() + "", 
							anio, mes,proyecto.getUnidadEjecutora().getId().getEntidadentidad());
					if (temp.desembolsadoAFecha == null)
						temp.desembolsadoAFecha = new BigDecimal(0);
					
					temp.usuarioCreo = prestamo.getUsuarioCreo();
					temp.usuarioActualizo = prestamo.getUsuarioActualizo();
					temp.fechaCreacion = Utils.formatDateHour(prestamo.getFechaCreacion());
					temp.fechaActualizacion = Utils.formatDate(proyecto.getFechaActualizacion());
					temp.objetivo = prestamo.getObjetivo();
					temp.objetivoEspecifico = prestamo.getObjetivoEspecifico();
					temp.ejecucionFisicaRealPEP = proyecto.getEjecucionFisicaReal();
					Long tiempo1 = 0L;
					sdf = new SimpleDateFormat("HH:mm:ss");
					try {
						tiempo1 = sdf.parse(sdf.format(fechaActual)).getTime();
					} catch (ParseException e) {
						
					}
					Double f1 = (((new Date().getTime()  * 1.0) - tiempo1) - proyecto.getFechaInicio().getTime()) / 86400000;
					Double f2 = (proyecto.getFechaFin().getTime() * 1.0-proyecto.getFechaInicio().getTime()) / 86400000;
					temp.plazoEjecucionPEP =  (f1*1.0/f2 * 100);
					
					response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
			        response_text = String.join("", "\"prestamo\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        	}else 
	        		response_text = "{ \"success\": false }";
        	}else 
        		response_text = "{ \"success\": false }";
        } else if(accion.equals("getPrestamoHistory")){
        	Integer idPrestamo = Utils.String2Int(map.get("prestamoId"));
        	Prestamo prestamo = PrestamoDAO.getPrestamoByIdHistory(idPrestamo,null);
        	
        	if(prestamo != null){
        		stprestamo temp = new stprestamo();
				temp.id = prestamo.getId();
				temp.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
				temp.codigoPresupuestario = prestamo.getCodigoPresupuestario();
				temp.numeroPrestamo = prestamo.getNumeroPrestamo(); 
				temp.destino = prestamo.getDestino();
				temp.sectorEconomico = prestamo.getSectorEconomico();
				temp.fechaFirma = (prestamo.getFechaFirma() == null ? null : Utils.formatDate(prestamo.getFechaFirma()));
				temp.tipoAutorizacionId = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getId());
				temp.tipoAutorizacionNombre = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getNombre());
				temp.numeroAutorizacion = (prestamo.getNumeroAutorizacion() == null ? null: prestamo.getNumeroAutorizacion());
				temp.fechaAutorizacion = prestamo.getFechaAutorizacion() == null ? null : Utils.formatDate(prestamo.getFechaAutorizacion());
				temp.aniosPlazo = (prestamo.getAniosPlazo() == null ? null : prestamo.getAniosPlazo()); 
				temp.aniosGracia = (prestamo.getAniosGracia() == null ? null : prestamo.getAniosGracia());  
				temp.fechaFinEjecucion = prestamo.getFechaFinEjecucion() == null ? null : Utils.formatDate(prestamo.getFechaFinEjecucion());
				temp.periodoEjecucion = (prestamo.getPeridoEjecucion() == null ? null :prestamo.getPeridoEjecucion()); 
				temp.tipoInteresId = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getId());
				temp.tipoInteresNombre = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getNombre());
				temp.porcentajeInteres = prestamo.getPorcentajeInteres(); 
				temp.porcentajeComisionCompra = prestamo.getPorcentajeComisionCompra();
				temp.tipoMonedaId = prestamo.getTipoMoneda().getId();
				temp.tipoMonedaNombre = prestamo.getTipoMoneda().getNombre();
				temp.montoContratado = prestamo.getMontoContratado();
				temp.amortizado = prestamo.getAmortizado();
				temp.porAmortizar = prestamo.getPorAmortizar();
				temp.principalAnio = prestamo.getPrincipalAnio();
				temp.interesesAnio = prestamo.getInteresesAnio();
				temp.comisionCompromisoAnio = prestamo.getComisionCompromisoAnio();
				temp.otrosGastos = prestamo.getOtrosGastos();
				temp.principalAcumulado = prestamo.getPrincipalAcumulado();
				temp.interesesAcumulados = prestamo.getInteresesAcumulados();
				temp.comisionCompromisoAcumulado = prestamo.getComisionCompromisoAcumulado();
				temp.otrosCargosAcumulados = prestamo.getOtrosCargosAcumulados();
				temp.presupuestoAsignadoFuncionamiento = prestamo.getPresupuestoAsignadoFuncionamiento();
				temp.presupuestoAsignadoInversion = prestamo.getPrespupuestoAsignadoInversion();
				temp.presupuestoModificadoFun = prestamo.getPresupuestoModificadoFuncionamiento();
				temp.presupuestoModificadoInv = prestamo.getPresupuestoModificadoInversion();
				temp.presupuestoVigenteFun = prestamo.getPresupuestoVigenteFuncionamiento();
				temp.presupuestoVigenteInv = prestamo.getPresupuestoVigenteInversion();
				temp.presupuestoDevengadoFun = prestamo.getPrespupuestoDevengadoFuncionamiento();
				temp.presupuestoDevengadoInv = prestamo.getPresupuestoDevengadoInversion();
				temp.presupuestoPagadoFun = prestamo.getPresupuestoPagadoFuncionamiento();
				temp.presupuestoPagadoInv = prestamo.getPresupuestoPagadoInversion();
				temp.saldoCuentas = prestamo.getSaldoCuentas();
				temp.desembolsoReal = prestamo.getSaldoCuentas();
				temp.ejecucionEstadoId = (prestamo.getEjecucionEstado() == null ? null :prestamo.getEjecucionEstado().getId()); 
				temp.ejecucionEstadoNombre = (prestamo.getEjecucionEstado() == null ? null : prestamo.getEjecucionEstado().getNombre());
				temp.proyectoPrograma = prestamo.getProyectoPrograma();
				temp.fechaDecreto = Utils.formatDate(prestamo.getFechaDecreto());
				temp.fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
				temp.fechaElegibilidadUe = Utils.formatDate(prestamo.getFechaElegibilidadUe());
				temp.fechaCierreOrigianlUe = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
				temp.fechaCierreActualUe = Utils.formatDate(prestamo.getFechaCierreActualUe());
				temp.mesesProrrogaUe = prestamo.getMesesProrrogaUe();
				temp.montoAsignadoUe = prestamo.getMontoAsignadoUe();
				temp.desembolsoAFechaUe = prestamo.getDesembolsoAFechaUe();
				temp.montoPorDesembolsarUe = prestamo.getMontoPorDesembolsarUe();
				temp.fechaVigencia = Utils.formatDate(prestamo.getFechaVigencia());
				temp.montoContratadoUsd = prestamo.getMontoContratadoUsd();
				temp.montoContratadoQtz = prestamo.getMontoContratadoQtz();
				temp.desembolsoAFechaUsd = prestamo.getDesembolsoAFechaUsd();
				temp.montoPorDesembolsarUsd = prestamo.getMontoPorDesembolsarUsd();
				temp.montoAsignadoUeUsd = prestamo.getMontoAsignadoUeUsd();
				temp.montoAsignadoUeQtz = prestamo.getMontoAsignadoUeQtz();
				temp.desembolsoAFechaUeUsd = prestamo.getDesembolsoAFechaUeUsd();
				temp.montoPorDesembolsarUeUsd = prestamo.getMontoPorDesembolsarUeUsd();
				temp.cooperanteid = prestamo.getCooperante().getId();
				temp.cooperantenombre =  (prestamo.getCooperante().getSiglas()!=null ? 
						prestamo.getCooperante().getSiglas() + " - " : "") + prestamo.getCooperante().getNombre();
				
				if (prestamo.getUnidadEjecutora()!=null){
					temp.unidadEjecutora = prestamo.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.unidadEjecutoraNombre = prestamo.getUnidadEjecutora().getNombre();
					temp.nombreEntidadEjecutora = prestamo.getUnidadEjecutora().getEntidad().getNombre();
				}
				
				temp.usuarioCreo = prestamo.getUsuarioCreo();
				temp.usuarioActualizo = prestamo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(prestamo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(prestamo.getFechaActualizacion());
				temp.objetivo = prestamo.getObjetivo();
				temp.objetivoEspecifico = prestamo.getObjetivoEspecifico();
				temp.porcentajeAvance = prestamo.getPorcentajeAvance();
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
		        response_text = String.join("", "\"prestamo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
        	}
        }else if(accion.equals("congelarDescongelar")){
        	boolean ret = true;
        	String peps = map.get("peps");
        	String nombre = map.get("nombre");
        	Integer generarLineasBases = Utils.String2Boolean(map.get("lineaBase"), 0); 
        	String lineaBaseId = map.get("lineaBaseId");
        	JsonParser parser = new JsonParser();
			JsonArray pepsArreglo = parser.parse(peps).getAsJsonArray();
			for(int i=0; i<pepsArreglo.size(); i++){
				JsonObject objeto = pepsArreglo.get(i).getAsJsonObject();
				Integer id = objeto.get("id").isJsonNull() ? null : objeto.get("id").getAsInt();
				Integer congelado = objeto.get("congelado").isJsonNull() ? null : objeto.get("congelado").getAsBoolean() ? 1 : 0;
				Integer tipoLinea = Utils.String2Int(map.get("tipoLineaBase"),1);
				Proyecto proyecto = ProyectoDAO.getProyecto(id);
				Integer sobreescribir = objeto.get("permiso").isJsonNull() ? null : objeto.get("permiso").getAsBoolean() ? 1 : 0;
				
				LineaBase ultimaLineaBase = LineaBaseDAO.getUltimaLinaBasePorProyecto(proyecto.getId(),2);
				if(ultimaLineaBase!=null){
					ultimaLineaBase.setSobreescribir(sobreescribir);
					ret = ret && LineaBaseDAO.guardarLineaBase(ultimaLineaBase);
				}
				
				if (!congelado.equals(proyecto.getCongelado())){
					proyecto.setCongelado(congelado);
					ret = ret && ProyectoDAO.guardarProyecto(proyecto, false);
					
					
					
					if (ret && proyecto.getCongelado().equals(1) && generarLineasBases.equals(1)){
						LineaBase lineaBase = new LineaBase(proyecto, nombre, usuario, null, new Date(), null,tipoLinea,null);
						ret = LineaBaseDAO.guardarLineaBase(lineaBase,lineaBaseId);
					}
				}
			}
			response_text = String.join(" ", "{ \"success\": ", ret ? "true" : "false","}");
        }else if(accion.equals("getVersionesMatriz")){
        	Integer prestamoId = Utils.String2Int(map.get("prestamoId"));
        	String resultado = PrestamoDAO.getVersionesMatriz(prestamoId); 
			response_text = String.join("", "{\"success\":true, \"versiones\": [" + resultado + "]}");
        }else if(accion.equals("getHistoriaMatriz")){
			Integer prestamoId = Utils.String2Int(map.get("prestamoId"),0);
			Integer version = Utils.String2Int(map.get("version"));			
			String codigoPresupuestario = map.get("codigoPresupuestario");
			
			int anio_actual = Year.now().getValue();
			List<?> unidadesEjecutorasSigade = DataSigadeDAO.getUnidadesEjecutoras(codigoPresupuestario,anio_actual);
			List<stunidadejecutora> unidadesEjecutroas =  new ArrayList<stunidadejecutora>();
			

			List<?> componentesSigade = DataSigadeDAO.getComponentes(codigoPresupuestario);
			List<stcomponentessigade> stcomponentes = new ArrayList<stcomponentessigade>();
			if(componentesSigade!=null && componentesSigade.size()>0){
				for(int i=0; i<componentesSigade.size(); i++){
					Object[] componenteSigade = (Object[]) componentesSigade.get(i);
					stcomponentessigade temp = new stcomponentessigade();
					temp.nombre = (String) componenteSigade[2];
					temp.orden = (Integer) componenteSigade[1];
					unidadesEjecutroas =  new ArrayList<stunidadejecutora>();
					if (unidadesEjecutorasSigade!=null && unidadesEjecutorasSigade.size()>0){
						for(int j=0; j<unidadesEjecutorasSigade.size(); j++){
							Object[] unidadEjecutora = (Object[]) unidadesEjecutorasSigade.get(j);
							UnidadEjecutora unidade = UnidadEjecutoraDAO.getUnidadEjecutora((Integer) unidadEjecutora[1],(Integer) unidadEjecutora[2],
									(Integer) unidadEjecutora[3]);
							stunidadejecutora temp_ = new stunidadejecutora();
							temp_.id = unidade.getId().getUnidadEjecutora();
							temp_.entidad = unidade.getId().getEntidadentidad() + "";
							temp_.entidadId = unidade.getId().getEntidadentidad();
							temp_.ejercicio = unidade.getId().getEjercicio();
							temp_.nombre = unidade.getNombre();
							temp_.techo = temp.techo;
							temp_.prestamoId=prestamoId;
							
							BigDecimal[] techos = PrestamoDAO.getComponenteMatrizHistoria(prestamoId, temp.orden, 
									temp_.entidadId, temp_.ejercicio, temp_.id, version);
							
							if(techos!=null){
								temp.techo = techos[0]!=null?techos[0]:new BigDecimal(0);
								temp_.prestamo = techos[1]!=null?techos[1].doubleValue():0;
								temp_.donacion = techos[2]!=null?techos[2].doubleValue():0;
								temp_.nacional = techos[3]!=null?techos[3].doubleValue():0;
								temp.fechaActualizacion = techos[4]!=null?techos[4].longValue():0;
							}								
							unidadesEjecutroas.add(temp_);
						}
					}
					
					temp.unidadesEjecutoras = unidadesEjecutroas;
					stcomponentes.add(temp);
				}
			}
			
			String unidades_text=new GsonBuilder().serializeNulls().create().toJson(unidadesEjecutroas);
			String componentes_text = new GsonBuilder().serializeNulls().create().toJson(stcomponentes);
						
			response_text = String.join("", ",\"unidadesEjecutoras\":",unidades_text);
	        response_text = String.join("", "\"componentes\":",componentes_text,response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
        }
		else
			response_text = "{ \"success\": false }";
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	public List<stcomponentessigade> bubbleSort(List<stcomponentessigade> lista) {
	      boolean swapped = true;
	      int j = 0;
	      stcomponentessigade tmp;
	      while (swapped) {
	            swapped = false;
	            j++;
	            for (int i = 0; i < lista.size() - j; i++) {                                       
	                  if (lista.get(i).orden > lista.get(i + 1).orden) {                          
	                        tmp = lista.get(i);
	                        lista.set(i, lista.get(i+1));
	                        lista.set(i + 1, tmp);
	                        swapped = true;
	                  }
	            }                
	      }
	      
	      return lista;
	}
	
	private Proyecto crearEditarProyecto(JsonObject unidad,Prestamo prestamo,String usuario, JsonArray est_unidadesEjecutoras,int existeData){
		Proyecto ret = null;
		Integer esCoordinador = 0;
		Date fechaElegibilidad = null;
		Date fechaCierre = null;
		//TODO: unidad ejecutora quemada en if
		for(int j=0; j<est_unidadesEjecutoras.size(); j++){
			JsonObject unidad_ = est_unidadesEjecutoras.get(j).getAsJsonObject();
			if (unidad.get("ejercicio").getAsString().equals(unidad_.get("ejercicio").getAsString()) &&
					unidad.get("entidad").getAsString().equals(unidad_.get("entidadId").getAsString()) && 
					(unidad.get("id").getAsString().equals(unidad_.get("id").getAsString())|| unidad_.get("id").getAsInt()==205)){
				esCoordinador = Utils.String2Boolean(unidad_.get("esCoordinador").getAsString(), 0);
				fechaElegibilidad = !unidad_.get("fechaElegibilidad").isJsonNull() ? Utils.stringToDateZ(unidad_.get("fechaElegibilidad").getAsString()) : null;
				fechaCierre = !unidad_.get("fechaCierre").isJsonNull() ? Utils.stringToDateZ(unidad_.get("fechaCierre").getAsString()) : null;
				break;
			}
		}
		
		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(
				Utils.String2Int(unidad.get("ejercicio").getAsString()),
				Utils.String2Int(unidad.get("entidad").getAsString()),
				Utils.String2Int(unidad.get("id").getAsString()));
		if (unidadEjecutora != null){
			Proyecto proyecto = null;
			if(existeData == 0){
				ProyectoTipo proyectoTipo = ProyectoTipoDAO.getProyectoTipoPorId(1);
				Etiqueta etiqueta = new Etiqueta();
				etiqueta.setId(1);
				AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				
				
				proyecto = new Proyecto(acumulacionCosto,null, etiqueta,prestamo,proyectoTipo, unidadEjecutora
						, prestamo.getNumeroPrestamo() + " - " + unidadEjecutora.getNombre(), null, usuario, null, new Date(), null, 1, null, null, null, null, 
						null, null, null,null, null, null, null,null, null, null,null,
						prestamo.getFechaSuscripcion(),prestamo.getFechaSuscripcion(),
						1, "d"
						,null,null,0,0,0, null,esCoordinador,fechaElegibilidad,fechaCierre,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
			}else{
				proyecto = ProyectoDAO.getProyectoPorUnidadEjecutora(unidadEjecutora.getId().getUnidadEjecutora(), prestamo.getId(), unidadEjecutora.getId().getEntidadentidad());
				proyecto.setCoordinador(esCoordinador);
				proyecto.setFechaElegibilidad(fechaElegibilidad);
				proyecto.setFechaCierre(fechaCierre);
				proyecto.setFechaActualizacion(new Date());
				proyecto.setUsuarioActualizo(usuario);
				
			}
			
			return ProyectoDAO.guardarProyecto(proyecto, false) ? proyecto : null;
		}
		
		return ret;
	}
	
	private boolean  crearEditarComponente(Proyecto proyecto,String nombreComponente, String descripcion, BigDecimal fPrestamo,
			BigDecimal donacion,BigDecimal nacional, String usuario, Long codigoPresupuestario,int orden){
		
		ComponenteSigade componenteSigade = ComponenteSigadeDAO.getComponenteSigadePorCodigoNumero(codigoPresupuestario + "", orden);
		
		Componente componente = ComponenteDAO.getComponentePorProyectoYComponenteSigade(proyecto.getId(), componenteSigade!= null ? componenteSigade.getId() : 0);
		
		if (componente == null){
		
			ComponenteTipo componenteTipo = ComponenteTipoDAO.getComponenteTipoPorId(1);
			
			AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
			
			componente = new Componente(acumulacionCosto,componenteSigade,componenteTipo, proyecto, proyecto.getUnidadEjecutora(), nombreComponente
					, descripcion,usuario, null, new Date(), null, 1, null, null, null, null, null, null, null, null, 
					null,null,null,proyecto.getFechaInicio(), proyecto.getFechaFin(),1
					, "d",null,null,1,1,fPrestamo,donacion,nacional,null,null,0,null,null,null,null);
		}else{
			componente.setFuentePrestamo(fPrestamo);
			componente.setFuenteDonacion(donacion);
			componente.setFuenteNacional(nacional);
			componente.setFechaActualizacion(new Date());
			componente.setUsuarioCreo(usuario);
		}
		return ComponenteDAO.guardarComponente(componente, false);
	}
	
	private int existeUnidad(List<Proyecto> proyectos,JsonObject unidad){
		int ejercicio = Utils.String2Int(unidad.get("ejercicio").getAsString()); 
		int entidad = Utils.String2Int(unidad.get("entidad").getAsString());
		int id = Utils.String2Int(unidad.get("id").getAsString());
		
		int x = -1;
		for (Proyecto proyecto : proyectos){
			x++;
			if (proyecto.getUnidadEjecutora().getId().getEjercicio() == ejercicio &&
					proyecto.getUnidadEjecutora().getId().getEntidadentidad()   == entidad &&
					proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() ==  id)
				return x;

		}
		return -1;
	}
	
	private boolean guardarComponentesSigade(String codigoPresupuestario,String usuario,int existeData){
		boolean ret = true;
		List<?> componentesSigade = DataSigadeDAO.getComponentes(codigoPresupuestario);
		
		for(Object objComponente : componentesSigade){
			
			Object[] componente = (Object[])objComponente;
			ComponenteSigade temp = new ComponenteSigade();
			temp.setCodigoPresupuestario((String)componente[0]);
			temp.setEstado(1);
			temp.setFechaCreacion(new Date());
			temp.setMontoComponente((BigDecimal) componente[4]);
			temp.setNombre((String) componente[2]);
			temp.setNumeroComponente((Integer)componente[1]);
			temp.setUsuaraioCreo(usuario);
			
			ComponenteSigade comp= ComponenteSigadeDAO.getComponenteSigadePorCodigoNumero(temp.getCodigoPresupuestario(), temp.getNumeroComponente());
			if (comp == null )
				ret = ret && ComponenteSigadeDAO.guardarComponenteSigade(temp);
			else if (comp!= null && existeData == 1){
				comp.setMontoComponente(temp.getMontoComponente());
				comp.setFechaActualizacion(new Date());
				comp.setUsuarioActualizo(usuario);
				ret = ret && ComponenteSigadeDAO.guardarComponenteSigade(comp);
			}
			
		}
		return ret;
	}
	
	private boolean guardarComponentes(String codigoPresupuestario, Integer proyectoId,String usuario, Date fechaSuscripcion){
		boolean ret = true;
		Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
		if (proyecto.getProjectCargado() == null || !proyecto.getProjectCargado().equals(1)){
			List<?> componentesSigade = DataSigadeDAO.getComponentes(codigoPresupuestario);
			List<Componente> componentesSipro = ComponenteDAO.getComponentesPorProyecto(proyectoId);
			
			if(componentesSigade!=null && componentesSigade.size()>0){
				for(int i=0; i<componentesSigade.size(); i++){
					Object[] componenteSigade = (Object[]) componentesSigade.get(i);
					if(i < componentesSipro.size() ){
						Componente componente = componentesSipro.get(i);
						componente.setNombre((String)componenteSigade[2]);
						componente.setEsDeSigade(1);
						componente.setUsuarioActualizo(usuario);
						componente.setFechaActualizacion(new Date());
						ret = ret && ComponenteDAO.guardarComponente(componente, false);
					}else{
						ComponenteTipo componenteTipo = ComponenteTipoDAO.getComponenteTipoPorId(1);
						
						int year = new DateTime().getYear();
						UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(year, 0, 0);
						AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
						
						Componente componente = new Componente(acumulacionCosto,null,componenteTipo, proyecto, unidadEjecutora,
								(String)componenteSigade[2], null, usuario, null, new Date(), null, 1, null, null, 
								null, null, null, null, null, null, null,null,null,fechaSuscripcion,fechaSuscripcion,1, 
								null,null,null,1,1,null,null,null,null,null,0,null,null,null,null);
						
						ret = ret && ComponenteDAO.guardarComponente(componente, true);
					}
				}
				
				if (componentesSipro.size() > componentesSigade.size()){
					for (int i = componentesSigade.size(); i< componentesSipro.size() ;i ++){
						Componente componente = componentesSipro.get(i);
						ret = ret && ObjetoDAO.borrarHijos(componente.getTreePath(), 2, usuario);				
					}
				}
			}
		}
		return ret;
	}

}
