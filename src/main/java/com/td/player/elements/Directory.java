package com.td.player.elements;

public class Directory extends Element {
    private int id;
    private String path;

    public Directory(int id, String path) {
        this.id = id;
        this.path = path;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
