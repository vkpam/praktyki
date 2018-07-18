import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OtherIncomes {
    public static void showMenu() {
        int answer = showIncomesMenuAndGetAnswer();
        while (answer != 0) {
            switch (answer) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    showAllIncomes();
                    break;
                case 3:
                    showIncomesByMonth();
                    break;
                case 4:
                    deleteIncome();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showIncomesMenuAndGetAnswer();
        }
    }

    private static int showIncomesMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\n OTHER INCOMES MENU\n 1.Add income\n 2.Show all incomes\n 3.Show incomes by month\n 4.Delete income\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static void addIncome() {
        try {
            String lastNumber = getLastIncomeNumber();
            String number;
            System.out.print("\nNumber (lastly used: "+ lastNumber + " ): ");
            Scanner reading = new Scanner(System.in);
            number = reading.nextLine();
            String description;
            System.out.print("Description: ");
            description = reading.nextLine();
            String date;
            System.out.print("Date: ");
            date = reading.nextLine();
            if (date.isEmpty()) {
                date = TimeUtils.getTodaysDate();
            }
            System.out.print("Value \n\tMajor: ");
            int major = reading.nextInt();
            reading = new Scanner(System.in);
            System.out.print("\tMinor: ");
            int minor = reading.nextInt();
            int value = 100 * major + minor; /* in minor */

            String query = "INSERT INTO otherincomes VALUES(null, '"
                    + number + "','" + description + "','" + date + "'," + value + ");";

            Database.sendQueryToDB(query);
            System.out.println("Income added");

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't add income: " + e.toString());
        }
    }

    private static String getLastIncomeNumber() throws SQLException {
        int incomesCount = Database.getCountHelper("select count(*) from otherincomes;");
        if(incomesCount == 0) {
            return "[NO INCOMES]";
        }
        String query = "select number from otherincomes where otherincomeid = (select max(otherincomeid) from otherincomes);";
        ResultSet result = Database.select(query);
        result.next();
        return result.getString(1);
    }

    private static void showIncomesFunction(ResultSet result, int incomesCount) {
        int valueSum = 0;

        try {
            String[][] outputArray = new String[incomesCount + 1][5];
            outputArray[0][0] = " ID ";
            outputArray[0][1] = " NUMBER ";
            outputArray[0][2] = " DESCRIPTION ";
            outputArray[0][3] = " DATE ";
            outputArray[0][4] = " VALUE ";

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("OTHERINCOMEID") + " ";
                outputArray[i][1] = " " + result.getString("NUMBER") + " ";
                outputArray[i][2] = " " + result.getString("DESCRIPTION") + " ";
                outputArray[i][3] = result.getString("DATE");
                int value = result.getInt("VALUE");
                outputArray[i][4] = " " + value / 100.0 + " ";
                valueSum += value;
                ++i;
            }
            Printer.printOutput(outputArray);

            System.out.println("Total value: " + valueSum / 100.0);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch incomes from the database: " + e.toString());
        }
    }

    private static void showAllIncomes() {
        String querySuffix = "FROM otherincomes WHERE date >= '" + TimeUtils.getBeginningOfTheYear() + "';";
        String query = "SELECT * " + querySuffix;
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) " + querySuffix;
            int incomesCount = Database.getCountHelper(query);
            showIncomesFunction(result, incomesCount);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't show all incomes: " + e.toString());
        }
    }

    private static void showIncomesByMonth() {
        System.out.print("Type year: ");
        Scanner reading = new Scanner(System.in);
        String year = reading.nextLine();
        System.out.print("Type month (01-12): ");
        String month = reading.nextLine();
        String begin = year + "-" + month + "-00";
        String end = year + "-" + month + "-32";
        String query = "SELECT * FROM otherincomes WHERE date > '" + begin + "' AND date < '" + end + "';";
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) FROM otherincomes WHERE date > '" + begin + "' AND date < '" + end + "';";
            int incomesCountByMonth = Database.getCountHelper(query);
            showIncomesFunction(result, incomesCountByMonth);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch incomes in month from the database: " + e.toString());
        }
    }

    private static void deleteIncome() {
        showAllIncomes();
        System.out.print("Type income ID to delete: ");
        try {
            Scanner reading = new Scanner(System.in);
            int answer = reading.nextInt();
            String query = "DELETE FROM otherincomes WHERE OTHERINCOMEID = " + answer + ";";
            Database.sendQueryToDB(query);
            System.out.println("Income deleted");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("Couldn't delete income: " + e.toString());
        }
    }
}