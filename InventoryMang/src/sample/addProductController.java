package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static sample.Main.checkInt;

public class addProductController implements Initializable {
    //initialises all the controls within the addProduct.fxml
    @FXML
    private TextField productID;
    @FXML
    private TextField productName;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productLocation;
    @FXML
    private TextField StockQuantity;
    @FXML
    private Button logout;
    @FXML
    private Button addProductButton;
    @FXML
    private Label productAdd;
    @FXML
    private Button viewProducts;
    @FXML
    private Button viewEmployees;

    public boolean viewProductReset = true;

    Stage dialogStage = new Stage();
    Scene scene;

    public void addProduct(){
        addProductButton.setOnAction(event1 -> newProduct());
    }

    public void newProduct() {
        //sets up the database connection
        Connection conn = databaseConnection.connect();
        try {
            //the sql string to add a new product
            String sql = "INSERT INTO products(PRODUCT_ID,PRODUCT_NAME,PRODUCT_PRICE,PRODUCT_LOCATION,PRODUCT_STOCK) VALUES(?,?,?,?,?)";
            //prepares the sql statement by linking it to the database
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //initialises the productIDInt
            int productIDInt;
            //initialises the productPriceInt
            int productPriceInt;
            //initialises the productStockInt
            int productStockInt;
            //initialises approved
            boolean approved=true;
            //checks that the values entered for productID product price and product Stock are integers
            productIDInt=checkInt(productID.getText());
            productPriceInt=checkInt(productPrice.getText());
            productStockInt=checkInt(StockQuantity.getText());
            //if the values are integers than a label is displayed showing the error and the product isn't added to the database
            if(productIDInt<=0||productPriceInt<=0||productStockInt<0){
                productAdd.setText("A string entered where there should be an integer");
                approved=false;

            }
            //if the values are the correct data types than they are added to the database
            if(approved){
                pstmt.setInt(1,productIDInt);
                pstmt.setString(2, productName.getText());
                pstmt.setInt(3, productPriceInt);
                pstmt.setString(4, productLocation.getText());
                pstmt.setInt(5, productStockInt);

                pstmt.executeUpdate();
                //labels tells the user that the product has been successfully added
                productAdd.setText("Product Successfully Added");
            }

        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                //if the database connection isn't closed than it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void addProductOnAction() throws IOException {
        newProduct();
    }

    //when the logout button is pressed the scene is changed
    public void logoutOnAction() throws IOException{
        viewProductReset = true;
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        dialogStage.setTitle("Sign in");
        dialogStage.setScene(new Scene(root, 526.4, 400));
        dialogStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addProduct();
    }

    //if the viewEmployees button is pressed the scene is changed
    public void viewEmployeesOnAction() throws IOException {
        viewProductReset = true;
        Stage stage = (Stage) viewEmployees.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("viewEmployees.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }

    //if the home button is pressed the scene is changed
    public void viewProductsOnAction() throws IOException {
        viewProductReset = true;
        Stage stage = (Stage) viewProducts.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }
}
