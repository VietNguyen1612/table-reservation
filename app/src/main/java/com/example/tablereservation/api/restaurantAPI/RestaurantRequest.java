package com.example.tablereservation.api.restaurantAPI;

import java.io.Serializable;
import java.util.List;

public class RestaurantRequest implements Serializable {
    private String name;
    private String address;
    private String description;
    private String price;
    private List<String> image;

    public RestaurantRequest(String name, String address, String description, String price, List<String> image) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.price = price;
        this.image = image;
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
}
