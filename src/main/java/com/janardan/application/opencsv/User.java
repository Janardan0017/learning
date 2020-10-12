package com.janardan.application.opencsv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

/**
 * Created for janardan on 2/10/20
 */
public class User {

//    @CsvBindByPosition(position = 0)
//    @CsvBindByName(column = "ID")
    private int id;

    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "Name aa")
    private String name;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "Email bb")
    private String email;

    public User() {
    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + email;
    }
}
