package com.td.player.managers;

import com.td.player.elements.Directory;
import com.td.player.elements.Element;
import com.td.player.util.Util;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class DirectoryManager {
    private ArrayList<Directory> directoryArray = new ArrayList<>();

    public void add(String path) {
        Directory directory = new Directory(Util.setId(directoryArray), path);
        directoryArray.add(directory);
    }

    public void delete(int id) {
        directoryArray.removeIf(directory -> directory.getId() == id);
    }

    public void delete(String path) {
        directoryArray.removeIf(directory -> directory.getPath().equals(path));
    }

    public Directory get(int id) {
        for (Directory directory : directoryArray) {
            if (directory.getId() == id) {
                return directory;
            }
        }
        return null;
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
