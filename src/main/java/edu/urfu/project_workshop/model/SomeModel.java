package edu.urfu.project_workshop.model;

public class SomeModel {
    private final long id;
    private final String content;

    public SomeModel(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
