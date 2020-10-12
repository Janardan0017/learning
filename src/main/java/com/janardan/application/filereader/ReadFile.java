package com.janardan.application.filereader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created for janardan on 10/10/20
 */
public class ReadFile {

    public static void main(String[] args) throws IOException {

        Pattern pattern = Pattern.compile("\\w+Api.java");

        File[] files = new File("/").listFiles();

//        try (Stream<Path> paths = Files.walk(Paths.get("/home/janardan/Documents/janardan/src/main/java/com/janardan"))) {
        try (Stream<Path> paths = Files.walk(Paths.get("/home/janardan/Documents/Zeal-Backend-Java.git/src/main/java/com/zeal/application/module/v1"))) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        Path fileName = file.getFileName();
                        Matcher matcher = pattern.matcher(fileName.toString());
                        if (matcher.matches()) {
                            try {
                                File file1 = file.toFile();
                                InputStream inputStream = new FileInputStream(file1);
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                String line = bufferedReader.readLine();
                                StringBuilder stringBuilder = new StringBuilder();
                                while (line != null) {
                                    System.out.println(line);
                                    stringBuilder.append(line);
                                    line = bufferedReader.readLine();
                                }
                                System.out.println(stringBuilder.toString());
                                stringBuilder.reverse();
                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1));
                                bufferedWriter.write(stringBuilder.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(fileName);
                            System.exit(0);
                        }
                    });
        }
    }
}
