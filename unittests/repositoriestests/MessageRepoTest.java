package repositoriestests;
import interfaces.IMessage;
import interfaces.IMessageRepo;
import repositories.MessageRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class MessageRepoTest {
    private IMessageRepo messageRepo;

    @Before
    public void setup()
    {
        this.messageRepo = new MessageRepo();
    }

    @Test
    public void message() {
        String test = new Date().toString();
        this.messageRepo.sendMessage(7, 9, test);
        boolean passed = false;
        for (IMessage m : messageRepo.getMessages(9, 7)
                ) {
            if (m.getContent().equals(test))
            {
                passed = true;
            }
        }
        Assert.assertEquals(true,passed);
    }
}
