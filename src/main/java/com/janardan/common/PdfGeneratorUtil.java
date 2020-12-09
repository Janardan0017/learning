package com.janardan.common;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created for Zeal on 3/11/20
 */
public class PdfGeneratorUtil {

    /**
     * this method create pdf, it needs one html file with thymeleaf
     * and prepare a stream which can traverse across the network, which is actually a pdf
     *
     * @param htmlTemplate : html template to generate pdf for
     * @return
     */
    public static ByteArrayInputStream generatePdf(String htmlTemplate) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlTemplate);
            renderer.layout();
            renderer.createPDF(outputStream, false);
            renderer.finishPDF();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
