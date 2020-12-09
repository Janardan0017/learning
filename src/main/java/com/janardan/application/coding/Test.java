package com.janardan.application.coding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created for janardan on 12/10/20
 */
public class Test {

    public static void main(String[] args) {

        List<ChartData> dataList = new ArrayList<>();
        dataList.add(new ChartData(1, 1, "abc"));
        dataList.add(new ChartData(1, 2, "def"));
        dataList.add(new ChartData(1, 2, "def"));
        dataList.add(new ChartData(2, 2, "abc"));
        dataList.add(new ChartData(2, 2, "abc"));
        dataList.add(new ChartData(3, 1, "abc"));
        dataList.add(new ChartData(3, 2, "abc"));
        dataList.add(new ChartData(3, 2, "abc"));

        System.out.println(dataList);
        System.out.println(new HashSet<>(dataList));

//        dataList.stream().collect(Collectors.groupingBy(ChartData::getName, Collectors.groupingBy(ChartData::getStatus)))
    }
}
