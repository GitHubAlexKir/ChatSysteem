package ChatClient.Register;

import ChatClient.Login.LoginController;
import Interfaces.IChatServerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                if (server.register(txt_username.getText(),md5(txt_password.getText()))) {
                    backToLogin();
                }
                else {
                    showMessageDialog(null, "Username is already taken.");
                }
            } catch (RemoteException  | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No connection to Server");
                alert.setContentText("The server is unavailable at this time, try again later.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }
        }
    }
    private String md5(String input) {

        String md5 = null;

        if (null == input) return null;

        try {
            input += "FEJOE0dC2u";
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            if (server != null) {
                try {
                    server.sendMail(0, e.toString());
                } catch (RemoteException e1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("No connection to Server");
                    alert.setContentText("The server is unavailable at this time, try again later.");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                        }
                    });
                }
            }
        }
        return md5;
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
            try {
                server.sendMail(0,e.toString());
            } catch (RemoteException e1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No connection to Server");
                alert.setContentText("The server is unavailable at this time, try again later.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }
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
