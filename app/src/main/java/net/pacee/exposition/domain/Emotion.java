package net.pacee.exposition.domain;

/**
 * Created by student on 25-04-17.
 */

public class Emotion {
    private String name;
    private int cotation;

    public Emotion(String name, int cotation) {
        this.name = name;
        this.cotation = cotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCotation() {
        return cotation;
    }

    public void setCotation(int cotation) {
        this.cotation = cotation;
    }
}
