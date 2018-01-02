package ChatClient.Home;

import ChatClient.Chat.ChatController;
import ChatClient.ChatBot.ChatBotController;
import ChatClient.Login.LoginController;
import ChatClient.NewChat.NewChatController;
import Domains.Session;
import Interfaces.IChat;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

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
    private Session session;
    private List<IChat> chats;
    private IChat selectedChat;
    public HomeController() {
    }

    public void setSettings(Session session)
    {
        this.session = session;
        this.txt_username.setText("Welcome back " + session.getUser().getUsername());
        loadChats();
    }
    @FXML
    private void loadChats()
    {
        tv_chats.getItems().clear();
        tc_user.setCellValueFactory(new PropertyValueFactory<IChat, String>("user_Name"));
        tc_name.setCellValueFactory(new PropertyValueFactory<IChat,String>("name"));
        tc_datecreated.setCellValueFactory(new PropertyValueFactory<IChat,String>("dateCreated"));
        try {
            chats = session.getServer().getChats(session.getUser().getID());
            if (!chats.isEmpty()) {
                for (IChat i : chats) {
                    tv_chats.getItems().add(i);
                }
            }
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
    private void selectedChat()
    {
        if (tv_chats.getSelectionModel().getSelectedItem() != null)
        {
            selectedChat = tv_chats.getSelectionModel().getSelectedItem();
        }
        else
        {
            selectedChat = null;
        }
    }

    @FXML
    private void messageDeveloper()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Message to Developer");
        dialog.setHeaderText("If you have found a bug or want to ask anything feel free to sent me a message");
        dialog.setContentText("Please enter your message:");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                session.getServer().sendMail(session.getUser().getID(),result.toString());
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
    private void toChatScreen()
    {
        if (selectedChat != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Chat/Chat.fxml"));

            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                try {
                    session.getServer().sendMail(session.getUser().getID(),e.toString());
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
            ChatController controller = fxmlLoader.<ChatController>getController();
            controller.setup(session, selectedChat);
            // There's no additional data required by the newly opened form.
            Scene registerScreen = new Scene(root);
            Stage stage;
            stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.
            stage.setScene(registerScreen);
            stage.show();
        }
    }
    @FXML
    private void toChatBotScreen() {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ChatBot/ChatBot.fxml"));

            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                try {
                    session.getServer().sendMail(session.getUser().getID(),e.toString());
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
            ChatBotController controller = fxmlLoader.<ChatBotController>getController();
            controller.setup(session);
            // There's no additional data required by the newly opened form.
            Scene registerScreen = new Scene(root);
            Stage stage;
            stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.
            stage.setScene(registerScreen);
            stage.show();

    }
    @FXML
    private void toNewChatScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../NewChat/NewChat.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            try {
                session.getServer().sendMail(session.getUser().getID(),e.toString());
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
        NewChatController controller = fxmlLoader.<NewChatController>getController();
        controller.setup(session);
        // There's no additional data required by the newly opened form.
        Scene registerScreen = new Scene(root);
        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.
        stage.setScene(registerScreen);
        stage.show();
    }
    @FXML
    private void logout() {
        // Set the next "page" (scene) to display.
        // Note that an incorrect path will result in unexpected NullPointer exceptions!
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Login/Login.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            try {
                session.getServer().sendMail(session.getUser().getID(),e.toString());
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
        LoginController controller = fxmlLoader.<LoginController>getController();
        // There's no additional data required by the newly opened form.
        Scene registerScreen = new Scene(root);
        Stage stage;
        stage = (Stage) txt_username.getScene().getWindow(); // Weird backwards logic trick to get the current scene window.
        stage.setScene(registerScreen);
        stage.show();
    }
}
