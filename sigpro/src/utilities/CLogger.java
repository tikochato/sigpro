package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLogger {

	private static Logger log;
	
	public CLogger(){
		
	}
	
	static public void write(String str, Object obj, Throwable e){
		log=LoggerFactory.getLogger(obj.getClass());
		log.error(str,e);
	}
	
	static public void write_simple(String error_num, Object obj, String error){
		log=LoggerFactory.getLogger(obj.getClass());
		log.error(String.join(" ",obj.toString(), error_num,"\n"+error));		
	}	
}
