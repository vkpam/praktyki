import java.sql.ResultSet;
import java.sql.SQLException;

public class Configuration {
    public static String getStringParameter(String paramName) throws SQLException {
        String query = "SELECT paramvalue FROM configuration WHERE paramname = '" + paramName + "');";
        ResultSet result = Database.select(query);
        result.next();
        return result.getString(1);
    }

    public static int getIntegerParameter(String paramName) throws SQLException {
        return Integer.parseInt(getStringParameter(paramName));
    }
}
