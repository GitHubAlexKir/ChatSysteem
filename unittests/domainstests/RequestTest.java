package domainstests;
import domains.Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    private Request request;

    @Before
    public void setup()
    {
        this.request = new Request("hallo?",false);
    }

    @Test
    public void getQuestion()
    {
        Assert.assertEquals("hallo?",request.getQuestion());
    }

    @Test
    public void isGithub()
    {
        Assert.assertEquals(false,request.isGithub());
    }
}
