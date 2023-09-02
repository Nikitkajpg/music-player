package com.td.player.elements;

@SuppressWarnings("FieldMayBeFinal")
public class Directory {
    private String path;

    /**
     * Конструктор для создания объекта
     *
     * @param path путь к папке
     */
    public Directory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
