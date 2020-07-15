package Interface.Bussiness_Connector;


import DeliveryModule.InterfaceLayer.DoThinks;
import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.BusinessLayer.mainBL;
import InventoryModule.Business.Inventory;
import SuppliersModule.Buisness.Result;
import SuppliersModule.Buisness.SupplierManager;
import SuppliersModule.DAL.OrderMapper;

import org.omg.CORBA.portable.ApplicationException;

import java.io.IOException;
import java.text.ParseException;
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

    public  int sendLackOfItemOrder(int productId, int amount, int storeID) throws NumberFormatException, IOException, ParseException, ApplicationException{
        return SupplierManager.placeLackOfInventory(productId, amount, storeID);
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

    public boolean hasRole(int id, String role) throws ApplicationException {
        return mainBL.getInstance().hasRole(id, role);
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
    public static boolean isAddressExist(String Address) throws ApplicationException {
    	return DoThinks.isAddressExist(Address);
    }
    public static String[] getPreviousDateForDelivery(Map<Integer, Integer> itemList, int storeID)throws ApplicationException, ParseException, IOException{
    	return DoThinks.getPreviousDateForDelivery(itemList, storeID);
    }
    
    public static void addSupplierID(String address, int SupplierID) throws ApplicationException {
    	DoThinks.addSupplierID(address, SupplierID);
    }
    public static boolean isPossibleUpdateForm(Map<Integer, Integer> newItems, int orderID) throws ApplicationException { 
    	return DoThinks.isPossibleUpdateForm(newItems, orderID);
    }
    
    public static void createFormFinal(String[] setDelivery, int orderID) throws NumberFormatException, IOException, ParseException, ApplicationException { 
    	DoThinks.insertPlannedDelivery(setDelivery, orderID);
    }
    public static int getSitebyOrder(int orderID)
    {
    	return SupplierManager.getSitebyOrder(orderID);
    }
    public Map<Integer, String> getOrdersToSet() {
    	return OrderMapper.getInstance().getOrdersToSet();
    }
    
    public static String getItemListString(int orderid) {
    	Map<Integer, Integer> m = SupplierManager.getItemListString(orderid);
    	StringBuilder str = new StringBuilder();
    	for (Integer product : m.keySet()) {
			str.append(product.toString());
			str.append(" X ").append(m.get(product)).append("\n");
		}
    	return str.toString();
    }

    public static void Load() throws ApplicationException {
        DoThinks.Load();
    }

    public static Result VerifyOrder(int orderId) {
        return SupplierManager.VerifyOrder(orderId);
    }
        public static String getItemName(int itemid)
    {
    	return Inventory.getInstance().getItemName(itemid);
    }
    public static void deliverOrder(int orderID)
    {
    	SupplierManager.DeliverOrder(orderID);
    }
    
    public static int getSource(int orderID)
    {
		return SupplierManager.getSupplierbyOrder(orderID);
    }
    public static int getDest(int orderID)
    {
    	return getSitebyOrder(orderID);
    }

    public static Map<String, Integer> getRequestedOrders(){
        return SupplierManager.getOrderAmount();
    }

    public static void DetermineDelivery() throws ApplicationException, ParseException, IOException {
        DoThinks.DetermineDelivery();
    }
}