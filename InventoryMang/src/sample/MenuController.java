package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    //assigns variables to all the children from the fxml file (label, button, gridpane, etc)
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchProduct;
    @FXML
    private MenuItem sortHighLow;
    @FXML
    private MenuItem sortLowHigh;
    @FXML
    private Button viewProducts;
    @FXML
    private Button logout;
    @FXML
    public Button searchButton;
    @FXML
    private Button addProduct;
    @FXML
    private Button viewEmployees;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button editOwnDetails;
    //sql will be equal to this string unless altered
    public String sql  = "SELECT * FROM products";
    //initialises string searching
    public static String searching = null;
    //initialises string boolean
    public static boolean menuBoolean = true;
    //creates a variable dialogStage for when a scene is to be replaced
    Stage dialogStage = new Stage();
    Scene scene;
    //initialises the list data
    public List<Product> data = new ArrayList<>();
    //initialises ResultSet under the variable res so it can be used across try statements
    public ResultSet res;

    public void menuController(){}

    public List<Product> getData(String sql){
        //when sortHighLow button is pressed sortHighLowOnAction() is called
        sortHighLow.setOnAction(event1 -> sortHighLowOnAction());
        //when sortLowHigh button is pressed sortLowHighOnAction() is called
        sortLowHigh.setOnAction(event1 -> sortLowHighOnAction());
        //when viewProducts button is pressed viewProductsOnAction() is called
        viewProducts.setOnAction(event1 -> viewProductsOnAction());
        //when searchButton is pressed searchButtonOnAction() is called
        searchButton.setOnAction(event1 -> searchButtonOnAction());
        //sets up the database connection
        Connection conn = databaseConnection.connect();
        data = null;
        data = new ArrayList<>();
        Product item;
        try {
            //if b is equal to true the products are called by the newest - oldest
            if(menuBoolean) {
                //assigns the ability to create an sql statement to the variable stmt
                Statement stmt = conn.createStatement();
                //assigns the ability to execute the sql statement (get the result set) to the variable res
                res = stmt.executeQuery(sql);
            //if b is equal to false the products are ordered in a certain way depending on the sql statement
            }if(!menuBoolean){
                //assigns the ability to parepare an sql statement to the variable found
                PreparedStatement found = conn.prepareStatement(sql);
                //sets the value of ? in the sql statement to the value of the variable searching
                found.setString(1, searching);
                //assigns the ability to execute the sql statement to the varuable res
                res = found.executeQuery();
            }
            {
                //loop through the columns within the database products (while there is another row loop)
                while (res.next()) {
                    String id = res.getString("ID");
                    String prodId = res.getString("PRODUCT_ID");
                    String prodName = res.getString("PRODUCT_NAME");
                    String prodPrice = res.getString("PRODUCT_PRICE");
                    String prodLocation = res.getString("PRODUCT_LOCATION");
                    String prodQuan = res.getString("PRODUCT_STOCK");

                    //creates a new product
                    item = new Product();
                    //assigns the methods within the product class to the values of the database
                    item.setId(id);
                    item.setProductId(prodId);
                    item.setProductName(prodName);
                    item.setProductPrice(prodPrice);
                    item.setProductLoc(prodLocation);
                    item.setStock(prodQuan);

                    //adds new item to the list data
                    data.add(item);
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                //if the database connection is not closed, its closed here
                if (conn != null) {
                    conn.close();
                }
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        //returns value of the list data
        return data;
    }

    public void sortLowHighOnAction(){
        menuBoolean = true;
        //changes the sql statement so the prodcuts are sorted by price from low - high
        sql = "SELECT * FROM products ORDER BY PRODUCT_PRICE";
        //calls the method viewProducts
        viewProducts();
    }

    public void viewProductsOnAction(){
        menuBoolean = true;
        //changes the sql statement so the prodcuts are not sorted
        sql  = "SELECT * FROM products";
        //calls the method viewProducts
        viewProducts();
    }

    public void sortHighLowOnAction(){
        menuBoolean = true;
        //changes the sql statement so the prodcuts are sorted by price from high - low
        sql = "SELECT * FROM products ORDER BY PRODUCT_PRICE DESC";
        //calls the method viewProducts
        viewProducts();
    }

    public void searchButtonOnAction(){
        //if the textfield searchProduct is not blank
        if(!searchProduct.getText().isBlank()) {
            //changes the boolean value so that the else if statement is run in the viewProducts method
            menuBoolean = false;
            //clears the gridpane so all the products dissapear
            grid.getChildren().clear();
            //gets the text from the search bar
            searching = searchProduct.getText();
            //creates a new sql statement
            sql = "SELECT * FROM products WHERE PRODUCT_NAME = ?";
            //runs the method viewProducts
            viewProducts();
        }
    }

    public void viewProducts(){
        //adds all the values to the list data from the method getData
        data.addAll(getData(sql));
        //initialises the variable column
        int column = 0;
        //initialises the variable row
        int row = 0;
        try {
            //for loop so the number of product.fxml's that are created are the number within the list edittedproducts
            for (Product data : data) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                //loads new scene product.fxml within the scene menu.fxml/userMenu.fxml
                fxmlLoader.setLocation(getClass().getResource("product.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();
                //assigns the class productController to the varibale productController
                productController productController = fxmlLoader.getController();
                //calls the method setData from the class productController to fill the labels with the data from the database
                productController.setData(data);
                //if column is equal to 2 than its reset the value to 0, and creates a new row
                if (column == 2) {
                    column = 0;
                    row++;
                }
                //(child, column, row)
                grid.add(anchorPane, column++, row);
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                //sets a margin for the products.fxml
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewProducts();
    }

    //changes the scene when the view employees button is pressed
    public void viewEmployeesOnAction() throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) viewEmployees.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("viewEmployees.fxml"));
        dialogStage.setTitle("View Employees");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }
    //changes the scene when the logout button is pressed
    @FXML
    public void logoutOnAction() throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        dialogStage.setTitle("Sign in");
        dialogStage.setScene(new Scene(root, 526.4, 400));
        dialogStage.show();
    }
    //changes the scene when the add new product button is pressed
    @FXML
    public void addProductOnAction() throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) addProduct.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        dialogStage.setTitle("Add Product");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }
    //changes the scene when the edit own details button is pressed
    public void editOwnDetailsOnAction() throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) editOwnDetails.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("editOwnDetails.fxml"));
        dialogStage.setTitle("Edit Details");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }
}

