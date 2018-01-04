package DomainsTests;
import Domains.User;
import Interfaces.IUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private IUser user;

    @Before
    public void setup()
    {
        this.user = new User(1,"a");
    }

    @Test
    public void getID()
    {
        Assert.assertEquals(1,user.getID());
    }

    @Test
    public void getUsername()
    {
        Assert.assertEquals("a",user.getUsername());
    }
}
