package Buisness;

import java.util.HashMap;
import java.util.Map;

public class SuperLi {

    private static Map<Integer,SupplierCard> _suppliers = new HashMap<Integer, SupplierCard>();

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

    public static boolean addSupplier (SupplierCard supplier){
        if (_suppliers.containsKey(supplier.getCompanyId())) return false;
        _suppliers.put(supplier.getCompanyId(), supplier);
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
}
