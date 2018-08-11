import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

class FileUtils {

    public static String getFileContentAsString(String fileName) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        return new String(encoded, "UTF-8");
    }

    public static void saveFile(String fileContent, String filePath) {
        try(PrintWriter out = new PrintWriter(filePath)) {
            out.println(fileContent);
        } catch(IOException e) {
            System.out.println("Couldn't save file '" + filePath + "': " + e.toString());
        }
    }

    public static void html2Pdf(String inFilePath, String outFilePath) {

        Document doc = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outFilePath));
            doc.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, doc, new FileInputStream(inFilePath));
        } catch(DocumentException | RuntimeWorkerException | IOException e) {
            System.out.println("ERROR Couldn't save PDF file '" + outFilePath + "': " + e.toString());
        } finally {
            if(doc.isOpen()) {
                doc.close();
            }
        }
    }

    public static void openhtmltopdf(String inFilePath, String outFilePath) {

        try(FileOutputStream fos = new FileOutputStream(outFilePath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withFile(new File(inFilePath));
            builder.toStream(fos);
            builder.run();
        } catch(Exception e) {
            System.out.println("ERROR Couldn't save PDF file '" + outFilePath + "': " + e.toString());
        }
    }
}
