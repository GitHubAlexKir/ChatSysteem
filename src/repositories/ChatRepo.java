package repositories;

import domains.Chat;
import domains.User;
import interfaces.IChat;
import interfaces.IChatRepo;
import interfaces.IConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        ResultSet rs = null;
        try {
            preparedStmt = conn.prepareStatement(getChats, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt (1, userId);
            preparedStmt.setInt (2, userId);
            rs = preparedStmt.executeQuery();
            while (rs.next()) {
                chats.add(new Chat(rs.getString("name"),
                        rs.getInt("ID"),rs.getTimestamp("dateCreated"),
                        new User(rs.getInt("userID"),rs.getString("username"))));
            }

        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatRepo",e);
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                conn.close();
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.SEVERE,"ChatRepo",e);
            }
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
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt2 = null;
        PreparedStatement preparedStmt3 = null;
        ResultSet rs = null;
        try {
            preparedStmt = conn.prepareStatement(queryCreateChat, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.execute();
            rs = preparedStmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            System.out.println(generatedKey);
            preparedStmt2 = conn.prepareStatement(queryJoinChat);
            preparedStmt2.setInt (1, userID);
            preparedStmt2.setInt (2, generatedKey);
            preparedStmt2.execute();
            preparedStmt3 = conn.prepareStatement(queryJoinChat);
            preparedStmt3.setInt (1, newChatUserId);
            preparedStmt3.setInt (2, generatedKey);
            preparedStmt3.execute();
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatRepo",e);
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (preparedStmt2 != null) {
                    preparedStmt2.close();
                }
                if (preparedStmt3 != null) {
                    preparedStmt3.close();
                }
                conn.close();
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.SEVERE,"ChatRepo",e);
            }
        }

    }

    @Override
    public void renameChat(int chatId, String chatName) {
        String queryRenameChat = "update chat set name = ? where id = ?;";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        PreparedStatement preparedStmt = null;
        try {

            preparedStmt = conn.prepareStatement(queryRenameChat);
            preparedStmt.setString (1, chatName);
            preparedStmt.setInt (2, chatId);
            preparedStmt.execute();
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatRepo",e);
        }
        finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                conn.close();
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.SEVERE,"ChatRepo",e);
            }
        }
    }
}
