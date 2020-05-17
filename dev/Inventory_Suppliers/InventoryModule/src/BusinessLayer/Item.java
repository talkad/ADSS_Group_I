package BusinessLayer;

import DTO.ItemDTO;

import java.time.LocalDate;

public class Item {
    private int orderID;
    private int count;
    private int numOfDefects;
    private LocalDate expiryDate;
    private String location;

    public Item(ItemDTO itemDTO){
        this.orderID = itemDTO.getOrderID();
        this.count = itemDTO.getCount();
        this.numOfDefects = itemDTO.getNumOfDefects();
        this.expiryDate = itemDTO.getExpiryDate();
        this.location = itemDTO.getLocation();
    }

    public Item(int orderID, int count, int numOfDefects, LocalDate expiryDate, String location) {
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

    /**
     * subtracts 1 from count
     * @return if count is 0 after the operation
     */
    public boolean removeOne(){
        this.count--;

        if(this.numOfDefects > 0){
            this.numOfDefects--;
        }

        return this.count == 0;
    }

    /**
     * 
     * @return an information about the item if it's defected or expired, otherwise returns null
     */
    public String defectInfo(){
        LocalDate currentDate = LocalDate.now(); // creates a Date object with the current date

        if(this.numOfDefects > 0 || expiryDate.compareTo(currentDate) == -1){
            String info = "Items order ID: " + this.orderID + "\n";
            info += "Expiration date: " + this.expiryDate + "\n";

            info += "Status: ";
            if(this.numOfDefects > 0){
                info += "There are " + this.numOfDefects +" Defected Items";

                if(expiryDate.compareTo(currentDate) == -1){
                    info += ", items are expired";
                }

                info += "\n";
            }
            else{
                info += "Items are expired\n";
            }
         
            info += "Located in: " + this.location + "\n";
        
            return info;
        }

        return null;
    }

    @Override
    public String toString(){
        String toString = "Item order ID: " + this.orderID + "\n";

        if(this.numOfDefects > 0){
            toString += "Status: There are " + this.numOfDefects + " Defected items\n";
        }
        else{
            toString += "Status: Non are defected\n";
        }

        toString += "Expiration date: " + this.expiryDate + "\n";
        toString += "Located in: " + this.location + "\n";
        
        return toString;
    }


}
