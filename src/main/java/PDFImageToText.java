
//Libraries
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
        // PDF file path
        String pdfFilePath = "C:\\Users\\Bryan\\OneDrive\\Escritorio\\New folder\\Screenshot 2022-08-13 at 15.46.14 (1).pdf";

        try {
            // Upload the PDF document
            PDDocument document = PDDocument.load(new File(pdfFilePath));

            // Instantiate a PDFRenderer
            PDFRenderer renderer = new PDFRenderer(document);

            // Create a Word document
            XWPFDocument wordDocument = new XWPFDocument();


            // For each page of the PDF
            for (int i = 0; i < document.getNumberOfPages(); i++) {

                // Render the page as an image
                String outputFilePath = String.format("C:\\Users\\Bryan\\OneDrive\\Escritorio\\New folder\\pagina%d.png", i + 1);
                ImageIOUtil.writeImage(renderer.renderImageWithDPI(i, 300), outputFilePath, 300);

                // Process the extracted image with OCR
                ITesseract tesseract = new Tesseract();
                tesseract.setDatapath("C:\\Users\\Bryan\\eclipse-workspace\\Simple-eclipse-maven\\Datos de entrenamiento");
                String extractedText = tesseract.doOCR(new File(outputFilePath));

                // Create the paragraph in the Word document
                XWPFParagraph paragraph = wordDocument.createParagraph();

                // Add the text to the paragraph
                XWPFRun run = paragraph.createRun();
                run.setText("Text extracted from the page " + (i + 1) + ":\n\n" + extractedText);

                // Save the Word document
                FileOutputStream out = new FileOutputStream("C:\\Users\\Bryan\\OneDrive\\Escritorio\\New folder\\documento.docx");
                wordDocument.write(out);
                out.close();
                wordDocument.close();
            }

            
            // Close the Word document
            document.close();

            // End of the Code
            System.out.println("End");
            
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
    }
}