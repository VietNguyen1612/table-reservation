package com.example.tablereservation.model;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {
    private String _id;
    private String name;
    private String address;
    private String description;
    private String price;
    private List<String> image;

    private String status;

    public Restaurant(String name, String address, String description, String price, List<String> image) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }
}
