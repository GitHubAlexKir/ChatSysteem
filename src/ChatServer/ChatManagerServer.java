package ChatServer;

import Interfaces.IChatBotManager;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Scanner;

public class ChatManagerServer {
    // Set port number
    private int portNumber = 1099;
    private String ip = "127.0.0.1";
    // Set binding name for student administration
    private String bindingName = "ChatServer";

    // References to registry and student administration
    private static Registry registry = null;
    private static ChatManager chatManager = null;

    // Constructor
    public ChatManagerServer() throws SQLException, IOException, ClassNotFoundException {

        System.setProperty("java.rmi.server.hostname", ip);
        // Print port number for registry
        System.out.println("ip : " + ip);
        System.out.println("Server: Port number " + portNumber);

        // Create ChatServer
        try {
            chatManager = new ChatManager();
        } catch (RemoteException ex) {
            System.out.println("Server: RemoteException: " + ex.getMessage());
            chatManager = null;
        }

        // Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Bind ChatClientController using registry
        try {
            registry.rebind(bindingName, chatManager);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind student administration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, NotBoundException {
        System.out.println("SERVER USING REGISTRY");
        ChatManagerServer server = new ChatManagerServer();
        Scanner scan = new Scanner(System.in);
        System.out.println("enter key and press enter when you have started ChatBotmanagerServer");
        String myLine = scan.nextLine();
        chatManager.setChatBotManager((IChatBotManager)registry.lookup("ChatBotServer"));
        System.out.println("Connected to ChatBotServer");

}


}
