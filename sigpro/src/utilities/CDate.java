package utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CDate {
	public static String formatTimestamp(Timestamp time){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
		return (time!=null) ? sdf.format(new Date(time.getTime())) : null;
	}
}
