import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class TimeUtils {
    public static String getTodaysDate() {
        LocalDate localDate1 = LocalDate.now();
        String date = localDate1.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return date;
    }

    public static String getBeginningOfTheYear() {
        LocalDate localDate1 = LocalDate.now();
        return localDate1.getYear() + "-01-01";
    }
}