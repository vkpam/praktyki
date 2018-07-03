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
            int customersCount = getCustomersCount();
            String[][] outputArray = new String[customersCount+1][6];
            outputArray[0][0] = " Lp. ";
            outputArray[0][1] = " NAME ";
            outputArray[0][2] = " ADDRESS ";
            outputArray[0][3] = " ADDRESS cont. ";
            outputArray[0][4] = " NIP ";
            outputArray[0][5] = " PHONE ";

            String query = ("SELECT * FROM customers");
            ResultSet result = Database.select(query);
            int i = 1;
            while(result.next()) {
                // TODO
                //outputArray[i][0] = result.getString("ROWID");
                outputArray[i][0] = " " + Integer.toString(i) + " ";
                outputArray[i][1] = " " + result.getString("NAME") + " ";
                outputArray[i][2] = " " + result.getString("ADDRESS") + " ";
                outputArray[i][3] = " " + result.getString("ADDRESS2") + " ";
                outputArray[i][4] = " " + result.getString("NIP") + " ";
                outputArray[i][5] = " " + result.getString("PHONE") + " ";
                ++i;
            }

            printOutput(outputArray);

        } catch(SQLException e) {
            System.out.println("ERROR: Couldn't fetch data from the database: " + e.toString());
        }

    }
    private static int shownMenuAndGetAnswer() {
        System.out.println("\nCUSTOMERS MENU\n 1.Add customer\n 2.Show customers\n 0.Exit");
        System.out.print("Choose an option: ");
        return reading.nextInt();
    }

    private static int getCustomersCount() throws SQLException {
        String query = ("SELECT count(*) FROM customers");
        ResultSet result = Database.select(query);
        result.next();
        return result.getInt(1);
    }

    private static void printOutput(String[][] output) {
        int[] lengths = new int[output[0].length];
        for(int i = 0; i<lengths.length; ++i) {
            lengths[i] = getMaxLengthInColumn(output, i);
        }
        String format = "";
        for(int i = 0; i<lengths.length; ++i) {
            format += "|%" + lengths[i] + "s";
        }
        format += "|%n";
        for(int i = 0; i<output.length; ++i) {
            System.out.format(format, output[i]);
        }
    }

    private static int getMaxLengthInColumn(String[][] array, int columnIndex) {
        int max = array[0][columnIndex].length();
        for(int i = 1; i < array.length; ++i) {
            int currentLength = array[i][columnIndex].length();
            if( currentLength > max) {
                max = currentLength;
            }
        }
        return max;
    }
}
