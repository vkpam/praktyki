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
                    show();
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

    public static void add() {
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
        try {
            long nip;
            System.out.print("Enter NIP: ");
            nip = reading.nextLong();
            long phone;
            System.out.print("Enter Phone: ");
            phone = reading.nextLong();
            String query = "INSERT INTO customers VALUES('"
                    + name + "','" + address + "','" + address2 + "'," + nip + "," + phone + ");";

            Database.sendQueryToDB(query);
            System.out.println("Customer added\n");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("ERROR: Couldn't add customer: " + e.toString());
        }
    }

    public static void show() {
        try {

            int customersCount = getCustomersCount();
            String[][] outputArray = new String[customersCount + 1][6];
            outputArray[0][0] = " Lp. ";
            outputArray[0][1] = " NAME ";
            outputArray[0][2] = " ADDRESS ";
            outputArray[0][3] = " ADDRESS cont. ";
            outputArray[0][4] = " NIP ";
            outputArray[0][5] = " PHONE ";


            String query = "SELECT ROWID,* FROM customers";
            ResultSet result = Database.select(query);

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("ROWID") + " ";
                outputArray[i][1] = " " + result.getString("NAME") + " ";
                outputArray[i][2] = " " + result.getString("ADDRESS") + " ";
                outputArray[i][3] = " " + result.getString("ADDRESS2") + " ";
                outputArray[i][4] = " " + result.getString("NIP") + " ";
                outputArray[i][5] = " " + result.getString("PHONE") + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch data from the database: " + e.toString());
        }
    }

    public static void delete() {
        show();
        System.out.print("Type customer number to delete: ");
        try {
            Scanner reading = new Scanner(System.in);
            int answer = reading.nextInt();
            String query = "DELETE FROM customers WHERE ROWID = " + answer + ";";
            Database.sendQueryToDB(query);
        } catch (SQLException | InputMismatchException e) {
            System.out.println("Couldn't delete customer: " + e.toString());
        }
    }

    private static int showMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nCUSTOMERS MENU\n 1.Add customer\n 2.Show customers\n 3.Delete customer\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static int getCustomersCount() throws SQLException {
        String query = "SELECT count(*) FROM customers";
        ResultSet result = Database.select(query);
        result.next();
        return result.getInt(1);
    }
}
