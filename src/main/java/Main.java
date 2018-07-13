
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        Database.connect();
        System.out.println("Company account v1.0");
        int answer = showMenuAndGetAnswer();
        while (answer != 0) {
            switch (answer) {
                case 1:
                    Customers.showMenu();
                    break;
                case 2:
                    Invoices.showMenu();
                    break;
                case 3:
                    CarEvidence.showCarEvidenceMenu();
                    break;
                case 4:
                    Costs.showMenu();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showMenuAndGetAnswer();
        }

        Database.disconnect();
    }

    private static int showMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nMENU\n 1.Customers\n 2.Invoices\n 3.Car evidence\n 4.Costs\n 5.Taxes\n 0.Exit");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

}
