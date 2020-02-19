package com.example.ecommerce.Model;

public class AdminOrders
{
    private String name,pnumber,address,city,date,state,time,Total_Amount;

    public AdminOrders()
    {

    }

    public AdminOrders(String name, String pnumber, String address, String city, String date, String state, String time, String total_Amount) {
        this.name = name;
        this.pnumber = pnumber;
        this.address = address;
        this.city = city;
        this.date = date;
        this.state = state;
        this.time = time;
        Total_Amount = total_Amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }
}
