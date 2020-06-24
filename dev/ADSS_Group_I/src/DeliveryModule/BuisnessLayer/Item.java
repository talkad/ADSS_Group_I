package DeliveryModule.BuisnessLayer;

public class Item {
    private String Name;
    private int Quantity;

    public Item (String Name, int quantity){
        this.Name = Name;
        this.Quantity = quantity;
    }
    
    @Override
    public String toString()
    {
    	return Quantity + " X " + Name;  
    }
}
