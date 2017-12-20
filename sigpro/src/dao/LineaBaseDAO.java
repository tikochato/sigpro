package dao;

import java.util.List;

import org.hibernate.Session;

import pojo.Actividad;
import pojo.AsignacionRaci;
import pojo.Componente;
import pojo.ComponentePropiedadValor;
import pojo.ComponenteSigade;
import pojo.Desembolso;
import pojo.LineaBase;
import pojo.Meta;
import pojo.MetaPlanificado;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Producto;
import pojo.ProductoPropiedadValor;
import pojo.Proyecto;
import pojo.Riesgo;
import pojo.Subcomponente;
import pojo.SubcomponentePropiedadValor;
import pojo.Subproducto;
import utilities.CHibernateSession;
import utilities.CLogger;
import org.hibernate.query.Query;


public class LineaBaseDAO {
	public static List<LineaBase> getLineasBaseById(Integer proyectoid){
		List<LineaBase> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<LineaBase> criteria = session.createQuery("FROM LineaBase lb where lb.proyecto.id=:proyectoid", LineaBase.class);
			criteria.setParameter("proyectoid", proyectoid);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", LineaBaseDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static LineaBase getLineaBasePorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		List<LineaBase> listRet = null;
		LineaBase ret = null;
		try{
			String query = "FROM LineaBase l where l.id=:id";
			Query<LineaBase> criteria = session.createQuery(query, LineaBase.class);
			criteria.setParameter("id", id);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarLineaBase(LineaBase lineaBase,String lineaBaseEditar){
		boolean ret = false;
		if(lineaBaseEditar!=null && lineaBaseEditar.trim().length()>0)
			ret = eliminarLinaeBase(lineaBaseEditar) >= 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			session.beginTransaction();
			
			session.saveOrUpdate(lineaBase);
			session.flush();
			session.getTransaction().commit();
			session.close();
			ret = true;
			ret = ret && lineaBasePEP(lineaBase) >= 0;
			if(ret)
				ret = ret && lineaBasePEPPropiedadValor(lineaBase) >=0;
			if(ret)
				ret = ret && lineaBaseComponentes(lineaBase) >=0;
			if(ret)
				ret = ret && lineaBaseComponentesPropiedadValor(lineaBase) >=0;
			if (ret)
				ret = ret && lineaBaseSubcomponentes(lineaBase) >=0;
			if(ret)
				ret = ret && lineaBaseSubcomponentesPropiedadValor(lineaBase) >=0;
			if (ret)
				ret = ret && lineaBaseProducto(lineaBase) >=0;
			if(ret)
				ret = ret && lineaBaseProductoPropiedadValor(lineaBase) >=0;
			if (ret)
				ret = ret && lineaBaseSubproducto(lineaBase) >=0;
			if(ret)
				ret = ret && lineaBaseSubproductoPropiedadValor(lineaBase) >=0;
			if (ret)
					ret = ret && lineaBaseActividad(lineaBase) >=0;
			if(ret)
				ret = ret && lineaBaseActividadPropiedadValor(lineaBase) >=0;
			if (ret)
				ret = ret && lineaBaseDesembolsos(lineaBase) >=0;
			if (ret)
				ret = ret && lineaBaseMetas(lineaBase) >=0;
			if (ret )
				ret = ret && lineaBaseMetasPlanificado(lineaBase) >= 0;
			if (ret )
				ret = ret && lineaBaseMetasAvance(lineaBase) >= 0;
			if (ret )
				ret = ret && lineaBasePlanAdquisiciones(lineaBase,3) >= 0;
			if (ret )
				ret = ret && lineaBasePlanAdquisiciones(lineaBase,4) >= 0;
			if (ret )
				ret = ret && lineaBasePlanAdquisiciones(lineaBase,5) >= 0;
			if (ret)
				ret = ret && lineaBasePlanAdquisicionPagos(lineaBase,3) >= 0;
			if (ret)
				ret = ret && lineaBasePlanAdquisicionPagos(lineaBase,4) >= 0;
			if (ret)
				ret = ret && lineaBasePlanAdquisicionPagos(lineaBase,5) >= 0;
			if (ret)
				ret = ret && lineaBaseRiesgos(lineaBase,0) >= 0;
			if (ret)
				ret = ret && lineaBaseRiesgos(lineaBase,1) >= 0;
			if (ret)
				ret = ret && lineaBaseRiesgos(lineaBase,2) >= 0;
			if (ret)
				ret = ret && lineaBaseRiesgos(lineaBase,3) >= 0;
			if (ret)
				ret = ret && lineaBaseRiesgos(lineaBase,4) >= 0;
			if (ret)
				ret = ret && lineaBaseMatrizRaci(lineaBase,5) >= 0;
			if (ret)
				ret = ret && lineaBaseComponenteSigade(lineaBase) >= 0;
			
		}
		catch(Throwable e){
			CLogger.write("2", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	private static Integer lineaBasePEP (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.proyecto", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where id = ?1",
							"and estado = 1",
							"and actual = 1");
			
			Query<Proyecto> criteria = session.createNativeQuery(query, Proyecto.class);
			criteria.setParameter(1, lineaBase.getProyecto().getId());
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("3", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBasePEPPropiedadValor (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.proyecto_propiedad_valor", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where proyectoid = ?1",
							"and estado = 1",
							"and actual = 1");
			
			Query<Proyecto> criteria = session.createNativeQuery(query, Proyecto.class);
			criteria.setParameter(1, lineaBase.getProyecto().getId());
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("4", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseComponentes (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.componente", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where treepath like '" + lineaBase.getProyecto().getTreePath().trim() + "%' ",
							"and estado = 1",
							"and actual = 1");
			
			Query<Componente> criteria = session.createNativeQuery(query, Componente.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("5", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseComponentesPropiedadValor (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.componente_propiedad_valor",
						"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
						"where componenteid in ( ",
							"select c.id from sipro_history.componente c",
							"where c.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ", 
							"and c.estado = 1 and c.actual = 1",
						")",
						"and actual = 1");
			
			Query<ComponentePropiedadValor> criteria = session.createNativeQuery(query, ComponentePropiedadValor.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("6", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseSubcomponentes (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.subcomponente", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
							"and estado = 1",
							"and actual = 1");
			
			Query<Subcomponente> criteria = session.createNativeQuery(query, Subcomponente.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("7", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseSubcomponentesPropiedadValor (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","select * from sipro_history.subcomponente_propiedad_valor", 
						"where subcomponenteid in ( ",
							"select c.id from sipro_history.subcomponente c",
							"where c.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ", 
							"and c.estado = 1 and c.actual = 1",
						")",
						"and actual = 1");
			
			Query<SubcomponentePropiedadValor> criteria = session.createNativeQuery(query, SubcomponentePropiedadValor.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("8", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseProducto (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.producto", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
							"and estado = 1",
							"and actual = 1");
			
			Query<Producto> criteria = session.createNativeQuery(query, Producto.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("9", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseProductoPropiedadValor (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.producto_propiedad_valor",
						"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
						"where productoid in ( ",
							"select p.id from sipro_history.producto p",
							"where p.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ", 
							"and p.estado = 1 and p.actual = 1",
						")",
						"and actual = 1");
			
			Query<ProductoPropiedadValor> criteria = session.createNativeQuery(query, ProductoPropiedadValor.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("10", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseSubproducto (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.subproducto", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
							"and estado = 1",
							"and actual = 1");
			
			Query<Subproducto> criteria = session.createNativeQuery(query, Subproducto.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("11", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseSubproductoPropiedadValor (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.subproducto_propiedad_valor",
						"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
						"where subproductoid in ( ",
							"select p.id from sipro_history.subproducto p",
							"where p.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ", 
							"and p.estado = 1 and p.actual = 1",
						")",
						"and actual = 1");
			
			Query<ProductoPropiedadValor> criteria = session.createNativeQuery(query, ProductoPropiedadValor.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("12", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseActividad (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.actividad", 
							"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
							"where treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
							"and estado = 1",
							"and actual = 1");
			
			Query<Actividad> criteria = session.createNativeQuery(query, Actividad.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("13", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseActividadPropiedadValor (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.actividad_propiedad_valor",
						"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
						"where actividadid in ( ",
							"select a.id from sipro_history.actividad a",
							"where a.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ", 
							"and a.estado = 1 and a.actual = 1",
						")",
						"and actual = 1");
			
			Query<ProductoPropiedadValor> criteria = session.createNativeQuery(query, ProductoPropiedadValor.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("14", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseDesembolsos (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.desembolso",
				"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
				"where proyectoid = ?1 ",
				"and estado = 1");
			
			Query<Desembolso> criteria = session.createNativeQuery(query, Desembolso.class);
			criteria.setParameter(1, lineaBase.getProyecto().getId());
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("15", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	private static Integer lineaBaseMetas (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.meta", 
				"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
				"where objeto_tipo = 3",
				"and estado = 1",
				"and actual = 1",
				"and objeto_id in (",
					"select c.id from sipro_history.producto c",
				    "where c.estado = 1",
				    "and c.actual = 1",
				    "and c.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
				")");
			
			Query<Meta> criteria = session.createNativeQuery(query, Meta.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("16", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseMetasPlanificado (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.meta_planificado",
				"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
				"where version = 1",
				"and estado = 1",
				"and metaid in (",
					"select m.id from sipro_history.producto p,sipro_history.meta m",
				    "where p.id = m.objeto_id",
				    "and p.estado = 1",
				    "and p.actual = 1",
				    "and p.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
				    "and m.estado = 1",
				    "and m.actual = 1",
				")");
			
			Query<MetaPlanificado> criteria = session.createNativeQuery(query, MetaPlanificado.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("17", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseMetasAvance (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.meta_avance",
				"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
				"where version = 1",
				"and estado = 1",
				"and metaid in (",
					"select m.id from sipro_history.producto p,sipro_history.meta m",
				    "where p.id = m.objeto_id",
				    "and p.estado = 1",
				    "and p.actual = 1",
				    "and p.treepath like '" + lineaBase.getProyecto().getTreePath() + "%' ",
				    "and m.estado = 1",
				    "and m.actual = 1",
				")");
			
			Query<MetaPlanificado> criteria = session.createNativeQuery(query, MetaPlanificado.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("17", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBasePlanAdquisiciones (LineaBase lineaBase,int objetoTipo){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String tabla = "";
			if(objetoTipo == 3)
				tabla = "producto";
			else if (objetoTipo == 4)
				tabla = "subproducto";
			else if (objetoTipo == 5)
				tabla = "actividad";
			
			String query = String.join(" ","update sipro_history.plan_adquisicion",
				"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
				"where actual = 1",
				"and estado = 1",
				"and objeto_tipo = ?1",
				"and objeto_id in (",
					"select t.id from sipro_history."+tabla + " t",
					"where t.estado = 1",
					"and t.actual = 1",
					"and t.treepath like '" + lineaBase.getProyecto().getTreePath() + "%') ");
			
			Query<PlanAdquisicion> criteria = session.createNativeQuery(query, PlanAdquisicion.class);
			criteria.setParameter(1, objetoTipo);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("18", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBasePlanAdquisicionPagos (LineaBase lineaBase,int objetoTipo){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String tabla = "";
			if(objetoTipo == 3)
				tabla = "producto";
			else if (objetoTipo == 4)
				tabla = "subproducto";
			else if (objetoTipo == 5)
				tabla = "actividad";
			
			String query = String.join(" ","update sipro_history.plan_adquisicion_pago",
						"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
						"where estado = 1",
						"and actual = 1",
						"and plan_adquisicionid in (",
							"select p.id",
							"from sipro_history." + tabla + " t, sipro_history.plan_adquisicion p",
							"where t.id = p.objeto_id",
							"and p.objeto_tipo = ?1",
							"and t.estado = 1",
							"and t.actual = 1",
							"and t.treepath like '" + lineaBase.getProyecto().getTreePath() + "%') ");
			Query<PlanAdquisicionPago> criteria = session.createNativeQuery(query, PlanAdquisicionPago.class);
			criteria.setParameter(1, objetoTipo);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("19", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseRiesgos (LineaBase lineaBase,int objetoTipo){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String tabla = "";
			if(objetoTipo == 0)
				tabla = "proyecto";
			if(objetoTipo == 1)
				tabla = "componente";
			if(objetoTipo == 2)
				tabla = "subcomponente";
			if(objetoTipo == 3)
				tabla = "producto";
			else if (objetoTipo == 4)
				tabla = "subproducto";
			else if (objetoTipo == 5)
				tabla = "actividad";
			
			String query = String.join(" ","update sipro_history.objeto_riesgo o,sipro_history.riesgo r",
					"set r.linea_base = CONCAT(ifnull(r.linea_base,''),'|lb',",lineaBase.getId().toString(),",'|'),",
					"o.linea_base = CONCAT(ifnull(o.linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
					"where o.riesgoid = r.id",
					"and o.actual = 1",
					"and o.objeto_tipo = ?1",
					"and r.actual = 1",
					"and r.estado = 1",
					"and o.objeto_id in (",
					    "select t.id ",
					    "from sipro_history." + tabla + " t",
					    "where t.actual = 1",
					    "and t.estado = 1",
					    "and t.treepath like '" + lineaBase.getProyecto().getTreePath() + "%') ");
					    
			Query<Riesgo> criteria = session.createNativeQuery(query, Riesgo.class);
			criteria.setParameter(1, objetoTipo);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("20", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseMatrizRaci (LineaBase lineaBase,int objetoTipo){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String tabla = "";
			if(objetoTipo == 0)
				tabla = "proyecto";
			if(objetoTipo == 1)
				tabla = "componente";
			if(objetoTipo == 2)
				tabla = "subcomponente";
			if(objetoTipo == 3)
				tabla = "producto";
			else if (objetoTipo == 4)
				tabla = "subproducto";
			else if (objetoTipo == 5)
				tabla = "actividad";
			
			String query = String.join(" ","update sipro_history.asignacion_raci ",
					"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
					"where actual = 1 ",
					"and estado = 1",
					"and objeto_tipo = ?1",
					"and objeto_id in (",
						"select t.id ",
						"from sipro_history." + tabla + " t",
					    "where t.actual = 1",
					    "and t.estado = 1",
					    "and t.treepath like '" + lineaBase.getProyecto().getTreePath() + "%') ");
					    
			Query<AsignacionRaci> criteria = session.createNativeQuery(query, AsignacionRaci.class);
			criteria.setParameter(1, objetoTipo);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("21", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer lineaBaseComponenteSigade (LineaBase lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			String query = String.join(" ","update sipro_history.componente_sigade",
					"set linea_base = CONCAT(ifnull(linea_base,''),'|lb',",lineaBase.getId().toString(),",'|')",
					"where actual = 1",
					"and estado = 1",
					"and id in (",
						"select c.componente_sigadeid",
						"from sipro_history.componente c",
						"where c.actual = 1",
						"and c.estado = 1",
						"and c.treepath like '" + lineaBase.getProyecto().getTreePath() + "%') ");
			
			Query<ComponenteSigade> criteria = session.createNativeQuery(query, ComponenteSigade.class);
			ret =   criteria.executeUpdate();
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			CLogger.write("22", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	private static Integer eliminarLinaeBase (String lineaBase){
		Integer ret = -1;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			
			String query = "UPDATE sipro_history.actividad SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.actividad_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.asignacion_raci SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.componente SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.componente_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.componente_sigade SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.desembolso SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();


			query = "UPDATE sipro_history.meta SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.meta_avance SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.meta_planificado SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.plan_adquisicion SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.plan_adquisicion_pago SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.prestamo SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();


			query = "UPDATE sipro_history.producto SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.producto_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.proyecto SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.proyecto_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.riesgo SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.riesgo_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();

			query = "UPDATE sipro_history.subcomponente SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.subcomponente_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.subproducto SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();
			
			query = "UPDATE sipro_history.subproducto_propiedad_valor SET linea_base = REPLACE(linea_base,'"+ lineaBase + "','')";
			ret = ret + session.createNativeQuery(query).executeUpdate();
			session.flush();


			session.getTransaction().commit();
			session.close();
		}
		catch(Throwable e){
			ret = -1;
			CLogger.write("23", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean eliminarTotalLineaBase(LineaBase lineaBase){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(lineaBase);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("24", LineaBaseDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}

