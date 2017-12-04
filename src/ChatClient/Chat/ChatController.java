package ChatClient.Chat;

import ChatClient.Home.HomeController;
import Interfaces.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatController extends UnicastRemoteObject implements IListener  {
    @FXML
    private Text txt_username;
    @FXML
    private ListView lv_messages;
    @FXML
    private TextArea txt_message;
    private IUser user;
    private IChatServerManager server;
    private IChat chat;
    private List<IMessage> messages = new ArrayList<>();
    private AnimationTimer messageTimer;




    public ChatController() throws RemoteException {
        messageTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateListViewMessages();
            }
        };
        messageTimer.start();
    }
    private void updateListViewMessages() {
        lv_messages.setCellFactory(t -> new CustomListCell());
        lv_messages.getItems().clear();
        for (IMessage message:messages
                ) {
            lv_messages.getItems().add(message);
        }
    }
    public void setup(IUser user, IChatServerManager server, IChat chat)
    {
        this.user = user;
        this.server = server;
        this.chat = chat;
        try {
            server.addListener(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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

    @Override
    public void setChatMessages(List<IMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getChatId() {
        return this.chat.getID();
    }

    @Override
    public int getUserId() {
        return this.user.getID();
    }

    @Override
    public List<IMessage> getchatMessages() {
        return this.messages;
    }
}
