package ChatServer;

import Domains.Message;
import Interfaces.*;
import Repositories.ChatRepo;
import Repositories.MessageRepo;
import Repositories.UserRepo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatServerController extends UnicastRemoteObject implements IChatServerManager {
    private IUserRepo userRepo;
    private IChatRepo chatRepo;
    private IMessageRepo messageRepo;
    private List<IListener> listeners = new ArrayList<>();
    private Timer messageTimer;
    private boolean timerPause = true;
    public ChatServerController() throws RemoteException {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        userRepo = new UserRepo();
        chatRepo = new ChatRepo();
        messageRepo = new MessageRepo();
        messageTimer = new Timer();
        messageTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                    UpdateListeners();

            }
        }, 1000 , 50);
    }

    private void UpdateListeners() {
        if (!timerPause) {
            if (listeners.isEmpty()) {
                timerPause = true;
            }
            for (IListener l : listeners) {
                try {
                    if (l.getchatMessages().isEmpty()) {
                        l.setChatMessages(messageRepo.getMessages(l.getChatId(), l.getUserId()));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IUser login(String username, String password) {
        return userRepo.login(username,password);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        return userRepo.register(username, password);
    }

    @Override
    public List<IChat> getChats(int userID) throws RemoteException {
        return chatRepo.getChats(userID);
    }

    @Override
    public void createChat(int userID, int newChatUserId) throws RemoteException {
        chatRepo.createChat(userID,newChatUserId);
    }

    @Override
    public List<IUser> getNewChats(int userId) throws RemoteException {
        return userRepo.getNewChats(userId);
    }

    @Override
    public void sendMessage(int userId, int chatId, String content) throws RemoteException {
        if (!timerPause)
        {
            for (IListener l:listeners
                 ) {
                if (l.getChatId() == chatId)
                {
                    if (l.getUserId() == userId)
                    {
                        l.addMessage(new Message(0,content,false));
                    }
                    else
                    {
                        l.addMessage(new Message(0,content,true));
                    }
                }
            }
        }
        messageRepo.sendMessage(userId,chatId,content);
    }

    @Override
    public void addListener(IListener listener) {
        if (timerPause) {
            timerPause = false;
    }
        listeners.add(listener);
    }

    @Override
    public void removeListener(IListener listener) {
        listeners.remove(listener);
    }
}
