package com.janardan.application.convertor;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
                                                      "pdfToHtmlByPdfBox,pdfToHtmlAspos,docToHtmlPoi,docToHtmlByMammoth,docxToHtmlByMammoth")
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
        }
        if (fileData != null)
            return ResponseEntity.ok().body(fileData);
        return ResponseEntity.badRequest().body("Unable to convert the file");
    }

}
