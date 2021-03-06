package chatclient.chat;

import chatclient.home.HomeController;
import domains.Message;
import domains.Session;
import interfaces.*;
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
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.swing.*;
import java.io.IOException;
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
    private Session session;
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
    public void setup(Session session, IChat chat)
    {
        this.session = session;
        this.chat = chat;
        this.txt_username.setText(chat.getUser_Name());
        this.txt_chat.setText(chat.getName());
        try {
            session.getServer().addListener(this);
        } catch (RemoteException e) {
            errorServer();
        }
    }
    @FXML
    private void sendMessage()
    {
        if (!txt_message.getText().trim().isEmpty())
        {
            try {
                session.getServer().sendMessage(session.getUser().getID(),chat.getID(),txt_message.getText());
            } catch (RemoteException e) {
                errorServer();
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
        String chatName = JOptionPane.showInputDialog("Type in a new chat name: ");
        if (chatName != "" && chatName != null)
        {
            txt_chat.setText(chatName);
            try {
                session.getServer().renameChat(this.chat.getID(),chatName);
            } catch (RemoteException e) {
                errorServer();
            }
        }


    }
    @FXML
    private void toHomeScreen()
    {
        try {
            session.getServer().removeListener(this);
        } catch (RemoteException e) {
            errorServer();
        }
        messageTimer.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../home/Home.fxml"));
        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            try {
                session.getServer().sendMail(session.getUser().getID(),e.toString());
            } catch (RemoteException e1) {
                errorServer();
            }
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
        return this.session.getUser().getID();
    }

    @Override
    public List<IMessage> getchatMessages() {
        return this.messages;
    }

    @Override
    public void addMessage(Message message) throws RemoteException {
        this.messages.add(message);
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
