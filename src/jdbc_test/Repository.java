/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc_test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author buster
 */
public class Repository {

    Connection con;
    Statement stmt;
    List<Shoe> products;

    public Repository() throws SQLException {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shoeshop2",
                "root", "lukas0691");

        stmt = con.createStatement();

    }

    public List<Costumer> getCostumers() throws SQLException {
        List<Costumer> costumers = new ArrayList<>();
        Map<Integer, Costumer> cm = new HashMap<>();
        ResultSet rs = stmt.executeQuery("select * from costumer");

        while (rs.next()) {
            Costumer tempCostumer = new Costumer(rs.getInt("id"), rs.getString("name"));
            costumers.add(tempCostumer);
            cm.put(tempCostumer.getId(), tempCostumer);

        }

        return costumers;
    }

    public List<Shoe> getProducts() throws SQLException {
        products = new ArrayList<>();

        ResultSet rs = stmt.executeQuery("select * from shoe");

        while (rs.next()) {
            Shoe tempShoe = new Shoe(rs.getInt("id"), rs.getInt("size"), rs.getInt("price"));
            getCatBrandColour(tempShoe);
            products.add(tempShoe);
        }

        return products;
    }

    public void getCatBrandColour(Shoe shoe) throws SQLException {
        Statement catStmt = con.createStatement();
        ResultSet catRs = catStmt.executeQuery("select shoe_category.shoeId, category.name \n"
                + "from shoe_category\n"
                + "inner join category on shoe_category.categoryId = category.id "
                + "having shoeId = " + shoe.getId());

        List<String> catList = new ArrayList<>();
        while (catRs.next()) {
            catList.add(catRs.getString("name"));
        }

        shoe.setCategories(catList);

        Statement brandStmt = con.createStatement();
        ResultSet brandRs = brandStmt.executeQuery("select shoe_brand.shoeId, brand.name \n"
                + "from shoe_brand\n"
                + "inner join brand on shoe_brand.brandId = brand.id "
                + "having shoeId = " + shoe.getId());

        List<String> brandList = new ArrayList<>();
        while (brandRs.next()) {
            brandList.add(brandRs.getString("name"));
        }

        shoe.setBrands(brandList);

        Statement colourStmt = con.createStatement();
        ResultSet colourRs = colourStmt.executeQuery("select shoe_colour.shoeId, colour.name \n"
                + "from shoe_colour\n"
                + "inner join colour on shoe_colour.colourId = colour.id "
                + "having shoeId = " + shoe.getId());

        List<String> colourList = new ArrayList<>();
        while (colourRs.next()) {
            colourList.add(colourRs.getString("name"));
        }

        shoe.setColours(colourList);

    }

    public List<Order> getOrders(String name) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "select * from "
                + "(select orders.id as orderId, orders.date as date, orders.expedierad as expedierad, costumer.name as name "
                + "from orders "
                + "inner join costumer on orders.costumerId = costumer.id having expedierad = 0) "
                + "as E "
                + "where name = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet orderRs = ps.executeQuery();

        while (orderRs.next()) {
            Order tempOrder = new Order(orderRs.getInt("orderId"), orderRs.getDate("date"));
            orders.add(tempOrder);

        }

        return orders;
    }

    public void callAddToCart(int costumerId, int ordersId, int shoeId) throws SQLException {
        CallableStatement cStmt = con.prepareCall("call AddToCart(?, ?, ?)");
        cStmt.setInt(1, costumerId);
        cStmt.setInt(2, ordersId);
        cStmt.setInt(3, shoeId);
        cStmt.execute();

    }

    public void callAddToCartNewOrder(int costumerId, int ordersId, int shoeId) throws SQLException {
        CallableStatement cStmt = con.prepareCall("call AddToCart(?, ?, ?)");
        cStmt.setInt(1, costumerId);
        cStmt.setInt(2, ordersId);
        cStmt.setInt(3, shoeId);
        cStmt.execute();

    }

}
