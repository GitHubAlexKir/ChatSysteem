package ChatBotServer;

import Interfaces.IChatBotManager;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatBotManager extends UnicastRemoteObject implements IChatBotManager {
    boolean githubLookUp = false;
    private List<Response> responses = new ArrayList<>();
    public ChatBotManager() throws RemoteException {
        responses.add(new Response("hallo","Hallo!"));
        responses.add(new Response("wat is je naam?","ChatBot!, maar je kan me ook Hans noemen."));
        responses.add(new Response("hans","dat ben ik"));
    }

    @Override
    public String askQuestion(String question)  {
        if (question.toLowerCase().contains("bitcoin"))
        {
            JSONObject obj = getJson("https://coinbin.org/btc");
            JSONObject res = obj.getJSONObject("coin");
            return "De waarde van Bitcoin is $ " + String.valueOf(res.getInt("usd"));
        }
        if (githubLookUp)
        {
            JSONObject obj = getJson("https://api.github.com/users/" + question);
            githubLookUp = false;
            return question + " : Naam: " + obj.getString("name")  + ", aantal openbare repositories: " + obj.getInt("public_repos")+ ", Volgers: " +
                    obj.getInt("followers") + ", volgend: " + obj.getInt("following") +
                    ", Link: " + obj.getString("html_url");
        }
        else if (question.toLowerCase().contains(" github"))
        {
            githubLookUp = true;
            return "Verstuur een github gebruikersnaam in voor informatie erover";
        }
        for (Response response:responses
             ) {
            if (question.toLowerCase().equals(response.getQuestion().toLowerCase()))
            {
                return response.getAnswer();
            }
        }
        return "ERROR: Geen reactie gevonden, voeg een reactie toe via de knop bovenin.";
    }
    private JSONObject getJson(String URL)
    {
        URL url = null;
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // read from the URL
        Scanner scan = null;
        try {
            scan = new Scanner(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        // build a JSON object
        return new JSONObject(str);
    }
}
