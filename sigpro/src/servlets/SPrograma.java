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

import dao.EjecucionEstadoDAO;
import dao.PrestamoDAO;
import dao.ProgramaDAO;
import dao.ProgramaPropiedadDAO;
import dao.ProgramaPropiedadValorDAO;
import dao.ProgramaProyectoDAO;
import dao.TipoMonedaDAO;
import pojo.AutorizacionTipo;
import pojo.EjecucionEstado;
import pojo.InteresTipo;
import pojo.ObjetoPrestamo;
import pojo.ObjetoPrestamoId;
import pojo.Prestamo;
import pojo.Programa;
import pojo.ProgramaPropiedad;
import pojo.ProgramaPropiedadValor;
import pojo.ProgramaPropiedadValorId;
import pojo.ProgramaProyecto;
import pojo.ProgramaProyectoId;
import pojo.ProgramaTipo;
import pojo.Proyecto;
import pojo.TipoMoneda;
import pojo.UnidadEjecutora;
import utilities.Utils;

/**
 * Servlet implementation class SPrograma
 */
@WebServlet("/SPrograma")
public class SPrograma extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stprograma {
		int id;
		String nombre;
		String descripcion;
		int programatipoid;
		String programatipo;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
	};

	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}
    
    public SPrograma() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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

		if (accion.equals("getProgramas")) {
			List<Programa> programas = ProgramaDAO.getProgramas();

			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

			List <stprograma> stprograma_ = new ArrayList<stprograma>();
			for (Programa programa : programas){
				stprograma dato = new stprograma();
				dato.id = programa.getId();
				dato.nombre = programa.getNombre();
				dato.descripcion = programa.getDescripcion();
				dato.programatipoid = programa.getProgramaTipo().getId();
				dato.programatipo = programa.getProgramaTipo().getNombre();
				dato.fechaCreacion = Utils.formatDateHour( programa.getFechaCreacion());
				dato.usuarioCreo = programa.getUsuarioCreo();
				dato.fechaactualizacion = Utils.formatDateHour( programa.getFechaActualizacion());
				dato.usuarioactualizo = programa.getUsuarioActualizo();
				stprograma_.add(dato);
			}

			response_text = new GsonBuilder().serializeNulls().create().toJson(stprograma_);
			response_text = String.join("", "\"programas\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
 
		}else if(accion.equals("getProgramaPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroPrograma = map.get("numeroprograma")!=null  ? Integer.parseInt(map.get("numeroprograma")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Programa> programas = ProgramaDAO.getProgramaPagina(pagina, numeroPrograma,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion,usuario);
			List<stprograma> stprograma_=new ArrayList<stprograma>();
			for (Programa programa : programas){
				stprograma dato = new stprograma();
				dato.id = programa.getId();
				dato.nombre = programa.getNombre();
				dato.descripcion = programa.getDescripcion();
				dato.programatipoid = programa.getProgramaTipo().getId();
				dato.programatipo = programa.getProgramaTipo().getNombre();
				dato.fechaCreacion = Utils.formatDateHour( programa.getFechaCreacion());
				dato.usuarioCreo = programa.getUsuarioCreo();
				dato.fechaactualizacion = Utils.formatDateHour( programa.getFechaActualizacion());
				dato.usuarioactualizo = programa.getUsuarioActualizo();
				stprograma_.add(dato);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stprograma_);
	        response_text = String.join("", "\"programas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}

		else if (accion.equals("guardar")){
			try{
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Programa programa;
			if (id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");

				ProgramaTipo programaTipo = new ProgramaTipo();
				programaTipo.setId(map.get("programatipoid") !=null ? Integer.parseInt(map.get("programatipoid")): null);
				
				//prestamo
				
				Date fechaCorte = Utils.dateFromString(map.get("fechaCorte"));
				int codigoPresupuestario = Utils.String2Int(map.get("codigoPresupuestario"));
				String numeroPrestamo =  map.get("numeroPrestamo"); 
				String destino = map.get("destino");
				String sectorEconomico = map.get("sectorEconomico");
				int unidadEjecutora = Utils.String2Int(map.get("unidadEjecutora"), 0); 
				Date fechaFirma = Utils.dateFromString(map.get("fechaFimra"));
				int tipoAutorizacionId = Utils.String2Int(map.get("tipoAutorizacionId"),0);
				String numeroAutorizacion = map.get("numeroAutorizacion");
				Date fechaAutorizacion = Utils.dateFromString(map.get("fechaAutorizacion"));
				int aniosPlazo = Utils.String2Int(map.get("aniosPlazo"));
				int aniosGracia = Utils.String2Int(map.get("aniosGracia"));
				Date fechaFinEjecucion = Utils.dateFromString(map.get("fechaFinEjecucion"));
				int peridoEjecucion = Utils.String2Int(map.get("periodoEjecucion"));
				int tipoInteresId = Utils.String2Int(map.get("tipoInteresId"));
				BigDecimal porcentajeInteres = Utils.String2BigDecimal(map.get("porcentajeInteres"), null); 
				BigDecimal porcentajeComisionCompra = Utils.String2BigDecimal(map.get("porcentajeComisionCompra"), null);
				int tipoMonedaId = Utils.String2Int(map.get("tipoMonedaId"));
				BigDecimal montoContratado = Utils.String2BigDecimal(map.get("montoContratado"), null);
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
				int ejecucionEstadoId = Utils.String2Int("ejecucionEstadoId",1); //
				String  proyectoPrograma = map.get("proyetoPrograma");
				Date fechaDecreto = Utils.dateFromString(map.get("fechaDecreto"));
				Date fechaSuscripcion = Utils.dateFromString(map.get("fechaSuscripcion"));
				Date fechaElegibilidadUe = Utils.dateFromString(map.get("fechaElegibilidad"));
				Date fechaCierreOrigianlUe = Utils.dateFromString(map.get("fechaCierreOriginal"));
				Date fechaCierreActualUe = Utils.dateFromString(map.get("fechaCierreActual"));
				int mesesProrrogaUe = Utils.String2Int("mesesProrroga");
				int plazoEjecucionUe = Utils.String2Int("plazoEjecucionUe");  
				BigDecimal montoAsignadoUe = Utils.String2BigDecimal(map.get("montoAisignadoUe"), null);
				BigDecimal desembolsoAFechaUe = Utils.String2BigDecimal(map.get("desembolsoAFechaUe"), null);
				BigDecimal montoPorDesembolsarUe = Utils.String2BigDecimal(map.get("montoPorDesembolsarUe"), null);
				
				int objetoId = Utils.String2Int(map.get("objetoId"));
				int objetoTipo = Utils.String2Int(map.get("objetoTipo"));
				
				AutorizacionTipo autorizacionTipo = new AutorizacionTipo();
				autorizacionTipo.setId(tipoAutorizacionId);
				
				EjecucionEstado ejecucionEstado = new EjecucionEstado();
				ejecucionEstado.setId(ejecucionEstadoId);
				InteresTipo interesTipo = new InteresTipo();
				interesTipo.setId(tipoInteresId);
				TipoMoneda tipoMoneda = new TipoMoneda();
				tipoMoneda.setId(tipoMonedaId);
				
				UnidadEjecutora unidadEjecutora_ = new UnidadEjecutora();
				unidadEjecutora_.setUnidadEjecutora(unidadEjecutora);
				
				
				
				

				type = new TypeToken<List<stdatadinamico>>() {
				}.getType();

				List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);

				if(esnuevo){
					programa = new Programa(programaTipo,nombre, descripcion, usuario, null, new Date(), null, 1,  null,null);
					
					Prestamo prestamo = new Prestamo(autorizacionTipo, ejecucionEstado, interesTipo, tipoMoneda, unidadEjecutora_,
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
							mesesProrrogaUe, plazoEjecucionUe, montoAsignadoUe, desembolsoAFechaUe, montoPorDesembolsarUe, null) ;
					
					ObjetoPrestamoId objetoPrestamoId = new ObjetoPrestamoId(1, objetoId, objetoTipo);
					ObjetoPrestamo objetoPrestamo = new ObjetoPrestamo(objetoPrestamoId, prestamo);
					Set <ObjetoPrestamo> objetoPrestamos = new HashSet<>();
					objetoPrestamos.add(objetoPrestamo);
					
					PrestamoDAO.guardarPrestamo(prestamo, objetoPrestamo);
							
				

				}else{
					programa = ProgramaDAO.getProgramaPorId(id);
					programa.setNombre(nombre);
					programa.setDescripcion(descripcion);
					programa.setUsuarioActualizo(usuario);
					programa.setFechaActualizacion(new DateTime().toDate());
					

				   List<ProgramaPropiedadValor> valores_temp = ProgramaPropiedadValorDAO.getProgramaPropiedadadesValoresPorPrograma(programa.getId());

					programa.setProgramaPropiedadValors(null);
					if (valores_temp!=null){
						for (ProgramaPropiedadValor valor : valores_temp){
							valor.setFechaActualizacion(new DateTime().toDate());
							valor.setUsuarioActualizo(usuario);
							ProgramaPropiedadValorDAO.eliminarProgramaPropiedadValor(valor);
						}
					}
					
					Set<ProgramaProyecto> programaproyecto_temp = programa.getProgramaProyectos();
					programa.setProgramaProyectos(null);
					if (programaproyecto_temp!=null){
						for (ProgramaProyecto programaProyecto : programaproyecto_temp){
							ProgramaProyectoDAO.eliminarProgramaProyecto(programaProyecto);
						}
					}
					
				}
				result = ProgramaDAO.guardarPrograma(programa);
				
				
				if (result){
					String[] idsProyectos =  map.get("idsproyectos") != null && map.get("idsproyectos").trim().length()>0 ? map.get("idsproyectos").toString().trim().split(",") : null;
					if (idsProyectos !=null && idsProyectos.length>0){
						for (String idProyecto : idsProyectos){
							ProgramaProyectoId programaProyectodId = new ProgramaProyectoId(programa.getId(), Integer.parseInt(idProyecto));
							Proyecto proyecto = new Proyecto();
							proyecto.setId(Integer.parseInt(idProyecto));
							
							ProgramaProyecto programaProyecto = new ProgramaProyecto(
									programaProyectodId, 
									programa, 
									proyecto, usuario, null, 
									new Date(), null
									, 1);
							
							if (programa.getProgramaProyectos() == null){
								programa.setProgramaProyectos(new HashSet<ProgramaProyecto>(0));
							}
							programa.getProgramaProyectos().add(programaProyecto);
						}
					}
					result = ProgramaDAO.guardarPrograma(programa);
				}
				
				
				
				
				if (result && datos!=null){
					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
							ProgramaPropiedad programaPropiedad = ProgramaPropiedadDAO.getProgramaPropiedadPorId(Integer.parseInt(data.id));
							ProgramaPropiedadValorId idValor = new ProgramaPropiedadValorId(programa.getId(),Integer.parseInt(data.id));
							
							ProgramaPropiedadValor valor = new ProgramaPropiedadValor(idValor, programa, programaPropiedad);
	
							switch (programaPropiedad.getDatoTipo().getId()){
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
							result = (result && ProgramaPropiedadValorDAO.guardarProgramaPropiedadValor(valor));
						}
					}
				}
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + programa.getId() +" }");
			}else
				response_text = "{ \"success\": false }";

			}
			catch (Throwable e){
				response_text = "{ \"success\": false }";
			}

		}
		else if(accion.equals("borrarPrograma")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Programa programa = ProgramaDAO.getProgramaPorId(id);

				List<ProgramaPropiedadValor> valores_temp = ProgramaPropiedadValorDAO.getProgramaPropiedadadesValoresPorPrograma(programa.getId());
				if (valores_temp!=null){
					for (ProgramaPropiedadValor valor : valores_temp){
						valor.setFechaActualizacion(new DateTime().toDate());
						valor.setUsuarioActualizo(usuario);
						ProgramaPropiedadValorDAO.eliminarProgramaPropiedadValor(valor);
					}
				}
				response_text = String.join("","{ \"success\": ",(ProgramaDAO.eliminarPrograma(programa) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroProgramas")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalprogramas\":",ProgramaDAO.getTotalProgramas(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("obtenerProgramaPorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Programa programa = ProgramaDAO.getProgramaPorId(id);
			response_text = String.join("","{ \"success\": ",(programa!=null && programa.getId()!=null ? "true" : "false"),", "
					+ "\"id\": " + (programa!=null ? programa.getId():"") +", "
					+ "\"nombre\": \"" + (programa!=null ? programa.getNombre():"") +"\" }");

		}else if(accion.equals("getTipoMnedaPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;

			List<TipoMoneda> tipoMonedas = TipoMonedaDAO.getAutorizacionTiposPagina(pagina, 0);
			
			List<stprograma> sttipomoneda=new ArrayList<stprograma>();
			for(TipoMoneda tipoMoneda:tipoMonedas){
				stprograma temp =new stprograma();
				temp.id = tipoMoneda.getId();
				temp.nombre = tipoMoneda.getNombre();
				sttipomoneda.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(sttipomoneda);
	        response_text = String.join("", "\"tipoMonedas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		
		}else if(accion.equals("getEjecucionEstadoPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;

			List<EjecucionEstado> ejecucionEstados = EjecucionEstadoDAO.getEjecucionEstadosPagina(pagina, 0);
			
			List<stprograma> sttipomoneda=new ArrayList<stprograma>();
			for(EjecucionEstado tipoMoneda:ejecucionEstados){
				stprograma temp =new stprograma();
				temp.id = tipoMoneda.getId();
				temp.nombre = tipoMoneda.getNombre();
				sttipomoneda.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(sttipomoneda);
	        response_text = String.join("", "\"ejecucionEstados\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		
		}
		
		
		else
		{
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
