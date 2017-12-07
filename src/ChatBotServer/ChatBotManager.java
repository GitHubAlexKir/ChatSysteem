package ChatBotServer;

import Interfaces.IChatBotManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatBotManager extends UnicastRemoteObject implements IChatBotManager {
    public ChatBotManager() throws RemoteException {
    }

    @Override
    public String askQuestion(String question) throws RemoteException {
        return "testing567";
    }
}
