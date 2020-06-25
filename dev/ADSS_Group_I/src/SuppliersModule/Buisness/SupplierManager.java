package SuppliersModule.Buisness;


import DeliveryModule.BuisnessLayer.Supplier;
import InventoryModule.DataAccessLayer.ProductMapper;
import SuppliersModule.DAL.ArrangementMapper;
import SuppliersModule.DAL.OrderMapper;
import SuppliersModule.DAL.SupplierMapper;
import SuppliersModule.Buisness.Arrangement;
import Interface.Bussiness_Connector.Connector;
import SuppliersModule.Buisness.Order;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.portable.ApplicationException;

public class SupplierManager {


    private static Map<Integer,SupplierCard> _suppliers = new HashMap<Integer, SupplierCard>();
    private static int _orderNumIncrement = 0;

    //added
    public static void load(){
        setSuppliers(SupplierMapper.getInstance().getAll());
    }

    public static int get_orderNumIncrement() {
        if (_orderNumIncrement == 0) {
            int max = OrderMapper.getInstance().getMaxOrder();
            if (max < 0) {
                _orderNumIncrement = 1;
            }
            else
                _orderNumIncrement = max + 1;
        }
        return _orderNumIncrement;
    }

    public static void incrementOrderNum() {
        _orderNumIncrement++;
    }

    public static Map<Integer, SupplierCard> getSuppliers() {
        if (_suppliers.isEmpty())
            return SupplierMapper.getInstance().getAll();
        return _suppliers;
    }

    public static void setSuppliers(Map<Integer, SupplierCard> _suppliers) {
        SupplierManager._suppliers = _suppliers;
    }

    public static boolean addSupplier (String name, String manufacturer, int companyId, int bankAccount, String paymentConditions, String arrangementType, boolean selfPickup){
        SupplierCard newSupplier = new SupplierCard(name,manufacturer,companyId,bankAccount,paymentConditions,arrangementType,selfPickup);
        if (_suppliers.containsKey(companyId)) return false;
        //added
        if(!SupplierMapper.getInstance().addMapper(newSupplier).isSuccessful()) return false;
        _suppliers.put(companyId, newSupplier);
        return true;
    }

    public static boolean deleteSupplier (int id) {
        if (!_suppliers.containsKey(id)) return false;
        //added
        if (!SupplierMapper.getInstance().deleteMapper(id).isSuccessful()) return false;
        _suppliers.remove(id);
        return true;
    }

    public static boolean addContact (int companyId, String contactName, Map<String,String> details){
        ContactPerson person = new ContactPerson(contactName,details);
        if (!_suppliers.get(companyId).addContact(person))return false;
        return SupplierMapper.getInstance().addContact(companyId,person).isSuccessful();
    }

    public static boolean deleteContact (int companyId, String contactName){
        return _suppliers.get(companyId).removeContact(contactName);
    }

    public static int findCheapestSupplier(int itemId, int amount, int supermarketId){
        int supplierId = -1;
        boolean QE = false;
        double cheapestPrice = ProductMapper.getInstance().getProduct(itemId, supermarketId).getBuyingPrice();
        double discount = 0;
        for (SupplierCard supplier: SupplierMapper.getInstance().getAll().values()) {
            Arrangement arr = ArrangementMapper.getInstance().getArrangement(supplierId);
            if (arr != null && !arr.getItems().containsKey(itemId))
                continue;
            if (QE && supplier.getArrangement().get_quantityAgreement() == null)
                continue;
            else if (QE){
                discount = supplier.getArrangement().get_quantityAgreement().checkDiscount(itemId,amount);
                if (discount > 0 && discount < cheapestPrice ){
                    supplierId = supplier.getCompanyId();
                    cheapestPrice = discount;
                }
            }
            else if (supplierId == -1){
                if (supplier.getArrangement().get_quantityAgreement() == null || supplier.getArrangement().get_quantityAgreement().checkDiscount(itemId,amount) == -1 )
                    supplierId = supplier.getCompanyId();
                else{
                    QE  = true;
                    supplierId = supplier.getCompanyId();
                    cheapestPrice = supplier.getArrangement().get_quantityAgreement().checkDiscount(itemId,amount);
                }
            }
        }
        return supplierId;
    }

    public static int placeLackOfInventory(int itemID, int amount,int supermarketId) throws NumberFormatException, IOException, ParseException, ApplicationException{
        int orderID = -1;
        int supplierID = findCheapestSupplier(itemID,amount,supermarketId);
        if (supplierID == -1)
            return -1;
        Map<Integer,Integer> map = new HashMap<>();
        map.put(itemID,amount);
        String[] closestOrderValues = Connector.getPreviousDateForDelivery(map, supermarketId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate closestDate = LocalDate.parse(closestOrderValues[0],formatter);

        boolean newOrder = !closestDate.isEqual(LocalDate.now().plusDays(8));
        for (SupplierCard supplier: SupplierMapper.getInstance().getAll().values()){
            Arrangement arr = ArrangementMapper.getInstance().getArrangement(supplier.getCompanyId());
            if (arr != null && !arr.getItems().containsKey(itemID))
                continue;
            for (Order order : OrderMapper.getInstance().getOrders(supplier.getCompanyId()).values()){
                if (order.getStatus() != "pending" || !order.getOrderDate().isBefore(closestDate) || order.getSupermarketID() != supermarketId)
                    continue;
                else if (Connector.isPossibleUpdateForm(map,orderID)) {
                    closestDate = order.getOrderDate();
                    orderID = order.getOrderNum();
                    supplierID = OrderMapper.getInstance().getSupplier(orderID);
                    newOrder = false;
                }
            }
        }
        if(newOrder){
            if(SupplierManager.getSuppliers().get(supplierID).placeOrder(map, closestDate, supermarketId))
                Connector.createFormFinal(closestOrderValues, _orderNumIncrement - 1);
        }
        else{
            SupplierManager.getSuppliers().get(supplierID).getOrders().get(orderID).addItems(map);
        }
        return orderID;
    }


    public static boolean setPeriodicOrder(int orderId, Map<Integer, Integer> toSet, int status){
        int companyID = OrderMapper.getInstance().getSupplier(orderId);
        SupplierCard supplier = SupplierMapper.getInstance().getSupplier(companyID);
        if (status == 1)
            return supplier.deleteItemsFromOrder(toSet, orderId);
        else
            return supplier.addItemsToOrder(toSet, orderId);
    }

    public static boolean changePeriodicOrderDate(int orderId, LocalDate newDate){
        if (!newDate.isAfter(LocalDate.now().plusDays(1)))
            return false;
        LocalDate date = OrderMapper.getInstance().getDate(orderId);
        if (date.isAfter(LocalDate.now().plusDays(1)))
            return false;
        else {
            OrderMapper.getInstance().updateDate(orderId, newDate);
            return true;
        }

    }

    public static Map<Integer,Integer> orderItems(int orderId,int supermarketId){
        Order order = OrderMapper.getInstance().getOrder(orderId);

        if(order == null)
            return null;

        if (order.getStatus() != "delivered" || order.getSupermarketID() != supermarketId )
            return null;
        else
            return order.getItemList();
    }

    public static Result DeliverOrder (int orderId){
        Order order = OrderMapper.getInstance().getOrder(orderId);
        if (order.isPeriodic()){
            SupplierManager.getSuppliers().get(OrderMapper.getInstance().getSupplier(orderId)).placeOrder(order.getItemList(),order.getOrderDate().plusDays(7),order.getSupermarketID());
        }
        return OrderMapper.getInstance().updateOrderStatus(orderId, "delivered");
    }

    public static Result VerifyOrder(int orderId){
        return OrderMapper.getInstance().updateOrderStatus(orderId, "accepted");
    }
    
    public static Order getOrder(int orderID)
    {
    	return OrderMapper.getInstance().getOrder(orderID);
    }
    public static int getSitebyOrder(int orderID)
    {
    	return OrderMapper.getInstance().getOrder(orderID).getSupermarketID();
    }
    public static int getSupplierbyOrder(int orderID)
    {
    	return OrderMapper.getInstance().getSupplier(orderID);
    } 
    
    public static Map<Integer, Integer> getItemListString(int orderid) {
    	return OrderMapper.getInstance().getItems(orderid);
    }
    
}
