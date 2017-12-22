package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

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

import dao.SubComponenteDAO;
import dao.ObjetoDAO;
import dao.PagoPlanificadoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import dao.SubproductoDAO.EstructuraPojo;
import dao.SubproductoPropiedadDAO;
import dao.SubproductoPropiedadValorDAO;
import dao.SubproductoUsuarioDAO;
import dao.UnidadEjecutoraDAO;
import dao.UsuarioDAO;
import pojo.AcumulacionCosto;
import pojo.PagoPlanificado;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import pojo.SubproductoPropiedad;
import pojo.SubproductoPropiedadValor;
import pojo.SubproductoPropiedadValorId;
import pojo.SubproductoTipo;
import pojo.SubproductoUsuario;
import pojo.SubproductoUsuarioId;
import pojo.UnidadEjecutora;
import pojo.Usuario;
import utilities.Utils;
import utilities.CLogger;

@WebServlet("/SSubproducto")
public class SSubproducto extends HttpServlet {
	
	private static final long serialVersionUID = 1457438583225714402L;
	String usuario ="";
	
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}
	
	static class stsubproducto {
		Integer id;
		Producto producto;
		Integer subProductoTipoId;
		String subProductoTipo;
		Integer unidadEjecutora;
		Integer entidadentidad;
		Integer ejercicio;
		String nombreUnidadEjecutora;
		String entidadnombre;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
		Long snip;
		Integer programa;
		Integer subprograma;
		Integer proyecto;
		Integer actividad;
		Integer obra;
		Integer renglon;
		Integer ubicacionGeografica;
		Integer duracion;
		String duracionDimension;
		String fechaInicio;
		String fechaFin;
		String latitud;
		String longitud;
		BigDecimal costo;
		Integer acumulacionCosto;
		String acumulacionCostoNombre;
		boolean tieneHijos;
		String fechaInicioReal;
		String fechaFinReal;
		String fechaElegibilidad;
		String fechaCierre;
		Integer inversionNueva;
	}
	
	

	public SSubproducto() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession sesionweb = request.getSession();
		usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;

		Map<String, String> parametro = Utils.getParams(request);

		if (parametro.get("accion").compareTo("cargar") == 0) {
			listar(parametro, response);
		} else if (parametro.get("accion").compareTo("guardar") == 0) {
			guardar(parametro, response,request);
		} else if (parametro.get("accion").compareTo("borrar") == 0) {
			eliminar(parametro, response);
		} else if (parametro.get("accion").compareTo("totalElementos") == 0) {
			total(parametro,response);
		} else if (parametro.get("accion").compareTo("listarTipos") == 0) {
			listarTipos(parametro, response);
		} else if (parametro.get("accion").compareTo("listarSubproductos") == 0) {
			listarSubproductos(parametro, response);
		} else if (parametro.get("accion").compareTo("listarComponentes") == 0) {
			listarComponentes(parametro, response);
		} else if (parametro.get("accion").compareTo("obtenerSubproductoPorId") == 0) {
			obtenerSubproductoPorId(parametro, response);
		}else if (parametro.get("accion").compareTo("getSubproductoPorId") == 0) {
			getSubproductoPorId(parametro, response);
		}else if (parametro.get("accion").compareTo("guardarModal") == 0){
			guardarModal(parametro, response,request);
		}else if(parametro.get("accion").equals("getCantidadHistoria")){
			Integer id = Utils.String2Int(parametro.get("id"));
			String resultado = SubproductoDAO.getVersiones(id); 
			String response_text = String.join("", "{\"success\":true, \"versiones\": [" + resultado + "]}");
			Utils.writeJSon(response, response_text);
		}else if(parametro.get("accion").equals("getHistoria")){
			Integer id = Utils.String2Int(parametro.get("id"));
			Integer version = Utils.String2Int(parametro.get("version"));
			String resultado = SubproductoDAO.getHistoria(id, version); 
			String response_text = String.join("", "{\"success\":true, \"historia\":" + resultado + "}");
			Utils.writeJSon(response, response_text);
		}else if(parametro.get("accion").equals("getValidacionAsignado")){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Integer ejercicio = cal.get(Calendar.YEAR);
			Integer id = Utils.String2Int(parametro.get("id"));
			
			Subproducto objSubproducto = SubproductoDAO.getSubproductoPorId(id);
			Proyecto objProyecto = ProyectoDAO.getProyectobyTreePath(objSubproducto.getTreePath());
			
//			Integer unidadEjecutora = objProyecto.getUnidadEjecutora().getId().getUnidadEjecutora();
			Integer entidad = objProyecto.getUnidadEjecutora().getId().getEntidadentidad();
			
			Integer programa = Utils.String2Int(parametro.get("programa"));
			Integer subprograma = Utils.String2Int(parametro.get("subprograma"));
			Integer proyecto = Utils.String2Int(parametro.get("proyecto"));
			Integer actividad = Utils.String2Int(parametro.get("actividad"));
			Integer obra = Utils.String2Int(parametro.get("obra"));
			Integer renglon = Utils.String2Int(parametro.get("renglon"));
			Integer geografico = Utils.String2Int(parametro.get("geografico"));
			BigDecimal asignado = ObjetoDAO.getAsignadoPorLineaPresupuestaria(ejercicio, entidad, programa, subprograma, 
					proyecto, actividad, obra, renglon, geografico);
			
			BigDecimal planificado = new BigDecimal(0);
			switch(objSubproducto.getAcumulacionCosto().getId()){
				case 1:
					cal.setTime(objSubproducto.getFechaInicio());
					Integer ejercicioInicial = cal.get(Calendar.YEAR);
					if(ejercicio.equals(ejercicioInicial)){
						planificado = objSubproducto.getCosto();
					}
					break;
				case 2:
					List<PagoPlanificado> lstPagos = PagoPlanificadoDAO.getPagosPlanificadosPorObjeto(objSubproducto.getId(), 4);
					for(PagoPlanificado pago : lstPagos){
						cal.setTime(pago.getFechaPago());
						Integer ejercicioPago = cal.get(Calendar.YEAR);
						if(ejercicio.equals(ejercicioPago)){
							planificado = planificado.add(pago.getPago());
						}
					}
					break;
				case 3:
					cal.setTime(objSubproducto.getFechaFin());
					Integer ejercicioFinal = cal.get(Calendar.YEAR);
					if(ejercicio.equals(ejercicioFinal)){
						planificado = objSubproducto.getCosto();
					}
					break;
			}
			
			String response_text = "";
			
			if(asignado.subtract(planificado).compareTo(new BigDecimal(0)) == -1){
				response_text = ",\"asignado\": " + asignado + ",\"sobrepaso\": " + true;
			}else{
				response_text = ",\"asignado\": " + asignado + ",\"sobrepaso\": " + false;
			}
			
			response_text = String.join(" ", "{ \"success\" : true ", response_text, "}");
			Utils.writeJSon(response, response_text);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int productoid = Utils.String2Int(parametro.get("productoid"), 0);
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");

		String resultadoJson = "";

		resultadoJson = SubproductoDAO.getJson(pagina, registros,productoid,usuario,filtro_nombre,filtro_usuario_creo
				,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void guardar(Map<String, String> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		String resultadoJson="";
		boolean esnuevo = map.get("esnuevo").equals("true");
		int id = Utils.String2Int(map.get("id"));
		Subproducto subproducto;
		boolean ret = false;
		if (id>0 || esnuevo){
			try{
			String nombre = map.get("nombre");
			String descripcion = map.get("descripcion");

			Integer productoId = Utils.String2Int(map.get("producto"));
			Integer subproductoPadreId = Utils.String2Int(map.get("subproductoPadre"));
			Integer tiposubproductoId = Utils.String2Int(map.get("tiposubproductoid")); 
			Integer unidadEjecutoraId = map.get("unidadEjecutora")!=null ? Utils.String2Int(map.get("unidadEjecutora")) : null;
			Integer entidadId = map.get("entidad")!=null ?  Utils.String2Int(map.get("entidad")) : null;
			Integer ejercicio = map.get("ejercicio")!=null ? Utils.String2Int(map.get("ejercicio")) : null;
			
			Long snip = Utils.String2Long(map.get("snip"), null);
			Integer programa = Utils.String2Int(map.get("programa"), null);
			Integer subprograma = Utils.String2Int(map.get("subprograma"), null);
			Integer proyecto_ = Utils.String2Int(map.get("proyecto_"), null);
			Integer obra = Utils.String2Int(map.get("obra"), null);
			Integer renglon = map.get("renglon")!=null ? Integer.parseInt(map.get("renglon")):null;
			Integer ubicacionGeografica = map.get("ubicacionGeografica")!=null ? Integer.parseInt(map.get("ubicacionGeografica")):null;
			Integer actividad = Utils.String2Int(map.get("actividad"), null);
			String latitud = map.get("latitud");
			String longitud = map.get("longitud");
			BigDecimal costo = new BigDecimal(map.get("costo"));
			Integer acumulacionCostoid = Utils.String2Int(map.get("acumulacionCostoId"), null);
			Date fechaInicio = Utils.dateFromStringCeroHoras(map.get("fechaInicio"));
			Date fechaFin = Utils.dateFromStringCeroHoras(map.get("fechaFin"));
			Integer duracion = Utils.String2Int(map.get("duaracion"), null);
			String duracionDimension = map.get("duracionDimension");
			Integer inversionNueva = Utils.String2Int(map.get("inversionNueva"), 0);
			
			AcumulacionCosto acumulacionCosto = null;
			if(acumulacionCostoid != 0){
				acumulacionCosto = new AcumulacionCosto();
				acumulacionCosto.setId(acumulacionCostoid);
			}
			
			Gson gson = new Gson();
		
			Type type = new TypeToken<List<stdatadinamico>>() {
			}.getType();

			List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);
			Producto producto = ProductoDAO.getProductoPorId(productoId);
			Subproducto subproductoPadre = new Subproducto();
			subproductoPadre.setId(subproductoPadreId);
			SubproductoTipo subproductoTipo = new SubproductoTipo();
			subproductoTipo.setId(tiposubproductoId);
			UnidadEjecutora unidadEjecutora = (ejercicio!=null && entidadId!=null && unidadEjecutoraId!=null) ? UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidadId, unidadEjecutoraId) : null;			
			if (esnuevo){
				
				subproducto = new Subproducto(acumulacionCosto, producto, subproductoTipo, unidadEjecutora, nombre, descripcion, 
						 usuario, null, new DateTime().toDate(), null, 1, snip, programa, subprograma, proyecto_, actividad, 
						 obra, latitud, longitud,costo,renglon, ubicacionGeografica, fechaInicio, fechaFin, duracion, 
						 duracionDimension, null,null, null,null,null,inversionNueva, null,null);
				
			}else{
				subproducto = SubproductoDAO.getSubproductoPorId(id);
				subproducto.setProducto(producto);
				subproducto.setSubproductoTipo(subproductoTipo);
				subproducto.setUnidadEjecutora(unidadEjecutora);
				subproducto.setNombre(nombre);
				subproducto.setDescripcion(descripcion);
				subproducto.setSnip(snip);
				subproducto.setPrograma(programa);
				subproducto.setSubprograma(subprograma);
				subproducto.setProyecto(proyecto_);
				subproducto.setObra(obra);
				subproducto.setActividad(actividad);
				subproducto.setRenglon(renglon);
				subproducto.setUbicacionGeografica(ubicacionGeografica);
				subproducto.setUsuarioActualizo(usuario);
				subproducto.setFechaActualizacion(new DateTime().toDate());
				subproducto.setLatitud(latitud);
				subproducto.setLongitud(longitud);
				subproducto.setCosto(costo);
				subproducto.setAcumulacionCosto(acumulacionCosto);
				subproducto.setFechaInicio(fechaInicio);
				subproducto.setFechaFin(fechaFin);
				subproducto.setDuracion(duracion);
				subproducto.setDuracionDimension(duracionDimension);
				subproducto.setInversionNueva(inversionNueva);
			}
			ret = SubproductoDAO.guardarSubproducto(subproducto, true);
			
			if(ret){
				String pagosPlanificados = map.get("pagosPlanificados");
				if(!acumulacionCostoid.equals(2)  || pagosPlanificados!= null && pagosPlanificados.replace("[", "").replace("]", "").length()>0 ){
					List<PagoPlanificado> pagosActuales = PagoPlanificadoDAO.getPagosPlanificadosPorObjeto(subproducto.getId(), 4);
					for (PagoPlanificado pagoTemp : pagosActuales){
						PagoPlanificadoDAO.eliminarTotalPagoPlanificado(pagoTemp);
					}
				}
					
				if (acumulacionCostoid.equals(2) && pagosPlanificados!= null && pagosPlanificados.replace("[", "").replace("]", "").length()>0){
					JsonParser parser = new JsonParser();
					JsonArray pagosArreglo = parser.parse(pagosPlanificados).getAsJsonArray();
					for(int i=0; i<pagosArreglo.size(); i++){
						JsonObject objeto = pagosArreglo.get(i).getAsJsonObject();
						Date fechaPago = objeto.get("fechaPago").isJsonNull() ? null : Utils.stringToDate(objeto.get("fechaPago").getAsString());
						BigDecimal monto = objeto.get("pago").isJsonNull() ? null : objeto.get("pago").getAsBigDecimal();
						
						PagoPlanificado pagoPlanificado = new PagoPlanificado(fechaPago, monto, subproducto.getId(), 4, usuario, null, new Date(), null, 1);
						ret = ret && PagoPlanificadoDAO.guardar(pagoPlanificado);
					}
				}
			}
			
			if (ret){
				SubproductoUsuarioId subproductoUsuarioId = new SubproductoUsuarioId(subproducto.getId(), usuario);
				Usuario usu = UsuarioDAO.getUsuario(usuario);
				SubproductoUsuario subproductoUsuario =  new SubproductoUsuario(subproductoUsuarioId, subproducto, usu, usuario, null, new DateTime().toDate(),null);
				SubproductoUsuarioDAO.guardarSubproductoUsuario(subproductoUsuario);
				
				for (stdatadinamico data : datos) {
					if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
						SubproductoPropiedad producotPropiedad = SubproductoPropiedadDAO.getSubproductoPropiedadPorId(Integer.parseInt(data.id));
						SubproductoPropiedadValorId idValor = new SubproductoPropiedadValorId(subproducto.getId(),Integer.parseInt(data.id));
						SubproductoPropiedadValor valor = new SubproductoPropiedadValor(idValor, subproducto, producotPropiedad, null, null, null, null, 
								usuario, null, new DateTime().toDate(), null, 1);
	
						switch (producotPropiedad.getDatoTipo().getId()){
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
								break;
							case 5:
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								valor.setValorTiempo(data.valor_f.compareTo("")!=0 ? sdf.parse(data.valor_f) : null);
								break;
						}
						ret = (ret && SubproductoPropiedadValorDAO.guardarSubproductoPropiedadValor(valor));
					}
				}
			}
			
			resultadoJson = String.join("","{ \"success\": ",(ret ? "true" : "false"),", "
					+ "\"id\": " + subproducto.getId().toString(), ","
					, "\"usuarioCreo\": \"" , subproducto.getUsuarioCreo(),"\","
					, "\"fechaCreacion\":\" " , Utils.formatDateHour(subproducto.getFechaCreacion()),"\","
					, "\"usuarioactualizo\": \"" , subproducto.getUsuarioActualizo() != null ? subproducto.getUsuarioActualizo() : "","\","
					, "\"fechaactualizacion\": \"" , Utils.formatDateHour(subproducto.getFechaActualizacion()),"\""
					," }");
			}
			catch (Throwable e){
				CLogger.write_simple("1", SSubproducto.class, e.getMessage());

				resultadoJson = "{ \"success\": false }";
			}
			
		}else {
			resultadoJson = "{ \"success\": false }";
		}
		Utils.writeJSon(response, resultadoJson);
	}

	private void eliminar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		
		Subproducto subproducto = SubproductoDAO.getSubproductoPorId(codigo);
		
		boolean resultado = ObjetoDAO.borrarHijos(subproducto.getTreePath(), 4, usuario);
		
		//SubproductoDAO.eliminar(codigo, usuario);
		
		String resultadoJson = "{\"success\":"+resultado+" }";

		Utils.writeJSon(response, resultadoJson);
		
	}

	private void total(Map<String, String> parametro,HttpServletResponse response) throws IOException {
		int productoid = Utils.String2Int(parametro.get("productoid"), 0);
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		Long total = SubproductoDAO.getTotalSubproductos(productoid,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,usuario);

		String resultadoJson = "{\"success\":true, \"total\":" + total + "}";

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarTipos(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
		
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");

		String resultadoJson = "";

		resultadoJson = SubproductoDAO.getJson(pagina, registros,componenteid,usuario,
				filtro_nombre,filtro_usuario_creo
				,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarSubproductos(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");


		String resultadoJson = "";
		
		List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(pagina, registros,componenteid,
				filtro_nombre,filtro_usuario_creo
				,filtro_fecha_creacion,columna_ordenada,orden_direccion,usuario);
		
		List<stsubproducto> listaSubProducto = new ArrayList<stsubproducto>();
		
		String fechaElegibilidad = null;
		String fechaCierre = null;
		
		if(subproductos!=null && subproductos.size()>0){
			Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(subproductos.get(0).getTreePath());
			if(proyecto!=null){
				fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
				fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
			}
		}
		
		for (Subproducto subproducto : subproductos){
			stsubproducto temp = new stsubproducto();
			temp.id = subproducto.getId();
			temp.producto = subproducto.getProducto();
			temp.nombre = subproducto.getNombre();
			temp.descripcion = subproducto.getDescripcion();
			temp.usuarioCreo = subproducto.getUsuarioCreo();
			temp.usuarioActualizo = subproducto.getUsuarioActualizo();
			temp.fechaCreacion = Utils.formatDate(subproducto.getFechaCreacion());
			temp.fechaActualizacion = Utils.formatDate(subproducto.getFechaActualizacion());
			temp.estado = subproducto.getEstado();
			temp.snip = subproducto.getSnip();
			temp.programa = subproducto.getPrograma();
			temp.subprograma = subproducto.getSubprograma();
			temp.proyecto = subproducto.getProyecto();
			temp.actividad = subproducto.getActividad();
			temp.obra = subproducto.getObra();
			temp.latitud = subproducto.getLatitud();
			temp.longitud = subproducto.getLongitud();
			temp.costo = subproducto.getCosto();
			temp.acumulacionCosto = subproducto.getAcumulacionCosto()!=null ? subproducto.getAcumulacionCosto().getId() : null;
			temp.acumulacionCostoNombre = subproducto.getAcumulacionCosto()!=null ? subproducto.getAcumulacionCosto().getNombre() : null;
			
			if (subproducto.getSubproductoTipo() != null){
				temp.subProductoTipoId = subproducto.getSubproductoTipo().getId();
				temp.subProductoTipo = subproducto.getSubproductoTipo().getNombre();
			}
			
			if (subproducto.getUnidadEjecutora() != null){
				temp.unidadEjecutora = subproducto.getUnidadEjecutora().getId().getUnidadEjecutora();
				temp.nombreUnidadEjecutora = subproducto.getUnidadEjecutora().getNombre();
			}
			else if (subproducto.getProducto().getUnidadEjecutora()!=null){
				temp.unidadEjecutora = subproducto.getProducto().getUnidadEjecutora().getId().getUnidadEjecutora();
				temp.nombreUnidadEjecutora = subproducto.getProducto().getUnidadEjecutora().getNombre();
			}
			
			temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 4);
			
			temp.fechaInicioReal = Utils.formatDate(subproducto.getFechaInicioReal());
			temp.fechaFinReal = Utils.formatDate(subproducto.getFechaFinReal());
						
			temp.fechaElegibilidad = fechaElegibilidad;
			temp.fechaCierre = fechaCierre;
			temp.inversionNueva = subproducto.getInversionNueva();
			
			listaSubProducto.add(temp);
		}
		
		resultadoJson=new GsonBuilder().serializeNulls().create().toJson(listaSubProducto);
		resultadoJson =  "\"subproductos\":" + resultadoJson;

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void listarComponentes(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);

		String resultadoJson = "";
		
		resultadoJson = Utils.getJSonString("subproductos", SubComponenteDAO.getSubComponentesPagina(pagina, registros,usuario));

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void obtenerSubproductoPorId(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		Integer id = parametro.get("id")!=null ? Integer.parseInt(parametro.get("id")) : 0;
		Subproducto subproducto = SubproductoDAO.getSubproductoPorId(id);
		String resultadoJson = "";
		
		resultadoJson = String.join("","{ \"success\": ",(subproducto!=null && subproducto.getId()!=null ? "true" : "false"),", "
			+ "\"id\": " + (subproducto!=null ? subproducto.getId():"0") +", "  + "\"fechaInicio\": \"" + (subproducto!=null ? Utils.formatDate(subproducto.getFechaInicio()): null) +"\", "
			+ "\"fechaFin\": \"" + (subproducto!=null ? Utils.formatDate(subproducto.getFechaInicio()): null) +"\", "
			+ "\"duracion\": " + (subproducto!=null ? subproducto.getDuracion():"null") +", "  
			+ "\"duracionDimension\": " + (subproducto!=null ? subproducto.getDuracionDimension():"null") +", "
			+ "\"nombre\": \"" + (subproducto!=null ? subproducto.getNombre():"Indefinido") +"\" }");
		Utils.writeJSon(response, resultadoJson);	
	}
	
	private void getSubproductoPorId(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		Integer id = parametro.get("id")!=null ? Integer.parseInt(parametro.get("id")) : 0;
		Subproducto subproducto = SubproductoDAO.getSubproductoPorId(id);
		String resultadoJson="";
		if (subproducto!=null){
			EstructuraPojo temp = new EstructuraPojo();
			temp.id = subproducto.getId();
			temp.nombre = subproducto.getNombre();
			temp.descripcion = subproducto.getDescripcion();
			temp.usuarioCreo = subproducto.getUsuarioCreo();
			temp.usuarioActualizo = subproducto.getUsuarioActualizo();
			temp.fechaCreacion = Utils.formatDateHour(subproducto.getFechaCreacion());
			temp.fechaActualizacion = Utils.formatDateHour(subproducto.getFechaActualizacion());
			temp.estado = subproducto.getEstado();
			temp.snip = subproducto.getSnip();
			temp.programa = subproducto.getPrograma();
			temp.subprograma = subproducto.getSubprograma();
			temp.proyecto_ = subproducto.getProyecto();
			temp.actividad = subproducto.getActividad();
			temp.obra = subproducto.getObra();
			temp.renglon = subproducto.getRenglon();
			temp.ubicacionGeografica = subproducto.getUbicacionGeografica();
			temp.latitud = subproducto.getLatitud();
			temp.longitud = subproducto.getLongitud();
			temp.fechaInicio = Utils.formatDate(subproducto.getFechaInicio());
			temp.fechaFin = Utils.formatDate(subproducto.getFechaFin());
			temp.duracion = subproducto.getDuracion();
			temp.duracionDimension = subproducto.getDuracionDimension();
			temp.costo = subproducto.getCosto();
			temp.acumulacionCosto = subproducto.getAcumulacionCosto()!=null ? subproducto.getAcumulacionCosto().getId() : null;
			temp.acumulacionCostoNombre = subproducto.getAcumulacionCosto()!=null ? subproducto.getAcumulacionCosto().getNombre() : null;
			temp.idProducto = subproducto.getProducto().getId();
			temp.ejercicio = subproducto.getUnidadEjecutora().getId().getEjercicio(); 
			temp.entidadentidad =  subproducto.getUnidadEjecutora().getId().getEntidadentidad();
			temp.entidadnombre = subproducto.getUnidadEjecutora().getEntidad().getNombre();
			temp.unidadEjecutora = subproducto.getUnidadEjecutora().getId().getUnidadEjecutora();
			temp.nombreUnidadEjecutora = subproducto.getUnidadEjecutora().getNombre();
			
			
			if (subproducto.getSubproductoTipo() != null){
				temp.idSubproductoTipo = subproducto.getSubproductoTipo().getId();
				temp.subProductoTipo = subproducto.getSubproductoTipo().getNombre();
			}
			
			if (subproducto.getUnidadEjecutora() != null){
				temp.unidadEjecutora = subproducto.getUnidadEjecutora().getId().getUnidadEjecutora();
				temp.nombreUnidadEjecutora = subproducto.getUnidadEjecutora().getNombre();
			}
			else if (subproducto.getProducto().getUnidadEjecutora()!=null){
				temp.unidadEjecutora = subproducto.getProducto().getUnidadEjecutora().getId().getUnidadEjecutora();
				temp.nombreUnidadEjecutora = subproducto.getProducto().getUnidadEjecutora().getNombre();
			}
			
			temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 4);
			
			temp.fechaInicioReal = Utils.formatDate(subproducto.getFechaInicioReal());
			temp.fechaFinReal = Utils.formatDate(subproducto.getFechaFinReal());
			
			Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(subproducto.getTreePath());
			temp.congelado = proyecto.getCongelado() != null ? proyecto.getCongelado() : 0;
			temp.fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
			temp.fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
			temp.inversionNueva = subproducto.getInversionNueva();
			
			resultadoJson = new GsonBuilder().serializeNulls().create().toJson(temp);
			resultadoJson = "{\"success\":true," +" \"subproducto\": " + resultadoJson + "}";	
		}else{
			resultadoJson = "{ \"success\": false }";
				
		}
		Utils.writeJSon(response, resultadoJson);
	}
	
	private void guardarModal(Map<String, String> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		boolean ret = false;
		String resultadoJson="";
		int id = Utils.String2Int(map.get("id"));
		boolean esnuevo = map.get("esnuevo").equals("true");
		
		Integer productoId = Utils.String2Int(map.get("productoId"));
		
		Subproducto subproducto = null;
		try{
			if(id>0 || esnuevo ){
				String nombre = map.get("nombre");
				Integer tiposubproductoId = Utils.String2Int(map.get("tiposubproductoid")); 
				Integer unidadEjecutoraId = Utils.String2Int(map.get("unidadEjecutora"));
				Integer entidadId = Utils.String2Int(map.get("entidadId"));
				Integer ejercicio = Utils.String2Int(map.get("ejercicio"));
				Date fechaInicio = Utils.dateFromString(map.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechaFin"));
				Integer duracion = Utils.String2Int(map.get("duaracion"), null);
				String duracionDimension = map.get("duracionDimension");
				
				SubproductoTipo subproductoTipo = new SubproductoTipo();
				subproductoTipo.setId(tiposubproductoId);
				UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidadId, unidadEjecutoraId);
				
				if(esnuevo){
					Producto producto = ProductoDAO.getProductoPorId(productoId, usuario);
					subproducto = new Subproducto(producto, subproductoTipo,  nombre, usuario, new Date(), 1, 0,0);
					subproducto.setFechaInicio(fechaInicio);
					subproducto.setFechaFin(fechaFin);
					subproducto.setDuracionDimension(duracionDimension);
					subproducto.setDuracion(duracion);
				}else{
					subproducto = SubproductoDAO.getSubproductoPorId(id);
					if (subproducto!=null){	
						subproducto.setSubproductoTipo(subproductoTipo);
						subproducto.setUnidadEjecutora(unidadEjecutora);
						subproducto.setNombre(nombre);
						subproducto.setFechaInicio(fechaInicio);
						subproducto.setFechaFin(fechaFin);
						subproducto.setDuracion(duracion);
						subproducto.setDuracionDimension(duracionDimension);
					}
				}
				
				ret = SubproductoDAO.guardarSubproducto(subproducto, true);
				
				
				
				stsubproducto temp = new stsubproducto();
				if (ret){
					temp.id = subproducto.getId();
					temp.nombre = subproducto.getNombre();
					temp.subProductoTipoId = subproducto.getSubproductoTipo().getId();
					temp.subProductoTipo = subproducto.getSubproductoTipo().getNombre();
					temp.nombreUnidadEjecutora = subproducto.getUnidadEjecutora().getNombre();
					temp.unidadEjecutora = subproducto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.entidadentidad = subproducto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.ejercicio = subproducto.getUnidadEjecutora().getId().getEjercicio();
					temp.nombreUnidadEjecutora = subproducto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = subproducto.getUnidadEjecutora().getEntidad().getNombre();
					temp.fechaInicio = Utils.formatDate(subproducto.getFechaInicio());
					temp.fechaFin = Utils.formatDate(subproducto.getFechaFin());
					temp.duracion = subproducto.getDuracion();
					temp.duracionDimension = subproducto.getDuracionDimension();
					temp.inversionNueva = subproducto.getInversionNueva();
					resultadoJson = Utils.getJSonString("subproducto", temp);
					resultadoJson = "{\"success\":true," + resultadoJson + "}";
				}else{
					resultadoJson = "{ \"success\": false }";
				}
				
				}
			}
		catch (Throwable e){
			CLogger.write_simple("2", SSubproducto.class, e.getMessage());
			resultadoJson = "{ \"success\": false }";
		}
		Utils.writeJSon(response, resultadoJson);
	}
}