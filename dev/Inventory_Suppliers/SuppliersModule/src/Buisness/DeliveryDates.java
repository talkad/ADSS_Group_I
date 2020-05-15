package Buisness;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DeliveryDates {

    private Map<LocalDate, Boolean> _dates;

    public DeliveryDates() {_dates = new HashMap<>(); }

    public DeliveryDates(HashMap<LocalDate, Boolean> dates) {
        this._dates = dates;
    }

    public Map<LocalDate,Boolean> getDates() {
        return _dates;
    }

    public void setDates(Map<LocalDate,Boolean> _dates) {
        this._dates = _dates;
    }

    public void addDate(LocalDate ld, boolean fixed){
        _dates.put(ld, fixed);
    }

    public String pastDateToString(){
        String output = "";
        for (LocalDate date: _dates.keySet()){
            if (date.compareTo(LocalDate.now()) < 0 )
                output+=date.toString();
        }
        return output;
    }

    public String futureDateToString(){
        String output = "";
        for (LocalDate date: _dates.keySet()){
            if (date.compareTo(LocalDate.now()) >= 0 )
                output+=date.toString();
        }
        return output;
    }


}
