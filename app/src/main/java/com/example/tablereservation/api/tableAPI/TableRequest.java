package com.example.tablereservation.api.tableAPI;

public class TableRequest {

    private String restaurantId;
    private int tableNumber;
    private String area;

    public TableRequest(int tableNumber, String area, String restaurantId) {
        this.restaurantId = restaurantId;
        this.tableNumber = tableNumber;
        this.area = area;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
