package utilities;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class CHistoria {

	
	public CHistoria() {
		
	}
	
	public static String getHistoria(String query, String[] campos){
		String resultado = "";
		if(query!=null && !query.isEmpty() && campos!=null && campos.length>0){
			List<?> datos  = getDatos(query);
			for(int d=0; d<datos.size(); d++){
				Object[] dato = (Object[])datos.get(d);
				if(!resultado.isEmpty()){
					resultado+=", ";
				}
				resultado += "[";
				String objeto = "";
				for(int c=0; c<campos.length; c++){
					if(!objeto.isEmpty()){
						objeto+=", ";
					}
					objeto += "{\"nombre\": \""+campos[c] + "\", \"valor\": \"" + (dato[c]!=null?(String)dato[c]:"") + "\"}";					
				}
				resultado += objeto + "]";
			}
		}
		resultado="["+resultado+"]";
		return resultado;
	}

	public static List<?> getDatos(String query){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{	
			Query<?> criteria = session.createNativeQuery(query);
			ret = criteria.getResultList();			
		}
		catch(Throwable e){
			CLogger.write("2", CHistoria.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
