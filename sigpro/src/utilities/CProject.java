package utilities;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;

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
	
	
	class item {
		int id;
		Integer idUnico;
		String contenido;
		int indentacion;
		Boolean expandido;
		Date fechaInicial;
		Date fechaFinal;
		boolean esHito;
		List<Relation> predecesor;
	}
	
	ProjectReader reader;
	ProjectFile project;
	Integer indetnacion;
	String itemsProject;
	HashMap<Integer,item> items;
	
	
	
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
		for (Task task : projectFile.getChildTasks())
		   {
		      //itemsProject = String.join(",", construirItem(task.getName(),null,true,task.getStart(),task.getFinish(),false));
		      listaJerarquica(task, "\t");
		   }
		return itemsProject;
	}
	
	
	
	private void listaJerarquica(Task task, String indent)
	{
		indetnacion ++;
		//itemsProject = String.join(itemsProject.trim().length()>0 ?"," : "",itemsProject,  construirItem(task.getName(),indetnacion,true,task.getStart(),task.getFinish(),task.getMilestone()));
		
		item item_ = new item();
		item_.id = task.getID();
		item_.contenido = task.getName();
		item_.indentacion = indetnacion;
		item_.expandido = true;
		item_.fechaInicial = task.getStart();
		item_.fechaFinal = task.getFinish();
		item_.esHito = task.getMilestone();
		item_.predecesor = task.getPredecessors();
		
		items.put(task.getUniqueID(), item_);
		
		for (Task child : task.getChildTasks())
		{
	      listaJerarquica(child, indent + "\t");
	      //System.out.println("Tarea: " + child.getName() + " Fecha Inicio=" + Utils.formatDate(child.getStart()) + " Fecha Final = " + Utils.formatDate(child.getFinish()) + "  parentTask=" + child.getParentTask().getName());
	      
		}
		indetnacion --;
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
	 
	
	
	

}
