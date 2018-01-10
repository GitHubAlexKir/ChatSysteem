package repositories;

import interfaces.IConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager implements IConnection {
    private java.sql.Connection connection;

    public ConnectionManager()  {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream("config.properties");
            prop.load(in);
            in.close();
            String driver = prop.getProperty("jdbc.driver");
            String connectionURL = prop.getProperty("jdbc.url");
            String username = prop.getProperty("jdbc.username");
            String password = prop.getProperty("jdbc.password");
            Class.forName(driver);
            this.connection = DriverManager.getConnection(connectionURL, username, password);
        }
         catch (SQLException | ClassNotFoundException | IOException e) {
             Logger.getGlobal().log(Level.SEVERE,"ConnectionManager",e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}

