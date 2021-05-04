package sample;

import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;

import static sample.Main.checkInt;
import static sample.Main.oldRecord;

public class editProductController implements Initializable{
    Stage dialogStage = new Stage();
    Scene scene;
    //initialises all the controls within the editProduct.fxml
    @FXML
    public TextField productID;

    @FXML
    public TextField productName;

    @FXML
    public TextField productPrice;

    @FXML
    public TextField productLocation;

    @FXML
    public TextField StockQuantity;

    @FXML
    public Button updateDetails;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label editLabel;
    //assigns the class Product to the variable item
    public Product item;

    public boolean editBoolean = true;
    //initialises the arrays entry and newEntry
    public String[] entry;
    public String[] newEntry;

    public String newID;
    public String newName;
    public String newPrice;
    public String newLoc;
    public String newStock;

    private ActionEvent event;

    boolean allowed = true;

    public String[] setData(Product item) {
        //gets just one specific product
        this.item = item;
        //sets the product details from the Product class
        //initialises all the Strings that are used within this class
        String id = item.getProductId();
        String name = item.getProductName();
        String price = item.getProductPrice();
        String loc = item.getProductLoc();
        String stock = item.getStock();
        //adds the details to the array entry
        entry = new String[]{id, name, price, loc, stock};
        //returns the array entry
        return entry;
    }

    public String[] newData(){
        //initialises the variables productPriceInt and productStockInt
        int productPriceInt;
        int productStockInt;
        //if the textfield is not blank then the text is taken and stored in the variable newName
        if(!productName.getText().isBlank()) {
            newName = productName.getText();
        }if(!productPrice.getText().isBlank()) {
            newPrice = productPrice.getText();
        }if(!productLocation.getText().isBlank()) {
            newLoc = productLocation.getText();
        }if(!StockQuantity.getText().isBlank()){
            newStock = StockQuantity.getText();
        }
        //passes the values for productPrice and productStock to check that its inetgers that have been entered
        productPriceInt=checkInt(newPrice);
        productStockInt=checkInt(newStock);
        //if the value returned is less or equal than 0 for productPrice or less than 0 for productStock the product is not edited
        if(productPriceInt<=0||productStockInt<0){
            editLabel.setText("A string entered where there should be an integer"); //replace with alert
        }else{
            //if the values entered are integers than the values are added to the array newEntry
            newEntry = new String[]{newID, newName, newPrice, newLoc, newStock};
        }
        //the array newEntry is returned
        return newEntry;
    }

    //update details button pressed
    @FXML
    public void updateDetailsOnAction(ActionEvent event) throws IOException {
        //productController is assigned to the variable data
        productController data = new productController();
        //a new array is made using the value returned from the method newData
        String[] editedData =newData();
        //if the array edittedData is null, it is assigned the value form the global array oldRecord
        if(editedData==null){
            editedData=oldRecord;
        }
        //the editProducts method is caled from the productsController class and the array edittedData is passed through it
        data.editProducts(editedData);

        finished();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    //when the update function is finished, it is returned to the normal menu
    public void finished() throws IOException {
        oldRecord = null;
        editBoolean = true;
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }

    @FXML
    public void deleteButtonOnAction(ActionEvent event) throws IOException {
        productController data = new productController();
        //when the delete button is pressed the deleteProduct method from the productController class is called
        data.deleteProduct();
        //reverts the scene back to the menu on load
        finished();
    }

    //when the cancel button is pressed it resets the scene to the initial menu load page
    @FXML
    public void cancelButtonOnAction(ActionEvent event) throws IOException {
        editBoolean = true;
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }
}

