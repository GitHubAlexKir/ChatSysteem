package Interfaces;

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
    String askQuestion(String question) throws RemoteException;
    void renameChat(int id, String chatName) throws RemoteException;
    void sendErrorMail(int userID, String exception) throws RemoteException;
}
