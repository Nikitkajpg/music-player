package com.td.player.elements;

@SuppressWarnings("FieldMayBeFinal")
public class Directory extends ParentElement {
    private String path;

    public Directory(int id, String name, String pathToDirectory) {
        super(id, name);
        this.path = pathToDirectory;
    }

    public String getPath() {
        return path;
    }
}
