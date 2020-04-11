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

    /**
     * 
     * @return an information about the item if it's defected or expired, otherwise returns null
     */
    public String defectInfo(){
        Date currentDate = new Date(); // creates a Date object with the current date

        if(this.isDefect || expiryDate.compareTo(currentDate) == -1){
            String info = "Item ID: " + this.id + "\n";
            info += "Experation date: " + this.expiryDate + "\n";

            info += "Status: ";
            if(this.isDefect){
                info += "Defect";

                if(expiryDate.compareTo(currentDate) == -1){
                    info += ", Exipred\n";
                }
            }
            else{
                info += "Expired\n";
            }
         
            info += "Located in: " + this.location + "\n";
        
            return info;
        }

        return null;
    }

    @Override
    public String toString(){
        String toString = "Item ID: " + this.id + "\n";

        if(this.isDefect){
            toString += "Status: Defect\n";
        }
        else{
            toString += "Status: Not defect\n";
        }

        toString += "Experation date: " + this.expiryDate + "\n";
        toString += "Located in: " + this.location + "\n";
        
        return toString;
    }

}
