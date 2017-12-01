package dao;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import pojo.Meta;
import pojo.MetaAvance;
import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CJasperReport;
import utilities.CLogger;
import utilities.CMariaDB;
 
public class PrestamoMetasDAO {
        
    public static ArrayList<ArrayList<BigDecimal>> getMetaValores(Integer metaId, Integer anioInicial, Integer anioFinal, String lineaBase){
    	ArrayList<ArrayList<BigDecimal>> result = new ArrayList<ArrayList<BigDecimal>>();

		try{
			if(CMariaDB.connect()){
				Connection conn = CMariaDB.getConnection();
				String str_Query = String.join(" ", "select valores.ejercicio, SUM(eneroP) eneroP, SUM(eneroR) eneroR, SUM(febreroP) febreroP, SUM(febreroR) febreroR,",
						"SUM(marzoP) marzoP, SUM(marzoR) marzoR, SUM(abrilP) abrilP, SUM(abrilR) abrilR, SUM(mayoP) mayoP, SUM(mayoR) mayoR, SUM(junioP) junioP, SUM(junioR) junioR,",
						"SUM(julioP) julioP, SUM(julioR) julioR, SUM(agostoP) agostoP, SUM(agostoR) agostoR, SUM(septiembreP) septiembreP, SUM(septiembreR) septiembreR, ",
						"SUM(octubreP) octubreP, SUM(octubreR) octubreR, SUM(noviembreP) noviembreP, SUM(noviembreR) noviembreR, SUM(diciembreP) diciembreP, SUM(diciembreR) diciembreR ",
						"from",
						"(select mp.metaid, ejercicio,",
						"CASE  WHEN m.dato_tipoid = 2 THEN mp.enero_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.enero_decimal  END eneroP,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.febrero_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.febrero_decimal END febreroP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.marzo_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.marzo_decimal  END marzoP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.abril_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.abril_decimal  END abrilP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.mayo_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.mayo_decimal  END mayoP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.junio_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.junio_decimal  END junioP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.julio_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.julio_decimal  END julioP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.agosto_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.agosto_decimal  END agostoP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.septiembre_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.septiembre_decimal  END septiembreP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.octubre_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.octubre_decimal  END octubreP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.noviembre_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.noviembre_decimal  END noviembreP ,",
						"CASE WHEN m.dato_tipoid = 2 THEN mp.diciembre_entero",
						"WHEN m.dato_tipoid = 3 THEN mp.diciembre_decimal  END diciembreP ,",
						"0 eneroR, 0 febreroR, 0 marzoR, 0 abrilR, 0 mayoR, 0 junioR, 0 julioR, 0 agostoR, 0 septiembreR, 0 octubreR, 0 noviembreR, 0 diciembreR",
						"from sipro_history.meta_planificado mp",
						"join sipro_history.meta m on mp.metaid = m.id",
						"where metaid = ? ",
						"and ejercicio between ? and ?",
						lineaBase!=null ? " and m.linea_base like '%"+lineaBase+"%' and mp.linea_base like '%"+lineaBase+"%' " : " and m.actual = 1 and mp.actual = 1 ",
						"union",
						"select t1.metaid, t1.anio ejercicio,", 
						"0 eneroP, 0 febreroP, 0 marzoP, 0 abrilP, 0 mayoP, 0 junioP, 0 julioP, 0 agostoP, 0 septiembreP, 0 octubreP, 0 noviembreP, 0 diciembreP,",
						"SUM(case when t1.mes = 1 then t1.valor end) eneroR,", 
						"SUM(case when t1.mes = 2 then t1.valor end) febreroR, ",
						"SUM(case when t1.mes = 3 then t1.valor end) marzoR,", 
						"SUM(case when t1.mes = 4 then t1.valor end) abrilR,", 
						"SUM(case when t1.mes = 5 then t1.valor end) mayoR,", 
						"SUM(case when t1.mes = 6 then t1.valor end) junioR,", 
						"SUM(case when t1.mes = 7 then t1.valor end) julioR,", 
						"SUM(case when t1.mes = 8 then t1.valor end) agostoR,", 
						"SUM(case when t1.mes = 9 then t1.valor end) septiembreR,",
						"SUM(case when t1.mes = 10 then t1.valor end) octubreR,",
						"SUM(case when t1.mes = 11 then t1.valor end) noviembreR, ",
						"SUM(case when t1.mes = 12 then t1.valor end) diciembreR",
						"from (",
						"select ma.metaid, YEAR(ma.fecha) anio, MONTH(ma.fecha) mes,", 
						"CASE", 
						"WHEN m.dato_tipoid = 2 THEN ma.valor_entero",
						"WHEN m.dato_tipoid = 3 THEN ma.valor_decimal",
						"END valor", 
						"from sipro_history.meta_avance ma", 
						"join sipro_history.meta m on ma.metaid = m.id",
						"where ma.metaid = ?",
						"and ma.estado = 1", 
						"and YEAR(ma.fecha) between ? and ?",
						lineaBase!=null ? " and m.linea_base like '%"+lineaBase+"%' and ma.linea_base like '%"+lineaBase+"%' " : " and m.actual = 1 and ma.actual = 1 ",
						") t1",
						"group by t1.anio",
						") valores", 
						"group by ejercicio"
						);
				PreparedStatement pstm  = conn.prepareStatement(str_Query);
				pstm.setInt(1, metaId);
                pstm.setInt(2, anioInicial);
                pstm.setInt(3, anioFinal);
                pstm.setInt(4, metaId);
                pstm.setInt(5, anioInicial);
                pstm.setInt(6, anioFinal);
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
                    result.add(temp);
                }
                
                rs.close();
                pstm.close();
                conn.close();
			}
		}
		catch(Throwable e){
			CLogger.write("1", PrestamoMetasDAO.class, e);
		}
		
		return result;
	}
    
    public static BigDecimal getPorcentajeAvanceMeta(Meta meta, String lineaBase){
    	BigDecimal totalAvance = null;
    	Integer datoTipo = meta.getDatoTipo().getId(); 
    	if(datoTipo.equals(2) || datoTipo.equals(3)){
	    	totalAvance = new BigDecimal(0);
	    	
	    	List<MetaAvance> ret = new ArrayList<MetaAvance>();
			Session session = CHibernateSession.getSessionFactory().openSession();
			try{
				String query = "Select metaid, fecha, usuario, valor_entero, valor_string, valor_decimal, valor_tiempo, estado, fecha_ingreso "
						+ "FROM sipro_history.meta_avance ma "
						+ "where ma.metaid = ?1 ";
				query += lineaBase!=null ? " and ma.linea_base like '%"+lineaBase+"%' " : " and ma.actual = 1 ";
				Query<MetaAvance> criteria = session.createNativeQuery(query, MetaAvance.class);
				criteria.setParameter("1", meta.getId());
				ret = criteria.getResultList();
				
				BigDecimal sumaDecimal = new BigDecimal(0);
	    		Integer sumaEntero = 0;
	    		Iterator<MetaAvance> iterator = ret.iterator();
	    	    while(iterator.hasNext()) {
	    	    	MetaAvance avance = iterator.next();
	    			if(datoTipo.equals(2)){
	    				if(avance.getValorEntero()!=null){
	    					sumaEntero += avance.getValorEntero();
	    				}
	    			}else if(datoTipo.equals(3)){
	    				if(avance.getValorDecimal()!=null){
	    					sumaDecimal = sumaDecimal.add(avance.getValorDecimal());
	    				}
	    			}
	    		}
	    		Integer metaFinalEntero = meta.getMetaFinalEntero()!=null ? meta.getMetaFinalEntero() : 0;
	    		BigDecimal metaFinalDecimal = meta.getMetaFinalDecimal()!=null ? meta.getMetaFinalDecimal() : new BigDecimal(0);
	    		if(datoTipo.equals(2) && metaFinalEntero.compareTo(0)!=0){
	    			BigDecimal total = new BigDecimal(sumaEntero); 
	    			total = (total.divide(new BigDecimal(metaFinalEntero), 3, BigDecimal.ROUND_HALF_UP));
    				total = total.multiply(new BigDecimal(100));
    				totalAvance = total.setScale(2, BigDecimal.ROUND_HALF_UP);
    			}else if(datoTipo.equals(3)&& metaFinalDecimal.compareTo(new BigDecimal(0))!=0){
    				BigDecimal total = (sumaDecimal.divide(metaFinalDecimal, 3, BigDecimal.ROUND_HALF_UP));
    				total = total.multiply(new BigDecimal(100));
    				totalAvance = total.setScale(2, BigDecimal.ROUND_HALF_UP);
    			}
			}
			catch(Throwable e){
				CLogger.write("2", PrestamoMetasDAO.class, e);
			}
			finally{
				session.close();
			}
    	}
    	return totalAvance;
    }
    
    public static JasperPrint generarJasper(Integer proyectoId, String usuario) throws JRException, SQLException{
		JasperPrint jasperPrint = null;
		Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
		if (proyecto!=null){

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("proyectoId",proyectoId);
			parameters.put("usuario",usuario);
			jasperPrint = CJasperReport.reporteJasperPrint(CJasperReport.PLANTILLA_METAS, parameters);
		}
		return jasperPrint;
	}
    
}