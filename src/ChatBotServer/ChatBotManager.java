package ChatBotServer;

import Interfaces.IChatBotManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatBotManager extends UnicastRemoteObject implements IChatBotManager {
    private List<Response> responses = new ArrayList<>();
    public ChatBotManager() throws RemoteException {
        responses.add(new Response(1,"hallo","Hallo!"));
        responses.add(new Response(2,"wat is je naam?","ChatBot!, maar je kan me ook Hans noemen."));
        responses.add(new Response(3,"hans","dat ben ik"));
    }

    @Override
    public String askQuestion(String question) throws RemoteException {
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
