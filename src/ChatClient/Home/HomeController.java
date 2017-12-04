package ChatClient.Home;

import ChatClient.Login.LoginController;
import Interfaces.IChat;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class HomeController {
    @FXML
    private Text txt_username;
    private IUser user;
    private IChatServerManager server;
    private List<IChat> chats;
    public HomeController() {
    }

    public void setSettings(IUser user, IChatServerManager server)
    {
        this.user = user;
        this.server = server;
        this.txt_username.setText("Welcome back " + user.getUsername());
        loadChats();
    }

    private void loadChats()
    {
        try {
            chats = server.getChats(user.getID());
            System.out.println(chats.get(0).getUser().getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void logout()  {
        // Set the next "page" (scene) to display.
        // Note that an incorrect path will result in unexpected NullPointer exceptions!
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Login/Login.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController controller = fxmlLoader.<LoginController>getController();

        // There's no additional data required by the newly opened form.
        Scene registerScreen = new Scene(root);

        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.

        stage.setScene(registerScreen);
        stage.show();
    }
}