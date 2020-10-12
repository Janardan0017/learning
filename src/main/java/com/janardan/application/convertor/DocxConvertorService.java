package com.janardan.application.convertor;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * Created for janardan on 30/09/20
 */
@Service
public class DocxConvertorService {

    public String readDocxFileData(MultipartFile multipartFile) throws IOException {
        XWPFDocument document = new XWPFDocument(multipartFile.getInputStream());
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        StringBuilder sb = new StringBuilder();
        for (XWPFParagraph paragraph : paragraphs) {
            sb.append(paragraph.getText());
        }
        return StringUtils.normalizeSpace(sb.toString());
    }

    public String docxToHtmlByMammoth(MultipartFile multipartFile) {
        try {
            DocumentConverter converter = new DocumentConverter();
            Result<String> result = converter.convertToHtml(multipartFile.getInputStream());
            String html = result.getValue(); // The generated HTML
            Set<String> warnings = result.getWarnings(); // Any warnings during conversion
            System.out.println(warnings);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String docxToHtmlBySax(MultipartFile multipartFile) {
        try {
            StringWriter sw = new StringWriter();
            SAXTransformerFactory factory = (SAXTransformerFactory)
                    SAXTransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
            handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
            handler.setResult(new StreamResult(sw));
            return "html";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String docxToHtmlPoi(MultipartFile multipartFile) {
        try {
            //convert .docx to HTML string
            XWPFDocument document = new XWPFDocument(multipartFile.getInputStream());
            XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(new File("word/media")));
            OutputStream out = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, out, options);
            String html=out.toString();
//            System.out.println(html);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
