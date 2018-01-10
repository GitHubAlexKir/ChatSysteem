package ChatClient.Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {
    private static String ip = "127.0.0.1";
    private static int port = 1099;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController controller = fxmlLoader.<LoginController>getController();
        controller.setSettings(ip,port);
        primaryStage.setTitle("ChatSysteem");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        if (args.length != 0) {
            if (!args[0].isEmpty()) {
                ip = args[0];
            }
            if (!args[1].isEmpty()) {
                port = Integer.parseInt(args[1]);
            }
        }
        launch(args);
    }
}
