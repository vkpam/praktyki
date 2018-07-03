
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static Scanner reading = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        Database.connect();
        System.out.println("Company account v1.0");
        int answer = shownMenuAndGetAnswer();

         do{
             switch (answer) {
                case 1:
                    Customers.showMenu();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
             answer = shownMenuAndGetAnswer();
        }while(answer!=0);

         Database.disconnect();
    }

    private static int shownMenuAndGetAnswer() {
        System.out.println("\nMENU\n 1.Customer\n 2.Invoices\n 3.Taxes\n 0.Exit");
        System.out.print("Choose an option: ");
        return reading.nextInt();
    }


}
