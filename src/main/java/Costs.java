import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Costs {
    public static void showMenu() {
        int answer = showMenuAndGetAnswerCosts();
        while (answer != 0) {
            switch (answer) {
                case 1:
                    addCosts();
                    break;
                case 2:
                    showAllCosts();
                    break;
                case 3:
                    showCostsByMonth();
                    break;
                case 4:
                    deleteCosts();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showMenuAndGetAnswerCosts();
        }
    }

    private static int showMenuAndGetAnswerCosts() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nCOSTS MENU\n 1.Add cost\n 2.Show all costs\n 3.Show costs by month\n 4.Delete cost\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static void addCosts() {
        int costsCount;
        System.out.print("How many costs?: ");
        Scanner reading = new Scanner(System.in);
        costsCount = reading.nextInt();
        reading = new Scanner(System.in);

        for (int i = 0; i < costsCount; ++i) {
            String date;
            System.out.print("\nDate: ");
            date = reading.nextLine();
            String description;
            System.out.print("Description: ");
            description = reading.nextLine();
            try {
                int netValue;
                System.out.print("NET value: ");
                netValue = reading.nextInt();
                reading = new Scanner(System.in);
                int vatValue;
                System.out.print("VAT value: ");
                vatValue = reading.nextInt();
                reading = new Scanner(System.in);

                String query = "INSERT INTO costs VALUES(null, '"
                        + date + "','" + description + "'," + netValue + "," + vatValue + ");";

                Database.sendQueryToDB(query);
                System.out.println("Costs added");
            } catch (SQLException e) {
                System.out.println("ERROR: Couldn't add costs: " + e.toString());
            }
        }
    }

    private static void showCostsFunction(ResultSet result, int costsCount) {
        try {
            String[][] outputArray = new String[costsCount + 1][5];
            outputArray[0][0] = " ID ";
            outputArray[0][1] = " DATE ";
            outputArray[0][2] = " DESCRIPTION ";
            outputArray[0][3] = " NET VALUE ";
            outputArray[0][4] = " VAT VALUE ";

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("COSTID") + " ";
                outputArray[i][1] = " " + result.getString("DATE") + " ";
                outputArray[i][2] = " " + result.getString("DESCRIPTION") + " ";
                outputArray[i][3] = " " + result.getString("NETVALUE") + " ";
                outputArray[i][4] = " " + result.getString("VATVALUE") + " ";
                ++i;
            }
            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch customers from the database: " + e.toString());
        }
    }

    public static void showAllCosts() {

        try {
            String query = "SELECT * FROM costs;";
            ResultSet result = Database.select(query);
            query = "SELECT count(*) FROM costs;";
            int costsCount = getCostsCountHelper(query);
            showCostsFunction(result, costsCount);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't show all costs: " + e.toString());
        }
    }

    private static void showCostsByMonth() {
        System.out.print("Type year: ");
        Scanner reading = new Scanner(System.in);
        String year = reading.nextLine();
        System.out.print("Type month (01-12): ");
        String month = reading.nextLine();
        String begin = year + "-" + month + "-00";
        String end = year + "-" + month + "-32";
        String query = "SELECT * FROM costs WHERE date > '" + begin + "' AND date < '" + end + "';";
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) FROM costs WHERE date > '" + begin + "' AND date < '" + end + "';";
            int costsCountByMonth = getCostsCountHelper(query);
            showCostsFunction(result, costsCountByMonth);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch costs in month from the database: " + e.toString());
        }
    }

    private static int getCostsCountHelper(String query) throws SQLException {
        ResultSet result = Database.select(query);
        result.next();
        return result.getInt(1);
    }

    private static void deleteCosts() {
        showAllCosts();
        System.out.print("Type cost ID to delete: ");
        try {
            Scanner reading = new Scanner(System.in);
            int answer = reading.nextInt();
            String query = "DELETE FROM costs WHERE COSTID = " + answer + ";";
            Database.sendQueryToDB(query);
            System.out.println("Cost deleted");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("Couldn't delete cost: " + e.toString());
        }
    }
}