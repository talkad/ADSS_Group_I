package DeliveryModule.BuisnessLayer;

import java.util.List;

public class Shop extends Site {
    public Shop(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea){
        super(Address, PhoneNumber, ContantName, DeliveryArea);
    }
}
