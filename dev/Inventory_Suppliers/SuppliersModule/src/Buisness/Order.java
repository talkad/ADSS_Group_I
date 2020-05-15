package Buisness;

import java.time.LocalDate;
import java.util.Map;

public class Order {

    private int _orderNum;
    private LocalDate _orderDate;
    private String _status;
    private Map<Integer, Integer> _itemList;
    private LocalDate _dateCreated;

    public Order(int orderNum, LocalDate orderDate, String status, Map<Integer, Integer> itemList) {
        this._orderNum = orderNum;
        this._orderDate = orderDate;
        this._status = status;
        this._itemList = itemList;
        this._dateCreated = LocalDate.now();
    }

    public int getOrderNum() {
        return _orderNum;
    }

    public void setOrderNum(int _orderNum) {
        this._orderNum = _orderNum;
    }

    public LocalDate getOrderDate() {
        return _orderDate;
    }

    public void setOrderDate(LocalDate _orderDate) {
        this._orderDate = _orderDate;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String _status) {
        this._status = _status;
    }

    public Map<Integer, Integer> getItemList() {
        return _itemList;
    }

    public void setItemList(Map<Integer, Integer> _itemList) {
        this._itemList = _itemList;
    }

    public LocalDate getDateCreated() {
        return _dateCreated;
    }

    public void setDateCreated(LocalDate _dateCreated) {
        this._dateCreated = _dateCreated;
    }

    public void addItems (Map<Integer,Integer> items){
        for (int item: items.keySet()) {
            if(!_itemList.containsKey(item))
                _itemList.put(item, items.get(item));
            else
                _itemList.replace(item, _itemList.get(item) + items.get(item));
        }
    }

    public boolean deleteItems (Map<Integer,Integer> items){
        if(!canDelete(items)) return false;
        for (int item: items.keySet()) {
            if (_itemList.get(item) - items.get(item) > 0)
                _itemList.replace(item, _itemList.get(item) - items.get(item));
            else
                _itemList.remove(item);
        }
        return true;
    }

    private boolean canDelete(Map<Integer, Integer> items) {
        for (int item: items.keySet()) {
            if(!_itemList.containsKey(item) || _itemList.get(item) < items.get(item))
                return false;
        }
        return true;
    }

    public boolean canbeUpdated(){
        return (this._orderDate.isAfter(LocalDate.now().plusDays(1)));
    }
}
