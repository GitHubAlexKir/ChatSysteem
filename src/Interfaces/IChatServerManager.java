package Interfaces;

import Domains.Request;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChatServerManager extends IRemotePublisher , Remote {
    IUser login(String username, String password) throws RemoteException;
    boolean register(String username, String password) throws RemoteException;
    List<IChat> getChats(int userID) throws RemoteException;
    void createChat(int userID, int newChatUserId) throws RemoteException;
    List<IUser> getNewChats(int userId) throws RemoteException;
    void sendMessage(int userId, int chatId, String content) throws RemoteException;
    String askQuestion(Request request) throws RemoteException;
    void renameChat(int id, String chatName) throws RemoteException;
    void sendMail(int userID, String message) throws RemoteException;
}
