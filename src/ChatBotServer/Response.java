package ChatBotServer;

import java.io.Serializable;

public class Response implements Serializable {
    private int id;
    private String question;
    private String answer;

    public Response(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
