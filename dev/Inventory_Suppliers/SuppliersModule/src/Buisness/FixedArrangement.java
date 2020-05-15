package Buisness;

import java.time.LocalDate;

public class FixedArrangement extends Arrangement{

    public FixedArrangement(LocalDate date, boolean _selfPickup) {
        super(_selfPickup);
        _deliveryDates.addDate(date,true);
    }

    public boolean modifyDate(LocalDate date){
        if(LocalDate.now().compareTo(date) >= 0) return false;
        for (LocalDate ld: _deliveryDates.getDates().keySet())
            if(_deliveryDates.getDates().get(ld)){
                _deliveryDates.getDates().remove(ld);
                _deliveryDates.getDates().put(date,true);
                break;
            }
        return true;
    }
}
