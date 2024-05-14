package com.td.player.elements;

@SuppressWarnings("FieldMayBeFinal")
public class Directory extends ParentElement {
    private String path;

    public Directory(String name, String pathToDirectory) {
        super(name);
        this.path = pathToDirectory;
    }

    public String getPath() {
        return path;
    }
}
