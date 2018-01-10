package domains;

import interfaces.IUser;

import java.io.Serializable;

public class User implements IUser , Serializable {
    private int ID;
    private String username;

    public User(int ID, String username) {
        this.ID = ID;
        this.username = username;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }
}
