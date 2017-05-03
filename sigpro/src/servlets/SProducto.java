package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.lang.reflect.Type;
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
import dao.ProductoDAO;
import dao.ProductoPropiedadDAO;
import dao.ProductoPropiedadValorDAO;
import dao.ProductoUsuarioDAO;
import pojo.Componente;
import pojo.Producto;
import pojo.ProductoPropiedad;
import pojo.ProductoPropiedadValor;
import pojo.ProductoPropiedadValorId;
import pojo.ProductoTipo;
import pojo.ProductoUsuario;
import pojo.ProductoUsuarioId;
import pojo.UnidadEjecutora;
import utilities.Utils;

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
		String nombreUnidadEjecutora;
		Long snip;
		Integer programa;
		Integer subprograma;
		Integer proyecto_;
		Integer actividad;
		Integer obra;
		Integer fuente;
		Integer estado;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
		String latitud;
		String longitud;
		
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
				temp.fuente = producto.getFuente();
				temp.snip = producto.getSnip();
				temp.estado = producto.getEstado();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				temp.latitud = producto.getLatitud();
				temp.longitud = producto.getLongitud();
				

				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getUnidadEjecutora();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
				}

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
				Integer productoPadreId = Utils.String2Int(parametro.get("productoPadre"));
				Integer tipoproductoId = Utils.String2Int(parametro.get("tipoproductoid")); 
				Integer unidadEjecutoraId = Utils.String2Int(parametro.get("unidadEjecutora"));
				
				Long snip = Utils.String2Long(parametro.get("snip"), null);
				Integer programa = Utils.String2Int(parametro.get("programa"), null);
				Integer subprograma = Utils.String2Int(parametro.get("subprograma"), null);
				Integer proyecto_ = Utils.String2Int(parametro.get("proyecto_"), null);
				Integer obra = Utils.String2Int(parametro.get("obra"), null);
				Integer fuente = Utils.String2Int(parametro.get("fuente"), null);
				Integer actividad = Utils.String2Int(parametro.get("actividad"), null);
				String latitud = parametro.get("latitud");
				String longitud = parametro.get("longitud");
				
				
				Gson gson = new Gson();
			
				Type type = new TypeToken<List<stdatadinamico>>() {
				}.getType();

				List<stdatadinamico> datos = gson.fromJson(parametro.get("datadinamica"), type);
				Componente componente = new Componente();
				componente.setId(componenteId);
				Producto productoPadre = new Producto();
				productoPadre.setId(productoPadreId);
				ProductoTipo productoTipo = new ProductoTipo();
				productoTipo.setId(tipoproductoId);
				UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
				unidadEjecutora.setUnidadEjecutora(unidadEjecutoraId);
				
				if (esnuevo){
					
					producto = new Producto(componente, productoTipo, unidadEjecutora, nombre, descripcion, 
							 usuario, null, new DateTime().toDate(), null, 1
							, snip, programa, subprograma, proyecto_, actividad, obra, fuente, latitud, longitud,null,null,null);
					
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
					producto.setFuente(fuente);
					producto.setUsuarioActualizo(usuario);
					producto.setFechaActualizacion(new DateTime().toDate());
					producto.setLatitud(latitud);
					producto.setLongitud(longitud);
				}
				ret = ProductoDAO.guardarProducto(producto);
				
				if (ret){
					ProductoUsuarioId productoUsuarioId = new ProductoUsuarioId(producto.getId(), usuario);
					ProductoUsuario productoUsuario =  new ProductoUsuario(productoUsuarioId, producto, usuario, null, new DateTime().toDate(),null);
					ProductoUsuarioDAO.guardarProductoUsuario(productoUsuario);
					
					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
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
				
				response_text = String.join("","{ \"success\": ",(ret ? "true" : "false"),", "
						, "\"id\": " , producto.getId().toString() , ","
						, "\"usuarioCreo\": \"" , producto.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(producto.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , producto.getUsuarioActualizo() != null ? producto.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(producto.getFechaActualizacion()),"\""
						," }");
				}
				catch (Throwable e){
					response_text = "{ \"success\": false }";
				}
				
			}else {
				response_text = "{ \"success\": false }";
			}
		} else if (accion.equals("borrar")) {
			int codigo = Utils.String2Int(parametro.get("codigo"), -1);
			boolean eliminado = ProductoDAO.eliminar(codigo, usuario);
			if (eliminado) {
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
					temp.fuente = producto.getFuente();
					temp.snip = producto.getSnip();
					temp.estado = producto.getEstado();
					temp.usuarioCreo = producto.getUsuarioCreo();
					temp.usuarioactualizo = producto.getUsuarioActualizo();
					temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
					temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
					temp.latitud = producto.getLatitud();
					temp.longitud = producto.getLongitud();
					

					if (producto.getComponente() != null) {
						temp.idComponente = producto.getComponente().getId();
						temp.componente = producto.getComponente().getNombre();
					}

					if (producto.getProductoTipo() != null) {
						temp.idProductoTipo = producto.getProductoTipo().getId();
						temp.productoTipo = producto.getProductoTipo().getNombre();
					}
					
					if (producto.getUnidadEjecutora() != null){
						temp.unidadEjectuora = producto.getUnidadEjecutora().getUnidadEjecutora();
						temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
					}

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
				temp.fuente = producto.getFuente();
				temp.snip = producto.getSnip();
				temp.estado = producto.getEstado();
				temp.usuarioCreo = producto.getUsuarioCreo();
				temp.usuarioactualizo = producto.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(producto.getFechaCreacion());
				temp.fechaactualizacion = Utils.formatDateHour(producto.getFechaActualizacion());
				temp.latitud = producto.getLatitud();
				temp.longitud = producto.getLongitud();
				

				if (producto.getComponente() != null) {
					temp.idComponente = producto.getComponente().getId();
					temp.componente = producto.getComponente().getNombre();
				}

				if (producto.getProductoTipo() != null) {
					temp.idProductoTipo = producto.getProductoTipo().getId();
					temp.productoTipo = producto.getProductoTipo().getNombre();
				}
				
				if (producto.getUnidadEjecutora() != null){
					temp.unidadEjectuora = producto.getUnidadEjecutora().getUnidadEjecutora();
					temp.nombreUnidadEjecutora = producto.getUnidadEjecutora().getNombre();
				}

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

			response_text = Utils.getJSonString("productos", ProductoDAO.getProductosPagina(pagina, registros,componenteid,usuario,
					filtro_nombre,filtro_usuario_creo
					,filtro_fecha_creacion,columna_ordenada,orden_direccion));

			if (Utils.isNullOrEmpty(response_text)) {
				response_text = "{\"success\":false}";
			} else {
				response_text = "{\"success\":true," + response_text + "}";
			}
		} else if (accion.equals("listarComponentes")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);

			response_text = Utils.getJSonString("productos", ComponenteDAO.getComponentesPagina(pagina, registros,usuario));

			if (Utils.isNullOrEmpty(response_text)) {
				response_text = "{\"success\":false}";
			} else {
				response_text = "{\"success\":true," + response_text + "}";
			}

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
