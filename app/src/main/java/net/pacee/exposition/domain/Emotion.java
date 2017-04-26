package net.pacee.exposition.domain;

/**
 * Created by student on 25-04-17.
 */

public class Emotion {
    private String parent;
    private String name;
    private int cotation;
    private String description;


    public Emotion() {
    }

    public Emotion(String parent, String name, int cotation, String description) {
        this.parent = parent;
        this.name = name;
        this.cotation = cotation;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Emotion{" +
                "parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                ", cotation=" + cotation +
                ", description='" + description + '\'' +
                '}';
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
