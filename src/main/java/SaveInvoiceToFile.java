import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class SaveInvoiceToFile {

    public static void save() {
        Invoices.showAll();
        System.out.print("\nType invoice number (not ID) to save: ");
        Scanner reading = new Scanner(System.in);
        String invoiceNumber = reading.nextLine();

        try {

            String fileContent = FileUtils.getFileContentAsString("Invoice_template.html");
            String[] fileParts = fileContent.split("<!--SEPARATOR-->");
            List<Product> products = getProductsFromDatabase(invoiceNumber);
            String invoiceContent = fileParts[0];

            int netSum = 0, vatSum = 0, grossSum = 0;
            for (int i = 0; i < products.size(); ++i) {

                Product p = products.get(i);
                netSum += p.getNetValue();
                vatSum += p.getVatValue();
                grossSum += p.getGrossValue();
                String productRow = replaceTagsWithRealData(fileParts[1], p, i+1);
                invoiceContent += productRow;
            }

            invoiceContent += fileParts[2];
            invoiceContent = invoiceContent.replace("{suma netto}", formatIntMinorToString(netSum));
            invoiceContent = invoiceContent.replace("{suma vat}", formatIntMinorToString(vatSum));
            invoiceContent = invoiceContent.replace("{suma brutto}", formatIntMinorToString(grossSum));
            invoiceContent = invoiceContent.replace("{suma netto PLN}", formatIntMinorToString(netSum));
            invoiceContent = invoiceContent.replace("{suma vat PLN}", formatIntMinorToString(vatSum));
            invoiceContent = invoiceContent.replace("{suma brutto PLN}", formatIntMinorToString(grossSum));
            invoiceContent = invoiceContent.replace("{razem do zaplaty}", formatIntMinorToString(grossSum));
            String postfix = " PLN "+ (grossSum % 100) + "/100";
            invoiceContent = invoiceContent.replace("{slownie}",
                    ConvertIntToWord.convert(grossSum / 100) + postfix);

            FileUtils.saveFile(invoiceContent, "invoiceNr" + invoiceNumber + ".html");

        } catch (Exception e) {
            System.out.println("ERROR Couldn't save invoice to file: " + e.toString());
        }
    }

    private static List<Product> getProductsFromDatabase(String invoiceNumber) throws SQLException {

        String query = "SELECT * FROM products WHERE invoicenumber = '" + invoiceNumber + "';";
        ResultSet result = Database.select(query);
        List<Product> products = new ArrayList<>();
        while (result.next()) {
            Product p = new Product();
            p.setName(result.getString("NAME"));
            p.setCount(result.getInt("COUNT"));
            p.setUnit(result.getString("UNIT"));
            p.setNetPrice(result.getInt("NETPRICE"));
            p.setVatRate(result.getInt("VATRATE"));
            products.add(p);
        }
        return products;
    }

    private static String replaceTagsWithRealData(String htmlCode, Product prod, int lp) {
        String result = htmlCode;
        result = result.replace("{lp}", lp + "");
        result = result.replace("{nazwa}", prod.getName());
        result = result.replace("{ilosc}", prod.getCount() + "");
        result = result.replace("{Jm}", prod.getUnit());
        result = result.replace("{cena netto}", formatIntMinorToString(prod.getNetPrice()));
        result = result.replace("{wartosc netto}", formatIntMinorToString(prod.getNetValue()));
        result = result.replace("{vat}", prod.getVatRate() + "%");
        result = result.replace("{kwota vat}", formatIntMinorToString(prod.getVatValue()));
        result = result.replace("{wartosc brutto}", formatIntMinorToString(prod.getGrossValue()));

        return result;
    }

    private static String formatIntMinorToString(int minor) {
        return String.format("%.2f", minor / 100.0);
    }
}