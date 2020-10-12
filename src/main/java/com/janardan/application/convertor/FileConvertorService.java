package com.janardan.application.convertor;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by emp350 on 04/02/20
 */
public interface FileConvertorService {

    String readFile(MultipartFile multipartFile, String target) throws IOException;

    String readFromInputStream(InputStream inputStream) throws IOException;
}
