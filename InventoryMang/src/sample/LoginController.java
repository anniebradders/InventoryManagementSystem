package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static sample.Main.checkInt;
import static sample.Main.userDetails;

public class LoginController {
    public static int attempt = 0;
    public static String dataUsername = null;
    public static String dataPassword = null;
    public static String dataRole = null;
    public static int userID;

    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Label numAttempt;
    @FXML
    private Button loginButton;

    Stage dialogStage = new Stage();
    Scene scene;

    public LoginController() {
    }

    public void loginButtonOnAction(){
        if(!usernameInput.getText().isBlank() && !passwordInput.getText().isBlank()){
            validateLogin();
        }else{
            loginMessageLabel.setText("Incorrect Username and/or Password");
        }
    }

    public String[] validateLogin(){
            Connection conn = databaseConnection.connect();
            String username = null;
            String password = null;
            Parent root;
            try {
                username = usernameInput.getText();
                password= passwordInput.getText();
                String sql = "SELECT * FROM users";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                boolean success = false;
                while (rs.next()) {
                    userID = rs.getInt("ID");
                    dataUsername = rs.getString("USERNAME");
                    dataPassword = rs.getString("PASSWORD");
                    dataPassword = decrypt(dataPassword);
                    dataRole = rs.getString("ROLE");
                    if (dataUsername.equals(username) && dataPassword.equals(password)) {
                        loginMessageLabel.setText("User is valid");
                        success = true;
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();
                        if(dataRole.equals("Admin")){
                            root = FXMLLoader.load(getClass().getResource("menu.fxml"));
                        }else{
                            root = FXMLLoader.load(getClass().getResource("userMenu.fxml"));
                        }
                        dialogStage.setTitle("Home");
                        dialogStage.setScene(new Scene(root, 1000, 600));
                        dialogStage.show();
                    }
                    if (success) {
                        break;
                    }
                }
                if (!success) {
                    attempt+=1;
                    if(attempt == 3){
                        System.exit(0); //WANT TO GET RID OF
                    }
                    else {
                        loginMessageLabel.setText("Username and/or Password Incorrect");
                        numAttempt.setText((3 - attempt) + " more attempt(s)");
                    }
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            userDetails = new String[]{String.valueOf(userID), username, password, dataRole};
            return userDetails;
    }

    public static String decrypt(String password){
        int length = password.length();
        int counter =0;
        int previous =0;
        String[] breakPoints = {};
        String breakChar;
        int[] intArray={};
        StringBuilder decrypted= new StringBuilder();
        for(int i=0;i<length;i++){
            breakChar =password.substring(i,i+1);

            if(checkInt(breakChar)==-9){
                breakPoints= Arrays.copyOf(breakPoints,breakPoints.length+1);
                if(previous==0){
                    breakPoints[counter]=password.substring(previous,i);
                }
                else{
                    breakPoints[counter]=password.substring(previous+1,i);
                }
                counter+=1;
                previous=i;
            }
        }
        for(int i2=0;i2<breakPoints.length;i2++){
            intArray = Arrays.copyOf(intArray,intArray.length+1);

            if(i2== breakPoints.length-1) {
                intArray[i2] = checkInt(breakPoints[0]);
            }else{
                intArray[i2] = checkInt(breakPoints[i2 + 1]);
            }
        }
        char convertedChar;
        for(int i3=0;i3<intArray.length;i3++){
            intArray[i3]=intArray[i3]-i3-4;
            convertedChar=(char)intArray[i3];
            decrypted.append(convertedChar);
        }
        System.out.println(decrypted);
        return decrypted.toString();
    }
}
