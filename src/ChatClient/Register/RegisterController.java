package ChatClient.Register;

import ChatClient.Login.LoginController;
import Interfaces.IChatServerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

import static javax.swing.JOptionPane.showMessageDialog;

public class RegisterController {
    private IChatServerManager server;
    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private PasswordField txt_password_confirm;
    @FXML
    private void confirmRegister()
    {
        if (txt_username.getText() == null || txt_username.getText().trim().isEmpty()) {
            showMessageDialog(null, "Username is a required field.");
            System.out.println("Username is a required field.");
        }
        else if (txt_username.getText().matches("[0-9]+")) {
            showMessageDialog(null, "Username cannot consist of numbers exclusively.");
            System.out.println("Username cannot consist of numbers exclusively.");
        }
        else if (txt_password.getText() == null || txt_password.getText().trim().isEmpty()) {
            showMessageDialog(null, "Password is a required field.");
            System.out.println("Password is a required field.");
        }
        else if (!txt_password.getText().equals(txt_password_confirm.getText())) {
            // Passwords do NOT match.
            showMessageDialog(null, "Passwords don't match.");
            System.out.println("Passwords don't match.");
        }
        else {
            try {
                if (server.register(txt_username.getText(),txt_password.getText())) {
                    backToLogin();
                }
                else {
                    showMessageDialog(null, "Username is already taken.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void cancelRegister()
    {
        backToLogin();
    }

    private void backToLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../login/login.fxml"));
        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController controller = fxmlLoader.<LoginController>getController();
        Scene loginScreen = new Scene(root);
        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.
        stage.setScene(loginScreen);
        stage.show();
    }

    public void setChatServer(IChatServerManager server) {
        this.server = server;
    }
}
