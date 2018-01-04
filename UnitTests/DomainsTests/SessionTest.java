package DomainsTests;
import ChatServer.ChatManager;
import Domains.Session;
import Domains.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.rmi.RemoteException;

public class SessionTest {
    private Session session;

    @Before
    public void setup()
    {
        try {
            this.session = new Session(new ChatManager());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getServer()
    {
        Assert.assertEquals(false,session.getServer().equals(null));
    }

    @Test
    public void setUser()
    {
        session.setUser(new User(1,"a"));
        Assert.assertEquals("a",session.getUser().getUsername());
    }
}
