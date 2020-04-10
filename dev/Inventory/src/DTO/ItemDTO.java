package DTO;

import java.util.Date;

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

}
