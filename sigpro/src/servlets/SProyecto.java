package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

import dao.EntidadDAO;
import dao.EstructuraProyectoDAO;
import dao.Nodo;
import dao.PrestamoDAO;
import dao.ProyectoDAO;
import dao.ProyectoImpactoDAO;
import dao.ProyectoMiembroDAO;
import dao.ProyectoPropiedadDAO;
import dao.ProyectoPropiedadValorDAO;
import dao.UnidadEjecutoraDAO;
import pojo.AcumulacionCosto;
import pojo.Colaborador;
import pojo.Cooperante;
import pojo.Entidad;
import pojo.Prestamo;
import pojo.Proyecto;
import pojo.ProyectoImpacto;
import pojo.ProyectoMiembro;
import pojo.ProyectoMiembroId;
import pojo.ProyectoPropedadValor;
import pojo.ProyectoPropedadValorId;
import pojo.ProyectoPropiedad;
import pojo.ProyectoTipo;
import pojo.UnidadEjecutora;
import utilities.Utils;

@WebServlet("/SProyecto")
public class SProyecto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class datos {
		int id;
		String nombre;
		String objetivo;
		String descripcion;
		Long snip;
		int proyectotipoid;
		String proyectotipo;
		String unidadejecutora;
		Integer unidadejecutoraid;
		Integer entidadentidad;
		String entidadnombre;
		Integer ejercicio;
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
		Integer renglon;
		Integer ubicacionGeografica;
		String longitud;
		String latitud;
		Integer directorProyectoId;
		String directorProyectoNmbre;
		BigDecimal costo;
		Integer acumulacionCosto;
		String acumulacionCostoNombre;
		String objetivoEspecifico;
		String visionGeneral;
		stprestamo prestamo;

	};

	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}

	class stprestamo{
		String fechaCorte;
		Long codigoPresupuestario;
		String numeroPrestamo;
		String destino;
		String sectorEconomico;
		Integer unidadEjecutora;
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
				dato.objetivo = proyecto.getObjetivo();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() :"";
				dato.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				dato.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
				dato.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
				dato.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
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
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				dato.costo = proyecto.getCosto();
				dato.acumulacionCosto = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getId() : null;
				dato.acumulacionCostoNombre = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getNombre() : null;
				dato.objetivoEspecifico = proyecto.getObjetivoEspecifico()!=null ? proyecto.getObjetivoEspecifico() : null;
				dato.visionGeneral = proyecto.getVisionGeneral() !=null ? proyecto.getVisionGeneral() : null;

				dato.directorProyectoId = proyecto.getColaborador() != null ? proyecto.getColaborador().getId() : 0;
				dato.directorProyectoId = proyecto.getColaborador()!= null ? proyecto.getColaborador().getId() : null;
				dato.directorProyectoNmbre = proyecto.getColaborador()!= null ? (proyecto.getColaborador().getPnombre()
										+ " " + proyecto.getColaborador().getSnombre()
										+ " " + proyecto.getColaborador().getPapellido()
										+ " " + proyecto.getColaborador().getSapellido()) : null;
				datos_.add(dato);
			}

			response_text = new GsonBuilder().serializeNulls().create().toJson(datos_);
			response_text = String.join("", "\"entidades\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");



		} else if (accion.equals("getProyectosPorUnidadEjecutora")){
			Integer unidadEjecutoraId = Utils.String2Int(map.get("unidadEjecutoraId"), 0);
			List<Proyecto> proyectos = ProyectoDAO.getProyectosPorUnidadEjecutora(usuario, unidadEjecutoraId);

			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

			List <datos> datos_ = new ArrayList<datos>();
			for (Proyecto proyecto : proyectos){
				datos dato = new datos();
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.objetivo = proyecto.getObjetivo();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() :"";
				dato.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				dato.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
				dato.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
				dato.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
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
				dato.renglon = proyecto.getRenglon();
				dato.ubicacionGeografica =proyecto.getUbicacionGeografica();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				dato.acumulacionCosto = proyecto.getAcumulacionCosto().getId();
				dato.acumulacionCostoNombre = proyecto.getAcumulacionCosto().getNombre();
				dato.objetivoEspecifico = proyecto.getObjetivoEspecifico()!=null ? proyecto.getObjetivoEspecifico() : null;
				dato.visionGeneral = proyecto.getVisionGeneral() !=null ? proyecto.getVisionGeneral() : null;

				dato.directorProyectoId = proyecto.getColaborador()!= null ? proyecto.getColaborador().getId() : null;
				dato.directorProyectoNmbre = proyecto.getColaborador()!= null ? (proyecto.getColaborador().getPnombre()
										+ " " + proyecto.getColaborador().getSnombre()
										+ " " + proyecto.getColaborador().getPapellido()
										+ " " + proyecto.getColaborador().getSapellido()) : null;
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
				dato.objetivo = proyecto.getObjetivo();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() :"";
				dato.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				dato.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
				dato.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
				dato.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
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
				dato.renglon = proyecto.getRenglon();
				dato.ubicacionGeografica =proyecto.getUbicacionGeografica();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				dato.acumulacionCosto = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getId() : null;
				dato.acumulacionCostoNombre = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getNombre() : null;
				dato.objetivoEspecifico = proyecto.getObjetivoEspecifico()!=null ? proyecto.getObjetivoEspecifico() : null;
				dato.visionGeneral = proyecto.getVisionGeneral() !=null ? proyecto.getVisionGeneral() : null;

				dato.directorProyectoId = proyecto.getColaborador()!= null ? proyecto.getColaborador().getId() : null;
				dato.directorProyectoNmbre = proyecto.getColaborador()!= null ? (proyecto.getColaborador().getPnombre()
										+ " " + proyecto.getColaborador().getSnombre()
										+ " " + proyecto.getColaborador().getPapellido()
										+ " " + proyecto.getColaborador().getSapellido()) : null;

				dato.prestamo = obtenerPrestamo(proyecto.getId(),1);
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
				dato.objetivo = proyecto.getObjetivo();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() :"";
				dato.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				dato.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
				dato.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
				dato.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
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
				dato.renglon = proyecto.getRenglon();
				dato.ubicacionGeografica =proyecto.getUbicacionGeografica();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				dato.acumulacionCosto = proyecto.getAcumulacionCosto() !=null ? proyecto.getAcumulacionCosto().getId() : null;
				dato.acumulacionCostoNombre = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getNombre() : null;
				dato.objetivoEspecifico = proyecto.getObjetivoEspecifico()!=null ? proyecto.getObjetivoEspecifico() : null;
				dato.visionGeneral = proyecto.getVisionGeneral() !=null ? proyecto.getVisionGeneral() : null;

				dato.directorProyectoId = proyecto.getColaborador()!= null ? proyecto.getColaborador().getId() : null;
				dato.directorProyectoNmbre = proyecto.getColaborador()!= null ? (proyecto.getColaborador().getPnombre()
										+ " " + proyecto.getColaborador().getSnombre()
										+ " " + proyecto.getColaborador().getPapellido()
										+ " " + proyecto.getColaborador().getSapellido()) : null;
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
				String objetivo = map.get("objetivo");
				String descripcion = map.get("descripcion");
				Integer ejercicio = (map.get("ejercicio")!=null) ? Utils.String2Int(map.get("ejercicio"),-1) : null;

				Integer programa = map.get("programa")!=null ? Integer.parseInt(map.get("programa")) : null;
				Integer subPrograma = map.get("subprograma")!=null ?  Integer.parseInt(map.get("subprograma")) : null;
				Integer proyecto_ = map.get("proyecto_")!=null ? Integer.parseInt(map.get("proyecto_")) : null;
				Integer actividad = map.get("actividad")!=null ? Integer.parseInt(map.get("actividad")):null;
				Integer obra = map.get("obra")!=null ? Integer.parseInt(map.get("obra")):null;
				String longitud = map.get("longitud");
				String latitud = map.get("latitud");
				Integer renglon = map.get("renglon")!=null ? Integer.parseInt(map.get("renglon")):null;
				Integer ubicacionGeografica = map.get("ubicacionGeografica")!=null ? Integer.parseInt(map.get("ubicacionGeografica")):null;
				BigDecimal costo = map.get("costo") != null && map.get("costo").length() > 0 ? new BigDecimal(map.get("costo")) : null;
				String objetivoEspecifico = map.get("objetoivoEspecifico");
				String visionGeneral = map.get("visionGeneral");
				Integer unidad_ejecutora = (map.get("unidadejecutoraid")!=null) ? Utils.String2Int(map.get("unidadejecutoraid")) : null;
				Integer entidad = (map.get("entidadid")!=null) ? Utils.String2Int(map.get("entidadid")) : null;
				Integer ejecucionFisicaReal = Utils.String2Int(map.get("ejecucionFisicaReal"),null);

				AcumulacionCosto acumulacionCosto = null;
				if (map.get("acumulacionCosto")!=null){
					acumulacionCosto = new AcumulacionCosto();
					acumulacionCosto.setId(Utils.String2Int(map.get("acumulacionCosto")));
				}

				String enunciadoAlcance = map.get("enunciadoAlcance");

				ProyectoTipo proyectoTipo = new ProyectoTipo();
				proyectoTipo.setId(map.get("proyectotipoid") !=null ? Integer.parseInt(map.get("proyectotipoid")): null);

				UnidadEjecutora unidadEjecutora = (ejercicio!=null && entidad!=null && unidad_ejecutora!=null) ? UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidad , unidad_ejecutora) :null;

				Cooperante cooperante = new Cooperante();
				cooperante.setId(map.get("cooperanteid")!=null ? Integer.parseInt(map.get("cooperanteid")): null);

				Colaborador directorProyecto = null;
				if (map.get("directorProyecto")!=null && map.get("directorProyecto").length()>0){
					directorProyecto = new Colaborador();
					directorProyecto.setId(map.get("directorProyecto")!=null ? Integer.parseInt(map.get("directorProyecto")): null);
				}


				type = new TypeToken<List<stdatadinamico>>() {
				}.getType();

				List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);

				if(esnuevo){
					proyecto = new Proyecto(acumulacionCosto,directorProyecto, cooperante, proyectoTipo, unidadEjecutora, nombre, descripcion,
							usuario, null, new DateTime().toDate(), null, 1, snip, programa, subPrograma, proyecto_, actividad, obra,latitud,
							longitud, objetivo,enunciadoAlcance, costo, objetivoEspecifico,visionGeneral,renglon, ubicacionGeografica,
							null, null, null, null,
							null, null, null, ejecucionFisicaReal,null, null, null, null,null,null,null,null,null);


				}else{
					proyecto = ProyectoDAO.getProyectoPorId(id,usuario);
					proyecto.setNombre(nombre);
					proyecto.setObjetivo(objetivo);
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
					proyecto.setLongitud(longitud);
					proyecto.setLatitud(latitud);
					proyecto.setColaborador(directorProyecto);
					proyecto.setEnunciadoAlcance(enunciadoAlcance);
					proyecto.setCosto(costo);
					proyecto.setAcumulacionCosto(acumulacionCosto);
					proyecto.setObjetivoEspecifico(objetivoEspecifico);
					proyecto.setVisionGeneral(visionGeneral);
					proyecto.setEjecucionFisicaReal(ejecucionFisicaReal);

				    List<ProyectoPropedadValor> valores_temp = ProyectoPropiedadValorDAO.getProyectoPropiedadadesValoresPorProyecto(proyecto.getId());
					proyecto.setProyectoPropiedadValors(null);
					if (valores_temp!=null){
						for (ProyectoPropedadValor valor : valores_temp){
							valor.setFechaActualizacion(new DateTime().toDate());
							valor.setUsuarioActualizo("admin");
							ProyectoPropiedadValorDAO.eliminarProyectoPropiedadValor(valor);
						}
					}

					List<ProyectoImpacto> impactos_temp = ProyectoImpactoDAO.getProyectoImpactoPorProyecto(proyecto.getId());
					proyecto.setProyectoImpactos(null);
					if (impactos_temp!=null){
						for(ProyectoImpacto pi:impactos_temp)
							ProyectoImpactoDAO.eliminarTotalProyectoImpacto(pi);
					}

					List<ProyectoMiembro> miembros_temp = ProyectoMiembroDAO.getProyectoMiembroPorProyecto(proyecto.getId());
					proyecto.setProyectoMiembros(null);
					if (miembros_temp!=null){
						for(ProyectoMiembro pm:miembros_temp)
							ProyectoMiembroDAO.eliminarProyectoMiembro(pm);
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
				}
				if (result){
					String[] impactos =  map.get("impactos") != null && map.get("impactos").length()>0 ? map.get("impactos").toString().split("~") : null;
					if (impactos !=null && impactos.length>0){
						for (String impacto : impactos){
							String [] temp = impacto.trim().split(",");
							Entidad tentidad = EntidadDAO.getEntidad(Integer.parseInt(temp[0]), ejercicio);
							ProyectoImpacto proyImpacto = new ProyectoImpacto(tentidad, proyecto, temp[1] , 1,  usuario, null, new Date(),null);
							result = ProyectoImpactoDAO.guardarProyectoImpacto(proyImpacto);
						}
					}
				}
				if (result){
					String[] miembroIds =  map.get("miembros") != null && map.get("miembros").length()>0 ? map.get("miembros").toString().split(",") : null;
					if (miembroIds != null && miembroIds.length > 0 ){
						for (String miembroId : miembroIds){
							Colaborador colaborador = new Colaborador();
							colaborador.setId(Utils.String2Int(miembroId));
							ProyectoMiembroId pmId = new ProyectoMiembroId(proyecto.getId(), colaborador.getId());
							ProyectoMiembro proyMiembro = new ProyectoMiembro(pmId, colaborador, proyecto, 1, new Date(), null, usuario, null);
							result = ProyectoMiembroDAO.guardarProyectoMiembro(proyMiembro);

						}

					}
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

		}else

		if (accion.equals("guardarModal")){
			try{
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				Proyecto proyecto;

				String nombre = map.get("nombre");
				Integer ejercicio = (map.get("ejercicio")!=null) ? Utils.String2Int(map.get("ejercicio"),-1) : null;
				Integer unidad_ejecutora = (map.get("unidadejecutoraid")!=null) ? Utils.String2Int(map.get("unidadejecutoraid")) : null;
				Integer entidad = (map.get("entidadid")!=null) ? Utils.String2Int(map.get("entidadid")) : null;

				ProyectoTipo proyectoTipo = new ProyectoTipo();
				proyectoTipo.setId(map.get("proyectotipoid") !=null ? Integer.parseInt(map.get("proyectotipoid")): null);

				UnidadEjecutora unidadEjecutora = (ejercicio!=null && entidad!=null && unidad_ejecutora!=null) ? UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidad , unidad_ejecutora) :null;

				Cooperante cooperante = new Cooperante();
				cooperante.setId(map.get("cooperanteid")!=null ? Integer.parseInt(map.get("cooperanteid")): null);


				proyecto = ProyectoDAO.getProyectoPorId(id,usuario);
				proyecto.setNombre(nombre);
				proyecto.setProyectoTipo(proyectoTipo);
				proyecto.setUnidadEjecutora(unidadEjecutora);
				proyecto.setCooperante(cooperante);
				proyecto.setUsuarioActualizo(usuario);
				proyecto.setFechaActualizacion(new DateTime().toDate());

				ProyectoDAO.guardarProyecto(proyecto);

				datos temp = new datos();
				temp.id = proyecto.getId();
				temp.nombre = proyecto.getNombre();
				temp.proyectotipoid = proyecto.getProyectoTipo().getId();
				temp.proyectotipo = proyecto.getProyectoTipo().getNombre();
				temp.unidadejecutora = proyecto.getUnidadEjecutora().getNombre();
				temp.unidadejecutoraid = proyecto.getUnidadEjecutora().getId().getUnidadEjecutora();
				temp.entidadentidad = proyecto.getUnidadEjecutora().getId().getEntidadentidad();
				temp.ejercicio = proyecto.getUnidadEjecutora().getId().getEjercicio();
				temp.cooperante = proyecto.getCooperante().getNombre();
				temp.cooperanteid = proyecto.getCooperante().getId();

			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
	        response_text = String.join("", "\"proyecto\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");


			}
			catch (Throwable e){
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
				dato.objetivo = proyecto.getObjetivo();
				dato.descripcion = proyecto.getDescripcion();
				dato.fechaCreacion = Utils.formatDateHour( proyecto.getFechaCreacion());
				dato.usuarioCreo = proyecto.getUsuarioCreo();
				datos_.add(dato);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(datos_);
	        response_text = String.join("", "\"proyectos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

		}else if(accion.equals("getProyectoPorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(id,usuario);

			datos dato = new datos();
			if (proyecto!=null){
				dato.id = proyecto.getId();
				dato.nombre = proyecto.getNombre();
				dato.objetivo = proyecto.getObjetivo();
				dato.descripcion = proyecto.getDescripcion();
				dato.snip = proyecto.getSnip();
				dato.proyectotipo = proyecto.getProyectoTipo().getNombre();
				dato.proyectotipoid = proyecto.getProyectoTipo().getId();
				dato.unidadejecutora = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() :"";
				dato.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				dato.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
				dato.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
				dato.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
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
				dato.renglon = proyecto.getRenglon();
				dato.ubicacionGeografica =proyecto.getUbicacionGeografica();
				dato.longitud = proyecto.getLongitud();
				dato.latitud = proyecto.getLatitud();
				dato.acumulacionCosto = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getId() : null;
				dato.acumulacionCostoNombre = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getNombre() : null;
				dato.objetivoEspecifico = proyecto.getObjetivoEspecifico()!=null ? proyecto.getObjetivoEspecifico() : null;
				dato.visionGeneral = proyecto.getVisionGeneral() !=null ? proyecto.getVisionGeneral() : null;

				dato.directorProyectoId = proyecto.getColaborador()!= null ? proyecto.getColaborador().getId() : null;
				dato.directorProyectoNmbre = proyecto.getColaborador()!= null ? (proyecto.getColaborador().getPnombre()
										+ " " + proyecto.getColaborador().getSnombre()
										+ " " + proyecto.getColaborador().getPapellido()
										+ " " + proyecto.getColaborador().getSapellido()) : null;

				dato.prestamo = obtenerPrestamo(proyecto.getId(),1);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(dato);
	        response_text = String.join("", "\"proyecto\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

		}
		else if(accion.equals("controlArbol")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Nodo arbol = EstructuraProyectoDAO.getEstructuraProyectoArbol(id, usuario);
			Nodo root = new Nodo(0, 0, "", 0, new ArrayList<Nodo>(), null, false);
			arbol.parent = root;
			root.children.add(arbol);
			response_text=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(root);
	        response_text = String.join("", "\"proyecto\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("controlArbolTodosProyectos")){
			String pusuario = map.get("usuario");
			ArrayList<Nodo> proyectos = EstructuraProyectoDAO.getEstructuraProyectosArbol(pusuario);
			Nodo root = new Nodo(0, 0, "", 0, new ArrayList<Nodo>(), null, false);
			if(proyectos!=null){
				for(int i=0; i<proyectos.size(); i++){
					proyectos.get(i).parent = root;
				}
				root.children=proyectos;
			}
			response_text=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(root);
	        response_text = String.join("", "\"proyectos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}


	private stprestamo obtenerPrestamo(int objetoId, int objetoTipo){
		stprestamo ret =  null;
		Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(objetoId, objetoTipo);

		if (prestamo !=null){
			ret =  new stprestamo();
			ret.fechaCorte = prestamo.getFechaCorte() == null ? null : Utils.formatDate(prestamo.getFechaCorte());
			ret.codigoPresupuestario = prestamo.getCodigoPresupuestario();
			ret.numeroPrestamo = prestamo.getNumeroPrestamo();
			ret.destino = prestamo.getDestino();
			ret.sectorEconomico = prestamo.getSectorEconomico();
			ret.unidadEjecutora = prestamo.getUnidadEjecutora() !=null ? prestamo.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
			ret.unidadEjecutoraNombre = prestamo.getUnidadEjecutora() !=null ? prestamo.getUnidadEjecutora().getNombre() : null;
			ret.fechaFirma = (prestamo.getFechaFirma() == null ? null : Utils.formatDate(prestamo.getFechaFirma()));
			ret.tipoAutorizacionId = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getId());
			ret.tipoAutorizacionNombre = (prestamo.getAutorizacionTipo() == null ? null : prestamo.getAutorizacionTipo().getNombre());
			ret.numeroAutorizacion = (prestamo.getNumeroAutorizacion() == null ? null: prestamo.getNumeroAutorizacion());
			ret.fechaAutorizacion = prestamo.getFechaAutorizacion() == null ? null : Utils.formatDate(prestamo.getFechaAutorizacion());
			ret.aniosPlazo = (prestamo.getAniosPlazo() == null ? null : prestamo.getAniosPlazo());
			ret.aniosGracia = (prestamo.getAniosGracia() == null ? null : prestamo.getAniosGracia());
			ret.fechaFinEjecucion = prestamo.getFechaFinEjecucion() == null ? null : Utils.formatDate(prestamo.getFechaFinEjecucion());
			ret.periodoEjecucion = (prestamo.getPeridoEjecucion() == null ? null :prestamo.getPeridoEjecucion());
			ret.tipoInteresId = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getId());
			ret.tipoInteresNombre = (prestamo.getInteresTipo() == null ? null : prestamo.getInteresTipo().getNombre());
			ret.porcentajeInteres = prestamo.getPorcentajeInteres();
			ret.porcentajeComisionCompra = prestamo.getPorcentajeComisionCompra();
			ret.tipoMonedaId = prestamo.getTipoMoneda().getId();
			ret.tipoMonedaNombre = prestamo.getTipoMoneda().getNombre();
			ret.montoContratado = prestamo.getMontoContratado();
			ret.amortizado = prestamo.getAmortizado();
			ret.porAmortizar = prestamo.getPorAmortizar();
			ret.principalAnio = prestamo.getPrincipalAnio();
			ret.interesesAnio = prestamo.getInteresesAnio();
			ret.comisionCompromisoAnio = prestamo.getComisionCompromisoAnio();
			ret.otrosGastos = prestamo.getOtrosGastos();
			ret.principalAcumulado = prestamo.getPrincipalAcumulado();
			ret.interesesAcumulados = prestamo.getInteresesAcumulados();
			ret.comisionCompromisoAcumulado = prestamo.getComisionCompromisoAcumulado();
			ret.otrosCargosAcumulados = prestamo.getOtrosCargosAcumulados();
			ret.presupuestoAsignadoFuncionamiento = prestamo.getPresupuestoAsignadoFuncionamiento();
			ret.presupuestoAsignadoInversion = prestamo.getPrespupuestoAsignadoInversion();
			ret.presupuestoModificadoFun = prestamo.getPresupuestoModificadoFuncionamiento();
			ret.presupuestoModificadoInv = prestamo.getPresupuestoModificadoInversion();
			ret.presupuestoVigenteFun = prestamo.getPresupuestoVigenteFuncionamiento();
			ret.presupuestoVigenteInv = prestamo.getPresupuestoVigenteInversion();
			ret.presupuestoDevengadoFun = prestamo.getPrespupuestoDevengadoFuncionamiento();
			ret.presupuestoDevengadoInv = prestamo.getPresupuestoDevengadoInversion();
			ret.presupuestoPagadoFun = prestamo.getPresupuestoPagadoFuncionamiento();
			ret.presupuestoPagadoInv = prestamo.getPresupuestoPagadoInversion();
			ret.saldoCuentas = prestamo.getSaldoCuentas();
			ret.desembolsoReal = prestamo.getSaldoCuentas();
			ret.ejecucionEstadoId = (prestamo.getEjecucionEstado() == null ? null :prestamo.getEjecucionEstado().getId());
			ret.ejecucionEstadoNombre = (prestamo.getEjecucionEstado() == null ? null : prestamo.getEjecucionEstado().getNombre());
			ret.proyectoPrograma = prestamo.getProyectoPrograma();
			ret.fechaDecreto = Utils.formatDate(prestamo.getFechaDecreto());
			ret.fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
			ret.fechaElegibilidadUe = Utils.formatDate(prestamo.getFechaElegibilidadUe());
			ret.fechaCierreOrigianlUe = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
			ret.fechaCierreActualUe = Utils.formatDate(prestamo.getFechaCierreActualUe());
			ret.mesesProrrogaUe = prestamo.getMesesProrrogaUe();
			ret.montoAsignadoUe = prestamo.getMontoAsignadoUe();
			ret.desembolsoAFechaUe = prestamo.getDesembolsoAFechaUe();
			ret.montoPorDesembolsarUe = prestamo.getMontoPorDesembolsarUe();
			ret.fechaVigencia = Utils.formatDate(prestamo.getFechaVigencia());
			ret.montoContratadoUsd = prestamo.getMontoContratadoUsd();
			ret.montoContratadoQtz = prestamo.getMontoContratadoQtz();
			ret.desembolsoAFechaUsd = prestamo.getDesembolsoAFechaUsd();
			ret.montoPorDesembolsarUsd = prestamo.getMontoPorDesembolsarUsd();
			ret.montoAsignadoUeUsd = prestamo.getMontoAsignadoUeUsd();
			ret.montoAsignadoUeQtz = prestamo.getMontoAsignadoUeQtz();
			ret.desembolsoAFechaUeUsd = prestamo.getDesembolsoAFechaUeUsd();
			ret.montoPorDesembolsarUeUsd = prestamo.getMontoPorDesembolsarUeUsd();
			ret.cooperanteid = prestamo.getCooperante().getId();
			ret.cooperantenombre =  prestamo.getCooperante().getSiglas() + " - " + prestamo.getCooperante().getNombre();

			ret.unidadEjecutora = prestamo.getUnidadEjecutora() !=null ? prestamo.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
			ret.unidadEjecutoraNombre = prestamo.getUnidadEjecutora() !=null ? prestamo.getUnidadEjecutora().getNombre() : "";

			ret.usuarioCreo = prestamo.getUsuarioCreo();
			ret.usuarioActualizo = prestamo.getUsuarioActualizo();
			ret.fechaCreacion = Utils.formatDate(prestamo.getFechaCreacion());
			ret.fechaActualizacion = Utils.formatDate(prestamo.getFechaActualizacion());
			ret.nombreEntidadEjecutora = prestamo.getUnidadEjecutora() !=null ? prestamo.getUnidadEjecutora().getEntidad().getNombre() : "";
		}

		return ret;
	}
}
