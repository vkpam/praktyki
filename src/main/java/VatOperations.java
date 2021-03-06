import java.sql.ResultSet;
        import java.sql.SQLException;
import java.util.List;

public class VatOperations {
    public static int returnSum() throws SQLException {
        String query = "SELECT SUM(value) FROM vatPayments WHERE date>= '" + TimeUtils.getBeginningOfTheYear() + "' AND date <= '" + TimeUtils.getTodaysDate() + "';";
        ResultSet result = Database.select(query);
        result.next();
        int sum = result.getInt(1);
        return sum;
    }

    public static int vatCount() throws SQLException {
        String query = "SELECT invoiceid FROM invoices WHERE selldate >='" + TimeUtils.getBeginningOfTheYear() + "' AND selldate <= '" + TimeUtils.getTodaysDate() + "';";
        int sum = 0;
        ResultSet result = Database.select(query);
        while (result.next()) {
            String invoiceNumber = result.getString("invoiceid");
            List<Product> products = SaveInvoiceToFile.getProductsFromDatabase(invoiceNumber);
            for (int i = 0; i < products.size(); ++i) {
                Product p = products.get(i);
                int vatValue = p.getNetPrice() * p.getCount() * p.getVatRate() / 100;
                sum += vatValue;
            }
        }
        return sum;
    }

    public static int savedVat() throws SQLException {
        String query = "SELECT vatvalue, iscarrelated FROM costs WHERE date >='" + TimeUtils.getBeginningOfTheYear() + "' AND date <= '" + TimeUtils.getTodaysDate() + "';";
        int savedVatSum = 0;
        ResultSet result = Database.select(query);

        while (result.next()) {
            int isCarRelated = result.getInt("iscarrelated");
            int vatValue = result.getInt("vatValue");
            int savedVat = Costs.getSavedVat(isCarRelated, vatValue);
            savedVatSum += savedVat;
        }
        return savedVatSum;
    }
}