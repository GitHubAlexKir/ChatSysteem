package chatbotserver;

import domains.Request;
import domains.Response;
import interfaces.IChatBotManager;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBotManager extends UnicastRemoteObject implements IChatBotManager {

    private List<Response> responses = new ArrayList<>();
    public ChatBotManager() throws RemoteException {
        responses.add(new Response("hallo","Hallo!"));
        responses.add(new Response("wat is je naam?","chatbot!, maar je kan me ook Hans noemen."));
        responses.add(new Response("hans","dat ben ik"));
    }

    @Override
    public String askQuestion(Request request)  {
        if (request.getQuestion().toLowerCase().contains("bitcoin"))
        {
            JSONObject obj = getJson("https://coinbin.org/btc");
            JSONObject res = obj.getJSONObject("coin");
            return "De waarde van Bitcoin is $ " + String.valueOf(res.getInt("usd"));
        }
        if (request.isGithub())
        {
            JSONObject obj = getJson("https://api.github.com/users/" + request.getQuestion());
            return request.getQuestion() + " : Naam: " + obj.getString("name")  + ", aantal openbare repositories: " + obj.getInt("public_repos")+ ", Volgers: " +
                    obj.getInt("followers") + ", volgend: " + obj.getInt("following");
        }
        else if (request.getQuestion().toLowerCase().contains(" github"))
        {
            return "Verstuur een github gebruikersnaam in voor informatie erover";
        }
        for (Response response:responses
             ) {
            if (request.getQuestion().equalsIgnoreCase(response.getQuestion()))
            {
                return response.getAnswer();
            }
        }
        return "ERROR: Geen reactie gevonden.";
    }
    private JSONObject getJson(String URL)
    {
            URL url = null;
            try {
                url = new URL(URL);
            } catch (MalformedURLException e) {
                Logger.getGlobal().log(Level.SEVERE,"ChatBotManager",e);
            }
            Scanner scan = null;
            if (url != null) {
                try {
                    scan = new Scanner(url.openStream());
                } catch (IOException e) {
                    Logger.getGlobal().log(Level.SEVERE,"ChatBotManager",e);
                }
            }
            StringBuilder str = new StringBuilder();
            if (scan != null)
            {
                while (scan.hasNext())
                    str.append(scan.nextLine());
                scan.close();
            }
            return new JSONObject(str.toString());

    }
}
