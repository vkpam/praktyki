import java.sql.ResultSet;
import java.sql.SQLException;

public class VatOperations {
    public static int returnSum() throws SQLException {
        String query = "SELECT SUM(value) FROM vatpayments WHERE date>=" + TimeUtils.getBeginningOfTheYear() + " AND date <=" + TimeUtils.getTodaysDate() + ";";
            ResultSet result = Database.select(query);
            result.next();
            return result.getInt(1); /* in minor */
    }
}
