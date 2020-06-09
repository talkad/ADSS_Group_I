package DeliveryModule.DAL;

public class SiteAreaDAL implements iDAL {

    protected String SiteAddress;
    protected String AreaName;

    public SiteAreaDAL(String siteAddress, String areaName){
        this.SiteAddress = siteAddress;
        this.AreaName = areaName;
    }

    public String getFields(){
        return "SiteAddress, AreaName";
    }

    public String getValues(){
        return "'"+SiteAddress + "', '" + AreaName + "'";
    }
}
