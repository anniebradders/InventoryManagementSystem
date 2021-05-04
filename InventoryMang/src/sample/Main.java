package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //makes a public array which stores a row from the products database
    public static String[] oldRecord;
    //makes a public array which stores the row from the users database for whoever signed in
    public static String[] userDetails;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //loads the initial scene for the user to log in
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        //sets the title of the scene as Sign In
        primaryStage.setTitle("Sign In");
        //sets the height and width of the scene
        primaryStage.setScene(new Scene(root, 526.4, 400));
        //displays the scene
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    //checks whether the valued entered in a textfield is an integer or string
    public static Integer checkInt(String stringConvert) {
        try {
            //if the value is an integer the value is returned
            return Integer.parseInt(stringConvert);
            //if the value is a string the value -9 is returned
        } catch (NumberFormatException e) {
            return(-9);
        }
    }
}
