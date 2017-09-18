package dao;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utilities.CLogger;
import utilities.CMariaDB;
 
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
    
    public static ArrayList<ArrayList<BigDecimal>> getMetasPorProducto(Integer producto, Integer anoInicial, Integer anoFinal){
    	ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();

		try{
			if(CMariaDB.connect()){
				Connection conn = CMariaDB.getConnection();
				String str_Query = String.join(" ", "select t1.anio ejercicio, t1.objeto_id, t1.objeto_tipo, t1.meta_unidad_medidaid, ",
						"SUM(case when t1.mes = 1 and t1.meta_tipoid = 1 then t1.valor end) eneroP, ",
						"SUM(case when t1.mes = 1 and t1.meta_tipoid = 2 then t1.valor end) eneroR, ",
						"SUM(case when t1.mes = 2 and t1.meta_tipoid = 1 then t1.valor end) febreroP, ",
						"SUM(case when t1.mes = 2 and t1.meta_tipoid = 2 then t1.valor end) febreroR, ",
						"SUM(case when t1.mes = 3 and t1.meta_tipoid = 1 then t1.valor end) marzoP, ",
						"SUM(case when t1.mes = 3 and t1.meta_tipoid = 2 then t1.valor end) marzoR, ",
						"SUM(case when t1.mes = 4 and t1.meta_tipoid = 1 then t1.valor end) abrilP, ",
						"SUM(case when t1.mes = 4 and t1.meta_tipoid = 2 then t1.valor end) abrilR, ",
						"SUM(case when t1.mes = 5 and t1.meta_tipoid = 1 then t1.valor end) mayoP, ",
						"SUM(case when t1.mes = 5 and t1.meta_tipoid = 2 then t1.valor end) mayoR, ",
						"SUM(case when t1.mes = 6 and t1.meta_tipoid = 1 then t1.valor end) junioP, ",
						"SUM(case when t1.mes = 6 and t1.meta_tipoid = 2 then t1.valor end) junioR, ",
						"SUM(case when t1.mes = 7 and t1.meta_tipoid = 1 then t1.valor end) julioP, ",
						"SUM(case when t1.mes = 7 and t1.meta_tipoid = 2 then t1.valor end) julioR, ",
						"SUM(case when t1.mes = 8 and t1.meta_tipoid = 1 then t1.valor end) agostoP, ",
						"SUM(case when t1.mes = 8 and t1.meta_tipoid = 2 then t1.valor end) agostoR, ",
						"SUM(case when t1.mes = 9 and t1.meta_tipoid = 1 then t1.valor end) septiembreP, ",
						"SUM(case when t1.mes = 9 and t1.meta_tipoid = 2 then t1.valor end) septiembreR, ",
						"SUM(case when t1.mes = 10 and t1.meta_tipoid = 1 then t1.valor end) octubreP, ",
						"SUM(case when t1.mes = 10 and t1.meta_tipoid = 2 then t1.valor end) octubreR, ",
						"SUM(case when t1.mes = 11 and t1.meta_tipoid = 1 then t1.valor end) noviembreP, ",
						"SUM(case when t1.mes = 11 and t1.meta_tipoid = 2 then t1.valor end) noviembreR, ",
						"SUM(case when t1.mes = 12 and t1.meta_tipoid = 1 then t1.valor end) diciembreP, ",
						"SUM(case when t1.mes = 12 and t1.meta_tipoid = 2 then t1.valor end) diciembreR, ",
						"SUM(case when t1.meta_tipoid = 3 then t1.valor end) lineaBase, ",
						"SUM(case when t1.meta_tipoid = 4 then t1.valor end) metaFinal ",
						"from ",
						"( ",
						"select m.objeto_id, m.objeto_tipo, m.meta_tipoid, m.meta_unidad_medidaid, YEAR(mv.fecha) anio, MONTH(mv.fecha) mes, ",
						"mv.valor_decimal valor ",
						"from meta m ",
						"join meta_valor mv on m.id = mv.metaid ",
						"where m.objeto_id = ? and m.objeto_tipo = ? ",
						"and m.estado = 1 ",
						"and (mv.estado = 1 or mv.estado = 2) ",
						") t1 ",
						"where t1.anio between ? and ? ",
						"group by t1.anio ");
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
                conn.close();
			}
		}
		catch(Throwable e){
			CLogger.write("8", ReporteDAO.class, e);
		}
		
		return result;
	}
}