package cl.deloitte.wsib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jinostrozau on 2017-06-27.
 */
public class DateUtils {

    public static String dateFormat(Date date){

        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static Date getDateYesterday(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        date.setTime( c.getTime().getTime() );
        return date;
    }
}