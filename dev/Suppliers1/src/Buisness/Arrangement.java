package Buisness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Arrangement {

    protected Map<Integer,Item> _items;
    protected boolean _selfPickup;
    protected QuantityAgreement _quantityAgreement = null;
    protected DeliveryDates _deliveryDates = new DeliveryDates();

    public Arrangement(boolean _selfPickup) {
        this._selfPickup = _selfPickup;
        this._items = new HashMap<Integer, Item>();
    }

    public Map<Integer, Item> getItems() {
        return _items;
    }

    public void setItems(Map<Integer, Item> _items) {
        this._items = _items;
    }

    public boolean isSelfPickup() {
        return _selfPickup;
    }

    public void setSelfPickup(boolean _selfPickup) {
        this._selfPickup = _selfPickup;
    }

    public boolean validateItems(Set<Integer> items){
        for (int key: items) {
            if (!_items.containsKey(key))
                return false;
        }
        return true;
    }

    public String toString(){
        String output = "";
        if (_quantityAgreement != null)
            output += agreementToString() +"=====================\n";
        else
            output+= "No Quantity agreement\n=====================\n";
        List<Integer> list = _items.keySet().stream().sorted().collect(Collectors.toList());
        for (Integer item: list){
            output = output + _items.get(item).toString()+"\n";
        }
        return output;
    }
    public boolean addNewAgreement (Map<Integer, Map<Integer,Double>> items) {
        if (!_items.keySet().containsAll(items.keySet()) || !checkPrices(items) || _quantityAgreement!=null) return false;
        _quantityAgreement = new QuantityAgreement(items);
        return true;
    }

    public boolean addItemsToAgreement (Map<Integer, Map<Integer,Double>> items) {
        if (!_items.keySet().containsAll(items.keySet()) || !checkPrices(items)) return false;
        _quantityAgreement.addItemsToAgreement(items);
        return true;
    }

    public void deleteItemsFromAgreement (List<Integer> items){_quantityAgreement.deleteItemsFromAgreement(items);}

    public boolean editItemInAgreement (Map<Integer, Map<Integer,Double>> items) {
        if (!checkPrices(items)) return false;
        _quantityAgreement.editItemInAgreement(items);
        return true;
    }

    private boolean checkPrices (Map<Integer, Map<Integer,Double>> items){
        for (Integer item: items.keySet())
            for (Integer value: items.get(item).keySet())
                if(items.get(item).get(value)<0)
                    return false;
        return true;
    }

    public String agreementToString () {return _quantityAgreement.toString();}

    public String pastDateToString (){
        return _deliveryDates.pastDateToString();
    }

    public String futureDateToString (){
        return _deliveryDates.futureDateToString();
    }

    public Map<Integer, Item> get_items() {
        return _items;
    }

    public void set_items(Map<Integer, Item> _items) {
        this._items = _items;
    }

    public QuantityAgreement get_quantityAgreement() {
        return _quantityAgreement;
    }

    public void set_quantityAgreement(QuantityAgreement _quantityAgreement) {
        this._quantityAgreement = _quantityAgreement;
    }

    public DeliveryDates get_deliveryDates() {
        return _deliveryDates;
    }

    public void set_deliveryDates(DeliveryDates _deliveryDates) {
        this._deliveryDates = _deliveryDates;
    }
}
