/*import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Accountancy {
    public static void showMenu() {
        int answer = showMenuAndGetAnswerCosts();
        while (answer != 0) {
            switch (answer) {
                case 1:
                    overview();
                    break;
                case 2:
                    incomeTax();
                    break;
                case 3:
                    vatTax();
                    break;
                case 4:
                    ZUSPremiums();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showMenuAndGetAnswerCosts();
        }
    }

    private static int showMenuAndGetAnswerCosts() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nAccountancy MENU \n 1.Overview\n 2.Income TAX\n 3.VAT Tax\n 4.ZUS Premiums\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static void overview() {
        System.out.print("\tOVERVIEW");
        System.out.print("Składki ZUS do zapłaty: " + "kwotaSkladekZus + \t STATUS:" + "statusSkladekZus");
        System.out.print("Podatek dochodowy do zapłaty: + kwotaPodatkuDochodowego + \t STATUS: + statusPodatkuDochodowego");
        System.out.print("Podatek VAT do zapłaty: + kwotaPodatkuVat + \t STATUS: + statusPodatkuVat");
    }

    private static void incomeTax() {
        System.out.print("\tINCOME TAX");
        System.out.print("PRZYCHÓD: ");
        System.out.print("KOSZTY UZYSKANIA: ");
        System.out.print("DOCHÓD: ");
        System.out.print("PODATEK DOCHODOWY: ");
        System.out.print("PROCENT PODATKU: ");
        System.out.print("KOSZTY MOŻLIWE DO ODLICZENIA: ");

        try {
            String[][] outputArray = new String[monthCount + 1][6];
            outputArray[0][0] = " OKRES ";
            outputArray[0][1] = " TERMIN ZAPŁATY ";
            outputArray[0][2] = " PRZYCHODY NARASTAJĄCO ";
            outputArray[0][3] = " KOSZTY NARASTAJĄCO ";
            outputArray[0][4] = " DOCHÓD NARASTAJĄCO ";
            outputArray[0][5] = " DOCHÓD Z OKRESU ";

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("SEASON") + " ";
                outputArray[i][1] = " " + result.getString("PAYMENTDATE") + " ";
                outputArray[i][2] = " " + result.getString("REVENUES") + " ";
                outputArray[i][3] = " " + result.getString("COSTS") + " ";
                outputArray[i][4] = " " + result.getString("INCOME") + " ";
                outputArray[i][5] = " " + result.getString("SEASONINCOME") + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch income Tax from the database: " + e.toString());
        }
    }

    private static void vatTax() {
        System.out.print("\tVAT TAX");
        System.out.print("PODATEK VAT: ");
        System.out.print("STAWKA PODATKU VAT: ");
        System.out.print("WARTOŚĆ NETTO FAKTURY: ");
        System.out.print("WARTOŚĆ BRUTTO FAKTURY: ");
        System.out.print("VAT DO ODLICZENIA: ");

        try {
            String[][] outputArray = new String[monthCount + 1][6];
            outputArray[0][0] = " OKRES ";
            outputArray[0][1] = " TERMIN ZAPŁATY ";
            outputArray[0][2] = " VAT OD SPRZEDAŻY ";
            outputArray[0][3] = " VAT OD ZAKUPÓW ";
            outputArray[0][4] = " NADPŁATA ";
            outputArray[0][5] = " DO ZAPŁATY ";

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("SEASON") + " ";
                outputArray[i][1] = " " + result.getString("PAYMENTDATE") + " ";
                outputArray[i][2] = " " + result.getString("VATFROMTHESALES") + " ";
                outputArray[i][3] = " " + result.getString("VATFROMSHOPPING") + " ";
                outputArray[i][4] = " " + result.getString("EXCESSPAYMENT") + " ";
                outputArray[i][5] = " " + result.getString("TOPAY") + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch vat Tax from the database: " + e.toString());
        }
    }

    private static void ZUSPremiums() {
        System.out.print("\tZUS PREMIUMS");
        System.out.print("SKŁADA ZUS: ");
        System.out.print("SKŁADKI SPOŁECZNE: ");

        try {
            String[][] outputArray = new String[monthCount + 1][7];
            outputArray[0][0] = " OKRES ";
            outputArray[0][1] = " PŁATNE DO ";
            outputArray[0][2] = " SKŁADKI SPOŁECZNE ";
            outputArray[0][3] = " SKŁADKI ZDROWOTNE ";
            outputArray[0][4] = " FUNDUSZ PRACY ";
            outputArray[0][5] = " SUMA ";
            outputArray[0][6] = " STATUS ";

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("SEASON") + " ";
                outputArray[i][1] = " " + result.getString("PAYTO") + " ";
                outputArray[i][2] = " " + result.getString("SOCIALPREMIUMS") + " ";
                outputArray[i][3] = " " + result.getString("HEALTHPREMIUMS") + " ";
                outputArray[i][4] = " " + result.getString("WORKMONEY") + " ";
                outputArray[i][5] = " " + result.getString("SUM") + " ";
                outputArray[i][6] = " " + result.getString("STAT") + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch ZUS premiums from the database: " + e.toString());
        }
    }
}*/