package ChatClient.Chat;

import Domains.Message;
import Interfaces.IChatServerManager;
import Interfaces.IListener;
import Interfaces.IMessage;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class MessagesManager implements IListener, Serializable {
    private List<IMessage> messages = new ArrayList<>();
    private int userId;
    private int chatId;
    private IChatServerManager server;

    public MessagesManager(int userId, int chatId, IChatServerManager server) {
        this.userId = userId;
        this.chatId = chatId;
        this.server = server;
        try {
            server.addListener(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setChatMessages(List<IMessage> messages) throws RemoteException {
        this.messages = messages;
    }

    @Override
    public int getChatId() throws RemoteException {
        return chatId;
    }

    @Override
    public int getUserId() throws RemoteException {
        return userId;
    }

    @Override
    public List<IMessage> getchatMessages() throws RemoteException {
        return messages;
    }
}
