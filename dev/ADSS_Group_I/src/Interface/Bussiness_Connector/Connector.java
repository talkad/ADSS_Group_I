package Interface.Bussiness_Connector;


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

    public int sendLackOfItemOrder(int productId, int amount, int price, int storeID){
        return Buisness.SupplierManager.placeLackOfInventory( productId, amount, price);
    }

    public boolean setPeriodicOrder(int orderId, Map<Integer, Integer> toSet, int status, int storeID){
        if(status == 0){
            Buisness.SupplierManager.setPeriodicOrder(orderId,toSet,status);
            return true;
        }
        else{
            return  Buisness.SupplierManager.setPeriodicOrder(orderId,toSet,status);
        }
    }

    public boolean changePeriodicOrderDate(int orderId, LocalDate newDate, int storeID){
        return Buisness.SupplierManager.changePeriodicOrderDate(orderId,newDate);
    }


    public Map<Integer,Integer> tryLoadInventory(int orderID, int storeID){
        return  Buisness.SupplierManager.orderItems(orderID, storeID);
    }
}