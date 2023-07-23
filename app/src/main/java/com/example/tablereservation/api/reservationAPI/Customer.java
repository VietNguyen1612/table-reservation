package com.example.tablereservation.api.reservationAPI;

public class Customer {
    private String _id;
    private String name;
    private String address;
    private String phone;

    public Customer(String _id, String name, String address, String phone) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
