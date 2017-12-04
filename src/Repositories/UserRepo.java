package Repositories;

import Classes.User;
import Interfaces.IConnection;
import Interfaces.IUser;
import Interfaces.IUserRepo;

import java.sql.*;

public class UserRepo implements IUserRepo {
    private IConnection connection;

    public UserRepo() {
        this.connection = new ConnectionManager();
    }

    @Override
    public IUser login(String username, String password) {
        IUser user = null;
        try {
            String query = "SELECT * FROM user WHERE username = ? AND password= ?;";
            IConnection connection = new ConnectionManager();
            Connection conn = connection.getConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1,username.toLowerCase());
            preparedStmt.setString(2,password);
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id"),rs.getString("username"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
