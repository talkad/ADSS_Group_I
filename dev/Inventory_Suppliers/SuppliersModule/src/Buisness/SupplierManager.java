package Buisness;

import DAL.ArrangementMapper;
import DAL.OrderMapper;
import DAL.SupplierMapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        if(!_suppliers.get(companyId).removeContact(contactName)) return false;
        return SupplierMapper.getInstance().deleteContact(companyId, contactName).isSuccessful();
    }


    public static int placeLackOfInventory(int itemID, int amount, double price){
        int orderID = -1;
        int supplierID = -1;
        double discount = price;
        boolean quantityAgreement = false;
        int cheapestSup = -1;
        double cheapestDiscount = price;
        double tempDiscount;
        for (SupplierCard supplier: SupplierMapper.getInstance().getAll().values()){



            Arrangement arr = ArrangementMapper.getInstance().getArrangement(supplier.getCompanyId());
            if (arr != null && !arr.getItems().containsKey(itemID))
                continue;
            else if(cheapestSup == -1)
            {
                cheapestSup = supplier.getCompanyId();
            }
            else if(!quantityAgreement || ArrangementMapper.getInstance().getQuantity(supplier.getCompanyId()) != null){
                Map<Integer,Order> map = OrderMapper.getInstance().getOrders(supplier.getCompanyId());
                for (Order order: map.values()){
                    if (!order.getOrderDate().isAfter(LocalDate.now().plusDays(1)))
                        continue;
                    if (order.getItemList().containsKey(itemID)){
                        if (supplier.getArrangement().get_quantityAgreement() != null){
                            tempDiscount = supplier.getArrangement().get_quantityAgreement().checkDiscount(itemID,amount + order.getItemList().get(itemID));
                            if(supplier.getArrangement().get_quantityAgreement().checkDiscount(itemID, amount) < cheapestDiscount){
                                cheapestDiscount = supplier.getArrangement().get_quantityAgreement().checkDiscount(itemID, amount);
                                cheapestSup = supplier.getCompanyId();
                            }
                        }
                        else if(orderID == -1) {
                            orderID = order.getOrderNum();
                            supplierID = supplier.getCompanyId();
                            continue;
                        }
                        else{
                            cheapestSup = supplier.getCompanyId();
                            continue;
                        }
                        if (tempDiscount < discount){
                            discount = tempDiscount;
                            orderID = order.getOrderNum();
                            supplierID = supplier.getCompanyId();
                            quantityAgreement = true;
                        }
                    }
                }
            }
            else{
                cheapestSup = supplier.getCompanyId();
            }
        }
        if (orderID != -1 && supplierID != -1){
            Map<Integer, Integer> map = OrderMapper.getInstance().getOrder(orderID).getItemList();
            map.put(itemID,map.get(itemID)+amount);
            OrderMapper.getInstance().saveOrderItems(supplierID,map);
        }
        else if ((orderID == -1 && cheapestSup != -1)){
            Map<Integer,Integer> map = new HashMap<>();
            map.put(itemID,amount);
            if(SupplierMapper.getInstance().getSupplier(cheapestSup).placeOrder(map, LocalDate.now().plusDays(2)))
                return _orderNumIncrement - 1;
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

    public static Map<Integer,Integer> orderItems(int orderId){
        Order order = OrderMapper.getInstance().getOrder(orderId);

        if(order == null)
            return null;

        if (!order.getStatus().equals("Pending") || order.getOrderDate().isAfter(LocalDate.now()))
            return null;
        else
            return order.getItemList();
    }
}
