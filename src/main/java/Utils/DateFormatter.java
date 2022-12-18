package Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private Date _date;

    public DateFormatter(String date) throws ParseException {
        Integer year = Year.now().getValue();
        if(date.length() == 5)
            date += " 00";
        DateFormat format = new SimpleDateFormat("MM dd HH yyyy", Locale.ENGLISH);
        date += " " + year.toString();
        this._date = format.parse(date);
    }

    public Date getDate() {
        return this._date;
    }
}
