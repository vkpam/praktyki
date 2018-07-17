import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Customers {

    public static void showMenu() {
        int answer = showMenuAndGetAnswer();
        while (answer != 0) {
            switch (answer) {
                case 1:
                    add();
                    break;
                case 2:
                    showAll();
                    break;
                case 3:
                    delete();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showMenuAndGetAnswer();
        }
    }

    private static void add() {
        Scanner reading = new Scanner(System.in);
        String name;
        System.out.print("\nEnter Name: ");
        name = reading.nextLine();
        String address;
        System.out.print("Enter Address: ");
        address = reading.nextLine();
        String address2;
        System.out.print("Enter Address (line 2): ");
        address2 = reading.nextLine();
        String nip;
        System.out.print("Enter NIP: ");
        nip = reading.nextLine();
        String phone;
        System.out.print("Enter Phone: ");
        phone = reading.nextLine();

        String query = "INSERT INTO customers VALUES(null, '"
                + name + "','" + address + "','" + address2 + "','" + nip + "','" + phone + "');";

        try {
            Database.sendQueryToDB(query);
            System.out.println("Customer added");
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't add customer: " + e.toString());
        }
    }

    public static void showAll() {
        try {
            int customersCount = Database.getCountHelper("SELECT count(*) FROM customers;");
            String[][] outputArray = new String[customersCount + 1][6];
            outputArray[0][0] = " ID ";
            outputArray[0][1] = " NAME ";
            outputArray[0][2] = " ADDRESS ";
            outputArray[0][3] = " ADDRESS cont. ";
            outputArray[0][4] = " NIP ";
            outputArray[0][5] = " PHONE ";

            String query = "SELECT * FROM customers;";
            ResultSet result = Database.select(query);

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("CUSTOMERID") + " ";
                outputArray[i][1] = " " + result.getString("NAME") + " ";
                outputArray[i][2] = " " + result.getString("ADDRESS") + " ";
                outputArray[i][3] = " " + result.getString("ADDRESS2") + " ";
                outputArray[i][4] = " " + result.getString("NIP") + " ";
                outputArray[i][5] = " " + result.getString("PHONE") + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch customers from the database: " + e.toString());
        }
    }

    public static void show(int customerid) {
        String query = "SELECT * FROM customers WHERE customerid = " + customerid + ";";

        try {
            ResultSet result = Database.select(query);
            result.next();
            System.out.println(" NAME " + result.getString("NAME"));
            System.out.println(" ADDRESS " + result.getString("ADDRESS") + ", " +
                result.getString("ADDRESS2"));
            System.out.println(" NIP " + result.getString("NIP"));
            System.out.println(" PHONE " + result.getString("PHONE"));
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch customer from the database: " + e.toString());
        }
    }

    private static void delete() {
        showAll();
        System.out.print("Type customer ID to delete: ");
        try {
            Scanner reading = new Scanner(System.in);
            int answer = reading.nextInt();
            String query = "DELETE FROM customers WHERE CUSTOMERID = " + answer + ";";
            Database.sendQueryToDB(query);
            System.out.println("Customer deleted");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("Couldn't delete customer: " + e.toString());
        }
    }

    private static int showMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nCUSTOMERS MENU\n 1.Add customer\n 2.Show all customers\n 3.Delete customer\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }
}
