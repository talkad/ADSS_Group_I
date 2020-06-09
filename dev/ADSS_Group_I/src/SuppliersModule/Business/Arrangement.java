package SuppliersModule.Business;

import SuppliersModule.Business.*;
import SuppliersModule.DAL.ArrangementMapper;
import InventoryModule.Business.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Arrangement {

    protected Map<Integer, Product> _items;
    protected boolean _selfPickup;
    protected QuantityAgreement _quantityAgreement = null;
    protected DeliveryDates _deliveryDates = new DeliveryDates();
    protected final int _supplierId;

    public Arrangement(boolean _selfPickup, int supplierId) {
        this._selfPickup = _selfPickup;
        this._supplierId = supplierId;
        this._items = new HashMap<Integer, Product>();
    }

    public Map<Integer, Product> getItems() {
        return _items;
    }

    public void setItems(Map<Integer, Product> _items) {
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
        if (_quantityAgreement == null)
            _quantityAgreement = ArrangementMapper.getInstance().getQuantity(_supplierId);
        if (_quantityAgreement != null)
            output += agreementToString() +"\n=====================\n";
        else
            output+= "No Quantity agreement\n=====================\n";
        List<Integer> list = _items.keySet().stream().sorted().collect(Collectors.toList());
        for (Integer item: list){
            output = output + _items.get(item).toString()+"\n";
        }
        return output;
    }
    public boolean addNewAgreement (Map<Integer, Map<Integer,Double>> items,int supplierid) {
        if (!_items.keySet().containsAll(items.keySet()) || !checkPrices(items) || _quantityAgreement!=null)
            return false;
        _quantityAgreement = new QuantityAgreement(items);
        ArrangementMapper.getInstance().insertToQuantity(_quantityAgreement,supplierid);
        return true;
    }

    public boolean addItemsToAgreement (Map<Integer, Map<Integer,Double>> items, int companyId) {
        if (!_items.keySet().containsAll(items.keySet()) || !checkPrices(items)) return false;
        if (_quantityAgreement == null)
            _quantityAgreement = ArrangementMapper.getInstance().getQuantity(_supplierId);
        if (_quantityAgreement == null)
            return false;
        _quantityAgreement.addItemsToAgreement(items);
        for(int product : items.keySet())
            for(int amount : items.get(product).keySet())
            ArrangementMapper.getInstance().insertProductToQuantity(companyId,product,amount, items.get(product).get(amount));
        return true;
    }

    public void deleteItemsFromAgreement (List<Integer> items, int companyId){
        if (_quantityAgreement == null)
            _quantityAgreement = ArrangementMapper.getInstance().getQuantity(_supplierId);
        if (_quantityAgreement == null)
            return;
        _quantityAgreement.deleteItemsFromAgreement(items,_supplierId);
        for(int product: items)
            ArrangementMapper.getInstance().deleteProductFromQuantity(product, companyId);

    }

    public boolean editItemInAgreement (Map<Integer, Map<Integer,Double>> items, int companyId) {
        if (!checkPrices(items) )
            return false;
        if (_quantityAgreement == null)
            _quantityAgreement = ArrangementMapper.getInstance().getQuantity(_supplierId);
        if (_quantityAgreement == null)
            return false;
        _quantityAgreement.editItemInAgreement(items);
        for(int product : items.keySet())
            ArrangementMapper.getInstance().updateItemInQuantity(_supplierId,product,items.get(product));
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
