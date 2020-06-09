package SuppliersModule.Business;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DeliveryDates {

    private Map<Integer,LocalDate> _dates;

    public DeliveryDates() {_dates = new HashMap<>(); }

    public DeliveryDates(HashMap<Integer,LocalDate> dates) {
        this._dates = dates;
    }

    public Map<Integer,LocalDate> getDates() {
        return _dates;
    }

    public void setDates(Map<Integer,LocalDate> _dates) {
        this._dates = _dates;
    }

    public void addDate(LocalDate ld, Integer order){
        _dates.put(order,ld);
    }

    public String pastDateToString(){
        String output = "";
        for (int orderNum: _dates.keySet()){
            if (_dates.get(orderNum).compareTo(LocalDate.now()) < 0)
                output+= orderNum + "\t\t" +_dates.get(orderNum).toString() +"\n";
        }
        return output;
    }

    public String futureDateToString(){
        String output = "";
        for (int orderNum: _dates.keySet()){
            if (_dates.get(orderNum).compareTo(LocalDate.now()) >= 0 )
                output+= orderNum + "\t\t" +_dates.get(orderNum).toString() +"\n";
        }
        return output;
    }


}
