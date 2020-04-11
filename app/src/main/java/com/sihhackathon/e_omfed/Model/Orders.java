package com.sihhackathon.e_omfed.Model;

public class Orders {

    private String total_price,Shipment_Name, Shipment_Address, Shipment_Cityname, Shipment_Pincode, Shipment_Contact, state, date , time,order_id;

    public Orders() {
    }

    public Orders(String total_price, String shipment_Name, String shipment_Address, String shipment_Cityname, String shipment_Pincode, String shipment_Contact, String state, String date, String time, String order_id) {
        this.total_price = total_price;
        Shipment_Name = shipment_Name;
        Shipment_Address = shipment_Address;
        Shipment_Cityname = shipment_Cityname;
        Shipment_Pincode = shipment_Pincode;
        Shipment_Contact = shipment_Contact;
        this.state = state;
        this.date = date;
        this.time = time;
        this.order_id = order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getShipment_Name() {
        return Shipment_Name;
    }

    public void setShipment_Name(String shipment_Name) {
        Shipment_Name = shipment_Name;
    }

    public String getShipment_Address() {
        return Shipment_Address;
    }

    public void setShipment_Address(String shipment_Address) {
        Shipment_Address = shipment_Address;
    }

    public String getShipment_Cityname() {
        return Shipment_Cityname;
    }

    public void setShipment_Cityname(String shipment_Cityname) {
        Shipment_Cityname = shipment_Cityname;
    }

    public String getShipment_Pincode() {
        return Shipment_Pincode;
    }

    public void setShipment_Pincode(String shipment_Pincode) {
        Shipment_Pincode = shipment_Pincode;
    }

    public String getShipment_Contact() {
        return Shipment_Contact;
    }

    public void setShipment_Contact(String shipment_Contact) {
        Shipment_Contact = shipment_Contact;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
