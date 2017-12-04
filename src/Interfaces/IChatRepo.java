package Interfaces;

import java.util.List;

public interface IChatRepo {
    List<IChat> getChats(int userId);
}
