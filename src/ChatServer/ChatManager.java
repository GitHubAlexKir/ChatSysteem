package ChatServer;

import Domains.Message;
import Interfaces.*;
import Repositories.ChatRepo;
import Repositories.MessageRepo;
import Repositories.UserRepo;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatManager extends UnicastRemoteObject implements IChatServerManager {
    private IUserRepo userRepo;
    private IChatRepo chatRepo;
    private IMessageRepo messageRepo;
    private List<IListener> listeners = new ArrayList<>();
    private Timer messageTimer;
    private boolean timerPause = true;
    private IChatBotManager chatBotManager;
    private Session session;
    public ChatManager() throws RemoteException {
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
        setupMail();
        sendErrorMail(1,"test");
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
    public String askQuestion(String question) throws RemoteException {
        return chatBotManager.askQuestion(question);
    }

    @Override
    public void renameChat(int chatId, String chatName) throws RemoteException {
        this.chatRepo.renameChat(chatId,chatName);
    }
    private void setupMail()
    {
        final String username = "chatsysteem@gmail.com";
        final String password = "Wachtwoord3";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });
    }
    @Override
    public void sendErrorMail(int userID, String exception) throws RemoteException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("chatsysteem@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO,
                    InternetAddress.parse("chatSysteem@gmail.com"));
            message.setSubject("Program ChatSysteem: Exception from userID " + userID);
            message.setText(exception);
            Transport.send(message);
            System.out.println("Error mail send");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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

    public void setChatBotManager(IChatBotManager chatBotManager) {
        this.chatBotManager = chatBotManager;
    }
}
