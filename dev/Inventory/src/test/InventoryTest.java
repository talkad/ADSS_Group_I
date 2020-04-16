package test;

import BusinessLayer.Inventory;
import BusinessLayer.Item;
import BusinessLayer.Product;
import DTO.ItemDTO;
import DTO.ProductDTO;
import org.junit.Assert;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InventoryTest {

    public static Inventory inventory;

    @org.junit.Before
    public void setUp() throws Exception {
        inventory = Inventory.getInstance();
    }

    @org.junit.Test
    public void addProduct() {
        List<String> milkCategories= new LinkedList<>(Arrays.asList("milky","500ml"));
        ProductDTO milk= new ProductDTO("milk","Tnuva",3, 10,15,
                4,2,milkCategories,null);

        inventory.addProduct(milk);// adding the product to the inventory
        Product product = inventory.getProduct("milk", "Tnuva");//getting the product
        Assert.assertNotNull(product); // if the product was not added then product will be null
    }

    @org.junit.Test
    public void removeProduct() {
        List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
        ProductDTO shampoo= new ProductDTO("shampoo","head&shoulders",3, 10,15,
                4,2,shampooCategories,null);

        inventory.addProduct(shampoo);// adding the product to the inventory
        inventory.removeProduct("shampoo", "head&shoulders"); //immediately removing the product
        Product product = inventory.getProduct("shampoo", "head&shoulders");//getting the product
        Assert.assertNull(product); // if the returned product is null then it means that the product was removed
    }

    @org.junit.Test
    public void addItem() {
        List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
        ProductDTO shampoo = new ProductDTO("shampoo","head&shoulders",3, 10,15,
                4,2,shampooCategories,null);

        inventory.addProduct(shampoo);// adding the product to the inventory

        ItemDTO shampoo1= new ItemDTO(7, false, null,"Store");
        inventory.addItem("shampoo", "head&shoulders", shampoo1);// adding the item to the inventory

        Product product = inventory.getProduct("shampoo", "head&shoulders"); //getting the product
        Item item = product.getItem(7); //getting the item

        Assert.assertNotNull(item); // if the returned item is null then it means the product was never added
    }

    @org.junit.Test
    public void removeItem() {
        List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
        ProductDTO shampoo= new ProductDTO("shampoo","head&shoulders",3, 10,15,
                4,2,shampooCategories,null);

        inventory.addProduct(shampoo);// adding the product to the inventory

        ItemDTO shampoo1= new ItemDTO(5, false, null,"Store");
        inventory.addItem("shampoo", "head&shoulders", shampoo1);// adding the item to the inventory
        inventory.removeItem("shampoo", "head&shoulders", 5); //immediately removing the product

        Product product = inventory.getProduct("shampoo", "head&shoulders"); //getting the product
        Item item = product.getItem(7); //getting the item

        Assert.assertNull(item); // if the returned item is null then it means the product was has been removed
    }

    @org.junit.Test
    public void minQuantityNotification() {
        List<String> broomCategories= new LinkedList<>(Arrays.asList("cleaning products","brooms"));
        ProductDTO broom= new ProductDTO("broom","Sano",3, 10,15,
                4,2,broomCategories,null);

        inventory.addProduct(broom); // adding a broom to the inventory with minimum capacity of 3

        //adding two items to the product
        ItemDTO broom1= new ItemDTO(13, false, null,"Store");
        inventory.addItem("broom", "Sano", broom1);
        ItemDTO broom2= new ItemDTO(14, true, null,"Inventory");
        inventory.addItem("broom", "Sano", broom2);

        Product product = inventory.getProduct("broom", "Sano"); //getting the product

        //since we are under the minumum capacity the function should return a String with the the notification info
        // so we'll just check if the String returned is not null
        Assert.assertNotNull(inventory.minQuantityNotification(product));

    }

    @org.junit.Test
    public void updateMinQuantity() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
                4,2,cheeseCategories,null);

        inventory.addProduct(cheese);
        inventory.updateMinQuantity("cheese", "Tara", 10);// updating the minimum quantity for the product cheese

        Product product = inventory.getProduct("cheese", "Tara");// getting the product
        Assert.assertEquals(10, product.getMinCapacity());// checking if the updating took place
    }

    @org.junit.Test
    public void updateSellingPrice() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
                4,2,cheeseCategories,null);

        inventory.addProduct(cheese);
        inventory.updateSellingPrice("cheese", "Tara", 50);// updating the selling price for the product cheese

        Product product = inventory.getProduct("cheese", "Tara");// getting the product
        Assert.assertEquals(50, product.getSellingPrice());// checking if the updating took place
    }

    @org.junit.Test
    public void updateBuyingPrice() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
                4,2,cheeseCategories,null);

        inventory.addProduct(cheese);
        inventory.updateBuyingPrice("cheese", "Tara", 25);// updating the buying for the product cheese

        Product product = inventory.getProduct("cheese", "Tara");// getting the product
        Assert.assertEquals(25, product.getBuyingPrice());// checking if the updating took place
    }

    @org.junit.Test
    public void setDefect() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
                4,2,cheeseCategories,null);
        inventory.addProduct(cheese);

        ItemDTO cheese1= new ItemDTO(35, false, null,"Store"); // creating an item which is not defect
        inventory.addItem("cheese", "Tara", cheese1);// adding the item to the inventory
        inventory.setDefect("cheese", "Tara", 35);

        Product product = inventory.getProduct("cheese", "Tara");// getting the product
        Item item = product.getItem(35); // getting the item

        Assert.assertTrue(item.isDefect());// checking if the item was set to be defect
    }

    @org.junit.Test
    public void updateItemLocation() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
                4,2,cheeseCategories,null);
        inventory.addProduct(cheese);

        ItemDTO cheese1= new ItemDTO(42, false, null,"Store"); // creating an item which is located in "Store"
        inventory.addItem("cheese", "Tara", cheese1);// adding the item to the inventory
        inventory.updateItemLocation("cheese", "Tara", 42, "On the floor");// updating the location of the item

        Product product = inventory.getProduct("cheese", "Tara");// getting the product
        Item item = product.getItem(42); // getting the item

        Assert.assertEquals("On the floor", item.getLocation());// checking if the location of the item was updated
    }

    @org.junit.Test
    public void isIdUnique() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
                4,2,cheeseCategories,null);
        inventory.addProduct(cheese);

        ItemDTO cheese1= new ItemDTO(1337, false, null,"Store"); // creating an item with it 1337
        inventory.addItem("cheese", "Tara", cheese1);// adding the item to the inventory

        Assert.assertFalse(inventory.isIdUnique(1337)); // checking if the id 1337 is unique (which we know it isn't)
                                                        // so if the function return False it means it works fine
    }
}