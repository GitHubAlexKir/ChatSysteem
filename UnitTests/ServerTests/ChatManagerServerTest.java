package ServerTests;

import ChatServer.ChatManager;
import Interfaces.IChat;
import Interfaces.IChatRepo;
import Interfaces.IChatServerManager;
import Interfaces.IMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.Date;

public class ChatManagerServerTest {
    private IChatServerManager server;


    @Before
    public void setup()
    {
        try {
            this.server = new ChatManager();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void register()
    {
        String testName = "UnitTest" + new Date().toString();
        try {
            Assert.assertEquals(true,server.register(testName,testName));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void registerNameExists()
    {
        try {
            Assert.assertEquals(false,server.register("c","c"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void login()
    {
        try {
            Assert.assertEquals(5,server.login("a","a").getID());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginWrongPassword()
    {
        try {
            Assert.assertEquals(null,server.login("a","b"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNewChats()
    {
        try {
            Assert.assertEquals(false,server.getNewChats(7).isEmpty());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void message() {
        String test = new Date().toString();
        boolean passed = true;
        try {
            this.server.sendMessage(7, 9, test);
        } catch (RemoteException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Test
    public void createChat()
    {
        boolean passed = false;
        try {
            server.createChat(7,8);
        } catch (RemoteException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        Assert.assertEquals(true,passed);
    }
}
