package SuppliersModule.Buisness;


import SuppliersModule.DAL.ArrangementMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuantityAgreement {

    private Map<Integer, Map<Integer,Double>> _discounts;

    public QuantityAgreement(Map<Integer, Map<Integer, Double>> discounts) {
        this._discounts = discounts;
    }

    public Map<Integer, Map<Integer, Double>> getDiscounts() {
        return _discounts;
    }

    public void setDiscounts(Map<Integer, Map<Integer, Double>> _discounts) {
        this._discounts = _discounts;
    }

    public void addItemsToAgreement (Map<Integer, Map<Integer,Double>> items){
        _discounts.putAll(items);
    }

    public void deleteItemsFromAgreement (List<Integer> items,int companyId){
        for (Integer item: items){
            _discounts.remove(item);
            ArrangementMapper.getInstance().deleteProductFromQuantity(companyId,item);
        }
    }
    public void editItemInAgreement (Map<Integer, Map<Integer,Double>> items) {
        addItemsToAgreement(items);
    }

    public String toString(){
        String output = "Quantity Agreement detail:\n==========================\n\n";
        List<Integer> item_list = _discounts.keySet().stream().sorted().collect(Collectors.toList());
        for (Integer item: item_list){
            output += "Item Id: " + item +" has the following discounts:\n";
            List<Integer> price_list = _discounts.get(item).keySet().stream().sorted().collect(Collectors.toList());
            for (Integer val: price_list)
                output += "Value: " + val + " Discount: " + _discounts.get(item).get(val)+"\n";
            output += "\n";
        }
        return output;
    }

    public double checkDiscount(int itemID,int amount){
        double discount = -1;
        if (!_discounts.containsKey(itemID)){
            return -1;
        }
        for(int step:_discounts.get(itemID).keySet()){
            if (step < amount)
                discount = _discounts.get(itemID).get(step);
        }
        return discount;
    }
}
