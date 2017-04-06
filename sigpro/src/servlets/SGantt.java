package servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.HitoDAO;
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
		HashMap<Integer,List<Integer>> predecesores;
		try {
		List<FileItem> parametros = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		for (FileItem parametro : parametros)
	    {
			if (parametro.isFormField()){

				if (parametro.getFieldName().compareTo("accion")==0 && parametro.getString().length()>0){
					accion = parametro.getString();
				}else if (parametro.getFieldName().compareTo("proyecto_id")==0 && parametro.getString().length()>0){
					proyectoId = Integer.parseInt(parametro.getString());
				}
			}
		}


		if(accion.equals("getProyecto")){
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
			String items_actividad="";
			String items_subproducto="";
			String items_producto="";
			String items_componente="";
			predecesores = new HashMap<>();
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
											construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(), 4, true, actividad.getFechaInicio(), actividad.getFechaFin(),false));
								}
							}
							items_subproducto = String.join(items_subproducto.trim().length()>0 ? ",":"", items_subproducto,
									construirItem(null,subproducto.getId(),OBJETO_ID_SUBPRODUCTO, subproducto.getNombre(),3, true, fechaPrimeraActividad, null,false));
							items_subproducto = items_actividad.trim().length() > 0 ? String.join(",", items_subproducto,items_actividad) : items_subproducto;
						}
						items_producto = String.join(items_producto.trim().length()>0 ? "," : "",items_producto,
								construirItem(null,producto.getId(),OBJETO_ID_PRODUCTO, producto.getNombre(),2, true, fechaPrimeraActividad, null,false));
						items_producto = items_subproducto.trim().length() > 0 ? String.join(",",items_producto, items_subproducto) : items_producto;

						items_actividad = obtenerItemsActividades(producto.getId(),3,3,predecesores);
						items_producto = (items_actividad.length()>0 ? String.join(",", items_producto,items_actividad):items_producto);

					}
					items_componente = String.join(items_componente.trim().length()>0 ? "," : "",items_componente,
							construirItem(null,componente.getId(),OBJETO_ID_COMPONENTE,componente.getNombre(),1, true, fechaPrimeraActividad, null,false));
					items_componente = items_producto.trim().length() > 0 ? String.join(",", items_componente,items_producto) : items_componente;

					items_actividad = obtenerItemsActividades(componente.getId(),2,2,predecesores);
					items_componente = (items_actividad.length()>0 ? String.join(",", items_componente,items_actividad):items_componente);
				}


				items = String.join(",",construirItem(null,proyecto.getId(),OBJETO_ID_PROYECTO,proyecto.getNombre(),null, true, fechaPrimeraActividad, null,false),items_componente);
				List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(0, 0, proyectoId, null, null, null, null, null);

				for (Hito hito:hitos){
					items = String.join(",",items,
							construirItem(null,hito.getId(),OBJETO_ID_HITO, hito.getNombre(), 1, null, hito.getFecha(), null,true));
				}
			}
			String estructruaPredecesores = getEstructuraPredecesores(predecesores);

			items = String.join("","{\"items\" : [", items,"]"
					,estructruaPredecesores!=null && estructruaPredecesores.length()>0 ? "," : ""
					,estructruaPredecesores,"}");


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

				project.imporatarArchivo(project.getProject(),usuario);
				if (file.exists())
				{
					file.delete();
				}

		}
		else if(accion.equals("exportar")){
			try{
				CProject project = new CProject("");
				project.exportarProject(proyectoId, usuario);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(accion.equals("getKanban")){
			items = "";
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);
			for (Componente componente : componentes){
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				for (Producto producto : productos){
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					for (Subproducto subproducto : subproductos){
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null, null, null, null, null, usuario);
						for (Actividad actividad : actividades ){
							items = String.join(items.length()>0 ? "," : "", items,
									construirItemKanban(actividad.getNombre(), actividad.getPorcentajeAvance()));
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){
						items = String.join(items.length()>0 ? "," : "", items,
								construirItemKanban(actividad.getNombre(), actividad.getPorcentajeAvance()));
					}

				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					items = String.join(items.length()>0 ? "," : "", items,
							construirItemKanban(actividad.getNombre(), actividad.getPorcentajeAvance()));
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				items = String.join(items.length()>0 ? "," : "", items,
						construirItemKanban(actividad.getNombre(), actividad.getPorcentajeAvance()));
			}

			items = String.join("","{\"items\" : [", items,"]}");

		}

		}catch(Exception e){
			e.printStackTrace();
		}

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");


        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(items.getBytes("UTF-8"));
        gz.close();
        output.close();

	}

	private String construirItem(Integer idItem,Integer objetoId, Integer objetoTipo, String content,Integer identation,
			Boolean isExpanded,Date start,Date finish ,boolean isMilestone){

		String cadena = String.join("", "{\"id\" :",idItem!=null ? idItem.toString() : "0",","
				,"\"content\" :\"",content,"\","
				,"\"objetoId\" :\"",objetoTipo.toString(),"\"," ,"\"objetoTipo\" :\"",objetoTipo.toString(),"\",",
				identation!=null ? "\"indentation\" :" : "", identation!=null ? identation.toString() :"",identation!=null ? "," : "",
				isExpanded!=null ? "\"isExpanded\" :\"":"" ,isExpanded!=null ? (isExpanded ? "true" : "false"):"",isExpanded!=null ?"\",":"",
				start !=null ? "\"start\" :\"" : "", start!=null ? Utils.formatDateHour24(start) :"", start!=null ? "\"" : "",
			    start!=null && finish!=null ? "," : "",
				finish!=null ? "\"finish\" :\"" : "",finish!=null ? Utils.formatDateHour24(finish) : "",finish!=null ?"\"":"",
				",\"isMilestone\":",isMilestone? "\"true\"" : "\"false\"",
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
				List<Integer> idPredecesores = new ArrayList<>();
				if (actividad.getPredObjetoId()!=null){
					idPredecesores.add(actividad.getPredObjetoId());
					predecesores.put(actividad.getId(), idPredecesores);
				}
				ret = String.join(ret.trim().length()>0 ? "," : "",ret,
						construirItem(actividad.getId(),actividad.getId(),OBJETO_ID_ACTIVIDAD,actividad.getNombre(), nivelObjeto, true, actividad.getFechaInicio(), actividad.getFechaFin(),false));
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

	private String construirItemKanban(String nombre, Integer porcentaje){
		Integer estado = 0;
		if (porcentaje == 0){
			estado = 0;
		}else if (porcentaje >0 && porcentaje < 100){
			estado = 1;
		}else if (porcentaje == 100){
			estado = 2;
		}
		return String.join("", "{\"name\": \"", nombre,"\", \"estadoId\" : " ,estado.toString(),
				",\"percentageValue\": \"",porcentaje.toString(),"%\"",
				"}");

	}
}
