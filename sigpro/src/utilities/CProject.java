package utilities;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.writer.ProjectWriter;
import pojo.Actividad;
import pojo.ActividadTipo;
import pojo.Componente;
import pojo.ComponenteTipo;
import pojo.Cooperante;
import pojo.Producto;
import pojo.ProductoTipo;
import pojo.Proyecto;
import pojo.ProyectoTipo;
import pojo.Subproducto;
import pojo.SubproductoTipo;
import pojo.UnidadEjecutora;



public class CProject {
	
	
	
	class stitem {
		int id;
		Integer idUnico;
		String contenido;
		int indentacion;
		Boolean expandido;
		Date fechaInicial;
		Date fechaFinal;
		int duracion;
		String unidades;
		boolean esHito;
		Integer []  idPredecesores;
		int objetoId;
		int objetoTipo;
		
	}
	
	
	static int COOPERANTE_ID_DEFECTO = 1;
	static int PROYECTO_TIPO_ID_DEFECTO = 3;
	static int UNIDAD_EJECUTORA_ID_DEFECTO = 1;
	static int COMPONENTE_TIPO_ID_DEFECTO = 2;
	static int PRODUCTO_TIPO_ID_DEFECTO = 7;
	static int SUBPRODUCTO_TIPO__ID_DEFECTO = 1;
	static int ACTIVIDAD_ID_DEFECTO = 7;
	
	ProjectReader reader;
	ProjectFile project;
	Integer indetnacion;
	String itemsProject;
	
	
	HashMap<Integer,stitem> items;
	
	
	
	public CProject (String nombre){
		try{
			reader = new MPPReader ();
			 project = reader.read(nombre);
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public ProjectReader getReader() {
		return reader;
	}

	public void setReader(ProjectReader reader) {
		this.reader = reader;
	}

	public ProjectFile getProject() {
		return project;
	}

	public void setProject(ProjectFile project) {
		this.project = project;
	}

	
	public Integer getIndetnacion() {
		return indetnacion;
	}

	public void setIndetnacion(Integer indetnacion) {
		this.indetnacion = indetnacion;
	}
	
	
	public Proyecto imporatarArchivo(ProjectFile projectFile, String usuario){
		indetnacion = 0;
		itemsProject = "";
		items = new HashMap<>();
		Proyecto proyecto = null;
		for (Task task : projectFile.getChildTasks())
		{
			if (task.getChildTasks()!=null && task.getChildTasks().size()>0){
				for (Task task1 : task.getChildTasks()){ //Proyectos
					
					if (task1.getChildTasks()!=null && task1.getChildTasks().size()>0){
						System.out.println(task1.getName());
						proyecto = crearProyecto(task1, usuario);
						cargarItem(task1,proyecto.getId(),1);
						for (Task task2 : task1.getChildTasks()){ //componentes
							if (task2.getChildTasks()!=null && task2.getChildTasks().size()>0){
								System.out.println("\t" + task2.getName());
								Componente componente = crearComponente(task2, proyecto, usuario);
								cargarItem(task2,componente.getId(),2);
								for (Task task3 : task2.getChildTasks()){ //producto
									if (task3.getChildTasks()!=null && task3.getChildTasks().size()>0){
										System.out.println("\t\t" + task3.getName());
										Producto producto = crearProducto(task3, componente, usuario);
										cargarItem(task3,producto.getId(),3);
										for (Task task4 : task3.getChildTasks()){ //subproductos
											if (task4.getChildTasks()!=null && task4.getChildTasks().size()>0){
												System.out.println("\t\t\t" + task4.getName());
												Subproducto subproducto = crearSubproducto(task4, producto, usuario);
												cargarItem(task4,subproducto.getId(),4);
												for (Task task5 : task4.getChildTasks()){ //actividades
													System.out.println("\t\t\t\t" + task5.getName());
													Actividad actividad = crearActividad(task5, usuario, subproducto.getId(),4);
													cargarItem(task5,actividad.getId(),5);
												}
											}else{
												Actividad actividad = crearActividad(task4, usuario, producto.getId(),3);
												cargarItem(task4,actividad.getId(),5);
											}
										}
									}else{
										Actividad actividad = crearActividad(task3, usuario, componente.getId(),2);
										cargarItem(task3,actividad.getId(),5);
									}
								}
								
							}else{
								Actividad actividad = crearActividad(task2, usuario, proyecto.getId(),1);
								cargarItem(task2,actividad.getId(),5);
								
							}
						}
					}else{
						//crear primera actividad
					}
				}	
			}
		}
		return proyecto;
	}
	
	public Proyecto crearProyecto(Task task,String usuario){
		Cooperante cooperante = new Cooperante();
		cooperante.setId(COOPERANTE_ID_DEFECTO);
		ProyectoTipo proyectoTipo = new ProyectoTipo();
		proyectoTipo.setId(PROYECTO_TIPO_ID_DEFECTO);
		UnidadEjecutora unidadEjecturoa = new UnidadEjecutora();
		unidadEjecturoa.setUnidadEjecutora(UNIDAD_EJECUTORA_ID_DEFECTO);
		Proyecto proyecto = new Proyecto(cooperante, proyectoTipo, unidadEjecturoa
				, task.getName(), null, usuario, null, new Date(), null, 1
				, null, null, null, null, null, null, null, null, null, null, null, null, null,null,null);
		ProyectoDAO.guardarProyecto(proyecto);
		
		return ProyectoDAO.guardarProyecto(proyecto) ? proyecto : null;
	}
	
	public Componente crearComponente(Task task,Proyecto proyecto ,String usuario){
		ComponenteTipo componenteTipo = new ComponenteTipo();
		componenteTipo.setId(COMPONENTE_TIPO_ID_DEFECTO);
		UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
		unidadEjecutora.setUnidadEjecutora(UNIDAD_EJECUTORA_ID_DEFECTO);
		
		
		Componente componente = new Componente(componenteTipo, proyecto, unidadEjecutora, task.getName(), null, usuario, null, new Date(), null, 1
				, null, null, null, null, null, null, null, null, null, null,null,null);
		
		return ComponenteDAO.guardarComponente(componente) ? componente : null;
	}
	
	public Producto crearProducto (Task task, Componente componente,String usuario){
		ProductoTipo productoTipo = new ProductoTipo();
		productoTipo.setId(PRODUCTO_TIPO_ID_DEFECTO);
		UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
		unidadEjecutora.setUnidadEjecutora(UNIDAD_EJECUTORA_ID_DEFECTO);
		
		Producto producto = new Producto(componente, productoTipo, unidadEjecutora
				,task.getName() , null, usuario, null, new Date(), null,1, 
				 null, null, null, null, null, null, null, 
				null, null, null,null,null);
		
		
		return ProductoDAO.guardarProducto(producto) ? producto : null;
	}
	
	public Subproducto crearSubproducto(Task task, Producto producto, String usuario){
		SubproductoTipo subproductoTipo = new SubproductoTipo();
		subproductoTipo.setId(SUBPRODUCTO_TIPO__ID_DEFECTO);
		
		UnidadEjecutora unidadEjecutroa = new UnidadEjecutora();
		unidadEjecutroa.setUnidadEjecutora(UNIDAD_EJECUTORA_ID_DEFECTO);
		
		Subproducto subproducto = new Subproducto(producto, subproductoTipo, unidadEjecutroa,task.getName(), null, usuario, null, new Date(), null, 1, 
				null, null, null, null, null, null, null, null, null,null,null);
		
		return SubproductoDAO.guardarSubproducto(subproducto) ? subproducto : null;
	}
	
	public Actividad crearActividad(Task task,String usuario, Integer objetoId, Integer objetoTipo){
		ActividadTipo actividadTipo = new ActividadTipo();
		actividadTipo.setId(ACTIVIDAD_ID_DEFECTO);
		
		Integer [] predecesores  = getListaPredecesores(task.getPredecessors());
		
		stitem itemPredecesor = null;
		if (predecesores!=null && predecesores.length > 0){
			 itemPredecesor =  items.get(predecesores[0]);	
		}
		
		
		
		Actividad actividad = new Actividad(actividadTipo, task.getName(), null, task.getStart(), task.getFinish()
				,0,usuario, null, new Date(), 
				null, 1, null, null, null, null, 
				null, null, null , objetoId, objetoTipo, 
				(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName()
				 
				 ,itemPredecesor!=null ? itemPredecesor.objetoId : null
						 , itemPredecesor != null ? itemPredecesor.objetoTipo : null, null, null,null,null);
		return ActividadDAO.guardarActividad(actividad) ? actividad : null;
	}
	
	public void cargarItem(Task task,int objetoId, int objetoTipo){
		stitem item_ = new stitem();
		item_.id = task.getUniqueID();
		item_.contenido = task.getName();
		item_.indentacion = indetnacion;
		item_.expandido = true;
		item_.fechaInicial = task.getStart();
		item_.fechaFinal = task.getFinish();
		item_.esHito = task.getMilestone();
		item_.idPredecesores = getListaPredecesores(task.getPredecessors());
		item_.duracion = (int) task.getDuration().getDuration();
		item_.unidades = task.getDuration().getUnits().getName();
		item_.objetoId = objetoId;
		item_.objetoTipo = objetoTipo;
		items.put(task.getUniqueID(), item_);
	}
	
	

	public  String getTask(ProjectFile projectFile){
		
		indetnacion = 0;
		itemsProject = "";
		items = new HashMap<>();
		for (Task task : projectFile.getChildTasks())
		   {
		      //itemsProject = String.join(",", construirItem(task.getName(),null,true,task.getStart(),task.getFinish(),false));
		      listaJerarquica(task);
		   }
		
		Iterator<Map.Entry<Integer, stitem>> entries = items.entrySet().iterator();
		while (entries.hasNext()) {
		    Map.Entry<Integer, stitem> item = entries.next();
		    System.out.println("id=" + item.getValue().id + " Contenido="+ item.getValue().contenido 
		    		+ " indentacion=" +item.getValue().indentacion + " fechaInicial=" + Utils.formatDate(item.getValue().fechaInicial)
		    		+ " duracion=" + item.getValue().duracion + " predecesores=" + (item.getValue().idPredecesores!=null ? Arrays.toString(item.getValue().idPredecesores) : ""));
		    itemsProject =String.join(itemsProject.trim().length()>0 ?",":"",itemsProject,construirItem(item.getValue().contenido, item.getValue().indentacion, item.getValue().expandido
		    		, item.getValue().fechaInicial, item.getValue().fechaFinal, item.getValue().esHito) );
		    
		}
		
		return itemsProject;
		
	}
	
	
	
	private void listaJerarquica(Task task)
	{
		indetnacion ++;
		//itemsProject = String.join(itemsProject.trim().length()>0 ?"," : "",itemsProject,  construirItem(task.getName(),indetnacion,true,task.getStart(),task.getFinish(),task.getMilestone()));
		stitem item_ = new stitem();
		item_.id = task.getUniqueID();
		item_.contenido = task.getName();
		item_.indentacion = indetnacion;
		item_.expandido = true;
		item_.fechaInicial = task.getStart();
		item_.fechaFinal = task.getFinish();
		item_.esHito = task.getMilestone();
		item_.idPredecesores = getListaPredecesores(task.getPredecessors());
		item_.duracion = (int) task.getDuration().getDuration();
		item_.unidades = task.getDuration().getUnits().getName();
		items.put(task.getUniqueID(), item_);
		
		
		for (Task child : task.getChildTasks())
		{
	      listaJerarquica(child);
		}
		indetnacion --;
	}
	
	private Integer [] getListaPredecesores(List<Relation> predecesores){
		if (predecesores.size()>0){
			Integer [] arregloPrdecesores = new Integer[predecesores.size()];
			int i=0;
			for (Relation relation : predecesores){
				arregloPrdecesores[i] = relation.getTargetTask().getUniqueID();
			}
			return arregloPrdecesores;
		}
		return null;
	}
	
	private String construirItem(String content,Integer identation,Boolean isExpanded,Date start,Date finish
			,boolean isMilestone){
		return String.join("", "{\"content\" :\"",content,"\",",
				identation!=null ? "\"indentation\" :" : "", identation!=null ? identation.toString() :"",identation!=null ? "," : "", 
				isExpanded!=null ? "\"isExpanded\" :\"":"" ,isExpanded!=null ? (isExpanded ? "true" : "false"):"",isExpanded!=null ?"\",":"",
				start !=null ? "\"start\" :\"" : "", start!=null ? Utils.formatDateHour24(start) :"", start!=null ? "\"" : "",
			    start!=null && finish!=null ? "," : "",
				finish!=null ? "\"finish\" :\"" : "",finish!=null ? Utils.formatDateHour24(finish) : "",finish!=null ?"\"":"",
				",\"isMilestone\":",isMilestone? "\"true\"" : "\"false\"",
				"}"
			);
	}
	
	
	
	
	public void exportarProject(int idProyecto,String usuario) throws Exception
	{
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(idProyecto, usuario);
		
		if (proyecto !=null){
			project = new ProjectFile();
			Task task1 = project.addTask();
			task1.setName(proyecto.getNombre());
			
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyecto.getId(),
					null, null, null, null, null, usuario);
			for (Componente componente :componentes){
				Task task2 = task1.addTask();
				task2.setName(componente.getNombre());
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				for (Producto producto : productos){
					
					Task task3 = task2.addTask();
					task3.setName(producto.getNombre());
					
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					
					for (Subproducto subproducto : subproductos){
						Task task4 = task3.addTask();
						task4.setName(subproducto.getNombre());
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), 4, 
								null,null, null, null, null, usuario);
						for (Actividad actividad : actividades){
							Task task5 = task4.addTask();
							task5.setName(actividad.getNombre());
							task5.setStart(actividad.getFechaInicio());
							task5.setFinish(actividad.getFechaFin()); 
						} 
					}
				}
			}
			
			ProjectWriter writer = new MPXWriter();
			writer.write(project, proyecto.getNombre()  + ".mpx");
		}
	}	
	
	public void construirTarea(Task task ,String nombre, int duracion,String unidades ,
			Date fechaInicio, Date fechaFinal , boolean esHito,Integer [] idPredecesores){

		task.setName(nombre);
		task.setDurationText(duracion+"d");
		task.setStart(fechaInicio);
		task.setFinish(fechaFinal);

	}
	
	 
	
	
	

}
