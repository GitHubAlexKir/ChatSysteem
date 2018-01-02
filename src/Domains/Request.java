package Domains;

import java.io.Serializable;

public class Request implements Serializable{
    private String question;
    private boolean github;

    public Request(String question, boolean github) {
        this.question = question;
        this.github = github;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isGithub() {
        return github;
    }
}
