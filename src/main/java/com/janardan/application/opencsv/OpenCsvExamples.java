package com.janardan.application.opencsv;

import com.janardan.common.DbConnection;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created for janardan on 2/10/20
 */
public class OpenCsvExamples {

    public static void main(String[] args) {
//        writeFromStringArray();
        writeFromBeanList();
    }

    public static void writeFromStringArray() {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter("example.csv"));
            String[] entries = "my name is janardan chaudhary".split(" ");
            csvWriter.writeNext(entries, false);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFromBeanList() {
        List<User> users = DbConnection.getUsers();
        try {
            Writer writer = new FileWriter("example2.csv");
            writer.append("Email,Name\n");

            ColumnPositionMappingStrategy<User> columnPositionMappingStrategy = new ColumnPositionMappingStrategy<>();
            columnPositionMappingStrategy.setType(User.class);

            StatefulBeanToCsv<User> beanToCsv = new StatefulBeanToCsvBuilder<User>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(columnPositionMappingStrategy)
                    .build();
            beanToCsv.write(users);
            writer.close();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }
}
