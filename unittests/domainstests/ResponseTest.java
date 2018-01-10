package domainstests;
import domains.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private Response response;

    @Before
    public void setup()
    {
        this.response = new Response("vraag", "antwoord");
    }

    @Test
    public void getQuestion()
    {
        Assert.assertEquals("vraag",response.getQuestion());
    }

    @Test
    public void getAnwer()
    {
        Assert.assertEquals("antwoord",response.getAnswer());
    }
}
