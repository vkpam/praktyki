import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Invoices {

    public static void showMenu() {
        int answer = showMenuAndGetAnswer();
        while (answer != 0)

        {
            switch (answer) {
                case 1:
                    add();
                    break;
                case 2:
                    showAll();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showMenuAndGetAnswer();
        }
    }

    private static int showMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nINVOICES MENU\n 1.Add invoice\n 2.Show all invoices\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    public static void add() {
        Customers.showAll();
        System.out.print("Type customerID: ");
        Scanner reading = new Scanner(System.in);
        try {
            int customerid = reading.nextInt();
            reading = new Scanner(System.in);
            String number;
            System.out.print("\nInvoice's number: ");
            number = reading.nextLine();
            String createdate;
            System.out.print("Creation date (YYYY-MM-DD): ");
            createdate = reading.nextLine();
            String selldate;
            System.out.print("Sell date (YYYY-MM-DD): ");
            selldate = reading.nextLine();
            String paymentdate;
            System.out.print("Payment date (YYYY-MM-DD): ");
            paymentdate = reading.nextLine();
            String payment;
            System.out.print("Payment type: ");
            payment = reading.nextLine();
            String bankname;
            System.out.print("Bank name: ");
            bankname = reading.nextLine();
            String accountnr;
            System.out.print("Account's number: ");
            accountnr = reading.nextLine();
            String currency;
            System.out.print("Currency: ");
            currency = reading.nextLine();
            String comments;
            System.out.print("Comments: ");
            comments = reading.nextLine();

            String query = "INSERT INTO invoices VALUES(null, '"
                    + number + "'," + customerid + ",'" + createdate + "','" + selldate + "','" + paymentdate + "','" + payment + "','" + bankname + "','" + accountnr + "','" + currency + "','" + comments + "');";

            Database.sendQueryToDB(query);
            System.out.println("Invoice added");

            addProductsToInvoice(number);

        } catch (SQLException | InputMismatchException e) {
            System.out.println("ERROR: Couldn't add invoice: " + e.toString());
        }

    }

    private static void addProductsToInvoice(String invoiceNumber) {
        int productscount;
        System.out.print("How many products?: ");
        Scanner reading = new Scanner(System.in);
        productscount = reading.nextInt();
        reading = new Scanner(System.in);

        for (int i = 0; i < productscount; ++i) {

            try {
                String name;
                System.out.print("\nProduct's name: ");
                name = reading.nextLine();
                int count;
                System.out.print("Count: ");
                count = reading.nextInt();
                reading = new Scanner(System.in);
                String unit;
                System.out.print("Unit: ");
                unit = reading.nextLine();
                int major;
                System.out.print("Major: ");
                major = reading.nextInt();
                reading = new Scanner(System.in);
                int minor;
                System.out.print("Minor: ");
                minor = reading.nextInt();
                reading = new Scanner(System.in);
                int vat;
                System.out.print("VAT: ");
                vat = reading.nextInt();
                reading = new Scanner(System.in);

                int netto = major * 100 + minor;

                String query = "INSERT INTO products VALUES(null, '"
                        + invoiceNumber + "','" + name + "'," + count + ",'" + unit + "'," + netto + "," + vat + ");";

                Database.sendQueryToDB(query);
                System.out.println("Product added");


            } catch (SQLException | InputMismatchException e) {
                System.out.println("ERROR: Couldn't add product: " + e.toString());
            }
        }

    }

    public static void showAll() {

        String query = "SELECT * FROM invoices;";

        try {
            ResultSet result = Database.select(query);
            while (result.next()) {
                String invoiceNumber = result.getString("NUMBER");
                System.out.println("\n INVOICE ID " + result.getInt("INVOICEID") + "\t" +
                        " NUMBER " + invoiceNumber);
                Customers.show(result.getInt("CUSTOMERID"));
                System.out.println(" CREATION DATE " + result.getString("CREATIONDATE") + "\t" +
                        " SELL DATE " + result.getString("SELLDATE") + "\t" +
                        " PAYMENT DATE " + result.getString("PAYMENTDATE"));
                System.out.println(" PAYMENT " + result.getString("PAYMENT") + "\t" +
                        " CURRENCY " + result.getString("CURRENCY"));
                System.out.println(" BANKNAME " + result.getString("BANKNAME") + "\t" +
                        " ACCOUNT NUMBER " + result.getString("ACCOUNTNR"));
                System.out.println(" COMMENTS " + result.getString("COMMENTS"));

                showProducts(invoiceNumber);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch data from the database: " + e.toString());
        }
    }

    public static void showProducts(String invoiceNumber) {
        try {

            int productsCount = getProductsCount(invoiceNumber);
            String[][] outputArray = new String[productsCount + 1][9];
            outputArray[0][0] = " ON. ";
            outputArray[0][1] = " NAME ";
            outputArray[0][2] = " COUNT ";  //10
            outputArray[0][3] = " UNIT ";
            outputArray[0][4] = " NETTO PRICE ";  //100
            outputArray[0][5] = " NETTO ";  //10*100
            outputArray[0][6] = " VAT RATE ";    //23%
            outputArray[0][7] = " VAT VALUE ";    //23%*10*100
            outputArray[0][8] = " BRUTTO "; //1230 <- obliczyc

            String query = "SELECT * FROM products WHERE invoicenumber='" + invoiceNumber + "';";
            ResultSet result = Database.select(query);

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + i + " ";
                outputArray[i][1] = " " + result.getString("NAME") + " ";
                int count = result.getInt("COUNT");
                outputArray[i][2] = " " + count + " ";
                outputArray[i][3] = " " + result.getString("UNIT") + " ";
                int nettoPrice = result.getInt("NETTO");
                outputArray[i][4] = " " + nettoPrice / 100.0 + " ";
                int nettoValue = nettoPrice * count;
                outputArray[i][5] = " " + nettoValue / 100.0 + " ";
                int vatRate = result.getInt("VAT");
                outputArray[i][6] = " " + vatRate + " ";
                int vatValue = vatRate * nettoValue / 100;
                outputArray[i][7] = " " + vatValue / 100.0 + " ";
                int brutto = nettoValue + vatValue;
                outputArray[i][8] = " " + brutto / 100.0 + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch data from the database: " + e.toString());
        }
    }

    private static int getProductsCount(String invoiceNumber) throws SQLException {
        String query = "SELECT count(*) FROM products WHERE invoicenumber = '" + invoiceNumber + "';";
        ResultSet result = Database.select(query);
        result.next();
        return result.getInt(1);
    }
}

