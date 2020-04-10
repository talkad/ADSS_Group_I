package BusinessLayer;

import java.util.Date;

import DTO.ItemDTO;

public class Item {
    private int id;
    private boolean isDefect;
    private Date expiryDate;
    private String location;

    public Item(ItemDTO itemDTO){
        this.id = itemDTO.getId();
        this.isDefect = itemDTO.isDefect();
        this.expiryDate = itemDTO.getExpiryDate();
        this.location = itemDTO.getLocation();
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
