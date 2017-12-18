package ChatClient.Chat;

import ChatClient.Home.HomeController;
import Domains.Message;
import Interfaces.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
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
    @FXML
    private Text txt_chat;
    private IUser user;
    private IChatServerManager server;
    private IChat chat;
    private List<IMessage> messages = new ArrayList<>();
    private AnimationTimer messageTimer;
    boolean autoScroll = false;



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
        lv_messages.setCellFactory(t -> new CustomListCell(chat.getUser_Name(),lv_messages.getWidth()));
        lv_messages.getItems().clear();
        for (IMessage message:messages
                ) {
            lv_messages.getItems().add(message);
        }
        if (autoScroll) {
            Platform.runLater(() -> lv_messages.scrollTo(lv_messages.getItems().size() - 1));
        }
    }
    public void setup(IUser user, IChatServerManager server, IChat chat)
    {
        this.user = user;
        this.server = server;
        this.chat = chat;
        this.txt_username.setText(chat.getUser_Name());
        this.txt_chat.setText(chat.getName());
        try {
            server.addListener(this);
        } catch (RemoteException e) {
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
    @FXML
    private void sendMessage()
    {
        if (!txt_message.getText().trim().isEmpty())
        {
            try {
                server.sendMessage(user.getID(),chat.getID(),txt_message.getText());
            } catch (RemoteException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No connection to Server");
                alert.setContentText("The server is unavailable at this time, try again later.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }
            txt_message.clear();
        }
    }
    @FXML
    private void toggleAutoScroll()
    {
        this.autoScroll = !this.autoScroll;
    }
    @FXML
    private void changeChatName()
    {
        String chatName = JOptionPane.showInputDialog("Type in a new Chat name: ");
        if (chatName != "" && chatName != null)
        {
            txt_chat.setText(chatName);
            try {
                server.renameChat(this.chat.getID(),chatName);
            } catch (RemoteException e) {
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
    @FXML
    private void toHomeScreen()
    {
        try {
            server.removeListener(this);
        } catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No connection to Server");
            alert.setContentText("The server is unavailable at this time, try again later.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                }
            });
        }
        messageTimer.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Home/Home.fxml"));
        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            try {
                server.sendMail(user.getID(),e.toString());
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

    @Override
    public void addMessage(Message message) throws RemoteException {
        this.messages.add(message);
    }
}
