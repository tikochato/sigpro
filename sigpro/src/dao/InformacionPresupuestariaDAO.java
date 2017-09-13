package dao;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utilities.CLogger;
 
public class InformacionPresupuestariaDAO {
		
	public static String getConsultaEstructuraArbol(int idPrestamo){
		String str_Query = String.join(" ", "select * from (",
				" select p.id prestamo, null componente, null producto, null subproducto, null actividad, p.nombre, 1 objeto_tipo,  p.treePath, p.nivel, p.fecha_inicio,",
				" p.fecha_fin, p.duracion, p.duracion_dimension,p.costo,0",
				" from proyecto p",
				" where p.id= ",String.valueOf(idPrestamo)," and p.estado=1",
				" union",
				" select c.proyectoid prestamo, c.id componente, null producto, null subproducto, null actividad, c.nombre, 2 objeto_tipo,  c.treePath, c.nivel, c.fecha_inicio,",
				" c.fecha_fin , c.duracion, c.duracion_dimension,c.costo,0",
				" from componente c",
				" where c.proyectoid=",String.valueOf(idPrestamo)," and c.estado=1",
				" union",
				" select p.id prestamo, c.id componente, pr.id producto, null subproducto, null actividad, pr.nombre, 3 objeto_tipo , pr.treePath, pr.nivel, pr.fecha_inicio,",
				" pr.fecha_fin, pr.duracion, pr.duracion_dimension,pr.costo,0",
				" from producto pr",
				" left outer join componente c on c.id=pr.componenteid",
				" left outer join proyecto p on p.id=c.proyectoid",
				" where p.id= ",String.valueOf(idPrestamo)," and p.estado=1 and c.estado=1 and pr.estado=1",
				" union",
				" select p.id prestamo, c.id componente, pr.id producto, sp.id subproducto, null actividad,  sp.nombre, 4 objeto_tipo,  sp.treePath, sp.nivel, sp.fecha_inicio,",
				" sp.fecha_fin , sp.duracion, sp.duracion_dimension,sp.costo,0",
				" from subproducto sp",
				" left outer join producto pr on pr.id=sp.productoid",
				" left outer join componente c on c.id=pr.componenteid",
				" left outer join proyecto p on p.id=c.proyectoid",
				" where p.id= ",String.valueOf(idPrestamo)," and p.estado=1 and c.estado=1 and pr.estado=1 and sp.estado=1",
				" union",
				" select p.id proyecto, null componente, null producto, null subproducto, a.id actividad, a.nombre, 5 objeto_tipo,  a.treePath, a.nivel, a.fecha_inicio,",
				" a.fecha_fin , a.duracion, a.duracion_dimension,a.costo,a.pred_objeto_id",
				" from actividad a",
				" left outer join proyecto p on p.id=a.proyecto_base",
				" where p.id= ",String.valueOf(idPrestamo)," and a.estado=1 and p.estado=1",
				" ) arbol",
				" order by treePath"
				);
		
		return str_Query;
	}
	
	public static ArrayList<Date> getEstructuraArbolPrestamoFecha(int idPrestamo, Connection conn){
    	ArrayList<Date> ret = new ArrayList<Date>();
        try {
            if( !conn.isClosed() ){
                try{
                	String str_Query = String.join(" ","select fecha_inicio, fecha_fin ",
                			"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
                            "where prestamo = ? ",
                            "and componente  is null",
                            "and producto  is null",
                            "and subproducto  is null",
                            "and actividad is null;"
                            );
                    
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
                    e.printStackTrace();
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
                    String str_Query = String.join(" ","select componente, min(fecha_inicio) fecha ",
                    		"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
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
                    e.printStackTrace();
                    CLogger.write("1", InformacionPresupuestariaDAO.class, e);
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
                    String str_Query = String.join(" ","select fecha_inicio,fecha_fin",
                    		"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ?",
                            "and producto  is null",
                            "and subproducto  is null",
                            "and actividad is null;"
                    		);
                    
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
                    e.printStackTrace();
                    CLogger.write("1", InformacionPresupuestariaDAO.class, e);
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
                    String str_Query = String.join(" ","select producto, fecha_inicio",
                    		"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto is not null",
                            "and subproducto  is null",
                            "and actividad is null;"
                    		);
                    
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
                    CLogger.write("2", InformacionPresupuestariaDAO.class, e);
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
                    String str_Query = String.join(" ","select producto, fecha_inicio, fecha_fin ",
                    		"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ?",
                            "and subproducto  is null",
                            "and actividad is null;"
                    		);
                    
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
                    e.printStackTrace();
                    CLogger.write("2", InformacionPresupuestariaDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("2", InformacionPresupuestariaDAO.class, e);
        }
        return ret;
    }
    
    public static ArrayList<Integer> getEstructuraArbolSubProducto(int idPrestamo,int idComponente, int idProducto, Connection conn){
    	ArrayList<Integer> ret = new ArrayList<Integer>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select subproducto,fecha_inicio",
                    		"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ? ",
                            "and subproducto is not null",
                            "and actividad is null;"
                            );
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
                    e.printStackTrace();
                    CLogger.write("3", InformacionPresupuestariaDAO.class, e);
                }            
            }
        } catch (SQLException e) {
            CLogger.write("3", InformacionPresupuestariaDAO.class, e);
        }
        
        
        return ret;
    }
    
    public static ArrayList<Date> getEstructuraArbolSubProductoFecha(int idPrestamo,int idComponente, int idProducto, int idSubProducto, Connection conn){
    	ArrayList<Date> ret = new ArrayList<Date>();
        try {
            if( !conn.isClosed() ){
                try{
                    String str_Query = String.join(" ","select fecha_inicio, fecha_fin",
                    		"from (",getConsultaEstructuraArbol(idPrestamo),") estructura_arbol",
                            "where prestamo = ? ",
                            "and componente = ? ",
                            "and producto = ? ",
                            "and subproducto = ?",
                            "and actividad is null;");
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
                    CLogger.write("3", InformacionPresupuestariaDAO.class, e);
                }            
            }
        } catch (SQLException e) {
            CLogger.write("3", InformacionPresupuestariaDAO.class, e);
        }
        
        
        return ret;
    }
    
    public static ArrayList<ArrayList<Integer>> getEstructuraArbolActividades(int idPrestamo, String treePath, Connection conn){
    	ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        try {
            if( !conn.isClosed()){
                try{
                	String str_treePathInicial = String.join("",treePath, "000000");
                	String str_treePathFinal = String.join("",treePath, "999999");
                    String str_Query = String.join(" ","select * from (",
            				" select p.id prestamo, null componente, null producto, null subproducto, null actividad, p.nombre, 1 objeto_tipo,  p.treePath, p.nivel, p.fecha_inicio,",
            				" p.fecha_fin, p.duracion, p.duracion_dimension,p.costo,0",
            				" from proyecto p",
            				" where p.id= ",String.valueOf(idPrestamo)," and p.estado=1",
            				" union",
            				" select c.proyectoid prestamo, c.id componente, null producto, null subproducto, null actividad, c.nombre, 2 objeto_tipo,  c.treePath, c.nivel, c.fecha_inicio,",
            				" c.fecha_fin , c.duracion, c.duracion_dimension,c.costo,0",
            				" from componente c",
            				" where c.proyectoid=",String.valueOf(idPrestamo)," and c.estado=1",
            				" union",
            				" select p.id prestamo, c.id componente, pr.id producto, null subproducto, null actividad, pr.nombre, 3 objeto_tipo , pr.treePath, pr.nivel, pr.fecha_inicio,",
            				" pr.fecha_fin, pr.duracion, pr.duracion_dimension,pr.costo,0",
            				" from producto pr",
            				" left outer join componente c on c.id=pr.componenteid",
            				" left outer join proyecto p on p.id=c.proyectoid",
            				" where p.id= ",String.valueOf(idPrestamo)," and p.estado=1 and c.estado=1 and pr.estado=1",
            				" union",
            				" select p.id prestamo, c.id componente, pr.id producto, sp.id subproducto, null actividad,  sp.nombre, 4 objeto_tipo,  sp.treePath, sp.nivel, sp.fecha_inicio,",
            				" sp.fecha_fin , sp.duracion, sp.duracion_dimension,sp.costo,0",
            				" from subproducto sp",
            				" left outer join producto pr on pr.id=sp.productoid",
            				" left outer join componente c on c.id=pr.componenteid",
            				" left outer join proyecto p on p.id=c.proyectoid",
            				" where p.id= ",String.valueOf(idPrestamo)," and p.estado=1 and c.estado=1 and pr.estado=1 and sp.estado=1",
            				" union",
            				" select p.id proyecto, null componente, null producto, null subproducto, a.id actividad, a.nombre, 5 objeto_tipo,  a.treePath, a.nivel, a.fecha_inicio,",
            				" a.fecha_fin , a.duracion, a.duracion_dimension,a.costo,a.pred_objeto_id",
            				" from actividad a",
            				" left outer join proyecto p on p.id=a.proyecto_base",
            				" where p.id= ",String.valueOf(idPrestamo)," and a.estado=1 and p.estado=1",
            				" ) arbol",
            				" where treePath > ",str_treePathInicial," and treePath < ",str_treePathFinal," ",
            				" and actividad is not null",
            				" order by treePath"
            				);
                    
                    PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    pstm.setFetchSize(50);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(rs.getInt("actividad"));
                        ret.add(temp);
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
            CLogger.write("4", InformacionPresupuestariaDAO.class, e);
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
			CLogger.write("8", ReporteDAO.class, e);
		}
		
		return result;
	}
    
    public static ArrayList<ArrayList<BigDecimal>> getPresupuestoPorObjeto(Integer fuente, Integer organismo, Integer correlativo, Integer anoInicial, Integer anoFinal, Integer programa, Integer subprograma, 
    		Integer proyecto, Integer actividad, Integer obra, Connection conn){
    	ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();
		//Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			if(!conn.isClosed()){
				if(programa != null && programa >=0){
					String str_Query = String.join(" ", "select enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre, ejercicio from mv_ep_estructura",
						"where fuente=? and organismo=? and correlativo=? and programa=? and subprograma=? and proyecto=? and actividad=? and obra=?",
						"and ejercicio between ? and ?");
					
					PreparedStatement pstm  = conn.prepareStatement(str_Query);
					pstm.setFetchSize(1000);
	                pstm.setInt(1, fuente);
	                pstm.setInt(2, organismo);
	                pstm.setInt(3, correlativo);
	                pstm.setInt(4, programa);
	                pstm.setInt(5, subprograma);
	                pstm.setInt(6, proyecto);
	                pstm.setInt(7, actividad);
	                pstm.setInt(8, obra);
	                pstm.setInt(9, anoInicial);
	                pstm.setInt(10, anoFinal);
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
			CLogger.write("6", ReporteDAO.class, e);
		}
		
		return result;
	}
}