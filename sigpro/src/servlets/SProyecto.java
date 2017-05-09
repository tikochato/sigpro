package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.PrestamoDAO;
import dao.ProyectoDAO;
import dao.ProyectoPropiedadDAO;
import dao.ProyectoPropiedadValorDAO;
import pojo.AutorizacionTipo;
import pojo.Cooperante;
import pojo.EjecucionEstado;
import pojo.InteresTipo;
import pojo.ObjetoPrestamo;
import pojo.ObjetoPrestamoId;
import pojo.Prestamo;
import pojo.Proyecto;
import pojo.ProyectoPropedadValor;
import pojo.ProyectoPropedadValorId;
import pojo.ProyectoPropiedad;
import pojo.ProyectoTipo;
import pojo.TipoMoneda;
import pojo.UnidadEjecutora;
import utilities.Utils;

@WebServlet("/SProyecto")
public class SProyecto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class datos {
		int id;
		String nombre;
		String descripcion;
		Long snip;
		int proyectotipoid;
		String proyectotipo;
		String unidadejecutora;
		int unidadejecutoraid;
		String cooperante;
		int cooperanteid;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
		Integer programa;
		Integer subprograma;
		Integer proyecto;
		Integer actividad;
		Integer obra;
		Integer fuente;
		String longitud;
		String latitud;
	};

	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}


    public SProyecto() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
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

		if (accion.equals("getProyectos")) {
			List<Proyecto> proyectos = ProyectoDAO.getProyectos(usuario);

			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

			List <datos> datos_ = new ArrayList<datos>();
			for (Proyecto proyecto : proyectos){
				datos dato = new datos();
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = proyecto.getUnidadEjecutora().getNombre();
				dato.unidadejecutoraid = proyecto.getUnidadEjecutora().getUnidadEjecutora();
				dato.cooperante = proyecto.getCooperante().getNombre();
				dato.cooperanteid = proyecto.getCooperante().getId();
				dato.fechaCreacion = Utils.formatDateHour( proyecto.getFechaCreacion());
				dato.usuarioCreo = proyecto.getUsuarioCreo();
				dato.fechaactualizacion = Utils.formatDateHour( proyecto.getFechaActualizacion());
				dato.usuarioactualizo = proyecto.getUsuarioActualizo();
				dato.programa = proyecto.getPrograma();
				dato.subprograma = proyecto.getSubprograma();
				dato.proyecto = proyecto.getProyecto();
				dato.obra = proyecto.getObra();
				dato.actividad = proyecto.getActividad();
				dato.fuente = proyecto.getFuente();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				datos_.add(dato);
			}

			response_text = new GsonBuilder().serializeNulls().create().toJson(datos_);
			response_text = String.join("", "\"entidades\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");


 
		}else if(accion.equals("getProyectoPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroProyecto = map.get("numeroproyecto")!=null  ? Integer.parseInt(map.get("numeroproyecto")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Proyecto> proyectos = ProyectoDAO.getProyectosPagina(pagina, numeroProyecto,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion,usuario);
			List<datos> datos_=new ArrayList<datos>();
			for (Proyecto proyecto : proyectos){
				datos dato = new datos();
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = proyecto.getUnidadEjecutora().getNombre();
				dato.unidadejecutoraid = proyecto.getUnidadEjecutora().getUnidadEjecutora();
				dato.cooperante = proyecto.getCooperante().getNombre();
				dato.cooperanteid = proyecto.getCooperante().getId();
				dato.fechaCreacion = Utils.formatDateHour( proyecto.getFechaCreacion());
				dato.usuarioCreo = proyecto.getUsuarioCreo();
				dato.fechaactualizacion = Utils.formatDateHour( proyecto.getFechaActualizacion());
				dato.usuarioactualizo = proyecto.getUsuarioActualizo();
				dato.programa = proyecto.getPrograma();
				dato.subprograma = proyecto.getSubprograma();
				dato.proyecto = proyecto.getProyecto();
				dato.obra = proyecto.getObra();
				dato.actividad = proyecto.getActividad();
				dato.fuente = proyecto.getFuente();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				datos_.add(dato);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(datos_);
	        response_text = String.join("", "\"proyectos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("getProyectoPaginaDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroProyecto = map.get("numeroproyecto")!=null  ? Integer.parseInt(map.get("numeroproyecto")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			String idsProyectos = map.get("idsproyectos");
			List<Proyecto> proyectos = ProyectoDAO.getProyectosPaginaDisponibles(pagina, numeroProyecto,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion,idsProyectos);
			List<datos> datos_=new ArrayList<datos>();
			for (Proyecto proyecto : proyectos){
				datos dato = new datos();
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = proyecto.getUnidadEjecutora().getNombre();
				dato.unidadejecutoraid = proyecto.getUnidadEjecutora().getUnidadEjecutora();
				dato.cooperante = proyecto.getCooperante().getNombre();
				dato.cooperanteid = proyecto.getCooperante().getId();
				dato.fechaCreacion = Utils.formatDateHour( proyecto.getFechaCreacion());
				dato.usuarioCreo = proyecto.getUsuarioCreo();
				dato.fechaactualizacion = Utils.formatDateHour( proyecto.getFechaActualizacion());
				dato.usuarioactualizo = proyecto.getUsuarioActualizo();
				dato.programa = proyecto.getPrograma();
				dato.subprograma = proyecto.getSubprograma();
				dato.proyecto = proyecto.getProyecto();
				dato.obra = proyecto.getObra();
				dato.actividad = proyecto.getActividad();
				dato.fuente = proyecto.getFuente();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				datos_.add(dato);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(datos_);
	        response_text = String.join("", "\"proyectos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if (accion.equals("guardar")){
			try{
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Proyecto proyecto;
			if (id>0 || esnuevo){
				String nombre = map.get("nombre");
				Long snip = map.get("snip")!=null ? Long.parseLong(map.get("snip")) : null;
				String descripcion = map.get("descripcion");

				Integer programa = map.get("programa")!=null ? Integer.parseInt(map.get("programa")) : null;
				Integer subPrograma = map.get("subprograma")!=null ?  Integer.parseInt(map.get("subprograma")) : null;
				Integer proyecto_ = map.get("proyecto_")!=null ? Integer.parseInt(map.get("proyecto_")) : null;
				Integer actividad = map.get("actividad")!=null ? Integer.parseInt(map.get("actividad")):null;
				Integer obra = map.get("obra")!=null ? Integer.parseInt(map.get("obra")):null;
				Integer fuente = map.get("fuente")!=null ? Integer.parseInt(map.get("fuente")):null;
				String longitud = map.get("longitud");
				String latitud = map.get("latitud");

				ProyectoTipo proyectoTipo = new ProyectoTipo();
				proyectoTipo.setId(map.get("proyectotipoid") !=null ? Integer.parseInt(map.get("proyectotipoid")): null);

				UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
				unidadEjecutora.setUnidadEjecutora(map.get("unidadejecutoraid")!=null ? Integer.parseInt(map.get("unidadejecutoraid")): null);

				Cooperante cooperante = new Cooperante();
				cooperante.setId(map.get("cooperanteid")!=null ? Integer.parseInt(map.get("cooperanteid")): null);
				
				// prestamo campos requeridos
				int codigoPresupuestario = Utils.String2Int(map.get("codigoPresupuestario"));
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
				
				Date fechaCorte = Utils.dateFromString(map.get("fechaCorte"));
				String destino = map.get("destino");
				String sectorEconomico = map.get("sectorEconomico");
				Date fechaFirma = Utils.dateFromString(map.get("fechaFimra"));
				Integer tipoAutorizacionId = Utils.String2Int(map.get("tipoAutorizacionId"),null);
				String numeroAutorizacion = map.get("numeroAutorizacion");
				Date fechaAutorizacion = Utils.dateFromString(map.get("fechaAutorizacion"));
				Integer aniosPlazo = Utils.String2Int(map.get("aniosPlazo"), null);
				Integer aniosGracia = Utils.String2Int(map.get("aniosGracia"), null);
				Date fechaFinEjecucion = Utils.dateFromString(map.get("fechaFinEjecucion"));
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
				
				int objetoTipo = Utils.String2Int(map.get("objetoTipo"),1);
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
				//Fin prestamo
				
				type = new TypeToken<List<stdatadinamico>>() {
				}.getType();

				List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);

				if(esnuevo){
					proyecto = new Proyecto(cooperante, proyectoTipo, unidadEjecutora, nombre, descripcion
							, usuario, null, new DateTime().toDate(), null, 1, snip
							,programa , subPrograma, proyecto_,actividad, obra, fuente,latitud,longitud
							, null, null, null, null,null,null);

				}else{
					proyecto = ProyectoDAO.getProyectoPorId(id,usuario);
					proyecto.setNombre(nombre);
					proyecto.setDescripcion(descripcion);
					proyecto.setSnip(snip);
					proyecto.setProyectoTipo(proyectoTipo);
					proyecto.setUnidadEjecutora(unidadEjecutora);
					proyecto.setCooperante(cooperante);
					proyecto.setUsuarioActualizo(usuario);
					proyecto.setFechaActualizacion(new DateTime().toDate());
					proyecto.setPrograma(programa);
					proyecto.setSubprograma(subPrograma);
					proyecto.setProyecto(proyecto_);
					proyecto.setActividad(actividad);
					proyecto.setObra(obra);
					proyecto.setFuente(fuente);
					proyecto.setLongitud(longitud);
					proyecto.setLatitud(latitud);

				   List<ProyectoPropedadValor> valores_temp = ProyectoPropiedadValorDAO.getProyectoPropiedadadesValoresPorProyecto(proyecto.getId());

					proyecto.setProyectoPropedadValors(null);
					if (valores_temp!=null){
						for (ProyectoPropedadValor valor : valores_temp){
							valor.setFechaActualizacion(new DateTime().toDate());
							valor.setUsuarioActualizo("admin");
							ProyectoPropiedadValorDAO.eliminarProyectoPropiedadValor(valor);
						}
					}
				}
				result = ProyectoDAO.guardarProyecto(proyecto);
				if (result){
					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
							ProyectoPropiedad proyectoPropiedad = ProyectoPropiedadDAO.getProyectoPropiedadPorId(Integer.parseInt(data.id));
							ProyectoPropedadValorId idValor = new ProyectoPropedadValorId(proyecto.getId(),Integer.parseInt(data.id));
							ProyectoPropedadValor valor = new ProyectoPropedadValor(idValor, proyecto, proyectoPropiedad, usuario, new DateTime().toDate(), 1);
	
							switch (proyectoPropiedad.getDatoTipo().getId()){
								case 1:
									valor.setValorString(data.valor);
									break;
								case 2:
									valor.setValorEntero(Utils.String2Int(data.valor, null));
									break;
								case 3:
									valor.setValorDecimal(Utils.String2BigDecimal(data.valor, null));
									break;
								case 4:
									valor.setValorEntero(Utils.String2Boolean(data.valor, null));
									break;
								case 5:
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									valor.setValorTiempo(data.valor_f.compareTo("")!=0 ? sdf.parse(data.valor_f) : null);
									break;
							}
							result = (result && ProyectoPropiedadValorDAO.guardarProyectoPropiedadValor(valor));
						}
					}
					
					//prestamo
					Prestamo prestamo = null;
					ObjetoPrestamo objetoPrestamo = null;
					
					if (esnuevo){
						prestamo = new Prestamo(autorizacionTipo, ejecucionEstado, interesTipo, tipoMoneda, unidadEjecutora_,
								fechaCorte, codigoPresupuestario, numeroPrestamo, destino, sectorEconomico, fechaFirma, 
								numeroAutorizacion, fechaAutorizacion, aniosPlazo, aniosGracia, fechaFinEjecucion, peridoEjecucion,
								porcentajeInteres, porcentajeComisionCompra, montoContratado, amortizado, porAmortizar,
								principalAnio, interesesAnio, comisionCompromisoAnio, otrosGastos, principalAcumulado, 
								interesesAcumulados, comisionCompromisoAcumulado, otrosCargosAcumulados,
								presupuestoAsignadoFuncionamiento, prespupuestoAsignadoInversion, 
								presupuestoModificadoFun, presupuestoModificadoInv, presupuestoVigenteFun, 
								presupuestoVigenteInv, prespupuestoDevengadoFun, presupuestoDevengadoInv, 
								presupuestoPagadoFun, presupuestoPagadoInv, saldoCuentas, desembolsadoReal, 
								usuario, null, new Date(), null, 1, proyectoPrograma, 
								fechaDecreto, fechaSuscripcion, fechaElegibilidadUe, fechaCierreOrigianlUe, fechaCierreActualUe, 
								mesesProrrogaUe, montoAsignadoUe, desembolsoAFechaUe, montoPorDesembolsarUe, null,
								fechaVigencia, montoContratadoUsd, montoContratadoQtz, desembolsoAFechaUsd, montoPorDesembolsarUsd, 
								montoAsignadoUeUsd,montoAsignadoUeQtz, desembolsoAFechaUeUsd,
								montoPorDesembolsarUeUsd,cooperanteUe);
						
						ObjetoPrestamoId objetoPrestamoId = new ObjetoPrestamoId(0, proyecto.getId(), objetoTipo);
						objetoPrestamo = new ObjetoPrestamo(objetoPrestamoId, prestamo);
						Set <ObjetoPrestamo> objetoPrestamos = new HashSet<>();
						objetoPrestamos.add(objetoPrestamo);
						PrestamoDAO.guardarPrestamo(prestamo, objetoPrestamo);
					}else{
						prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyecto.getId(), objetoTipo);
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
						prestamo.setFechaActualizacion(new DateTime().toDate());
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
						PrestamoDAO.guardarPrestamo(prestamo, PrestamoDAO.getObjetoPrestamo(prestamo.getId()));
					}
					
					//fin prestamo
				}
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						, "\"id\": " , proyecto.getId().toString() , ","
						, "\"usuarioCreo\": \"" , proyecto.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(proyecto.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , proyecto.getUsuarioActualizo() != null ? proyecto.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(proyecto.getFechaActualizacion()),"\""
						," }");
			}else
				response_text = "{ \"success\": false }";

			}
			catch (Throwable e){
				e.printStackTrace();
				response_text = "{ \"success\": false }";
			}

		}
		else if(accion.equals("borrarProyecto")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(id,usuario);

				List<ProyectoPropedadValor> valores_temp = ProyectoPropiedadValorDAO.getProyectoPropiedadadesValoresPorProyecto(proyecto.getId());
				if (valores_temp!=null){
					for (ProyectoPropedadValor valor : valores_temp){
						valor.setFechaActualizacion(new DateTime().toDate());
						valor.setUsuarioActualizo(usuario);
						ProyectoPropiedadValorDAO.eliminarProyectoPropiedadValor(valor);
					}
				}
				response_text = String.join("","{ \"success\": ",(ProyectoDAO.eliminarProyecto(proyecto) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroProyectos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalproyectos\":",ProyectoDAO.getTotalProyectos(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,usuario).toString()," }");
		}
		else if(accion.equals("numeroProyectosDisponibles")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String idsProyectos = map.get("idsproyectos");
			response_text = String.join("","{ \"success\": true, \"totalproyectos\":",ProyectoDAO.getTotalProyectosDisponibles(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,idsProyectos).toString()," }");
		}
		else if(accion.equals("obtenerProyectoPorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(id,usuario);
			response_text = String.join("","{ \"success\": ",(proyecto!=null && proyecto.getId()!=null ? "true" : "false"),", "
					+ "\"id\": " + (proyecto!=null ? proyecto.getId():"0") +", "
					+ "\"nombre\": \"" + (proyecto!=null ? proyecto.getNombre():"") +"\" }");

		}else if(accion.equals("obtenerProyectosPorPrograma")){
			Integer idPrograma = map.get("idPrograma")!=null ? Integer.parseInt(map.get("idPrograma")) : 0;
			List<Proyecto> proyectos = ProyectoDAO.getProyectosPorPrograma(idPrograma);
			List<datos> datos_=new ArrayList<datos>();
			for (Proyecto proyecto : proyectos){
				datos dato = new datos();
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.descripcion = proyecto.getDescripcion();
				dato.fechaCreacion = Utils.formatDateHour( proyecto.getFechaCreacion());
				dato.usuarioCreo = proyecto.getUsuarioCreo();
				datos_.add(dato);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(datos_);
	        response_text = String.join("", "\"proyectos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

		}
		else{
			response_text = "{ \"success\": false }";
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
