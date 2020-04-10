package DTO;

import java.util.Date;

//TODO: do we need setters?
public class ItemDTO {
    private int id;
    private boolean isDefect;
    private Date expiryDate;
    private String location;

    public ItemDTO(int id, boolean isDefect, Date expiryDate, String location) {
        this.id = id;
        this.isDefect = isDefect;
        this.expiryDate = expiryDate;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDefect() {
        return isDefect;
    }

    public void setDefect(boolean isDefect) {
        this.isDefect = isDefect;
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
}
