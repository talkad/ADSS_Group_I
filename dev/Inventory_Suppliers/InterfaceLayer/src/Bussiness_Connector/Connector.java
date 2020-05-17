package Bussiness_Connector;

import BusinessLayer.Inventory;

import java.time.LocalDate;
import java.util.Map;

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
            //SuperLi.setPeriodicOrder(orderId,toSet,status);
            return true;
        }
        else{
            return false; // Superli.setPeriodicOrder(orderId,toSet,status);
        }
    }

    //TODO: complete the function
    public boolean changePeriodicOrderDate(int orderId, LocalDate newDate){ //TODO change in iventory module to use LocalDate
        return false; //Superli.ChangePeriodicOrderDate(orderId,newDate);
    }

    public void loadInventory(int orderID, Map<Integer, Integer> toLoad){
        Inventory.getInstance().loadInventory(orderID, toLoad);
    }
}