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
        System.out.println("\nCOSTS MENU\n 1.Add costs\n 2.Show all costs\n \t3.Only car costs\n \t4.By month\n 5.Delete cost\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static void addCosts() {
        System.out.print("How many costs?: ");
        Scanner reading = new Scanner(System.in);
        try {
            int costsCount = reading.nextInt();
            for (int i = 0; i < costsCount; ++i) {
                reading = new Scanner(System.in);
                String date;
                System.out.print("\nDate (yyyy-mm-dd): ");
                date = reading.nextLine();
                if (date.isEmpty()) {
                    date = TimeUtils.getTodaysDate();
                }
                String description;
                System.out.print("Description: ");
                description = reading.nextLine();

                System.out.print("Net Value \n\tMajor: ");
                int major = reading.nextInt();
                reading = new Scanner(System.in);
                System.out.print("\tMinor: ");
                int minor = reading.nextInt();
                reading = new Scanner(System.in);
                int netValue = 100 * major + minor;

                System.out.print("Vat Value \n\tMajor: ");
                int majorVat = reading.nextInt();
                reading = new Scanner(System.in);
                System.out.print("\tMinor: ");
                int minorVat = reading.nextInt();
                int vatValue = 100 * majorVat + minorVat;

                int isCarRelated;
                do {
                    reading = new Scanner(System.in);
                    System.out.print("Is Car Related ( 0 - NO, 1 - YES): ");
                    isCarRelated = reading.nextInt();
                } while (isCarRelated != 0 && isCarRelated != 1);

                String query = "INSERT INTO costs VALUES(null, '"
                        + date + "','" + description + "'," + netValue + "," + vatValue + "," + isCarRelated + ");";

                Database.sendQueryToDB(query);
                System.out.println("Cost added");
            }
        } catch (SQLException | InputMismatchException e) {
            System.out.println("ERROR: Couldn't add costs: " + e.toString());
        }
    }

    private static void showCostsFunction(ResultSet result, int costsCount) {
        int netValueSum = 0;
        int savedTaxSum = 0;
        int savedVatSum = 0;

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
                netValueSum += netValue;
                outputArray[i][3] = " " + netValue / 100.0 + " ";
                int vatValue = result.getInt("VATVALUE");
                outputArray[i][4] = " " + vatValue / 100.0 + " ";
                int isCarRelated = result.getInt("ISCARRELATED");
                outputArray[i][5] = " " + (isCarRelated == 1 ? "YES" : "NO") + " ";
                int savedTax = getSavedTax(isCarRelated, netValue, vatValue);
                savedTaxSum += savedTax;
                outputArray[i][6] = " " + savedTax / 100.0 + " ";
                int savedVat = getSavedVat(isCarRelated, vatValue);
                savedVatSum += savedVat;
                outputArray[i][7] = " " + savedVat / 100.0 + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

            System.out.println("Total net value: " + netValueSum / 100.0);
            System.out.println("Total saved income tax: " + savedTaxSum / 100.0);
            System.out.println("Total saved vat tax: " + savedVatSum / 100.0);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch costs from the database: " + e.toString());
        }
    }

    private static int getSavedTax(int isCarRelated, int netValue, int vatValue) throws SQLException {
        double incomeTaxRate = Configuration.getIntegerParameter("incometaxrate") / 100.0;  //0.19
        int savedTax;
        if (isCarRelated == 0) {
            savedTax = netValue;
        } else {
            savedTax = netValue + vatValue / 2;
        }
        return (int) (savedTax * incomeTaxRate);
    }

    private static int getSavedVat(int isCarRelated, int vatValue) {
        int savedVat;
        if (isCarRelated == 0) {
            savedVat = vatValue;
        } else {
            savedVat = vatValue / 2;
        }
        return savedVat;
    }

    private static void showAllCosts() {
        String querySuffix = "FROM costs WHERE date >= '" + TimeUtils.getBeginningOfTheYear() + "';";
        String query = "SELECT * " + querySuffix;
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) " + querySuffix;
            int costsCount = Database.getCountHelper(query);
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
            int costsCountByMonth = Database.getCountHelper(query);
            showCostsFunction(result, costsCountByMonth);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch costs in month from the database: " + e.toString());
        }
    }

    private static void showOnlyCarCosts() {
        String querySuffix = "FROM costs WHERE iscarrelated = 1 AND date >= '" + TimeUtils.getBeginningOfTheYear() + "';";
        String query = "SELECT * " + querySuffix;
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) " + querySuffix;
            int carCostCount = Database.getCountHelper(query);
            showCostsFunction(result, carCostCount);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch car costs from the database: " + e.toString());
        }
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