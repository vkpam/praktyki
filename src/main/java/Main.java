
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        Database.connect();
        System.out.println("Company account v1.0");
        showLogo();
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
                    OtherIncomes.showMenu();
                    break;
                case 4:
                    CarEvidence.showCarEvidenceMenu();
                    break;
                case 5:
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
        System.out.println("\nMENU\n 1.Customers\n 2.Invoices\n 3.Other incomes\n 4.Car evidence\n 5.Costs\n 6.Taxes\n 0.Exit");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static void showLogo() {
        System.out.println("\n" +
                "\n" +
                " _______  _____  _______  _____  _______ __   _ __   __     ----------------\n" +
                " |       |     | |  |  | |_____] |_____| | \\  |   \\_/     --|---------|---\n" +
                " |_____  |_____| |  |  | |       |     | |  \\_|    |          ----------------\n" +
                "                                                            ----------------\n"
        +" _______ _______ _______  _____  _     _ __   _ _______            ----|------|------\n" +
                " |_____| |       |       |     | |     | | \\  |    |        ----------------\n" +
                " |     | |_____  |_____  |_____| |_____| |  \\_|    |   \n" +
                "                                                       \n");
    }
}
