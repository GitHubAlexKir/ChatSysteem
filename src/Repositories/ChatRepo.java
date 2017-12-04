package Repositories;

import Classes.Chat;
import Classes.User;
import Interfaces.IChat;
import Interfaces.IChatRepo;
import Interfaces.IConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatRepo implements IChatRepo {
    private IConnection connection;

    public ChatRepo() {
        this.connection = new ConnectionManager();
    }

    @Override
    public List<IChat> getChats(int userId) {
        List<IChat> chats = new ArrayList<>();
        String getChats = "Select c.ID,c.name,c.dateCreated,u.ID as userID, u.username from chat c\n" +
                "join user_chat uc on uc.ChatID=c.ID\n" +
                "join user u on u.ID=uc.UserID\n" +
                "Where c.ID = (Select c.ID from chat c\n" +
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
                        rs.getInt("ID"),rs.getDate("dateCreated"),
                        new User(rs.getInt("userID"),rs.getString("username"))));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chats;
    }
}
