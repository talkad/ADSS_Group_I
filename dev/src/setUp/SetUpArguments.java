package setUp;

import Buisness.Item;
import Buisness.Order;
import Buisness.SuperLi;
import Buisness.SupplierCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUpArguments {

    public static void setup() {
        SupplierCard sup1 = new SupplierCard("sup1", "man1", 111, 123456789, "none", "FIXED", false);
        SuperLi.getSuppliers().put(111, sup1);
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(15151, 151);
        map.put(15152, 151);
        map.put(15153, 151);
        map.put(15154, 151);
        map.put(15155, 151);
        Order order1 = new Order(111, LocalDate.now(), "pending", map);
        Order order2 = new Order(222, LocalDate.now(), "pending", new HashMap<Integer, Integer>());
        Order order3 = new Order(333, LocalDate.now(), "pending", new HashMap<Integer, Integer>());
        Order order4 = new Order(444, LocalDate.now(), "pending", new HashMap<Integer, Integer>());
        HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
        orders.put(111, order1);
        orders.put(222, order2);
        orders.put(333, order3);
        orders.put(444, order4);

        sup1.setOrders(orders);
        Item item1 = new Item(12, 12, "milk", 111);
        Item item2 = new Item(13, 13, "sugar", 111);
        Item item3 = new Item(14, 14, "chocolate", 111);
        Item item4 = new Item(15, 15, "honey", 111);
        Item item5 = new Item(16, 16, "tomato", 111);
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item4);
        items.add(item5);
        items.add(item2);
        items.add(item1);
        items.add(item3);
        sup1.addToArrangement(items);
        HashMap<Integer, Map<Integer, Double>> Qmap = new HashMap<>();
        for (int i = 4; i >= 0; i -= 2) {
            HashMap<Integer, Double> pmap = new HashMap<>();
            pmap.put(items.get(i).getId() * 2, items.get(i).getPrice() / 2);
            pmap.put(items.get(i).getId() * 4, items.get(i).getPrice() / 4);
            Qmap.put(items.get(i).getId(), pmap);
        }
        sup1.getArrangement().addNewAgreement(Qmap);
    }
}
