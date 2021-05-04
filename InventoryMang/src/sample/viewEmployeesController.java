package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class viewEmployeesController implements Initializable {
    //initialises the ability to create new stages under the variable dialogStage
    Stage dialogStage = new Stage();
    Scene scene;
    //initialises the controls within the viewEmployees.fxml
    @FXML
    public TextField newUsername;
    @FXML
    public TextField newPassword;
    @FXML
    private TableView<User> tableview;
    @FXML
    private TableColumn<User,String> UserName;
    @FXML
    private TableColumn<User,String> Password;
    @FXML
    private TableColumn<User,String> Role;
    @FXML
    private Button viewProducts;
    @FXML
    private Button viewEmployees;
    @FXML
    private Button addProduct;
    @FXML
    private Button logout;
    //initialises the observable list data
    private ObservableList<User> data;
    //initialises the strings username, password and role
    private String username;
    private String password;
    private String role;
    private int id;
    //initialises the boolean b
    public boolean b = true;

    int[] valueArray;
    String storedPassword;

    //method to display users in a table
    public void displayUsers(){
        //makes data a new empty observable list that is backed by an arraylist.
        data = FXCollections.observableArrayList();
        //creates a connection to the database
        Connection conn = databaseConnection.connect();
        try{
            //creates a string containing the sql command
            String sql = "SELECT * FROM users";
            //creates a connection between the database and sqlite
            Statement stmt  = conn.createStatement();
            //executes the sql statement
            ResultSet res = stmt.executeQuery(sql);
            //while there is another row in the database
            while(res.next()){
                //a new user is created based off thr class user
                User user = new User(id,username, password, role);
                user.id.set(res.getInt("ID"));
                //username is get to the value from the USERNAME column in the users database
                user.username.set(res.getString("USERNAME"));
                //password is get to the value from the PASSWORD column in the users database
                user.password.set(res.getString("PASSWORD"));
                //role is get to the value from the ROLE column in the users database
                user.role.set(res.getString("ROLE"));
                //user is added to the observable list data
                data.add(user);
            }
            //sets the item within the correct row and column in the database
            tableview.setItems(data);

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                //if the database connection is not closed, it is closed here
                if (conn != null) {
                    conn.close();
                }
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    //when the scene is loaded
    public void initialize(URL location, ResourceBundle resources){
        assert tableview != null;
        //the column labelled Username is set the value of username within the user class
        UserName.setCellValueFactory(
                new PropertyValueFactory<User,String>("username"));
        //the column labelled Password is set the value of password within the user class
        Password.setCellValueFactory(
                new PropertyValueFactory<User,String>("password"));
        //the column labelled Role is set the value of role within the user class
        Role.setCellValueFactory(
                new PropertyValueFactory<User,String>("role"));
        //table is able to be edited
        tableview.setEditable(true);
        //Username column is able to be edited
        UserName.setCellFactory(TextFieldTableCell.forTableColumn());
        //Password column is able to be edited
        Password.setCellFactory(TextFieldTableCell.forTableColumn());
        try{
          displayUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //when the home button is pressed the scene is changed
    public void viewProductsOnAction() throws IOException {
        b = true;
        Stage stage = (Stage) viewProducts.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }

    public void viewEmployeesOnAction(){
        displayUsers();
    }

    //when the add products button is pressed the scene is changed
    public void addProductOnAction() throws IOException {
        b = true;
        Stage stage = (Stage) addProduct.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        dialogStage.setTitle("Add Product");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }

    //when the logout button is pressd the scene is changed
    public void logoutOnAction() throws IOException {
        b = true;
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        dialogStage.setTitle("Sign in");
        dialogStage.setScene(new Scene(root, 526.4, 400));
        dialogStage.show();
    }

    //method to edit the username details
    public void onEditChanged(TableColumn.CellEditEvent<User, String> userStringCellEditEvent) {
        //connection to the database is established
        Connection conn = databaseConnection.connect();
        try {
            //gets the details from the row selected
            User user = tableview.getSelectionModel().getSelectedItem();
            //assigns the value id from user class based on the row selected
            int id = user.getID();
            //assigns the new value entered into the username column to the string newUser
            String newUser = user.setUsername(userStringCellEditEvent.getNewValue());
            //creates the sql statement
            String sql = "UPDATE users SET USERNAME = ? WHERE ID = ?";
            //links the sql statement and the database connection
            PreparedStatement ps = conn.prepareStatement(sql);
            //assigns the value of the first ? to the data the user has just inputted
            ps.setString(1, newUser);
            //assigns the value of the second ? to the id of the row
            ps.setInt(2, id);
            //executes the sql statement
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                //if the database connection is not closed, it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //method to edit the password details
    public void passwordOnEdit(TableColumn.CellEditEvent<User, String> PasswordStringCellEditEvent) {
        //connection to the databse is established
        Connection conn = databaseConnection.connect();
        try {
            //gets the details from the row selected
            User user = tableview.getSelectionModel().getSelectedItem();
            //assigns the value id from user class based on the row selected
            int id = user.getID();
            //assigns the new value entered into the password column to the string newPass
            String newPass = user.setPassword(PasswordStringCellEditEvent.getNewValue());
            storedPassword = arrayCreator(newPass);
            //creates the sql statement
            String sql = "UPDATE users SET PASSWORD = ? WHERE ID = ?";
            //links the sql statement and the database connection
            PreparedStatement ps = conn.prepareStatement(sql);
            //assigns the value of the first ? to the data the user has just inputted
            ps.setString(1, storedPassword);
            //assigns the value of the second ? to the id of the row
            ps.setInt(2, id);
            //executes the sql statement
            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                //if the database connection is not closed, it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //method select whole row and delete user
    public void buttonRemove(ActionEvent actionEvent) {
        //creates two observable lists allUser and singleUser
        ObservableList<User> allUser,SingleUser;
        //assigns all the data in the table to the observale list allUser
        allUser=tableview.getItems();
        //assigns the data from the row selected to the observable list SingleUser
        SingleUser=tableview.getSelectionModel().getSelectedItems();
        //runs the method removeUser to remove them from the database
        removeUser();
        //removes the vale of singleUser from the observable list allUser
        SingleUser.forEach(allUser::remove);

    }

    int userDel = 0;

    public void removeUser(){
        //creates a connection to the database
        Connection conn = databaseConnection.connect();
        try {
            //gets the details from the row selected
            User user = tableview.getSelectionModel().getSelectedItem();
            //assigns the value id from user class based on the row selected
            userDel = user.getID();
            //creates the sql statement
            String sql = "DELETE FROM users WHERE ID = ?";
            //links the sql statement to the database
            PreparedStatement ps = conn.prepareStatement(sql);
            //sets the ? to the id of the row selected
            ps.setInt(1, userDel);
            //executes the sql statement
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    //method to add new user
    public void buttonAdd(ActionEvent actionEvent) {
        //runs the values inputted by the user thorough the class User

        //adds the new user to the table

        //data inputted into the username textfield is assigned to the String username
        username = newUsername.getText();
        //data inputted into the password textfield is assigned to the String password
        password = newPassword.getText();

        storedPassword = arrayCreator(password);

        User user = new User(id, newUsername.getText(), storedPassword ,role);

        tableview.getItems().add(user);

        System.out.println(storedPassword);
        //creates a connection to the database
        Connection conn = databaseConnection.connect();
        try{
            //creates the sql statement
            String sql = "INSERT INTO users(USERNAME,PASSWORD,ROLE)VALUES(?,?,?)";
            //links the database and the sql statement
            PreparedStatement ps = conn.prepareStatement(sql);
            //assigns the first ? to the string username
            ps.setString(1, username);
            //assigns the second ? to the string password
            ps.setString(2, storedPassword);
            //assigns the third ? to the string role
            ps.setString(3, role);
            //statement is executed
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                //if the database connection isn't closed, it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //if the admin button is selected from the drop down menu role is assigned the value "Admin"
    public String adminRole(ActionEvent actionEvent) {
        role = "Admin";
        return role;
    }

    //if the employee button is selected from the drop down menu role is assigned the value "Employee"
    public String employeeRole(ActionEvent actionEvent) {
        role = "Employee";
        return role;
    }

    public static String encrypt(int[] values){
        String encryptPassword ="";
        int length = values.length;

        int[] encryptedArray = new int[length];
        for(int i=0;i<length;i++){
            values[i]=values[i]+4+i;
            if(i==length-1) {
                encryptedArray[0] = values[i];
            }
            else
            {
                encryptedArray[i + 1] = values[i];
            }
        }
        for(int i2=0;i2<length;i2++){
            int checkpoint = 65+i2;
            String checkpointStr = String.valueOf(Character.toChars(checkpoint));
            encryptPassword=encryptPassword+(encryptedArray[i2])+checkpointStr;

        }
        return encryptPassword;
    }
    public static String arrayCreator(String password){
        int length=password.length();
        int ascii;
        String encrypted;

        int[] asciiValues=new int[length];
        for(int i=0;i<length;i++){
            ascii= password.charAt(i);
            asciiValues[i]=ascii;


        }
        encrypted = encrypt(asciiValues);
        return encrypted;
    }
}
