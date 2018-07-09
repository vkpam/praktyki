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
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showMenuAndGetAnswer();
        }
    }

    private static int showMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nINVOICES MENU\n 1.Add invoice\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    public static void add() {
        Customers.show();
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
    
}
