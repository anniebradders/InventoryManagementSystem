package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    //id, username, password and role are intialised
    public SimpleIntegerProperty id;
    public SimpleStringProperty username;
    public SimpleStringProperty password;
    public SimpleStringProperty role;

    //allows the data to be used across classes
    public User(int id, String username, String password, String role) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
    }

    public int getID(){ return id.get();}

    public int setID(int id){
        this.id = new SimpleIntegerProperty(id);
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public String setUsername(String username) {
        this.username = new SimpleStringProperty(username);
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public String setPassword(String password) {
        this.password = new SimpleStringProperty(password);
        return password;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role = new SimpleStringProperty(role);
    }
}
