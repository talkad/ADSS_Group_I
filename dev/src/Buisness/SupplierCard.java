package Buisness;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierCard {

    private String _name;
    private String _manufacturer;
    private int _companyId;
    private int _bankAccount;
    private String _paymentConditions;
    private Arrangement _arrangement;
    private ArrayList<ContactPerson> _contacts;
    private Map<Integer,Order> _orders;
    private int _orderNumIncrement = 0;

    public SupplierCard(String name, String manufacturer, int companyId, int bankAccount, String paymentConditions, String arrangementType, boolean selfPickup){
        _name = name;
        _manufacturer = manufacturer;
        _companyId = companyId;
        _bankAccount = bankAccount;
        _paymentConditions = paymentConditions;
        if (arrangementType.toUpperCase().equals("FIXED"))
            _arrangement = new FixedArrangement(LocalDate.now(),selfPickup);
        else
            _arrangement = new SingleArrangement(selfPickup);
        _contacts = new ArrayList<>();
        _orders = new HashMap<>();
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getManufacturer() {
        return _manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this._manufacturer = manufacturer;
    }

    public int getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(int companyId) {
        this._companyId = companyId;
    }

    public int getBankAccount() {
        return _bankAccount;
    }

    public void setBankAccount(int bankAccount) {
        this._bankAccount = bankAccount;
    }

    public String getPaymentConditions() {
        return _paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this._paymentConditions = paymentConditions;
    }

    public Arrangement getArrangement() {
        return _arrangement;
    }

    public void setArrangement(Arrangement arrangement) {
        this._arrangement = _arrangement;
    }

    public ArrayList<ContactPerson> getContacts() {
        return _contacts;
    }

    public ContactPerson getContactByName (String name) {
        for (ContactPerson person: _contacts) {
            if (person.getName().equals(name))
                return person;
        }
        return null;
    }

    public void setContacts(ArrayList<ContactPerson> contacts) {
        this._contacts = _contacts;
    }

    public Map<Integer, Order> getOrders() { return _orders; }

    public void setOrders(Map<Integer, Order> _orders) { this._orders = _orders; }

    public boolean placeOrder(Map<Integer, Integer> items, LocalDate date){
        for (int item: items.keySet()){
            if (!_arrangement.getItems().containsKey(item))
                return false;
        }
        Order order = new Order(_orderNumIncrement,date, "Pending", items);
        _orders.put(_orderNumIncrement,order);
        _arrangement.get_deliveryDates().getDates().put(date, false);
        _orderNumIncrement++;
        return true;
    }

    public boolean addContact(ContactPerson contact){
        if (contact == null || _contacts.contains(contact))
            return false;
        else{
            _contacts.add(contact);
            return true;
        }
    }

    public boolean removeContact(String contact){
        if (contact == null)
            return false;
        for (ContactPerson person: _contacts){
            if (person.getName().equals(contact)){
                _contacts.remove(person);
                return true;
            }
        }
        return false;
    }

    public boolean cancelOrder (int orderNum){
        if(!_orders.containsKey(orderNum))
            return false;
        else{
            _orders.get(orderNum).setStatus("Canceled");
            _orders.get(orderNum).setOrderDate(null);
            return true;
        }
    }

    public boolean addItemsToOrder(Map<Integer,Integer> items, int orderNumber){
        if(!_arrangement.validateItems(items.keySet())) return false;
        Order toAdd = _orders.get(orderNumber);
        toAdd.addItems(items);
        return true;
    }

    public boolean deleteItemsFromOrder(Map<Integer,Integer> items, int orderNumber){
        Order toDelete = _orders.get(orderNumber);
        return toDelete.deleteItems(items);
    }

    public void addToArrangement (ArrayList<Item> items){
        for (Item item: items){
            _arrangement.getItems().put(item.getId(),item);
        }
    }

    public void deleteFromArrangement (ArrayList<Integer> items){
        for (Integer item: items){
            _arrangement.getItems().remove(item);
        }
    }

    public void changePriceInAgreement (Map<Integer,Double> items){
        for (Integer item: items.keySet()){
            _arrangement.getItems().get(item).setPrice(items.get(item));
        }
    }

    public String toString(){
        String contacts ="";
        for (ContactPerson person:_contacts)
            contacts+=person.getName()+" ";

        return "Company Id: " + _companyId + "\nName: " + _name + "\nManufacturer: " + _manufacturer + "\nBank account:" + _bankAccount
                + "\nPayment conditions: " + _paymentConditions + "\nContacts: " + contacts;
    }

    public String contactsToString(){
        String output = "";
        for (ContactPerson person : _contacts)
            output += person.toString();
        return output;
    }

}
