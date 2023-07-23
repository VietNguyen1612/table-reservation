package com.example.tablereservation.model;

public class Table {
    private String _id;
    private int tableNumber;
    private String area;

    public Table(int tableNumber, String area) {
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
