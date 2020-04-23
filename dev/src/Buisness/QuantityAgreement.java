package Buisness;
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

    public void deleteItemsFromAgreement (List<Integer> items){
        for (Integer item: items){
            _discounts.remove(item);
        }
    }
    public void editItemInAgreement (Map<Integer, Map<Integer,Double>> items) {addItemsToAgreement(items);}

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
}
