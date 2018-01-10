package chatclient.chatbot;

import chatclient.chat.CustomListCell;
import chatclient.home.HomeController;
import domains.Message;
import domains.Request;
import domains.Session;
import interfaces.*;
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
import java.util.ArrayList;
import java.util.List;

public class ChatBotController {
    @FXML
    private Text txt_username;
    @FXML
    private ListView lv_messages;
    @FXML
    private TextArea txt_message;
    private Session session;
    private List<IMessage> messages = new ArrayList<>();
    private boolean github = false;


    public ChatBotController() throws RemoteException {

    }

    public void setup(Session session)
    {
        this.session = session;
        this.txt_username.setText("chatbot");
    }
    private void updateListViewMessages()
    {
        lv_messages.setCellFactory(t -> new CustomListCell("chatbot",lv_messages.getWidth()));
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
                String answer = session.getServer().askQuestion(new Request(txt_message.getText(),github));
                if (txt_message.getText().toLowerCase().contains(" github"))
                {
                    github = true;
                }
                else
                {
                    github = false;
                }
                addMessage(new Message(1,answer,true));
            } catch (RemoteException e) {
                errorServer();
            }
            txt_message.clear();
        }
    }
    @FXML
    private void toHomeScreen()
    {
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

    public void addMessage(Message message) throws RemoteException {
        this.messages.add(message);
        updateListViewMessages();
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
