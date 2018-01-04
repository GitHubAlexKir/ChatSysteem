package DomainsTests;
import Domains.Message;
import Interfaces.IMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {
    private IMessage message;

    @Before
    public void setup()
    {
        this.message = new Message(23, "hallo", false);
    }

    @Test
    public void getID()
    {
        Assert.assertEquals(23,message.getId());
    }

    @Test
    public void getContent()
    {
        Assert.assertEquals("hallo",message.getContent());
    }

    @Test
    public void getReceiver()
    {
        Assert.assertEquals(false,message.getReceiver());
    }
}
