package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
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
import com.google.gson.reflect.TypeToken;

import dao.ComponenteDAO;
import dao.ObjetoDAO;
import dao.ProductoDAO;
import dao.ProductoPropiedadDAO;
import dao.ProductoPropiedadValorDAO;
import dao.ProductoUsuarioDAO;
import dao.UnidadEjecutoraDAO;
import dao.UsuarioDAO;
import pojo.AcumulacionCosto;
import pojo.Componente;
import pojo.Producto;
import pojo.ProductoPropiedad;
import pojo.ProductoPropiedadValor;
import pojo.ProductoPropiedadValorId;
import pojo.ProductoTipo;
import pojo.ProductoUsuario;
import pojo.ProductoUsuarioId;
import pojo.UnidadEjecutora;
import pojo.Usuario;
import utilities.Utils;
import utilities.CLogger;

@WebServlet("/SProducto")
public class SProducto extends HttpServlet {
	
	private static final long serialVersionUID = 1457438583225714402L;
	
	static class stproducto {
		Integer id;
		String nombre;
		String descripcion;
		Integer idComponente;
		String componente;
		Integer idProductoTipo;
		String productoTipo;
		Integer unidadEjectuora;
		Integer entidadentidad;
		Integer ejercicio;
		String nombreUnidadEjecutora;
		String entidadnombre;
		Long snip;
		Integer programa;
		Integer subprograma;
		Integer proyecto_;
		Integer actividad;
		Integer obra;
		Integer renglon;
		Integer ubicacionGeografica;
		Integer duracion;
		String duracionDimension;
		String fechaInicio;
		String fechaFin;
		Integer estado;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
		String latitud;
		String longitud;
		Integer peso;
		BigDecimal costo;
		Integer acumulacionCostoId;
		String acumulacionCostoNombre;
		boolean tieneHijos;
	}
	
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
	}

	public SProducto() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Map<String, String> parametro = Utils.getParams(request);
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String accion = parametro.get("accion");
		String response_text="";

		if (accion.equals("cargar")) {
			int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			String columna_ordenada = parametro.get("columna_ordenada");
			String orden_direccion = parametro.get("orden_direccion");

			List<Producto> productos = ProductoDAO.getProductosPagina(pagina, registros,componenteid
					,filtro_nombre, filtro_usuario_creo,filtro_fecha_creacion
					,columna_ordenada,orden_direccion,usuario);
			List<stproducto> listaProducto = new ArrayList<stproducto>();

			for (Producto producto : productos) {
				stproducto temp = new stproducto();
				temp.id = producto.getId();
				temp.nombre = producto.getNombre();
				temp.descripcion = producto.getDescripcion();
				temp.programa = producto.getPrograma();
				temp.subprograma = producto.getSubprograma();
				temp.proyecto_ = producto.getProyecto();
				temp.obra = producto.getObra();
				temp.actividad = producto.getActividad();
				temp.renglon = producto.getRenglon();
				temp.ubicacionGeografica = producto.getUbicacionGeografica();
				temp.duracion = producto.getDuracion();
				temp.duracionDimension = producto.getDuracionDimension();
				temp.fechaInicio = Utils.formatDate(producto.getFechaInicio());
				temp.fechaFin = Utils.formatDate(producto.getFechaFin());
				temp.snip = producto.getSnip();
				temp.estado = producto.getEstado();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				temp.latitud = producto.getLatitud();
				temp.longitud = producto.getLongitud();
				temp.peso = producto.getPeso();
				temp.costo = producto.getCosto();
				temp.acumulacionCostoId = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getId() : null;
				temp.acumulacionCostoNombre = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getNombre() : null;

				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.entidadentidad = producto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.ejercicio = producto.getUnidadEjecutora().getId().getEjercicio();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = producto.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(componenteid);
					if (componente!=null && componente.getUnidadEjecutora()!=null){
						temp.unidadEjectuora = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
						temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
						temp.nombreUnidadEjecutora = componente.getUnidadEjecutora().getNombre();
						temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
					}
				}

				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 3);
				listaProducto.add(temp);
			}


				response_text=new GsonBuilder().serializeNulls().create().toJson(listaProducto);
		        response_text = String.join("", "\"productos\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");	
			
		} else if (accion.equals("guardar")) {
			boolean esnuevo = parametro.get("esnuevo").equals("true");
			int id = Utils.String2Int(parametro.get("id"));
			Producto producto;
			boolean ret = false;
			if (id>0 || esnuevo){
				try{
				String nombre = parametro.get("nombre");
				String descripcion = parametro.get("descripcion");
				
				Integer componenteId = Utils.String2Int(parametro.get("componente"));
				Integer tipoproductoId = Utils.String2Int(parametro.get("tipoproductoid"));
				
				Integer unidadEjecutoraId = Utils.String2Int(parametro.get("unidadEjecutora"));
				Integer entidad = Utils.String2Int(parametro.get("entidad"));
				Integer ejercicio = Utils.String2Int(parametro.get("ejercicio"));
				
				Long snip = Utils.String2Long(parametro.get("snip"), null);
				Integer programa = Utils.String2Int(parametro.get("programa"), null);
				Integer subprograma = Utils.String2Int(parametro.get("subprograma"), null);
				Integer proyecto_ = Utils.String2Int(parametro.get("proyecto_"), null);
				Integer obra = Utils.String2Int(parametro.get("obra"), null);
				Integer renglon = parametro.get("renglon")!=null ? Integer.parseInt(parametro.get("renglon")):null;
				Integer ubicacionGeografica = parametro.get("ubicacionGeografica")!=null ? Integer.parseInt(parametro.get("ubicacionGeografica")):null;
				Integer actividad = Utils.String2Int(parametro.get("actividad"), null);
				String latitud = parametro.get("latitud");
				String longitud = parametro.get("longitud");
				Integer peso = Utils.String2Int(parametro.get("peso"), null);
				BigDecimal costo = Utils.String2BigDecimal(parametro.get("costo"), null);
				Integer acumulacionCostoid = Utils.String2Int(parametro.get("acumulacionCosto"), null);
				Date fechaInicio = Utils.dateFromStringCeroHoras(parametro.get("fechaInicio"));
				Date fechaFin = Utils.dateFromStringCeroHoras(parametro.get("fechaFin"));
				Integer duracion = Utils.String2Int(parametro.get("duaracion"), null);
				String duracionDimension = parametro.get("duracionDimension");
				
				AcumulacionCosto acumulacionCosto = null;
				if(acumulacionCostoid!= null && acumulacionCostoid > 0){
					acumulacionCosto =   new AcumulacionCosto();
					acumulacionCosto.setId(Utils.String2Int(parametro.get("acumulacionCosto")));
				}
				
				Gson gson = new Gson();
			
				Type type = new TypeToken<List<stdatadinamico>>() {
				}.getType();

				List<stdatadinamico> datos = gson.fromJson(parametro.get("datadinamica"), type);
				Componente componente = ComponenteDAO.getComponente(componenteId);
				
				
				ProductoTipo productoTipo = new ProductoTipo();
				productoTipo.setId(tipoproductoId);
				new UnidadEjecutoraDAO();
				UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio,entidad,unidadEjecutoraId);
				
				if (esnuevo){
					
					producto = new Producto(acumulacionCosto, componente, productoTipo, unidadEjecutora, nombre, descripcion, 
							usuario, null, new DateTime().toDate(), null, 1, snip, programa, subprograma, proyecto_, 
							actividad, obra, latitud, longitud,null,costo, renglon, ubicacionGeografica,fechaInicio, 
							fechaFin, duracion, duracionDimension,null,null,null,null,null,null);
					
				}else{
					producto = ProductoDAO.getProductoPorId(id);
					producto.setComponente(componente);
					producto.setProductoTipo(productoTipo);
					producto.setUnidadEjecutora(unidadEjecutora);
					producto.setNombre(nombre);
					producto.setDescripcion(descripcion);
					producto.setSnip(snip);
					producto.setPrograma(programa);
					producto.setSubprograma(subprograma);
					producto.setProyecto(proyecto_);
					producto.setObra(obra);
					producto.setActividad(actividad);
					producto.setRenglon(renglon);
					producto.setUbicacionGeografica(ubicacionGeografica);
					producto.setUsuarioActualizo(usuario);
					producto.setFechaActualizacion(new DateTime().toDate());
					producto.setLatitud(latitud);
					producto.setLongitud(longitud);
					producto.setPeso(peso);
					producto.setCosto(costo);
					producto.setAcumulacionCosto(acumulacionCosto);
					producto.setFechaInicio(fechaInicio);
					producto.setFechaFin(fechaFin);
					producto.setDuracion(duracion);
					producto.setDuracionDimension(duracionDimension);
				}
				
				ret = ProductoDAO.guardarProducto(producto, true);
				
				if (ret){
					ProductoUsuarioId productoUsuarioId = new ProductoUsuarioId(producto.getId(), usuario);
					Usuario usu = UsuarioDAO.getUsuario(usuario);
					ProductoUsuario productoUsuario =  new ProductoUsuario(productoUsuarioId, producto, usu,usuario, null, new DateTime().toDate(),null);
					ProductoUsuarioDAO.guardarProductoUsuario(productoUsuario);
					
					for (stdatadinamico data : datos) {
						if (data.id != null && data.valor!=null && data.valor.compareTo("null")!=0 && data.valor.length()>0 ){
							ProductoPropiedad producotPropiedad = ProductoPropiedadDAO.getProductoPropiedadPorId(Integer.parseInt(data.id));
							ProductoPropiedadValorId idValor = new ProductoPropiedadValorId(Integer.parseInt(data.id),producto.getId());
							ProductoPropiedadValor valor = new ProductoPropiedadValor(idValor, producto, producotPropiedad, null, null, null, null, 
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
							ret = (ret && ProductoPropiedadValorDAO.guardarProductoPropiedadValor(valor));
						}
					}
				}
				
				/*if(ret){
					COrden orden = new COrden();
					ret = orden.calcularOrdenObjetosSuperiores(producto.getComponente().getId(), 2, usuario, COrden.getSessionCalculoOrden(), null, null, producto.getId());				
					
				}*/
				
				response_text = String.join("","{ \"success\": ",(ret ? "true" : "false"),", "
						, "\"id\": " , producto.getId().toString() , ","
						, "\"usuarioCreo\": \"" , producto.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(producto.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , producto.getUsuarioActualizo() != null ? producto.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(producto.getFechaActualizacion()),"\""
						," }");
				}
				catch (Throwable e){
					CLogger.write("1", SProducto.class, e);
					response_text = "{ \"success\": false }";
				}
				
			}else {
				response_text = "{ \"success\": false }";
			}
		} else if (accion.equals("borrar")) {
			int codigo = Utils.String2Int(parametro.get("codigo"), -1);
			
			//Producto pojo = ProductoDAO.getProductoPorId(codigo,usuario);
			boolean eliminado = ProductoDAO.eliminar(codigo, usuario);
			
			if (eliminado) {
				/*COrden orden = new COrden();
				orden.calcularOrdenObjetosSuperiores(pojo.getComponente().getId(), 2, usuario, COrden.getSessionCalculoOrden(),null, null, codigo);		
				*/
				
				int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
				int pagina = Utils.String2Int(parametro.get("pagina"), 1);
				int registros = Utils.String2Int(parametro.get("registros"), 20);
				String filtro_nombre = parametro.get("filtro_nombre");
				String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
				String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
				String columna_ordenada = parametro.get("columna_ordenada");
				String orden_direccion = parametro.get("orden_direccion");

				List<Producto> productos = ProductoDAO.getProductosPagina(pagina, registros,componenteid
						,filtro_nombre, filtro_usuario_creo,filtro_fecha_creacion
						,columna_ordenada,orden_direccion,usuario);
				List<stproducto> listaProducto = new ArrayList<stproducto>();

				for (Producto producto : productos) {
					stproducto temp = new stproducto();
					temp.id = producto.getId();
					temp.nombre = producto.getNombre();
					temp.descripcion = producto.getDescripcion();
					temp.programa = producto.getPrograma();
					temp.subprograma = producto.getSubprograma();
					temp.proyecto_ = producto.getProyecto();
					temp.obra = producto.getObra();
					temp.actividad = producto.getActividad();
					temp.renglon = producto.getRenglon();
					temp.ubicacionGeografica = producto.getUbicacionGeografica();
					temp.snip = producto.getSnip();
					temp.estado = producto.getEstado();
					temp.usuarioCreo = producto.getUsuarioCreo();
					temp.usuarioactualizo = producto.getUsuarioActualizo();
					temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
					temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
					temp.latitud = producto.getLatitud();
					temp.longitud = producto.getLongitud();
					temp.peso = producto.getPeso();
					temp.costo = producto.getCosto();
					temp.acumulacionCostoId = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getId() : null;
					temp.acumulacionCostoNombre = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getNombre() : null;
					temp.fechaInicio = Utils.formatDate(producto.getFechaInicio());
					temp.fechaFin = Utils.formatDate(producto.getFechaFin());
					temp.duracion = producto.getDuracion();
					temp.duracionDimension = producto.getDuracionDimension();
					temp.acumulacionCostoId = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getId() : null;
					temp.acumulacionCostoNombre = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getNombre() : null;
					
					if (producto.getComponente() != null) {
						temp.idComponente = producto.getComponente().getId();
						temp.componente = producto.getComponente().getNombre();
					}

					if (producto.getProductoTipo() != null) {
						temp.idProductoTipo = producto.getProductoTipo().getId();
						temp.productoTipo = producto.getProductoTipo().getNombre();
					}
					
					if (producto.getUnidadEjecutora() != null){
						temp.unidadEjectuora = producto.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.entidadentidad = producto.getUnidadEjecutora().getId().getEntidadentidad();
						temp.ejercicio = producto.getUnidadEjecutora().getId().getEjercicio();
						temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
						temp.entidadnombre = producto.getUnidadEjecutora().getEntidad().getNombre();
					}else{
						Componente componente = ComponenteDAO.getComponente(componenteid);
						if (componente!=null && componente.getUnidadEjecutora()!=null){
							temp.unidadEjectuora = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
							temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
							temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
							temp.nombreUnidadEjecutora = componente.getUnidadEjecutora().getNombre();
							temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
						}
					}

					temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 3);
					
					listaProducto.add(temp);
				}


					response_text=new GsonBuilder().serializeNulls().create().toJson(listaProducto);
			        response_text =  "\"productos\":" + response_text;
			        response_text = "{\"success\":true," + response_text + "}";	
				
			}
		} else if (accion.equals("totalElementos")) {
			int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			Long total = ProductoDAO.getTotalProductos(componenteid,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,usuario);

			response_text = "{\"success\":true, \"total\":" + total + "}";
			
		} else if (accion.equals("listarTipos")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);
			int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
			
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			String columna_ordenada = parametro.get("columna_ordenada");
			String orden_direccion = parametro.get("orden_direccion");
			
			List<Producto> productos = ProductoDAO.getProductosPagina(pagina, registros,componenteid,usuario,
					filtro_nombre,filtro_usuario_creo
					,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stproducto> listaProducto = new ArrayList<stproducto>();

			for (Producto producto : productos) {
				stproducto temp = new stproducto();
				temp.id = producto.getId();
				temp.nombre = producto.getNombre();
				temp.descripcion = producto.getDescripcion();
				temp.programa = producto.getPrograma();
				temp.subprograma = producto.getSubprograma();
				temp.proyecto_ = producto.getProyecto();
				temp.obra = producto.getObra();
				temp.actividad = producto.getActividad();
				temp.renglon = producto.getRenglon();
				temp.ubicacionGeografica = producto.getUbicacionGeografica();
				temp.snip = producto.getSnip();
				temp.estado = producto.getEstado();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				temp.latitud = producto.getLatitud();
				temp.longitud = producto.getLongitud();
				temp.peso = producto.getPeso();
				temp.costo = producto.getCosto();
				temp.acumulacionCostoId = producto.getAcumulacionCosto()!= null ? producto.getAcumulacionCosto().getId(): null;
				temp.acumulacionCostoNombre = producto.getAcumulacionCosto()!= null ? producto.getAcumulacionCosto().getNombre() : null;
				temp.fechaInicio = Utils.formatDate(producto.getFechaInicio());
				temp.fechaFin = Utils.formatDate(producto.getFechaFin());
				temp.duracion = producto.getDuracion();
				temp.duracionDimension = producto.getDuracionDimension();
				temp.acumulacionCostoId = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getId() : null;
				temp.acumulacionCostoNombre = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getNombre() : null;
				
				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.entidadentidad = producto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.ejercicio = producto.getUnidadEjecutora().getId().getEjercicio();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = producto.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(componenteid);
					if (componente!=null && componente.getUnidadEjecutora()!=null){
						temp.unidadEjectuora = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
						temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
						temp.nombreUnidadEjecutora = componente.getUnidadEjecutora().getNombre();
						temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
					}
				}

				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 3);
				
				listaProducto.add(temp);
			}


				response_text=new GsonBuilder().serializeNulls().create().toJson(listaProducto);
		        response_text =  "\"productos\":" + response_text;
		        response_text = "{\"success\":true," + response_text + "}";	
			
		} else if (accion.equals("listarProductos")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);
			int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			String columna_ordenada = parametro.get("columna_ordenada");
			String orden_direccion = parametro.get("orden_direccion");

			List<Producto> productos = ProductoDAO.getProductosPagina(pagina, registros,componenteid,
					filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion,usuario);
			List<stproducto> stproductos=new ArrayList<stproducto>();
			
			for(Producto producto:productos){
				stproducto temp = new stproducto();
				temp.id = producto.getId();
				temp.nombre = producto.getNombre();
				temp.descripcion = producto.getDescripcion();
				temp.programa = producto.getPrograma();
				temp.subprograma = producto.getSubprograma();
				temp.proyecto_ = producto.getProyecto();
				temp.obra = producto.getObra();
				temp.actividad = producto.getActividad();
				temp.renglon = producto.getRenglon();
				temp.ubicacionGeografica = producto.getUbicacionGeografica();
				temp.snip = producto.getSnip();
				temp.estado = producto.getEstado();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				temp.latitud = producto.getLatitud();
				temp.longitud = producto.getLongitud();
				temp.peso = producto.getPeso();
				temp.costo = producto.getCosto();
				temp.acumulacionCostoId = producto.getAcumulacionCosto()!= null ? producto.getAcumulacionCosto().getId(): null;
				temp.acumulacionCostoNombre = producto.getAcumulacionCosto()!= null ? producto.getAcumulacionCosto().getNombre() : null;
				temp.fechaInicio = Utils.formatDate(producto.getFechaInicio());
				temp.fechaFin = Utils.formatDate(producto.getFechaFin());
				temp.duracion = producto.getDuracion();
				temp.duracionDimension = producto.getDuracionDimension();
				temp.acumulacionCostoId = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getId() : null;
				temp.acumulacionCostoNombre = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getNombre() : null;
				
				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.entidadentidad = producto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.ejercicio = producto.getUnidadEjecutora().getId().getEjercicio();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = producto.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(componenteid);
					if (componente!=null && componente.getUnidadEjecutora()!=null){
						temp.unidadEjectuora = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
						temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
						temp.nombreUnidadEjecutora = componente.getUnidadEjecutora().getNombre();
						temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
					}
				}

				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 3);
				
				stproductos.add(temp);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(stproductos);
			response_text = String.join("", "\"productos\":",response_text);
			response_text = String.join("", "{\"success\":true,", response_text,"}");
			
		} else if (accion.equals("listarComponentes")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);

			response_text = Utils.getJSonString("productos", ComponenteDAO.getComponentesPagina(pagina, registros,usuario));

			if (Utils.isNullOrEmpty(response_text)) {
				response_text = "{\"success\":false}";
			} else {
				response_text = "{\"success\":true," + response_text + "}";
			}

		}else if(accion.equals("obtenerProductoPorId")){
			Integer id = parametro.get("id")!=null ? Integer.parseInt(parametro.get("id")) : 0;
			Producto producto = ProductoDAO.getProductoPorId(id,usuario);

			response_text = String.join("","{ \"success\": ",(producto!=null && producto.getId()!=null ? "true" : "false"),", "
				+ "\"id\": " + (producto!=null ? producto.getId():"0") +", "  + "\"fechaInicio\": \"" + (producto!=null ? Utils.formatDate(producto.getFechaInicio()): null) +"\", "
				+ "\"nombre\": \"" + (producto!=null ? producto.getNombre():"Indefinido") +"\" }");

		}else if(accion.equals("getProductoPorId")){
			Integer id = parametro.get("id")!=null ? Integer.parseInt(parametro.get("id")) : 0;
			Producto producto = ProductoDAO.getProductoPorId(id,usuario);
			stproducto temp = new stproducto();
			if (producto !=null) {
				temp.id = producto.getId();
				temp.nombre = producto.getNombre();
				temp.costo = producto.getCosto()!=null ? producto.getCosto() : new BigDecimal(0);
				temp.acumulacionCostoId = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getId() : null;
				temp.acumulacionCostoNombre = producto.getAcumulacionCosto()!=null ? producto.getAcumulacionCosto().getNombre() : null;
				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.entidadentidad = producto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.ejercicio = producto.getUnidadEjecutora().getId().getEjercicio();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = producto.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(producto.getComponente().getId());
					if (componente!=null && componente.getUnidadEjecutora()!=null){
						temp.unidadEjectuora = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
						temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
						temp.nombreUnidadEjecutora = componente.getUnidadEjecutora().getNombre();
						temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
					}
				}
				temp.fechaInicio = Utils.formatDate(producto.getFechaInicio());
				temp.fechaFin = Utils.formatDate(producto.getFechaFin());
				temp.duracion = producto.getDuracion();
				temp.duracionDimension = producto.getDuracionDimension();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 3);
			}

			response_text = new GsonBuilder().serializeNulls().create().toJson(temp);
			response_text = String.join("", "\"producto\":",response_text);
			response_text = String.join("", "{\"success\":true,", response_text,"}");

		} else if (accion.equals("guardarModal")) {
			boolean ret = false;
			int id = Utils.String2Int(parametro.get("id"));
			boolean esnuevo = parametro.get("esnuevo").equals("true");
			Integer componenteId = Utils.String2Int(parametro.get("componenteId"));
			Producto producto = null;
			
			if(id>0 || esnuevo ){
			
				String nombre = parametro.get("nombre");
				Integer tipoproductoId = Utils.String2Int(parametro.get("tipoproductoid")); 
				Integer unidadEjecutoraId = Utils.String2Int(parametro.get("unidadEjecutora"));
				Integer entidadId = Utils.String2Int(parametro.get("entidadId"));
				Integer ejercicio = Utils.String2Int("ejercicio");
				Date fechaInicio = Utils.dateFromString(parametro.get("fechaInicio"));
				Date fechaFin = Utils.dateFromString(parametro.get("fechaFin"));
				Integer duracion = Utils.String2Int(parametro.get("duaracion"), null);
				String duracionDimension = parametro.get("duracionDimension");
				
				ProductoTipo productoTipo = new ProductoTipo();
				productoTipo.setId(tipoproductoId);
				UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(ejercicio, entidadId, unidadEjecutoraId);
				if(esnuevo){
					Componente componente = new Componente();
					componente.setId(componenteId);
					producto = new Producto(componente, productoTipo,  nombre, usuario, new Date());
					producto.setEstado(1);
					producto.setFechaInicio(fechaInicio);
					producto.setFechaFin(fechaFin);
					producto.setDuracion(duracion);
					producto.setDuracionDimension(duracionDimension);
				}else{
					producto = ProductoDAO.getProductoPorId(id);
					producto.setProductoTipo(productoTipo);
					producto.setUnidadEjecutora(unidadEjecutora);
					producto.setNombre(nombre);
					producto.setUsuarioActualizo(usuario);
					producto.setFechaActualizacion(new DateTime().toDate());
					producto.setFechaInicio(fechaInicio);
					producto.setFechaFin(fechaFin);
					producto.setDuracion(duracion);
					producto.setDuracionDimension(duracionDimension);
				}
				
				ret = ProductoDAO.guardarProducto(producto, true);
				
				/*COrden orden = new COrden();
				orden.calcularOrdenObjetosSuperiores(producto.getComponente().getId(), 2, usuario, COrden.getSessionCalculoOrden(),
						null, null, producto.getId()); */
			}
			
			stproducto temp = new stproducto();
			if (ret) {
				temp.id = producto.getId();
				temp.nombre = producto.getNombre();
				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getId().getUnidadEjecutora();
					temp.entidadentidad = producto.getUnidadEjecutora().getId().getEntidadentidad();
					temp.ejercicio = producto.getUnidadEjecutora().getId().getEjercicio();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
					temp.entidadnombre = producto.getUnidadEjecutora().getEntidad().getNombre();
				}else{
					Componente componente = ComponenteDAO.getComponente(producto.getId());
					if (componente!=null && componente.getUnidadEjecutora()!=null){
						temp.unidadEjectuora = componente.getUnidadEjecutora().getId().getUnidadEjecutora();
						temp.entidadentidad = componente.getUnidadEjecutora().getId().getEntidadentidad();
						temp.ejercicio = componente.getUnidadEjecutora().getId().getEjercicio();
						temp.nombreUnidadEjecutora = componente.getUnidadEjecutora().getNombre();
						temp.entidadnombre = componente.getUnidadEjecutora().getEntidad().getNombre();
					}
				}
				
				
				temp.fechaInicio = Utils.formatDate(producto.getFechaInicio());
				temp.fechaFin = Utils.formatDate(producto.getFechaFin());
				temp.duracion = producto.getDuracion();
				temp.duracionDimension = producto.getDuracionDimension();
				
				temp.tieneHijos = ObjetoDAO.tieneHijos(temp.id, 3);
				
				response_text = new GsonBuilder().serializeNulls().create().toJson(temp);
				response_text = String.join("", "\"producto\":",response_text);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else{
				response_text = "{ \"success\": false }";
			}

			
				
				
		}else if (accion.equals("getProductoPorProyecto")) {
			Integer idProyecto = Utils.String2Int(parametro.get("idProyecto"));
			List<Producto> productos = ProductoDAO.getProductosPorProyecto(idProyecto, usuario);
			
			List<stproducto> stproductos=new ArrayList<stproducto>();
			
			for(Producto producto:productos){
				stproducto temp = new stproducto();
				temp.id = producto.getId();
				temp.nombre = producto.getNombre();
				temp.descripcion = producto.getDescripcion();
				temp.estado = producto.getEstado();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				temp.peso = producto.getPeso();
				stproductos.add(temp);
			}
			
			response_text = new GsonBuilder().serializeNulls().create().toJson(stproductos);
			response_text = String.join("", "\"productos\":",response_text);
			response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if (accion.equals("guardarPesoProducto")) {
			String param_productos = parametro.get("productos");
			String[] split = param_productos.split("~");
			boolean ret = true;
			for (int i = 0; i<split.length;i++){
				String[] temp = split[i].split(",");
				if (temp.length == 2){
					Producto producto = ProductoDAO.getProductoPorId(Utils.String2Int(temp[0],0));
					if (producto!=null){
						producto.setPeso(Utils.String2Int(temp[1]));
						producto.setUsuarioActualizo(usuario);
						producto.setFechaActualizacion(new Date());
						ret = ProductoDAO.guardarProducto(producto, true);
					}
				}
			}
			response_text = String.join("", "{ \"success\": ",(ret ? "true":"false")," }");
			
		}
		else 
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