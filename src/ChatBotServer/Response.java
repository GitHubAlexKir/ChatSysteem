package ChatBotServer;

import java.io.Serializable;

public class Response implements Serializable {
    private String question;
    private String answer;

    public Response(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
