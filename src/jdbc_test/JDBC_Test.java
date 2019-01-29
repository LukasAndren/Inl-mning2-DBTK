/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc_test;

import java.sql.Date;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class JDBC_Test extends JFrame implements ActionListener {

    JPanel costumerPanel;
    JButton cos1;
    JButton cos2;
    JButton cos3;
    JButton cos4;
    JButton cos5;
    JPanel productPanel;
    JPanel ordersPanel;
    JButton newOrder;

    JButton exit;
    JButton[] orderButtons = new JButton[2];
    Order chosenOrder;
    List<Order> orders;
    Shoe chosenShoe;
    Repository r;
    List<Shoe> shoeList;
    Costumer activeCostumer;
    JButton[] productButtons = new JButton[9];
    List<Costumer> costumerList;
    ActionListener ALExit = e -> System.exit(0);

    public JDBC_Test() throws SQLException {
        r = new Repository();
        costumerPanel = new JPanel();
        cos1 = new JButton();
        cos2 = new JButton();
        cos3 = new JButton();
        cos4 = new JButton();
        cos5 = new JButton();

        costumerList = new ArrayList<>();
        costumerList = r.getCostumers();

        cos1.setText(costumerList.get(0).getName());
        cos2.setText(costumerList.get(1).getName());
        cos3.setText(costumerList.get(2).getName());
        cos4.setText(costumerList.get(3).getName());
        cos5.setText(costumerList.get(4).getName());

        cos1.addActionListener(this);
        cos2.addActionListener(this);
        cos3.addActionListener(this);
        cos4.addActionListener(this);
        cos5.addActionListener(this);

        costumerPanel.setLayout(new GridLayout(3, 3));
        costumerPanel.add(cos1);
        costumerPanel.add(cos2);
        costumerPanel.add(cos3);
        costumerPanel.add(cos4);
        costumerPanel.add(cos5);

        costumerPanel.setSize(500, 500);
        add(costumerPanel);
        setSize(600, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        JDBC_Test i = new JDBC_Test();

//    Class.forName("com.mysql.cj.jdbc.Driver");
//        
//    Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/shoeshop2",
//    "root", "lukas0691");    
//    
//    Statement stmt = con.createStatement();
//    
//    ResultSet rs = stmt.executeQuery("select * from costumer");
//    rs.next();
//        int i = rs.getInt("id");
//        String s = rs.getString("name");
//        String k = rs.getString("city");
//    while(rs.next()){
//        int x = rs.getInt("id");
//        String name = rs.getString("name");
//        String city = rs.getString("city");
//        System.out.println(x + name + city);
//        
//    }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == cos1) {
                activeCostumer = costumerList.get(0);
                productsGUI();
            } else if (e.getSource() == cos2) {
                activeCostumer = costumerList.get(1);
                productsGUI();
            } else if (e.getSource() == cos3) {
                activeCostumer = costumerList.get(2);
                productsGUI();
            } else if (e.getSource() == cos4) {
                activeCostumer = costumerList.get(3);
                productsGUI();
            } else if (e.getSource() == cos5) {
                activeCostumer = costumerList.get(4);
                productsGUI();
            } else if (e.getSource() == productButtons[0]) {
                chosenShoe = shoeList.get(0);
                ordersGUI();
            } else if (e.getSource() == productButtons[1]) {
                chosenShoe = shoeList.get(1);
                ordersGUI();
            } else if (e.getSource() == productButtons[2]) {
                chosenShoe = shoeList.get(2);
                ordersGUI();
            } else if (e.getSource() == productButtons[3]) {
                chosenShoe = shoeList.get(3);
                ordersGUI();
            } else if (e.getSource() == productButtons[4]) {
                chosenShoe = shoeList.get(4);
                ordersGUI();
            } else if (e.getSource() == productButtons[5]) {
                chosenShoe = shoeList.get(5);
                ordersGUI();
            } else if (e.getSource() == productButtons[6]) {
                chosenShoe = shoeList.get(6);
                ordersGUI();
            } else if (e.getSource() == productButtons[7]) {
                chosenShoe = shoeList.get(7);
                ordersGUI();
            } else if (e.getSource() == productButtons[8]) {
                chosenShoe = shoeList.get(8);
                ordersGUI();
            } else if (e.getSource() == orderButtons[0]) {
                chosenOrder = orders.get(0);
                r.callAddToCart(activeCostumer.getId(), chosenOrder.getId(), chosenShoe.getId());
                System.out.println("En ny produkt har lagts till för " + activeCostumer.getName());
                printToFile(activeCostumer, chosenShoe);
            } else if (e.getSource() == orderButtons[1]) {
                chosenOrder = orders.get(1);
                r.callAddToCart(activeCostumer.getId(), chosenOrder.getId(), chosenShoe.getId());
                System.out.println("En ny produkt har lagts till för " + activeCostumer.getName());
                printToFile(activeCostumer, chosenShoe);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(JDBC_Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void productsGUI() throws SQLException {
        remove(costumerPanel);

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(3, 3));

        shoeList = r.getProducts();

        for (int i = 0; i < 9; i++) {
            JButton tempButton = new JButton();
            String categories;
            String brands;
            String colours;

            categories = String.join("/", shoeList.get(i).getCategories());
            brands = String.join("/", shoeList.get(i).getBrands());
            colours = String.join("/", shoeList.get(i).getColours());

            tempButton.setText("Pris:\t" + shoeList.get(i).getPrice()
                    + "\nStrl:\t" + shoeList.get(i).getSize()
                    + "\nKategori:\t" + categories
                    + "\nMärken:\t" + brands
                    + "\nFärger:\t" + colours);

            productButtons[i] = new JButton();
            productButtons[i] = tempButton;
            tempButton.addActionListener(this);
            productPanel.add(tempButton);

        }

        productPanel.setSize(1400, 1400);
        add(productPanel);
        setSize(1500, 1500);

    }

    public void ordersGUI() throws SQLException {
        remove(productPanel);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new GridLayout(2, 2));

        orders = new ArrayList<>();
        orders = r.getOrders(activeCostumer.getName());

        for (int o = 0; o < orders.size(); o++) {
            JButton tempButton = new JButton();
            tempButton.setText(orders.get(o).getDate().toString());
            tempButton.addActionListener(this);
            orderButtons[o] = new JButton();
            orderButtons[o] = tempButton;
            ordersPanel.add(tempButton);

        }

        newOrder = new JButton();
        newOrder.setText("Ny beställning");
        newOrder.addActionListener(e -> {
            try {
                r.callAddToCartNewOrder(activeCostumer.getId(), 0, chosenShoe.getId());
                System.out.println("En ny beställning har lagts till för " + activeCostumer.getName());
                printToFile(activeCostumer, chosenShoe);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(JDBC_Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        exit = new JButton("Exit");
        exit.addActionListener(ALExit);
        ordersPanel.add(exit);
        ordersPanel.add(newOrder);
        ordersPanel.setSize(500, 500);
        add(ordersPanel);
        setSize(600, 600);

    }

    public void printToFile(Costumer costumer, Shoe shoe) throws IOException {
        PrintWriter ut = new PrintWriter(new BufferedWriter(new FileWriter("test.txt", true)));
        ut.println(costumer.getName() + " har lagt en beställning på sko nr " + shoe.getId());
        ut.close();
    }

}
