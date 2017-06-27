package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.DatoTipoDAO;
import dao.MetaDAO;
import dao.MetaTipoDAO;
import dao.MetaUnidadMedidaDAO;
import dao.MetaValorDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.DatoTipo;
import pojo.Meta;
import pojo.MetaTipo;
import pojo.MetaUnidadMedida;
import pojo.MetaValor;
import pojo.MetaValorId;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.Utils;

@WebServlet("/SPrestamoMetas")
public class SPrestamoMetas extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int OBJETO_ID_PROYECTO = 1;
	private static final int OBJETO_ID_COMPONENTE = 2;
	private static final int OBJETO_ID_PRODUCTO = 3;
	//private static final int OBJETO_ID_SUBPRODUCTO = 4;
	//private static final int OBJETO_ID_ACTIVIDAD= 5;
	
	private static final int DATOTIPO_DECIMAL = 3;
	
	private static final int META_ID_REAL = 1;
	private static final int META_ID_PLANIFICADA = 2;
	private static final int META_ID_LINEABASE= 3;
	private static final int META_ID_FINAL= 4;
		
	class stproductometa {
		Integer id;
		Integer objetoTipo;
		String objetoTipoNombre;
		String nombreMeta;
		Integer unidadDeMedidaId;
		Integer datoTipoId;
		String fechaInicio;
		String fechaFin;
		List <stmeta> metasReales = new ArrayList<>();
		List <stmeta> metasPlanificadas = new ArrayList<>();
		Integer lineaBaseId;
		String lineaBase;
		Integer metaFinalId;
		String metaFinal;
	}
	
	class stmeta {
		Integer id;
		String fecha;
		String valor;
		Integer tipoMetaId;
		Integer unidadMedidaId;
		Integer objetoId;
		Integer objetoTipoId;
	}

    public SPrestamoMetas() {
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

		if(accion.equals("getMetasProducto")){
			Integer proyectoId = map.get("proyectoid")!=null ? Integer.parseInt(map.get("proyectoid")) : 0;
			
			List<stproductometa> lstproductometas = obtenerMetasPorProducto(proyectoId, usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstproductometas);
	        response_text = String.join("", "\"proyectometas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		
		}else if(accion.equals("guardarProyectoMeta")){
			stmeta metavalor = new stmeta();
			metavalor.id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0; 
			metavalor.fecha = map.get("fecha")!=null ? map.get("fecha") : "";
			metavalor.valor = map.get("valor")!=null ? map.get("valor") : "";
			metavalor.tipoMetaId = map.get("tipoMetaId")!=null ? Integer.parseInt(map.get("tipoMetaId")) : 0; 
			metavalor.unidadMedidaId = map.get("unidadMedidaId")!=null ? Integer.parseInt(map.get("unidadMedidaId")) : 0; 
			metavalor.objetoId = map.get("objetoId")!=null ? Integer.parseInt(map.get("objetoId")) : 0;
			metavalor.objetoTipoId = map.get("objetoTipoId")!=null ? Integer.parseInt(map.get("objetoTipoId")) : 0;
			
			metavalor = guardarProductoMetaValor(metavalor, usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(metavalor);
	        response_text = String.join("", "\"metavalor\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}else if (accion.equals("exportarExcel")){
			CExcel excel = new CExcel("PrestamoMetas",false);
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			
			List<stproductometa> lstproductometa = obtenerMetasPorProducto(proyectoId, usuario);
			
			Map<String,Object[]> datos = new HashMap<>();
			datos.put("0",   new Object[] {"Producto"
					, "Unidad de Medida"
					, "Meta Real"
					, "Meta Anual Planificada"
					, "Linea Base"
					, "Meta Final"});
			int fila = 1;
			
			for (stproductometa productometa : lstproductometa){
				String sangria;
				switch (productometa.objetoTipo){
					case 1: sangria = ""; break;
					case 2: sangria = "\t"; break;
					case 3: sangria = "\t\t"; break;
					case 4: sangria = "\t\t\t"; break;
					case 5: sangria = "\t\t\t\t"; break;
					default: sangria = "";
		}
				datos.put(fila+"", new Object [] { sangria + productometa.nombreMeta 
						,productometa.unidadDeMedidaId
						,productometa.fechaInicio
						,productometa.fechaFin
						,productometa.lineaBase
						,productometa.metaFinal
						});
				fila++;
			}
			
			String path = excel.ExportarExcel(datos, "Metas de Préstamo", usuario);

			
			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
		        	
		        }
		        //
		        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		        
		        int readByte = 0;
		        byte[] buffer = new byte[2024];

                while(true)
                {
                    readByte = is.read(buffer);
                    if(readByte == -1)
                    {
                        break;
                    }
                    outByteStream.write(buffer);
                }
                
                file.delete();
                
                is.close();
                outByteStream.flush();
                outByteStream.close();
                
		        byte [] outArray = Base64.encode(outByteStream.toByteArray());
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; MetasPrestamo_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}else{
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

	
	private List<stproductometa> obtenerMetasPorProducto(int proyectoId, String usuario){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		List<stproductometa> lstproductometas = new ArrayList<>();
		if (proyecto!=null){
			stproductometa proyectometa = new stproductometa();
			proyectometa.id = proyecto.getId();
			proyectometa.objetoTipo = OBJETO_ID_PROYECTO;
			proyectometa.objetoTipoNombre = "Prestamo";
			proyectometa.nombreMeta = proyecto.getNombre();
			proyectometa.unidadDeMedidaId = null;
			proyectometa.datoTipoId = null;
			proyectometa.fechaInicio = "";
			proyectometa.fechaFin = "";
			proyectometa.lineaBase = "";
			proyectometa.metaFinal = "";
			lstproductometas.add(proyectometa);
		}
		List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
				null, null, null, null, null, usuario);
		for (Componente componente : componentes){
			stproductometa proyectometa = new stproductometa();
			proyectometa.id = componente.getId();
			proyectometa.objetoTipo = OBJETO_ID_COMPONENTE;
			proyectometa.objetoTipoNombre = "Componente";
			proyectometa.nombreMeta = componente.getNombre();
			proyectometa.unidadDeMedidaId = null;
			proyectometa.datoTipoId = null;
			proyectometa.fechaInicio = "";
			proyectometa.fechaFin = "";
			proyectometa.lineaBase = "";
			proyectometa.metaFinal = "";
			lstproductometas.add(proyectometa);
			
			List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
					null, null, null, null, null, usuario);
			for (Producto producto : productos){
				proyectometa = new stproductometa();
				proyectometa.id = producto.getId();
				proyectometa.objetoTipo = OBJETO_ID_PRODUCTO;
				proyectometa.objetoTipoNombre = "Producto";
				proyectometa.nombreMeta = producto.getNombre();
				proyectometa.unidadDeMedidaId = null;
				proyectometa.datoTipoId = null;
				proyectometa.fechaInicio = "";
				proyectometa.fechaFin = "";
				proyectometa.lineaBase = "";
				proyectometa.metaFinal = "";
				
				proyectometa = getFechaInicioFinProducto(proyectometa);
				
				
				//Obteniendo meta real
				List<Meta> metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_REAL, null, null, null, null);
				if (metas!= null && !metas.isEmpty()){
					Meta Meta = metas.get(0);
					proyectometa.unidadDeMedidaId = Meta.getMetaUnidadMedida().getId();
					proyectometa.datoTipoId = Meta.getDatoTipo().getId();
					List <MetaValor> metavalores = MetaValorDAO.getValoresMeta(Meta.getId());
					for (MetaValor metavalor : metavalores){	
						stmeta meta = new stmeta();
						meta.id = metavalor.getId().getMetaid();
						meta.fecha = Utils.formatDate(metavalor.getId().getFecha());
						meta.valor = "";
						if (null != metavalor.getValorDecimal()){
							meta.valor = metavalor.getValorDecimal().toString();
						}
						proyectometa.metasReales.add(meta);
					}
				}
				
				//Obteniendo meta planificada
				metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_PLANIFICADA, null, null, null, null);
				if (metas!= null && !metas.isEmpty()){
					Meta Meta = metas.get(0);
					proyectometa.unidadDeMedidaId = Meta.getMetaUnidadMedida().getId();
					proyectometa.datoTipoId = Meta.getDatoTipo().getId();
					List <MetaValor> metavalores = MetaValorDAO.getValoresMeta(Meta.getId());
					for (MetaValor metavalor : metavalores){	
						stmeta meta = new stmeta();
						meta.id = metavalor.getId().getMetaid();
						meta.fecha = Utils.formatDate(metavalor.getId().getFecha());
						meta.valor = "";
						if (null != metavalor.getValorDecimal()){
							meta.valor = metavalor.getValorDecimal().toString();
						}
						proyectometa.metasPlanificadas.add(meta);
					}
				}

				//Obteniendo linea base
				metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_LINEABASE, null, null, null, null);
				if (metas!= null && !metas.isEmpty()){
					Meta Meta = metas.get(0);
					proyectometa.unidadDeMedidaId = Meta.getMetaUnidadMedida().getId();
					proyectometa.datoTipoId = Meta.getDatoTipo().getId();
					proyectometa.lineaBaseId = Meta.getId();
					MetaValor metavalor = MetaValorDAO.getMetaValorPorMetaid(Meta.getId());
					proyectometa.lineaBase = "";
					if (null != metavalor.getValorDecimal()){
						proyectometa.lineaBase = metavalor.getValorDecimal().toString();
					}				
				}
				
				//Obteniendo meta final
				metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_FINAL, null, null, null, null);
				if (metas!= null && !metas.isEmpty()){
					Meta Meta = metas.get(0);
					proyectometa.unidadDeMedidaId = Meta.getMetaUnidadMedida().getId();
					proyectometa.datoTipoId = Meta.getDatoTipo().getId();
					proyectometa.metaFinalId = Meta.getId();
					MetaValor metavalor = MetaValorDAO.getMetaValorPorMetaid(Meta.getId());
					proyectometa.metaFinal = "";
					if (null != metavalor.getValorDecimal()){
						proyectometa.metaFinal = metavalor.getValorDecimal().toString();
					}
				}
				
				lstproductometas.add(proyectometa);				
			}
		}
		return lstproductometas;
	}
		
	private stmeta guardarProductoMetaValor(stmeta metavalor, String usuario){
		Meta meta;
		if(metavalor.id>0){
			meta = MetaDAO.getMetaPorId(metavalor.id);
		}else{
			MetaTipo metatipo = MetaTipoDAO.getMetaTipoPorId(metavalor.tipoMetaId);
			MetaUnidadMedida metaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(metavalor.unidadMedidaId);
			DatoTipo datoTipo = DatoTipoDAO.getDatoTipo(DATOTIPO_DECIMAL);
			meta = new Meta(datoTipo, metatipo, metaUnidadMedida, metatipo.getDescripcion(), usuario, new Date());
			meta.setObjetoId(metavalor.objetoId);
			meta.setObjetoTipo(metavalor.objetoTipoId);
			meta.setEstado(1);
			MetaDAO.guardarMeta(meta);
		}
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy"); 
		Date fecha = new Date();
		try {
			fecha = dateformat.parse(metavalor.fecha);
		}catch (ParseException e) {
		    e.printStackTrace();
		}
		MetaValorId MetaValorId = new MetaValorId(meta.getId(), fecha);
		MetaValor MetaValor = MetaValorDAO.getMetaValorPorId(MetaValorId);
		if (MetaValor != null){
			MetaValor.setValorDecimal(Utils.String2BigDecimal(metavalor.valor, new BigDecimal(0)));
		}else{
			MetaValor = new MetaValor(MetaValorId, meta, usuario, null, null, Utils.String2BigDecimal(metavalor.valor, new BigDecimal(0)), null);
		}
		MetaValorDAO.guardarMetaValor(MetaValor);
		metavalor.id = meta.getId();
		
		return metavalor;		
	}
	
	private stproductometa getFechaInicioFinProducto(stproductometa productometa){
		Date fechaInicio = null;
		Date fechaFin = null;
		List <Actividad> actividades = getActividadesProducto(productometa.id);
		for(Actividad actividad : actividades){
			actividad = ActividadDAO.getFechasActividad(actividad);
			if (fechaInicio == null || fechaInicio.after(actividad.getFechaInicio())){
				fechaInicio = actividad.getFechaInicio();
			}
			if (fechaFin == null || fechaFin.before(actividad.getFechaFin())){
				fechaFin = actividad.getFechaFin();
			}
		}
		productometa.fechaInicio = Utils.formatDate(fechaInicio);
		productometa.fechaFin = Utils.formatDate(fechaFin);
		return productometa;
	}
	
	private List <Actividad> getActividadesProducto(int productoId){
		List <Actividad> actividades = new ArrayList<Actividad>();
		Producto producto = ProductoDAO.getProductoPorId(productoId);
		Set <Subproducto> subproductos = producto.getSubproductos();
		actividades = ActividadDAO.getActividadsSubactividadsPorObjeto(productoId, 3);
		for (Subproducto subproducto : subproductos){
			actividades.addAll(ActividadDAO.getActividadsSubactividadsPorObjeto(subproducto.getId(), 4));
		}
		return actividades;
	}
	
}
