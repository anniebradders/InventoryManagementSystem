package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

import static sample.LoginController.dataRole;
import static sample.Main.oldRecord;


public class productController implements Initializable {
    //initialises all the controls within the product.fxml file (e.g. buttons, labels, etc)
    @FXML
    private Label productName;

    @FXML
    private Label productID;

    @FXML
    private Label productPrice;

    @FXML
    private Label productLocation;

    @FXML
    private Label productStock;

    @FXML
    private Button editProduct;

    @FXML
    public AnchorPane rootPane;

    private ActionEvent event;
    //initialises the array old
    public String[] old;
    //initialises the string id
    public String id = null;
    //creates the variable item for the product class
    public Product item;
    //creates the variable edit for the class editProductController
    public editProductController edit = new editProductController();
    //initialises the array entry
    public String[] entry;


    //sets the labels in the product.fxml to a specific item
    public Product setData(Product item) {
        //item is worth one specific item
        this.item = item;
        //sets the label productName to the value of getProductName method from the Product class
        productName.setText(item.getProductName());
        productID.setText(item.getProductId());
        productPrice.setText("£" + item.getProductPrice());
        productLocation.setText(item.getProductLoc());
        productStock.setText(item.getStock());
        return item;
    }

    public String[] dataNeed() throws IOException {
        //sets the value of the array entry to the array returned in the class setData from editProductController class
        entry = edit.setData(item);
        //returns the array entry
        return entry;
    }

    @FXML
    //when the editProduct button is clicked
    public void editProductOnAction(ActionEvent event) throws IOException {
        //if the global array oldRecord equals null than it is assigned the value returned from dataNeed()
        if(oldRecord ==null) {
            oldRecord = dataNeed();
        //if the role assigned to the user is not Admin it does not allow the action to go any further
        }if(!dataRole.equals("Admin")){
        //if the role assigned to the user is Admin than the scene is changed to editProduct.fxml
        }else{
            //changes the panel
            AnchorPane pane = FXMLLoader.load(getClass().getResource("editProduct.fxml"));
            rootPane.getChildren().setAll(pane);
        }
    }

    @Override
    //when the page is loaded
    public void initialize(URL location, ResourceBundle resources) {
        //if the role assigned to the user isnt Admin
        if(!dataRole.equals("Admin")) {
            //than the edit button opacity is set to 0, so it is not displayed
            editProduct.setOpacity(0);
        }
    }
    //method to edit details
    public void editProducts(String[] newEntry) throws IOException {
        //connection to the database is established
        Connection conn = databaseConnection.connect();
        //array entry is set to the value of oldRecord
        String[] entry = oldRecord;
        oldRecord =null;
       //the string id is set to the 1st entry from the array oldRecord (the id column)
        String id = entry[0];
        //creates an array containing the names of the columns within the products database we want to change
        String[] recordNames={
                "PRODUCT_NAME",
                "PRODUCT_PRICE",
                "PRODUCT_LOCATION",
                "PRODUCT_STOCK",
        };
        //creates a for loop
        for(int i=0;i<4;i++){
            try{
                //the for loop causes the column names to all be looped through
                String newProd = newEntry[i+1];
                //sql statement to edit the product
                String sql = "UPDATE products SET "+recordNames[i]+" = ? WHERE PRODUCT_ID = ?";
                //creates the sql statement
                PreparedStatement ps = conn.prepareStatement(sql);
                //sets the first ? to the users new input
                ps.setString(1, newProd);
                //sets the second ? to the value of the String id
                ps.setString(2, id);
                //executes the sql command
                ps.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    //method to delete product
    public void deleteProduct(){
        //id is set to the 1st entry within the array oldRecord
        id = oldRecord[0];
        //sql statement to delete a product
        String sql = "DELETE FROM products WHERE PRODUCT_ID = ?";
        //creates a database connection
        Connection conn = databaseConnection.connect();
        try {
            //prepares the sql statement by linking it with the database connection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //sets the first ? to the value of the String id
            pstmt.setString(1, id);
            //executes the sql
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                //if the database connection isn't closed, its closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
