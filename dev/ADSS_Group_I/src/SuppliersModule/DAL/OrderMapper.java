package SuppliersModule.DAL;


import Interface.DAL_Connector.DatabaseManager;
import SuppliersModule.Buisness.Order;
import SuppliersModule.Buisness.Result;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMapper {
    private static Connection conn = DatabaseManager.getInstance().getConnection();

    private Map<Integer, Order> identityOrderMap;

    private static OrderMapper instance;

    public OrderMapper() {
        identityOrderMap = new HashMap<>();
    }

    public static OrderMapper getInstance() {
        if (instance == null)
            instance = new OrderMapper();
        return instance;
    }

    public Map<Integer, Integer> getItems(int orderId) {
        String sql = "SELECT * FROM OrderItems WHERE orderNum = ?";
        Map<Integer, Integer> items = new HashMap<>();
        try {

            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setInt(1, orderId);

            ResultSet rs = categoryStatement.executeQuery();

            // loop through the result set
            while (rs.next()) {
                items.put(rs.getInt("productId"), rs.getInt("amount"));

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " from here");
        }

        return items;
    }

    public Map<Integer, Order> getOrders(int supplierId) {
        Map<Integer, Order> orders = new HashMap<>();
        String sql = "SELECT orderNum FROM Orders WHERE supplierId =?";
        try {
            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setInt(1, supplierId);

            ResultSet rs = categoryStatement.executeQuery();
            while (rs.next()) {
                Order order = getOrder(rs.getInt("orderNum"));
                orders.put(order.getOrderNum(), order);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " from here");
            return null;
        }
        return orders;
    }

    public Order getOrder(int orderNum) {
        String sql = "SELECT orderDate,Status,dateCreated,supermarketId,supplierId, isPeriodic From Orders WHERE orderNum =?";
        Order order = null;
        try {

            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setInt(1, orderNum);

            ResultSet rs = categoryStatement.executeQuery();
            while (rs.next()) {
                order = new Order(orderNum, stringToDate(rs.getString("orderDate")), rs.getString("status"), getItems(orderNum),
                        stringToDate(rs.getString("dateCreated")), rs.getInt("supermarketId"), rs.getInt("isPeriodic") == 1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " from here");
            return null;
        }
        return order;
    }

    public Result saveOrder(Order order, int supplierId) {
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO Orders (orderNum, orderDate, status, dateCreated, supplierId, supermarketId, isPeriodic)" +
                "VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, order.getOrderNum());
            statement.setString(2, order.getOrderDate().toString());
            statement.setString(3, order.getStatus());
            statement.setString(4, order.getDateCreated().toString());
            statement.setInt(5, supplierId);
            statement.setInt(6, order.getSupermarketID());
            statement.setInt(7, order.isPeriodic() ? 1 : 0);
            numRowsInserted = statement.executeUpdate();


            if (numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to insert Order");

        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        if (result.isSuccessful()) {
            saveOrderItems(order.getOrderNum(), order.getItemList());
            result = ArrangementMapper.getInstance().insertItemToDelivery(order.getOrderDate(), supplierId, order.getOrderNum());
        }

        return result;
    }


    public Result updateOrder(Order order, int supplierId) {
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Orders SET orderDate = ?, status = ?" +
                " WHERE orderNum = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setString(1, order.getOrderDate().toString());
            statement.setString(2, order.getStatus());
            statement.setInt(3, order.getOrderNum());

            numRowsUpdated = statement.executeUpdate();


            if (numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Order");

        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        updateDate(order.getOrderNum(), order.getOrderDate());
        return result;
    }


    public Result deleteOrder(int orderNum) {
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Orders WHERE orderNum = ?";

        // delete the product only if there are no items belong to this product
        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, orderNum);

            numRowsDeleted = statement.executeUpdate();

            if (numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to delete Order");

        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        result = deleteOrderItems(orderNum);
        return result;
    }

    public Result deleteOrders(int supplierId) {
        Result result = new Result();
        Map<Integer, Order> map = getOrders(supplierId);
        for (Order order : map.values()) {
            result = deleteOrder(order.getOrderNum());
        }
        return result;
    }

    public LocalDate getDate(int orderID) {
        String date = null;
        String sql = "SELECT * FROM Order WHERE orderNum = ?";
        try {
            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setInt(1, orderID);
            ResultSet rs = categoryStatement.executeQuery();

            // loop through the result set
            date = rs.getString("orderDate");

        } catch (SQLException e) {
            System.err.println(e.getMessage() + " from here");
        }
        return stringToDate(date);
    }

    public int getSupplier(int orderID) {
        int supplierId = -1;
        String sql = "SELECT supplierId FROM Orders WHERE orderNum = ?";
        try {
            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setInt(1, orderID);
            ResultSet rs = categoryStatement.executeQuery();

            // loop through the result set
            supplierId = rs.getInt("supplierId");

        } catch (SQLException e) {
            System.err.println(e.getMessage() + " from here");
            return -1;
        }
        return supplierId;
    }

    public Map<String, Integer> getOrderAmount() {
        Map<String, Integer> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 1; i < 8; i++) {
            String sql = "SELECT COUNT(orderNum) FROM Orders WHERE status = ? AND orderDate = ?";
            try {
                PreparedStatement categoryStatement = conn.prepareStatement(sql);
                categoryStatement.setString(1, "pending");
                categoryStatement.setString(2, LocalDate.now().plusDays(i).format(formatter));
                ResultSet rs = categoryStatement.executeQuery();

                // loop through the result set
                int data = rs.getInt(1);
                map.put(LocalDate.now().plusDays(i).format(formatter), data);

            } catch (SQLException e) {
            }
        }
        return map;
    }

    public Map<Integer, String> getOrdersToSet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Map<Integer, String> map = new HashMap<>();
        for (int i = 1; i < 8; i++) {
            String sql = "SELECT orderNum FROM Orders WHERE status = ? AND orderDate = ?";
            try {
                PreparedStatement categoryStatement = conn.prepareStatement(sql);
                categoryStatement.setString(1, "pending");
                categoryStatement.setString(2, LocalDate.now().plusDays(i).format(formatter));
                ResultSet rs = categoryStatement.executeQuery();
                while (rs.next()) {
                    map.put(rs.getInt("orderNum"), LocalDate.now().plusDays(i).format(formatter));
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage() + " from here");
            }
        }
        return map;
    }

    public Result saveOrderItems(int orderId, Map<Integer, Integer> itemMap) {
        Result result = new Result();
        result.successful();
        String sql = "INSERT INTO OrderItems(OrderNum, productId, amount) VALUES(?,?,?)";
        int numRowsInserted;
        for (int itemId : itemMap.keySet()) {
            try {
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, orderId);
                statement.setInt(2, itemId);
                statement.setInt(3, itemMap.get(itemId));
                numRowsInserted = statement.executeUpdate();

                if (numRowsInserted >= 1)
                    result.successful();
            } catch (java.sql.SQLException e) {
                result.failure("Failed to create a statement");
            }
        }
        return result;
    }

    public Result updateOrderItems(int orderId, Map<Integer, Integer> ItemMap) {
        Result result = new Result();
        result = deleteOrderItems(orderId);
        if (!result.isSuccessful())
            return result;
        result = saveOrderItems(orderId, ItemMap);
        return result;
    }// deletes all current orderItems and inserts the new ones

    public Result deleteOrderItems(int orderId) {
        Result result = new Result();
        int numRowsDeleted;

        String deleteCommand = "DELETE FROM OrderItems WHERE orderNum = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, orderId);
            numRowsDeleted = statement.executeUpdate();

            if (numRowsDeleted >= 1)
                result.successful();
            else
                result.failure("Failed to update Arrangement");
        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        return result;
    }

    public Result updateDate(int orderId, LocalDate date) {
        Result result = new Result();
        String sql = "UPDATE Orders SET orderDate = ?" +
                " WHERE orderNum = ?";
        int numRowsUpdated = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, date.toString());
            statement.setInt(2, orderId);

            numRowsUpdated = statement.executeUpdate();
            if (numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Order Date");
        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        return result;

    }

    //date = "16/08/2016" ----- example
    private LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //String date = "16/08/2016";
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    public int getMaxOrder() {
        String sql = "SELECT MAX(orderNum) AS maxOrder FROM Orders";
        int max;
        try {
            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            ResultSet rs = categoryStatement.executeQuery();

            // loop through the result set
            max = rs.getInt("maxOrder");

        } catch (SQLException e) {
            System.err.println(e.getMessage() + " from here");
            return -1;
        }
        return max;
    }

    public Result updateOrderStatus(int orderId, String status) {
        Result result = new Result();
        String sql = "UPDATE Orders SET status = ?" +
                " WHERE orderNum = ?";
        int numRowsUpdated = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, orderId);

            numRowsUpdated = statement.executeUpdate();
            if (numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Order status");
        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        return result;
    }
}

