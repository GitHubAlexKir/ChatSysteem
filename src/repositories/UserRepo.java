package repositories;

import domains.User;
import interfaces.IConnection;
import interfaces.IUser;
import interfaces.IUserRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepo implements IUserRepo {


    @Override
    public IUser login(String username, String password) {
        IUser user = null;
        String query = "SELECT * FROM user WHERE username = ? AND password= ?;";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        PreparedStatement preparedStmt = null;
        ResultSet rs = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1,username.toLowerCase());
            preparedStmt.setString(2,password);
            rs = preparedStmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id"),rs.getString("username"));
            }
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
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
                Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
            }
        }
        return user;
    }
    public Boolean checkIfNameExists(String username) {
        boolean exists = false;
        String query = "SELECT * FROM user WHERE username = ?;";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        PreparedStatement preparedStmt = null;
        ResultSet rs = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1,username.toLowerCase());
            rs = preparedStmt.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
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
                Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
            }
        }
        return exists;
    }
    @Override
    public boolean register(String username, String password) {
        if (!checkIfNameExists(username))
        {
            String query = "INSERT into user(username, password) VALUES(?, ?);";
            IConnection connection = new ConnectionManager();
            Connection conn = connection.getConnection();
            PreparedStatement preparedStmt = null;
            try {
                preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, username.toLowerCase());
                preparedStmt.setString(2,password);
                preparedStmt.execute();
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
                return false;
            }finally {
                try {
                    if (preparedStmt != null) {
                        preparedStmt.close();
                    }
                    conn.close();
                } catch (SQLException e) {
                    Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
                }
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
        String query = "Select * from user u WHERE u.ID NOT IN (\n" +
                "Select u.ID from chat c\n" +
                "join user_chat uc on uc.ChatID=c.ID\n" +
                "join user u on u.ID=uc.UserID\n" +
                "Where c.ID in \n" +
                "(\n" +
                "Select c.ID from chat c\n" +
                "join user_chat uc on uc.ChatID=c.ID\n" +
                "join user u on u.ID=uc.UserID\n" +
                "where u.ID = ?\n" +
                ")\n" +
                "and u.ID != ?\n" +
                ")\n" +
                "and u.ID != ?";
        IConnection connection = new ConnectionManager();
        Connection conn = connection.getConnection();
        PreparedStatement preparedStmt = null;
        ResultSet rs = null;
        try {
            preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt(1,id);
            preparedStmt.setInt(2,id);
            preparedStmt.setInt(3,id);
            rs = preparedStmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),rs.getString("username")));
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
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
                Logger.getGlobal().log(Level.SEVERE,"UserRepo",e);
            }
        }
        return users;
    }
}
