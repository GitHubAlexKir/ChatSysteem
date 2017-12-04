package ChatServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Enumeration;

public class ChatServer {
    // Set port number
    private int portNumber = 1099;
    private String ip = "127.0.0.1";
    // Set binding name for student administration
    private String bindingName = "ChatServer";

    // References to registry and student administration
    private Registry registry = null;
    private ChatServerController ChatServer = null;

    // Constructor
    public ChatServer() throws SQLException, IOException, ClassNotFoundException {

        System.setProperty("java.rmi.server.hostname", ip);
        // Print port number for registry
        System.out.println("ip : " + ip);
        System.out.println("Server: Port number " + portNumber);

        // Create ChatServerController
        try {
            ChatServer = new ChatServerController();
        } catch (RemoteException ex) {
            System.out.println("Server: RemoteException: " + ex.getMessage());
            ChatServer = null;
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
            registry.rebind(bindingName, ChatServer);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind student administration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        System.out.println("SERVER USING REGISTRY");
        ChatServer server = new ChatServer();
    }
}
