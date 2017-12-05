package Repositories;

import Domains.User;
import Interfaces.IConnection;
import Interfaces.IUser;
import Interfaces.IUserRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepo implements IUserRepo {


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
    public Boolean checkIfNameExists(String username) {
        boolean exists = false;
        try {

            String query = "SELECT * FROM user WHERE username = ?;";
            IConnection connection = new ConnectionManager();
            Connection conn = connection.getConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1,username.toLowerCase());
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                exists = true;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    @Override
    public boolean register(String username, String password) {
        if (!checkIfNameExists(username))
        {
            try {
                String query = "INSERT into user(username, password) VALUES(?, ?);";
                IConnection connection = new ConnectionManager();
                Connection conn = connection.getConnection();
                PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, username.toLowerCase());
                preparedStmt.setString(2,password);
                preparedStmt.execute();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<IUser> getNewChats(int id) {
        List<IUser> users = new ArrayList<>();
        try {

            String query = "SELECT * FROM user WHERE id != ?;";
            IConnection connection = new ConnectionManager();
            Connection conn = connection.getConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt(1,id);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),rs.getString("username")));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
