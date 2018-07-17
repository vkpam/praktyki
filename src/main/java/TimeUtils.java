import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static String getTodaysDate() {
        java.time.LocalDate localDate1 = java.time.LocalDate.now();
        String date = localDate1.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return date;
    }
}