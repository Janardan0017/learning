package com.janardan.application.filereader;

import java.io.File;

/**
 * Created for janardan on 11/10/20
 */
public class ReadFile2 {

    public static void main(String[] args) {
        File[] files = new File("/home/janardan/Documents/Zeal-Backend-Java.git/src/main/java/com/zeal/application/module/v1").listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
