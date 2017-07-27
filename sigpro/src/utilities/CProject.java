package utilities;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProgramaDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.ResourceAssignment;
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
import pojo.Programa;
import pojo.ProgramaProyecto;
import pojo.ProgramaProyectoId;
import pojo.ProgramaTipo;
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
	
	
	static int PROGRAMA_TIPO_ID_DEFECTO = 1;
	static int COOPERANTE_ID_DEFECTO = 1;
	static int PROYECTO_TIPO_ID_DEFECTO = 3;
	static int UNIDAD_EJECUTORA_ID_DEFECTO = 1;
	static int COMPONENTE_TIPO_ID_DEFECTO = 2;
	static int PRODUCTO_TIPO_ID_DEFECTO = 7;
	static int SUBPRODUCTO_TIPO__ID_DEFECTO = 1;
	static int ACTIVIDAD_ID_DEFECTO = 7;
	
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;

	
	ProjectReader reader;
	ProjectFile project;
	Integer indetnacion;
	String itemsProject;
	boolean multiproyecto;
	
	HashMap<Integer,stitem> items;
	
	public CProject (String nombre){
		try{
			reader = new MPPReader ();
			 project = reader.read(nombre);
			
		}catch (Exception e){
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
	
	
	public boolean imporatarArchivo(ProjectFile projectFile, String usuario,boolean multiproyecto){
		indetnacion = 0;
		itemsProject = "";
		items = new HashMap<>();
		this.multiproyecto = multiproyecto; 
		return getTask(projectFile,usuario);
	}
	
	public Programa crearPrograma(Task task,String usuario){
		
		ProgramaTipo programaTipo = new ProgramaTipo();
		programaTipo.setId(PROGRAMA_TIPO_ID_DEFECTO);
		
		Programa programa = new Programa(programaTipo, task.getName(), null, usuario, null, new Date(), null, 1, null, null);
		
		return ProgramaDAO.guardarPrograma(programa) ? programa : null;
	}
	
	public boolean crearProgramaProyecto (Proyecto proyecto, Programa programa,String usuario){
		ProgramaProyecto programaProyecto = new ProgramaProyecto();
		programaProyecto.setId( new ProgramaProyectoId(programa.getId(), proyecto.getId()));
		programaProyecto.setEstado(1);
		programaProyecto.setFechaCreacion(new Date());
		programaProyecto.setPrograma(programa);
		programaProyecto.setProyecto(proyecto);
		programaProyecto.setUsuarioCreo(usuario);
		
		if (programa.getProgramaProyectos() == null){
			programa.setProgramaProyectos(new HashSet<ProgramaProyecto>(0));
		}
		programa.getProgramaProyectos().add(programaProyecto);
		return ProgramaDAO.guardarPrograma(programa);
	}
	
	public Proyecto crearProyecto(Task task,String usuario){
		Cooperante cooperante = new Cooperante();
		cooperante.setId(COOPERANTE_ID_DEFECTO);
		ProyectoTipo proyectoTipo = new ProyectoTipo();
		proyectoTipo.setId(PROYECTO_TIPO_ID_DEFECTO);
		UnidadEjecutora unidadEjecturoa = new UnidadEjecutora();
		unidadEjecturoa.setUnidadEjecutora(UNIDAD_EJECUTORA_ID_DEFECTO);
		Proyecto proyecto = new Proyecto(null,cooperante, proyectoTipo, unidadEjecturoa
				, task.getName(), null, usuario, null, new Date(), null, 1
				, null, null, null, null, null, null, null,null, null, null, null, null, null, null,null,null,null,null,null);
		
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
				null, null, null,null,null,null);
		
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
		
		List<ResourceAssignment> recursos = task.getResourceAssignments();
		if (recursos!=null && recursos.size()>0){
			recursos.get(0).getResource().getName();
		}
		
		
		
		Actividad actividad = new Actividad(actividadTipo,null, task.getName(), null, task.getStart(), task.getFinish()
				, task.getPercentageComplete() != null ? (Integer) task.getPercentageComplete(): 0, usuario, null, new Date(), 
				null, 1, null, null, null,null, null, null, null, objetoId, objetoTipo, 
				(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName() 
				,itemPredecesor!=null ? itemPredecesor.objetoId : null
				, itemPredecesor != null ? itemPredecesor.objetoTipo : null
				, null, null, new BigDecimal(task.getCost().toString()),new BigDecimal(task.getActualCost().toString()),null,null
				);
		
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
	

	public  boolean getTask(ProjectFile projectFile,String usuario){
		
		
		itemsProject = "";
		items = new HashMap<>();
		boolean ret = false;
		indetnacion = multiproyecto ? -1 : 0;
		
		for (Task task : projectFile.getChildTasks())
		{
		      listaJerarquica(task,usuario,null);
		}
		ret = true;
		
		
		return ret;
	}
	
	private boolean listaJerarquica(Task task,String usuario,Object objeto)
	{
		indetnacion ++;
		Object objeto_temp = null;
		if (task.getID()>= 0 && 	indetnacion >=  0 ){ 
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
			
			System.out.println(task.getID() + " - " + item_.contenido);
			items.put(task.getUniqueID(), item_);
			
			boolean tieneHijos = task.getChildTasks()!=null && task.getChildTasks().size()>0;
			
			if (indetnacion == 0 && multiproyecto){
				objeto_temp = crearPrograma(task, usuario);
				
			}else if (indetnacion == 1){
				if(tieneHijos){
					objeto_temp =  crearProyecto(task, usuario);
					if (objeto != null){
						crearProgramaProyecto((Proyecto)objeto_temp,(Programa)objeto, usuario);
					}
					cargarItem(task,((Proyecto) objeto_temp).getId(), OBJETO_ID_PROYECTO);
				}
			}else if (indetnacion == 2){
				if (tieneHijos){
					objeto_temp =  crearComponente(task, (Proyecto) objeto, usuario);
					cargarItem(task,((Componente) objeto_temp).getId(), OBJETO_ID_COMPONENTE);
				}
				else{
					objeto_temp = crearActividad(task, usuario,((Proyecto) objeto).getId(),OBJETO_ID_PROYECTO );
					cargarItem(task,((Actividad) objeto_temp).getId(), OBJETO_ID_ACTIVIDAD);
				}
			}else if (indetnacion == 3){
				if (tieneHijos){
					objeto_temp = crearProducto(task, (Componente) objeto, usuario);
					cargarItem(task,((Producto) objeto_temp).getId(), OBJETO_ID_PRODUCTO);
				}
				else {
					objeto_temp = crearActividad(task, usuario,((Componente) objeto).getId(),OBJETO_ID_COMPONENTE );
					cargarItem(task,((Actividad) objeto_temp).getId(), OBJETO_ID_ACTIVIDAD);
				}
			}else if (indetnacion == 4){
				if (tieneHijos){
					objeto_temp = crearSubproducto(task, (Producto) objeto, usuario);
					cargarItem(task,((Subproducto) objeto_temp).getId(), OBJETO_ID_SUBPRODUCTO);
				}
				else{ 
					objeto_temp = crearActividad(task, usuario,((Producto) objeto).getId(),OBJETO_ID_PRODUCTO );
					cargarItem(task,((Actividad) objeto_temp).getId(), OBJETO_ID_ACTIVIDAD);
				}
			}else if (indetnacion == 5){
					objeto_temp = crearActividad(task, usuario,((Subproducto) objeto).getId(),OBJETO_ID_SUBPRODUCTO );
					cargarItem(task,((Actividad) objeto_temp).getId(), OBJETO_ID_ACTIVIDAD);
			}else if (indetnacion > 5){
				objeto_temp = crearActividad(task, usuario,((Actividad) objeto).getId(),OBJETO_ID_ACTIVIDAD );
				cargarItem(task,((Actividad) objeto_temp).getId(), OBJETO_ID_ACTIVIDAD);
			}
			
		}
		/*if (task.getID()==0){
			indetnacion --;
		}*/
		
		for (Task child : task.getChildTasks())
		{
	      listaJerarquica(child,usuario,objeto_temp);
		}
		indetnacion --;
		return true;
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
	
	
	
	public String exportarProject(int idProyecto,String usuario) throws Exception
	{
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(idProyecto, usuario);
		String path="";
		
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
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), 3, 
							null,null, null, null, null, usuario);
					
					for (Actividad actividad : actividades){
						Task task4 = task3.addTask();
						task4.setName(actividad.getNombre());
						task4.setStart(actividad.getFechaInicio());
						task4.setFinish(actividad.getFechaFin()); 
					} 
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), 2, 
						null,null, null, null, null, usuario);
				
				for (Actividad actividad : actividades){
					Task task3 = task2.addTask();
					task3.setName(actividad.getNombre());
					task3.setStart(actividad.getFechaInicio());
					task3.setFinish(actividad.getFechaFin()); 
				}
			}
			
			ProjectWriter writer = new MPXWriter();
			path = "/archivos/temporales/Programa.mpx";
			writer.write(project,path);
		}
		return path;
	}	
	

}
