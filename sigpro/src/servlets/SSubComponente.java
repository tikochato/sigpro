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

import dao.SubComponenteDAO;
import dao.SubComponentePropiedadDAO;
import dao.SubComponentePropiedadValorDAO;
import dao.ObjetoDAO;
import dao.ProyectoDAO;
import dao.UnidadEjecutoraDAO;
import pojo.AcumulacionCosto;
import pojo.Componente;
import pojo.ComponentePropiedad;
import pojo.ComponentePropiedadValor;
import pojo.ComponentePropiedadValorId;
import pojo.ComponenteTipo;
import pojo.Proyecto;
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
		BigDecimal costoTecho;
		boolean tieneHijos;
		boolean esDeSigade;
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
			List<Componente> subcomponentes = SubComponenteDAO.getSubComponentesPagina(pagina, numeroSubComponentes,usuario);
			List<stsubcomponente> stsubcomponentes=new ArrayList<stsubcomponente>();
			for(Componente subcomponente:subcomponentes){
				stsubcomponente temp =new stsubcomponente();
				temp.descripcion = subcomponente.getDescripcion();
				temp.estado = subcomponente.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
				temp.id = subcomponente.getId();
				temp.nombre = subcomponente.getNombre();
				temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
				temp.usuarioCreo = subcomponente.getUsuarioCreo();
				temp.subcomponentetipoid = subcomponente.getComponenteTipo().getId();
				temp.subcomponentetiponombre = subcomponente.getComponenteTipo().getNombre();
				temp.snip = subcomponente.getSnip();
				temp.programa = subcomponente.getPrograma();
				temp.subprograma = subcomponente.getSubprograma();
				temp.proyecto_ = subcomponente.getProyecto_1();
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
					Proyecto proyecto = ProyectoDAO.getProyecto(subcomponente.getProyecto().getId());
					temp.unidadejecutoraid = proyecto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = proyecto.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = proyecto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.unidadejecutoranombre = proyecto.getUnidadEjecutora().getNombre();
				}
				
				temp.entidadnombre = subcomponente.getUnidadEjecutora().getEntidad().getNombre();
				temp.latitud = subcomponente.getLatitud();
				temp.longitud = subcomponente.getLongitud();
				temp.costo = subcomponente.getCosto();
				temp.acumulacionCostoId = subcomponente.getAcumulacionCosto().getId();
				temp.acumulacionCostoNombre = subcomponente.getAcumulacionCosto().getNombre();
				
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 2);
				temp.esDeSigade = subcomponente.getEsDeSigade().equals(1);
				
				stsubcomponentes.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentes);
	        response_text = String.join("", "\"subcomponentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getSubComponentes")){
			List<Componente> subcomponentes = SubComponenteDAO.getSubComponentes(usuario);
			List<stsubcomponente> stsubcomponentes=new ArrayList<stsubcomponente>();
			for(Componente subcomponente:subcomponentes){
				stsubcomponente temp =new stsubcomponente();

				temp.descripcion = subcomponente.getDescripcion();
				temp.estado = subcomponente.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
				temp.id = subcomponente.getId();
				temp.nombre = subcomponente.getNombre();
				temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
				temp.usuarioCreo = subcomponente.getUsuarioCreo();
				temp.subcomponentetipoid = subcomponente.getComponenteTipo().getId();
				temp.subcomponentetiponombre = subcomponente.getComponenteTipo().getNombre();
				temp.snip = subcomponente.getSnip();
				temp.programa = subcomponente.getPrograma();
				temp.subprograma = subcomponente.getSubprograma();
				temp.proyecto_ = subcomponente.getProyecto_1();
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
					Proyecto proyecto = ProyectoDAO.getProyecto(subcomponente.getProyecto().getId());
					temp.unidadejecutoraid = proyecto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.ejercicio = proyecto.getUnidadEjecutora().getId().getEjercicio();
					temp.entidadentidad = proyecto.getUnidadEjecutora().getId().getEjercicio();
					temp.unidadejecutoranombre = proyecto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = proyecto.getUnidadEjecutora().getEntidad().getNombre();
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
				temp.esDeSigade = subcomponente.getEsDeSigade().equals(1);
				temp.costoTecho = subcomponente.getCostoTecho();
				
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
					int proyectoid= map.get("proyectoid")!=null ? Integer.parseInt(map.get("proyectoid")) : 0;
					
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
					Integer acumulacionCostoid = Utils.String2Int(map.get("acumulacionCosto"), null);
					Date fechaInicio = Utils.dateFromStringCeroHoras(map.get("fechaInicio"));
					Date fechaFin = Utils.dateFromStringCeroHoras(map.get("fechaFin"));
					Integer duracion = Utils.String2Int(map.get("duaracion"), null);
					String duracionDimension = map.get("duracionDimension");
					Integer esDeSigade = Utils.String2Boolean("true",0);
					BigDecimal costoTecho = Utils.String2BigDecimal(map.get("costoTecho")!= null ? map.get("costoTecho") : null, null);
					
					AcumulacionCosto acumulacionCosto = null;
					if(acumulacionCostoid != null){
						acumulacionCosto = new AcumulacionCosto();
						acumulacionCosto.setId(Utils.String2Int(map.get("acumulacionCosto")));
					}
					
					ComponenteTipo subcomponenteTipo= new ComponenteTipo();
					subcomponenteTipo.setId(subcomponentetipoid);

					UnidadEjecutora unidadEjecutora_ = UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidad, unidadEjecutoraId);
					Proyecto proyecto = ProyectoDAO.getProyecto(proyectoid);

					type = new TypeToken<List<stdatadinamico>>() {
					}.getType();

					List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);

					Componente subcomponente;
					if(esnuevo){
						
						subcomponente = new Componente(acumulacionCosto,subcomponenteTipo, proyecto, unidadEjecutora_, nombre,
								descripcion, usuario, null, new DateTime().toDate(), null, 1,
								snip, programa, subPrograma, proyecto_, actividad,obra, latitud,longitud, costo, renglon, 
								ubicacionGeografica, fechaInicio, fechaFin, duracion, duracionDimension, null,null,null,esDeSigade,costoTecho,
								null,null,null);
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
						subcomponente.setProyecto_1(proyecto_);
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
						subcomponente.setComponenteTipo(subcomponenteTipo);
						subcomponente.setCostoTecho(costoTecho);
					}
					result = SubComponenteDAO.guardarSubComponente(subcomponente, true);
					
					Set<ComponentePropiedadValor> valores_temp = subcomponente.getComponentePropiedadValors();
					subcomponente.setComponentePropiedadValors(null);
					if (valores_temp!=null){
						for (ComponentePropiedadValor valor : valores_temp){
							SubComponentePropiedadValorDAO.eliminarTotalSubComponentePropiedadValor(valor);
						}
					}

					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
							
							ComponentePropiedad subcomponentePropiedad = SubComponentePropiedadDAO.getSubComponentePropiedadPorId(Integer.parseInt(data.id));
							ComponentePropiedadValorId idValor = new ComponentePropiedadValorId(subcomponente.getId(),Integer.parseInt(data.id));
							ComponentePropiedadValor valor = new ComponentePropiedadValor(idValor, subcomponente, subcomponentePropiedad, usuario, new DateTime().toDate());
	
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
			response_text = String.join("","{ \"success\": true, \"totalcomponentes\":",SubComponenteDAO.getTotalSubComponentes(usuario).toString()," }");
		}
		else if(accion.equals("numeroSubComponentesPorProyecto")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalcomponentes\":",SubComponenteDAO.getTotalSubComponentesPorProyecto(proyectoId, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,usuario).toString()," }");
		}
		else if(accion.equals("getSubComponentesPaginaPorProyecto")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			int numeroCooperantes = map.get("numerosubcomponentes")!=null  ? Integer.parseInt(map.get("numerosubcomponentes")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");

			List<Componente> subcomponentes = SubComponenteDAO.getSubComponentesPaginaPorProyecto(pagina, numeroCooperantes,proyectoId
					,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion,usuario);
			List<stsubcomponente> stsubcomponentes=new ArrayList<stsubcomponente>();
			for(Componente subcomponente:subcomponentes){
				stsubcomponente temp =new stsubcomponente();
				temp.descripcion = subcomponente.getDescripcion();
				temp.estado = subcomponente.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
				temp.id = subcomponente.getId();
				temp.nombre = subcomponente.getNombre();
				temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
				temp.usuarioCreo = subcomponente.getUsuarioCreo();
				temp.subcomponentetipoid = subcomponente.getComponenteTipo().getId();
				temp.subcomponentetiponombre = subcomponente.getComponenteTipo().getNombre();
				temp.snip = subcomponente.getSnip();
				temp.programa = subcomponente.getPrograma();
				temp.subprograma = subcomponente.getSubprograma();
				temp.proyecto_ = subcomponente.getProyecto_1();
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
					Proyecto proyecto = ProyectoDAO.getProyecto(subcomponente.getProyecto().getId());
					temp.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
					temp.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
					temp.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
					temp.unidadejecutoranombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() : "";
					temp.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
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
				temp.esDeSigade = subcomponente.getEsDeSigade().equals(1);
				temp.costoTecho = subcomponente.getCostoTecho();
				
				stsubcomponentes.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentes);
	        response_text = String.join("", "\"subcomponentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("obtenerSubComponentePorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Componente subcomponente = SubComponenteDAO.getSubComponentePorId(id,usuario);

			response_text = String.join("","{ \"success\": ",(subcomponente!=null && subcomponente.getId()!=null ? "true" : "false"),", "
				+ "\"id\": " + (subcomponente!=null ? subcomponente.getId():"0") +", " + "\"fechaInicio\": \"" + (subcomponente!=null ? Utils.formatDate(subcomponente.getFechaInicio()): null) +"\", "
				+ "\"nombre\": \"" + (subcomponente!=null ? subcomponente.getNombre():"Indefinido") +"\" }");

		}else if(accion.equals("getSubComponentePorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Componente subcomponente = SubComponenteDAO.getSubComponentePorId(id,usuario);
			stsubcomponente temp =new stsubcomponente();
			temp.descripcion = subcomponente.getDescripcion();
			temp.estado = subcomponente.getEstado();
			temp.fechaActualizacion = Utils.formatDateHour(subcomponente.getFechaActualizacion());
			temp.fechaCreacion = Utils.formatDateHour(subcomponente.getFechaCreacion());
			temp.id = subcomponente.getId();
			temp.nombre = subcomponente.getNombre();
			temp.usuarioActualizo = subcomponente.getUsuarioActualizo();
			temp.usuarioCreo = subcomponente.getUsuarioCreo();
			temp.subcomponentetipoid = subcomponente.getComponenteTipo().getId();
			temp.subcomponentetiponombre = subcomponente.getComponenteTipo().getNombre();
			temp.snip = subcomponente.getSnip();
			temp.programa = subcomponente.getPrograma();
			temp.subprograma = subcomponente.getSubprograma();
			temp.proyecto_ = subcomponente.getProyecto_1();
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
				Proyecto proyecto = ProyectoDAO.getProyecto(subcomponente.getProyecto().getId());
				temp.unidadejecutoraid = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getUnidadEjecutora() : null;
				temp.ejercicio = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEjercicio() : null;
				temp.entidadentidad = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getId().getEntidadentidad() : null;
				temp.unidadejecutoranombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getNombre() : "";
				temp.entidadnombre = (proyecto.getUnidadEjecutora()!=null) ? proyecto.getUnidadEjecutora().getEntidad().getNombre() : "";
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
			temp.esDeSigade = subcomponente.getEsDeSigade().equals(1);
			temp.costoTecho = subcomponente.getCostoTecho();
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
	        response_text = String.join("", "\"subcomponente\":",response_text);
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