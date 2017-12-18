package ChatClient.NewChat;

import ChatClient.Home.HomeController;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class NewChatController {
    @FXML
    private Text txt_username;
    @FXML
    private TableView<IUser> tv_users;
    @FXML
    private TableColumn tc_user;

    private IUser user;
    private IChatServerManager server;
    private List<IUser> users;
    private IUser selectedUser;
    public NewChatController() {
    }

    public void setup(IUser user, IChatServerManager server)
    {
        this.user = user;
        this.server = server;
        getUsers();
    }

    private void getUsers()
    {
        tv_users.getItems().clear();
        tc_user.setCellValueFactory(new PropertyValueFactory<IUser, String>("username"));
        try {
            users = server.getNewChats(user.getID());
            if (!users.isEmpty()) {
                for (IUser i : users) {
                    tv_users.getItems().add(i);
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
    private void createChat()
    {
        if (selectedUser != null)
        {
            try {
                server.createChat(this.user.getID(),selectedUser.getID());
                toHomeScreen();
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
    private void selectedUser()
    {
            if (tv_users.getSelectionModel().getSelectedItem() != null)
            {
                selectedUser = tv_users.getSelectionModel().getSelectedItem();
            }
            else
            {
                selectedUser = null;
            }
    }
    @FXML
    private void cancelNewChat()
    {
        toHomeScreen();
    }

    private void toHomeScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Home/Home.fxml"));
        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            try {
                server.sendErrorMail(user.getID(),e.toString());
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

}
