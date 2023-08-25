import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.poi.xwpf.usermodel.*;

public class PDFImageToText {
    public static void main(String[] args) {
        // Ruta del archivo PDF
        String pdfFilePath = "C:\\Users\\Bryan\\OneDrive\\Escritorio\\New folder\\Screenshot 2022-08-13 at 15.46.14 (1).pdf";

        try {
            // Cargar el documento PDF
            PDDocument document = PDDocument.load(new File(pdfFilePath));

            // Instanciar un PDFRenderer
            PDFRenderer renderer = new PDFRenderer(document);

            // Crear un documento Word
            XWPFDocument wordDocument = new XWPFDocument();

            // Para cada página del PDF
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                // Renderizar la página como una imagen
                String outputFilePath = String.format("C:\\Users\\Bryan\\OneDrive\\Escritorio\\New folder\\pagina%d.png", i + 1);
                ImageIOUtil.writeImage(renderer.renderImageWithDPI(i, 300), outputFilePath, 300);

                // Procesar la imagen extraída con OCR
                ITesseract tesseract = new Tesseract();
                tesseract.setDatapath("C:\\Users\\Bryan\\eclipse-workspace\\Simple-eclipse-maven\\Datos de entrenamiento");
                String extractedText = tesseract.doOCR(new File(outputFilePath));

                // Crear un párrafo en el documento Word
                XWPFParagraph paragraph = wordDocument.createParagraph();

                // Agregar el texto extraído al párrafo
                XWPFRun run = paragraph.createRun();
                run.setText("Text extracted from the page " + (i + 1) + ":\n\n" + extractedText);

                // Guardar el documento Word
                FileOutputStream out = new FileOutputStream("C:\\Users\\Bryan\\OneDrive\\Escritorio\\New folder\\documento.docx");
                wordDocument.write(out);
                out.close();
            }

            // Cerrar el documento PDF
            document.close();
            
            System.out.println("End");
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
    }
}