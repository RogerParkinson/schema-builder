
package nz.co.senanque.schemabuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JAXBDataAdapter
{
    public static Date parseDate(String value) {
        Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(value);
        return cal.getTime();
    }

    public static String printDate(Date value) {
        if (value == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(value);
        return (javax.xml.bind.DatatypeConverter.printDate(cal));
    }

}
