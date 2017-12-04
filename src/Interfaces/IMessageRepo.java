package Interfaces;

public interface IMessageRepo {
    void sendMessage(int userId, int chatId, String content);
}
