package SuppliersModule.Buisness;

import SuppliersModule.DAL.ArrangementMapper;
import SuppliersModule.DAL.OrderMapper;
import java.time.LocalDate;

public class FixedArrangement extends Arrangement{

    public FixedArrangement(LocalDate date, boolean _selfPickup, int supplierId) {
        super(_selfPickup,supplierId);
        _deliveryDates.addDate(date,-1);
    }

    public boolean modifyDate(LocalDate date){
        if(LocalDate.now().compareTo(date) >= 0) return false;
        if (_deliveryDates.getDates().size() == 0)
            _deliveryDates = ArrangementMapper.getInstance().getDeliveryDates(_supplierId);
        for (int orderNum: _deliveryDates.getDates().keySet()) {
            if (orderNum == -1)
                ArrangementMapper.getInstance().updateItemInDelivery(-1, _supplierId, date);
            else {
                Order order = OrderMapper.getInstance().getOrder(orderNum);
                if (order.isPeriodic()) {
                    _deliveryDates.getDates().remove(orderNum);
                    _deliveryDates.getDates().put(orderNum, date);
                    break;
                }
            }
        }
        return true;
    }
}
