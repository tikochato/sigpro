package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import utilities.CLogger;
import utilities.CProperties;

public class CMariaDB {
	private static Connection connection;
	private static String host;
	private static Integer port; 
	private static String user;
	private static String password;
	private static String schema;
	
	
	private static Statement st;
	
	static {		
		host = CProperties.getMaria_host();
		port = CProperties.getMaria_port();
		user = CProperties.getMaria_user();
		password = CProperties.getMaria_password();
		schema = CProperties.getMaria_schema();
		
	}
	
	public static boolean connect(){
		try{
			Class.forName("org.mariadb.jdbc.Driver").newInstance();
			connection=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+schema+"?" +
                    "user="+user+"&password="+password);
			if(!connection.isClosed())
				return true;
		}
		catch(Exception e){
			try{
				host = "localhost";
				Class.forName("org.mariadb.jdbc.Driver").newInstance();
				connection=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+schema+"?" +
	                    "user="+user+"&password="+password);
				if(!connection.isClosed())
					return true;
				
			}catch(Exception ee){
				CLogger.writeFullConsole("Error 1 : CMariaDB.class ", ee);
			}
			
			
			
		}
		return false;
	}
	
	public static Connection getConnection(){
		return connection;
	}
	

	public static void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			CLogger.writeFullConsole("Error 3 : CMariaDB.class ", e);
		}
	}
	
	public static long getNextID(Connection connection,String table){
		long ret = -1;
		try{
			PreparedStatement stm = connection.prepareStatement("SELECT last_id FROM uid WHERE table_name=?");
			stm.setString(1, table);
			ResultSet rs = stm.executeQuery();
			if(rs.next()){
				ret = rs.getLong("last_id");
			}
		}
		catch(Exception e){
			CLogger.writeFullConsole("Error 4: CMariaDB.class", e);
		}
		return ret;
	}
	
	
	public static boolean saveLastID(Connection connection, String table, long last_id){
		boolean ret = false;
		try{
			PreparedStatement stm = connection.prepareStatement("UPDATE uid SET last_id=? WHERE table_name=?");
			stm.setLong(1, last_id);
			stm.setString(2, table);
			if(stm.executeUpdate()>0)
				ret = true;
		}
		catch(Exception e){
			CLogger.writeFullConsole("Error 5: CMariaDB.class", e);
		}
		return ret;
	}
	
	public static ResultSet runQuery(String query){
		ResultSet ret=null;
		try{
			if(!connection.isClosed()){
				st = connection.createStatement();
				ret = st.executeQuery(query);
			}
		}
		catch(Exception e){
			CLogger.writeFullConsole("Error 6: CMariaDB.class", e);
		}
		return ret;
	}

}
