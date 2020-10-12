package com.janardan.application.convertor;

import com.aspose.pdf.Document;
import com.aspose.pdf.HtmlSaveOptions;
import com.aspose.pdf.LettersPositioningMethods;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.fit.pdfdom.PDFDomTree;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created for janardan on 30/09/20
 */
@Service
public class PdfConvertorService {

    public String pdfToHtmlByPdfBox(MultipartFile multipartFile) {
        try {
            PDDocument pdf = PDDocument.load(multipartFile.getInputStream());
//            pdf = clearPdfImages(pdf);
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
            long start = System.currentTimeMillis();
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

}
