package chatclient.login;

import chatclient.home.HomeController;
import chatclient.register.RegisterController;
import domains.Session;
import interfaces.IChatServerManager;
import interfaces.IUser;
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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.JOptionPane.showMessageDialog;

public class LoginController {
    private Registry registry;
    private Session session;
    private String ip = "192.168.87.135";
    private int port = 1099;
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;

    public LoginController() {
        try {
            System.setProperty("java.rmi.server.hostname","192.168.1.114");
            this.registry = locateRegistry();
            if (registry != null) {
                     this.session = new Session((IChatServerManager) registry.lookup("chatserver"));
                 }
            }
            catch (SQLException | IOException | ClassNotFoundException | NotBoundException e) {
                errorServer();
            this.session = null;
        }

    }
    @FXML
    private void register()
    {
        toRegisterScreen();
    }
    @FXML
    private void toRegisterScreen()  {
        // Set the next "page" (scene) to display.
        // Note that an incorrect path will result in unexpected NullPointer exceptions!
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../register/Register.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE,"LoginController",e);
        }
        RegisterController controller = fxmlLoader.<RegisterController>getController();
        controller.setSession(session);
        // There's no additional data required by the newly opened form.
        Scene registerScreen = new Scene(root);

        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.

        stage.setScene(registerScreen);
        stage.show();
    }

    private void toHomeScreen(IUser user)  {
        // Set the next "page" (scene) to display.
        // Note that an incorrect path will result in unexpected NullPointer exceptions!
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../home/Home.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE,"LoginController",e);
        }
        HomeController controller = fxmlLoader.<HomeController>getController();
        controller.setSettings(session);
        // There's no additional data required by the newly opened form.
        Scene registerScreen = new Scene(root);

        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.

        stage.setScene(registerScreen);
        stage.show();
    }
    @FXML
    private void login()
    {
        IUser user;
        if (!txt_username.getText().trim().isEmpty() && !txt_password.getText().trim().isEmpty() && session.getServer() != null) {
            try {
                user = session.getServer().login(txt_username.getText(),md5(txt_password.getText()));
                if (user != null){
                    session.setUser(user);
                    toHomeScreen(user);
                }
                else
                {
                    showMessageDialog(null, "username or password incorrect");
                }
            } catch (RemoteException e) {
                errorServer();
            }
        }

    }
    private String md5(String input) {

        String md5 = null;

        if(null == input) return null;

        try {
            input += "FEJOE0dC2u";
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            if (session != null)
            {
                try {
                    session.getServer().sendMail(0,e.toString());
                } catch (RemoteException e1) {
                    errorServer();
                }
            }
        }
        return md5;
    }

    private Registry locateRegistry() throws SQLException, IOException, ClassNotFoundException {
        try
        {
            return LocateRegistry.getRegistry(ip, port);
        }
        catch (RemoteException ex) {
            errorServer();
            return null;
        }
    }

    public void setSettings(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    private void errorServer()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("No connection to Server");
        alert.setContentText("The server is unavailable at this time, try again later.");
        alert.show();
    }
}
