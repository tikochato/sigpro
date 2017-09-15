package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.joda.time.DateTime;

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
	
	private Integer getNivel(String treePath){
		treePath = treePath.substring(1,treePath.length());
		double longitudTree = treePath.length();
		String[] result = new String[(int)Math.ceil(longitudTree/6)];
	    return result.length ;
	}
	
	public void calcularOrdenObjetosSuperiores(Integer objetoId, Integer objetoTipo, String usuario, 
			Session session, Integer proyectoId){
		if(estructuraPrestamo == null){
			estructuraPrestamo = getEstructuraPrestamo(proyectoId, usuario);
		}
				
		Date fechaMax = new DateTime(2200,1,1,0,0).toDate();
		
		getTransactionSession(session);
		Object[] objPadre = getObjeto(objetoId, objetoTipo, estructuraPrestamo);
		String treePath = (objetoTipo==1) ? "1" : (String)getObjeto((Integer)objPadre[4], (Integer)objPadre[5], estructuraPrestamo)[6];
		List<Object[]> hijos = getHijos((Integer)objPadre[4], (Integer)objPadre[5], estructuraPrestamo);
		ArrayList<Long[]> fechas = new ArrayList<Long[]>();
		Integer orden = 1;
		
		for(Object[] obj: hijos){
			switch((int)obj[5]){
				case 2: Componente componente = (Componente)obj[3];
					fechas.add(new Long[]{new Long(componente.getId()), new Long(2), componente.getFechaInicio() != null ? componente.getFechaInicio().getTime() : fechaMax.getTime()});
					break;
				case 3:
					Producto producto = (Producto)obj[3];
					fechas.add(new Long[]{ new Long(producto.getId()), new Long(3), producto.getFechaInicio() != null ? producto.getFechaInicio().getTime() : fechaMax.getTime()});
					break;
				case 4:
					Subproducto subproducto = (Subproducto)obj[3];
					fechas.add(new Long[]{new Long(subproducto.getId()), new Long(4),subproducto.getFechaInicio() != null ? subproducto.getFechaInicio().getTime() : fechaMax.getTime()});
					break;
				case 5:
					Actividad actividad = (Actividad)obj[3];
					fechas.add(new Long[]{ new Long(actividad.getId()),new Long(5), actividad.getFechaInicio() != null ? actividad.getFechaInicio().getTime() : fechaMax.getTime()});
					break;
			}
		}
		
		Collections.sort(fechas, new Comparator<Long[]>(){
            public int compare( Long[] o1, Long[] o2 )
            {
                return (o1[2].compareTo(o2[2]));
            }
        });
        
		Date fecha_menor = new DateTime(2200,1,1,0,0).toDate();
		Date fecha_mayor = new DateTime(1970,1,1,0,0).toDate();
        for(Long[] entry:fechas){
        	Object[] objeto = getObjeto(entry[0].intValue(), entry[1].intValue(), estructuraPrestamo);
        	switch(entry[1].intValue()){
        		case 2:
        			Componente componente = (Componente)objeto[3];
    	        	componente.setOrden(orden);
    	        	componente.setTreePath(treePath + String.format("%6s", orden).replace(' ', '0'));
    	        	componente.setNivel(getNivel(componente.getTreePath()));
    	        	ComponenteDAO.guardarComponenteOrden(componente, session);
    	        	fecha_menor = componente.getFechaInicio()!=null && fecha_menor.after(componente.getFechaInicio()) ? componente.getFechaInicio() : fecha_menor;
    	        	fecha_mayor = componente.getFechaFin()!=null && fecha_mayor.before(componente.getFechaFin()) ? componente.getFechaFin() : fecha_mayor;
        			break;
        		case 3:
        			Producto producto = (Producto)objeto[3];
    	        	producto.setOrden(orden);
    	        	producto.setTreePath(treePath + String.format("%6s", orden).replace(' ', '0'));
    	        	producto.setNivel(getNivel(producto.getTreePath()));
    	        	ProductoDAO.guardarProductoOrden(producto, session);
    	        	fecha_menor = producto.getFechaInicio()!=null && fecha_menor.after(producto.getFechaInicio()) ? producto.getFechaInicio() : fecha_menor;
    	        	fecha_mayor = producto.getFechaFin()!=null && fecha_mayor.before(producto.getFechaFin()) ? producto.getFechaFin() : fecha_mayor;
        			break;
        		case 4:
        			Subproducto sproducto = (Subproducto)objeto[3];
        			sproducto.setOrden(orden);
        			sproducto.setTreePath(treePath + String.format("%6s", orden).replace(' ', '0'));
        			sproducto.setNivel(getNivel(sproducto.getTreePath()));
    	        	SubproductoDAO.guardarSubproductoOrden(sproducto, session);
    	        	fecha_menor = sproducto.getFechaInicio()!=null && fecha_menor.after(sproducto.getFechaInicio()) ? sproducto.getFechaInicio() : fecha_menor;
    	        	fecha_mayor = sproducto.getFechaFin()!=null && fecha_mayor.before(sproducto.getFechaFin()) ? sproducto.getFechaFin() : fecha_mayor;
        			break;
        		case 5:
        			Actividad actividad = (Actividad)objeto[3];
        			actividad.setOrden(orden);
        			actividad.setTreePath(treePath + String.format("%6s", orden).replace(' ', '0'));
        			actividad.setNivel(getNivel(actividad.getTreePath()));
    	        	ActividadDAO.guardarActividadOrden(actividad, session);
    	        	fecha_menor = actividad.getFechaInicio()!=null && fecha_menor.after(actividad.getFechaInicio()) ? actividad.getFechaInicio() : fecha_menor;
    	        	fecha_mayor = actividad.getFechaFin()!=null && fecha_mayor.before(actividad.getFechaFin()) ? actividad.getFechaFin() : fecha_mayor;
        			break;
        	}
	        orden++;
        }
        switch((int)objPadre[5]){
        	case 1: Proyecto proyecto = (Proyecto)getObjeto((int)objPadre[4], 1, estructuraPrestamo)[3];
        		proyecto.setFechaInicio(fecha_menor);
        		proyecto.setFechaFin(fecha_mayor);
        		ProyectoDAO.guardarProyectoOrden(proyecto, session);
        		break;
        	case 2: Componente componente = (Componente)getObjeto((int)objPadre[4], 2, estructuraPrestamo)[3];
        		componente.setFechaInicio(fecha_menor);
        		componente.setFechaFin(fecha_mayor);
	    		ComponenteDAO.guardarComponenteOrden(componente, session);
	    		break;	
        	case 3: Producto producto = (Producto)getObjeto((int)objPadre[4], 3, estructuraPrestamo)[3];
	        	producto.setFechaInicio(fecha_menor);
	        	producto.setFechaFin(fecha_mayor);
	    		ProductoDAO.guardarProductoOrden(producto, session);
	    		break;
        	case 4: Subproducto subproducto = (Subproducto)getObjeto((int)objPadre[4], 4, estructuraPrestamo)[3];
	        	subproducto.setFechaInicio(fecha_menor);
	        	subproducto.setFechaFin(fecha_mayor);
	    		SubproductoDAO.guardarSubproductoOrden(subproducto, session);
	    		break;
        	case 5: Actividad actividad = (Actividad)getObjeto((int)objPadre[4], 5, estructuraPrestamo)[3];
	        	actividad.setFechaInicio(fecha_menor);
	        	actividad.setFechaFin(fecha_mayor);
	    		ActividadDAO.guardarActividadOrden(actividad, session);
	    		break;
        }
        if((Integer)objPadre[1]>0){
        	calcularOrdenObjetosSuperiores((Integer)objPadre[1], (Integer)objPadre[2], usuario, session,proyectoId);
        }
		commitCalculoOrden(session);
	}

	
	
	private static List<Object[]> getEstructuraPrestamo(Integer proyectoId, String usuario){
		Object[] objProyecto = null;
		List<Object[]> lstProyecto = new ArrayList<Object[]>();
		
		try{
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			objProyecto = new Object[]{ 1, 0, 0, proyecto, proyecto.getId(),1, proyecto.getTreePath() == null ? "1" : proyecto.getTreePath() };
			lstProyecto.add(objProyecto);
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0,0, proyectoId, null, null, null, null, null, usuario);
			for(Componente componente : componentes){
				objProyecto = new Object[]{2, proyectoId, 1, componente, componente.getId(), 2, componente.getTreePath()};
				lstProyecto.add(objProyecto);
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(), null, null, null, null, null, usuario);
				for(Producto producto : productos){
					objProyecto = new Object[]{ 3, producto.getComponente().getId(), 2, producto, producto.getId(), 3, producto.getTreePath()};
					lstProyecto.add(objProyecto);
					
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(), null, null, null, null, null, usuario);
					for(Subproducto subproducto : subproductos){
						objProyecto = new Object[]{ 4, subproducto.getProducto().getId(), 3, subproducto, subproducto.getId(), 4, subproducto.getTreePath()};
						lstProyecto.add(objProyecto);
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), 4, null, null, null, null, null, usuario);
						for(Actividad actividad : actividades){
							lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, subproducto.getId(), 4);
						}
					}
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), 3, null, null, null, null, null, usuario);
					for(Actividad actividad : actividades){
						lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, producto.getId(), 3);
					}
				}
				
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), 2, null, null, null, null, null, usuario);
				for(Actividad actividad : actividades){
					lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, componente.getId(), 2);
				}
			}
			
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyecto.getId(), 1, null, null, null, null, null, usuario);
			for(Actividad actividad : actividades){
				lstProyecto = obtenerActividades(actividad, lstProyecto, usuario, proyecto.getId(), 1);
			}
			
		}catch(Exception ex){
			return null;
		}
		return lstProyecto;
	}
	
	private static List<Object[]> obtenerActividades(Actividad actividad, List<Object[]> lstProyecto, String usuario, Integer objetoPadreId, Integer objetoPadreTipo){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), 5, null, null, null, null, null, usuario);
		Object[] objProyecto = new Object[]{5, objetoPadreId, objetoPadreTipo, actividad, actividad.getId(), 5, actividad.getTreePath()};
		lstProyecto.add(objProyecto);
		
		for(Actividad subActividad : actividades){
			lstProyecto = obtenerActividades(subActividad, lstProyecto, usuario, actividad.getId(), 5);
		}
		
		
		return lstProyecto;
	}
	
	private static List<Object[]> getHijos(Integer objetoPadreId, Integer objetoPadreTipo, List<Object[]> estructuraPrestamo){
		List<Object[]> ret = new ArrayList<Object[]>();
		try{
			for(Object[] obj : estructuraPrestamo){
				if(((Integer)obj[1]).intValue() == objetoPadreId && ((Integer)obj[2]).intValue() == objetoPadreTipo){
					ret.add(obj);
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