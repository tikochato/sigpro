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

import dao.ActividadDAO;
import dao.ActividadPropiedadDAO;
import dao.ActividadPropiedadValorDAO;
import dao.AsignacionRaciDAO;
import dao.ObjetoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.ActividadPropiedad;
import pojo.ActividadPropiedadValor;
import pojo.ActividadPropiedadValorId;
import pojo.ActividadTipo;
import pojo.AcumulacionCosto;
import pojo.AsignacionRaci;
import pojo.Colaborador;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.Utils;

@WebServlet("/SActividad")
public class SActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stactividad{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		String fechaInicio;
		String fechaFin;
		Integer actividadtipoid;
		String actividadtiponombre;
		Integer porcentajeavance;
		Integer programa;
		Integer subprograma;
		Integer proyecto;
		Integer actividad;
		Integer obra;
		Integer renglon;
		Integer ubicacionGeografica;
		String longitud;
		String latitud;
		Integer prececesorId;
		Integer predecesorTipo;
		Integer duracion;
		String duracionDimension;
		BigDecimal costo;
		Integer acumulacionCostoId;
		String acumulacionCostoNombre;
		BigDecimal presupuestoModificado;
		BigDecimal presupuestoPagado;
		BigDecimal presupuestoVigente;
		BigDecimal presupuestoDevengado;
		Integer avanceFinanciero;
		int estado;
		Integer proyectoBase;
		boolean tieneHijos;
		String fechaInicioReal;
		String fechaFinReal;
		Integer congelado;
		String fechaElegibilidad;
		String fechaCierre;
	}

	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}
	
	class stasignacionroles {
		Integer id;
		String nombre;
		String rol;
		String nombrerol;
	}


    public SActividad() {
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
		};
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getActividadsPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroActividades = map.get("numeroactividades")!=null  ? Integer.parseInt(map.get("numeroactividades")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Actividad> actividads = ActividadDAO.getActividadsPagina(pagina, numeroActividades, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,
					columna_ordenada, orden_direccion,usuario);
			List<stactividad> stactividads=new ArrayList<stactividad>();
			
			
			int congelado = 0;
			String fechaElegibilidad = null;
			String fechaCierre = null;
			
			if(actividads!=null && actividads.size()>0){
				Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(actividads.get(0).getTreePath());
				if(proyecto!=null){
					congelado = proyecto.getCongelado()!=null?proyecto.getCongelado():0;
					fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
					fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
				}
			}
			
			for(Actividad actividad:actividads){
				stactividad temp =new stactividad();
				temp.descripcion = actividad.getDescripcion();
				temp.estado = actividad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(actividad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(actividad.getFechaCreacion());
				temp.id = actividad.getId();
				temp.nombre = actividad.getNombre();
				temp.usuarioActualizo = actividad.getUsuarioActualizo();
				temp.usuarioCreo = actividad.getUsuarioCreo();
				temp.actividadtipoid = actividad.getActividadTipo().getId();
				temp.actividadtiponombre = actividad.getActividadTipo().getNombre();
				temp.porcentajeavance = actividad.getPorcentajeAvance();
				temp.programa = actividad.getPrograma();
				temp.subprograma = actividad.getSubprograma();
				temp.proyecto = actividad.getProyecto();
				temp.actividad = actividad.getActividad();
				temp.obra = actividad.getObra();
				temp.renglon = actividad.getRenglon();
				temp.ubicacionGeografica = actividad.getUbicacionGeografica();
				temp.longitud = actividad.getLongitud();
				temp.latitud = actividad.getLatitud();
				temp.costo = actividad.getCosto();
				temp.acumulacionCostoId = actividad.getAcumulacionCosto().getId();
				temp.acumulacionCostoNombre = actividad.getAcumulacionCosto().getNombre();
				temp.proyectoBase = actividad.getProyectoBase();
				temp.fechaInicioReal = Utils.formatDate(actividad.getFechaInicioReal());
				temp.fechaFinReal = Utils.formatDate(actividad.getFechaFinReal());
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 5);
				
				temp.congelado = congelado;
				temp.fechaElegibilidad = fechaElegibilidad;
				temp.fechaCierre = fechaCierre;
				
				stactividads.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividads);
	        response_text = String.join("", "\"actividads\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getActividads")){
			List<Actividad> actividads = ActividadDAO.getActividads(usuario);
			List<stactividad> stactividads=new ArrayList<stactividad>();
			
			int congelado = 0;
			String fechaElegibilidad = null;
			String fechaCierre = null;
			
			if(actividads!=null && actividads.size()>0){
				Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(actividads.get(0).getTreePath());
				if(proyecto!=null){
					congelado = proyecto.getCongelado()!=null?proyecto.getCongelado():0;
					fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
					fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
				}
			}
			
			for(Actividad actividad:actividads){
				stactividad temp =new stactividad();
				temp.descripcion = actividad.getDescripcion();
				temp.estado = actividad.getEstado();
				temp.fechaActualizacion = Utils.formatDate(actividad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(actividad.getFechaCreacion());
				temp.id = actividad.getId();
				temp.nombre = actividad.getNombre();
				temp.usuarioActualizo = actividad.getUsuarioActualizo();
				temp.usuarioCreo = actividad.getUsuarioCreo();
				temp.actividadtipoid = actividad.getActividadTipo().getId();
				temp.actividadtiponombre = actividad.getActividadTipo().getNombre();
				temp.porcentajeavance = actividad.getPorcentajeAvance();
				temp.programa = actividad.getPrograma();
				temp.subprograma = actividad.getSubprograma();
				temp.proyecto = actividad.getProyecto();
				temp.actividad = actividad.getActividad();
				temp.obra = actividad.getObra();
				temp.renglon = actividad.getRenglon();
				temp.ubicacionGeografica = actividad.getUbicacionGeografica();
				temp.longitud = actividad.getLongitud();
				temp.latitud = actividad.getLatitud();
				temp.costo = actividad.getCosto();
				temp.acumulacionCostoId = actividad.getAcumulacionCosto().getId();
				temp.acumulacionCostoNombre = actividad.getAcumulacionCosto().getNombre();
				temp.fechaInicio = Utils.formatDate(actividad.getFechaInicio());
				temp.fechaFin = Utils.formatDate(actividad.getFechaFin());
				temp.proyectoBase = actividad.getProyectoBase();
				temp.fechaInicioReal = actividad.getFechaInicioReal() != null ? Utils.formatDate(actividad.getFechaInicioReal()) : null;
				temp.fechaFinReal = actividad.getFechaFinReal() != null ? Utils.formatDate(actividad.getFechaFinReal()) : null;
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 5);

				temp.congelado = congelado;
				temp.fechaElegibilidad = fechaElegibilidad;
				temp.fechaCierre = fechaCierre;;
				stactividads.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividads);
	        response_text = String.join("", "\"actividads\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarActividad")){
			try{
				boolean result = false;
				boolean esnuevo = map.get("esnuevo").equals("true");
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				if(id>0 || esnuevo){
					Integer proyectoBase = 0;
					Integer componenteBase = 0;
					Integer productoBase = 0;
					String nombre = map.get("nombre");
					String descripcion = map.get("descripcion");
					int actividadtipoid =Utils.getParameterInteger(map, "actividadtipoid");
					Date fechaInicio = Utils.dateFromStringCeroHoras(map.get("fechainicio"));
					Date fechaFin = Utils.dateFromStringCeroHoras(map.get("fechafin"));
					
					Long snip = Utils.String2Long(map.get("snip"));
					Integer programa= Utils.getParameterInteger(map, "programa");
					Integer subprograma= Utils.getParameterInteger(map, "subprograma");
					Integer proyecto= Utils.getParameterInteger(map, "proyecto");
					Integer iactividad= Utils.getParameterInteger(map, "actividad");
					Integer obra= Utils.getParameterInteger(map, "obra");
					Integer objetoId = Utils.getParameterInteger(map, "objetoid");
					Integer objetoTipo = Utils.getParameterInteger(map, "objetotipo");
					Integer porcentajeAvance = Utils.getParameterInteger(map, "porcentajeavance");
					Integer duracion = Utils.String2Int(map.get("duaracion"), null);
					String duracionDimension = map.get("duracionDimension");
					String longitud = map.get("longitud");
					BigDecimal costo = Utils.String2BigDecimal(map.get("costo") != null && map.get("costo").equals("0") ? null : map.get("costo"), null);
					String latitud = map.get("latitud");
					Integer acumulacionCostoid = Utils.String2Int(map.get("acumulacionCosto"), null);
					Integer renglon = map.get("renglon")!=null ? Integer.parseInt(map.get("renglon")):null;
					Integer ubicacionGeografica = map.get("ubicacionGeografica")!=null ? Integer.parseInt(map.get("ubicacionGeografica")):null;
					proyectoBase = null;
					componenteBase = null;
                    productoBase = null;
					switch (objetoTipo){
	                    case 1:
	                        proyectoBase = objetoId;
	                        break;
	                    case 2:
	                        componenteBase = objetoId;
	                        break;
	                    case 3:
	                        productoBase = objetoId;
	                        break;
	                    case 4:
	                        Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId);
	                        productoBase = subproducto.getProducto().getId();
	                    break;
	                    case 5:
	                        Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
	                        proyectoBase = actividad.getProyectoBase();
	                        componenteBase = actividad.getComponenteBase();
	                        productoBase = actividad.getProductoBase();
	                        break;
                    }
					
					ActividadTipo actividadTipo= new ActividadTipo();
					actividadTipo.setId(actividadtipoid);
					AcumulacionCosto acumulacionCosto = null;
					if(acumulacionCostoid != 0){
						acumulacionCosto = new AcumulacionCosto();
						acumulacionCosto.setId(acumulacionCostoid);	
					}
					
					type = new TypeToken<List<stdatadinamico>>() {
					}.getType();

					List<stdatadinamico> datos = (map.get("datadinamica")!=null && map.get("datadinamica").compareTo("{}")!=0) ?  gson.fromJson(map.get("datadinamica"), type) : new ArrayList<stdatadinamico>();
					
					Actividad actividad;
					if(esnuevo){
						duracion = (int) ((fechaFin.getTime()-fechaInicio.getTime())/86400000);
						duracionDimension = "d";
						
						Date fechaInicioReal = null;
						Date fechaFinReal = null;
						
						if(porcentajeAvance > 0 && porcentajeAvance < 100)
							fechaInicioReal = new Date();
						else if(porcentajeAvance == 100){
							fechaFinReal = new Date();
							fechaInicioReal = new Date();
						}
						
						actividad = new Actividad(actividadTipo, acumulacionCosto, nombre, descripcion, fechaInicio, fechaFin,
								porcentajeAvance, usuario, null, new Date(), null, 1, snip, programa, subprograma, proyecto, iactividad, obra,
								objetoId,objetoTipo,duracion,duracionDimension,null,null,latitud,longitud,costo,renglon, ubicacionGeografica, null, null,null
								, proyectoBase,componenteBase,productoBase,fechaInicioReal,fechaFinReal,null,null);
					}
					else{
						actividad = ActividadDAO.getActividadPorId(id);
						actividad.setNombre(nombre);
						actividad.setDescripcion(descripcion);
						actividad.setUsuarioActualizo(usuario);
						actividad.setFechaActualizacion(new DateTime().toDate());
						actividad.setFechaInicio(fechaInicio);
						actividad.setFechaFin(fechaFin);
						actividad.setPorcentajeAvance(porcentajeAvance);
						actividad.setPrograma(programa);
						actividad.setSubprograma(subprograma);
						actividad.setProyecto(proyecto);
						actividad.setActividad(iactividad);
						actividad.setObra(obra);
						actividad.setRenglon(renglon);
						actividad.setUbicacionGeografica(ubicacionGeografica);
						actividad.setActividadTipo(actividadTipo);
						actividad.setLatitud(latitud);
						actividad.setLongitud(longitud);
						actividad.setCosto(costo);
						actividad.setAcumulacionCosto(acumulacionCosto);
						actividad.setDuracion(duracion);
						actividad.setDuracionDimension(duracionDimension);
						actividad.setProyectoBase(proyectoBase);
						actividad.setComponenteBase(componenteBase);
						actividad.setProductoBase(productoBase);
						
						if(porcentajeAvance > 0 && porcentajeAvance < 100 && actividad.getFechaInicioReal() == null)
							actividad.setFechaInicioReal(new Date());
						else if(porcentajeAvance == 100 && actividad.getFechaFinReal() == null){
							actividad.setFechaFinReal(new Date());
							
							if(actividad.getFechaInicioReal() == null)
								actividad.setFechaInicioReal(new Date());
						}
					}
					
					result = ActividadDAO.guardarActividad(actividad, true);
					
					if (result){
						List<AsignacionRaci> asignaciones_temp = AsignacionRaciDAO.getAsignacionPorTarea(actividad.getId(), 5, null);
						
						if (asignaciones_temp!=null){
							for (AsignacionRaci asign : asignaciones_temp){
								AsignacionRaciDAO.eliminarTotalAsignacion(asign);
							}
						}
						
						String asignaciones_param = map.get("asignacionroles");
						
						if(!asignaciones_param.equals("")){
							String[] asignaciones = asignaciones_param.split("\\|");
							if (asignaciones.length > 0){
								for (String temp : asignaciones){
									AsignacionRaci asigna_temp = new AsignacionRaci();
									String[] datosaasignacion = temp.split("~");
									Colaborador colaborador = new Colaborador();
									colaborador.setId(Integer.parseInt(datosaasignacion[0]));
									
									asigna_temp.setColaborador(colaborador);
									asigna_temp.setEstado(1);
									asigna_temp.setFechaCreacion(new Date());
									asigna_temp.setObjetoId(actividad.getId());
									asigna_temp.setObjetoTipo(5);
									asigna_temp.setRolRaci(datosaasignacion[1]);
									asigna_temp.setUsuarioCreo(usuario);
									result = result && AsignacionRaciDAO.guardarAsignacion(asigna_temp);
								}
							}
						}
					}

					Set<ActividadPropiedadValor> valores_temp = actividad.getActividadPropiedadValors();
					actividad.setActividadPropiedadValors(null);
					if (valores_temp!=null){
						for (ActividadPropiedadValor valor : valores_temp){
							ActividadPropiedadValorDAO.eliminarTotalActividadPropiedadValor(valor);
						}
					}

					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
							ActividadPropiedad actividadPropiedad = ActividadPropiedadDAO.getActividadPropiedadPorId(Integer.parseInt(data.id));
							ActividadPropiedadValorId idValor = new ActividadPropiedadValorId(actividad.getId(),Integer.parseInt(data.id));
							ActividadPropiedadValor valor = new ActividadPropiedadValor(idValor, actividad, actividadPropiedad,null, null, null, null,
									usuario, null, new DateTime().toDate(), null, 1);
	
							switch (actividadPropiedad.getDatoTipo().getId()){
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
							result = (result && ActividadPropiedadValorDAO.guardarActividadPropiedadValor(valor));
						}
					}
					
					response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
							+ "\"id\": " + actividad.getId() ,","
							, "\"usuarioCreo\": \"" , actividad.getUsuarioCreo(),"\","
							, "\"fechaCreacion\":\" " , Utils.formatDateHour(actividad.getFechaCreacion()),"\","
							, "\"usuarioactualizo\": \"" , actividad.getUsuarioActualizo() != null ? actividad.getUsuarioActualizo() : "","\","
							, "\"fechaactualizacion\": \"" , Utils.formatDateHour(actividad.getFechaActualizacion()),"\","
							, "\"fechaInicioReal\": " , actividad.getFechaInicioReal() != null ? "\"" + Utils.formatDate(actividad.getFechaInicioReal()) + "\"" : null, ","
							, "\"fechaFinReal\": " , actividad.getFechaFinReal() != null ? "\"" + Utils.formatDate(actividad.getFechaFinReal()) + "\"" : null
							, " }");
				}
				else
					response_text = "{ \"success\": false }";
			}
			catch (Throwable e){
				e.printStackTrace();
				response_text = "{ \"success\": false }";
			}
		}
		else if(accion.equals("borrarActividad")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Actividad actividad = ActividadDAO.getActividadPorId(id);
				
				response_text = String.join("","{ \"success\": ",( ObjetoDAO.borrarHijos(actividad.getTreePath(), 5, usuario) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroActividads")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalactividades\":",ActividadDAO.getTotalActividads(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,usuario).toString()," }");
		}
		else if(accion.equals("numeroActividadesPorObjeto")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			int objetoId = map.get("objetoid")!=null  ? Integer.parseInt(map.get("objetoid")) : 0;
			int objetoTipo = map.get("tipo")!=null  ? Integer.parseInt(map.get("tipo")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalactividades\":",ActividadDAO.getTotalActividadsPorObjeto(objetoId, objetoTipo, filtro_nombre,
					filtro_usuario_creo, filtro_fecha_creacion,usuario).toString()," }");
		}
		else if(accion.equals("getActividadesPaginaPorObjeto")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;

			int objetoId = map.get("objetoid")!=null  ? Integer.parseInt(map.get("objetoid")) : 0;
			int objetoTipo = map.get("tipo")!=null  ? Integer.parseInt(map.get("tipo")) : 0;
			int numeroActividads = map.get("numeroactividades")!=null  ? Integer.parseInt(map.get("numeroactividades")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Actividad> actividads = ActividadDAO.getActividadsPaginaPorObjeto(pagina, numeroActividads, objetoId, objetoTipo,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion,usuario);
			
			int congelado = 0;
			String fechaElegibilidad = null;
			String fechaCierre = null;
			if(actividads!=null && actividads.size()>0){
				Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(actividads.get(0).getTreePath());
				if(proyecto!=null){
					congelado = proyecto.getCongelado()!=null?proyecto.getCongelado():0;
					fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
					fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
				}
			}
			List<stactividad> stactividads=new ArrayList<stactividad>();
			for(Actividad actividad:actividads){
				stactividad temp =new stactividad();
				temp.descripcion = actividad.getDescripcion();
				temp.estado = actividad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(actividad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(actividad.getFechaCreacion());
				temp.fechaInicio = Utils.formatDate(actividad.getFechaInicio());
				temp.fechaFin = Utils.formatDate(actividad.getFechaFin());
				temp.id = actividad.getId();
				temp.nombre = actividad.getNombre();
				temp.usuarioActualizo = actividad.getUsuarioActualizo();
				temp.usuarioCreo = actividad.getUsuarioCreo();
				temp.actividadtipoid = actividad.getActividadTipo().getId();
				temp.actividadtiponombre = actividad.getActividadTipo().getNombre();
				temp.porcentajeavance = actividad.getPorcentajeAvance();
				temp.programa = actividad.getPrograma();
				temp.subprograma = actividad.getSubprograma();
				temp.proyecto = actividad.getProyecto();
				temp.actividad = actividad.getActividad();
				temp.obra = actividad.getObra();
				temp.ubicacionGeografica = actividad.getUbicacionGeografica();
				temp.renglon = actividad.getRenglon();
				temp.longitud = actividad.getLongitud();
				temp.latitud = actividad.getLatitud();
				temp.costo = actividad.getCosto();
				temp.acumulacionCostoId = actividad.getAcumulacionCosto() == null ? null : actividad.getAcumulacionCosto().getId();
				temp.acumulacionCostoNombre = actividad.getAcumulacionCosto() == null ? null : actividad.getAcumulacionCosto().getNombre();
				temp.duracion = actividad.getDuracion();
				temp.duracionDimension = actividad.getDuracionDimension();
				temp.proyectoBase = actividad.getProyectoBase();
				temp.fechaInicioReal = actividad.getFechaInicioReal() != null ? Utils.formatDate(actividad.getFechaInicioReal()) : null;
				temp.fechaFinReal = actividad.getFechaFinReal() != null ? Utils.formatDate(actividad.getFechaFinReal()) : null;
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 5);
				temp.congelado = congelado;
				temp.fechaElegibilidad = fechaElegibilidad;
				temp.fechaCierre = fechaCierre;
				stactividads.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividads);
	        response_text = String.join("", "\"actividades\":",response_text);
	        response_text = String.join("", response_text, ", \"congelado\":",String.valueOf(congelado));
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("obtenerActividadPorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Actividad actividad = ActividadDAO.getActividadPorId(id);

			response_text = String.join("","{ \"success\": ",(actividad!=null && actividad.getId()!=null ? "true" : "false"),", "
				+ "\"id\": " + (actividad!=null ? actividad.getId():"0") +", "
				+ "\"nombre\": \"" + (actividad!=null ? actividad.getNombre():"Indefinido") +"\" }");

		}
		else if (accion.equals("getActividadPorId")){
			Integer id = Utils.String2Int(map.get("id"));
			Actividad actividad = ActividadDAO.getActividadPorId(id);
			stactividad temp = new stactividad();
			temp.descripcion = actividad.getDescripcion();
			temp.estado = actividad.getEstado();
			temp.fechaActualizacion = Utils.formatDateHour(actividad.getFechaActualizacion());
			temp.fechaCreacion = Utils.formatDateHour(actividad.getFechaCreacion());
			temp.fechaInicio = Utils.formatDate(actividad.getFechaInicio());
			temp.fechaFin = Utils.formatDate(actividad.getFechaFin());
			temp.id = actividad.getId();
			temp.nombre = actividad.getNombre();
			temp.usuarioActualizo = actividad.getUsuarioActualizo();
			temp.usuarioCreo = actividad.getUsuarioCreo();
			temp.actividadtipoid = actividad.getActividadTipo().getId();
			temp.actividadtiponombre = actividad.getActividadTipo().getNombre();
			temp.porcentajeavance = actividad.getPorcentajeAvance();
			temp.programa = actividad.getPrograma();
			temp.subprograma = actividad.getSubprograma();
			temp.proyecto = actividad.getProyecto();
			temp.actividad = actividad.getActividad();
			temp.obra = actividad.getObra();
			temp.ubicacionGeografica = actividad.getUbicacionGeografica();
			temp.renglon = actividad.getRenglon();
			temp.longitud = actividad.getLongitud();
			temp.latitud = actividad.getLatitud();
			temp.prececesorId = actividad.getPredObjetoId();
			temp.predecesorTipo = actividad.getPredObjetoTipo();
			temp.duracion = actividad.getDuracion();
			temp.duracionDimension = actividad.getDuracionDimension();
			temp.costo = actividad.getCosto();
			temp.acumulacionCostoId = actividad.getAcumulacionCosto()!=null ? actividad.getAcumulacionCosto().getId(): null;
			temp.acumulacionCostoNombre = actividad.getAcumulacionCosto()!=null ? actividad.getAcumulacionCosto().getNombre(): null;
			temp.proyectoBase = actividad.getProyectoBase();
			temp.fechaInicioReal = actividad.getFechaInicioReal() != null ? Utils.formatDate(actividad.getFechaInicioReal()) : null;
			temp.fechaFinReal = actividad.getFechaFinReal() != null ? Utils.formatDate(actividad.getFechaFinReal()) : null;
			temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 5);

			Proyecto proyecto = ProyectoDAO.getProyectobyTreePath(actividad.getTreePath());
			if(proyecto!=null){
				temp.congelado = proyecto.getCongelado()!=null?proyecto.getCongelado():0;
				temp.fechaElegibilidad = Utils.formatDate(proyecto.getFechaElegibilidad());
				temp.fechaCierre = Utils.formatDate(proyecto.getFechaCierre());
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
	        response_text = String.join("", "\"actividad\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
			
		}else if(accion.equals("guardarModal")){
			boolean result = false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int objetoId = map.get("objetoId")!=null ? Integer.parseInt(map.get("objetoId")) : 0;
			int objetoTipo = map.get("objetoTipo")!=null ? Integer.parseInt(map.get("objetoTipo")) : 0;
			Actividad actividad = null;
			if(id>0 || (esnuevo && objetoId > 0 && objetoTipo > 0)){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				Date fechaInicio = Utils.dateFromString(map.get("fechainicio"));
				Date fechaFin = Utils.dateFromString(map.get("fechafin"));
				Integer porcentajeAvance = Utils.getParameterInteger(map, "porcentajeavance");
				Integer duracion = Utils.String2Int(map.get("duracion"), null);
				String duracionDimension = map.get("duracionDimension");
				int actividadtipoid =Utils.getParameterInteger(map, "actividadtipoid");
				
				ActividadTipo actividadTipo= new ActividadTipo();
				actividadTipo.setId(actividadtipoid);
				Integer proyectoBase=null;

				if (esnuevo){
					actividad = new Actividad(actividadTipo, nombre, fechaInicio, fechaFin, porcentajeAvance
							, usuario, new Date(), 1, objetoId, objetoTipo, duracion, duracionDimension);
					actividad.setProyectoBase(proyectoBase);
				}else{
					actividad = ActividadDAO.getActividadPorId(id);
					actividad.setNombre(nombre);
					actividad.setDescripcion(descripcion);
					actividad.setUsuarioActualizo(usuario);
					actividad.setFechaActualizacion(new Date());
					actividad.setFechaInicio(fechaInicio);
					actividad.setFechaFin(fechaFin);
					actividad.setPorcentajeAvance(porcentajeAvance);
					actividad.setActividadTipo(actividadTipo);
					actividad.setDuracion(duracion);
					actividad.setDuracionDimension(duracionDimension);
					objetoTipo = actividad.getObjetoTipo();
					objetoId = actividad.getObjetoId();
					
					
				}
				result = ActividadDAO.guardarActividad(actividad, true);
			}
			
			if (result){
				stactividad temp = new stactividad();
				temp.id = actividad.getId();
				temp.nombre = actividad.getNombre();
				temp.duracion = actividad.getDuracion();
				temp.duracionDimension = actividad.getDuracionDimension();
				temp.fechaInicio = (actividad.getPredObjetoId() == null ? Utils.formatDate(actividad.getFechaInicio()) : "");
				response_text=new GsonBuilder().serializeNulls().create().toJson(temp);
		        response_text = String.join("", "\"actividad\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
						
			}else{
				response_text = "{ \"success\": false }";
			}
			
			
		}else if(accion.equals("getCantidadHistoria")){
			Integer id = Utils.String2Int(map.get("id"));
			String resultado = ActividadDAO.getVersiones(id); 
			response_text = String.join("", "{\"success\":true, \"versiones\": [" + resultado + "]}");
		}else if(accion.equals("getHistoria")){
			Integer id = Utils.String2Int(map.get("id"));
			Integer version = Utils.String2Int(map.get("version"));
			String resultado = ActividadDAO.getHistoria(id, version); 
			response_text = String.join("", "{\"success\":true, \"historia\":" + resultado + "}");
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
