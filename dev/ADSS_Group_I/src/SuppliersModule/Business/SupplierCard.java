package SuppliersModule.Business;

import InventoryModule.DataAccessLayer.ProductMapper;
import SuppliersModule.Business.*;
import SuppliersModule.DAL.ArrangementMapper;
import SuppliersModule.DAL.OrderMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public SupplierCard(String name, String manufacturer, int companyId, int bankAccount, String paymentConditions, String arrangementType, boolean selfPickup){
        _name = name;
        _manufacturer = manufacturer;
        _companyId = companyId;
        _bankAccount = bankAccount;
        _paymentConditions = paymentConditions;
        if (arrangementType.toUpperCase().equals("FIXED"))
            _arrangement = new FixedArrangement(LocalDate.now(),selfPickup, companyId);
        else
            _arrangement = new SingleArrangement(selfPickup,companyId);
        _contacts = new ArrayList<>();
        _orders = new HashMap<>();
    }

    public String getType(){
        if (this._arrangement instanceof FixedArrangement)
            return "fixed";
        else
            return "single";
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
        return ArrangementMapper.getInstance().getArrangement(_companyId);
    }

    public void setArrangement(Arrangement arrangement) {
        this._arrangement = _arrangement;
        ArrangementMapper.getInstance().updateArrangement(_companyId,arrangement);
    }

    public ArrayList<ContactPerson> getContacts() {
        return _contacts;
    }//TODO

    public ContactPerson getContactByName (String name) {
        for (ContactPerson person: _contacts) {
            if (person.getName().equals(name))
                return person;
        }
        return null;
    }//TODO

    public void setContacts(ArrayList<ContactPerson> contacts) {
        this._contacts = _contacts;
    }

    public Map<Integer, Order> getOrders() {
        Map<Integer,Order> map =OrderMapper.getInstance().getOrders(_companyId);
        return map;
    }

    public void setOrders(Map<Integer, Order> _orders) {
        OrderMapper.getInstance().deleteOrders(_companyId);
        for(Order order: _orders.values())
            OrderMapper.getInstance().saveOrder(order,_companyId);
    }

    public boolean placeOrder(Map<Integer, Integer> items, LocalDate date){
        for (int item: items.keySet()){
            if (!ArrangementMapper.getInstance().getArrangement(_companyId).getItems().containsKey(item))
                return false;
        }
        Order order = new Order(SupplierManager.get_orderNumIncrement(),date, "Pending", items, false);
        _orders.put(SupplierManager.get_orderNumIncrement(),order);
        _arrangement.get_deliveryDates().getDates().put(order.getOrderNum(),date);
        SupplierManager.incrementOrderNum();
        OrderMapper.getInstance().saveOrder(order, this._companyId);
        return true;
    }

    public boolean addContact(ContactPerson contact){
        if (contact == null || _contacts.contains(contact))
            return false;
        else{
            _contacts.add(contact);
            return true;
        }
    } //TODO

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
    } //TODO

    public boolean cancelOrder (int orderNum){
        if (_orders.size() == 0)
            _orders = getOrders();
        if(!_orders.containsKey(orderNum))
            return false;
        else{
            _orders.get(orderNum).setStatus("Canceled");
            OrderMapper.getInstance().updateOrder(_orders.get(orderNum), _companyId);
            return true;
        }
    }

    public boolean changeOrderDate(int orderNum, LocalDate date){
        if (_orders.size() == 0)
            _orders = OrderMapper.getInstance().getOrders(_companyId);
        _orders.get(orderNum).setOrderDate(date);
        OrderMapper.getInstance().updateOrder(_orders.get(orderNum),_companyId);
        return ArrangementMapper.getInstance().updateItemInDelivery(orderNum, _companyId, date).isSuccessful();
    }

    public boolean addItemsToOrder(Map<Integer,Integer> items, int orderNumber){
        if (_arrangement.getItems().size() == 0)
            _arrangement = ArrangementMapper.getInstance().getArrangement(_companyId);
        if(!_arrangement.validateItems(items.keySet())) return false;
        Order order = OrderMapper.getInstance().getOrder(orderNumber);
        for(int productId:items.keySet()){
            if (order.getItemList().containsKey(productId))
                order.getItemList().put(productId, order.getItemList().get(productId)+items.get(productId));
        }
        OrderMapper.getInstance().updateOrderItems(orderNumber, order.getItemList());
        return true;
    }

    public boolean deleteItemsFromOrder(Map<Integer,Integer> items, int orderNumber){
        Order toDelete =  OrderMapper.getInstance().getOrder(orderNumber);
        if(!toDelete.deleteItems(items))
            return false;
        return OrderMapper.getInstance().updateOrderItems(orderNumber, toDelete.getItemList()).isSuccessful();
    }

    public void addToArrangement (List<Integer> products){
        for (int product: products){
            _arrangement.getItems().put(product, ProductMapper.getInstance().getProduct(product));
            ArrangementMapper.getInstance().insertProduct(this._companyId, product);
        }
    }

    public void deleteFromArrangement (ArrayList<Integer> products){
        for (Integer product: products){
            _arrangement.getItems().remove(product);
            ArrangementMapper.getInstance().deleteProduct(this._companyId, product);

        }
    }

    public void changePriceInArrangement (Map<Integer,Integer> products){ //TODO talk to tal
        for (Integer item: products.keySet()){
            _arrangement.getItems().get(item).setBuyingPrice(products.get(item));
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
