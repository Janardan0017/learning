package com.janardan.application.convertor;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Created for janardan on 30/09/20
 */
@Service
public class DocConvertorService {


    public String readDocFileData(MultipartFile multipartFile) throws IOException {
        HWPFDocument document = new HWPFDocument(multipartFile.getInputStream());
        return StringUtils.normalizeSpace(document.getText().toString());
    }

    public String docToHtmlPoi(MultipartFile multipartFile) {
        try {
            HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(multipartFile.getInputStream());

            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            out.close();

            String result = new String(out.toByteArray());
            // remove line breaks
            result = StringUtils.normalizeSpace(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String docToHtmlByMammoth(MultipartFile multipartFile) {
        try {
            DocumentConverter converter = new DocumentConverter();
            Result<String> result = converter.convertToHtml(multipartFile.getInputStream());
            String html = result.getValue(); // The generated HTML
            Set<String> warnings = result.getWarnings(); // Any warnings during conversion
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
