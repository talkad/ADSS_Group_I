import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SuperLi {

    private static Map<Integer,SupplierCard> _suppliers = new HashMap<Integer, SupplierCard>();
    private static int _orderNumIncrement = 0;


    public static int get_orderNumIncrement() {
        return _orderNumIncrement;
    }

    public static void incrementOrderNum() {
        _orderNumIncrement++;
    }

    public static Map<Integer, SupplierCard> getSuppliers() {
        return _suppliers;
    }

    public static void setSuppliers(Map<Integer, SupplierCard> _suppliers) {
        SuperLi._suppliers = _suppliers;
    }

    public static boolean addSupplier (String name, String manufacturer, int companyId, int bankAccount, String paymentConditions, String arrangementType, boolean selfPickup){
        SupplierCard newSupplier = new SupplierCard(name,manufacturer,companyId,bankAccount,paymentConditions,arrangementType,selfPickup);
        if (_suppliers.containsKey(companyId)) return false;
        _suppliers.put(companyId, newSupplier);
        return true;
    }

    public static boolean deleteSupplier (int id) {
        if (!_suppliers.containsKey(id)) return false;
        _suppliers.remove(id);
        return true;
    }

    public static boolean addContact (int companyId, String contactName, Map<String,String> detailes){
       return _suppliers.get(companyId).addContact(new ContactPerson(contactName,detailes));
    }

    public static boolean deleteContact (int companyId, String contactName){
        return _suppliers.get(companyId).removeContact(contactName);
    }

    public static int placeLackOfInventory(int itemID, int amount, double price){
        int orderID = -1;
        int supplierID = -1;
        double discount = price;
        boolean quantityAgreement = false;
        int cheapestSup = -1;
        double cheapestDiscount = price;
        double tempDiscount;
        for (SupplierCard supplier: _suppliers.values()){
            if (!supplier.getArrangement().get_items().containsKey(itemID))
                continue;
            else if(!quantityAgreement || supplier.getArrangement().get_quantityAgreement() != null){
                for (Order order: supplier.getOrders().values()){
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
        }
        if (orderID != -1 && supplierID != -1){
            Map<Integer, Integer> map = _suppliers.get(supplierID).getOrders().get(orderID).getItemList();
            map.put(itemID,map.get(itemID)+amount);
        }
        else if ((orderID == -1 && cheapestSup != -1)){
            Map<Integer,Integer> map = new HashMap<>();
            map.put(itemID,amount);
            if(_suppliers.get(cheapestSup).placeOrder(map, LocalDate.now().plusDays(2)))
                return _orderNumIncrement - 1;
        }
        return orderID;
    }
}
