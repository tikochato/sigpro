package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Session;

import java.util.Map.Entry;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;

public class COrden {
	
	List<Object[]> estructuraPrestamo = null;
	
	public void calcularOrdenActividades(Integer nivel, Integer objetoId, Integer objetoTipo, String usuario, Session session){
		if(estructuraPrestamo == null){
			estructuraPrestamo = getEstructuraPrestamo(30, usuario);
		}
		
		Date fechaMax = new Date();
		try{
			String fMax = "01/01/2200";
			DateFormat format = new SimpleDateFormat("DD/MM/YYYY");
			fechaMax = format.parse(fMax);
		}
		catch (Exception ex) {
		}
		
		getTransactionSession(session);
		
		Object[] objPadre = getObjeto(objetoId, objetoTipo, estructuraPrestamo);
		String treePath = (String)getObjeto((Integer)objPadre[4], (Integer)objPadre[5], estructuraPrestamo)[6];
		List<Object[]> hijos = getHijos(nivel, objetoId, objetoTipo, estructuraPrestamo);
		
		//List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, objetoId, objetoTipo, null, null, null, null, null, usuario);
		
		if(hijos != null && hijos.size() > 1){
			Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
			//Encontrar orden
			Integer orden = 1;
			for(Object[] obj : hijos){
				Actividad actividad = (Actividad)obj[3];
				fechas.put(actividad.getId(), actividad.getFechaInicio() != null ? actividad.getFechaInicio().getTime() : fechaMax.getTime());
			}
			
			Set<Entry<Integer, Long>> set = fechas.entrySet();
	        List<Entry<Integer, Long>> list = new ArrayList<Entry<Integer, Long>>(set);
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>(){
	            public int compare( Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2 )
	            {
	                return (o1.getValue()).compareTo(o2.getValue());
	            }
	        });
	        for(Map.Entry<Integer, Long> entry:list){
	        	Object[] objeto = getObjeto(entry.getKey(), 5, estructuraPrestamo);
	        	Actividad actividad = (Actividad)objeto[3];
	        	actividad.setTreePath(treePath + "." + orden);
	 			String[] level = treePath.split("\\.");
	        	actividad.setNivel(level.length + 1);
	        	actividad.setOrden(orden);
	        	ActividadDAO.guardarActividadOrden(actividad, session);
	        	orden++;
	        }
	        
	        commitCalculoOrden(session);
	        calcularFechasPadre(objetoId, objetoTipo, usuario, 5, session, estructuraPrestamo);
	        commitCalculoOrden(session);
	        
	        //Obtener fecha inicial y final del padre de las actividades	        
	        if(objetoTipo == 5){//recursivo si son subactividades
	        	//Actividad actividad = ActividadDAO.getActividadPorId(objetoId, usuario);
	        	//calcularOrdenActividades(actividad.getObjetoId(),actividad.getObjetoTipo(),usuario, session);
	        	Object[] objActividad = getObjeto(objetoId, objetoTipo, estructuraPrestamo);
	        	calcularOrdenActividades(5, (Integer)objActividad[1], (Integer)objActividad[2], usuario, session);
	        	
	        }
	 	}else{
	 		if(hijos.size() == 1){
	 			Actividad actividad = (Actividad)hijos.get(0)[3];
	 			actividad.setOrden(1);
	 			actividad.setTreePath(treePath + "." + 1);
	 			String[] level = treePath.split("\\.");
	        	actividad.setNivel(level.length + 1);
				ActividadDAO.guardarActividadOrden(actividad, session);
				
				commitCalculoOrden(session);
		        calcularFechasPadre(objetoId, objetoTipo, usuario, 5, session, estructuraPrestamo);
		        commitCalculoOrden(session);
		        
		        calcularOrdenObjetosSuperiores(objetoTipo,objetoId, objetoTipo, usuario, session);
		        commitCalculoOrden(session);
			}

		}
	}	
	
	public void calcularOrdenObjetosSuperiores(Integer nivel, Integer objetoId, Integer objetoTipo, String usuario, Session session){
		if(estructuraPrestamo == null){
			estructuraPrestamo = getEstructuraPrestamo(30, usuario);
		}
				
		Date fechaMax = new Date();
		try{
			String fMax = "01/01/2200";
			DateFormat format = new SimpleDateFormat("DD/MM/YYYY");
			fechaMax = format.parse(fMax);
		}
		catch (Exception ex) {
		}
		
		getTransactionSession(session);
		Object[] objPadre = getObjeto(objetoId, objetoTipo, estructuraPrestamo);
		String treePath = (String)getObjeto((Integer)objPadre[1], (Integer)objPadre[2], estructuraPrestamo)[6];
		List<Object[]> hijos = getHijos(nivel, (Integer)objPadre[1], (Integer)objPadre[2], estructuraPrestamo);
		Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
		Integer orden = 1;
		
		if(objetoTipo == 2){
			if(hijos.size() > 1){		
				for(Object[] obj : hijos){
					Componente componente = (Componente)obj[3];
					fechas.put(componente.getId(), componente.getFechaInicio() != null ? componente.getFechaInicio().getTime() : fechaMax.getTime());
				}
				
				Set<Entry<Integer, Long>> set = fechas.entrySet();
		        List<Entry<Integer, Long>> list = new ArrayList<Entry<Integer, Long>>(set);
		        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>(){
		            public int compare( Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2 )
		            {
		                return (o1.getValue()).compareTo(o2.getValue());
		            }
		        });
		        
		        for(Map.Entry<Integer, Long> entry:list){
		        	Object[] objeto = getObjeto(entry.getKey(), 2, estructuraPrestamo);
		        	Componente componente = (Componente)objeto[3];
		        	componente.setOrden(orden);
		        	componente.setTreePath(treePath + "." + orden);
		        	ComponenteDAO.guardarComponenteOrden(componente, session);
		        	orden++;
		        }
		        
		        //commitCalculoOrden(session);
		        calcularFechasPadre((Integer)objPadre[1], 1, usuario, 2, session, estructuraPrestamo);
		        //commitCalculoOrden(session);
			}else{
				if(hijos.size() == 1){
					Componente componente = (Componente)hijos.get(0)[3];
					componente.setOrden(1);
					componente.setTreePath(treePath + "." + 1);
					ComponenteDAO.guardarComponenteOrden(componente, session);
					
					//commitCalculoOrden(session);
			        calcularFechasPadre(objetoId, 1, usuario, 2, session, estructuraPrestamo);
			        //commitCalculoOrden(session);
				}
			}
		} else if(objetoTipo == 3){
			if(hijos.size() > 1){
				for(Object[] obj : hijos){
					Producto producto = (Producto)obj[3];
					fechas.put(producto.getId(), producto.getFechaInicio() != null ? producto.getFechaInicio().getTime() : fechaMax.getTime());
				}

				Set<Entry<Integer, Long>> set = fechas.entrySet();
		        List<Entry<Integer, Long>> list = new ArrayList<Entry<Integer, Long>>(set);
		        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>(){
		            public int compare( Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2 )
		            {
		                return (o1.getValue()).compareTo(o2.getValue());
		            }
		        });
				
		        for(Map.Entry<Integer, Long> entry:list){
		        	Object[] objeto = getObjeto(entry.getKey(), 3, estructuraPrestamo);
		        	Producto producto = (Producto)objeto[3];
		        	producto.setTreePath(treePath + "." + orden);
		        	producto.setOrden(orden);
		        	ProductoDAO.guardarProductoOrden(producto, session);
		        	orden++;
		        }
		        
		        //commitCalculoOrden(session);
		        calcularFechasPadre((Integer)objPadre[1], 2, usuario, 3, session, estructuraPrestamo);
		        //commitCalculoOrden(session);
		        calcularOrdenObjetosSuperiores(2,(Integer)objPadre[1], (Integer)objPadre[2], usuario, session);
		        //commitCalculoOrden(session);
			}else{
				if(hijos.size() == 1){
					Producto producto = (Producto)hijos.get(0)[3];
					producto.setOrden(1);
					producto.setTreePath(treePath + "." + 1);
					ProductoDAO.guardarProductoOrden(producto, session);

			        //commitCalculoOrden(session);
			        calcularFechasPadre((Integer)objPadre[1], 2, usuario, 3, session, estructuraPrestamo);
			        //commitCalculoOrden(session);
			        calcularOrdenObjetosSuperiores(2,(Integer)objPadre[1], (Integer)objPadre[2], usuario, session);
			        //commitCalculoOrden(session);
				}
			}
			
		} else if(objetoTipo == 4){
			if(hijos.size() > 1){
				for(Object[] obj : hijos){
					Subproducto subproducto = (Subproducto)obj[3];
					fechas.put(subproducto.getId(), subproducto.getFechaInicio() != null ? subproducto.getFechaInicio().getTime() : fechaMax.getTime());
				}
				
				Set<Entry<Integer, Long>> set = fechas.entrySet();
		        List<Entry<Integer, Long>> list = new ArrayList<Entry<Integer, Long>>(set);
		        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>(){
		            public int compare( Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2 )
		            {
		                return (o1.getValue()).compareTo(o2.getValue());
		            }
		        });
		        
		        for(Map.Entry<Integer, Long> entry:list){
		        	Object[] objeto = getObjeto(entry.getKey(), 3, estructuraPrestamo);
		        	Subproducto subproducto = (Subproducto)objeto[3];
		        	subproducto.setOrden(orden);
		        	subproducto.setTreePath(treePath + "." + orden);
		        	SubproductoDAO.guardarSubproductoOrden(subproducto, session);
		        	orden++;
		        }
		        
		        //commitCalculoOrden(session);
		        calcularFechasPadre((Integer)objPadre[1], 3, usuario, 4, session, estructuraPrestamo);
		        //commitCalculoOrden(session);
		        calcularOrdenObjetosSuperiores(3,(Integer)objPadre[1], (Integer)objPadre[2], usuario, session);
		        //commitCalculoOrden(session);
			}else{
				if(hijos.size() == 1){
					Subproducto subproducto = (Subproducto)hijos.get(0)[3];
					subproducto.setOrden(1);
					subproducto.setTreePath(treePath + "." + 1);
					SubproductoDAO.guardarSubproductoOrden(subproducto, session);
					//commitCalculoOrden(session);
					
					calcularFechasPadre((Integer)objPadre[1], 3, usuario, 4, session, estructuraPrestamo);
			        //commitCalculoOrden(session);
			        calcularOrdenObjetosSuperiores(3,(Integer)objPadre[1], (Integer)objPadre[2], usuario, session);
			        //commitCalculoOrden(session);
				}
			}
		}
		
		 commitCalculoOrden(session);
	}

	private static void calcularFechasPadre(Integer objetoIdPadre, Integer objetoTipoPadre, String usuario, Integer objetoNivelActual, Session session, List<Object[]> estructuraPrestamo){
		Proyecto proyecto = null;
		Componente componente =  null;
		Producto producto = null;
		Subproducto subproducto = null;
		Actividad actividad = null;
		Date fechaInicial = new Date();
		Date fechaFinal = new Date();
		
		getTransactionSession(session);
		
		//Obtener fecha Inicial y fecha final
		if(objetoNivelActual == 2){//Padre del 2 es proyecto
			fechaInicial = getObjetoFechaInicial(2, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
			fechaFinal = getObjetoFechaFin(2, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
		} else if(objetoNivelActual == 3){//Padre del 3 es componente
			fechaInicial = getObjetoFechaInicial(3, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
			fechaFinal = getObjetoFechaFin(3, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
		} else if(objetoNivelActual == 4){//Padre del 4 es producto
			fechaInicial = getObjetoFechaInicial(4, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
			fechaFinal = getObjetoFechaFin(4, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
		} else if(objetoNivelActual == 5){//padre del 5 es proyecto, componente, producto, subproducto ó actividad
			fechaInicial = getObjetoFechaInicial(5, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
			fechaFinal = getObjetoFechaFin(5, objetoIdPadre, objetoTipoPadre, estructuraPrestamo);
		}
		
		if(objetoTipoPadre == 1){
    		proyecto = (Proyecto)getObjeto(objetoIdPadre, 1, estructuraPrestamo)[3];
    		proyecto.setFechaInicio(fechaInicial);
    		proyecto.setFechaFin(fechaFinal);
    		ProyectoDAO.guardarProyectoOrden(proyecto, session);
    	}else if(objetoTipoPadre == 2){
    		componente = (Componente)getObjeto(objetoIdPadre, 2, estructuraPrestamo)[3];
    		//componente = ComponenteDAO.getComponentePorId(objetoIdPadre, usuario);
    		componente.setFechaInicio(fechaInicial);
    		componente.setFechaFin(fechaFinal);
    		ComponenteDAO.guardarComponenteOrden(componente, session);
    	}else if(objetoTipoPadre == 3){
    		producto = (Producto)getObjeto(objetoIdPadre, 3, estructuraPrestamo)[3];
    		//producto = ProductoDAO.getProductoPorId(objetoIdPadre);
    		producto.setFechaInicio(fechaInicial);
    		producto.setFechaFin(fechaFinal);
    		ProductoDAO.guardarProducto(producto);
    	}else if(objetoTipoPadre == 4){
    		subproducto = (Subproducto)getObjeto(objetoIdPadre, 4, estructuraPrestamo)[3];
    		//subproducto = SubproductoDAO.getSubproductoPorId(objetoIdPadre);
    		subproducto.setFechaInicio(fechaInicial);
    		subproducto.setFechaFin(fechaFinal);
    		SubproductoDAO.guardarSubproducto(subproducto);
    	}else if(objetoTipoPadre == 5){
    		actividad = (Actividad)getObjeto(objetoIdPadre, 5, estructuraPrestamo)[3];
    		//actividad = ActividadDAO.getActividadPorId(objetoIdPadre, usuario);
    		actividad.setFechaInicio(fechaInicial);
    		actividad.setFechaFin(fechaFinal);
    		ActividadDAO.guardarActividad(actividad);
    	}
	}
	
	private static List<Object[]> getEstructuraPrestamo(Integer proyectoId, String usuario){
		Object[] objProyecto = null;
		List<Object[]> lstProyecto = new ArrayList<Object[]>();
		
		try{
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			objProyecto = new Object[7];
			objProyecto[0] = 1;//nivel
			objProyecto[1] = 0;//padreid
			objProyecto[2] = 0;//padreobjetoTipo
			objProyecto[3] = proyecto;
			objProyecto[4] = proyecto.getId();
			objProyecto[5] = 1;
			objProyecto[6] = proyecto.getTreePath() == null ? "1" : proyecto.getTreePath();
			lstProyecto.add(objProyecto);
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0,0, proyectoId, null, null, null, null, null, usuario);
			for(Componente componente : componentes){
				objProyecto = new Object[7];
				objProyecto[0] = 2;
				objProyecto[1] = proyectoId;
				objProyecto[2] = 1;
				objProyecto[3] = componente;
				objProyecto[4] = componente.getId();
				objProyecto[5] = 2;
				objProyecto[6] = componente.getTreePath();
				lstProyecto.add(objProyecto);
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(), null, null, null, null, null, usuario);
				for(Producto producto : productos){
					objProyecto = new Object[7];
					objProyecto[0] = 3;
					objProyecto[1] = producto.getComponente().getId();
					objProyecto[2] = 2;
					objProyecto[3] = producto;
					objProyecto[4] = producto.getId();
					objProyecto[5] = 3;
					objProyecto[6] = producto.getTreePath();
					lstProyecto.add(objProyecto);
					
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(), null, null, null, null, null, usuario);
					for(Subproducto subproducto : subproductos){
						objProyecto = new Object[7];
						objProyecto[0] = 4;
						objProyecto[1] = subproducto.getProducto().getId();
						objProyecto[2] = 3;
						objProyecto[3] = subproducto;
						objProyecto[4] = subproducto.getId();
						objProyecto[5] = 4;
						objProyecto[6] = subproducto.getTreePath();
						lstProyecto.add(objProyecto);
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), 4, null, null, null, null, null, usuario);
						for(Actividad actividad : actividades){
							lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, subproducto.getId(), 4);
							/*objProyecto = new Object[7];
							objProyecto[0] = 5;
							objProyecto[1] = subproducto.getId();
							objProyecto[2] = 4;
							objProyecto[3] = actividad;
							objProyecto[4] = actividad.getId();
							objProyecto[5] = 5;
							objProyecto[6] = actividad.getTreePath();
							lstProyecto.add(objProyecto);*/
						}
					}
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), 3, null, null, null, null, null, usuario);
					for(Actividad actividad : actividades){
						lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, producto.getId(), 3);
						/*objProyecto = new Object[7];
						objProyecto[0] = 5;
						objProyecto[1] = producto.getId();
						objProyecto[2] = 3;
						objProyecto[3] = actividad;
						objProyecto[4] = actividad.getId();
						objProyecto[5] = 5;
						objProyecto[6] = actividad.getTreePath();
						lstProyecto.add(objProyecto);*/
					}
				}
				
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), 2, null, null, null, null, null, usuario);
				for(Actividad actividad : actividades){
					lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, componente.getId(), 2);
					/*objProyecto = new Object[7];
					objProyecto[0] = 5;
					objProyecto[1] = componente.getId();
					objProyecto[2] = 2;
					objProyecto[3] = actividad;
					objProyecto[4] = actividad.getId();
					objProyecto[5] = 5;
					objProyecto[6] = actividad.getTreePath();
					lstProyecto.add(objProyecto);*/
				}
			}
			
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyecto.getId(), 1, null, null, null, null, null, usuario);
			for(Actividad actividad : actividades){
				lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, proyecto.getId(), 1);
				/*objProyecto = new Object[7];
				objProyecto[0] = 5;
				objProyecto[1] = proyecto.getId();
				objProyecto[2] = 1;
				objProyecto[3] = actividad;
				objProyecto[4] = actividad.getId();
				objProyecto[5] = 5;
				objProyecto[6] = actividad.getTreePath();
				lstProyecto.add(objProyecto);*/
			}
			
		}catch(Exception ex){
			return null;
		}
		return lstProyecto;
	}
	
	private static List<Object[]> obtenerActividades(Actividad actividad, List<Object[]> lstProyecto, String usuario, Integer objetoPadreId, Integer objectoPadreTipo){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), 5, null, null, null, null, null, usuario);
		Object[] objProyecto = new Object[7];
		objProyecto[0] = 5;
		objProyecto[1] = objetoPadreId;
		objProyecto[2] = objectoPadreTipo;
		objProyecto[3] = actividad;
		objProyecto[4] = actividad.getId();
		objProyecto[5] = 5;
		objProyecto[6] = actividad.getTreePath();
		lstProyecto.add(objProyecto);
		
		for(Actividad subActividad : actividades){
			lstProyecto = obtenerActividades(subActividad, lstProyecto, usuario, actividad.getId(), 5);
		}
		
		
		return lstProyecto;
	}
	
	private static List<Object[]> getHijos(Integer nivel, Integer objetoPadreId, Integer objetoPadreTipo, List<Object[]> estructuraPrestamo){
		List<Object[]> ret = new ArrayList<Object[]>();
		try{
			for(Object[] obj : estructuraPrestamo){
				if(((Integer)obj[0]).intValue() == nivel){
					if(((Integer)obj[1]).intValue() == objetoPadreId && ((Integer)obj[2]).intValue() == objetoPadreTipo){
						ret.add(obj);
					}
				}
			}
		}catch(Exception ex){
			return null;
		}
		
		return ret;
	}
	
	private static Object[] getObjeto(Integer objetoId, Integer objetoTipo, List<Object[]> estructuraPrestamo){
		Object[] ret = null;
		try{
			for(Object[] obj : estructuraPrestamo){
				if(((Integer) obj[4]).intValue() == objetoId && ((Integer)obj[5]).intValue() == objetoTipo)
					return obj;
			}
		}catch(Exception ex){
			return null;
		}
		
		return ret;
	}
	
	private static Date getObjetoFechaInicial(Integer nivel, Integer objetoPadreId, Integer objetoTipoPadre, List<Object[]> estructuraPrestamo){
		Date ret = null;
		Date fechaMax = new Date();

		try{
			String fMax = "01/01/2200";
			DateFormat format = new SimpleDateFormat("DD/MM/YYYY");
			fechaMax = format.parse(fMax);
			
			List<Object[]> hijos = getHijos(nivel, objetoPadreId, objetoTipoPadre, estructuraPrestamo);
			Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
			for(Object[] obj : hijos){
				if(nivel == 2){
					Componente componente = (Componente)obj[3];
					fechas.put(componente.getId(), componente.getFechaInicio() != null ? componente.getFechaInicio().getTime() : fechaMax.getTime());	
				}else if(nivel == 3){
					Producto producto = (Producto)obj[3];
					fechas.put(producto.getId(), producto.getFechaInicio() != null ? producto.getFechaInicio().getTime() : fechaMax.getTime());
				}else if(nivel == 4){
					Subproducto subproducto = (Subproducto)obj[3];
					fechas.put(subproducto.getId(), subproducto.getFechaInicio() != null ? subproducto.getFechaInicio().getTime() : fechaMax.getTime());
				}else if(nivel == 5){
					Actividad actividad = (Actividad)obj[3];
					fechas.put(actividad.getId(), actividad.getFechaInicio() != null ? actividad.getFechaInicio().getTime() : fechaMax.getTime());
				}
				
			}
			
			Set<Entry<Integer, Long>> set = fechas.entrySet();
	        List<Entry<Integer, Long>> list = new ArrayList<Entry<Integer, Long>>(set);
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>(){
	            public int compare( Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2 )
	            {
	                return (o1.getValue()).compareTo(o2.getValue());
	            }
	        });
	        
	        for(Map.Entry<Integer, Long> entry:list){
	        	if(nivel == 2){
	        		Componente componente = (Componente)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		return componente.getFechaInicio();	
	        	}else if(nivel == 3){
	        		Producto producto = (Producto)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		return producto.getFechaInicio();
	        	}else if(nivel == 4){
	        		Subproducto subproducto = (Subproducto)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		return subproducto.getFechaInicio();
	        	}else if(nivel == 5){
	        		Actividad actividad = (Actividad)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		return actividad.getFechaInicio();
	        	}
	        }
			
			
		}catch(Exception ex){
			
		}
		return ret;
	}
	
	private static Date getObjetoFechaFin(Integer nivel, Integer objetoPadreId, Integer objetoTipoPadre, List<Object[]> estructuraPrestamo){
		Date ret = null;
		//Date fechaMax = new Date();

		try{
			//String fMax = "01/01/2200";
			//DateFormat format = new SimpleDateFormat("DD/MM/YYYY");
			//fechaMax = format.parse(fMax);
			
			List<Object[]> hijos = getHijos(nivel, objetoPadreId, objetoTipoPadre, estructuraPrestamo);
			Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
			for(Object[] obj : hijos){
				if(nivel == 2){
					Componente componente = (Componente)obj[3];
					fechas.put(componente.getId(), componente.getFechaFin() != null ? componente.getFechaFin().getTime() : 0);	
				}else if(nivel == 3){
					Producto producto = (Producto)obj[3];
					fechas.put(producto.getId(), producto.getFechaFin() != null ? producto.getFechaFin().getTime() : 0);
				}else if(nivel == 4){
					Subproducto subproducto = (Subproducto)obj[3];
					fechas.put(subproducto.getId(), subproducto.getFechaFin() != null ? subproducto.getFechaFin().getTime() : 0);
				}else if(nivel == 5){
					Actividad actividad = (Actividad)obj[3];
					fechas.put(actividad.getId(), actividad.getFechaFin() != null ? actividad.getFechaFin().getTime() : 0);
				}
				
			}
			
			Set<Entry<Integer, Long>> set = fechas.entrySet();
	        List<Entry<Integer, Long>> list = new ArrayList<Entry<Integer, Long>>(set);
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>(){
	            public int compare( Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2 )
	            {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });
	        
	        for(Map.Entry<Integer, Long> entry:list){
	        	if(nivel == 2){
	        		Componente componente = (Componente)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		if(componente.getFechaFin() != null)
	        			return componente.getFechaFin();
	        	}else if(nivel == 3){
	        		Producto producto = (Producto)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		if(producto.getFechaFin() != null)
	        			return producto.getFechaFin();
	        	}else if(nivel == 4){
	        		Subproducto subproducto = (Subproducto)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		if(subproducto.getFechaFin() != null)
	        			return subproducto.getFechaFin();
	        	}else if(nivel == 5){
	        		Actividad actividad = (Actividad)getObjeto(entry.getKey(), nivel, estructuraPrestamo)[3];
	        		if(actividad.getFechaFin() != null)
	        			return actividad.getFechaFin();
	        	}
	        }
			
			
		}catch(Exception ex){
			
		}
		return ret;
	}
	
	public static Session getSessionCalculoOrden(){
		Session session = CHibernateSession.getSessionFactory().openSession();
		return session;
	}
	
	public static void getTransactionSession(Session session){
		if(!session.getTransaction().isActive())
			session.beginTransaction();
	}
	
	public static void commitCalculoOrden(Session session){
		if(session.getTransaction().isActive())
			session.getTransaction().commit();
	}
	
	public static void closeSessionCalculoOrden(Session session){
		session.close();
	}
}
