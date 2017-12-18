package Repositories;

import Domains.Chat;
import Domains.User;
import Interfaces.IChat;
import Interfaces.IChatRepo;
import Interfaces.IConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatRepo implements IChatRepo {

    @Override
    public List<IChat> getChats(int userId) {
        List<IChat> chats = new ArrayList<>();
        String getChats = "Select c.ID,c.name,c.dateCreated,u.ID as userID, u.username from chat c\n" +
                "join user_chat uc on uc.ChatID=c.ID\n" +
                "join user u on u.ID=uc.UserID\n" +
                "Where c.ID in (Select c.ID from chat c\n" +
                "join user_chat uc on uc.ChatID=c.ID\n" +
                "join user u on u.ID=uc.UserID\n" +
                "where u.ID = ?) and u.ID != ?";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(getChats, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt (1, userId);
            preparedStmt.setInt (2, userId);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                chats.add(new Chat(rs.getString("name"),
                        rs.getInt("ID"),rs.getTimestamp("dateCreated"),
                        new User(rs.getInt("userID"),rs.getString("username"))));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chats;
    }

    @Override
    public void createChat(int userID, int newChatUserId) {
        String queryCreateChat = " insert into chat (name) value ('new chat')";
        String queryJoinChat = " insert into user_chat (userID,chatID)"
                + " values (?, ?)";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(queryCreateChat, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.execute();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            System.out.println(generatedKey);
            PreparedStatement preparedStmt2 = conn.prepareStatement(queryJoinChat);
            preparedStmt2.setInt (1, userID);
            preparedStmt2.setInt (2, generatedKey);
            preparedStmt2.execute();
            PreparedStatement preparedStmt3 = conn.prepareStatement(queryJoinChat);
            preparedStmt3.setInt (1, newChatUserId);
            preparedStmt3.setInt (2, generatedKey);
            preparedStmt3.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void renameChat(int chatId, String chatName) {
        String queryRenameChat = "update chat set name = ? where id = ?;";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        try {

            PreparedStatement preparedStmt = conn.prepareStatement(queryRenameChat);
            preparedStmt.setString (1, chatName);
            preparedStmt.setInt (2, chatId);
            preparedStmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
