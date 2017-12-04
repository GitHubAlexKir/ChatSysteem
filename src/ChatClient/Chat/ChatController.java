package ChatClient.Chat;

import ChatClient.Home.HomeController;
import Interfaces.IChat;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class ChatController {
    @FXML
    private Text txt_username;
    @FXML
    private ListView lv_messages;
    @FXML
    private TextArea txt_message;
    private IUser user;
    private IChatServerManager server;
    private IChat chat;
    public ChatController() {

    }
    public void setup(IUser user, IChatServerManager server, IChat chat)
    {
        this.user = user;
        this.server = server;
        this.chat = chat;
    }
    @FXML
    private void sendMessage()
    {
        if (!txt_message.getText().trim().isEmpty())
        {
            try {
                server.sendMessage(user.getID(),chat.getID(),txt_message.getText());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            txt_message.clear();
        }
    }

    @FXML
    private void toHomeScreen()
    {
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
}
