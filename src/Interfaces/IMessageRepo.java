package Interfaces;

import java.util.List;

public interface IMessageRepo {
    void sendMessage(int userId, int chatId, String content);
    List<IMessage> getMessages(int chatId, int userId);
}
