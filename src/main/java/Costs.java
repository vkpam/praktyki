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
                    showOnlyCarCosts();
                    break;
                case 4:
                    showCostsByMonth();
                    break;
                case 5:
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
        System.out.println("\nCOSTS MENU\n 1.Add cost\n 2.Show all costs\n \t3.Only car costs\n \t4.By month\n 5.Delete cost\n 0.Main menu");
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
            if (date.isEmpty()) {
                date = TimeUtils.getTodaysDate();
            }
            String description;
            System.out.print("Description: ");
            description = reading.nextLine();
            try {
                System.out.print("Net Value \n\tMajor: ");
                int major = reading.nextInt();
                reading = new Scanner(System.in);
                System.out.print("\tMinor: ");
                int minor = reading.nextInt();
                reading = new Scanner(System.in);
                int netValue = 100* major + minor;
                reading = new Scanner(System.in);
                System.out.print("Vat Value \n\tMajor: ");
                int majorVat = reading.nextInt();
                reading = new Scanner(System.in);
                System.out.print("\tMinor: ");
                int minorVat = reading.nextInt();
                reading = new Scanner(System.in);
                int vatValue = 100* majorVat + minorVat;
                reading = new Scanner(System.in);
                int isCarRelated;
                System.out.print("Is Car Related ( 0 - NO, 1 - YES): ");
                isCarRelated = reading.nextInt();
                reading = new Scanner(System.in);

                String query = "INSERT INTO costs VALUES(null, '"
                        + date + "','" + description + "'," + netValue + "," + vatValue + "," + isCarRelated + ");";

                Database.sendQueryToDB(query);
                System.out.println("Costs added");
            } catch (SQLException e) {
                System.out.println("ERROR: Couldn't add costs: " + e.toString());
            }
        }
    }


    private static void showCostsFunction(ResultSet result, int costsCount) {
        try {
            String[][] outputArray = new String[costsCount + 1][8];
            outputArray[0][0] = " ID ";
            outputArray[0][1] = " DATE ";
            outputArray[0][2] = " DESCRIPTION ";
            outputArray[0][3] = " NET VALUE ";
            outputArray[0][4] = " VAT VALUE ";
            outputArray[0][5] = " CAR ";
            outputArray[0][6] = " SAVED TAX ";
            outputArray[0][7] = " SAVED VAT ";


            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("COSTID") + " ";
                outputArray[i][1] = " " + result.getString("DATE") + " ";
                outputArray[i][2] = " " + result.getString("DESCRIPTION") + " ";
                int netValue = result.getInt("NETVALUE");
                outputArray[i][3] = " " + netValue + " ";
                int vatValue = result.getInt("VATVALUE") * netValue;
                outputArray[i][4] = " " + vatValue + " ";
                int isCarRelated = result.getInt("ISCARRELATED");
                outputArray[i][5] = " " + isCarRelated + " ";
                outputArray[i][6] = " " + getSavedTax(isCarRelated, netValue, vatValue) / 100 + " ";
                outputArray[i][7] = " " + getSavedVat(isCarRelated, netValue, vatValue) / 100 + " ";
                ++i;
            }
            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch customers from the database: " + e.toString());
        }
    }

    private static int getSavedTax(int isCarRelated, int netValue, int vatValue) {
        int savedTax;
        if(isCarRelated == 0) {
            savedTax = netValue ;
        } else {
            savedTax = netValue + vatValue/2;
        }
        return savedTax;
    }

    private static int getSavedVat(int isCarRelated, int netValue, int vatValue) {
        int savedVat;
        if(isCarRelated == 0) {
            savedVat = vatValue;
        } else {
            savedVat = vatValue/2;
        }
        return savedVat;
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

    private static void showOnlyCarCosts() {
        String query = "SELECT * FROM costs WHERE iscarrelated = 1;";
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) FROM costs WHERE iscarrelated = 1;";
            int carCostCount = getCostsCountHelper(query);
            showCostsFunction(result, carCostCount);
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
