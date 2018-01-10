package domains;

import interfaces.IChat;
import interfaces.IUser;

import java.io.Serializable;
import java.sql.Timestamp;

public class Chat implements IChat, Serializable {
    private String name;
    private int id;
    private Timestamp dateCreated;
    private IUser user;

    public Chat(String name, int id, Timestamp dateCreated, IUser user) {
        this.name = name;
        this.id = id;
        this.dateCreated = dateCreated;
        this.user = user;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public String getUser_Name()
    {
        return this.user.getUsername();
    }
}
