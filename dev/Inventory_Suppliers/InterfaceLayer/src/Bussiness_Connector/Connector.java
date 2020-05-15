package Bussiness_Connector;

import java.util.Date;
import java.util.Map;
import BusinessLayer.Inventory;

public class Connector {

    private static Connector instance = null;

    public static Connector getInstance(){
        if(instance == null){
            instance = new Connector();
        }

        return instance;
    }

    //TODO: complete the function
    public int sendLackOfItemOrder(int productId, int amount, int price){
        return 0;
    }

    //TODO: complete the function
    //status 0 - add
    // status 1 - remove
    public boolean setPeriodicOrder(int orderId, Map<Integer, Integer> toSet, int status){
        if(status == 0){
            //TODO: add
            return true;
        }
        else if(status == 1){
            //TODO: remove
            return true;
        }

        return false;
    }

    //TODO: complete the function
    public boolean changePeriodicOrderDate(int orderId, Date newDate){
        return true;
    }

    public void loadInventory(int orderID, Map<Integer, Integer> toLoad){
        Inventory.getInstance().loadInventory(orderID, toLoad);
    }
}
