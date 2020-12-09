package com.janardan.application.coding;

import java.util.Objects;

/**
 * Created for janardan on 26/11/20
 */
public class ChartData {

    private int id;
    private int status;
    private String name;

    public ChartData(int id, int status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartData chartData = (ChartData) o;
        return id == chartData.id &&
                status == chartData.status &&
                Objects.equals(name, chartData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, name);
    }

    @Override
    public String toString() {
        return "ChartData{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }
}
