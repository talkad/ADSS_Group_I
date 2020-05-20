package DeliveryModule.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import DeliveryModule.BuisnessLayer.DeliveryArea;

public class SiteDAL implements iDAL
{
    protected String Address;
    protected String PhoneNumber;
    protected String ContantName;
    protected int isSupplier;



    public SiteDAL(String Address, String PhoneNumber, String ContantName, int isSupplier){
        this.Address = Address;
        this.ContantName = ContantName;
        this.PhoneNumber = PhoneNumber;
        this.isSupplier = isSupplier;
    }

    public String getFields(){
        return "Address, Contant_Name, Phone_Number, isSupplier";
    }
    public String getValues(){
        return "'"+Address + "', '" + ContantName + "', '" + PhoneNumber + "', " + isSupplier;
    }
    
    
}