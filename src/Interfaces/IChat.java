package Interfaces;

import java.util.Date;

public interface IChat {
    String getName();
    void setName(String name);
    int getID();
    Date getDateCreated();
    IUser getUser();
}
