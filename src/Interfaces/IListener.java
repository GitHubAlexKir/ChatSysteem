package Interfaces;

import Domains.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IListener extends Remote {
    void setChatMessages(List<IMessage> messages) throws RemoteException;
    int getChatId() throws RemoteException;
    int getUserId() throws RemoteException;
    List<IMessage> getchatMessages() throws RemoteException;
    void addMessage(Message message) throws RemoteException;
}
