package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import dao.SubComponenteDAO;
import dao.SubComponentePropiedadDAO;
import dao.SubComponentePropiedadValorDAO;
import dao.AcumulacionCostoDAO;
import dao.ComponenteDAO;
import dao.ObjetoDAO;
import dao.PagoPlanificadoDAO;
import dao.ProyectoDAO;
import dao.UnidadEjecutoraDAO;
import pojo.AcumulacionCosto;
import pojo.Componente;
import pojo.PagoPlanificado;
import pojo.Proyecto;
import pojo.Subcomponente;
import pojo.SubcomponentePropiedad;
import pojo.SubcomponentePropiedadValor;
import pojo.SubcomponentePropiedadValorId;
import pojo.SubcomponenteTipo;
import pojo.UnidadEjecutora;
import utilities.Utils;

/**
 * Servlet implementation class SSubComponente
 */
@WebServlet("/SSubComponente")
public class SSubComponente extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stsubcomponente{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		Integer subcomponentetipoid;
		String subcomponentetiponombre;
		int estado;
		Long snip;
		Integer programa;
		Integer subprograma;
		Integer proyecto_;
		Integer obra;
		Integer actividad;
		Integer renglon;
		Integer ubicacionGeografica;
		Integer duracion;
		String duracionDimension;
		String fechaInicio;
		String fechaFin;
		Integer unidadejecutoraid;
		Integer ejercicio;
		Integer entidadentidad;
		String unidadejecutoranombre;
		String entidadnombre;
		String latitud;
		String longitud;
		BigDecimal costo;
		Integer acumulacionCostoId;
		String acumulacionCostoNombre;
		boolean tieneHijos;
		String fechaInicioReal;
		String fechaFinReal;
		Integer congelado;
		Integer inversionNueva;
	}

	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}

    
    public SSubComponente() {
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
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getSubComponentesPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroSubComponentes = map.get("numerosubcomponentes")!=null  ? Integer.parseInt(map.get("numerosubcomponentes")) : 0;
			List<Subcomponente> subcomponentes = SubComponenteDAO.getSubComponentesPagina(pagina, numeroSubComponentes,usuario);
			List<stsubcomponente> stsubcomponentes=new ArrayList<stsubcomponente>();
			for(Subcomponente subcomponente:subcomponentes){
				stsubcomponente temp =new stsubcomponente();
				temp.descripcion = subcomponente.getDescripcion();
				temp.estado = subcomponente.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
				temp.id = subcomponente.getId();
				temp.nombre = subcomponente.getNombre();
				temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
				temp.usuarioCreo = subcomponente.getUsuarioCreo();
				temp.subcomponentetipoid = subcomponente.getSubcomponenteTipo().getId();
				temp.subcomponentetiponombre = subcomponente.getSubcomponenteTipo().getNombre();
				temp.snip = subcomponente.getSnip();
				temp.programa = subcomponente.getPrograma();
				temp.subprograma = subcomponente.getSubprograma();
				temp.proyecto_ = subcomponente.getProyecto();
				temp.actividad = subcomponente.getActividad();
				temp.renglon = subcomponente.getRenglon();
				temp.ubicacionGeografica = subcomponente.getUbicacionGeografica();
				temp.duracion = subcomponente.getDuracion();
				temp.duracionDimension = subcomponente.getDuracionDimension();
				temp.fechaInicio = Utils.formatDate(subcomponente.getFechaInicio());
				temp.fechaFin = Utils.formatDate(subcomponente.getFechaFin());
				temp.obra = subcomponente.getObra();
				
				if(subcomponente.getUnidadEjecutora() != null){
					temp.unidadejecutoraid = subcomponente.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = subcomponente.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = subcomponente.getUnidadEjecutora().getId().getEntidadentidad();
					temp.unidadejecutoranombre = subcomponente.getUnidadEjecutora().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(subcomponente.getComponente().getId());
					temp.unidadejecutoraid = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
					temp.unidadejecutoranombre = componente.getUnidadEjecutora().getNombre();
				}
				
				temp.entidadnombre = subcomponente.getUnidadEjecutora().getEntidad().getNombre();
				temp.latitud = subcomponente.getLatitud();
				temp.longitud = subcomponente.getLongitud();
				temp.costo = subcomponente.getCosto();
				temp.acumulacionCostoId = subcomponente.getAcumulacionCosto().getId();
				temp.acumulacionCostoNombre = subcomponente.getAcumulacionCosto().getNombre();
				
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 2);
				temp.fechaInicioReal = Utils.formatDate(subcomponente.getFechaInicioReal());
				temp.fechaFinReal = Utils.formatDate(subcomponente.getFechaFinReal());
				temp.inversionNueva = subcomponente.getInversionNueva();
				stsubcomponentes.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentes);
	        response_text = String.join("", "\"subcomponentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getSubComponentes")){
			List<Subcomponente> subcomponentes = SubComponenteDAO.getSubComponentes(usuario);
			List<stsubcomponente> stsubcomponentes=new ArrayList<stsubcomponente>();
			for(Subcomponente subcomponente:subcomponentes){
				stsubcomponente temp =new stsubcomponente();

				temp.descripcion = subcomponente.getDescripcion();
				temp.estado = subcomponente.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
				temp.id = subcomponente.getId();
				temp.nombre = subcomponente.getNombre();
				temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
				temp.usuarioCreo = subcomponente.getUsuarioCreo();
				temp.subcomponentetipoid = subcomponente.getSubcomponenteTipo().getId();
				temp.subcomponentetiponombre = subcomponente.getSubcomponenteTipo().getNombre();
				temp.snip = subcomponente.getSnip();
				temp.programa = subcomponente.getPrograma();
				temp.subprograma = subcomponente.getSubprograma();
				temp.proyecto_ = subcomponente.getProyecto();
				temp.obra = subcomponente.getObra();
				temp.actividad = subcomponente.getActividad();
				temp.renglon = subcomponente.getRenglon();
				temp.ubicacionGeografica = subcomponente.getUbicacionGeografica();
				
				if(subcomponente.getUnidadEjecutora() != null){
					temp.unidadejecutoraid = subcomponente.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = subcomponente.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = subcomponente.getUnidadEjecutora().getId().getEntidadentidad();
					temp.unidadejecutoranombre = subcomponente.getUnidadEjecutora().getNombre();
					temp.entidadnombre = subcomponente.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(subcomponente.getComponente().getId());
					temp.unidadejecutoraid = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
					temp.unidadejecutoranombre = componente.getUnidadEjecutora().getNombre();
					temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
				}
				
				temp.latitud = subcomponente.getLatitud();
				temp.longitud = subcomponente.getLongitud();
				temp.costo = subcomponente.getCosto();
				temp.acumulacionCostoId = subcomponente.getAcumulacionCosto().getId();
				temp.acumulacionCostoNombre = subcomponente.getAcumulacionCosto().getNombre();
				temp.fechaInicio = Utils.formatDate(subcomponente.getFechaInicio());
				temp.fechaFin = Utils.formatDate(subcomponente.getFechaFin());
				temp.duracion = subcomponente.getDuracion();
				temp.duracionDimension = subcomponente.getDuracionDimension();
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 2);
				temp.fechaInicioReal = Utils.formatDate(subcomponente.getFechaInicioReal());
				temp.fechaFinReal = Utils.formatDate(subcomponente.getFechaFinReal());
				temp.inversionNueva = subcomponente.getInversionNueva();
				
				stsubcomponentes.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentes);
	        response_text = String.join("", "\"subcomponentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarSubComponente")){
			try{
				boolean result = false;
				boolean esnuevo = map.get("esnuevo").equals("true");
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				if(id>0 || esnuevo){
					String nombre = map.get("nombre");
					String descripcion = map.get("descripcion");
					int subcomponentetipoid = map.get("subcomponentetipoid")!=null ? Integer.parseInt(map.get("subcomponentetipoid")) : 0;
					int componenteid= map.get("componenteid")!=null ? Integer.parseInt(map.get("componenteid")) : 0;
					
					Integer unidadEjecutoraId = map.get("unidadejecutoraid") != null ? Integer.parseInt(map.get("unidadejecutoraid")) : null;
					Integer ejercicio = map.get("ejercicio") != null ? map.get("ejercicio") != null ? Utils.String2Int(map.get("ejercicio")) : null : null;
					Integer entidad = map.get("entidad") != null ? map.get("entidad") != null ? Utils.String2Int(map.get("entidad")) : null : null;
					
					Long snip = map.get("snip")!=null ? Long.parseLong(map.get("snip")) : null;
					Integer programa = map.get("programa")!=null ? Integer.parseInt(map.get("programa")) : null;
					Integer subPrograma = map.get("subprograma")!=null ?  Integer.parseInt(map.get("subprograma")) : null;
					Integer proyecto_ = map.get("proyecto_")!=null ? Integer.parseInt(map.get("proyecto_")) : null;
					Integer actividad = map.get("actividad")!=null ? Integer.parseInt(map.get("actividad")):null;
					Integer obra = map.get("obra")!=null ? Integer.parseInt(map.get("obra")):null;
					Integer renglon = map.get("renglon")!=null ? Integer.parseInt(map.get("renglon")):null;
					Integer ubicacionGeografica = map.get("ubicacionGeografica")!=null ? Integer.parseInt(map.get("ubicacionGeografica")):null;
					String latitud = map.get("latitud");
					String longitud = map.get("longitud");
					BigDecimal costo = Utils.String2BigDecimal(map.get("costo") != null && map.get("costo").equals("0") ? null : map.get("costo"), null);
					Integer acumulacionCostoid = Utils.String2Int(map.get("acumulacionCosto"), 3);
					Date fechaInicio = Utils.dateFromStringCeroHoras(map.get("fechaInicio"));
					Date fechaFin = Utils.dateFromStringCeroHoras(map.get("fechaFin"));
					Integer duracion = Utils.String2Int(map.get("duaracion"), null);
					String duracionDimension = map.get("duracionDimension");
					Integer inversionNueva = Utils.String2Int(map.get("inversionNueva"), 0);
					
					AcumulacionCosto acumulacionCosto = null;
					if(acumulacionCostoid != null){
						acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(acumulacionCostoid);
					}
					
					SubcomponenteTipo subcomponenteTipo= new SubcomponenteTipo();
					subcomponenteTipo.setId(subcomponentetipoid);

					UnidadEjecutora unidadEjecutora_ = UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidad, unidadEjecutoraId);
					Componente componente = ComponenteDAO.getComponente(componenteid);

					type = new TypeToken<List<stdatadinamico>>() {
					}.getType();

					List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);

					Subcomponente subcomponente;
					if(esnuevo){
						subcomponente = new Subcomponente(acumulacionCosto, componente, subcomponenteTipo, unidadEjecutora_, 
								nombre, descripcion, usuario, null, new DateTime().toDate(), null, 1, 
								snip, programa, subPrograma, proyecto_, actividad, obra, latitud, longitud, costo, renglon, 
								ubicacionGeografica, fechaInicio, fechaFin, duracion, duracionDimension, null, null, null, 
								null, null, inversionNueva,null,null,null);						
					}
					else{
						subcomponente = SubComponenteDAO.getSubComponentePorId(id,usuario);
						subcomponente.setNombre(nombre);
						subcomponente.setDescripcion(descripcion);
						subcomponente.setUsuarioActualizo(usuario);
						subcomponente.setFechaActualizacion(new DateTime().toDate());
						subcomponente.setSnip(snip);
						subcomponente.setPrograma(programa);
						subcomponente.setSubprograma(subPrograma);
						subcomponente.setProyecto(proyecto_);
						subcomponente.setObra(obra);
						subcomponente.setRenglon(renglon);
						subcomponente.setUbicacionGeografica(ubicacionGeografica);
						subcomponente.setActividad(actividad);
						subcomponente.setLatitud(latitud);
						subcomponente.setLongitud(longitud);
						subcomponente.setCosto(costo);
						subcomponente.setAcumulacionCosto(acumulacionCosto);
						subcomponente.setFechaInicio(fechaInicio);
						subcomponente.setFechaFin(fechaFin);
						subcomponente.setDuracion(duracion);
						subcomponente.setDuracionDimension(duracionDimension);
						subcomponente.setUnidadEjecutora(unidadEjecutora_);
						subcomponente.setSubcomponenteTipo(subcomponenteTipo);
						subcomponente.setInversionNueva(inversionNueva);
					}
					result = SubComponenteDAO.guardarSubComponente(subcomponente, true);
					
					if(result){
						String pagosPlanificados = map.get("pagosPlanificados");
						if(!acumulacionCostoid.equals(2)  || pagosPlanificados!= null && pagosPlanificados.replace("[", "").replace("]", "").length()>0 ){
							List<PagoPlanificado> pagosActuales = PagoPlanificadoDAO.getPagosPlanificadosPorObjeto(subcomponente.getId(), 2);
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
								
								PagoPlanificado pagoPlanificado = new PagoPlanificado(fechaPago, monto, subcomponente.getId(), 2, usuario, null, new Date(), null, 1);
								result = result && PagoPlanificadoDAO.guardar(pagoPlanificado);
							}
						}
					}
					
					Set<SubcomponentePropiedadValor> valores_temp = subcomponente.getSubcomponentePropiedadValors();
					subcomponente.setSubcomponentePropiedadValors(null);
					if (valores_temp!=null){
						for (SubcomponentePropiedadValor valor : valores_temp){
							SubComponentePropiedadValorDAO.eliminarTotalSubComponentePropiedadValor(valor);
						}
					}

					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
							
							SubcomponentePropiedad subcomponentePropiedad = SubComponentePropiedadDAO.getSubComponentePropiedadPorId(Integer.parseInt(data.id));
							SubcomponentePropiedadValorId idValor = new SubcomponentePropiedadValorId(subcomponente.getId(),Integer.parseInt(data.id));
							SubcomponentePropiedadValor valor = new SubcomponentePropiedadValor(idValor, subcomponente, subcomponentePropiedad, usuario, new DateTime().toDate());
	
							switch (subcomponentePropiedad.getDatoTipo().getId()){
								case 1:
									valor.setValorString(data.valor);
									break;
								case 2:
									valor.setValorEntero(Utils.String2Int(data.valor, null));
									break;
								case 3:
									valor.setValorDecimal(Utils.String2BigDecimal(data.valor,null));
									break;
								case 4:
									valor.setValorEntero(Utils.String2Boolean(data.valor, null));
									break;
								case 5:
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									valor.setValorTiempo(data.valor_f.compareTo("")!=0 ? sdf.parse(data.valor_f) : null);
									break;
							}
							result = (result && SubComponentePropiedadValorDAO.guardarSubComponentePropiedadValor(valor));
						}
					}
					
					response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
							+ "\"id\": " + subcomponente.getId() , ","
							, "\"usuarioCreo\": \"" , subcomponente.getUsuarioCreo(),"\","
							, "\"fechaCreacion\":\" " , Utils.formatDateHour(subcomponente.getFechaCreacion()),"\","
							, "\"usuarioactualizo\": \"" , subcomponente.getUsuarioActualizo() != null ? subcomponente.getUsuarioActualizo() : "","\","
							, "\"fechaactualizacion\": \"" , Utils.formatDateHour(subcomponente.getFechaActualizacion()),"\""+
							" }");
				}
				else
					response_text = "{ \"success\": false }";
			}
			catch (Throwable e){
				response_text = "{ \"success\": false }";
			}
		}
		else if(accion.equals("numeroSubComponentes")){
			response_text = String.join("","{ \"success\": true, \"totalsubcomponentes\":",SubComponenteDAO.getTotalSubComponentes(usuario).toString()," }");
		}
		else if(accion.equals("numeroSubComponentesPorComponente")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			int componenteId = map.get("componenteid")!=null  ? Integer.parseInt(map.get("componenteid")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalsubcomponentes\":",SubComponenteDAO.getTotalSubComponentesPorComponente(componenteId, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,usuario).toString()," }");
		}
		else if(accion.equals("getSubComponentesPaginaPorComponente")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int componenteId = map.get("componenteid")!=null  ? Integer.parseInt(map.get("componenteid")) : 0;
			int numeroSubComponentes = map.get("numerosubcomponentes")!=null  ? Integer.parseInt(map.get("numerosubcomponentes")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");

			List<Subcomponente> subcomponentes = SubComponenteDAO.getSubComponentesPaginaPorComponente(pagina, numeroSubComponentes, 
					componenteId, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, 
					orden_direccion, usuario);
			List<stsubcomponente> stsubcomponentes=new ArrayList<stsubcomponente>();
			for(Subcomponente subcomponente:subcomponentes){
				stsubcomponente temp =new stsubcomponente();
				temp.descripcion = subcomponente.getDescripcion();
				temp.estado = subcomponente.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
				temp.id = subcomponente.getId();
				temp.nombre = subcomponente.getNombre();
				temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
				temp.usuarioCreo = subcomponente.getUsuarioCreo();
				temp.subcomponentetipoid = subcomponente.getSubcomponenteTipo().getId();
				temp.subcomponentetiponombre = subcomponente.getSubcomponenteTipo().getNombre();
				temp.snip = subcomponente.getSnip();
				temp.programa = subcomponente.getPrograma();
				temp.subprograma = subcomponente.getSubprograma();
				temp.proyecto_ = subcomponente.getProyecto();
				temp.obra = subcomponente.getObra();
				temp.renglon = subcomponente.getRenglon();
				temp.ubicacionGeografica = subcomponente.getUbicacionGeografica();
				temp.actividad = subcomponente.getActividad();
				
				if(subcomponente.getUnidadEjecutora() != null){
					temp.unidadejecutoraid = subcomponente.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = subcomponente.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = subcomponente.getUnidadEjecutora().getId().getEntidadentidad();
					temp.unidadejecutoranombre = subcomponente.getUnidadEjecutora().getNombre();
					temp.entidadnombre = subcomponente.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(subcomponente.getComponente().getId());
					temp.unidadejecutoraid = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
					temp.ejercicio = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getId().getEjercicio() : null;
					temp.entidadentidad = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getId().getEntidadentidad() : null;
					temp.unidadejecutoranombre = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getNombre() : "";
					temp.entidadnombre = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getEntidad().getNombre() : "";
				}
				
				temp.latitud = subcomponente.getLatitud();
				temp.longitud = subcomponente.getLongitud();
				temp.costo = subcomponente.getCosto();
				temp.acumulacionCostoId = subcomponente.getAcumulacionCosto() != null ? subcomponente.getAcumulacionCosto().getId() : null;
				temp.acumulacionCostoNombre = subcomponente.getAcumulacionCosto() != null ? subcomponente.getAcumulacionCosto().getNombre() : null;
				temp.fechaInicio = Utils.formatDate(subcomponente.getFechaInicio());
				temp.fechaFin = Utils.formatDate(subcomponente.getFechaFin());
				temp.duracion = subcomponente.getDuracion();
				temp.duracionDimension = subcomponente.getDuracionDimension();
				
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 2);
				temp.fechaInicioReal = Utils.formatDate(subcomponente.getFechaInicioReal());
				temp.fechaFinReal = Utils.formatDate(subcomponente.getFechaFinReal());
				temp.inversionNueva = subcomponente.getInversionNueva();
				
				stsubcomponentes.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentes);
	        response_text = String.join("", "\"subcomponentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("obtenerSubComponentePorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Subcomponente subcomponente = SubComponenteDAO.getSubComponentePorId(id,usuario);
			Integer congelado = 0;
			
			if(subcomponente != null){
				Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(subcomponente.getTreePath());
				congelado = proyecto.getCongelado() != null ? proyecto.getCongelado() : 0;
			}
			
			response_text = String.join("","{ \"success\": ",(subcomponente!=null && subcomponente.getId()!=null ? "true" : "false"),", "
				+ "\"id\": " + (subcomponente!=null ? subcomponente.getId():"0") +", " + "\"fechaInicio\": \"" + (subcomponente!=null ? Utils.formatDate(subcomponente.getFechaInicio()): null) +"\", "
				+ "\"prestamoId\": " + (subcomponente!=null ? subcomponente.getComponente().getProyecto().getPrestamo() != null ? subcomponente.getComponente().getProyecto().getPrestamo().getId() : 0 : 0) +", "
				+ "\"ejercicio\": " + (subcomponente!=null ? subcomponente.getComponente().getUnidadEjecutora().getId().getEjercicio() :"0") +", " 
				+ "\"entidad\": " + (subcomponente!=null ? subcomponente.getComponente().getUnidadEjecutora().getId().getEntidadentidad() :"0") +", "
				+ "\"entidadNombre\": \"" + (subcomponente!=null ? subcomponente.getComponente().getUnidadEjecutora().getEntidad().getNombre() : "") +"\", "
				+ "\"unidadEjecutora\": " + (subcomponente!=null ? subcomponente.getComponente().getUnidadEjecutora().getId().getUnidadEjecutora() :"0") +", "
				+ "\"unidadEjecutoraNombre\": \"" + (subcomponente!=null ? subcomponente.getComponente().getUnidadEjecutora().getNombre() : "") +"\", "
				+ "\"congelado\": " + congelado +", "
				+ "\"nombre\": \"" + (subcomponente!=null ? subcomponente.getNombre():"Indefinido") +"\" }");

		}else if(accion.equals("getSubComponentePorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Subcomponente subcomponente = SubComponenteDAO.getSubComponentePorId(id,usuario);
			stsubcomponente temp =new stsubcomponente();
			temp.descripcion = subcomponente.getDescripcion();
			temp.estado = subcomponente.getEstado();
			temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
			temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
			temp.id = subcomponente.getId();
			temp.nombre = subcomponente.getNombre();
			temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
			temp.usuarioCreo = subcomponente.getUsuarioCreo();
			temp.subcomponentetipoid = subcomponente.getSubcomponenteTipo().getId();
			temp.subcomponentetiponombre = subcomponente.getSubcomponenteTipo().getNombre();
			temp.snip = subcomponente.getSnip();
			temp.programa = subcomponente.getPrograma();
			temp.subprograma = subcomponente.getSubprograma();
			temp.proyecto_ = subcomponente.getProyecto();
			temp.obra = subcomponente.getObra();
			temp.renglon = subcomponente.getRenglon();
			temp.ubicacionGeografica = subcomponente.getUbicacionGeografica();
			temp.actividad = subcomponente.getActividad();
			
			if(subcomponente.getUnidadEjecutora() != null){
				temp.unidadejecutoraid = subcomponente.getUnidadEjecutora().getId().getUnidadEjecutora();
				temp.ejercicio = subcomponente.getUnidadEjecutora().getId().getEjercicio();
				temp.entidadentidad = subcomponente.getUnidadEjecutora().getId().getEntidadentidad();
				temp.unidadejecutoranombre = subcomponente.getUnidadEjecutora().getNombre();
				temp.entidadnombre = subcomponente.getUnidadEjecutora().getEntidad().getNombre();
			}else{
				Componente componente = ComponenteDAO.getComponente(subcomponente.getComponente().getId());
				temp.unidadejecutoraid = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				temp.ejercicio = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getId().getEjercicio() : null;
				temp.entidadentidad = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getId().getEntidadentidad() : null;
				temp.unidadejecutoranombre = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getNombre() : "";
				temp.entidadnombre = componente.getUnidadEjecutora()!=null ? componente.getUnidadEjecutora().getEntidad().getNombre() : "";
			}
			
			temp.latitud = subcomponente.getLatitud();
			temp.longitud = subcomponente.getLongitud();
			temp.costo = subcomponente.getCosto();
			temp.acumulacionCostoId = subcomponente.getAcumulacionCosto() != null ? subcomponente.getAcumulacionCosto().getId() : null;
			temp.acumulacionCostoNombre = subcomponente.getAcumulacionCosto() != null ? subcomponente.getAcumulacionCosto().getNombre() : null;
			temp.fechaInicio = Utils.formatDate(subcomponente.getFechaInicio());
			temp.fechaFin = Utils.formatDate(subcomponente.getFechaFin());
			temp.duracion = subcomponente.getDuracion();
			temp.duracionDimension = subcomponente.getDuracionDimension();
			
			temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 2);
			temp.fechaInicioReal = Utils.formatDate(subcomponente.getFechaInicioReal());
			temp.fechaFinReal = Utils.formatDate(subcomponente.getFechaFinReal());
			temp.inversionNueva = subcomponente.getInversionNueva();
			
			Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(subcomponente.getTreePath());
			temp.congelado = proyecto.getCongelado() != null ? proyecto.getCongelado() : 0;
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
	        response_text = String.join("", "\"subcomponente\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

		}else if(accion.equals("borrarSubComponente")){
			Integer subcomponentId = Utils.String2Int(map.get("id"));
			Subcomponente subcomponente = SubComponenteDAO.getSubComponente(subcomponentId);
			
			 response_text = String.join("", "{\"success\":" + ObjetoDAO.borrarHijos(subcomponente.getTreePath(), 2, usuario) + "}");
		}else if(accion.equals("getCantidadHistoria")){
			Integer id = Utils.String2Int(map.get("id"));
			String resultado = SubComponenteDAO.getVersiones(id); 
			response_text = String.join("", "{\"success\":true, \"versiones\": [" + resultado + "]}");
		}else if(accion.equals("getHistoria")){
			Integer id = Utils.String2Int(map.get("id"));
			Integer version = Utils.String2Int(map.get("version"));
			String resultado = SubComponenteDAO.getHistoria(id, version); 
			response_text = String.join("", "{\"success\":true, \"historia\":" + resultado + "}");
		}else if(accion.equals("getValidacionAsignado")){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Integer ejercicio = cal.get(Calendar.YEAR);
			Integer id = Utils.String2Int(map.get("id"));
			
			Subcomponente objSubComponente = SubComponenteDAO.getSubComponente(id);
			Proyecto objProyecto = ProyectoDAO.getProyectobyTreePath(objSubComponente.getTreePath());
			
//			Integer unidadEjecutora = objProyecto.getUnidadEjecutora().getId().getUnidadEjecutora();
			Integer entidad = objProyecto.getUnidadEjecutora().getId().getEntidadentidad();
			
			Integer programa = Utils.String2Int(map.get("programa"));
			Integer subprograma = Utils.String2Int(map.get("subprograma"));
			Integer proyecto = Utils.String2Int(map.get("proyecto"));
			Integer actividad = Utils.String2Int(map.get("actividad"));
			Integer obra = Utils.String2Int(map.get("obra"));
			Integer renglon = Utils.String2Int(map.get("renglon"));
			Integer geografico = Utils.String2Int(map.get("geografico"));
			BigDecimal asignado = ObjetoDAO.getAsignadoPorLineaPresupuestaria(ejercicio, entidad, programa, subprograma, 
					proyecto, actividad, obra, renglon, geografico);
			
			BigDecimal planificado = new BigDecimal(0);
			switch(objSubComponente.getAcumulacionCosto().getId()){
				case 1:
					cal.setTime(objSubComponente.getFechaInicio());
					Integer ejercicioInicial = cal.get(Calendar.YEAR);
					if(ejercicio.equals(ejercicioInicial)){
						planificado = objSubComponente.getCosto();
					}
					break;
				case 2:
					List<PagoPlanificado> lstPagos = PagoPlanificadoDAO.getPagosPlanificadosPorObjeto(objSubComponente.getId(), 2);
					for(PagoPlanificado pago : lstPagos){
						cal.setTime(pago.getFechaPago());
						Integer ejercicioPago = cal.get(Calendar.YEAR);
						if(ejercicio.equals(ejercicioPago)){
							planificado = planificado.add(pago.getPago());
						}
					}
					break;
				case 3:
					cal.setTime(objSubComponente.getFechaFin());
					Integer ejercicioFinal = cal.get(Calendar.YEAR);
					if(ejercicio.equals(ejercicioFinal)){
						planificado = objSubComponente.getCosto();
					}
					break;
			}
			
			if(asignado.subtract(planificado).compareTo(new BigDecimal(0)) == -1){
				response_text = ",\"asignado\": " + asignado + ",\"sobrepaso\": " + true;
			}else{
				response_text = ",\"asignado\": " + asignado + ",\"sobrepaso\": " + false;
			}
			
			response_text = String.join(" ", "{ \"success\" : true ", response_text, "}");
			Utils.writeJSon(response, response_text);
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