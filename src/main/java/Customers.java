import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customers {

    private static Scanner reading = new Scanner(System.in);

    public static void showMenu() {
        int answer = shownMenuAndGetAnswer();
       do {
           switch (answer) {
               case 1:
                   add();
                   break;
               case 2:
                   show();
                   break;
               default:
                   System.out.println("Wrong answer, please type again");
           }
           answer = shownMenuAndGetAnswer();
       } while(answer!=0);
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
        long nip;
        System.out.print("Enter NIP: ");
        nip = reading.nextLong();
        long phone;
        System.out.print("Enter Phone: ");
        phone = reading.nextLong();
        String query = "INSERT INTO customers VALUES('"
                + name + "','" + address + "','" + address2 + "'," + nip + "," + phone + ");";
        try {
            Database.sendQueryToDB(query);
            System.out.println("Customer added\n");
        } catch(SQLException e) {
            System.out.println("ERROR: Couldn't add customer: " + e.toString());
        }
    }

    public static void show() {
        try {
            String query = ("SELECT * FROM customers");
            ResultSet result = Database.select(query);
            while(result.next()) {
                System.out.println(result.getString("NAME") + "\t"
                        + result.getString("ADDRESS") + "\t"
                        + result.getString("ADDRESS2") + "\t"
                        + result.getLong("NIP") + "\t"
                        + result.getLong("PHONE"));
            }

        } catch(SQLException e) {
            System.out.println("ERROR: Couldn't fetch data from the database: " + e.toString());
        }

    }
    private static int shownMenuAndGetAnswer() {
        System.out.println("\nCUSTOMERS MENU\n 1.Add customer\n 2.Show customers\n 0.Exit");
        System.out.print("Choose an option: ");
        return reading.nextInt();
    }
}
