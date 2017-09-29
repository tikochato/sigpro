package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.DatoTipoDAO;
import dao.MetaDAO;
import dao.MetaTipoDAO;
import dao.MetaUnidadMedidaDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.DatoTipo;
import pojo.Meta;
import pojo.MetaAvance;
import pojo.MetaAvanceId;
import pojo.MetaPlanificado;
import pojo.MetaPlanificadoId;
import pojo.MetaTipo;
import pojo.MetaUnidadMedida;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CLogger;
import utilities.Utils;

/**
 * Servlet implementation class SMeta
 */
@WebServlet("/SMeta")
public class SMeta extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class stmeta{
		Integer id;
		String nombre;
		String descripcion;
		Integer estado;
		Integer unidadMedidaId;
		String usuarioCreo;
		String fechaCreacion;
		String usuarioActualizo;
		String fechaActualizacion;
		Integer objetoId;
		Integer objetoTipo;
		Integer datoTipoId;
		String metaFinalString;
		Integer metaFinalEntero;
		BigDecimal metaFinalDecimal;
		String metaFinalTiempo;
		stplanificado[] planificado;
		stavance[] avance;
	}
	
	public class stplanificado{
		Integer ejercicio;
		String eneroString;
		String febreroString;
		String marzoString;
		String abrilString;
		String mayoString;
		String junioString;
		String julioString;
		String agostoString;
		String septiembreString;
		String octubreString;
		String noviembreString;
		String diciembreString;
		Integer eneroEntero;
		Integer febreroEntero;
		Integer marzoEntero;
		Integer abrilEntero;
		Integer mayoEntero;
		Integer junioEntero;
		Integer julioEntero;
		Integer agostoEntero;
		Integer septiembreEntero;
		Integer octubreEntero;
		Integer noviembreEntero;
		Integer diciembreEntero;
		BigDecimal eneroDecimal;
		BigDecimal febreroDecimal;
		BigDecimal marzoDecimal;
		BigDecimal abrilDecimal;
		BigDecimal mayoDecimal;
		BigDecimal junioDecimal;
		BigDecimal julioDecimal;
		BigDecimal agostoDecimal;
		BigDecimal septiembreDecimal;
		BigDecimal octubreDecimal;
		BigDecimal noviembreDecimal;
		BigDecimal diciembreDecimal;
		String eneroTiempo;
		String febreroTiempo;
		String marzoTiempo;
		String abrilTiempo;
		String mayoTiempo;
		String junioTiempo;
		String julioTiempo;
		String agostoTiempo;
		String septiembreTiempo;
		String octubreTiempo;
		String noviembreTiempo;
		String diciembreTiempo;
		String usuario;
	}
	
	public class stavance{
		String fecha;
		Integer valorEntero;
		String valorString;
		BigDecimal valorDecimal;
		String valorTiempo;
		String usuario;
	}

	public class sttipometa{
		Integer id;
		String nombre;
		String descripcion;
		Integer estado;
		String fechaCreacion;
		String fechaActualizacion;
		String usuarioCreo;
		String usuarioActulizo;
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMeta() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String str;
		while ((str = br.readLine()) != null) {
			CLogger.write_simple("0", SMeta.class, str);
			sb.append(str);
		}
		;
		
		String texto = sb.toString();
		CLogger.write_simple("1", SMeta.class, "sb.toString()");
		CLogger.write_simple("2", SMeta.class, texto);
		
		
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getMetasPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroMetas = map.get("numerometas")!=null  ? Integer.parseInt(map.get("numerometas")) : 0;
			Integer id =  map.get("id")!=null  ? Integer.parseInt(map.get("id")) : 0;
			Integer tipo =  map.get("tipo")!=null  ? Integer.parseInt(map.get("tipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Meta> Metas = MetaDAO.getMetasPagina(pagina, numeroMetas, id, tipo,filtro_nombre, -1, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stmeta> tmetas = new ArrayList<stmeta>();
			for(Meta meta : Metas){
				stmeta temp = new stmeta();
				temp.id = meta.getId();
				temp.nombre = meta.getNombre();
				temp.descripcion = meta.getDescripcion();
				temp.objetoId = meta.getObjetoId();
				temp.objetoTipo = meta.getObjetoTipo();
				temp.estado = meta.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(meta.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(meta.getFechaCreacion());
				temp.unidadMedidaId = meta.getMetaUnidadMedida().getId();
				temp.datoTipoId = meta.getDatoTipo().getId();
				temp.usuarioActualizo = meta.getUsuarioActualizo();
				temp.usuarioCreo = meta.getUsuarioCreo();
				temp.metaFinalString = meta.getMetaFinalString();
				temp.metaFinalEntero = meta.getMetaFinalEntero();
				temp.metaFinalDecimal = meta.getMetaFinalDecimal();
				temp.metaFinalTiempo = meta.getMetaFinalFecha() != null ? Utils.formatDate(meta.getMetaFinalFecha()) : null;
				tmetas.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(tmetas);
	        response_text = String.join("", "\"Metas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetasCompletas")){
			Integer id =  map.get("id")!=null  ? Integer.parseInt(map.get("id")) : 0;
			Integer tipo =  map.get("tipo")!=null  ? Integer.parseInt(map.get("tipo")) : 0;
			List<Meta> Metas = MetaDAO.getMetasObjeto(id, tipo);
			List<stmeta> tmetas = new ArrayList<stmeta>();
			for(Meta meta : Metas){
				stmeta temp = new stmeta();
				temp.id = meta.getId();
				temp.nombre = meta.getNombre();
				temp.descripcion = meta.getDescripcion();
				temp.objetoId = meta.getObjetoId();
				temp.objetoTipo = meta.getObjetoTipo();
				temp.estado = meta.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(meta.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(meta.getFechaCreacion());
				temp.unidadMedidaId = meta.getMetaUnidadMedida().getId();
				temp.datoTipoId = meta.getDatoTipo().getId();
				temp.usuarioActualizo = meta.getUsuarioActualizo();
				temp.usuarioCreo = meta.getUsuarioCreo();
				temp.metaFinalString = meta.getMetaFinalString();
				temp.metaFinalEntero = meta.getMetaFinalEntero();
				temp.metaFinalDecimal = meta.getMetaFinalDecimal();
				temp.metaFinalTiempo = meta.getMetaFinalFecha() != null ? Utils.formatDate(meta.getMetaFinalFecha()) : null;
				Set<MetaAvance> metaavances = meta.getMetaAvances();
				temp.avance = new stavance[metaavances.size()];
				int i=0;
				
				Iterator<MetaAvance> iterator = metaavances.iterator();
	    	    while(iterator.hasNext()) {
	    	    	MetaAvance ma = iterator.next();
					temp.avance[i] = new stavance();
					temp.avance[i].fecha = Utils.formatDate(ma.getId().getFecha());
					temp.avance[i].valorString = ma.getValorString();
					temp.avance[i].valorEntero = ma.getValorEntero();
					temp.avance[i].valorDecimal = ma.getValorDecimal();
					temp.avance[i].valorTiempo = ma.getValorTiempo() !=null ? Utils.formatDate(ma.getValorTiempo()) : null;
					temp.avance[i].usuario = ma.getUsuario();
					i++;
				}
				Set<MetaPlanificado> metaplanificado = meta.getMetaPlanificados();
				temp.planificado = new stplanificado[metaplanificado.size()];
				i=0;
				Iterator<MetaPlanificado> iteratorP = metaplanificado.iterator();
	    	    while(iteratorP.hasNext()) {
	    	    	MetaPlanificado mp = iteratorP.next();
					temp.planificado[i] = new stplanificado();
					temp.planificado[i].ejercicio = mp.getId().getEjercicio();
					
					temp.planificado[i].eneroString = mp.getEneroString();
					temp.planificado[i].febreroString = mp.getFebreroString();
					temp.planificado[i].marzoString = mp.getMarzoString();
					temp.planificado[i].abrilString = mp.getAbrilString();
					temp.planificado[i].mayoString = mp.getMayoString();
					temp.planificado[i].junioString = mp.getJunioString();
					temp.planificado[i].julioString = mp.getJulioString();
					temp.planificado[i].agostoString = mp.getAgostoString();
					temp.planificado[i].septiembreString = mp.getSeptiembreString();
					temp.planificado[i].octubreString = mp.getOctubreString();
					temp.planificado[i].noviembreString = mp.getNoviembreString();
					temp.planificado[i].diciembreString = mp.getDiciembreString();
					
					temp.planificado[i].eneroEntero = mp.getEneroEntero();
					temp.planificado[i].febreroEntero = mp.getFebreroEntero();
					temp.planificado[i].marzoEntero = mp.getMarzoEntero();
					temp.planificado[i].abrilEntero = mp.getAbrilEntero();
					temp.planificado[i].mayoEntero = mp.getMayoEntero();
					temp.planificado[i].junioEntero = mp.getJunioEntero();
					temp.planificado[i].julioEntero = mp.getJulioEntero();
					temp.planificado[i].agostoEntero = mp.getAgostoEntero();
					temp.planificado[i].septiembreEntero = mp.getSeptiembreEntero();
					temp.planificado[i].octubreEntero = mp.getOctubreEntero();
					temp.planificado[i].noviembreEntero = mp.getNoviembreEntero();
					temp.planificado[i].diciembreEntero = mp.getDiciembreEntero();
					
					temp.planificado[i].eneroDecimal = mp.getEneroDecimal();
					temp.planificado[i].febreroDecimal = mp.getFebreroDecimal();
					temp.planificado[i].marzoDecimal = mp.getMarzoDecimal();
					temp.planificado[i].abrilDecimal = mp.getAbrilDecimal();
					temp.planificado[i].mayoDecimal = mp.getMayoDecimal();
					temp.planificado[i].junioDecimal = mp.getJunioDecimal();
					temp.planificado[i].julioDecimal = mp.getJulioDecimal();
					temp.planificado[i].agostoDecimal = mp.getAgostoDecimal();
					temp.planificado[i].septiembreDecimal = mp.getSeptiembreDecimal();
					temp.planificado[i].octubreDecimal = mp.getOctubreDecimal();
					temp.planificado[i].noviembreDecimal = mp.getNoviembreDecimal();
					temp.planificado[i].diciembreDecimal = mp.getDiciembreDecimal();
					
					temp.planificado[i].eneroTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getEneroTiempo()) : null;
					temp.planificado[i].febreroTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getFebreroTiempo()) : null;
					temp.planificado[i].marzoTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getMarzoTiempo()) : null;
					temp.planificado[i].abrilTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getAbrilTiempo()) : null;
					temp.planificado[i].mayoTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getMayoTiempo()) : null;
					temp.planificado[i].junioTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getJunioTiempo()) : null;
					temp.planificado[i].julioTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getJulioTiempo()) : null;
					temp.planificado[i].agostoTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getAgostoTiempo()) : null;
					temp.planificado[i].septiembreTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getSeptiembreTiempo()) : null;
					temp.planificado[i].octubreTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getOctubreTiempo()) : null;
					temp.planificado[i].noviembreTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getNoviembreTiempo()) : null;
					temp.planificado[i].diciembreTiempo = mp.getEneroTiempo()!=null ? Utils.formatDate(mp.getDiciembreTiempo()) : null;
					
					temp.planificado[i].usuario = mp.getUsuario();
					i++;
				}
				tmetas.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(tmetas);
	        response_text = String.join("", "\"Metas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("guardarMetasCompletas")){
			boolean result = false;
			Integer objetoId =  map.get("objeto_id")!=null  ? Integer.parseInt(map.get("objeto_id")) : 0;
			Integer objetoTipo =  map.get("objeto_tipo")!=null  ? Integer.parseInt(map.get("objeto_tipo")) : 0;
			String metas = map.get("metas");
			
			JsonParser parser = new JsonParser();
			JsonArray metasArreglo = parser.parse(metas).getAsJsonArray();
			for(int i=0; i<metasArreglo.size(); i++){
				JsonObject objeto = metasArreglo.get(i).getAsJsonObject();
				Integer id = objeto.get("id").isJsonNull() ? null : objeto.get("id").getAsInt();
				String nombre = objeto.get("nombre").isJsonNull() ? "" : objeto.get("nombre").getAsString();
				String descripcion = objeto.get("descripcion").isJsonNull() ? null : objeto.get("descripcion").getAsString();
				Integer estado = objeto.get("estado").getAsInt();
				Integer unidadMedidaId = objeto.get("unidadMedidaId").getAsJsonObject().get("id").getAsInt();
				Integer datoTipoId = objeto.get("datoTipoId").getAsJsonObject().get("id").getAsInt();
				
				String usuarioActualizo = null;
				Date fechaActualizacion =  null;				
				Integer metaFinalEntero=null;
				String metaFinalString=null;
				BigDecimal metaFinalDecimal = null;
				Date metaFinalTiempo = null;
				switch(datoTipoId){
					case 1: //texto
						metaFinalString = objeto.get("metaFinalString").isJsonNull() ? null : objeto.get("metaFinalString").getAsString();
						break;
					case 2: //entero
						metaFinalEntero = objeto.get("metaFinalEntero").isJsonNull() ? null : objeto.get("metaFinalEntero").getAsInt();
						break;
					case 3: //decimal
						metaFinalDecimal = objeto.get("metaFinalDecimal").isJsonNull() ? null : objeto.get("metaFinalDecimal").getAsBigDecimal();
						break;
					case 4: //boolean
						metaFinalString = objeto.get("metaFinalString").isJsonNull() ? null : objeto.get("metaFinalString").getAsString();
						break;
					case 5: //fecha
						metaFinalTiempo = objeto.get("metaFinalTiempo").isJsonNull() ? null : Utils.dateFromString(objeto.get("metaFinalTiempo").getAsString());
				}

				DatoTipo datoTipo = DatoTipoDAO.getDatoTipo(datoTipoId);
				MetaUnidadMedida metaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(unidadMedidaId);
								
				Meta meta;
				if(id!=null && id>0){
					meta = MetaDAO.getMetaPorId(id);
					meta.setDatoTipo(datoTipo);
					meta.setMetaUnidadMedida(metaUnidadMedida);
					meta.setNombre(nombre);
					meta.setDescripcion(descripcion);
					meta.setUsuarioActualizo(usuario);
					meta.setFechaActualizacion(new Date());
					meta.setEstado(estado);
					meta.setMetaFinalEntero(metaFinalEntero);
					meta.setMetaFinalString(metaFinalString);
					meta.setMetaFinalDecimal(metaFinalDecimal);
					meta.setMetaFinalFecha(metaFinalTiempo);
					MetaDAO.guardarMeta(meta);
					result = MetaDAO.borrarPlanificadoAvanceMeta(meta);
				}else{
					meta = new Meta(datoTipo, metaUnidadMedida, nombre, descripcion, usuario, usuarioActualizo, new Date(), fechaActualizacion, estado, 
							objetoId, objetoTipo, metaFinalEntero, metaFinalString, metaFinalDecimal, metaFinalTiempo, null, null);
					result = MetaDAO.guardarMeta(meta);
				}
				
				if(result){
					//Obtener planificados
					JsonArray planificados = objeto.get("planificado").getAsJsonArray();
					for(int p=0; p<planificados.size(); p++){
						JsonObject planificado = planificados.get(p).getAsJsonObject();
						Integer ejercicio = planificado.get("ejercicio").getAsInt();
						
						Integer eneroEntero = null;
						Integer febreroEntero = null;
						Integer marzoEntero = null;
						Integer abrilEntero = null;
						Integer mayoEntero = null;
						Integer junioEntero = null;
						Integer julioEntero = null;
						Integer agostoEntero = null;
						Integer septiembreEntero = null;
						Integer octubreEntero = null;
						Integer noviembreEntero = null;
						Integer diciembreEntero = null;
						
						String eneroString = null;
						String febreroString = null;
						String marzoString = null;
						String abrilString = null;
						String mayoString = null;
						String junioString = null;
						String julioString = null;
						String agostoString = null;
						String septiembreString = null;
						String octubreString = null;
						String noviembreString = null;
						String diciembreString = null;
						
						BigDecimal eneroDecimal = null;
						BigDecimal febreroDecimal = null;
						BigDecimal marzoDecimal = null;
						BigDecimal abrilDecimal = null;
						BigDecimal mayoDecimal = null;
						BigDecimal junioDecimal = null;
						BigDecimal julioDecimal = null;
						BigDecimal agostoDecimal = null;
						BigDecimal septiembreDecimal = null;
						BigDecimal octubreDecimal = null;
						BigDecimal noviembreDecimal = null;
						BigDecimal diciembreDecimal = null;
						
						Date eneroTiempo = null;
						Date febreroTiempo = null;
						Date marzoTiempo = null;
						Date abrilTiempo = null;
						Date mayoTiempo = null;
						Date junioTiempo = null;
						Date julioTiempo = null;
						Date agostoTiempo = null;
						Date septiembreTiempo = null;
						Date octubreTiempo = null;
						Date noviembreTiempo = null;
						Date diciembreTiempo = null;
						
						switch(datoTipoId){
							case 1: //texto
								eneroString = planificado.get("eneroString").isJsonNull() ? null : planificado.get("eneroString").getAsString();
								febreroString = planificado.get("febreroString").isJsonNull() ? null : planificado.get("febreroString").getAsString();
								marzoString = planificado.get("marzoString").isJsonNull() ? null : planificado.get("marzoString").getAsString();
								abrilString = planificado.get("abrilString").isJsonNull() ? null : planificado.get("abrilString").getAsString();
								mayoString = planificado.get("mayoString").isJsonNull() ? null : planificado.get("mayoString").getAsString();
								junioString = planificado.get("junioString").isJsonNull() ? null : planificado.get("junioString").getAsString();
								julioString = planificado.get("julioString").isJsonNull() ? null : planificado.get("julioString").getAsString();
								agostoString = planificado.get("agostoString").isJsonNull() ? null : planificado.get("agostoString").getAsString();
								septiembreString = planificado.get("septiembreString").isJsonNull() ? null : planificado.get("septiembreString").getAsString();
								octubreString = planificado.get("octubreString").isJsonNull() ? null : planificado.get("octubreString").getAsString();
								noviembreString = planificado.get("noviembreString").isJsonNull() ? null : planificado.get("noviembreString").getAsString();
								diciembreString = planificado.get("diciembreString").isJsonNull() ? null : planificado.get("diciembreString").getAsString();
								break;
							case 2: //entero
								eneroEntero = planificado.get("eneroEntero").isJsonNull() ? null : planificado.get("eneroEntero").getAsInt();
								febreroEntero = planificado.get("febreroEntero").isJsonNull() ? null : planificado.get("febreroEntero").getAsInt();
								marzoEntero = planificado.get("marzoEntero").isJsonNull() ? null : planificado.get("marzoEntero").getAsInt();
								abrilEntero = planificado.get("abrilEntero").isJsonNull() ? null : planificado.get("abrilEntero").getAsInt();
								mayoEntero = planificado.get("mayoEntero").isJsonNull() ? null : planificado.get("mayoEntero").getAsInt();
								junioEntero = planificado.get("junioEntero").isJsonNull() ? null : planificado.get("junioEntero").getAsInt();
								julioEntero = planificado.get("julioEntero").isJsonNull() ? null : planificado.get("julioEntero").getAsInt();
								agostoEntero = planificado.get("agostoEntero").isJsonNull() ? null : planificado.get("agostoEntero").getAsInt();
								septiembreEntero = planificado.get("septiembreEntero").isJsonNull() ? null : planificado.get("septiembreEntero").getAsInt();
								octubreEntero = planificado.get("octubreEntero").isJsonNull() ? null : planificado.get("octubreEntero").getAsInt();
								noviembreEntero = planificado.get("noviembreEntero").isJsonNull() ? null : planificado.get("noviembreEntero").getAsInt();
								diciembreEntero = planificado.get("diciembreEntero").isJsonNull() ? null : planificado.get("diciembreEntero").getAsInt();
								break;
							case 3: //decimal
								eneroDecimal = planificado.get("eneroDecimal").isJsonNull() ? null : planificado.get("eneroDecimal").getAsBigDecimal();
								febreroDecimal = planificado.get("febreroDecimal").isJsonNull() ? null : planificado.get("febreroDecimal").getAsBigDecimal();
								marzoDecimal = planificado.get("marzoDecimal").isJsonNull() ? null : planificado.get("marzoDecimal").getAsBigDecimal();
								abrilDecimal = planificado.get("abrilDecimal").isJsonNull() ? null : planificado.get("abrilDecimal").getAsBigDecimal();
								mayoDecimal = planificado.get("mayoDecimal").isJsonNull() ? null : planificado.get("mayoDecimal").getAsBigDecimal();
								junioDecimal = planificado.get("junioDecimal").isJsonNull() ? null : planificado.get("junioDecimal").getAsBigDecimal();
								julioDecimal = planificado.get("julioDecimal").isJsonNull() ? null : planificado.get("julioDecimal").getAsBigDecimal();
								agostoDecimal = planificado.get("agostoDecimal").isJsonNull() ? null : planificado.get("agostoDecimal").getAsBigDecimal();
								septiembreDecimal = planificado.get("septiembreDecimal").isJsonNull() ? null : planificado.get("septiembreDecimal").getAsBigDecimal();
								octubreDecimal = planificado.get("octubreDecimal").isJsonNull() ? null : planificado.get("octubreDecimal").getAsBigDecimal();
								noviembreDecimal = planificado.get("noviembreDecimal").isJsonNull() ? null : planificado.get("noviembreDecimal").getAsBigDecimal();
								diciembreDecimal = planificado.get("diciembreDecimal").isJsonNull() ? null : planificado.get("diciembreDecimal").getAsBigDecimal();
								break;
							case 4: //boolean
								eneroString = planificado.get("eneroString").isJsonNull() ? null : planificado.get("eneroString").getAsString();
								febreroString = planificado.get("febreroString").isJsonNull() ? null : planificado.get("febreroString").getAsString();
								marzoString = planificado.get("marzoString").isJsonNull() ? null : planificado.get("marzoString").getAsString();
								abrilString = planificado.get("abrilString").isJsonNull() ? null : planificado.get("abrilString").getAsString();
								mayoString = planificado.get("mayoString").isJsonNull() ? null : planificado.get("mayoString").getAsString();
								junioString = planificado.get("junioString").isJsonNull() ? null : planificado.get("junioString").getAsString();
								julioString = planificado.get("julioString").isJsonNull() ? null : planificado.get("julioString").getAsString();
								agostoString = planificado.get("agostoString").isJsonNull() ? null : planificado.get("agostoString").getAsString();
								septiembreString = planificado.get("septiembreString").isJsonNull() ? null : planificado.get("septiembreString").getAsString();
								octubreString = planificado.get("octubreString").isJsonNull() ? null : planificado.get("octubreString").getAsString();
								noviembreString = planificado.get("noviembreString").isJsonNull() ? null : planificado.get("noviembreString").getAsString();
								diciembreString = planificado.get("diciembreString").isJsonNull() ? null : planificado.get("diciembreString").getAsString();
								break;
							case 5: //Tiempo
								eneroTiempo = planificado.get("eneroTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("eneroTiempo").getAsString());
								febreroTiempo = planificado.get("febreroTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("febreroTiempo").getAsString());
								marzoTiempo = planificado.get("marzoTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("marzoTiempo").getAsString());
								abrilTiempo = planificado.get("abrilTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("abrilTiempo").getAsString());
								mayoTiempo = planificado.get("mayoTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("mayoTiempo").getAsString());
								junioTiempo = planificado.get("junioTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("junioTiempo").getAsString());
								julioTiempo = planificado.get("julioTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("julioTiempo").getAsString());
								agostoTiempo = planificado.get("agostoTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("agostoTiempo").getAsString());
								septiembreTiempo = planificado.get("septiembreTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("septiembreTiempo").getAsString());
								octubreTiempo = planificado.get("octubreTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("octubreTiempo").getAsString());
								noviembreTiempo = planificado.get("noviembreTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("noviembreTiempo").getAsString());
								diciembreTiempo = planificado.get("diciembreTiempo").isJsonNull() ? null : Utils.dateFromString(planificado.get("diciembreTiempo").getAsString());
						}

						MetaPlanificadoId planificadoId = new MetaPlanificadoId(meta.getId(), ejercicio);
						MetaPlanificado metaPlanificado = new MetaPlanificado(planificadoId, meta, eneroEntero, eneroString, eneroDecimal, eneroTiempo, 
								febreroEntero, febreroString, febreroDecimal, febreroTiempo, marzoEntero, marzoString, marzoDecimal, marzoTiempo, 
								abrilEntero, abrilString, abrilDecimal, abrilTiempo, mayoEntero, mayoString, mayoDecimal, mayoTiempo, 
								junioEntero, junioString, junioDecimal, junioTiempo, julioEntero, julioString, julioDecimal, julioTiempo, 
								agostoEntero, agostoString, agostoDecimal, agostoTiempo, septiembreEntero, septiembreString, septiembreDecimal, septiembreTiempo, 
								octubreEntero, octubreString, octubreDecimal, octubreTiempo, noviembreEntero, noviembreString, noviembreDecimal, noviembreTiempo, 
								diciembreEntero, diciembreString, diciembreDecimal, diciembreTiempo, 1, usuario, new Date());
						MetaDAO.agregarMetaPlanificado(metaPlanificado);
					}
					//obtener avances
					JsonArray avances = objeto.get("avance").getAsJsonArray();
					ArrayList<Date> fechasUtilizadas = new ArrayList<Date>(); 
					for(int a=0; a<avances.size(); a++){
						JsonObject avance = avances.get(a).getAsJsonObject();
						Date fecha = Utils.dateFromString(avance.get("fecha").getAsString());
						String usuarioCrea = avance.get("usuario").isJsonNull()? usuario : avance.get("usuario").getAsString();
						if(fechasUtilizadas.contains(fecha)){
							DateTime fechaNueva = new DateTime(fecha);
							fechaNueva = fechaNueva.plusSeconds(a);
							fecha = fechaNueva.toDate();
						}
						fechasUtilizadas.add(fecha);
						
						Integer valorEntero=null;
						String valorString=null;
						BigDecimal valorDecimal=null;
						Date valorTiempo=null;
						switch(datoTipoId){
							case 1: //texto
								valorString = avance.get("valorString").isJsonNull() ? null : avance.get("valorString").getAsString();
								break;
							case 2: //entero
								valorEntero = avance.get("valorEntero").isJsonNull() ? null : avance.get("valorEntero").getAsInt();
								break;
							case 3: //decimal
								valorDecimal = avance.get("valorDecimal").isJsonNull() ? null : avance.get("valorDecimal").getAsBigDecimal();
								break;
							case 4: //boolean
								valorString = avance.get("valorString").isJsonNull() ? null : avance.get("valorString").getAsString();
								break;
							case 5: //fecha
								valorTiempo = avance.get("valorString").isJsonNull() ? null : Utils.dateFromString(avance.get("valorString").getAsString());
						}
						
						MetaAvanceId avanceId = new MetaAvanceId(meta.getId(), fecha);
						MetaAvance metaAvance = new MetaAvance(avanceId, meta, usuarioCrea, valorEntero, valorString, valorDecimal, valorTiempo, 1, new Date());
						MetaDAO.agregarMetaAvance(metaAvance);
					}
				}
				
			}
			
			response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
		}
		else if(accion.equals("getMetas")){
			List<Meta> Metas = MetaDAO.getMetas();
			response_text=new GsonBuilder().serializeNulls().create().toJson(Metas);
	        response_text = String.join("", "\"Metas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("obtenerMetaPorId")){
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			Meta meta= MetaDAO.getMetaPorId(id);
			response_text = String.join("","{ \"success\": ",(meta!=null && meta.getId()!=null ? "true" : "false"),", "
					+ "\"id\": " + (meta!=null ? meta.getId():"0") +", "
					+ "\"nombre\": \"" + (meta!=null ? meta.getNombre():"") +"\" }");

		}
		else if(accion.equals("guardarMeta")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				Integer idUnidadMedida = map.get("unidadMedidaId")!=null ? Integer.parseInt(map.get("unidadMedidaId")) : 0;
				MetaUnidadMedida metaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(idUnidadMedida);
				String descripcion = map.get("descripcion");
				Integer datoTipoId = Utils.getParameterInteger(map, "datoTipoId");
				DatoTipo datoTipo = DatoTipoDAO.getDatoTipo(datoTipoId);
				Integer objetoId = Utils.getParameterInteger(map, "objetoId");
				Integer objetoTipo = Utils.getParameterInteger(map, "objetoTipo");
				
				Meta Meta;
				if(esnuevo){
					Meta = new Meta(datoTipo, metaUnidadMedida, nombre, descripcion,
							usuario, null, new DateTime().toDate(), null, 1, objetoId, objetoTipo, null, null, null, null, null, null);
				}
				else{
					Meta = MetaDAO.getMetaPorId(id);
					Meta.setNombre(nombre);
					Meta.setMetaUnidadMedida(metaUnidadMedida);
					Meta.setDescripcion(descripcion);
					Meta.setUsuarioActualizo(usuario);
					Meta.setFechaActualizacion(new DateTime().toDate());
				}
				result = MetaDAO.guardarMeta(Meta);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						, "\"id\": " , Meta.getId().toString() , ","
						, "\"datoTipoId\": " , Meta.getDatoTipo().getId().toString() , ","
						, "\"usuarioCreo\": \"" , Meta.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(Meta.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , Meta.getUsuarioActualizo() != null ? Meta.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(Meta.getFechaActualizacion()),"\""
						," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarMeta")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Meta Meta = MetaDAO.getMetaPorId(id);
				Meta.setUsuarioActualizo(usuario);
				Meta.setFechaActualizacion(new DateTime().toDate());
				response_text = String.join("","{ \"success\": ",(MetaDAO.eliminarMeta(Meta) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroMetas")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			Integer id =  map.get("id")!=null  ? Integer.parseInt(map.get("id")) : 0;
			Integer tipo =  map.get("tipo")!=null  ? Integer.parseInt(map.get("tipo")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalMetas\":",MetaDAO.getTotalMetas(id, tipo,filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("getMetasTipos")){
			List<MetaTipo> MetaTipos = MetaTipoDAO.getMetaTipos();
			List<sttipometa> sttipo = new ArrayList<sttipometa>();
			for(MetaTipo metatipo:MetaTipos){
				sttipometa temp = new sttipometa();
				temp.descripcion = metatipo.getDescripcion();
				temp.estado = metatipo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(metatipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(metatipo.getFechaCreacion());
				temp.id = metatipo.getId();
				temp.nombre = metatipo.getNombre();
				temp.usuarioActulizo = metatipo.getUsuarioActualizo();
				temp.usuarioCreo = metatipo.getUsuarioCreo();
				sttipo.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(sttipo);
	        response_text = String.join("", "\"MetasTipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetasUnidadesMedida")){
			List<MetaUnidadMedida> MetaUnidades = MetaUnidadMedidaDAO.getMetaUnidadMedidas();
			List<sttipometa> stunidad = new ArrayList<sttipometa>();
			for(MetaUnidadMedida metaunidad : MetaUnidades){
				sttipometa temp = new sttipometa();
				temp.descripcion = metaunidad.getDescripcion();
				temp.estado = metaunidad.getEstado();
				temp.fechaActualizacion = Utils.formatDate(metaunidad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(metaunidad.getFechaCreacion());
				temp.id = metaunidad.getId();
				temp.nombre = metaunidad.getNombre();
				temp.usuarioActulizo = metaunidad.getUsuarioActualizo();
				temp.usuarioCreo = metaunidad.getUsuarioCreo();
				stunidad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stunidad);
			response_text = String.join("", "\"MetasUnidades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getPcp")){
			String nombre = "";
			String fechaInicio = "";
			String fechaFin = "";
			Integer tipo = map.get("tipo")!=null ? Integer.parseInt(map.get("tipo")) : 0;
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			switch(tipo){
				case 1: Proyecto proyecto = ProyectoDAO.getProyectoPorId(id,usuario); 
					nombre = (proyecto!=null) ? proyecto.getNombre() : ""; 
					fechaInicio = (proyecto!=null && proyecto.getFechaInicio()!=null) ? Utils.formatDate(proyecto.getFechaInicio()): "";
					fechaFin = (proyecto!=null && proyecto.getFechaFin()!=null) ? Utils.formatDate(proyecto.getFechaFin()): "";
					break;
				case 2: Componente componente = ComponenteDAO.getComponentePorId(id,usuario); 
					nombre = (componente!=null) ? componente.getNombre() : ""; 
					fechaInicio = (componente!=null && componente.getFechaInicio()!=null) ? Utils.formatDate(componente.getFechaInicio()): "";
					fechaFin = (componente!=null && componente.getFechaFin()!=null) ? Utils.formatDate(componente.getFechaFin()): "";
					break;
				case 3: Producto producto = ProductoDAO.getProductoPorId(id,usuario); 
					nombre = (producto!=null) ? producto.getNombre() : ""; 
					fechaInicio = (producto!=null && producto.getFechaInicio()!=null) ? Utils.formatDate(producto.getFechaInicio()): "";
					fechaFin = (producto!=null && producto.getFechaFin()!=null) ? Utils.formatDate(producto.getFechaFin()): "";
					break;
				case 4: Subproducto subproducto = SubproductoDAO.getSubproductoPorId(id,usuario); 
					nombre = (subproducto!=null) ? subproducto.getNombre() : ""; 
					fechaInicio = (subproducto!=null && subproducto.getFechaInicio()!=null) ? Utils.formatDate(subproducto.getFechaInicio()): "";
					fechaFin = (subproducto!=null && subproducto.getFechaFin()!=null) ? Utils.formatDate(subproducto.getFechaFin()): "";
					break;
				case 5: Actividad actividad = ActividadDAO.getActividadPorId(id); 
					nombre = (actividad!=null) ? actividad.getNombre() : ""; 
					fechaInicio = (actividad!=null && actividad.getFechaInicio()!=null) ? Utils.formatDate(actividad.getFechaInicio()): "";
					fechaFin = (actividad!=null && actividad.getFechaFin()!=null) ? Utils.formatDate(actividad.getFechaFin()): "";
					break;
			}
	        response_text = String.join("", "\"nombre\":\"",nombre,"\", ", "\"fechaInicio\":\"",fechaInicio,"\", ", "\"fechaFin\":\"",fechaFin,"\"");
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