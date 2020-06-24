package DeliveryModule.BusinessLayer;

import java.util.ArrayList;
import java.util.List;

public class ItemList {
    private List<Item> ItemList;
    private int ListNumber;
    private static int counter = 1;

    public ItemList(int ListNumber){
        this.ListNumber = ListNumber;
        this.ItemList = new ArrayList<>();
    }

    //auto count
    public ItemList(){
        this.ListNumber = counter;
        counter++;
        this.ItemList = new ArrayList<>();
    }
    
    public void addItem(Item item){
        ItemList.add(item);
    }
    
    @Override
    public String toString()
    {
    	String ans = "List Number: " + ListNumber + "\n";
    	int counter = 1;
    	for (Item item : ItemList) {
			ans += counter +". " + item.toString() + "\n";
			counter++;
		}
    	return ans;
    	
    }
}
