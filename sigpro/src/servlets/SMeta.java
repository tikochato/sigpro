package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
		String metaFinal;
		stplanificado[] planificado;
		stavance[] avance;
	}
	
	public class stplanificado{
		Integer ejercicio;
		String enero;
		String febrero;
		String marzo;
		String abril;
		String mayo;
		String junio;
		String julio;
		String agosto;
		String septiembre;
		String octubre;
		String noviembre;
		String diciembre;
		String usuario;
	}
	
	public class stavance{
		String fecha;
		String valor;
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
			sb.append(str);
		}
		;
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
				switch(meta.getDatoTipo().getId()){
					case 1: //texto
						temp.metaFinal = meta.getMetaFinalString();
						break;
					case 2: //entero
						temp.metaFinal = meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero().toString() : null;
						break;
					case 3: //decimal
						temp.metaFinal = meta.getMetaFinalDecimal() !=null  ? meta.getMetaFinalDecimal().toString(): null;
						break;
					case 4: //boolean
						temp.metaFinal = meta.getMetaFinalString();
						break;
					case 5: //fecha
						temp.metaFinal = Utils.formatDate(meta.getMetaFinalFecha());
				}
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
				switch(meta.getDatoTipo().getId()){
					case 1: //texto
						temp.metaFinal = meta.getMetaFinalString();
						break;
					case 2: //entero
						temp.metaFinal = meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero().toString() : null;
						break;
					case 3: //decimal
						temp.metaFinal = meta.getMetaFinalDecimal() !=null  ? meta.getMetaFinalDecimal().toString(): null;
						break;
					case 4: //boolean
						temp.metaFinal = meta.getMetaFinalString();
						break;
					case 5: //fecha
						temp.metaFinal = Utils.formatDate(meta.getMetaFinalFecha());
				}
				Set<MetaAvance> metaavances = meta.getMetaAvances();
				temp.avance = new stavance[metaavances.size()];
				int i=0;
				for (MetaAvance ma : metaavances) {
					temp.avance[i] = new stavance();
					temp.avance[i].fecha = Utils.formatDate(ma.getId().getFecha());
					switch(meta.getDatoTipo().getId()){
					case 1: //texto
						temp.avance[i].valor = ma.getValorString();
						break;
					case 2: //entero
						temp.avance[i].valor = ma.getValorEntero() != null ? ma.getValorEntero().toString() : null;
						break;
					case 3: //decimal
						temp.avance[i].valor = ma.getValorDecimal() !=null  ? ma.getValorDecimal().toString(): null;
						break;
					case 4: //boolean
						temp.avance[i].valor = ma.getValorString();
						break;
					case 5: //fecha
						temp.avance[i].valor = Utils.formatDate(ma.getValorTiempo());
					}
					temp.avance[i].usuario = ma.getUsuario();
					i++;
				}
				Set<MetaPlanificado> metaplanificado = meta.getMetaPlanificados();
				temp.planificado = new stplanificado[metaplanificado.size()];
				i=0;
				for (MetaPlanificado mp : metaplanificado) {
					temp.planificado[i] = new stplanificado();
					temp.planificado[i].ejercicio = mp.getId().getEjercicio();
					switch(meta.getDatoTipo().getId()){
						case 1: //texto
							temp.planificado[i].enero = mp.getEneroString();
							temp.planificado[i].febrero = mp.getFebreroString();
							temp.planificado[i].marzo = mp.getMarzoString();
							temp.planificado[i].abril = mp.getAbrilString();
							temp.planificado[i].mayo = mp.getMayoString();
							temp.planificado[i].junio = mp.getJunioString();
							temp.planificado[i].julio = mp.getJulioString();
							temp.planificado[i].agosto = mp.getAgostoString();
							temp.planificado[i].septiembre = mp.getSeptiembreString();
							temp.planificado[i].octubre = mp.getOctubreString();
							temp.planificado[i].noviembre = mp.getNoviembreString();
							temp.planificado[i].diciembre = mp.getDiciembreString();
							break;
						case 2: //entero
							temp.planificado[i].enero = mp.getEneroEntero() != null ? mp.getEneroEntero().toString() : null;
							temp.planificado[i].febrero = mp.getFebreroEntero() != null ? mp.getFebreroEntero().toString() : null;
							temp.planificado[i].marzo = mp.getMarzoEntero() != null ? mp.getMarzoEntero().toString() : null;
							temp.planificado[i].abril = mp.getAbrilEntero() != null ? mp.getAbrilEntero().toString() : null;
							temp.planificado[i].mayo = mp.getMayoEntero() != null ? mp.getMayoEntero().toString() : null;
							temp.planificado[i].junio = mp.getJunioEntero() != null ? mp.getJunioEntero().toString() : null;
							temp.planificado[i].julio = mp.getJulioEntero() != null ? mp.getJulioEntero().toString() : null;
							temp.planificado[i].agosto = mp.getAgostoEntero() != null ? mp.getAgostoEntero().toString() : null;
							temp.planificado[i].septiembre = mp.getSeptiembreEntero() != null ? mp.getSeptiembreEntero().toString() : null;
							temp.planificado[i].octubre = mp.getOctubreEntero() != null ? mp.getOctubreEntero().toString() : null;
							temp.planificado[i].noviembre = mp.getNoviembreEntero() != null ? mp.getNoviembreEntero().toString() : null;
							temp.planificado[i].diciembre = mp.getDiciembreEntero() != null ? mp.getDiciembreEntero().toString() : null;
							break;
						case 3: //decimal
							temp.planificado[i].enero = mp.getEneroDecimal() !=null  ? mp.getEneroDecimal().toString(): null;
							temp.planificado[i].febrero = mp.getFebreroDecimal() !=null  ? mp.getFebreroDecimal().toString(): null;
							temp.planificado[i].marzo = mp.getMarzoDecimal() !=null  ? mp.getMarzoDecimal().toString(): null;
							temp.planificado[i].abril = mp.getAbrilDecimal() !=null  ? mp.getAbrilDecimal().toString(): null;
							temp.planificado[i].mayo = mp.getMayoDecimal() !=null  ? mp.getMayoDecimal().toString(): null;
							temp.planificado[i].junio = mp.getJunioDecimal() !=null  ? mp.getJunioDecimal().toString(): null;
							temp.planificado[i].julio = mp.getJulioDecimal() !=null  ? mp.getJulioDecimal().toString(): null;
							temp.planificado[i].agosto = mp.getAgostoDecimal() !=null  ? mp.getAgostoDecimal().toString(): null;
							temp.planificado[i].septiembre = mp.getSeptiembreDecimal() !=null  ? mp.getSeptiembreDecimal().toString(): null;
							temp.planificado[i].octubre = mp.getOctubreDecimal() !=null  ? mp.getOctubreDecimal().toString(): null;
							temp.planificado[i].noviembre = mp.getNoviembreDecimal() !=null  ? mp.getNoviembreDecimal().toString(): null;
							temp.planificado[i].diciembre = mp.getDiciembreDecimal() !=null  ? mp.getDiciembreDecimal().toString(): null;
							break;
						case 4: //boolean
							temp.planificado[i].enero = mp.getEneroString();
							temp.planificado[i].febrero = mp.getFebreroString();
							temp.planificado[i].marzo = mp.getMarzoString();
							temp.planificado[i].abril = mp.getAbrilString();
							temp.planificado[i].mayo = mp.getMayoString();
							temp.planificado[i].junio = mp.getJunioString();
							temp.planificado[i].julio = mp.getJulioString();
							temp.planificado[i].agosto = mp.getAgostoString();
							temp.planificado[i].septiembre = mp.getSeptiembreString();
							temp.planificado[i].octubre = mp.getOctubreString();
							temp.planificado[i].noviembre = mp.getNoviembreString();
							temp.planificado[i].diciembre = mp.getDiciembreString();
							break;
						case 5: //fecha
							temp.planificado[i].enero = Utils.formatDate(mp.getEneroTiempo());
							temp.planificado[i].febrero = Utils.formatDate(mp.getFebreroTiempo());
							temp.planificado[i].marzo = Utils.formatDate(mp.getMarzoTiempo());
							temp.planificado[i].abril = Utils.formatDate(mp.getAbrilTiempo());
							temp.planificado[i].mayo = Utils.formatDate(mp.getMayoTiempo());
							temp.planificado[i].junio = Utils.formatDate(mp.getJunioTiempo());
							temp.planificado[i].julio = Utils.formatDate(mp.getJulioTiempo());
							temp.planificado[i].agosto = Utils.formatDate(mp.getAgostoTiempo());
							temp.planificado[i].septiembre = Utils.formatDate(mp.getSeptiembreTiempo());
							temp.planificado[i].octubre = Utils.formatDate(mp.getOctubreTiempo());
							temp.planificado[i].noviembre = Utils.formatDate(mp.getNoviembreTiempo());
							temp.planificado[i].diciembre = Utils.formatDate(mp.getDiciembreTiempo());
						}
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
				String metaFinal = objeto.get("metaFinal").isJsonNull() ? null : objeto.get("metaFinal").getAsString();
				String usuarioActualizo = null;
				Date fechaActualizacion =  null;				
				Integer metaFinalEntero=null;
				String metaFinalString=null;
				BigDecimal metaFinalDecimal = null;
				Date metaFinalFecha = null;
				if(metaFinal!=null){
					switch(datoTipoId){
						case 1: //texto
							metaFinalString = metaFinal;
							break;
						case 2: //entero
							metaFinalEntero = Integer.parseInt(metaFinal);
							break;
						case 3: //decimal
							metaFinalDecimal = new BigDecimal(metaFinal);
							break;
						case 4: //boolean
							metaFinalString = metaFinal;
							break;
						case 5: //fecha
							metaFinalFecha = Utils.dateFromString(metaFinal);
					}
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
					meta.setMetaFinalFecha(metaFinalFecha);
					MetaDAO.guardarMeta(meta);
					result = MetaDAO.borrarPlanificadoAvanceMeta(meta);
				}else{
					meta = new Meta(datoTipo, metaUnidadMedida, nombre, descripcion, usuario, usuarioActualizo, new Date(), fechaActualizacion, estado, 
							objetoId, objetoTipo, metaFinalEntero, metaFinalString, metaFinalDecimal, metaFinalFecha, null, null);
					result = MetaDAO.guardarMeta(meta);
				}
				
				if(result){
					//TODO: obtener planificados
					JsonArray planificados = objeto.get("planificado").getAsJsonArray();
					for(int p=0; p<planificados.size(); p++){
						JsonObject planificado = planificados.get(p).getAsJsonObject();
						Integer ejercicio = planificado.get("ejercicio").getAsInt();
						String enero = planificado.get("enero").isJsonNull() ? null : planificado.get("enero").getAsString();
						String febrero = planificado.get("febrero").isJsonNull() ? null : planificado.get("febrero").getAsString();
						String marzo = planificado.get("marzo").isJsonNull() ? null : planificado.get("marzo").getAsString();
						String abril = planificado.get("abril").isJsonNull() ? null : planificado.get("abril").getAsString();
						String mayo = planificado.get("mayo").isJsonNull() ? null : planificado.get("mayo").getAsString();
						String junio = planificado.get("junio").isJsonNull() ? null : planificado.get("junio").getAsString();
						String julio = planificado.get("julio").isJsonNull() ? null : planificado.get("julio").getAsString();
						String agosto = planificado.get("agosto").isJsonNull() ? null : planificado.get("agosto").getAsString();
						String septiembre = planificado.get("septiembre").isJsonNull() ? null : planificado.get("septiembre").getAsString();
						String octubre = planificado.get("octubre").isJsonNull() ? null : planificado.get("octubre").getAsString();
						String noviembre = planificado.get("noviembre").isJsonNull() ? null : planificado.get("noviembre").getAsString();
						String diciembre = planificado.get("diciembre").isJsonNull() ? null : planificado.get("diciembre").getAsString();
						
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
								eneroString = enero;
								febreroString = febrero;
								marzoString = marzo;
								abrilString = abril;
								mayoString = mayo;
								junioString = junio;
								julioString = julio;
								agostoString = agosto;
								septiembreString = septiembre;
								octubreString = octubre;
								noviembreString = noviembre;
								diciembreString = diciembre;
								break;
							case 2: //entero
								eneroEntero = enero!=null ? Integer.parseInt(enero) : null;
								febreroEntero = febrero!=null ? Integer.parseInt(febrero) : null;
								marzoEntero = marzo!=null ? Integer.parseInt(marzo) : null;
								abrilEntero = abril!=null ? Integer.parseInt(abril) : null;
								mayoEntero = mayo!=null ? Integer.parseInt(mayo) : null;
								junioEntero = junio!=null ? Integer.parseInt(junio) : null;
								julioEntero = julio!=null ? Integer.parseInt(julio) : null;
								agostoEntero = agosto!=null ? Integer.parseInt(agosto) : null;
								septiembreEntero = septiembre!=null ? Integer.parseInt(septiembre) : null;
								octubreEntero = octubre!=null ? Integer.parseInt(octubre) : null;
								noviembreEntero = noviembre!=null ? Integer.parseInt(noviembre) : null;
								diciembreEntero = diciembre!=null ? Integer.parseInt(diciembre) : null;
								break;
							case 3: //decimal
								eneroDecimal = enero!=null ? new BigDecimal(enero) : null;
								febreroDecimal = febrero!=null ? new BigDecimal(febrero) : null;
								marzoDecimal = marzo!=null ? new BigDecimal(marzo) : null;
								abrilDecimal = abril!=null ? new BigDecimal(abril) : null;
								mayoDecimal = mayo!=null ? new BigDecimal(mayo) : null;
								junioDecimal = junio!=null ? new BigDecimal(junio) : null;
								julioDecimal = julio!=null ? new BigDecimal(julio) : null;
								agostoDecimal = agosto!=null ? new BigDecimal(agosto) : null;
								septiembreDecimal = septiembre!=null ? new BigDecimal(septiembre) : null;
								octubreDecimal = octubre!=null ? new BigDecimal(octubre) : null;
								noviembreDecimal = noviembre!=null ? new BigDecimal(noviembre) : null;
								diciembreDecimal = diciembre!=null ? new BigDecimal(diciembre) : null;
								break;
							case 4: //boolean
								eneroString = enero;
								febreroString = febrero;
								marzoString = marzo;
								abrilString = abril;
								mayoString = mayo;
								junioString = junio;
								julioString = julio;
								agostoString = agosto;
								septiembreString = septiembre;
								octubreString = octubre;
								noviembreString = noviembre;
								diciembreString = diciembre;
								break;
							case 5: //Tiempo
								eneroTiempo = enero!=null ? Utils.dateFromString(enero) : null;
								febreroTiempo = febrero!=null ? Utils.dateFromString(febrero) : null;
								marzoTiempo = marzo!=null ? Utils.dateFromString(marzo) : null;
								abrilTiempo = abril!=null ? Utils.dateFromString(abril) : null;
								mayoTiempo = mayo!=null ? Utils.dateFromString(mayo) : null;
								junioTiempo = junio!=null ? Utils.dateFromString(junio) : null;
								julioTiempo = julio!=null ? Utils.dateFromString(julio) : null;
								agostoTiempo = agosto!=null ? Utils.dateFromString(agosto) : null;
								septiembreTiempo = septiembre!=null ? Utils.dateFromString(septiembre) : null;
								octubreTiempo = octubre!=null ? Utils.dateFromString(octubre) : null;
								noviembreTiempo = noviembre!=null ? Utils.dateFromString(noviembre) : null;
								diciembreTiempo = diciembre!=null ? Utils.dateFromString(diciembre) : null;
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
					
					//TODO: obtener avances
					JsonArray avances = objeto.get("avance").getAsJsonArray();
					ArrayList<Date> fechasUtilizadas = new ArrayList<Date>(); 
					for(int a=0; a<avances.size(); a++){
						JsonObject avance = avances.get(a).getAsJsonObject();
						Date fecha = Utils.dateFromString(avance.get("fecha").getAsString());
						String valor = avance.get("valor").isJsonNull() ? null : avance.get("valor").getAsString();
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
						if(valor!=null){
							switch(datoTipoId){
								case 1: //texto
									valorString = valor;
									break;
								case 2: //entero
									valorEntero = Integer.parseInt(valor);
									break;
								case 3: //decimal
									valorDecimal = new BigDecimal(valor);
									break;
								case 4: //boolean
									valorString = valor;
									break;
								case 5: //fecha
									valorTiempo = Utils.dateFromString(valor);
							}
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