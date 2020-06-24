package Interface.Bussiness_Connector;


import DeliveryModule.InterfaceLayer.DoThinks;
import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.BusinessLayer.mainBL;
import InventoryModule.Business.Inventory;
import SuppliersModule.Buisness.SupplierManager;
import org.omg.CORBA.portable.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        return SupplierManager.placeLackOfInventory( productId, amount, price);
    }

    public boolean setPeriodicOrder(int orderId, Map<Integer, Integer> toSet, int status, int storeID){
        if(status == 0){
            SupplierManager.setPeriodicOrder(orderId,toSet,status);
            return true;
        }
        else{
            return  SupplierManager.setPeriodicOrder(orderId,toSet,status);
        }
    }

    public boolean changePeriodicOrderDate(int orderId, LocalDate newDate, int storeID){
        return SupplierManager.changePeriodicOrderDate(orderId,newDate);
    }


    public Map<Integer,Integer> tryLoadInventory(int orderID, int storeID){
        return  SupplierManager.orderItems(orderID, storeID);
    }

    public void addDriver(Employee employee){
        try {
            DoThinks.addDriver(employee, new ArrayList<>());
        } catch(Exception ignored){}
    }

    public void AddOrEditDriver(int id ,Employee employee) throws ApplicationException {
            DoThinks.AddOrEditDriver(id ,employee);
    }

    public void RemoveDriver(int id) throws ApplicationException {
        DoThinks.RemoveDriver(id);
    }

    public List<String> getAvailableDrivers(String shiftTime) throws ApplicationException {
        return mainBL.getInstance().getAvailableDrivers(shiftTime);
    }

    public boolean addDriverToShift(String shiftTime, int id) throws ApplicationException {
        return mainBL.getInstance().addDriverToShift(shiftTime, id);
    }

    public boolean isEmployeeInShift(int id, String shiftTime) throws ApplicationException {
        return mainBL.getInstance().isEmployeeInShift(id, shiftTime);
    }

    public List<String> getDriversInShift(String shiftTime) {
        return mainBL.getInstance().getDriversInShift(shiftTime);
    }

    public double getWeightItem(int itemID, int storeID) {
        return Inventory.getInstance().getProductsWeight(itemID,storeID);
    }
}