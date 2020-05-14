package test;

import BusinessLayer.Inventory;
import BusinessLayer.Item;
import BusinessLayer.Product;
import DTO.ItemDTO;
import DTO.ProductDTO;
import DataAccessLayer.ProductMapper;
import org.junit.Assert;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//TODO: fix the tests
public class InventoryTest {

    public static Inventory inventory;
    public static ProductMapper productMapper;

    @org.junit.Before
    public void setUp() throws Exception {
        inventory = Inventory.getInstance();
    }

    @org.junit.Test
    public void addProduct() {
        List<String> milkCategories= new LinkedList<>(Arrays.asList("milky","500ml"));
        ProductDTO milk= new ProductDTO(1, "milk","Tnuva",3, 10,15,
                2.5,4 ,2,milkCategories,null);

        inventory.addProduct(milk);// adding the product to the inventory
        Product product = productMapper.getProduct(1);//getting the product
        Assert.assertNotNull(product); // if the product was not added then product will be null
    }

    @org.junit.Test
    public void removeProduct() {
        List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
        ProductDTO shampoo= new ProductDTO(2,"shampoo","head&shoulders",3, 10,15,
                1.5,4,2,shampooCategories,null);

        inventory.addProduct(shampoo);// adding the product to the inventory
        inventory.removeProduct(2); //immediately removing the product
        Product product = productMapper.getProduct(2);//getting the product
        Assert.assertNull(product); // if the returned product is null then it means that the product was removed
    }

    @org.junit.Test
    public void addItem() {
        List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
        ProductDTO shampoo = new ProductDTO(3,"shampoo","head&shoulders",3, 10,15,
                1, 0,0,shampooCategories,null);

        inventory.addProduct(shampoo);// adding the product to the inventory

        ItemDTO shampoo1= new ItemDTO(1, 3, 0, null,"Store");
        inventory.addItem(3, shampoo1);// adding the item to the inventory

        Product product = productMapper.getProduct(3); //getting the product
        Item item = product.getItem(1); //getting the item

        Assert.assertNotNull(item); // if the returned item is null then it means the product was never added
    }

    @org.junit.Test
    public void removeItem() {
        List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
        ProductDTO shampoo= new ProductDTO(4, "shampoo","head&shoulders",3, 10,15,
                1, 0,0,shampooCategories,null);

        inventory.addProduct(shampoo);// adding the product to the inventory

        ItemDTO shampoo1= new ItemDTO(2, 1, 2,null,"Store");
        inventory.addItem(4, shampoo1);// adding the item to the inventory
        inventory.removeOneItem(4, 2); //immediately removing the product

        Product product = productMapper.getProduct(4); //getting the product
        Item item = product.getItem(2); //getting the item

        Assert.assertNull(item); // if the returned item is null then it means the product was has been removed
    }

    @org.junit.Test
    public void minQuantityNotification() {
        List<String> broomCategories= new LinkedList<>(Arrays.asList("cleaning products","brooms"));
        ProductDTO broom= new ProductDTO(5,"broom","Sano",5, 10,15,
                7,4,2,broomCategories,null);

        inventory.addProduct(broom); // adding a broom to the inventory with minimum capacity of 5

        //adding two items to the product
        ItemDTO broom1= new ItemDTO(13, 1, 0,null,"Store");
        inventory.addItem(5, broom1);
        ItemDTO broom2= new ItemDTO(14, 1, 2,null,"Inventory");
        inventory.addItem(5, broom2);

        Product product = productMapper.getProduct(5); //getting the product

        //since we are under the minumum capacity the function should return a String with the the notification info
        // so we'll just check if the String returned is not null
        Assert.assertNotNull(inventory.minQuantityNotification(product));

    }

    @org.junit.Test
    public void updateMinQuantity() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO(6, "cheese","Tara",3, 10,15,
                10, 4,2,cheeseCategories,null);

        inventory.addProduct(cheese);
        inventory.updateMinQuantity(6, 10);// updating the minimum quantity for the product cheese

        Product product = productMapper.getProduct(6);// getting the product
        Assert.assertEquals(10, product.getMinCapacity());// checking if the updating took place
    }

    @org.junit.Test
    public void updateSellingPrice() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO(7, "cheese","Tara",3, 10,15,
                6, 4,2,cheeseCategories,null);

        inventory.addProduct(cheese);
        inventory.updateSellingPrice(7, 50);// updating the selling price for the product cheese

        Product product = productMapper.getProduct(7);// getting the product
        Assert.assertEquals(50, product.getSellingPrice());// checking if the updating took place
    }

    @org.junit.Test
    public void updateBuyingPrice() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO(8, "cheese","Tara",3, 10,15,
                7,4,2,cheeseCategories,null);

        inventory.addProduct(cheese);
        inventory.updateBuyingPrice(8, 25);// updating the buying for the product cheese

        Product product = productMapper.getProduct(8);// getting the product
        Assert.assertEquals(25, product.getBuyingPrice());// checking if the updating took place
    }

    @org.junit.Test
    public void setDefect() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO(9, "cheese","Tara",3, 10,15,
                8, 4,2,cheeseCategories,null);
        inventory.addProduct(cheese);

        ItemDTO cheese1= new ItemDTO(35, 4, 2,null,"Store"); // creating an item which is not defect
        inventory.addItem(9, cheese1);// adding the item to the inventory
        inventory.setDefect(9, 35, 3);

        Product product = productMapper.getProduct(9);// getting the product
        Item item = product.getItem(35); // getting the item

        Assert.assertEquals(item.getNumOfDefects(), 3);// checking if the item was set to be defect
    }

    @org.junit.Test
    public void updateItemLocation() {
        List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
        ProductDTO cheese= new ProductDTO(10, "cheese","Tara",3, 10,15,
                60, 0,6,cheeseCategories,null);
        inventory.addProduct(cheese);

        ItemDTO cheese1= new ItemDTO(42, 6, 0, null,"Store"); // creating an item which is located in "Store"
        inventory.addItem(10, cheese1);// adding the item to the inventory
        inventory.updateItemLocation(10, 42,  "Inventory");// updating the location of the item

        Product product = productMapper.getProduct(10);// getting the product
        Item item = product.getItem(42); // getting the item

        Assert.assertEquals("Inventory", item.getLocation());// checking if the location of the item was updated
        Assert.assertEquals(6, product.getInventoryCapacity()); // checking if the amount specified in the product match
    }
}