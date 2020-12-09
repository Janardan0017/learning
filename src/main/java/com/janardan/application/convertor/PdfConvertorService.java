package com.janardan.application.convertor;

import com.aspose.pdf.Document;
import com.aspose.pdf.HtmlSaveOptions;
import com.aspose.pdf.LettersPositioningMethods;
import com.aspose.pdf.SaveFormat;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.documents4j.job.RemoteConverter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import org.apache.pdfbox.pdmodel.*;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.fit.pdfdom.PDFDomTree;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created for janardan on 30/09/20
 */
@Service
public class PdfConvertorService {

    public String pdfToHtmlByPdfBox(MultipartFile multipartFile) {
        try {
            PDDocument pdf = PDDocument.load(multipartFile.getInputStream());
            OutputStream outputStream = new ByteArrayOutputStream();
            Writer output = new PrintWriter(outputStream);
            new PDFDomTree().writeText(pdf, output);
            return outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String pdfToHtmlAspos(MultipartFile multipartFile) {
        try {
            Document doc = new Document(multipartFile.getInputStream());
            HtmlSaveOptions newOptions = new HtmlSaveOptions();
            // Enable option to embed all resources inside the HTML
            newOptions.PartsEmbeddingMode = HtmlSaveOptions.PartsEmbeddingModes.EmbedAllIntoHtml;
            // This is just optimization for IE and can be omitted
            newOptions.LettersPositioningMethod = LettersPositioningMethods.UseEmUnitsAndCompensationOfRoundingErrorsInCss;
            newOptions.RasterImagesSavingMode = HtmlSaveOptions.RasterImagesSavingModes.AsEmbeddedPartsOfPngPageBackground;
            newOptions.FontSavingMode = HtmlSaveOptions.FontSavingModes.SaveInAllFormats;
            newOptions.setSplitCssIntoPages(true);
            newOptions.setSplitCssIntoPages(false);
            OutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, newOptions);
            String result = outputStream.toString();
            result = result.replaceAll("<div.*Evaluation.*Copyright.*</div>", "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PDDocument clearPdfImages(PDDocument document) {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        for (Object pageObj : catalog.getPages()) {
            PDPage page = (PDPage) pageObj;
            PDResources resources = page.getResources();
            resources.getCOSObject().clear();
        }
        return document;
    }

    public String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public String pdfToDoc(MultipartFile multipartFile) {
        try {
            //Create the word document
            XWPFDocument doc = new XWPFDocument();

            // Open the pdf file
            PdfReader reader = new PdfReader(multipartFile.getInputStream());
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);

            // Read the PDF page by page
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                // Extract the text
                String text = strategy.getResultantText();
                // Create a new paragraph in the word document, adding the extracted text
                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setText(text);
                // Adding a page break
                run.addBreak(BreakType.PAGE);
            }
            // Write the word document
            FileOutputStream out = new FileOutputStream("myfile.docx");
            doc.write(out);
            // Close all open files
            out.close();
            reader.close();
            return "output";
        } catch (Exception e) {
            return null;
        }
    }

    public void pdfToDocAspos(MultipartFile multipartFile) {
        try {
            Document pdfDocument = new Document(multipartFile.getInputStream());
            pdfDocument.save("pdftodoc2.doc", SaveFormat.Doc);
        } catch (Exception e) {

        }
    }

    public void docsslf4j(MultipartFile multipartFile) {
        try {
            File target = new File("/home/janardan/Documents/janardan/pdftodoc.doc");
            IConverter iConverter = LocalConverter.builder().build();
            Future<Boolean> schedule = iConverter.convert(multipartFile.getInputStream()).as(DocumentType.PDF)
                    .to(target).as(DocumentType.DOC)
                    .prioritizeWith(1000).schedule();
            System.out.println("converted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void docsslf4jV2(MultipartFile multipartFile) {
        try {
            IConverter iConverter = RemoteConverter.builder()
                    .workerPool(20, 25, 2, TimeUnit.SECONDS)
                    .requestTimeout(10, TimeUnit.SECONDS)
                    .baseUri("http://localhost:8080")
                    .build();
            File target = new File("/home/janardan/Documents/janardan/pdftodoc2.doc");
            Future<Boolean> schedule = iConverter.convert(multipartFile.getInputStream()).as(DocumentType.PDF)
                    .to(target).as(DocumentType.DOC)
                    .prioritizeWith(1000).schedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
