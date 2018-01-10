package interfaces;

import java.util.List;

public interface IChatRepo {
    List<IChat> getChats(int userId);
    void createChat(int userID, int newChatUserId);
    void renameChat(int chatId, String chatName);
}
