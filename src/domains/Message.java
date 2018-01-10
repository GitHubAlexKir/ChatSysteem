package domains;


import interfaces.IMessage;

import java.io.Serializable;

public class Message implements IMessage , Serializable{

    private int id;
    private String content;
    private boolean receiver;

    public Message(int id, String content, boolean receiver) {
        this.id = id;
        this.content = content;
        this.receiver = receiver;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean getReceiver() {
        return receiver;
    }
}
