package ChatClient;

import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintStream;
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

    }

    @FXML
    private void login()
    {
        if (!txt_username.getText().trim().isEmpty() && !txt_password.getText().trim().isEmpty()) {
            try {
                user = server.login(txt_username.getText(),txt_password.getText());
                if (user != null){
                    System.out.println("success");
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
