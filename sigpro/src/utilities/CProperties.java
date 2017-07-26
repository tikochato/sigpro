package utilities;

import java.io.InputStream;
import java.util.Properties;

public class CProperties {
	private static Properties properties;
	private static String memsql_host="";
	private static Integer memsql_port=null;
	private static String memsql_user="";
	private static String memsql_password="";
	private static String memsql_schema=null;
	private static String memsql_schema_des=null;
	
	private static String oracle_host="";
	private static Integer oracle_port=null;
	private static String oracle_user="";
	private static String oracle_password="";
	private static String oracle_schema=null;
	
	private static String maria_host="";
	private static Integer maria_port=null;
	private static String maria_user="";
	private static String maria_password="";
	private static String maria_schema=null;
	
	
	static{
		InputStream input;
		properties = new Properties();
		input = CProperties.class.getClassLoader().getResourceAsStream("config.properties");
		try{
			properties.load(input);
			memsql_host = properties.getProperty("memsql_host");
			memsql_port = properties.getProperty("memsql_port") != null ? 
					Integer.parseInt(properties.getProperty("memsql_port")) : null;
			memsql_schema = properties.getProperty("memsql_schema");
			memsql_schema_des = properties.getProperty("memsql_schemades");
			memsql_user = properties.getProperty("memsql_user");
			memsql_password = properties.getProperty("memsql_password");
			
			oracle_host = properties.getProperty("oracle_host");
			oracle_port = properties.getProperty("oracle_port") != null ? 
					Integer.parseInt(properties.getProperty("oracle_port")) : null;
			oracle_schema = properties.getProperty("oracle_schema");
			oracle_user = properties.getProperty("oracle_user");
			oracle_password = properties.getProperty("oracle_password");
			
			maria_host = properties.getProperty("maria_host");
			maria_port = properties.getProperty("maria_port") != null ? 
					Integer.parseInt(properties.getProperty("maria_port")) : null;
			maria_schema = properties.getProperty("maria_schema");
			maria_user = properties.getProperty("maria_user");
			maria_password = properties.getProperty("maria_password");
			
			
		}
		catch(Throwable e){
			CLogger.write("1", CProperties.class, e);
		}
		finally{
			
		}
	}

	public static String getOracle_host() {
		return oracle_host;
	}

	public static void setOracle_host(String oracle_host) {
		CProperties.oracle_host = oracle_host;
	}

	public static Integer getOracle_port() {
		return oracle_port;
	}

	public static void setOracle_port(Integer oracle_port) {
		CProperties.oracle_port = oracle_port;
	}

	public static String getOracle_user() {
		return oracle_user;
	}

	public static void setOracle_user(String oracle_user) {
		CProperties.oracle_user = oracle_user;
	}

	public static String getOracle_password() {
		return oracle_password;
	}

	public static void setOracle_password(String oracle_password) {
		CProperties.oracle_password = oracle_password;
	}

	public static String getOracle_schema() {
		return oracle_schema;
	}

	public static void setOracle_schema(String oracle_schema) {
		CProperties.oracle_schema = oracle_schema;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		CProperties.properties = properties;
	}

	public static String getmemsql_host() {
		return memsql_host;
	}

	public static void setmemsql_host(String memsql_host) {
		CProperties.memsql_host = memsql_host;
	}

	public static Integer getmemsql_port() {
		return memsql_port;
	}

	public static void setmemsql_port(Integer memsql_port) {
		CProperties.memsql_port = memsql_port;
	}

	public static String getmemsql_user() {
		return memsql_user;
	}

	public static void setmemsql_user(String memsql_user) {
		CProperties.memsql_user = memsql_user;
	}

	public static String getmemsql_password() {
		return memsql_password;
	}

	public static void setmemsql_password(String memsql_password) {
		CProperties.memsql_password = memsql_password;
	}

	public static String getmemsql_schema(){
		return memsql_schema;
	}
	
	public void setmemsql_schema(String memsql_schema){
		CProperties.memsql_schema = memsql_schema;
	}
	
	public static String getmemsql_schema_des(){
		return memsql_schema_des;
	}
	
	public void setmemsql_schema_des(String memsql_schema_des){
		CProperties.memsql_schema_des = memsql_schema_des;
	}

	public static String getMemsql_host() {
		return memsql_host;
	}

	public static void setMemsql_host(String memsql_host) {
		CProperties.memsql_host = memsql_host;
	}

	public static String getMaria_host() {
		return maria_host;
	}

	public static void setMaria_host(String maria_host) {
		CProperties.maria_host = maria_host;
	}

	public static Integer getMaria_port() {
		return maria_port;
	}

	public static void setMaria_port(Integer maria_port) {
		CProperties.maria_port = maria_port;
	}

	public static String getMaria_user() {
		return maria_user;
	}

	public static void setMaria_user(String maria_user) {
		CProperties.maria_user = maria_user;
	}

	public static String getMaria_password() {
		return maria_password;
	}

	public static void setMaria_password(String maria_password) {
		CProperties.maria_password = maria_password;
	}

	public static String getMaria_schema() {
		return maria_schema;
	}

	public static void setMaria_schema(String maria_schema) {
		CProperties.maria_schema = maria_schema;
	}
}
