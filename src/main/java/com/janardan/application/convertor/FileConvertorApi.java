package com.janardan.application.convertor;

import com.janardan.application.opencsv.User;
import com.janardan.common.PdfGeneratorUtil;
import com.janardan.common.PrepareHtmlForPdfService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created for fileconvertor on 25/09/20
 */
@RestController
@RequestMapping("/api/v1")
public class FileConvertorApi {

    private FileConvertorService fileConvertorService;

    @Autowired
    public void setFileConvertorService(FileConvertorService fileConvertorService) {
        this.fileConvertorService = fileConvertorService;
    }

    @PostMapping("/read")
    public ResponseEntity<String> readFileData(@RequestParam(name = "file") MultipartFile multipartFile,
                                               @ApiParam(name = "target", allowableValues = "html,text")
                                               @RequestParam(name = "target", defaultValue = "html") String target) throws IOException {

        String fileData = fileConvertorService.readFile(multipartFile, target);
        return ResponseEntity.ok().body(fileData);
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertFile(@RequestParam(name = "file") MultipartFile multipartFile,
                                              @ApiParam(name = "type", allowableValues =
                                                      "pdfToHtmlByPdfBox,pdfToHtmlAspos,docToHtmlPoi,docToHtmlByMammoth," +
                                                              "docxToHtmlByMammoth,pdfToDoc,pdfToDocAspos,docsslf4j,docsslf4jV2")
                                              @RequestParam(name = "type") String type) throws IOException {

        String fileData = null;
        switch (type) {
            case "pdfToHtmlByPdfBox":
                fileData = new PdfConvertorService().pdfToHtmlByPdfBox(multipartFile);
                break;
            case "pdfToHtmlAspos":
                fileData = new PdfConvertorService().pdfToHtmlAspos(multipartFile);
                break;
            case "docToHtmlPoi":
                fileData = new DocConvertorService().docToHtmlPoi(multipartFile);
                break;
            case "docToHtmlByMammoth":
                fileData = new DocConvertorService().docToHtmlByMammoth(multipartFile);
                break;
            case "docxToHtmlByMammoth":
                fileData = new DocxConvertorService().docxToHtmlByMammoth(multipartFile);
                break;
            case "docxToHtmlBySax":
                fileData = new DocxConvertorService().docxToHtmlBySax(multipartFile);
                break;
            case "docxToHtmlPoi":
                fileData = new DocxConvertorService().docxToHtmlPoi(multipartFile);
                break;
            case "pdfToDoc":
                fileData = new PdfConvertorService().pdfToDoc(multipartFile);
                break;
            case "pdfToDocAspos":
                new PdfConvertorService().pdfToDocAspos(multipartFile);
                break;
            case "docsslf4j":
                new PdfConvertorService().docsslf4j(multipartFile);
                break;
            case "docsslf4jV2":
                new PdfConvertorService().docsslf4jV2(multipartFile);
                fileData = "converted";
        }
        if (fileData != null)
            return ResponseEntity.ok().body(fileData);
        return ResponseEntity.badRequest().body("Unable to convert the file");
    }

    @GetMapping("/pdf/download")
    @ApiOperation(value = "Download quotation as PDF api",
            notes = "This Api is used to download quotation as PDF for client")
    public ResponseEntity<InputStreamResource> downloadPdfByClient(
            @RequestParam(required = false, defaultValue = "", value = "fileUrl") String fileUrl) {

        String html = "";
        try {
            URL url = new URL(fileUrl);

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
            html = builder.toString();
//            html = prepareHtmlForPdfService.demo(builder.toString());
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        ByteArrayInputStream byteArrayInputStream = PdfGeneratorUtil.generatePdf(document.html());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(byteArrayInputStream));
    }


    @GetMapping("/csv")
    public void getCsv(HttpServletResponse response) {
        String filename = "someFileName.csv";
        List<User> users = new ArrayList<>();
        users.add(new User(1, "ja", "ka"));
        users.add(new User(2, "ja", "ka"));
        users.add(new User(3, "ja", "ka"));
        users.add(new User(4, "ja", "ka"));
        try {
            CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                    CSVFormat.DEFAULT.withHeader("field1", "field2", "field3"));
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"");
            for (User user : users) {
                csvPrinter.printRecord(Arrays.asList(user.getId(), user.getName(), user.getEmail()));
            }
            csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private PrepareHtmlForPdfService prepareHtmlForPdfService;

}
