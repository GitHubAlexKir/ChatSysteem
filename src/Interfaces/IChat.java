package Interfaces;

import java.sql.Timestamp;
import java.util.Date;

public interface IChat {
    String getName();
    void setName(String name);
    int getID();
    Timestamp getDateCreated();
    IUser getUser();
    String getUser_Name();
}
