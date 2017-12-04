package ChatServer;

import Interfaces.*;
import Repositories.ChatRepo;
import Repositories.MessageRepo;
import Repositories.UserRepo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChatServerController extends UnicastRemoteObject implements IChatServerManager {
    private IUserRepo userRepo;
    private IChatRepo chatRepo;
    private IMessageRepo messageRepo;
    public ChatServerController() throws RemoteException {
        userRepo = new UserRepo();
        chatRepo = new ChatRepo();
        messageRepo = new MessageRepo();
    }

    @Override
    public IUser login(String username, String password) {
        return userRepo.login(username,password);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        return userRepo.register(username, password);
    }

    @Override
    public List<IChat> getChats(int userID) throws RemoteException {
        return chatRepo.getChats(userID);
    }

    @Override
    public void createChat(int userID, int newChatUserId) throws RemoteException {
        chatRepo.createChat(userID,newChatUserId);
    }

    @Override
    public List<IUser> getNewChats(int userId) throws RemoteException {
        return userRepo.getNewChats(userId);
    }

    @Override
    public void sendMessage(int userId, int chatId, String content) throws RemoteException {
        messageRepo.sendMessage(userId,chatId,content);
    }
}
