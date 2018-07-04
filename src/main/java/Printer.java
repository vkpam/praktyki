public class Printer {

    public static void printOutput(String[][] output) {
        int columnsCount = output[0].length;
        int[] lengths = new int[columnsCount];
        for (int i = 0; i < lengths.length; ++i) {
            lengths[i] = getMaxLengthInColumn(output, i);
        }

        String format = prepareFormat(lengths);
        String horizontalLine = prepareLine(lengths);

        System.out.println("\n" + horizontalLine);
        System.out.format(format, output[0]);
        System.out.println(horizontalLine);
        for (int i = 1; i < output.length; ++i) {
            System.out.format(format, output[i]);
        }
        System.out.println(horizontalLine);
    }

    private static int getMaxLengthInColumn(String[][] array, int columnIndex) {
        int max = array[0][columnIndex].length();
        for (int i = 1; i < array.length; ++i) {
            int currentLength = array[i][columnIndex].length();
            if (currentLength > max) {
                max = currentLength;
            }
        }
        return max;
    }

    private static String prepareFormat(int[] lengths) {
        String format = "";
        for (int i = 0; i < lengths.length; ++i) {
            format += "|%" + lengths[i] + "s";
        }
        format += "|%n";
        return format;
    }

    private static String prepareLine(int[] lengths) {
        int dashCount = 0;
        for(int i = 0; i < lengths.length; ++i) {
            dashCount += lengths[i];
        }
        dashCount += lengths.length + 1;
        String line = "";
        for(int i = 0; i < dashCount; ++i) {
            line += "-";
        }
        return line;
    }
}
