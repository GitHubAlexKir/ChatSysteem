package interfaces;

import java.sql.Timestamp;

public interface IChat {
    String getName();
    void setName(String name);
    int getID();
    Timestamp getDateCreated();
    IUser getUser();
    String getUser_Name();
}
