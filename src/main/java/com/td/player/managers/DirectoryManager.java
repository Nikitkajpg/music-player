package com.td.player.managers;

import com.td.player.elements.Directory;

import java.util.ArrayList;

/**
 * Класс для управления списком папок. Конструктор отсутствует
 */
@SuppressWarnings("FieldMayBeFinal")
public class DirectoryManager {
    private ArrayList<Directory> directoryArray = new ArrayList<>();

    public void add(String path) {
        Directory directory = new Directory(path);
        directoryArray.add(directory);
    }

    public void delete(Directory directory) {
        directoryArray.remove(directory);
    }

    public ArrayList<Directory> getDirectoryArray() {
        return directoryArray;
    }

    public boolean pathIsUnique(String path) {
        boolean unique = true;
        for (Directory directory : directoryArray) {
            if (directory.getPath().equals(path)) {
                unique = false;
                break;
            }
        }
        return unique;
    }
}
