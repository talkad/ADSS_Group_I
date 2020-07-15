package DeliveryModule.BuisnessLayer;

import java.util.List;

public class Supplier extends Site {
    public Supplier(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea){
        super(Address, PhoneNumber, ContantName, DeliveryArea);
    }

    public Supplier(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea, int siteID){
        super(Address, PhoneNumber, ContantName, DeliveryArea, siteID);
    }
}
