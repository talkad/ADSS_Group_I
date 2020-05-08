package DTO;

import java.util.Date;

public class ItemDTO {
    private int orderId;
    private int count;
    private int numOfDefects;
    private Date expiryDate;
    private String location;

    public ItemDTO(int orderId, int count, int numOfDefects, Date expiryDate, String location) {
        this.orderId = orderId;
        this.count = count;
        this.numOfDefects = numOfDefects;
        this.expiryDate = expiryDate;
        this.location = location;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int id) {
        this.orderId = id;
    }

    public int getNumOfDefects() {
        return numOfDefects;
    }

    public void setNumOfDefects(int isDefect) {
        this.numOfDefects = isDefect;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
