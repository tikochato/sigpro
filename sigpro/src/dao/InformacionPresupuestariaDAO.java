package dao;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import pojo.Proyecto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utilities.CJasperReport;
import utilities.CLogger;
 
public class InformacionPresupuestariaDAO {
	public static ArrayList<Date> getEstructuraArbolPrestamoFecha(int idPrestamo, Connection conn){
    	ArrayList<Date> ret = new ArrayList<Date>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select min(fecha_inicio) fecha_inicio, max(fecha_fin) fecha_fin from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente  is not null",
                            "and producto  is not null",
                            "and subproducto  is not null",
                            "group by componente",
                            "order by 2;");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                       ret.add(rs.getDate("fecha_inicio") );
                       ret.add(rs.getDate("fecha_fin"));
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("1", InformacionPresupuestariaDAO.class, e);
                }
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return ret;
        
    }
	
	
    public static ArrayList<Integer> getEstructuraArbolComponentes(int idPrestamo, Connection conn){
    	ArrayList<Integer> ret = new ArrayList<Integer>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select componente, min(fecha_inicio) fecha from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente is not null",
                            "group by componente",
                            "order by 2;");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                       ret.add(rs.getInt("componente") );
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("2", InformacionPresupuestariaDAO.class, e);
                }
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return ret;
        
    }
    
    public static ArrayList<Date> getEstructuraArbolComponentesFecha(int idPrestamo, int idComponente, Connection conn){
    	ArrayList<Date> ret = new ArrayList<Date>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select min(fecha_inicio) fecha_inicio, max(fecha_fin) fecha_fin from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ?",
                            "group by componente",
                            "order by 2;");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                       ret.add(rs.getDate("fecha_inicio") );
                       ret.add(rs.getDate("fecha_fin"));
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("3", InformacionPresupuestariaDAO.class, e);
                }
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return ret;
        
    }
    
    public static ArrayList<Integer> getEstructuraArbolProducto(int idPrestamo, int idComponente, Connection conn){
    	ArrayList<Integer> ret = new ArrayList<Integer>();
        try {
            if( !conn.isClosed()){
                try{
                    String str_Query = String.join(" ","select producto, min(fecha_inicio) fecha ",
                            "from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto is not null",
                            "group by producto",
                            "order by 2");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    ResultSet rs = pstm.executeQuery();
                   
                    
                    while(rs!=null && rs.next()){
                        ret.add(rs.getInt("producto"));
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    e.printStackTrace();
                    CLogger.write("4", InformacionPresupuestariaDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("2", InformacionPresupuestariaDAO.class, e);
        }
        return ret;
    }
    
    public static ArrayList<Date> getEstructuraArbolProductoFecha(int idPrestamo, int idComponente, int idProducto, Connection conn){
    	ArrayList<Date> ret = new ArrayList<Date>();
        try {
            if( !conn.isClosed()){
                try{
                    String str_Query = String.join(" ","select producto, min(fecha_inicio) fecha_inicio, max(fecha_fin) fecha_fin ",
                            "from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ?",
                            "group by producto",
                            "order by 2");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    pstm.setInt(3, idProducto);
                    ResultSet rs = pstm.executeQuery();
                   
                    
                    while(rs!=null && rs.next()){
                        ret.add(rs.getDate("fecha_inicio"));
                        ret.add(rs.getDate("fecha_fin"));
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("5", InformacionPresupuestariaDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("6", InformacionPresupuestariaDAO.class, e);
        }
        return ret;
    }
    
    public static ArrayList<Integer> getEstructuraArbolSubProducto(int idPrestamo,int idComponente, int idProducto, Connection conn){
    	ArrayList<Integer> ret = new ArrayList<Integer>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select subproducto, min(fecha_inicio) fecha from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ? ",
                            "and subproducto is not null",
                            "group by subproducto",
                            "order by 2;");
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    pstm.setInt(3, idProducto);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                       ret.add(rs.getInt("subproducto"));
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("7", InformacionPresupuestariaDAO.class, e);
                }            
            }
        } catch (SQLException e) {
            CLogger.write("8", InformacionPresupuestariaDAO.class, e);
        }
        
        
        return ret;
    }
    
    public static ArrayList<Date> getEstructuraArbolSubProductoFecha(int idPrestamo,int idComponente, int idProducto, int idSubProducto, Connection conn){
    	ArrayList<Date> ret = new ArrayList<Date>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select min(fecha_inicio) fecha_inicio, max(fecha_fin) fecha_fin from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ? ",
                            "and subproducto = ?",
                            "group by subproducto",
                            "order by 2;");
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    pstm.setInt(3, idProducto);
                    pstm.setInt(4, idSubProducto);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                       ret.add(rs.getDate("fecha_inicio"));
                       ret.add(rs.getDate("fecha_fin"));
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    e.printStackTrace();
                    CLogger.write("9", InformacionPresupuestariaDAO.class, e);
                }            
            }
        } catch (SQLException e) {
            CLogger.write("9", InformacionPresupuestariaDAO.class, e);
        }
        
        
        return ret;
    }
    
    public static ArrayList<ArrayList<Integer>> getEstructuraArbolComponentesActividades(int idPrestamo, int idComponente, Connection conn){
    	ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        try {
            if( !conn.isClosed()){
                try{
                    String str_Query = String.join(" ","select prestamo,componente, actividad, treelevel, min(fecha_inicio) fecha",
                            "from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto is null",
                            "and actividad is not null",
                            "group by prestamo, componente, actividad",
                            "order by 5, treelevel");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setFetchSize(50);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(rs.getInt("actividad"));
                        temp.add(rs.getInt("treelevel"));
                        ret.add(temp);
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("10", InformacionPresupuestariaDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("11", InformacionPresupuestariaDAO.class, e);
        }
        return ret;
    }
    
    public static ArrayList<ArrayList<Integer>> getEstructuraArbolProductoActividades(int idPrestamo, int idComponente, int idProducto, Connection conn){
        ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        try {
            if( !conn.isClosed()){
                try{
                    String str_Query = String.join(" ","select prestamo,componente, producto, actividad, treelevel, min(fecha_inicio) fecha",
                            "from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ? ",
                            "and subproducto is null",
                            "and actividad is not null",
                            "group by prestamo, componente, producto,actividad",
                            "order by 6, treelevel");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setFetchSize(50);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    pstm.setInt(3, idProducto);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                    	ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(rs.getInt("actividad"));
                        temp.add(rs.getInt("treelevel"));
                        ret.add(temp);
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("12", InformacionPresupuestariaDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("13", InformacionPresupuestariaDAO.class, e);
        }
        return ret;
    }
    
    public static ArrayList<ArrayList<Integer>> getEstructuraArbolSubProductoActividades(int idPrestamo, int idComponente, int idProducto, int idsubProducto,
            Connection conn){
    	ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select prestamo,componente, producto, subproducto, actividad, treelevel,min(fecha_inicio) fecha",
                            "from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ? ",
                            "and subproducto = ? ",
                            "and actividad is not null",
                            "group by prestamo, componente, producto,subproducto, actividad",
                            "order by 7, treelevel");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setFetchSize(1000);
                    pstm.setInt(1, idPrestamo);
                    pstm.setInt(2, idComponente);
                    pstm.setInt(3, idProducto);
                    pstm.setInt(4, idsubProducto);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(rs.getInt("actividad"));
                        temp.add(rs.getInt("treelevel"));
                        ret.add(temp);
                    }
                    
                    rs.close();
                    pstm.close();
                }
                catch(Throwable e){
                    CLogger.write("14",InformacionPresupuestariaDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("15", InformacionPresupuestariaDAO.class, e);
        }
        
        return ret;
    }
    
    public static ArrayList<ArrayList<Integer>> getEstructuraArbolPrestamoActividades(int idPrestamo, Connection conn){
    	 ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select prestamo,actividad, treelevel, min(fecha_inicio) fecha",
                            "from estructura_arbol",
                            "where prestamo = ? ",
                            "and componente is null",
                            "and producto is null",
                            "and subproducto is null",
                            "and actividad is not null",
                            "group by prestamo, actividad",
                            "order by 4, treelevel");
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setFetchSize(50);
                    pstm.setInt(1, idPrestamo);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(rs.getInt("actividad"));
                        temp.add( rs.getInt("treelevel"));   
                        ret.add(temp);
                    }
                    
                    rs.close();
                    pstm.close();
                    
                }
                catch(Throwable e){
                    CLogger.write("16",InformacionPresupuestariaDAO.class, e);
                }
            }
        } catch (SQLException e) {
            CLogger.write("17", InformacionPresupuestariaDAO.class, e);
        }
        
        return ret;
    }
    
    public static ArrayList<ArrayList<BigDecimal>> getPresupuestoProyecto(Integer fuente, Integer organismo, Integer correlativo, Integer anoInicial, Integer anoFinal, Connection conn){
    	ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();

		try{
			if(!conn.isClosed() ){
				String str_Query = String.join(" ", "select enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre, ejercicio",
						"from mv_ep_prestamo where",
						"fuente=? and",
						"organismo=? and",
						"correlativo=? and",
						"ejercicio between ? and ?");
				PreparedStatement pstm  = conn.prepareStatement(str_Query);
				pstm.setFetchSize(1000);
                pstm.setInt(1, fuente);
                pstm.setInt(2, organismo);
                pstm.setInt(3, correlativo);
                pstm.setInt(4, anoInicial);
                pstm.setInt(5, anoFinal);
                ResultSet rs = pstm.executeQuery();
                
                while(rs!=null && rs.next()){
                    ArrayList<BigDecimal> temp = new ArrayList<BigDecimal>();
            		temp.add(rs.getBigDecimal("enero"));
                    temp.add(rs.getBigDecimal("febrero"));
                    temp.add(rs.getBigDecimal("marzo"));
                    temp.add(rs.getBigDecimal("abril"));
                    temp.add(rs.getBigDecimal("mayo"));
                    temp.add(rs.getBigDecimal("junio"));
                    temp.add(rs.getBigDecimal("julio"));
                    temp.add(rs.getBigDecimal("agosto"));
                    temp.add(rs.getBigDecimal("septiembre"));
                    temp.add(rs.getBigDecimal("octubre"));
                    temp.add(rs.getBigDecimal("noviembre"));
                    temp.add(rs.getBigDecimal("diciembre"));
                    temp.add(new BigDecimal(rs.getInt("ejercicio")));

                    result.add(temp);
                }
                
                rs.close();
                pstm.close();
			}
		}
		catch(Throwable e){
			CLogger.write("18", InformacionPresupuestariaDAO.class, e);
		}
		
		return result;
	}
    
    public static ArrayList<ArrayList<BigDecimal>> getPresupuestoPorObjeto(Integer fuente, Integer organismo, Integer correlativo, Integer anoInicial, Integer anoFinal, Integer programa, Integer subprograma, 
    		Integer proyecto, Integer actividad, Integer obra, Integer renglon, Integer geografico, Connection conn){
    	ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();
		//Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			if(!conn.isClosed()){
				if(programa != null && programa >=0){
					String str_Query = String.join(" ", "select sum(enero) enero, sum(febrero) febrero, sum(marzo) marzo, sum(abril) abril, sum(mayo) mayo, sum(junio) junio, sum(julio) julio, sum(agosto) agosto, sum(septiembre) septiembre, sum(octubre) octubre, sum(noviembre) noviembre, sum(diciembre) diciembre, ejercicio from mv_ep_estructura",
						"where ejercicio between ? and ?");
					
					if(fuente != null){
						str_Query = String.join(" ", str_Query, "and fuente=?");	
					}
					
					if(organismo != null){
						str_Query = String.join(" ", str_Query, "and organismo=?");	
					}
					
					if(correlativo != null){
						str_Query = String.join(" ", str_Query, "and correlativo=?");	
					}
					
					if(programa != null){
						str_Query = String.join(" ", str_Query, "and programa=?");	
					}
					
					if(subprograma != null){
						str_Query = String.join(" ", str_Query, "and subprograma=?");	
					}
					
					if(proyecto != null){
						str_Query = String.join(" ", str_Query, "and proyecto=?");	
					}
					
					if(actividad != null){
						str_Query = String.join(" ", str_Query, "and actividad=?");	
					}
					
					if(obra != null){
						str_Query = String.join(" ", str_Query, "and obra=?");	
					}
					
					if(renglon != null){
						str_Query = String.join(" ",str_Query, "and renglon=?");
					}
					
					if(geografico != null){
						str_Query = String.join(" ",str_Query, "and geografico=?");
					}
					
					PreparedStatement pstm  = conn.prepareStatement(str_Query);
					
					int pos = 1;
					if(anoInicial != null){
						pstm.setInt(pos, anoInicial);
						pos++;
					}
					
					if(anoFinal != null){
						pstm.setInt(pos, anoFinal);
						pos++;
					}
	                
					if(fuente != null){
						pstm.setInt(pos, fuente);
						pos++;
					}
	                
					if(organismo != null){
						pstm.setInt(pos, organismo);
						pos++;
					}
	                
					if(correlativo != null){
						pstm.setInt(pos, correlativo);
						pos++;
					}
	                
					if(programa != null){
						pstm.setInt(pos, programa);
						pos++;
					}
	                
					if(subprograma != null){
						pstm.setInt(pos, subprograma);
						pos++;
					}
	                
					if(proyecto != null){
						pstm.setInt(pos, proyecto);
						pos++;
					}
	                
					if(actividad != null){
						pstm.setInt(pos, actividad);
						pos++;
					}
	                
					if(obra != null){
						pstm.setInt(pos, obra);	
						pos++;
					}
					
					if(renglon != null){
						pstm.setInt(pos, renglon);
						pos++;
					}
					
					if(geografico != null){
						pstm.setInt(pos, geografico);
					}
	                
	                ResultSet rs = pstm.executeQuery();
					
	                while(rs!=null && rs.next()){
	                    ArrayList<BigDecimal> temp = new ArrayList<BigDecimal>();
	            		temp.add(rs.getBigDecimal("enero"));
	                    temp.add(rs.getBigDecimal("febrero"));
	                    temp.add(rs.getBigDecimal("marzo"));
	                    temp.add(rs.getBigDecimal("abril"));
	                    temp.add(rs.getBigDecimal("mayo"));
	                    temp.add(rs.getBigDecimal("junio"));
	                    temp.add(rs.getBigDecimal("julio"));
	                    temp.add(rs.getBigDecimal("agosto"));
	                    temp.add(rs.getBigDecimal("septiembre"));
	                    temp.add(rs.getBigDecimal("octubre"));
	                    temp.add(rs.getBigDecimal("noviembre"));
	                    temp.add(rs.getBigDecimal("diciembre"));
	                    temp.add(new BigDecimal(rs.getInt("ejercicio")));

	                    result.add(temp);
	                }
	                
	                rs.close();
	                pstm.close();
				}
			}
		}
		catch(Throwable e){
			CLogger.write("19", InformacionPresupuestariaDAO.class, e);
		}
		
		return result;
	}
    

    public static ArrayList<BigDecimal> getPresupuestosPorObjeto(Integer fuente, Integer organismo, Integer correlativo, Integer ejercicio,
    		Integer mes, Integer entidad, Integer programa, Integer subprograma, 
    		Integer proyecto, Integer actividad, Integer obra, Integer renglon, Integer geografico, Connection conn){
    	ArrayList<BigDecimal> result = new ArrayList<BigDecimal>();
    	result.add(new BigDecimal(0));
    	result.add(new BigDecimal(0));
    	result.add(new BigDecimal(0));
		//Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			if(!conn.isClosed()){
				String str_Query = "select sum(asignado) asignado, sum(devengado) devengado, sum(modificaciones) modificaciones "
						+ " from ( "														
						+ " select sum(asignado) asignado, 0 devengado, 0 modificaciones "
						+ " from sipro_analytic.mv_ep_ejec_asig_vige  "
						+ " where "
						+ " mes = 0 ";
						str_Query += entidad!=null? " and entidad = '"+entidad+"' " : "";
//						str_Query += unidad_ejecutora!=null? " and unidad_ejecutra = "+unidad_ejecutora : "";
						str_Query += programa!=null? " and programa = "+programa : "";
						str_Query += subprograma!=null? " and subprograma = "+subprograma  : "";
						str_Query += proyecto!=null? " and proyecto = "+proyecto : "";
						str_Query += actividad!=null? " and actividad = "+actividad : "";
						str_Query += renglon!=null? " and renglon = "+renglon : "";
						str_Query += geografico!=null? " and geografico = "+geografico : "";
						str_Query += correlativo!=null? " and correlativo = "+correlativo : "";
						str_Query += organismo!=null? " and organismo = "+organismo : "";
						str_Query += fuente!=null? " and fuente = "+fuente : "";
						str_Query += " and ejercicio = "+ejercicio 
						+ " group by entidad "
						+ " UNION " 
						+ " select 0 asignado, sum(ejecutado) devengado, 0 modificaciones "
						+ " from sipro_analytic.mv_ep_ejec_asig_vige  "
						+ " where ejercicio = "+ejercicio;
						str_Query += entidad!=null? " and entidad = '"+entidad+"' " : "";
//						str_Query += unidad_ejecutora!=null? " and unidad_ejecutra = "+unidad_ejecutora : "";
						str_Query += programa!=null? " and programa = "+programa : "";
						str_Query += subprograma!=null? " and subprograma = "+subprograma  : "";
						str_Query += proyecto!=null? " and proyecto = "+proyecto : "";
						str_Query += actividad!=null? " and actividad = "+actividad : "";
						str_Query += renglon!=null? " and renglon = "+renglon : "";
						str_Query += geografico!=null? " and geografico = "+geografico : "";
						str_Query += correlativo!=null? " and correlativo = "+correlativo : "";
						str_Query += organismo!=null? " and organismo = "+organismo : "";
						str_Query += fuente!=null? " and fuente = "+fuente : "";
						str_Query += " and mes = "+mes
						+ " group by entidad "
						+ " UNION " 
						+ " select 0 asignado, 0 devengado, sum(modificaciones) modificaciones "
						+ " from sipro_analytic.mv_ep_ejec_asig_vige  "
						+ " where ejercicio = "+ejercicio;
						str_Query += entidad!=null? " and entidad = '"+entidad+"' " : "";
//						str_Query += unidad_ejecutora!=null? " and unidad_ejecutra = "+unidad_ejecutora : "";
						str_Query += programa!=null? " and programa = "+programa : "";
						str_Query += subprograma!=null? " and subprograma = "+subprograma  : "";
						str_Query += proyecto!=null? " and proyecto = "+proyecto : "";
						str_Query += actividad!=null? " and actividad = "+actividad : "";
						str_Query += renglon!=null? " and renglon = "+renglon : "";
						str_Query += geografico!=null? " and geografico = "+geografico : "";
						str_Query += correlativo!=null? " and correlativo = "+correlativo : "";
						str_Query += organismo!=null? " and organismo = "+organismo : "";
						str_Query += fuente!=null? " and fuente = "+fuente : "";
						str_Query += " and mes = "+mes
						+ " group by entidad "
						+ " ) t1 ";

				PreparedStatement pstm  = conn.prepareStatement(str_Query);
				
                ResultSet rs = pstm.executeQuery();
				
                while(rs!=null && rs.next()){
                	result.set(0, rs.getBigDecimal("asignado"));
                	result.set(1, rs.getBigDecimal("devengado"));
                	result.set(2, rs.getBigDecimal("modificaciones"));
                	break;
                }
                
                rs.close();
                pstm.close();
			}
		}
		catch(Throwable e){
			CLogger.write("20", InformacionPresupuestariaDAO.class, e);
		}
		
		return result;
	}
    
    public static JasperPrint generarJasper(Integer proyectoId, Integer anio, Integer mesPresupuestos, String lineaBase, String usuario) throws JRException, SQLException{
		JasperPrint jasperPrint = null;
		Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
		if (proyecto!=null){
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("proyectoId",proyectoId);
			parameters.put("usuario",usuario);
			
			List<ObjetoCostoJasper> listadoCostos = ObjetoDAO.getEstructuraConCostoJasper(proyectoId, anio, anio, mesPresupuestos, usuario, lineaBase);
			
			parameters.put("costos",listadoCostos);
			jasperPrint = CJasperReport.reporteJasperPrint(CJasperReport.PLANTILLA_EJECUCIONFINANCIERA, parameters);
		}
		return jasperPrint;
	}
}