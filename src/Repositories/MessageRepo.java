package Repositories;

import Domains.Message;
import Interfaces.IConnection;
import Interfaces.IMessage;
import Interfaces.IMessageRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepo implements IMessageRepo {


    @Override
    public void sendMessage(int userId, int chatId, String content) {
        String query = "INSERT into message(chatid, userid,content) VALUES(?, ?, ?);";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt(1, chatId);
            preparedStmt.setInt(2,userId);
            preparedStmt.setString(3,content);
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<IMessage> getMessages(int chatId, int userId) {
        List<IMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM message WHERE chatid = ?;";
        IConnection connection = new ConnectionManager();
        Connection conn  = connection.getConnection();
        PreparedStatement preparedStmt = null;
        ResultSet rs = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt(1,chatId);
            rs = preparedStmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("userId") == userId)
                {
                    messages.add(new Message(rs.getInt("id"),rs.getString("content"),false));
                }
                else
                {
                    messages.add(new Message(rs.getInt("id"),rs.getString("content"),true));
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return messages;
    }
}
