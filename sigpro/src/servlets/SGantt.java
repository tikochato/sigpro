package servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.shiro.codec.Base64;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.EstructuraProyectoDAO;
import dao.HitoDAO;
import dao.MetaValorDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Hito;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CProject;
import utilities.Utils;


@WebServlet("/SGantt")
public class SGantt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_HITO = 0;
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;


    public SGantt() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String items = "";
		String accion = "";
		Integer proyectoId = null;
		Integer objetoId = null;
		Integer objetoTipo = null;
		String nombre = "";
		Date inicio = null;
		Date fin = null;
		boolean completada = false;
		boolean multiproyecto = false;
		Integer programaId = null;

		Map<String, String> map = null;

		HashMap<Integer,List<Integer>> predecesores;
		try {
			List<FileItem> parametros = null;
			if (request.getContentType() != null && request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) {


				parametros = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem parametro : parametros)
			    {
					if (parametro.isFormField()){

						if (parametro.getFieldName().compareTo("accion")==0 && parametro.getString().length()>0){
							accion = parametro.getString();
						}else if (parametro.getFieldName().compareTo("proyecto_id")==0 && parametro.getString().length()>0){
							proyectoId = Integer.parseInt(parametro.getString());
						}else if (parametro.getFieldName().compareTo("multiproyecto")==0 && parametro.getString().length()>0){
							multiproyecto = parametro.getString().equals("1");
						}else if (parametro.getFieldName().compareTo("programa_id")==0 && parametro.getString().length()>0)
							programaId = Integer.parseInt(parametro.getString());
					}
				}
			}else {
				request.setCharacterEncoding("UTF-8");

				Gson gson = new Gson();
				Type type = new TypeToken<Map<String, String>>(){}.getType();
				StringBuilder sb = new StringBuilder();
				BufferedReader br = request.getReader();
				String str;
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				;
				map = gson.fromJson(sb.toString(), type);
				accion = map.get("accion");
			}

		if(accion.equals("getProyecto")){
			predecesores = new HashMap<>();
			
			items = cargarProyecto(proyectoId,usuario,predecesores);
			

			String estructruaPredecesores = getEstructuraPredecesores(predecesores);
			items = String.join("","{\"items\" : [", items,"]"
					,estructruaPredecesores!=null && estructruaPredecesores.length()>0 ? "," : ""
					,estructruaPredecesores,"}");

			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");


	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(items.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}else if(accion.equals("getPrograma")){
			predecesores = new HashMap<>();
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			items = "";

			List<Proyecto> proyectos = ProyectoDAO.getProyectosPorPrograma(programaId);
			for (Proyecto proyecto : proyectos){
				items = String.join(items.length()> 0 ? "," : "", items, getProyecto(proyecto.getId(),usuario,predecesores));
			}

			String estructruaPredecesores = getEstructuraPredecesores(predecesores);

			items = String.join("","{\"items\" : [", items,"]"
					,estructruaPredecesores!=null && estructruaPredecesores.length()>0 ? "," : ""
					,estructruaPredecesores,"}");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(items.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}else if(accion.equals("importar")){

				String directorioTemporal = "/archivos/temporales";
				ArrayList<FileItem> fileItems=new ArrayList<FileItem>();

				Long time=  0L;
				File file = null;

				for (FileItem parametro : parametros)
			    {
					if (!parametro.isFormField()){
						fileItems.add(parametro);

						File directorio = new File(directorioTemporal);
						if (directorio.exists()){
							directorio.mkdirs();
							time = new Date().getTime();
							file = new File(directorioTemporal,"temp_" + time.toString());
							while (file.exists()){
								time = new Date().getTime();
								file = new File(directorioTemporal,"temp_" + time.toString());
							}
							parametro.write(file);
						}
					}
			    }

				CProject project = new CProject(directorioTemporal + "/temp_"+ time.toString());

				boolean creado = project.imporatarArchivo(project.getProject(),usuario,multiproyecto);

				if (file.exists())
				{
					file.delete();
				}
				if (creado){
				    items = "{ \"success\": true }";
				}else{
					items = "{ \"success\": false }";
				}

				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");


		        OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
		        gz.write(items.getBytes("UTF-8"));
		        gz.close();
		        output.close();


		}
		else if(accion.equals("exportar")){
			try{
				CProject project = new CProject("");
				String path = project.exportarProject(proyectoId, usuario);

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
					response.setHeader("Cache-Control", "no-cache");
					response.setHeader("Content-Disposition", "attachment; Prestamo.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}

			}catch(Exception e){
				e.printStackTrace();
			}
		}else if (accion.equals("modificarData")){

			objetoId = Utils.String2Int(map.get("objetoId"));
			objetoTipo = Utils.String2Int(map.get("objetoTipo"));
			nombre = map.get("nombre");
			inicio=Utils.dateFromString(map.get("inicio"));
			fin = Utils.dateFromString(map.get("fin"));
			completada = Utils.String2Boolean(map.get("completada"), 0) == 1;

			boolean guardado = modificarDatos(objetoTipo, objetoId, nombre, inicio, fin,usuario,completada);
			String response_text = "{ \"success\": " + (guardado ? "true" : "false") +" }";
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");


	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String construirItem(Integer idItem,Integer objetoId, Integer objetoTipo, String content,Integer identation,
			Boolean isExpanded,Date start,Date finish ,boolean isMilestone,Integer duracion,BigDecimal costo,
			BigDecimal metaPlanificada, BigDecimal metaReal){

		String f_inicio = Utils.formatDate(start);
		String f_fin = Utils.formatDate(finish);

		content = content.replace("\"", "");
		String cadena = String.join("", "{\"id\" :",idItem!=null ? idItem.toString() : "0",","
				,"\"content\" :\"",content,"\","
				,"\"objetoId\" :\"",objetoId.toString(),"\"," ,"\"objetoTipo\" :\"",objetoTipo.toString(),"\",",
				identation!=null ? "\"indentation\" :" : "", identation!=null ? identation.toString() :"",identation!=null ? "," : "",
				isExpanded!=null ? "\"isExpanded\" :\"":"" ,isExpanded!=null ? (isExpanded ? "true" : "false"):"",isExpanded!=null ?"\",":"",
				start !=null ? "\"start\" :\"" : "", start!=null ? f_inicio + " 00:00:00"  :"", start!=null ? "\"" : "",
			    start!=null && finish!=null ? "," : "",
				finish!=null ? "\"finish\" :\"" : "",finish!=null ? f_fin + " 23:59:59" : "",finish!=null ?"\"":"",
				finish!=null ? "," : "",
				duracion!=null ? "\"duracion\" :\"" : "",duracion!=null ? duracion.toString() : "",duracion!=null ?"\"":"",
				",\"isMilestone\":",isMilestone? "\"true\"" : "\"false\"",

			    costo!=null ? ",\"costo\" :" : "",costo!=null ? costo.toString() : "",costo!=null ?"":"",
			    metaPlanificada!=null ? ",\"metaPlanificada\" :" : "",metaPlanificada!=null ?
			    		metaPlanificada.floatValue() + "" : "",metaPlanificada!=null ?"":"",
				metaReal!=null ? ",\"metaReal\" :" : "",metaReal!=null ?
						metaReal.floatValue() + "" : "",metaReal!=null ?"":"",

				"}"
			);
		
		return cadena.replaceAll(",,", ",");
	}

	private String obtenerItemsActividades(int objetoId,int objetoTipo,int nivelObjeto,HashMap<Integer,List<Integer>> predecesores){
		String ret = "";

		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, objetoId, objetoTipo
				, null, null, null, null, null, null);
		if (!actividades.isEmpty()){
			for (Actividad actividad : actividades){

				String retRec = obtenerItemsActividadesRecursivas(actividad.getId(), OBJETO_ID_ACTIVIDAD, nivelObjeto + 1, predecesores);

				List<Integer> idPredecesores = new ArrayList<>();
				if (actividad.getPredObjetoId()!=null && retRec!=null && retRec.isEmpty()){
					idPredecesores.add(actividad.getPredObjetoId());
					predecesores.put(actividad.getId(), idPredecesores);
				}

				ret = String.join(ret.trim().length()>0 ? "," : "",ret,
						construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(),
								nivelObjeto, true, actividad.getFechaInicio(), actividad.getFechaFin(),
								false,actividad.getDuracion(),actividad.getCosto(),null,null));


				if (retRec!=null && retRec.length()>0){
					ret = String.join(",", ret,retRec);
				}
			}
		}
		return ret;
	}

	private String obtenerItemsActividadesRecursivas(int objetoId,int objetoTipo,Integer nivelObjeto,HashMap<Integer,List<Integer>> predecesores){
		String ret = "";

		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, objetoId, objetoTipo
				, null, null, null, null, null, null);
		if (!actividades.isEmpty()){
			for (Actividad actividad : actividades){

				List<Integer> idPredecesores = new ArrayList<>();
				if (actividad.getPredObjetoId()!=null){
					idPredecesores.add(actividad.getPredObjetoId());
					predecesores.put(actividad.getId(), idPredecesores);
				}
				ret = String.join(ret.trim().length()>0 ? "," : "",ret,
						construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(),
								nivelObjeto, true, actividad.getFechaInicio(), actividad.getFechaFin(),false,
								actividad.getDuracion(),actividad.getCosto(),null,null));

				String retRec = obtenerItemsActividadesRecursivas(actividad.getId(), OBJETO_ID_ACTIVIDAD, nivelObjeto + 1, predecesores);

				if (retRec!=null && retRec.length()>0){
					ret = String.join(",", ret,retRec);
				}
			}
		}
		return ret;
	}

	private String getEstructuraPredecesores(HashMap<Integer,List<Integer>> predecesores){
		String ret = "";
		for (Entry<Integer, List<Integer>> predecesor : predecesores.entrySet()) {
			String itemPred = "";
			if (predecesor.getValue()!=null && predecesor.getValue().size()>0){
				 itemPred = String.join("", "{\"id\":",predecesor.getKey().toString(),",",
						"\"idPredecesor\":", predecesor.getValue().get(0).toString(),"}");
			}
		    ret = itemPred.length()> 0 ?  String.join(ret.length()>0 ? ",": "", ret,itemPred) : ret;
		}


		if (ret.length()>0){
			ret = String.join("","\"predecesores\": [",ret,"]");
		}
		return ret;
	}

	private boolean modificarDatos(int objetoTipo, int objetoId, String nombre, Date inicio, Date fin,String usuario,boolean completada){
		switch(objetoTipo){
			case 1:
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(objetoId, usuario);
				if (proyecto!=null){
					proyecto.setNombre(nombre);
					proyecto.setUsuarioActualizo(usuario);
					proyecto.setFechaActualizacion(new Date());
					return ProyectoDAO.guardarProyecto(proyecto);
				}
				break;
			case 2:
				Componente componente = ComponenteDAO.getComponentePorId(objetoId, usuario);
				if (componente!=null){
					componente.setNombre(nombre);
					componente.setUsuarioActualizo(usuario);
					componente.setFechaActualizacion(new Date());
					return ComponenteDAO.guardarComponente(componente);
				}
				break;
			case 3:
				Producto  producto = ProductoDAO.getProductoPorId(objetoId);
				if  (producto != null){
					producto.setNombre(nombre);
					producto.setUsuarioActualizo(usuario);
					producto.setFechaActualizacion(new Date());
					return ProductoDAO.guardarProducto(producto);
				}
				break;
			case 4:
				Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId);
				if (subproducto !=null){
					subproducto.setNombre(nombre);
					subproducto.setUsuarioActualizo(usuario);
					subproducto.setFechaActualizacion(new Date());
					return SubproductoDAO.guardarSubproducto(subproducto);
				}
				break;
			case 5:
				Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
				if (actividad!=null){
					actividad.setNombre(nombre);
					actividad.setFechaInicio(inicio);
					actividad.setFechaFin(fin);
					actividad.setPorcentajeAvance(100);
					actividad.setUsuarioActualizo(usuario);
					actividad.setFechaActualizacion(new Date());
					return ActividadDAO.guardarActividad(actividad);
				}
		}
		return false;
	}

	private String getProyecto(Integer proyectoId,String usuario, HashMap<Integer,List<Integer>> predecesores){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		String items_actividad="";
		String items_subproducto="";
		String items_producto="";
		String items_componente="";
		String items="";

		//predecesores = new HashMap<>();
		if (proyecto !=null){
			Date fechaPrimeraActividad = null;
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyecto.getId(),
					null, null, null, null, null, usuario);
			items_componente="";
			for (Componente componente :componentes){
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				items_producto="";
				for (Producto producto : productos){
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(), null, null, null, null, null, usuario);

					items_subproducto="";
					for (Subproducto subproducto : subproductos){

						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), 4,
								null,null, null, null, null, usuario);
						items_actividad="";
						if (!actividades.isEmpty()){

							for (Actividad actividad : actividades){
								if (fechaPrimeraActividad==null) {
									fechaPrimeraActividad = actividad.getFechaInicio();
								}
								List<Integer> idPredecesores = new ArrayList<>();
								if (actividad.getPredObjetoId()!=null){
									idPredecesores.add(actividad.getPredObjetoId());
									predecesores.put(actividad.getId(), idPredecesores);
								}

								items_actividad = String.join(items_actividad.trim().length()>0 ? "," : "",items_actividad,
										construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(), 4,
												true, actividad.getFechaInicio(), actividad.getFechaFin(),
												false,actividad.getDuracion(),actividad.getCosto(),null,null));

								String items_actividad_recursiva = obtenerItemsActividades(actividad.getId(),5,5,predecesores);

								items_actividad = String.join(items_actividad_recursiva !=null && items_actividad_recursiva.trim().length()>0 ? "," : "", items_actividad,items_actividad_recursiva!=null && items_actividad_recursiva.length() > 0 ?
										items_actividad_recursiva : "");

							}
						}
						items_subproducto = String.join(items_subproducto.trim().length()>0 ? ",":"", items_subproducto,
								construirItem(subproducto.getId(),subproducto.getId(),OBJETO_ID_SUBPRODUCTO, subproducto.getNombre(),3, true,
										fechaPrimeraActividad, null,false,null,subproducto.getCosto(),null,null));
						items_subproducto = items_actividad.trim().length() > 0 ? String.join(",", items_subproducto,items_actividad) : items_subproducto;
					}
					BigDecimal metaPlanificada = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(2, producto.getId(), OBJETO_ID_PRODUCTO);
					BigDecimal metaReal = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(1, producto.getId(), OBJETO_ID_PRODUCTO);

					items_producto = String.join(items_producto.trim().length()>0 ? "," : "",items_producto,
							construirItem(producto.getId(),producto.getId(),OBJETO_ID_PRODUCTO, producto.getNombre(),2, true, fechaPrimeraActividad,
									null,false,null,producto.getCosto(),metaPlanificada,metaReal));
					items_producto = items_subproducto.trim().length() > 0 ? String.join(",",items_producto, items_subproducto) : items_producto;

					items_actividad = obtenerItemsActividades(producto.getId(),3,3,predecesores);
					items_producto = (items_actividad.length()>0 ? String.join(",", items_producto,items_actividad):items_producto);

				}
				items_componente = String.join(items_componente.trim().length()>0 ? "," : "",items_componente,
						construirItem(componente.getId(),componente.getId(),OBJETO_ID_COMPONENTE,componente.getNombre(),1, true, fechaPrimeraActividad,
								null,false,null,componente.getCosto(),null,null));
				items_componente = items_producto.trim().length() > 0 ? String.join(",", items_componente,items_producto) : items_componente;

				items_actividad = obtenerItemsActividades(componente.getId(),2,2,predecesores);
				items_componente = (items_actividad.length()>0 ? String.join(",", items_componente,items_actividad):items_componente);
			}


			items = String.join(",",construirItem(proyecto.getId(),proyecto.getId(),OBJETO_ID_PROYECTO,proyecto.getNombre()
					,null, true, fechaPrimeraActividad, null,false,null,proyecto.getCosto(),null,null),items_componente);
			List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, proyectoId, null, null, null, null, null);

			for (Hito hito:hitos){
				items = String.join(",",items,
						construirItem(hito.getId(),hito.getId(),OBJETO_ID_HITO, hito.getNombre(), 1, null, hito.getFecha(),
								null,true,null,null,null,null));
			}
		}
		//String estructruaPredecesores = getEstructuraPredecesores(predecesores);



		return items;

	}


	
	private String cargarProyecto(Integer proyectoId,String usuario, HashMap<Integer,List<Integer>> predecesores){
		
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(proyectoId);
		String items = "";
		for(Object objeto : estructuraProyecto){
			String item="";
			Object[] obj = (Object[]) objeto;
			Integer tipoObjeto =((BigInteger)obj[2]).intValue();
			switch(tipoObjeto){
				case 1:
					item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_PROYECTO, (String)obj[1], null, 
							true, (Date)obj[5], null, false,null, (BigDecimal) obj[9], null, null);
					
					break;
				case 2:
					item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_COMPONENTE, (String)obj[1], (Integer)obj[4], 
							true, null, null, false,null, (BigDecimal) obj[9], null, null);
					break;
				case 3:
					BigDecimal metaPlanificada = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(2, (Integer)obj[0], OBJETO_ID_PRODUCTO);
					BigDecimal metaReal = MetaValorDAO.getMetaValorPorMetaTipoObjetoObjetoTipo(1, (Integer)obj[0], OBJETO_ID_PRODUCTO);
					
					item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_PRODUCTO, (String)obj[1], (Integer)obj[4], 
							true, null, null, false,null, (BigDecimal) obj[9], metaPlanificada,metaReal);
					break;
				case 4:
					item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_SUBPRODUCTO, (String)obj[1], (Integer)obj[4], 
							true, null, null, false,null, (BigDecimal) obj[9], null, null);
					break;
				case 5:
					item = construirItem((Integer)obj[0], (Integer)obj[0], OBJETO_ID_ACTIVIDAD, (String)obj[1], (Integer)obj[4], 
							true, (Date)obj[5], (Date)obj[6], false,(Integer) obj[7],(BigDecimal) obj[9], null, null);
					if (obj[10]!=null){
						List<Integer> idPredecesores = new ArrayList<>();
						idPredecesores.add(((BigInteger)obj[10]).intValue());
						predecesores.put((Integer)obj[0], idPredecesores);
					}
					break;
			}
			items = String.join(items.length() > 0 ? "," : "", items,item);
		}
		return items;
		
	}
	
	


}
