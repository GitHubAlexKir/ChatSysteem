package DomainsTests;

import Domains.Chat;
import Domains.User;
import Interfaces.IChat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class ChatTest {
    private IChat chat;

    @Before
    public void setup()
    {
        this.chat = new Chat("naam",34,new Timestamp(1), new User(1,"Alex"));
    }
    @Test
    public void getName()
    {
        Assert.assertEquals("naam",chat.getName());
    }

    @Test
    public void setName()
    {
        chat.setName("nieuw");
        Assert.assertEquals("nieuw",chat.getName());
    }

    @Test
    public void getID()
    {
        Assert.assertEquals(34,chat.getID());
    }

    @Test
    public void getDateCreated()
    {
        Assert.assertEquals("1970-01-01 01:00:00.001",chat.getDateCreated().toString());
    }

    @Test
    public void getUser()
    {
        Assert.assertEquals("Alex",chat.getUser().getUsername());
    }

    @Test
    public void getUsername()
    {
        Assert.assertEquals("Alex",chat.getUser_Name());
    }
}
