package Repositories;

import Interfaces.IConnection;
import Interfaces.IMessageRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageRepo implements IMessageRepo {

    public MessageRepo() {
    }

    @Override
    public void sendMessage(int userId, int chatId, String content) {
        try {
            String query = "INSERT into message(chatid, userid,content) VALUES(?, ?, ?);";
            IConnection connection = new ConnectionManager();
            Connection conn = connection.getConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt(1, chatId);
            preparedStmt.setInt(2,userId);
            preparedStmt.setString(3,content);
            preparedStmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
