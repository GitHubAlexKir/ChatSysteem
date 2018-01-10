package domainstests;
import chatserver.ChatManager;
import domains.Session;
import domains.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionTest {
    private Session session;

    @Before
    public void setup()
    {
        try {
            this.session = new Session(new ChatManager());
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"SessionTest",e);
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
