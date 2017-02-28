package utilities;


import java.util.Date;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.reader.ProjectReader;



public class CProject {
	ProjectReader reader;
	ProjectFile project;
	Integer indetnacion;
	String items;
	
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
		items = "";
		for (Task task : projectFile.getChildTasks())
		   {
		      System.out.println("Tarea: " + task.getName() + " Fecha Inicio=" + Utils.formatDate(task.getStart()) + " Fecha Fonal = " + Utils.formatDate(task.getFinish()));
		      items = String.join(",", construirItem(task.getName(),null,true,task.getStart(),task.getFinish(),false));
		      listaJerarquica(task, "\t");
		   }
		return items;
	}
	
	private void listaJerarquica(Task task, String indent)
	{
		indetnacion ++;
		for (Task child : task.getChildTasks())
		{
	      listaJerarquica(child, indent + "\t");
	      
	      items = String.join(",", items, construirItem(child.getName(),indetnacion,true,task.getStart(),task.getFinish(),false));
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
				finish!=null ? "\"finish\" :\"" : "",finish!=null ? Utils.formatDateHour24(start) : "",finish!=null ?"\"":"",
				",\"isMilestone\":",isMilestone? "\"true\"" : "\"false\"",
				"}"
			);
	}

}
