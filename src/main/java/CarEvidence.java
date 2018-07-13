import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarEvidence {
    public static void showCarEvidenceMenu() {
        int answer = showCarEvidenceMenuAndGetAnswer();
        while (answer != 0) {
            switch (answer) {
                case 1:
                    addCarEvidence();
                    break;
                case 2:
                    addManySimilarCarEvidence();
                    break;
                case 3:
                    showAllCarEvidence();
                    break;
                case 4:
                    showCarEvidenceByMonth();
                    break;
                case 5:
                    deleteCarEvidence();
                    break;
                default:
                    System.out.println("Wrong answer, please type again");
            }
            answer = showCarEvidenceMenuAndGetAnswer();
        }
    }

    private static int showCarEvidenceMenuAndGetAnswer() {
        Scanner reading = new Scanner(System.in);
        System.out.println("\nCAR EVIDENCE MENU\n 1.Add\n 2.Add many similar\n 3.Show all\n 4.Show by month\n 5.Delete\n 0.Main menu");
        System.out.print("Choose an option: ");
        try {
            return reading.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    private static void addCarEvidence() {
        Scanner reading = new Scanner(System.in);
        String source;
        System.out.print("\nSource: ");
        source = reading.nextLine();
        String destination;
        System.out.print("Destination: ");
        destination = reading.nextLine();
        String goal;
        System.out.print("Goal: ");
        goal = reading.nextLine();
        String date;
        System.out.print("Date (yyyy-mm-dd): ");
        date = reading.nextLine();
        int distance;
        System.out.print("Distance: ");
        try {
            distance = reading.nextInt();
            String query = "INSERT INTO carevidence VALUES(null, '"
                    + source + "','" + destination + "','" + goal + "','" + date + "'," + distance + ");";
            Database.sendQueryToDB(query);
            System.out.println("CarEvidence added");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("ERROR: Couldn't add car evidence: " + e.toString());
        }
    }

    private static void addManySimilarCarEvidence() {
        Scanner reading = new Scanner(System.in);
        System.out.print("How many car evidence will you add ?: ");
        try {
            int carEvidenceCount = reading.nextInt();
            reading = new Scanner(System.in);
            String source;
            System.out.print("\nSource: ");
            source = reading.nextLine();
            String destination;
            System.out.print("Destination: ");
            destination = reading.nextLine();
            String goal;
            System.out.print("Goal: ");
            goal = reading.nextLine();
            int distance;
            System.out.print("Distance: ");
            distance = reading.nextInt();
            reading = new Scanner(System.in);
            String date;
            for (int i = 1; i <= carEvidenceCount; i++) {
                System.out.print("Date " + i + " (yyyy-mm-dd): ");
                date = reading.nextLine();
                String query = "INSERT INTO carevidence VALUES(null, '"
                        + source + "','" + destination + "','" + goal + "','" + date + "'," + distance + ");";
                Database.sendQueryToDB(query);
                System.out.println("CarEvidence " + i + " added");
            }
        } catch (SQLException | InputMismatchException e) {
            System.out.println("ERROR: Couldn't add car evidence: " + e.toString());
        }
    }

    private static void deleteCarEvidence() {
        showAllCarEvidence();
        System.out.print("Type car evidence ID to delete: ");
        try {
            Scanner reading = new Scanner(System.in);
            int answer = reading.nextInt();
            String query = "DELETE FROM carevidence WHERE EVIDENCEID = " + answer + ";";
            Database.sendQueryToDB(query);
            System.out.println("Car evidence deleted");
        } catch (SQLException | InputMismatchException e) {
            System.out.println("Couldn't delete car evidence: " + e.toString());
        }
    }

    private static void showCarEvidenceFunction(ResultSet result, int evidenceCount) {
        try {
            String[][] outputArray = new String[evidenceCount + 1][6];
            outputArray[0][0] = " EVIDENCE ID ";
            outputArray[0][1] = " SOURCE ";
            outputArray[0][2] = " DESTINATION ";
            outputArray[0][3] = " GOAL ";
            outputArray[0][4] = " DATE ";
            outputArray[0][5] = " DISTANCE ";

            int i = 1;
            while (result.next()) {
                outputArray[i][0] = " " + result.getString("EVIDENCEID") + " ";
                outputArray[i][1] = " " + result.getString("SOURCE") + " ";
                outputArray[i][2] = " " + result.getString("DESTINATION") + " ";
                outputArray[i][3] = " " + result.getString("GOAL") + " ";
                outputArray[i][4] = " " + result.getString("DATE") + " ";
                outputArray[i][5] = " " + result.getString("DISTANCE") + " ";
                ++i;
            }

            Printer.printOutput(outputArray);

        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch car evidence from the database: " + e.toString());
        }
    }

    private static void showAllCarEvidence() {
        try {
            String query = "SELECT * FROM carevidence;";
            ResultSet result = Database.select(query);
            query = "SELECT count(*) FROM carevidence;";
            int evidenceCount = getEvidenceCountHelper(query);
            showCarEvidenceFunction(result, evidenceCount);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't show all car evidence: " + e.toString());
        }
    }

    private static void showCarEvidenceByMonth() {
        System.out.print("Type year: ");
        Scanner reading = new Scanner(System.in);
        String year = reading.nextLine();
        System.out.print("Type month (01-12): ");
        String month = reading.nextLine();
        String begin = year + "-" + month + "-00";
        String end = year + "-" + month + "-32";
        String query = "SELECT * FROM carevidence WHERE date > '" + begin + "' AND date < '" + end + "';";
        try {
            ResultSet result = Database.select(query);
            query = "SELECT count(*) FROM carevidence WHERE date > '" + begin + "' AND date < '" + end + "';";
            int evidenceCountByMonth = getEvidenceCountHelper(query);
            showCarEvidenceFunction(result, evidenceCountByMonth);
        } catch (SQLException e) {
            System.out.println("ERROR: Couldn't fetch car evidence in month from the database: " + e.toString());
        }
    }

    private static int getEvidenceCountHelper(String query) throws SQLException {
        ResultSet result = Database.select(query);
        result.next();
        return result.getInt(1);
    }
}