package com.td.player.elements;

@SuppressWarnings("FieldMayBeFinal")
public class Directory extends Element {
    private String path;

    public Directory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
