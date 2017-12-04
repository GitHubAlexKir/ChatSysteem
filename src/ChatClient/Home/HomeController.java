package ChatClient.Home;

import ChatClient.Login.LoginController;
import ChatClient.NewChat.NewChatController;
import Interfaces.IChat;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class HomeController {
    @FXML
    private Text txt_username;
    @FXML
    private TableView<IChat> tv_chats;
    @FXML
    private TableColumn tc_user;
    @FXML
    private TableColumn tc_name;
    @FXML
    private TableColumn tc_datecreated;
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
        tv_chats.getItems().clear();
        tc_user.setCellValueFactory(new PropertyValueFactory<IChat, String>("user_Name"));
        tc_name.setCellValueFactory(new PropertyValueFactory<IChat,String>("name"));
        tc_datecreated.setCellValueFactory(new PropertyValueFactory<IChat,String>("dateCreated"));
        try {
            chats = server.getChats(user.getID());
            if (!chats.isEmpty()) {
                for (IChat i : chats) {
                    tv_chats.getItems().add(i);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toNewChatScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../NewChat/NewChat.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewChatController controller = fxmlLoader.<NewChatController>getController();
        controller.setup(user,server);
        // There's no additional data required by the newly opened form.
        Scene registerScreen = new Scene(root);
        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.
        stage.setScene(registerScreen);
        stage.show();
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
