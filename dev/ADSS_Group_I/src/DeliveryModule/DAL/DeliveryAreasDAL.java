package DeliveryModule.DAL;


public class DeliveryAreasDAL implements  iDAL{
    protected String Name;

    public DeliveryAreasDAL(String name){
        this.Name = name;
    }

    public String getFields(){
        return "Name";
    }

    public String getValues(){
        return "'" + Name + "'";
    }

}
