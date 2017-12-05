package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatBotManager extends Remote{
    String askQuestion(String question) throws RemoteException;
}
