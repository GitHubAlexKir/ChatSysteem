package Interfaces;

import java.util.List;

public interface IUserRepo {
    IUser login(String username, String password);
    boolean register(String username, String password);
    List<IUser> getNewChats(int id);
}
