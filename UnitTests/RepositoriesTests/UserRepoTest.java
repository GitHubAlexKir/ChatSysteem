package RepositoriesTests;

import Interfaces.IUserRepo;
import Repositories.UserRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class UserRepoTest {
    private IUserRepo userRepo;

    @Before
    public void setup()
    {
        this.userRepo = new UserRepo();
    }

    @Test
    public void register()
    {
        String testName = "UnitTest" + new Date().toString();
        Assert.assertEquals(true,userRepo.register(testName,testName));
    }

    @Test
    public void registerNameExists()
    {
        Assert.assertEquals(false,userRepo.register("c","c"));
    }

    @Test
    public void login()
    {
        Assert.assertEquals(5,userRepo.login("a","a").getID());
    }

    @Test
    public void loginWrongPassword()
    {
        Assert.assertEquals(null,userRepo.login("a","b"));
    }

    @Test
    public void getNewChats()
    {
        Assert.assertEquals(false,userRepo.getNewChats(7).isEmpty());
    }
}
