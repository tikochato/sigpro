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
	
	public void calcularOrdenActividades(Integer objetoId, Integer objetoTipo, String usuario){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, objetoId, objetoTipo, null, null, 
				null, null, null, usuario);
		
		if(actividades != null && actividades.size() > 1){
			Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
			//Encontrar orden
			Integer orden = 1;
			for(Actividad actividad : actividades){
				fechas.put(actividad.getId(), actividad.getFechaInicio() != null ? actividad.getFechaInicio().getTime() : new Date().getTime());
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
	        	Actividad actividad = ActividadDAO.getActividadPorId(entry.getKey(), usuario);
			    actividad.setOrden(orden);
			    ActividadDAO.guardarActividad(actividad);
			    orden++;
	        }
	        //Obtener fecha inicial y final del padre de las actividades
	        calcularFechasPadre(objetoId, objetoTipo, usuario, 5);
	        
	        if(objetoTipo == 5){//recursivo si son subactividades
	        	Actividad actividad = ActividadDAO.getActividadPorId(objetoId, usuario);
	        	calcularOrdenActividades(actividad.getObjetoId(),actividad.getObjetoTipo(),usuario);
	        }
	 	}else{
			Actividad actividad = actividades.get(0);
			actividad.setOrden(1);
			ActividadDAO.guardarActividad(actividad);
			calcularFechasPadre(objetoId, objetoTipo, usuario, 5);
		}
	}	
	
	public void calcularOrdenObjetosSuperiores(Integer objetoId, Integer objetoTipo, String usuario){
		Date fechaMax = new Date();
		try{
			String fMax = "01/01/2200";
			DateFormat format = new SimpleDateFormat("DD/MM/YYYY");
			fechaMax = format.parse(fMax);
		}
		catch (Exception ex) {
		}
		
		if(objetoTipo == 1){
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, objetoId, null, null, 
					null, null, null, usuario);
			
			if(componentes != null & componentes.size() > 1){
				Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
				Integer orden = 1;
				
				for(Componente componente : componentes){
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
	        		Componente componente = ComponenteDAO.getComponentePorId(entry.getKey(), usuario);
	        		componente.setOrden(orden);
				    ComponenteDAO.guardarComponente(componente);
				    orden++;
		        }
		        
		        calcularFechasPadre(objetoId, 1, usuario, 2);
			}else{
				Componente componente = componentes.get(0);
				componente.setOrden(1);
				ComponenteDAO.guardarComponente(componente);
			}
		} else if(objetoTipo == 2){
			Componente c = ComponenteDAO.getComponentePorId(objetoId, usuario);
			Proyecto proyecto = ProyectoDAO.getProyecto(c.getProyecto().getId());
			
			if(proyecto != null){
				List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyecto.getId(), null, null, 
						null, null, null, usuario);
				
				if(componentes != null & componentes.size() > 1){
					Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
					Integer orden = 1;
					
					for(Componente componente : componentes){
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
		        		Componente componente = ComponenteDAO.getComponentePorId(entry.getKey(), usuario);
		        		componente.setOrden(orden);
					    ComponenteDAO.guardarComponente(componente);
					    orden++;
			        }
			        
			        calcularFechasPadre(proyecto.getId(), 1, usuario, 2);
				}else{
					Componente componente = componentes.get(0);
					componente.setOrden(1);
					ComponenteDAO.guardarComponente(componente);
					calcularFechasPadre(proyecto.getId(), 1, usuario, 2);
				}
			}
		} else if(objetoTipo == 3){
			Producto pr = ProductoDAO.getProductoPorId(objetoId);
			Componente componente = ComponenteDAO.getComponentePorId(pr.getComponente().getId(), usuario);
			
			if(componente != null){
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(), null, 
						null, null, null, null, usuario);
				
				if(productos != null && productos.size() > 1){//orden de los productos
					Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
					Integer orden = 1;
					for(Producto producto : productos){
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
		        		Producto producto = ProductoDAO.getProductoPorId(entry.getKey(), usuario);
		        		producto.setOrden(orden);
					    ProductoDAO.guardarProducto(producto);
					    orden++;
			        }
			        
			        calcularFechasPadre(componente.getId(), 2, usuario, 3);
			        calcularOrdenObjetosSuperiores(componente.getId(), 2, usuario);
				}else{
					Producto producto = productos.get(0);
					producto.setOrden(1);
					ProductoDAO.guardarProducto(producto);
					calcularFechasPadre(componente.getId(), 2, usuario, 3);
					calcularOrdenObjetosSuperiores(componente.getId(), 2, usuario);
				}			
			}
		} else if(objetoTipo == 4){
			Subproducto sp = SubproductoDAO.getSubproductoPorId(objetoId);
			Producto producto = ProductoDAO.getProductoPorId(sp.getProducto().getId(), usuario);
			
			if(producto != null){
				List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(), null, null, null, 
						null, null, usuario);
				
				if(subproductos != null && subproductos.size() > 1){//orden de los subproductos
					Map<Integer, Long> fechas = new TreeMap<Integer, Long>();
					Integer orden = 1;
					for(Subproducto subproducto : subproductos){
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
		        		Subproducto subproducto = SubproductoDAO.getSubproductoPorId(entry.getKey(), usuario);
		        		subproducto.setOrden(orden);
					    SubproductoDAO.guardarSubproducto(subproducto);
					    orden++;
			        }
			        
			        calcularFechasPadre(producto.getId(), 3, usuario, 4);
			        calcularOrdenObjetosSuperiores(producto.getId(), 3, usuario);
				}else{
					Subproducto subproducto = subproductos.get(0);
					subproducto.setOrden(1);
					SubproductoDAO.guardarSubproducto(subproducto);
					calcularFechasPadre(producto.getId(), 3, usuario, 4);
					calcularOrdenObjetosSuperiores(producto.getId(), 3, usuario);
				}			
			}
		}
	}

	private static void calcularFechasPadre(Integer objetoIdPadre, Integer objetoTipoPadre, String usuario, Integer objetoNivelActual){
		Proyecto proyecto = null;
		Componente componente =  null;
		Producto producto = null;
		Subproducto subproducto = null;
		Actividad actividad = null;
		Date fechaInicial = new Date();
		Date fechaFinal = new Date();
		
		//Obtener fecha Inicial y fecha final
		if(objetoNivelActual == 2){//Padre del 2 es proyecto
			componente = ComponenteDAO.getComponenteInicial(objetoIdPadre, usuario);
			fechaInicial = componente.getFechaInicio();
			componente = ComponenteDAO.getComponenteFechaMaxima(objetoIdPadre, usuario);
			fechaFinal = componente.getFechaFin();
		} else if(objetoNivelActual == 3){//Padre del 3 es componente
			producto = ProductoDAO.getProductoInicial(objetoIdPadre, usuario);
			fechaInicial = producto.getFechaInicio();
			producto = ProductoDAO.getProductoFechaMaxima(objetoIdPadre, usuario);
			fechaFinal = producto.getFechaFin();
		} else if(objetoNivelActual == 4){//Padre del 4 es producto
			subproducto = SubproductoDAO.getSubproductoInicial(objetoIdPadre, usuario);
			fechaInicial = subproducto.getFechaInicio();
			subproducto = SubproductoDAO.getSubproductoFechaMaxima(objetoIdPadre, usuario);
			fechaFinal = subproducto.getFechaFin();
		} else if(objetoNivelActual == 5){//padre del 5 es proyecto, componente, producto, subproducto ó actividad
			actividad = ActividadDAO.getActividadInicial(objetoIdPadre, objetoTipoPadre, usuario);
			fechaInicial = actividad.getFechaInicio();
			actividad = ActividadDAO.getActividadFechaMaxima(objetoIdPadre,objetoTipoPadre,usuario);
			fechaFinal = actividad.getFechaFin();
		}
		
		if(objetoTipoPadre == 1){
    		proyecto = ProyectoDAO.getProyecto(objetoIdPadre);
    		proyecto.setFechaInicio(fechaInicial);
    		proyecto.setFechaFin(fechaFinal);
    		ProyectoDAO.guardarProyecto(proyecto);
    	}else if(objetoTipoPadre == 2){
    		componente = ComponenteDAO.getComponentePorId(objetoIdPadre, usuario);
    		componente.setFechaInicio(fechaInicial);
    		componente.setFechaFin(fechaFinal);
    		ComponenteDAO.guardarComponente(componente);
    	}else if(objetoTipoPadre == 3){
    		producto = ProductoDAO.getProductoPorId(objetoIdPadre);
    		producto.setFechaInicio(fechaInicial);
    		producto.setFechaFin(fechaFinal);
    		ProductoDAO.guardarProducto(producto);
    	}else if(objetoTipoPadre == 4){
    		subproducto = SubproductoDAO.getSubproductoPorId(objetoIdPadre);
    		subproducto.setFechaInicio(fechaInicial);
    		subproducto.setFechaFin(fechaFinal);
    		SubproductoDAO.guardarSubproducto(subproducto);
    	}else if(objetoTipoPadre == 5){
    		actividad = ActividadDAO.getActividadPorId(objetoIdPadre, usuario);
    		actividad.setFechaInicio(fechaInicial);
    		actividad.setFechaFin(fechaFinal);
    		ActividadDAO.guardarActividad(actividad);
    	}
	}
}
