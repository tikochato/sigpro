package utilities;


import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;

import dao.ActividadDAO;
import dao.AcumulacionCostoDAO;
import dao.AsignacionRaciDAO;
import dao.ComponenteDAO;
import dao.ComponenteTipoDAO;
import dao.EstructuraProyectoDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProgramaDAO;
import dao.ProyectoDAO;
import dao.ProyectoTipoDAO;
import dao.SubComponenteDAO;
import dao.SubComponenteTipoDAO;
import dao.SubproductoDAO;
import dao.UnidadEjecutoraDAO;
import net.sf.mpxj.AccrueType;
import net.sf.mpxj.CustomFieldContainer;
import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.reader.ProjectReader;
import pojo.Actividad;
import pojo.ActividadTipo;
import pojo.AcumulacionCosto;
import pojo.AsignacionRaci;
import pojo.Componente;
import pojo.ComponenteTipo;
import pojo.Etiqueta;
import pojo.Prestamo;
import pojo.Producto;
import pojo.ProductoTipo;
import pojo.Programa;
import pojo.ProgramaProyecto;
import pojo.ProgramaProyectoId;
import pojo.ProgramaTipo;
import pojo.Proyecto;
import pojo.ProyectoTipo;
import pojo.Subcomponente;
import pojo.SubcomponenteTipo;
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
	static int SUBCOMPONENTE_TIPO_ID_DEFECTO = 1;
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
			Integer proyectoId, boolean marcarCargado,Integer prestamoId){
		itemsProject = "";
		items = new HashMap<>();
		this.multiproyecto = multiproyecto; 
		
		return getTask(projectFile,usuario, proyectoId, marcarCargado,prestamoId);
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
	
	public Proyecto crearProyecto(Task task,String usuario,Integer prestamoId){
		ProyectoTipo proyectoTipo = ProyectoTipoDAO.getProyectoTipoPorId(PROYECTO_TIPO_ID_DEFECTO);
		Etiqueta etiqueta = new Etiqueta();
		etiqueta.setId(PROYECTO_ETIQUETA_DEFECTO);
		
		AcumulacionCosto acumulacionCosto = null;
		
		switch(task.getFixedCostAccrual().getValue()){
			case 1:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(1);
				break;
			case 2:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(2);
				break;
			case 3:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				break;
		}
		
		Prestamo prestamo = PrestamoDAO.getPrestamoById(prestamoId);
		
		UnidadEjecutora unidadEjecturoa = UnidadEjecutoraDAO.getUnidadEjecutora(new DateTime().getYear(),ENTIDAD_ID_DEFECTO,UNIDAD_EJECUTORA_ID_DEFECTO);
		Proyecto proyecto = new Proyecto(acumulacionCosto,null, etiqueta,prestamo,proyectoTipo, unidadEjecturoa
				, task.getName(), null, usuario, null, new Date(), null, 1, null, null, null, null, 
				null, null, null,null, null, null, new BigDecimal(task.getCost().toString()),null, null, null,null,
				Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish()),
				(( Double ) task.getDuration().getDuration()).intValue(), task.getDuration().getUnits().getName()
				,null,null,0,0,0,null, null,null,null,task.getActualStart(),task.getActualFinish(),null,null,null,null,null,null,null,null,null,null,null,null);
		
		return ProyectoDAO.guardarProyecto(proyecto, false) ? proyecto : null;
	}
	
	public Componente crearComponente(Task task,Proyecto proyecto ,String usuario){
		
		
		ComponenteTipo componenteTipo = ComponenteTipoDAO.getComponenteTipoPorId(COMPONENTE_TIPO_ID_DEFECTO);
		
		int year = new DateTime().getYear();
		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(year, ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		
		AcumulacionCosto acumulacionCosto = null;

		switch(task.getFixedCostAccrual().getValue()){
			case 1:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(1);
				break;
			case 2:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(2);
				break;
			case 3:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				break;
		}
		
		
		Componente componente = new Componente(acumulacionCosto,null,componenteTipo, proyecto, unidadEjecutora, task.getName()
				, null,usuario, null, new Date(), null, 1, null, null, null, null, null, null, null, null, 
				new BigDecimal(task.getCost().toString()),null,null,Utils.setDateCeroHoras(task.getStart()),
				Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),null,null,1,0,null,null,null,task.getActualStart(),task.getActualFinish(),null,null ,null,null);
		
		return ComponenteDAO.guardarComponente(componente, false) ? componente : null;
	}


	public Subcomponente crearSubComponente(Task task,Componente componente ,String usuario){
		
		
		SubcomponenteTipo componenteTipo = SubComponenteTipoDAO.getSubComponenteTipoPorId(SUBCOMPONENTE_TIPO_ID_DEFECTO);
		
		int year = new DateTime().getYear();
		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(year, ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		
		AcumulacionCosto acumulacionCosto = null;
		
		switch(task.getFixedCostAccrual().getValue()){
			case 1:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(1);
				break;
			case 2:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(2);
				break;
			case 3:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				break;
		}
		
		Subcomponente subcomponente = new Subcomponente(acumulacionCosto,componente,componenteTipo,  unidadEjecutora, task.getName()
				, null,usuario, null, new Date(), null, 1, null, null, null, null, null, null, null, null, 
				new BigDecimal(task.getCost().toString()),null,null,Utils.setDateCeroHoras(task.getStart()),
				Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),null,null,2,task.getActualStart(),task.getActualFinish(),null ,null,null);
		
		return SubComponenteDAO.guardarSubComponente(subcomponente, false) ? subcomponente : null;
	}
	
	public Producto crearProducto (Task task, Componente componente,Subcomponente subcomponente,String usuario){
		
		ProductoTipo productoTipo = new ProductoTipo();
		productoTipo.setId(PRODUCTO_TIPO_ID_DEFECTO);
		UnidadEjecutora unidadEjecutora = UnidadEjecutoraDAO.getUnidadEjecutora(new DateTime().getYear(), ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		
		AcumulacionCosto acumulacionCosto = null;
		
		switch(task.getFixedCostAccrual().getValue()){
			case 1:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(1);
				break;
			case 2:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(2);
				break;
			case 3:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				break;
		}
		
		Producto producto = new Producto(acumulacionCosto,null, productoTipo,subcomponente, unidadEjecutora
				,task.getName() , null, usuario, null, new Date(), null,1, 
				 null, null, null, null, null, null, null, 
				null, null, new BigDecimal(task.getCost().toString()),null,null,
				Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),
				null,null,3,task.getActualStart(),task.getActualFinish(),null ,null,null);
		
		return ProductoDAO.guardarProducto(producto, false) ? producto : null;
	}
	
	public Subproducto crearSubproducto(Task task, Producto producto, String usuario){
		
		SubproductoTipo subproductoTipo = new SubproductoTipo();
		subproductoTipo.setId(SUBPRODUCTO_TIPO__ID_DEFECTO);
		
		UnidadEjecutora unidadEjecutroa = UnidadEjecutoraDAO.getUnidadEjecutora(new DateTime().getYear(), ENTIDAD_ID_DEFECTO, UNIDAD_EJECUTORA_ID_DEFECTO);
		
		AcumulacionCosto acumulacionCosto = null;
		
		switch(task.getFixedCostAccrual().getValue()){
			case 1:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(1);
				break;
			case 2:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(2);
				break;
			case 3:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				break;
		}
		
		Subproducto subproducto = new Subproducto(acumulacionCosto,producto, subproductoTipo, unidadEjecutroa,task.getName(), 
				null, usuario, null, new Date(), null, 1,null, null, null, null, null, null, null, null,
				new BigDecimal(task.getCost().toString()),null,null,
				Utils.setDateCeroHoras(task.getStart()),Utils.setDateCeroHoras(task.getFinish()),(( Double ) task.getDuration().getDuration()).intValue()
				, task.getDuration().getUnits().getName(),
				null,null,4,task.getActualStart(),task.getActualFinish(),null,null);
		
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
		
		AcumulacionCosto acumulacionCosto = null;
		
		switch(task.getFixedCostAccrual().getValue()){
			case 1:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(1);
				break;
			case 2:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(2);
				break;
			case 3:
				acumulacionCosto = AcumulacionCostoDAO.getAcumulacionCostoById(3);
				break;
		}
		
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
				task.getActualStart(),task.getActualFinish(),null,null
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
	

	public  boolean getTask(ProjectFile projectFile,String usuario,Integer proyectoId, boolean marcarCargado,Integer prestamoId){
		
		itemsProject = "";
		items = new HashMap<>();
		
		Proyecto proyecto = null;
		List<Componente> componentes = null;
		if (marcarCargado){
			proyecto = ProyectoDAO.getProyecto(proyectoId);
			componentes = ComponenteDAO.getComponentesPorProyecto(proyectoId);
			
		}
		Integer ret = listaJerarquica(projectFile.getChildTasks().get(0),usuario,null,0,multiproyecto ? -1 : 0,
				proyecto,  componentes,marcarCargado,0,prestamoId);
		ProyectoDAO.calcularCostoyFechas(ret);
		return ret > 0;
		
	}
	
	
	
	private Integer listaJerarquica(Task task,String usuario,Object objeto,int objetoTipo, int nivel,
			Proyecto proyecto_, List<Componente> componentes, boolean marcarCargado, int posicionComponente,
			Integer prestamoId){
		Integer ret = 1;
		Integer ret_hijos=1;
		try{
			boolean tieneHijos = task.getChildTasks()!=null && task.getChildTasks().size()>0;
			Object objeto_temp=null;
			Method getId = (objeto!=null) ? objeto.getClass().getMethod("getId") : null;
			Method getTreePath =  (objeto!=null) ? objeto.getClass().getMethod("getTreePath") : null;
			Integer objetoTipoTemp=0;
			switch(nivel){
				case -1:
					objeto_temp = crearPrograma(task, usuario);
					ret = ((Programa) objeto_temp).getId();
					break;
				case 0:
					Proyecto proyecto;
					if (marcarCargado){
						proyecto =  proyecto_ ;
						proyecto.setProjectCargado(1);
						ProyectoDAO.guardarProyecto(proyecto, false);
					}else{
						proyecto =  crearProyecto(task, usuario,prestamoId);
					}
					
					if (objeto != null){
						crearProgramaProyecto(proyecto,(Programa)objeto, usuario);
						objetoTipoTemp=0;
					}
					ret = proyecto.getId();
					objeto_temp=(Object)proyecto;
					break;
				case 1:			
					if(tieneHijos){
						objetoTipoTemp=1;
						Componente componente = null;
						if (marcarCargado){
							 componente =  componentes.size() > posicionComponente 
									? componentes.get(posicionComponente) : null;
						}else{
							componente =  crearComponente(task, (Proyecto) objeto, usuario);
						}
						
						ret = componente!= null ? componente.getId() : 1;
						objeto_temp = (Object)componente;
					}
					else{
						if (objeto != null){
							objetoTipoTemp=5;
							Proyecto objeto_padre = ((Proyecto) objeto);
							Actividad actividad = crearActividad(task, usuario,objeto_padre.getId(),0 
								,2,objeto_padre.getTreePath(),objeto_padre.getId(),null,null);
							cargarItem(task,actividad.getId(), 5,2);
							ret = actividad.getId();
							objeto_temp = (Object)actividad;
						}
					}
					break;
				case 2:
					if (objeto != null){
						if(tieneHijos){
							
							objetoTipoTemp=2;
							Subcomponente subcomponente = null;
							subcomponente =  crearSubComponente(task,(Componente) objeto, usuario);
							
							
							ret = subcomponente.getId();
							objeto_temp = (Object)subcomponente;
						}
						else{
							if (objeto != null){
								objetoTipoTemp=5;
								Componente objeto_padre = ((Componente) objeto);
								Actividad actividad = crearActividad(task, usuario,objeto_padre.getId(),1 
									,2,objeto_padre.getTreePath(),objeto_padre.getId(),null,null);
								cargarItem(task,actividad.getId(), 5,2);
								ret = actividad.getId();
								objeto_temp = (Object)actividad;
							}
						}
					}
					break;
				case 3:
					if (objeto != null){
						if(tieneHijos){
							objetoTipoTemp=3;
							Producto producto =  crearProducto(task, null,(Subcomponente) objeto, usuario);
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
						proyecto_,componentes, marcarCargado, posicionComponente++,prestamoId) : -1;
			
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
	
	
	
	public String exportarProject(int idProyecto, String lineaBase, String usuario) throws Exception
	{
		String path="";
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idProyecto, lineaBase);
		if(estructuraProyecto != null){
			project = new ProjectFile();
			
			CustomFieldContainer cfc = project.getCustomFields();
			cfc.registerAliasValue("responsable", 500, 500);
			
			Task task0 = null;
			Task task1 = null;
			Task task2 = null;
			Task task3 = null;
			Task task4 = null;
			Task task5 = null;
			
			
			for(Object objeto : estructuraProyecto){
				Object[] obj = (Object[]) objeto;
				BigInteger objPred = (BigInteger) obj[19];
				BigInteger avance = (BigInteger) obj[18];

				switch(((BigInteger) obj[2]).intValue()){
					case 0:
						task0 = project.addTask();
						task0.setName((String)obj[1]);
						task0.setCost((BigDecimal)obj[8]);
						task0.setActualCost((BigDecimal)obj[8]);
						task0.setFixedCost((BigDecimal)obj[8]);
						asignarAcumulacionCosto(task0, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
						break;
					case 1:
						task1 = task0.addTask();
						task1.setName((String)obj[1]);
						task1.setCost((BigDecimal)obj[8]);
						task1.setActualCost((BigDecimal)obj[8]);
						task1.setFixedCost((BigDecimal)obj[8]);
						asignarAcumulacionCosto(task1, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
						break;
					case 2:
						task2 = task1.addTask();
						task2.setName((String)obj[1]);
						task2.setCost((BigDecimal)obj[8]);
						task2.setActualCost((BigDecimal)obj[8]);
						task2.setFixedCost((BigDecimal)obj[8]);
						asignarAcumulacionCosto(task2, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
						break;
					case 3:
						task3 = task2.addTask();
						task3.setName((String)obj[1]);
						task3.setCost((BigDecimal)obj[8]);
						task3.setActualCost((BigDecimal)obj[8]);
						task3.setFixedCost((BigDecimal)obj[8]);
						asignarAcumulacionCosto(task3, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
						break;
					case 4:
						task4 = task3.addTask();
						task4.setName((String)obj[1]);
						task4.setCost((BigDecimal)obj[8]);
						task4.setActualCost((BigDecimal)obj[8]);
						task4.setFixedCost((BigDecimal)obj[8]);
						asignarAcumulacionCosto(task4,obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
						break;
					case 5:
						if(objPred.intValue() == 0){
							task5 = task0.addTask();
							task5.setName((String)obj[1]);
							task5.setStart((Date)obj[4]);
							task5.setFinish((Date)obj[5]); 
							task5.setCost((BigDecimal)obj[8]);
							task5.setActualCost((BigDecimal)obj[8]);
							task5.setFixedCost((BigDecimal)obj[8]);
							task5.setPercentageComplete(avance.intValue());
							task5.setDuration(Duration.getInstance((Integer)obj[6], TimeUnit.DAYS));
							task5.setActualStart((Date)obj[16]);
							task5.setActualFinish((Date)obj[17]);
							task5.setMilestone(false);
							asignarAcumulacionCosto(task5,obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
							asignarColumnasAdicionales(task5,  ((BigInteger) obj[2]).intValue(), (Integer) obj[0]);
							
						} else if(objPred.intValue() == 1){
							task5 = task1.addTask();
							task5.setName((String)obj[1]);
							task5.setStart((Date)obj[4]);
							task5.setFinish((Date)obj[5]); 
							task5.setCost((BigDecimal)obj[8]);
							task5.setActualCost((BigDecimal)obj[8]);
							task5.setFixedCost((BigDecimal)obj[8]);
							task5.setPercentageComplete(avance.intValue());
							task5.setDuration(Duration.getInstance((Integer)obj[6], TimeUnit.DAYS));
							task5.setActualStart((Date)obj[16]);
							task5.setActualFinish((Date)obj[17]);
							asignarAcumulacionCosto(task5,obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
							asignarColumnasAdicionales(task5,  ((BigInteger) obj[2]).intValue(), (Integer) obj[0]);
							
						} else if(objPred.intValue() == 2){
							task5 = task2.addTask();
							task5.setName((String)obj[1]);
							task5.setStart((Date)obj[4]);
							task5.setFinish((Date)obj[5]); 
							task5.setCost((BigDecimal)obj[8]);
							task5.setActualCost((BigDecimal)obj[8]);
							task5.setFixedCost((BigDecimal)obj[8]);
							task5.setPercentageComplete(avance.intValue());
							task5.setDuration(Duration.getInstance((Integer)obj[6], TimeUnit.DAYS));
							task5.setActualStart((Date)obj[16]);
							task5.setActualFinish((Date)obj[17]);
							asignarAcumulacionCosto(task5, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
							
						} else if(objPred.intValue() == 3){
							task5 = task3.addTask();
							task5.setName((String)obj[1]);
							task5.setStart((Date)obj[4]);
							task5.setFinish((Date)obj[5]); 
							task5.setCost((BigDecimal)obj[8]);
							task5.setActualCost((BigDecimal)obj[8]);
							task5.setFixedCost((BigDecimal)obj[8]);
							task5.setPercentageComplete(avance.intValue());
							task5.setDuration(Duration.getInstance((Integer)obj[6], TimeUnit.DAYS));
							task5.setActualStart((Date)obj[16]);
							task5.setActualFinish((Date)obj[17]);
							asignarAcumulacionCosto(task5, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
							asignarColumnasAdicionales(task5,  ((BigInteger) obj[2]).intValue(), (Integer) obj[0]);
							
						} else if(objPred.intValue() == 4){
							task5 = task4.addTask();
							task5.setName((String)obj[1]);
							task5.setStart((Date)obj[4]);
							task5.setFinish((Date)obj[5]); 
							task5.setCost((BigDecimal)obj[8]);
							task5.setActualCost((BigDecimal)obj[8]);
							task5.setFixedCost((BigDecimal)obj[8]);
							task5.setPercentageComplete(avance.intValue());
							task5.setDuration(Duration.getInstance((Integer)obj[6], TimeUnit.DAYS));
							task5.setActualStart((Date)obj[16]);
							task5.setActualFinish((Date)obj[17]);
							asignarAcumulacionCosto(task5, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
							asignarColumnasAdicionales(task5,  ((BigInteger) obj[2]).intValue(), (Integer) obj[0]);
						} else if(objPred.intValue() == 5){
							
							Task task6 = task5.addTask();
							task6.setName((String)obj[1]);
							task6.setStart((Date)obj[4]);
							task6.setFinish((Date)obj[5]); 
							task6.setCost((BigDecimal)obj[8]);
							task6.setActualCost((BigDecimal)obj[8]);
							task6.setFixedCost((BigDecimal)obj[8]);
							task6.setPercentageComplete(avance.intValue());
							task6.setDuration(Duration.getInstance((Integer)obj[6], TimeUnit.DAYS));
							task6.setActualStart((Date)obj[16]);
							task6.setActualFinish((Date)obj[17]);
							asignarAcumulacionCosto(task6, obj[9] != null ? ((BigInteger) obj[9]).intValue() : 0);
							asignarColumnasAdicionales(task6,  ((BigInteger) obj[2]).intValue(), (Integer) obj[0]);
						}
						break;
				}
			}	
		}
					
		MSPDIWriter writer = new MSPDIWriter();
		path = "/SIPRO/archivos/temporales/Programa.xml";
		writer.write(project,path);
		
		return path;
	}	
	
	public void asignarColumnasAdicionales(Task task ,Integer objetoTipo, Integer objetoId){
		switch (objetoTipo){
			case 5:
				String nombre ="";
				AsignacionRaci recurso = AsignacionRaciDAO.getAsignacionPorRolTarea(objetoId, objetoTipo, "r", null);
				if (recurso!=null){
					nombre = recurso.getColaborador().getPnombre() 
							+ (recurso.getColaborador().getSnombre() != null ? " " + recurso.getColaborador().getSnombre()  : "")
							+ recurso.getColaborador().getPapellido() + 
							(recurso.getColaborador().getSapellido()!=null ? " " + recurso.getColaborador().getSapellido() : "");
					task.setResourceNames(nombre);
				}
				
				if (nombre.length()>0)
					task.setText(1, nombre);
			break;
		}

	}
	
	public void asignarAcumulacionCosto(Task task ,int tipoAcumulacion){
		if(tipoAcumulacion== 1)
			task.setFixedCostAccrual(AccrueType.START);
		else if (tipoAcumulacion == 2)
			task.setFixedCostAccrual(AccrueType.PRORATED);
		else
			task.setFixedCostAccrual(AccrueType.END);
		
	}
		
}
