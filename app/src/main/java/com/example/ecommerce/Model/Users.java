package com.example.ecommerce.Model;

public class Users {

    public String name,pass,pnumber,image,address;

    public Users(){

    }

    public Users(String name, String pass, String pnumber, String image, String address) {
        this.name = name;
        this.pass=pass;
        this.pnumber = pnumber;
        this.image = image;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
