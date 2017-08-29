package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import java.math.BigDecimal;

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
import pojo.AutorizacionTipo;
import pojo.Cooperante;
import pojo.EjecucionEstado;
import pojo.InteresTipo;
import pojo.ObjetoPrestamo;
import pojo.ObjetoPrestamoId;
import pojo.Prestamo;
import pojo.TipoMoneda;
import pojo.UnidadEjecutora;
import utilities.Utils;


@WebServlet("/SPrestamo")
public class SPrestamo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stprestamo{
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
			
			try{
				Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(objetoId, objetoTipo);
				if (prestamo !=null){
				stprestamo temp =  new stprestamo();
				temp.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
				temp.codigoPresupuestario = prestamo.getCodigoPresupuestario();
				temp.numeroPrestamo = prestamo.getNumeroPrestamo(); 
				temp.destino = prestamo.getDestino();
				temp.sectorEconomico = prestamo.getSectorEconomico();
				temp.unidadEjecutora = prestamo.getUnidadEjecutora().getUnidadEjecutora();
				temp.unidadEjecutoraNombre = prestamo.getUnidadEjecutora().getNombre();
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
				temp.cooperantenombre =  prestamo.getCooperante().getSiglas() + " - " + prestamo.getCooperante().getNombre();
				
				temp.unidadEjecutora = prestamo.getUnidadEjecutora().getUnidadEjecutora();
				temp.unidadEjecutoraNombre = prestamo.getUnidadEjecutora().getNombre();
				
				temp.usuarioCreo = prestamo.getUsuarioCreo();
				temp.usuarioActualizo = prestamo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDate(prestamo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDate(prestamo.getFechaActualizacion());
				temp.nombreEntidadEjecutora = prestamo.getUnidadEjecutora().getEntidad().getNombre();
				
					response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
			        response_text = String.join("", "\"prestamo\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
				}else{
			        response_text = String.join("", "\"prestamo\":null");
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
				}
			}
			catch(Throwable e){
				response_text = "{ \"success\": false }";
			}	
		}else if (accion.equals("gurdarPrestamo")) {
			boolean result = false;
			Integer objetoId = Utils.String2Int(map.get("objetoId"), null);
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),null);
			Long codigoPresupuestario = Utils.String2Long(map.get("codigoPresupuestario"));
			String numeroPrestamo =  map.get("numeroPrestamo"); 
			String  proyectoPrograma = map.get("proyetoPrograma");
			
			int unidadEjecutoraPrestamo = Utils.String2Int(map.get("unidadEjecutora"), 0);
			UnidadEjecutora unidadEjecutora_ = new UnidadEjecutora();
			unidadEjecutora_.setUnidadEjecutora(unidadEjecutoraPrestamo);
			
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
			int mesesProrrogaUe = Utils.String2Int(map.get("mesesProrroga"), null);
			BigDecimal montoAsignadoUe = Utils.String2BigDecimal(map.get("montoAisignadoUe"), null);
			BigDecimal desembolsoAFechaUe = Utils.String2BigDecimal(map.get("desembolsoAFechaUe"), null);
			BigDecimal montoPorDesembolsarUe = Utils.String2BigDecimal(map.get("montoPorDesembolsarUe"), null);
			BigDecimal montoAsignadoUeUsd = Utils.String2BigDecimal(map.get("montoAsignadoUeUsd"), null);
			BigDecimal montoAsignadoUeQtz = Utils.String2BigDecimal(map.get("montoAsignadoUeQtz"), null);
			BigDecimal desembolsoAFechaUeUsd = Utils.String2BigDecimal(map.get("desembolsoAFechaUeUsd"), null);
			BigDecimal montoPorDesembolsarUeUsd = Utils.String2BigDecimal(map.get("montoPorDesembolsarUeUsd"), null);
			
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
			
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(objetoId, objetoTipo);
			ObjetoPrestamo objetoPrestamo = null;
			
			if (prestamo==null){
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
						montoAsignadoUeQtz, desembolsoAFechaUeUsd, montoPorDesembolsarUeUsd, null);
				
				ObjetoPrestamoId objetoPrestamoId = new ObjetoPrestamoId(0, objetoId, objetoTipo);
				objetoPrestamo = new ObjetoPrestamo(objetoPrestamoId, prestamo);
				Set <ObjetoPrestamo> objetoPrestamos = new HashSet<>();
				objetoPrestamos.add(objetoPrestamo);
				result = PrestamoDAO.guardarPrestamo(prestamo, objetoPrestamo);
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
				result = PrestamoDAO.guardarPrestamo(prestamo, PrestamoDAO.getObjetoPrestamo(prestamo.getId()));
			}
			response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
		}else
			response_text = "{ \"success\": false }";
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
