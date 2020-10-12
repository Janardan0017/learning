package com.janardan.application.convertor;

import java.text.SimpleDateFormat;

/**
 * Created for janardan on 7/10/20
 */
public class DateConvertor {

    public static void main(String[] args) {
        String format = new SimpleDateFormat("MMM d, yyyy").format(1591943905626L);
        System.out.println(format);
    }
}
