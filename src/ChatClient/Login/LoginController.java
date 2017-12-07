package ChatClient.Login;

import ChatClient.Home.HomeController;
import ChatClient.Register.RegisterController;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class LoginController {
    private Registry registry;
    private IChatServerManager server;
    private IUser user;
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;

    public LoginController() {
        try {
            System.setProperty("java.rmi.server.hostname","127.0.0.1");
            this.registry = locateRegistry();
            this.server = (IChatServerManager) registry.lookup("ChatServer");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Register/Register.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegisterController controller = fxmlLoader.<RegisterController>getController();
        controller.setChatServer(server);
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Home/Home.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HomeController controller = fxmlLoader.<HomeController>getController();
        controller.setSettings(user,server);
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
        if (!txt_username.getText().trim().isEmpty() && !txt_password.getText().trim().isEmpty()) {
            try {
                user = server.login(txt_username.getText(),txt_password.getText());
                if (user != null){
                    System.out.println("success");
                    toHomeScreen(user);
                }
                else
                {
                    System.out.println("failed");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }
    private Registry locateRegistry() throws SQLException, IOException, ClassNotFoundException {
        try
        {
            return LocateRegistry.getRegistry("127.0.0.1", 1099);
        }
        catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            return null;
        }
    }

}
