package interfaces;

import domains.Request;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatBotManager extends Remote{
    String askQuestion(Request request) throws RemoteException;
}
