package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatServerManager extends Remote {
    IUser login(String username, String password) throws RemoteException;
    boolean register(String username, String password) throws RemoteException;
}
