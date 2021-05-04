package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static sample.viewEmployeesController.encrypt;
import static sample.viewEmployeesController.arrayCreator;

import static sample.Main.userDetails;

public class editOwnDetailsController {

    public boolean editBoolean = true;

    Stage dialogStage = new Stage();
    Scene scene;

    @FXML
    private Button updateUser;

    @FXML
    private Button updatePassword;

    @FXML
    private TextField newUsername;

    @FXML
    private PasswordField newPassword;

    @FXML
    private Button logout;

    @FXML
    private Button viewProducts;

    @FXML
    private Label updateSuccess;

    public String updateData;
    public int oldData;

    public void viewProductsOnAction(ActionEvent actionEvent) throws IOException {
        editBoolean = true;
        Stage stage = (Stage) viewProducts.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1000, 600));
        dialogStage.show();
    }

    public void editDetailsOnAction(ActionEvent actionEvent) {
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        editBoolean = true;
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        dialogStage.setTitle("Sign in");
        dialogStage.setScene(new Scene(root, 526.4, 400));
        dialogStage.show();
    }

    public String column;

    public void updateUserOnAction(ActionEvent actionEvent) {
        column = "USERNAME";
        updateData = newUsername.getText();
        oldData = Integer.parseInt(userDetails[0]);
        updateDetails(updateData, oldData);

    }

    int[] valueArray;
    String storedPassword;

    public void updatePasswordOnAction(ActionEvent actionEvent) {
        column = "PASSWORD";
        updateData = newPassword.getText();
        oldData = Integer.parseInt(userDetails[0]);
        storedPassword = arrayCreator(updateData);
        updateDetails(storedPassword, oldData);
    }

    public void updateDetails(String updateData, int oldData){
        Connection conn = databaseConnection.connect();
        try {
            String sql = "UPDATE users SET " + column + " = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, updateData);
            ps.setInt(2, oldData);
            ps.execute();
            updateSuccess.setText("Account Successfully Update");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
