package chatserver;

import domains.Message;
import domains.Request;
import interfaces.*;
import repositories.ChatRepo;
import repositories.MessageRepo;
import repositories.UserRepo;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        sendMail(1,"Server started");
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
                    log(e);
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
    public String askQuestion(Request request) throws RemoteException {
        return chatBotManager.askQuestion(request);
    }

    @Override
    public void renameChat(int chatId, String chatName) throws RemoteException {
        this.chatRepo.renameChat(chatId,chatName);
    }
    private void setupMail()
    {
        FileInputStream in = null;
        Properties prop = new Properties();
        try {

            in = new FileInputStream("config.properties");
            prop.load(in);
            in.close();
        } catch (IOException e) {
            log(e);
        }
        final String username = "chatsysteem@gmail.com";
        final String password = prop.getProperty("gmail");
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
    public void sendMail(int userID, String text) throws RemoteException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("chatsysteem@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO,
                    InternetAddress.parse("chatSysteem@gmail.com"));
            message.setSubject("Program ChatSysteem: Exception/message from userID " + userID);
            message.setText(text);
            Transport.send(message);
            System.out.println("mail send");
        } catch (MessagingException e) {
            log(e);
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

    private void log(Exception e)
    {
        Logger.getGlobal().log(Level.SEVERE,"ChatManager",e);
    }
}
