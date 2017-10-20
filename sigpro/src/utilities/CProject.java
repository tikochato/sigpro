package utilities;


import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;

import dao.ActividadDAO;
import dao.AcumulacionCostoDAO;
import dao.ComponenteDAO;
import dao.ComponenteTipoDAO;
import dao.CooperanteDAO;
import dao.ProductoDAO;
import dao.ProgramaDAO;
import dao.ProyectoDAO;
import dao.ProyectoTipoDAO;
import dao.SubproductoDAO;
import dao.UnidadEjecutoraDAO;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.writer.ProjectWriter;
import pojo.Actividad;
import pojo.ActividadTipo;
import pojo.AcumulacionCosto;
import pojo.Componente;
import pojo.ComponenteTipo;
import pojo.Cooperante;
import pojo.Etiqueta;
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
	static int COOPERANTE_ID_DEFECTO = 0;
	static int PROYECTO_TIPO_ID_DEFECTO = 1;
	static int ENTIDAD_ID_DEFECTO = 0;
	static int UNIDAD_EJECUTORA_ID_DEFECTO = 0;
	static int COMPONENTE_TIPO_ID_DEFECTO = 1;
	static int PRODUCTO_TIPO_ID_DEFECTO = 1;
	static int SUBPRODUCTO_TIPO__ID_DEFECTO = 1;
	static int ACTIVIDAD_TIPO_ID_DEFECTO = 1;
	static int PROYECTO_ETIQUETA_DEFECTO = 1;
	
	ProjectReader reader;
	ProjectFile project;
	String itemsProject;
	boolean multiproyecto;
	private Long proyectoId;
	
	
	


	int contComponente = 0;
	int contProducto = 0;
	int contSubproducto = 0;
	int contActividad = 0;
	
	
	
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

	public Long getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(Long proyectoId) {
		this.proyectoId = proyectoId;
	}

	public boolean imporatarArchivo(ProjectFile projectFile, String usuario,boolean multiproyecto,
			Integer proyectoId, boolean marcarCargado){
		itemsProject = "";
		items = new HashMap<>();
		this.multiproyecto = multiproyecto; 
		
		return getTask(projectFile,usuario, proyectoId, marcarCargado);
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
		Cooperante cooperante =CooperanteDAO.getCooperantePorCodigo(COOPERANTE_ID_DEFECTO);
		ProyectoTipo proyectoTipo = ProyectoTipoDAO.getProyectoTipoPorId(PROYECTO_TIPO_ID_DEFECTO);
		Etiqueta etiqueta = new Etiqueta();
		etiqueta.setId(PROYECTO_ETIQUETA_DEFECTO);
		AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
		
		UnidadEjecutora unidadEjecturoa = UnidadEjecutoraDAO.getUnidadEjecutora(new DateTime().getYear(),ENTIDAD_ID_DEFECTO,UNIDAD_EJECUTORA_ID_DEFECTO);
		Proyecto proyecto = new Proyecto(acumulacionCosto,null,cooperante, etiqueta,proyectoTipo, unidadEjecturoa
				, task.getName(), null, usuario, null, new Date(), null, 1, null, null, null, null, 
				null, null, null,null, null, null, new BigDecimal(task.getCost().toString()),null, null, null,null,
				Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish()),
				(( Double ) task.getDuration().getDuration()).intValue(), task.getDuration().getUnits().getName()
				,null,null,0,0,0, null,null,null,null,null,null,null,null,null);
		
		return ProyectoDAO.guardarProyecto(proyecto, false) ? proyecto : null;
	}
	
	public Componente crearComponente(Task task,Proyecto proyecto ,String usuario){
		
		
		ComponenteTipo componenteTipo = ComponenteTipoDAO.getComponenteTipoPorId(COMPONENTE_TIPO_ID_DEFECTO);
		
		int year = new DateTime().getYear();
		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(year, ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
		
		
		Componente componente = new Componente(acumulacionCosto,null,componenteTipo, proyecto, unidadEjecutora, task.getName()
				, null,usuario, null, new Date(), null, 1, null, null, null, null, null, null, null, null, 
				new BigDecimal(task.getCost().toString()),null,null,Utils.setDateCeroHoras(task.getStart()),
				Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),null,null,1,0,null,null,null,null,null,null,null);
		
		return ComponenteDAO.guardarComponente(componente, false) ? componente : null;
	}

	//TODO: agregar subcomponente
//	public Subcomponente crearSubComponente(Task task,Proyecto proyecto ,String usuario){
//		
//		
//		SubcomponenteTipo componenteTipo = SubComponenteTipoDAO.getSubComponenteTipoPorId(COMPONENTE_TIPO_ID_DEFECTO);
//		
//		int year = new DateTime().getYear();
//		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(year, ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
//		AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
//		
//		
//		Subcomponente subcomponente = new Subcomponente(acumulacionCosto,componenteTipo, proyecto, unidadEjecutora, task.getName()
//				, null,usuario, null, new Date(), null, 1, null, null, null, null, null, null, null, null, 
//				new BigDecimal(task.getCost().toString()),null,null,Utils.setDateCeroHoras(task.getStart()),
//				Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
//				, task.getDuration().getUnits().getName(),null,null,1,0,null,null,null,null);
//		
//		return SubComponenteDAO.guardarSubComponente(subcomponente, false) ? subcomponente : null;
//	}
	
	public Producto crearProducto (Task task, Componente componente,String usuario){
		
		ProductoTipo productoTipo = new ProductoTipo();
		productoTipo.setId(PRODUCTO_TIPO_ID_DEFECTO);
		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(new DateTime().getYear(), ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
		
		Producto producto = new Producto(acumulacionCosto,componente, productoTipo,null, unidadEjecutora
				,task.getName() , null, usuario, null, new Date(), null,1, 
				 null, null, null, null, null, null, null, 
				null, null, new BigDecimal(task.getCost().toString()),null,null,
				Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),
				null,null,2,null,null,null);
		
		return ProductoDAO.guardarProducto(producto, false) ? producto : null;
	}
	
	public Subproducto crearSubproducto(Task task, Producto producto, String usuario){
		
		SubproductoTipo subproductoTipo = new SubproductoTipo();
		subproductoTipo.setId(SUBPRODUCTO_TIPO__ID_DEFECTO);
		
		UnidadEjecutora unidadEjecutroa = UnidadEjecutoraDAO.getUnidadEjecutora(new DateTime().getYear(), ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
		
		Subproducto subproducto = new Subproducto(acumulacionCosto,producto, subproductoTipo, unidadEjecutroa,task.getName(), 
				null, usuario, null, new Date(), null, 1,null, null, null, null, null, null, null, null,
				new BigDecimal(task.getCost().toString()),null,null,
				Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),
				null,null,3,null,null);
		
		return SubproductoDAO.guardarSubproducto(subproducto, false) ? subproducto : null;
	}
	
	public Actividad crearActividad(Task task,String usuario, Integer objetoId, Integer objetoTipo,
			int nivel, String path,Integer proyectoBase, Integer componenteBase, Integer productoBase){
		
		ActividadTipo actividadTipo = new ActividadTipo();
		actividadTipo.setId(ACTIVIDAD_TIPO_ID_DEFECTO);
		
		Integer [] predecesores  = getListaPredecesores(task.getPredecessors());
		
		stitem itemPredecesor = null;
		if (predecesores!=null && predecesores.length > 0){
			 itemPredecesor =  items.get(predecesores[0]);	
		}
		
		AcumulacionCosto acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
		
		int duracion = (( Double ) task.getDuration().getDuration()).intValue();
		duracion = duracion>0 ? duracion : 1; 
		
		Actividad actividad = new Actividad(actividadTipo,acumulacionCosto, task.getName(), null, Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish())
				, task.getPercentageComplete().intValue(), usuario, null, new Date(), 
				null, 1, null, null, null,null, null, null, objetoId, objetoTipo, 
				duracion
				, task.getDuration().getUnits().getName()
				,itemPredecesor!=null ? itemPredecesor.objetoId : null
				, itemPredecesor != null ? itemPredecesor.objetoTipo : null
				, null, null, new BigDecimal(task.getCost().toString()),null,null,null,null,
				nivel,proyectoBase,componenteBase,productoBase,
				null,null
				);
		
		return ActividadDAO.guardarActividad(actividad, false) ? actividad : null;
	}
	
	public void cargarItem(Task task,int objetoId, int objetoTipo, int indentacion){
		stitem item_ = new stitem();
		item_.id = task.getUniqueID();
		item_.contenido = task.getName();
		item_.indentacion = indentacion;
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
	

	public  boolean getTask(ProjectFile projectFile,String usuario,Integer proyectoId, boolean marcarCargado){
		
		
		itemsProject = "";
		items = new HashMap<>();
		
		
/*		if (!multiproyecto){
			List<Actividad> actividades = ActividadDAO.obtenerActividadesHijas(id);
			for (Actividad actividad : actividades)
				ActividadDAO.guardarActividad(actividad, true);
		} */
		
		Proyecto proyecto = null;
		List<Componente> componentes = null;
		if (marcarCargado){
			proyecto = ProyectoDAO.getProyecto(proyectoId);
			componentes = ComponenteDAO.getComponentesPorProyecto(proyectoId);
			
		}
		
		Integer ret = listaJerarquica(projectFile.getChildTasks().get(0),usuario,null,1,multiproyecto ? 0 : 1,
				proyecto,  componentes,marcarCargado,0);
		ProyectoDAO.calcularCostoyFechas(ret);
		return ret > 0;
		
	}
	
	
	
	private Integer listaJerarquica(Task task,String usuario,Object objeto,int objetoTipo, int nivel,
			Proyecto proyecto_, List<Componente> componentes, boolean marcarCargado, int posicionComponente){
		Integer ret = 1;
		Integer ret_hijos=1;
		try{
			boolean tieneHijos = task.getChildTasks()!=null && task.getChildTasks().size()>0;
			Object objeto_temp=null;
			Method getId = (objeto!=null) ? objeto.getClass().getMethod("getId") : null;
			Method getTreePath =  (objeto!=null) ? objeto.getClass().getMethod("getTreePath") : null;
			Integer objetoTipoTemp=0;
			switch(nivel){
				case 0:
					objeto_temp = crearPrograma(task, usuario);
					ret = ((Programa) objeto_temp).getId();
					break;
				case 1:
					Proyecto proyecto;
					if (marcarCargado){
						proyecto =  proyecto_ ;
						proyecto.setProjectCargado(1);
						ProyectoDAO.guardarProyecto(proyecto, false);
					}else{
						proyecto =  crearProyecto(task, usuario);
					}
					
					if (objeto != null){
						crearProgramaProyecto(proyecto,(Programa)objeto, usuario);
						objetoTipoTemp=1;
					}
					ret = proyecto.getId();
					objeto_temp=(Object)proyecto;
					break;
				case 2:
					if(tieneHijos){
						
						objetoTipoTemp=2;
						Componente componente = null;
						if (marcarCargado){
							 componente =  componentes.size() > posicionComponente 
									? componentes.get(posicionComponente) : null;
						}else{
							componente =  crearComponente(task, (Proyecto) objeto, usuario);
						}
						
						ret = componente.getId();
						objeto_temp = (Object)componente;
					}
					else{
						if (objeto != null){
							objetoTipoTemp=5;
							Proyecto objeto_padre = ((Proyecto) objeto);
							Actividad actividad = crearActividad(task, usuario,objeto_padre.getId(),1 
								,2,objeto_padre.getTreePath(),objeto_padre.getId(),null,null);
							cargarItem(task,actividad.getId(), 5,2);
							ret = actividad.getId();
							objeto_temp = (Object)actividad;
						}
					}
					break;
				case 3:
					if (objeto != null){
						if(tieneHijos){
							objetoTipoTemp=3;
							Producto producto =  crearProducto(task, (Componente) objeto, usuario);
							ret = producto.getId();
							objeto_temp = (Object)producto;
						}
						else{
							objetoTipoTemp=5;
							Actividad actividad = crearActividad(task, usuario, (Integer)getId.invoke(objeto),objetoTipo 
								,3, (String)getTreePath.invoke(objeto), null,null,null);
							cargarItem(task,actividad.getId(), 5,3);
							ret = actividad.getId();
							objeto_temp = (Object)actividad;
						}
					}
					break;
				case 4:
					if (objeto != null){
						if(tieneHijos){
							objetoTipoTemp=4;
							Subproducto subproducto =  crearSubproducto(task, (Producto) objeto, usuario);
							ret = subproducto.getId();
							objeto_temp = (Object)subproducto;
						}
						else{
							objetoTipoTemp=5;
							Actividad actividad = crearActividad(task, usuario, (Integer)getId.invoke(objeto),objetoTipo 
								,4, (String)getTreePath.invoke(objeto), null,null,null);
							cargarItem(task,actividad.getId(), 5,4);
							ret = actividad.getId();
							objeto_temp = (Object)actividad;
						}
					}
					break;
				default:
					if (objeto != null){
						objetoTipoTemp=5;
						Actividad actividad = crearActividad(task, usuario, (Integer)getId.invoke(objeto),objetoTipo 
							,nivel, (String)getTreePath.invoke(objeto), null,null,null);
						cargarItem(task,actividad.getId(), 5,nivel);
						ret = actividad.getId();
						objeto_temp = (Object)actividad;
					}
					break;
			}
			for (Task child : task.getChildTasks())
				ret_hijos=ret_hijos>0 ? listaJerarquica(child,usuario,objeto_temp,objetoTipoTemp,nivel+1,
						proyecto_,componentes, marcarCargado, posicionComponente++) : -1;
			
		}
		catch(Exception e){
			CLogger.write("1", CProject.class, e);
			ret=-1;
		}
		return ret>0 && ret_hijos>0 ? ret : -1;
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
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(), null,
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
							task5.setCost(actividad.getCosto());
							task5.setPercentageComplete(actividad.getPorcentajeAvance());
						}	
					}
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), 3, 
							null,null, null, null, null, usuario);
					
					for (Actividad actividad : actividades){
						Task task4 = task3.addTask();
						task4.setName(actividad.getNombre());
						task4.setStart(actividad.getFechaInicio());
						task4.setFinish(actividad.getFechaFin()); 
						task4.setCost(actividad.getCosto());
						task4.setPercentageComplete(actividad.getPorcentajeAvance());
					} 
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), 2, 
						null,null, null, null, null, usuario);
				
				for (Actividad actividad : actividades){
					Task task3 = task2.addTask();
					task3.setName(actividad.getNombre());
					task3.setStart(actividad.getFechaInicio());
					task3.setFinish(actividad.getFechaFin()); 
					task3.setCost(actividad.getCosto());
					task3.setPercentageComplete(actividad.getPorcentajeAvance());
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(proyecto.getId(), 1);
			for (Actividad actividad : actividades){
				Task t = task1.addTask();
				t.setName(actividad.getNombre());
				t.setStart(actividad.getFechaInicio());
				t.setFinish(actividad.getFechaFin()); 
				t.setCost(actividad.getCosto());
				t.setPercentageComplete(actividad.getPorcentajeAvance());
			}
			
			ProjectWriter writer = new MPXWriter();
			path = "/archivos/temporales/Programa.mpx";
			writer.write(project,path);
		}
		return path;
	}	
}
