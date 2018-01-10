package chatbotserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBotManagerServer {
    private static int portNumber = 1099;
    private static String ip = "127.0.0.1";
    // Set binding name for student administration
    private  String bindingName = "chatbotserver";

    // References to registry and student administration
    private Registry registry = null;
    private ChatBotManager chatBotManager = null;

    // Constructor
    public ChatBotManagerServer() throws IOException, ClassNotFoundException {

        System.setProperty("java.rmi.server.hostname", ip);
        // Print port number for registry
        System.out.println("ip : " + ip);
        System.out.println("Server: Port number " + portNumber);

        // Create chatserver
        try {
            chatBotManager = new ChatBotManager();
        } catch (RemoteException ex) {
            System.out.println("Server: RemoteException: " + ex.getMessage());
            Logger.getGlobal().log(Level.SEVERE,"ChatBotManagerServer",ex);
        }

        // Create registry at port number
        try {
            registry = locateRegistry();
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            Logger.getGlobal().log(Level.SEVERE,"ChatBotManagerServer",ex);
            registry = null;
        }

        // Bind ChatClientController using registry
        try {
            if (registry != null) {
                registry.rebind(bindingName, chatBotManager);
            }
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind student administration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            Logger.getGlobal().log(Level.SEVERE,"ChatBotManagerServer",ex);
        }
    }
    private Registry locateRegistry() throws IOException, ClassNotFoundException {
        try
        {
            return LocateRegistry.getRegistry("127.0.0.1", 1099);
        }
        catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            Logger.getGlobal().log(Level.SEVERE,"ChatBotManagerServer",ex);
            return null;
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("SERVER USING REGISTRY");
        if (args.length != 0) {
            if (!args[0].isEmpty()) {
                ip = args[0];
            }
            if (!args[1].isEmpty()) {
                portNumber = Integer.parseInt(args[1]);
            }
        }
        ChatBotManagerServer server = new ChatBotManagerServer();
    }
}
