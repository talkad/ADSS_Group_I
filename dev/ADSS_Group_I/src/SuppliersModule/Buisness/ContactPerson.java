package SuppliersModule.Buisness;

import java.util.Map;

public class ContactPerson {

    private String _name;
    private Map<String, String> _contactMethods;

    public ContactPerson(String name, Map<String, String> contactMethods) {
        this._name = name;
        this._contactMethods = contactMethods;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public Map<String, String> getContactMethods() {
        return _contactMethods;
    }

    public void setContactMethods(Map<String, String> _contactMethods) {
        this._contactMethods = _contactMethods;
    }

    public String toString(){
        return "Name: " + _name + "\nContact methods: " + _contactMethods.toString();
    }

    public void addToMethods(String device, String info){
        _contactMethods.put(device,info);
    }

    public void deleteFromMethods (String device){
        _contactMethods.remove(device);
    }

    public void editMethod (String device, String newInfo){
        _contactMethods.replace(device,newInfo);
    }
}
