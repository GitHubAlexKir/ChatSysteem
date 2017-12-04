package ChatServer;

import Classes.User;
import Interfaces.IChatServerManager;
import Interfaces.IUser;
import Interfaces.IUserRepo;
import Repositories.UserRepo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServerController extends UnicastRemoteObject implements IChatServerManager {
    private IUserRepo userRepo;

    public ChatServerController() throws RemoteException {
        userRepo = new UserRepo();
    }

    @Override
    public IUser login(String username, String password) {
        return userRepo.login(username,password);
    }
}
