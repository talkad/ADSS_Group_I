package DTO;

import java.time.LocalDate;

public class ItemDTO {
    private int orderID;
    private int count;
    private int numOfDefects;
    private LocalDate expiryDate;
    private String location;


    public ItemDTO(int orderID, int count, int numOfDefects, LocalDate expiryDate, String location) {
        this.orderID = orderID;
        this.count = count;
        this.numOfDefects = numOfDefects;
        this.expiryDate = expiryDate;
        this.location = location;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNumOfDefects() {
        return numOfDefects;
    }

    public void setNumOfDefects(int numOfDefects) {
        this.numOfDefects = numOfDefects;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
