package com.sihhackathon.e_omfed.Model;

public class Admins {

    private String name,password,phone_number;
    public Admins()
    {

    }

    public Admins(String name, String password, String phone_number) {
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
