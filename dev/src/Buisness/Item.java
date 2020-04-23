package Buisness;

public class Item {

    private int _id;
    private double _price;
    private String _name;
    private int _supplierId;

    public Item(int id, double price, String name, int supplier) {
        this._id = id;
        this._price = price;
        this._name = name;
        this._supplierId = supplier;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public double getPrice() {
        return _price;
    }

    public void setPrice(double _price) {
        this._price = _price;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public int getSupplierId() {
        return _supplierId;
    }

    public void setSupplier(int _supplier) {
        this._supplierId = _supplier;
    }

    public String toString(){
        return "Buisness.Item Id: " + _id + " Name: " + _name + " Price: " + _price;
    }
}
