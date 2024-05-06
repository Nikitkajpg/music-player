package com.td.player.elements;

@SuppressWarnings("FieldMayBeFinal")
public class Directory {
    private String path;

    public Directory(String pathToDirectory) {
        this.path = pathToDirectory;
    }

    public String getPath() {
        return path;
    }
}
