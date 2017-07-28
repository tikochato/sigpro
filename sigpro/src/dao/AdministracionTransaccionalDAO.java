package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utilities.CLogger;

public class AdministracionTransaccionalDAO {
	public List<List<String>> obtenerUsuarios(Connection conn){
		List<List<String>> ret = new ArrayList<List<String>>();
		List<String> result = new ArrayList<String>();
		try {
            if(!conn.isClosed() ){
                try{
                	String str_Query = String.join(" ","SELECT Q1.usuario, Q1.creacion, Q2.actualizacion FROM (",
                			"select usuario, sum(creacion) creacion from (",
                			"select usuario, count(p.id) creacion from usuario u",
                			"left join proyecto p on (p.usuario_creo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(c.id) creacion from usuario u",
                			"left outer join componente c on (c.usuario_creo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(pr.id) creacion from usuario u",
                			"left outer join producto pr on (pr.usuario_creo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(sp.id) creacion from usuario u",
                			"left outer join subproducto sp on (sp.usuario_creo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(a.id) creacion from usuario u",
                			"left outer join actividad a on (a.usuario_creo=u.usuario)",
                			"group by u.usuario) t1 group by t1.usuario) Q1",
                			"JOIN(",
                			"select usuario, sum(actualizacion) actualizacion from (",
                			"select usuario, count(p.id) actualizacion from usuario u",
                			"left join proyecto p on (p.usuario_actualizo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(c.id) actualizacion from usuario u",
                			"left outer join componente c on (c.usuario_actualizo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(pr.id) actualizacion from usuario u",
                			"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(sp.id) actualizacion from usuario u",
                			"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
                			"group by u.usuario",
                			"union",
                			"select usuario, count(a.id) actualizacion from usuario u",
                			"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
                			"group by u.usuario) t2 group by t2.usuario) Q2 on Q1.usuario=Q2.usuario");
                	
                	PreparedStatement pstm  = conn.prepareStatement(str_Query);
                    ResultSet rs = pstm.executeQuery();
                    
                    while(rs!=null && rs.next()){
                    	result = new ArrayList<String>();
                    	result.add(rs.getString("usuario"));
                    	result.add(rs.getString("creacion"));
                    	result.add(rs.getString("actualizacion"));
                    	ret.add(result);
                    }
                    
                    rs.close();
                    pstm.close();
                }catch(Throwable e){
                    e.printStackTrace();
                    CLogger.write("1", AdministracionTransaccionalDAO.class, e);
                }
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
		
		return ret;
	}
}
