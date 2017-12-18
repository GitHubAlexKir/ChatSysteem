package ChatClient.ChatBot;

import ChatClient.Chat.CustomListCell;
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

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatBotController {
    @FXML
    private Text txt_username;
    @FXML
    private ListView lv_messages;
    @FXML
    private TextArea txt_message;
    private IUser user;
    private IChatServerManager server;
    private List<IMessage> messages = new ArrayList<>();



    public ChatBotController() throws RemoteException {

    }

    public void setup(IUser user, IChatServerManager server)
    {
        this.user = user;
        this.server = server;
        this.txt_username.setText("ChatBot");
    }
    private void updateListViewMessages()
    {
        lv_messages.setCellFactory(t -> new CustomListCell("ChatBot",lv_messages.getWidth()));
        lv_messages.getItems().clear();
        for (IMessage message:messages
                ) {
            lv_messages.getItems().add(message);
        }
    }
    @FXML
    private void sendMessage()
    {
        if (!txt_message.getText().trim().isEmpty())
        {
            try {
                addMessage(new Message(1,txt_message.getText(),false));
                String answer = server.askQuestion(txt_message.getText());
                addMessage(new Message(1,answer,true));
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
    private void toHomeScreen()
    {
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

    public void addMessage(Message message) throws RemoteException {
        this.messages.add(message);
        updateListViewMessages();
    }
}
