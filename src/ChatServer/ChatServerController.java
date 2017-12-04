package ChatServer;

import Classes.User;
import Interfaces.*;
import Repositories.ChatRepo;
import Repositories.UserRepo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChatServerController extends UnicastRemoteObject implements IChatServerManager {
    private IUserRepo userRepo;
    private IChatRepo chatRepo;

    public ChatServerController() throws RemoteException {
        userRepo = new UserRepo();
        chatRepo = new ChatRepo();
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
}
