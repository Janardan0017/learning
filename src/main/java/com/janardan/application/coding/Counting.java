package com.janardan.application.coding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created for janardan on 26/11/20
 */
public class Counting {

    public static void main(String[] args) {
        
    }

    public static List<String> getLabels(Long startDate, Long endDate){
        if(startDate == null)
            startDate = System.currentTimeMillis();
        if(endDate == null)
            endDate = startDate + 31556952000L;
        List<String> dates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        long oneDayMillis = 24*60*60*1000L;
        while(endDate >= startDate){
            dates.add(dateFormat.format(startDate));
            startDate += oneDayMillis;
        }
        return dates;
    }
}
