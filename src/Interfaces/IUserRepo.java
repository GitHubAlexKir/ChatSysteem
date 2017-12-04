package Interfaces;

public interface IUserRepo {
    IUser login(String username, String password);
    boolean register(String username, String password);
}
