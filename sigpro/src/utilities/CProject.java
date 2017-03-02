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
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.writer.ProjectWriter;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;


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
		
	}
	
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
	
	
	
	
	public void generaMPP(int idProyecto,String usuario) throws Exception
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
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), 3, 
							null,null, null, null, null, usuario);
					for (Actividad actividad : actividades){
						Task task4 = task3.addTask();
						task4.setName(actividad.getNombre());
						task4.setStart(actividad.getFechaInicio());
						task4.setFinish(actividad.getFechaFin()); 
					}
				}
			}
			
			ProjectWriter writer = new MPXWriter();
			writer.write(project, proyecto.getNombre()  + ".mpx");
		}
	}	
	
	public void construirTarea(Task task ,String nombre, int duracion,String unidades ,
			Date fechaInicio, Date fechaFinal , boolean esHito,Integer [] idPredecesores){
		
		Relation relation = new Relation(task,task,null,null);
		task.setName(nombre);
		task.setDurationText(duracion+"d");
		task.setStart(fechaInicio);
		task.setFinish(fechaFinal);
		//task.getPredecessors().add(task);
		
		
		
	}
	
	 
	
	
	

}
