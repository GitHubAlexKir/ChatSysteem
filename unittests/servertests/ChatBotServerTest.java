package servertests;

import chatbotserver.ChatBotManager;
import domains.Request;
import interfaces.IChatBotManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBotServerTest {
    private IChatBotManager chatBotManager;

    @Before
    public void setup()
    {
        try {
            this.chatBotManager = new ChatBotManager();
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatBotServerTest",e);
        }
    }

    @Test
    public void askQuestionHello()
    {
        try {
            Assert.assertEquals("Hallo!",chatBotManager.askQuestion(new Request("hallo",false)));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatBotServerTest",e);
        }
    }
    @Test
    public void askQuestionGitHub()
    {
        try {
            chatBotManager.askQuestion(new Request("zoek op github",false));
            Assert.assertEquals(true,chatBotManager.askQuestion(new Request("larsjanssen6",true)).contains("Lars Janssen"));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatBotServerTest",e);
        }
    }

    @Test
    public void askQuestionBitcoin()
    {
        try {
            Assert.assertEquals(true,chatBotManager.askQuestion(new Request("bitcoin",false)).contains("$"));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatBotServerTest",e);
        }
    }
    @Test
    public void askQuestionUnknown()
    {
        try {
            Assert.assertEquals("ERROR: Geen reactie gevonden.",chatBotManager.askQuestion(new Request("qwerty",false)));
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE,"ChatBotServerTest",e);
        }
    }
}
