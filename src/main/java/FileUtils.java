import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.io.IOException;

class FileUtils {

    public static String getFileContentAsString(String fileName) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        return new String(encoded, "UTF-8");
    }

    public static void saveFile(String fileContent, String fileName) {
        try(PrintWriter out = new PrintWriter(fileName)) {
            out.println(fileContent);
        } catch(IOException e) {
            System.out.println("Couldn't save file '" + fileName + "': " + e.toString());
        }
    }
}
