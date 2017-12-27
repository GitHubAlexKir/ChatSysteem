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
            // build a URL
            String s = "http://maps.google.com/maps/api/geocode/json?" +
                    "sensor=false&address=";
            String addr = "...";
            try {
                s += URLEncoder.encode(addr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            URL url = null;
            try {
                url = new URL(s);
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
            JSONObject obj = new JSONObject(str);
            // get the first result
            JSONObject res = obj.getJSONArray("results").getJSONObject(0);
            return res.getString("formatted_address");
        }
        System.out.println(question.toLowerCase());
        for (Response response:responses
             ) {
            System.out.println(response.getQuestion().toLowerCase());
            if (question.toLowerCase().equals(response.getQuestion().toLowerCase()))
            {
                System.out.println("test");
                return response.getAnswer();
            }
        }
        return "ERROR: Geen reactie gevonden, voeg een reactie toe via de knop bovenin.";
    }
}
