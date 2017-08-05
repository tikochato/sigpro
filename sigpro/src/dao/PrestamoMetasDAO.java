package dao;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utilities.CLogger;
 
public class PrestamoMetasDAO {
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
                    e.printStackTrace();
                    CLogger.write("1", PrestamoMetasDAO.class, e);
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
                    CLogger.write("2", PrestamoMetasDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("2", PrestamoMetasDAO.class, e);
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
                    e.printStackTrace();
                    CLogger.write("3", PrestamoMetasDAO.class, e);
                }            
            }
        } catch (SQLException e) {
            CLogger.write("3", PrestamoMetasDAO.class, e);
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
                    e.printStackTrace();
                    CLogger.write("4", PrestamoMetasDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("4", PrestamoMetasDAO.class, e);
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
                    e.printStackTrace();
                    CLogger.write("5", PrestamoMetasDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("5", PrestamoMetasDAO.class, e);
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
                    e.printStackTrace();
                    CLogger.write("6",PrestamoMetasDAO.class, e);
                }
                
            }
        } catch (SQLException e) {
            CLogger.write("6", PrestamoMetasDAO.class, e);
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
                    e.printStackTrace();
                    CLogger.write("7",PrestamoMetasDAO.class, e);
                }
            }
        } catch (SQLException e) {
            CLogger.write("7", PrestamoMetasDAO.class, e);
        }
        
        return ret;
    }
    
    public static ArrayList<ArrayList<BigDecimal>> getMetasPorProducto(Integer producto, Integer anoInicial, Integer anoFinal, Connection conn){
    	ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();

		try{
			if(!conn.isClosed() ){
				String str_Query = String.join(" ", "select ejercicio, objeto_id, objeto_tipo, meta_unidad_medidaid, eneroP, eneroR, febreroP, febreroR, marzoP, marzoR, abrilP, abrilR, mayoP, mayoR, junioP, junioR, julioP, julioR, agostoP, agostoR, septiembreP, septiembreR, octubreP, octubreR, noviembreP, noviembreR, diciembreP, diciembreR, lineaBase, metaFinal ",
						"from mv_ep_metas where ",
						"objeto_id=? and ",
						"objeto_tipo=? and",
						"ejercicio between ? and ? ");
				PreparedStatement pstm  = conn.prepareStatement(str_Query);
				pstm.setFetchSize(1000);
                pstm.setInt(1, producto);
                pstm.setInt(2, 3);
                pstm.setInt(3, anoInicial);
                pstm.setInt(4, anoFinal);
                ResultSet rs = pstm.executeQuery();
                
                while(rs!=null && rs.next()){
                    ArrayList<BigDecimal> temp = new ArrayList<BigDecimal>();
            		temp.add(rs.getBigDecimal("eneroP"));
            		temp.add(rs.getBigDecimal("eneroR"));
                    temp.add(rs.getBigDecimal("febreroP"));
                    temp.add(rs.getBigDecimal("febreroR"));
                    temp.add(rs.getBigDecimal("marzoP"));
                    temp.add(rs.getBigDecimal("marzoR"));
                    temp.add(rs.getBigDecimal("abrilP"));
                    temp.add(rs.getBigDecimal("abrilR"));
                    temp.add(rs.getBigDecimal("mayoP"));
                    temp.add(rs.getBigDecimal("mayoR"));
                    temp.add(rs.getBigDecimal("junioP"));
                    temp.add(rs.getBigDecimal("junioR"));
                    temp.add(rs.getBigDecimal("julioP"));
                    temp.add(rs.getBigDecimal("julioR"));
                    temp.add(rs.getBigDecimal("agostoP"));
                    temp.add(rs.getBigDecimal("agostoR"));
                    temp.add(rs.getBigDecimal("septiembreP"));
                    temp.add(rs.getBigDecimal("septiembreR"));
                    temp.add(rs.getBigDecimal("octubreP"));
                    temp.add(rs.getBigDecimal("octubreR"));
                    temp.add(rs.getBigDecimal("noviembreP"));
                    temp.add(rs.getBigDecimal("noviembreR"));
                    temp.add(rs.getBigDecimal("diciembreP"));
                    temp.add(rs.getBigDecimal("diciembreR"));
                    temp.add(new BigDecimal(rs.getInt("ejercicio")));
                    temp.add(new BigDecimal(rs.getInt("meta_unidad_medidaid")));
                    temp.add(new BigDecimal(rs.getInt("lineaBase")));
                    temp.add(new BigDecimal(rs.getInt("metaFinal")));
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
}