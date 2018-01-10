package servertests;

import chatserver.ChatManager;
import interfaces.IChat;
import interfaces.IChatServerManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatManagerServerTest {
    private IChatServerManager server;


    @Before
    public void setup()
    {
        try {
            this.server = new ChatManager();
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }

    @Test
    public void register()
    {
        String testName = "UnitTest" + new Date().toString();
        try {
            Assert.assertEquals(true,server.register(testName,testName));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }

    @Test
    public void registerNameExists()
    {
        try {
            Assert.assertEquals(false,server.register("c","c"));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }

    @Test
    public void login()
    {
        try {
            Assert.assertEquals(5,server.login("a","a").getID());
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }

    @Test
    public void loginWrongPassword()
    {
        try {
            Assert.assertEquals(null,server.login("a","b"));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }

    @Test
    public void getNewChats()
    {
        try {
            Assert.assertEquals(false,server.getNewChats(7).isEmpty());
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }
    @Test
    public void message() {
        String test = new Date().toString();
        boolean passed = true;
        try {
            this.server.sendMessage(7, 9, test);
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
            passed = false;
        }
        Assert.assertEquals(true,passed);
    }

    @Test
    public void getChats()
    {
        try {
            Assert.assertEquals(false,server.getChats(7).isEmpty());
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
    }

    @Test
    public void createChat()
    {
        boolean passed = false;
        try {
            server.createChat(7,8);
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
        try {
            for (IChat c:server.getChats(7)
                    ) {
                if (c.getUser().getID() == 8)
                {
                    passed = true;
                }
            }
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
        Assert.assertEquals(true,passed);
    }

    @Test
    public void renameChat()
    {
        String name = new Date().toString();
        boolean passed = false;
        try {
            server.renameChat(8,name);
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
        try {
            for (IChat c:server.getChats(7)
                    ) {
                if (c.getID() == 8)
                {
                    if (name.equals(c.getName()))
                    {
                        passed = true;
                    }
                }
            }
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatServerManagerTest",e);
        }
        Assert.assertEquals(true,passed);
    }
}
