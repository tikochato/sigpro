package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import org.joda.time.DateTime;

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
import pojo.Meta;
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
	
	private static final int META_ID_REAL = 1;
	private static final int META_ID_ANUALPLANIFICADA = 2;
	private static final int META_ID_LINEABASE= 3;
	private static final int META_ID_FINAL= 4;
	
	private static final int DATOTIPO_TEXTO = 1;
	private static final int DATOTIPO_ENTERO = 2;
	private static final int DATOTIPO_DECIMAL = 3;
	private static final int DATOTIPO_BOOLEANO = 4;
	private static final int DATOTIPO_FECHA = 5;
	
	class stprestamometa {
		Integer id;
		Integer objetoTipo;
		String objetoTipoNombre;
		String nombre;
		Integer unidadDeMedidaId;
		Integer datoTipoId;
		String metaFechaInicio;
		String metaFechaFin;
		Integer metaRealId;
		String metaReal;
		Integer metaAnualPlanificadaId;
		String metaAnualPlanificada;
		Integer lineaBaseId;
		String lineaBase;
		Integer metaFinalId;
		String metaFinal;
	}
	
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

		if(accion.equals("getProyectoMetas")){
			Integer proyectoId = map.get("proyectoid")!=null ? Integer.parseInt(map.get("proyectoid")) : 0;
			
			List<stprestamometa> lstproyectometas = obtenerListadoProyectoMetas(proyectoId, usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstproyectometas);
	        response_text = String.join("", "\"proyectometas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		
		}else if(accion.equals("getMetasProducto")){
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
			stprestamometa proyectometa = new stprestamometa();
			proyectometa.id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0; //Producto 
			proyectometa.objetoTipo = map.get("objetoTipo")!=null ? Integer.parseInt(map.get("objetoTipo")) : 0; //ObjetoTipo
			proyectometa.unidadDeMedidaId = map.get("unidadDeMedidaId")!=null ? Integer.parseInt(map.get("unidadDeMedidaId")) : 0;
			proyectometa.datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
			proyectometa.metaFechaInicio = map.get("metaFechaInicio")!=null ? map.get("metaFechaInicio") : "";
			proyectometa.metaFechaFin = map.get("metaFechaFin")!=null ? map.get("metaFechaFin") : "";
			proyectometa.metaRealId = map.get("metaRealId")!=null ? Integer.parseInt(map.get("metaRealId")) : 0;
			proyectometa.metaReal = map.get("metaReal")!=null ? map.get("metaReal") : "";
			proyectometa.metaAnualPlanificadaId = map.get("metaAnualPlanificadaId")!=null ? Integer.parseInt(map.get("metaAnualPlanificadaId")) : 0;
			proyectometa.metaAnualPlanificada = map.get("metaAnualPlanificada")!=null ? map.get("metaAnualPlanificada") : "";
			proyectometa.lineaBaseId = map.get("lineaBaseId")!=null ? Integer.parseInt(map.get("lineaBaseId")) : 0;
			proyectometa.lineaBase = map.get("lineaBase")!=null ? map.get("lineaBase") : "";
			proyectometa.metaFinalId = map.get("metaFinalId")!=null ? Integer.parseInt(map.get("metaFinalId")) : 0;
			proyectometa.metaFinal = map.get("metaFinal")!=null ? map.get("metaFinal") : "";
			
			proyectometa = guardarProyectoMeta(proyectometa, usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(proyectometa);
	        response_text = String.join("", "\"proyectometa\":",response_text);
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
			List<stprestamometa> lstproyectometas = obtenerListadoProyectoMetas(proyectoId, usuario);
			
			Map<String,Object[]> datos = new HashMap<>();
			datos.put("0",   new Object[] {"Producto"
					, "Unidad de Medida"
					, "Meta Real"
					, "Meta Anual Planificada"
					, "Linea Base"
					, "Meta Final"});
			int fila = 1;
			
			for (stprestamometa proyectometa : lstproyectometas){
				String sangria;
				switch (proyectometa.objetoTipo){
					case 1: sangria = ""; break;
					case 2: sangria = "\t"; break;
					case 3: sangria = "\t\t"; break;
					case 4: sangria = "\t\t\t"; break;
					case 5: sangria = "\t\t\t\t"; break;
					default: sangria = "";
		}
				datos.put(fila+"", new Object [] { sangria + proyectometa.nombre 
						,proyectometa.unidadDeMedidaId
						,proyectometa.metaReal
						,proyectometa.metaAnualPlanificada
						,proyectometa.lineaBase
						,proyectometa.metaFinal
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
				
				stprestamometa prestamometa = new stprestamometa();
				prestamometa.id = proyectometa.id;
				prestamometa = getFechaInicioFinProducto(prestamometa);
				proyectometa.fechaInicio = prestamometa.metaFechaInicio;
				proyectometa.fechaFin = prestamometa.metaFechaFin;
				
				
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
						switch(metas.get(0).getDatoTipo().getId()){
							case DATOTIPO_TEXTO: 
								if (null != metavalor.getValorString()){
									meta.valor = metavalor.getValorString();
								}
								break;
							case DATOTIPO_ENTERO: 
								if (null != metavalor.getValorEntero()){
									meta.valor = metavalor.getValorEntero().toString();
								}
								break;
							case DATOTIPO_DECIMAL: 
								if (null != metavalor.getValorDecimal()){
									meta.valor = metavalor.getValorDecimal().toString();
								}
								break;
							case DATOTIPO_BOOLEANO: 
	
								break;
							case DATOTIPO_FECHA: 
								if (null != metavalor.getValorTiempo()){
									meta.valor = Utils.formatDateHour(metavalor.getValorTiempo());
								}
								break;
						}
						proyectometa.metasReales.add(meta);
					}
				}
				
				//Obteniendo meta anual planificada
				metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_ANUALPLANIFICADA, null, null, null, null);
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
						switch(metas.get(0).getDatoTipo().getId()){
							case DATOTIPO_TEXTO: 
								if (null != metavalor.getValorString()){
									meta.valor = metavalor.getValorString();
								}
								break;
							case DATOTIPO_ENTERO: 
								if (null != metavalor.getValorEntero()){
									meta.valor = metavalor.getValorEntero().toString();
								}
								break;
							case DATOTIPO_DECIMAL: 
								if (null != metavalor.getValorDecimal()){
									meta.valor = metavalor.getValorDecimal().toString();
								}
								break;
							case DATOTIPO_BOOLEANO: 
	
								break;
							case DATOTIPO_FECHA: 
								if (null != metavalor.getValorTiempo()){
									meta.valor = Utils.formatDateHour(metavalor.getValorTiempo());
								}
								break;
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
					switch(metas.get(0).getDatoTipo().getId()){
						case DATOTIPO_TEXTO: 
							if (null != metavalor.getValorString()){
							proyectometa.lineaBase = metavalor.getValorString();
							}else{
								proyectometa.lineaBase = "";
							}
							break;
						case DATOTIPO_ENTERO: 
							if (null != metavalor.getValorEntero()){
							proyectometa.lineaBase = metavalor.getValorEntero().toString();
							}else{
								proyectometa.lineaBase = "";
							}
							break;
						case DATOTIPO_DECIMAL: 
							if (null != metavalor.getValorDecimal()){
							proyectometa.lineaBase = metavalor.getValorDecimal().toString();
							}else{
								proyectometa.lineaBase = "";
							}
							break;
						case DATOTIPO_BOOLEANO: 

							break;
						case DATOTIPO_FECHA: 
							if (null != metavalor.getValorTiempo()){
							proyectometa.lineaBase = Utils.formatDateHour(metavalor.getValorTiempo());
							}else{
								proyectometa.lineaBase = "";
							}
							break;
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
					switch(metas.get(0).getDatoTipo().getId()){
						case DATOTIPO_TEXTO: 
							if (null != metavalor.getValorString()){
							proyectometa.metaFinal = metavalor.getValorString();
							}else{
								proyectometa.metaFinal = "";
							}
							break;
						case DATOTIPO_ENTERO: 
							if (null != metavalor.getValorEntero()){
							proyectometa.metaFinal = metavalor.getValorEntero().toString();
							}else{
								proyectometa.metaFinal = "";
							}
							break;
						case DATOTIPO_DECIMAL: 
							if (null != metavalor.getValorDecimal()){
							proyectometa.metaFinal = metavalor.getValorDecimal().toString();
							}else{
								proyectometa.metaFinal = "";
							}
							break;
						case DATOTIPO_BOOLEANO: 

							break;
						case DATOTIPO_FECHA: 
							if (null != metavalor.getValorTiempo()){
							proyectometa.metaFinal = Utils.formatDateHour(metavalor.getValorTiempo());
							}else{
								proyectometa.metaFinal = "";
							}
							break;
					}
				}
				
				lstproductometas.add(proyectometa);				
			}
		}
		return lstproductometas;
	}
	
	private List<stprestamometa> obtenerListadoProyectoMetas(int proyectoId, String usuario){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		List<stprestamometa> lstproyectometas = new ArrayList<>();
		if (proyecto!=null){
			stprestamometa proyectometa = new stprestamometa();
			proyectometa.id = proyecto.getId();
			proyectometa.objetoTipo = OBJETO_ID_PROYECTO;
			proyectometa.objetoTipoNombre = "Prestamo";
			proyectometa.nombre = proyecto.getNombre();
			proyectometa.unidadDeMedidaId = null;
			proyectometa.datoTipoId = null;
			proyectometa.metaFechaInicio = "";
			proyectometa.metaFechaFin = "";
			proyectometa.metaRealId = 0;
			proyectometa.metaReal = "";
			proyectometa.metaAnualPlanificadaId = 0;
			proyectometa.metaAnualPlanificada = "";
			proyectometa.lineaBaseId = 0;
			proyectometa.lineaBase = "";
			proyectometa.metaFinalId = 0;
			proyectometa.metaFinal = "";
			lstproyectometas.add(proyectometa);
		}
		List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
				null, null, null, null, null, usuario);
		for (Componente componente : componentes){
			stprestamometa proyectometa = new stprestamometa();
			proyectometa.id = componente.getId();
			proyectometa.objetoTipo = OBJETO_ID_COMPONENTE;
			proyectometa.objetoTipoNombre = "Componente";
			proyectometa.nombre = componente.getNombre();
			proyectometa.unidadDeMedidaId = null;
			proyectometa.datoTipoId = null;
			proyectometa.metaFechaInicio = "";
			proyectometa.metaFechaFin = "";
			proyectometa.metaRealId = 0;
			proyectometa.metaReal = "";
			proyectometa.metaAnualPlanificadaId = 0;
			proyectometa.metaAnualPlanificada = "";
			proyectometa.lineaBaseId = 0;
			proyectometa.lineaBase = "";
			proyectometa.metaFinalId = 0;
			proyectometa.metaFinal = "";
			lstproyectometas.add(proyectometa);
			
			List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
					null, null, null, null, null, usuario);
			for (Producto producto : productos){
				proyectometa = new stprestamometa();
				proyectometa.id = producto.getId();
				proyectometa.objetoTipo = OBJETO_ID_PRODUCTO;
				proyectometa.objetoTipoNombre = "Producto";
				proyectometa.nombre = producto.getNombre();
				proyectometa.unidadDeMedidaId = null;
				proyectometa.datoTipoId = null;
				proyectometa.metaFechaInicio = "";
				proyectometa.metaFechaFin = "";
				proyectometa.metaRealId = 0;
				proyectometa.metaReal = "";
				proyectometa.metaAnualPlanificadaId = 0;
				proyectometa.metaAnualPlanificada = "";
				proyectometa.lineaBaseId = 0;
				proyectometa.lineaBase = "";
				proyectometa.metaFinalId = 0;
				proyectometa.metaFinal = "";
				
				proyectometa = getFechaInicioFinProducto(proyectometa);
				
				//Obteniendo meta real
				List<Meta> metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_REAL, null, null, null, null);
				if (metas!= null && !metas.isEmpty()){
					Meta Meta = metas.get(0);
					proyectometa.unidadDeMedidaId = Meta.getMetaUnidadMedida().getId();
					proyectometa.datoTipoId = Meta.getDatoTipo().getId();
					MetaValor metavalor = MetaValorDAO.getMetaValorPorMetaid(Meta.getId());
					proyectometa.metaRealId = Meta.getId();
					switch(metas.get(0).getDatoTipo().getId()){
						case DATOTIPO_TEXTO: 
							if (null != metavalor.getValorString()){
							proyectometa.metaReal = metavalor.getValorString();
							}else{
								proyectometa.metaReal = "";
							}
							break;
						case DATOTIPO_ENTERO: 
							if (null != metavalor.getValorEntero()){
							proyectometa.metaReal = metavalor.getValorEntero().toString();
							}else{
								proyectometa.metaReal = "";
							}
							break;
						case DATOTIPO_DECIMAL: 
							if (null != metavalor.getValorDecimal()){
							proyectometa.metaReal = metavalor.getValorDecimal().toString();
							}else{
								proyectometa.metaReal = "";
							}
							break;
						case DATOTIPO_BOOLEANO: 

							break;
						case DATOTIPO_FECHA: 
							if (null != metavalor.getValorTiempo()){
							proyectometa.metaReal = Utils.formatDateHour(metavalor.getValorTiempo());
							}else{
								proyectometa.metaReal = "";
					}
							break;
					}
				}
				
				//Obteniendo meta anual planificada
				metas = MetaDAO.getMetasPagina(-1, -1, producto.getId(), OBJETO_ID_PRODUCTO, null, META_ID_ANUALPLANIFICADA, null, null, null, null);
				if (metas!= null && !metas.isEmpty()){
					Meta Meta = metas.get(0);
					proyectometa.unidadDeMedidaId = Meta.getMetaUnidadMedida().getId();
					proyectometa.datoTipoId = Meta.getDatoTipo().getId();
					proyectometa.metaAnualPlanificadaId = Meta.getId();
					MetaValor metavalor = MetaValorDAO.getMetaValorPorMetaid(Meta.getId());
					switch(metas.get(0).getDatoTipo().getId()){
						case DATOTIPO_TEXTO: 
							if (null != metavalor.getValorString()){
							proyectometa.metaAnualPlanificada = metavalor.getValorString();
							}else{
								proyectometa.metaAnualPlanificada = "";
							}
							break;
						case DATOTIPO_ENTERO: 
							if (null != metavalor.getValorEntero()){
							proyectometa.metaAnualPlanificada = metavalor.getValorEntero().toString();
							}else{
								proyectometa.metaAnualPlanificada = "";
							}
							break;
						case DATOTIPO_DECIMAL: 
							if (null != metavalor.getValorDecimal()){
							proyectometa.metaAnualPlanificada = metavalor.getValorDecimal().toString();
							}else{
								proyectometa.metaAnualPlanificada = "";
							}
							break;
						case DATOTIPO_BOOLEANO: 

							break;
						case DATOTIPO_FECHA: 
							if (null != metavalor.getValorTiempo()){
							proyectometa.metaAnualPlanificada = Utils.formatDateHour(metavalor.getValorTiempo());
							}else{
								proyectometa.metaAnualPlanificada = "";
							}
							break;
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
					switch(metas.get(0).getDatoTipo().getId()){
						case DATOTIPO_TEXTO: 
							if (null != metavalor.getValorString()){
							proyectometa.lineaBase = metavalor.getValorString();
							}else{
								proyectometa.lineaBase = "";
							}
							break;
						case DATOTIPO_ENTERO: 
							if (null != metavalor.getValorEntero()){
							proyectometa.lineaBase = metavalor.getValorEntero().toString();
							}else{
								proyectometa.lineaBase = "";
							}
							break;
						case DATOTIPO_DECIMAL: 
							if (null != metavalor.getValorDecimal()){
							proyectometa.lineaBase = metavalor.getValorDecimal().toString();
							}else{
								proyectometa.lineaBase = "";
							}
							break;
						case DATOTIPO_BOOLEANO: 

							break;
						case DATOTIPO_FECHA: 
							if (null != metavalor.getValorTiempo()){
							proyectometa.lineaBase = Utils.formatDateHour(metavalor.getValorTiempo());
							}else{
								proyectometa.lineaBase = "";
							}
							break;
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
					switch(metas.get(0).getDatoTipo().getId()){
						case DATOTIPO_TEXTO: 
							if (null != metavalor.getValorString()){
							proyectometa.metaFinal = metavalor.getValorString();
							}else{
								proyectometa.metaFinal = "";
							}
							break;
						case DATOTIPO_ENTERO: 
							if (null != metavalor.getValorEntero()){
							proyectometa.metaFinal = metavalor.getValorEntero().toString();
							}else{
								proyectometa.metaFinal = "";
							}
							break;
						case DATOTIPO_DECIMAL: 
							if (null != metavalor.getValorDecimal()){
							proyectometa.metaFinal = metavalor.getValorDecimal().toString();
							}else{
								proyectometa.metaFinal = "";
							}
							break;
						case DATOTIPO_BOOLEANO: 

							break;
						case DATOTIPO_FECHA: 
							if (null != metavalor.getValorTiempo()){
							proyectometa.metaFinal = Utils.formatDateHour(metavalor.getValorTiempo());
							}else{
								proyectometa.metaFinal = "";
							}
							break;
					}
				}
				
				lstproyectometas.add(proyectometa);				
			}
		}
		return lstproyectometas;
	}
	
	private stprestamometa guardarProyectoMeta(stprestamometa proyectometa, String usuario){
		Meta meta;
		
		if (proyectometa.metaReal != null && proyectometa.metaReal != ""){
			if (proyectometa.metaRealId != null && proyectometa.metaRealId > 0){
				meta = MetaDAO.getMetaPorId(proyectometa.metaRealId);
				meta.setUsuarioActualizo(usuario);
				meta.setFechaActualizacion(new DateTime().toDate());
			}else{
				meta = new Meta();
				meta.setNombre("Meta Real");
				meta.setUsuarioCreo(usuario);
				meta.setFechaCreacion(new DateTime().toDate());
			}
			meta.setDatoTipo(DatoTipoDAO.getDatoTipo(proyectometa.datoTipoId));
			meta.setMetaUnidadMedida(MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(proyectometa.unidadDeMedidaId));
			meta.setMetaTipo(MetaTipoDAO.getMetaTipoPorId(META_ID_REAL));
			meta.setEstado(1);
			meta.setObjetoId(proyectometa.id);
			meta.setObjetoTipo(proyectometa.objetoTipo);
			MetaDAO.guardarMeta(meta);
			proyectometa.metaRealId = meta.getId();
			MetaValor metavalor = new MetaValor();
			MetaValorId metavalorid = new MetaValorId(meta.getId(), new DateTime().toDate());
			metavalor.setId(metavalorid);
			metavalor.setMeta(meta);
			metavalor.setUsuario(usuario);
			metavalor = setMetaValorTipo(proyectometa.datoTipoId, metavalor, proyectometa.metaReal);
			MetaValorDAO.guardarMetaValor(metavalor);
		}
		
		if (proyectometa.metaAnualPlanificada != null && proyectometa.metaAnualPlanificada != ""){
			if (proyectometa.metaAnualPlanificadaId != null && proyectometa.metaAnualPlanificadaId > 0){
				meta = MetaDAO.getMetaPorId(proyectometa.metaAnualPlanificadaId);
				meta.setUsuarioActualizo(usuario);
				meta.setFechaActualizacion(new DateTime().toDate());
			}else{
				meta = new Meta();
				meta.setNombre("Meta Anual Planificada");
				meta.setUsuarioCreo(usuario);
				meta.setFechaCreacion(new DateTime().toDate());
			}
			meta.setDatoTipo(DatoTipoDAO.getDatoTipo(proyectometa.datoTipoId));
			meta.setMetaUnidadMedida(MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(proyectometa.unidadDeMedidaId));
			meta.setMetaTipo(MetaTipoDAO.getMetaTipoPorId(META_ID_ANUALPLANIFICADA));
			meta.setEstado(1);
			meta.setObjetoId(proyectometa.id);
			meta.setObjetoTipo(proyectometa.objetoTipo);
			MetaDAO.guardarMeta(meta);
			proyectometa.metaAnualPlanificadaId = meta.getId();
			MetaValor metavalor = new MetaValor();
			MetaValorId metavalorid = new MetaValorId(meta.getId(), new DateTime().toDate());
			metavalor.setId(metavalorid);
			metavalor.setMeta(meta);
			metavalor.setUsuario(usuario);
			metavalor = setMetaValorTipo(proyectometa.datoTipoId, metavalor, proyectometa.metaAnualPlanificada);
			MetaValorDAO.guardarMetaValor(metavalor);
		}
		
		if (proyectometa.lineaBase != null && proyectometa.lineaBase != ""){
			if (proyectometa.lineaBaseId != null && proyectometa.lineaBaseId > 0){
				meta = MetaDAO.getMetaPorId(proyectometa.lineaBaseId);
				meta.setUsuarioActualizo(usuario);
				meta.setFechaActualizacion(new DateTime().toDate());
			}else{
				meta = new Meta();
				meta.setNombre("Línea Base");
				meta.setUsuarioCreo(usuario);
				meta.setFechaCreacion(new DateTime().toDate());
			}
			meta.setDatoTipo(DatoTipoDAO.getDatoTipo(proyectometa.datoTipoId));
			meta.setMetaUnidadMedida(MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(proyectometa.unidadDeMedidaId));
			meta.setMetaTipo(MetaTipoDAO.getMetaTipoPorId(META_ID_LINEABASE));
			meta.setEstado(1);
			meta.setObjetoId(proyectometa.id);
			meta.setObjetoTipo(proyectometa.objetoTipo);
			MetaDAO.guardarMeta(meta);
			proyectometa.lineaBaseId = meta.getId();
			MetaValor metavalor = new MetaValor();
			MetaValorId metavalorid = new MetaValorId(meta.getId(), new DateTime().toDate());
			metavalor.setId(metavalorid);
			metavalor.setMeta(meta);
			metavalor.setUsuario(usuario);
			metavalor = setMetaValorTipo(proyectometa.datoTipoId, metavalor, proyectometa.lineaBase);
			MetaValorDAO.guardarMetaValor(metavalor);
		}
		
		if (proyectometa.metaFinal != null && proyectometa.metaFinal != ""){
			if (proyectometa.metaFinalId != null && proyectometa.metaFinalId > 0){
				meta = MetaDAO.getMetaPorId(proyectometa.metaFinalId);
				meta.setUsuarioActualizo(usuario);
				meta.setFechaActualizacion(new DateTime().toDate());
			}else{
				meta = new Meta();
				meta.setNombre("Meta Final");
				meta.setUsuarioCreo(usuario);
				meta.setFechaCreacion(new DateTime().toDate());
			}
			meta.setDatoTipo(DatoTipoDAO.getDatoTipo(proyectometa.datoTipoId));
			meta.setMetaUnidadMedida(MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(proyectometa.unidadDeMedidaId));
			meta.setMetaTipo(MetaTipoDAO.getMetaTipoPorId(META_ID_FINAL));
			meta.setEstado(1);
			meta.setObjetoId(proyectometa.id);
			meta.setObjetoTipo(proyectometa.objetoTipo);
			MetaDAO.guardarMeta(meta);
			proyectometa.metaFinalId = meta.getId();
			MetaValor metavalor = new MetaValor();
			MetaValorId metavalorid = new MetaValorId(meta.getId(), new DateTime().toDate());
			metavalor.setId(metavalorid);
			metavalor.setMeta(meta);
			metavalor.setUsuario(usuario);
			metavalor = setMetaValorTipo(proyectometa.datoTipoId, metavalor, proyectometa.metaFinal);
			MetaValorDAO.guardarMetaValor(metavalor);
		}
		
		return proyectometa;
	}
	
	private MetaValor setMetaValorTipo(int datoTipoId, MetaValor metaValor, String valor){
		
		switch(datoTipoId){
		case DATOTIPO_TEXTO: //texto
			metaValor.setValorString(valor);
			break;
		case DATOTIPO_ENTERO: //entero
			metaValor.setValorEntero(Utils.String2Int(valor,0));
			break;
		case DATOTIPO_DECIMAL: //decimal
			metaValor.setValorDecimal(Utils.String2BigDecimal(valor, new BigDecimal(0)));
			break;
		case DATOTIPO_BOOLEANO: //booleano
			break;
		case DATOTIPO_FECHA: //fecha
			metaValor.setValorTiempo(Utils.dateFromString(valor));
			break;
		}
		return metaValor;
	}

	private stprestamometa getFechaInicioFinProducto(stprestamometa productometa){
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
		productometa.metaFechaInicio = Utils.formatDate(fechaInicio);
		productometa.metaFechaFin = Utils.formatDate(fechaFin);
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
