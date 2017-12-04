package Classes;

import Interfaces.IChat;
import Interfaces.IUser;

import java.io.Serializable;
import java.util.Date;

public class Chat implements IChat, Serializable {
    private String name;
    private int id;
    private Date dateCreated;
    private IUser user;

    public Chat(String name, int id, Date dateCreated, IUser user) {
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
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public IUser getUser() {
        return user;
    }
}
