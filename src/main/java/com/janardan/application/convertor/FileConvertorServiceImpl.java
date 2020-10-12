package com.janardan.application.convertor;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by emp350 on 04/02/20
 */
@Service
public class FileConvertorServiceImpl implements FileConvertorService {

    private static volatile long counter = 1L;
    private final DocConvertorService docConvertorService;
    private final PdfConvertorService pdfConvertorService;
    private final DocxConvertorService docxConvertorService;

    @Autowired
    public FileConvertorServiceImpl(DocConvertorService docConvertorService, PdfConvertorService pdfConvertorService,
                                    DocxConvertorService docxConvertorService) {
        this.docConvertorService = docConvertorService;
        this.pdfConvertorService = pdfConvertorService;
        this.docxConvertorService = docxConvertorService;
    }

    /**
     * read a multipart file and return its content as string
     *
     * @param multipartFile
     * @param target
     * @return
     * @throws IOException
     */
    @Override
    public String readFile(MultipartFile multipartFile, String target) throws IOException {
        if (!multipartFile.isEmpty()) {
            // get file type
            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            switch (Objects.requireNonNull(extension)) {
                case "doc": {
                    if (target.equals("html"))
                        return docConvertorService.docToHtmlPoi(multipartFile);
                    return docConvertorService.readDocFileData(multipartFile);
                }
                case "docx": {
                    if (target.equals("html"))
                        return docxConvertorService.docxToHtmlPoi(multipartFile);
                    return docxConvertorService.readDocxFileData(multipartFile);
                }
                case "pdf":
                    if (target.equals("html"))
                        return pdfConvertorService.pdfToHtmlAspos(multipartFile);
                    return pdfConvertorService.pdfToHtmlByPdfBox(multipartFile);
                case "text": {
                    return textToHtml(multipartFile);
//                    return new String(multipartFile.getBytes());
                }
            }
        }
        return null;
    }


    /**
     * read the content from input stream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    @Override
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


    private String textToHtml(MultipartFile multipartFile) {
        try {
            // create unique file name
            String fileName = counter + System.currentTimeMillis() + multipartFile.getOriginalFilename();
            counter++;
            // create a temporary file (in tmp directory)
            Path tempFile = Files.createTempFile("", fileName);
            Path htmlFile = Files.createTempFile("", "pdfpdf.html");
            // set multipart file data to temporary file
            multipartFile.transferTo(tempFile.toFile());
            FileInputStream inputStream = new FileInputStream(tempFile.toFile());
            String s = readFromInputStream(inputStream);
            s = s.replace("&", "&amp;");
            s = s.replace("<", "&lt;");
            s = s.replace(">", "&gt;");
            s = s.replace("[\\r\\n?|\\n]", "<br>");
            s = s.replace("<br>\\s*<br>", "</p><p>");
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
