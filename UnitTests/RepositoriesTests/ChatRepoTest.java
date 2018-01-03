package RepositoriesTests;

import Domains.Chat;
import Interfaces.IChat;
import Interfaces.IChatRepo;
import Repositories.ChatRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

public class ChatRepoTest {
    private IChatRepo chatRepo;
    @Before
    public void setup()
    {
        this.chatRepo = new ChatRepo();
    }

    @Test
    public void getChats()
    {
        Assert.assertEquals(false,chatRepo.getChats(7).isEmpty());
    }

    @Test
    public void createChat()
    {
        boolean passed = false;
        chatRepo.createChat(7,8);
        for (IChat c:chatRepo.getChats(7)
             ) {
            if (c.getUser().getID() == 8)
            {
                passed = true;
            }
        }
        Assert.assertEquals(true,passed);
    }
    @Test
    public void renameChat()
    {
        String name = new Date().toString();
        boolean passed = false;
        chatRepo.renameChat(8,name);
        for (IChat c:chatRepo.getChats(7)
                ) {
            if (c.getID() == 8)
            {
                if (name.equals(c.getName()))
                {
                    passed = true;
                }
            }
        }
        Assert.assertEquals(true,passed);
    }
}
