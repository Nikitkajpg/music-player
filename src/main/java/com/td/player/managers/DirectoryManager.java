package com.td.player.managers;

import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;

import java.util.ArrayList;

/**
 * Класс для управления списком папок
 */
@SuppressWarnings("FieldMayBeFinal")
public class DirectoryManager implements ParentElementManager {
    private ArrayList<Directory> directories = new ArrayList<>();

    @Override
    public void add(ParentElement parentElement) {
        directories.add((Directory) parentElement);
    }

    @Override
    public void delete(ParentElement parentElement) {
        directories.remove((Directory) parentElement);
    }

    public ArrayList<Directory> getDirectories() {
        return directories;
    }

    public boolean pathIsUnique(String path) {
        boolean unique = true;
        for (Directory directory : directories) {
            if (directory.getPath().equals(path)) {
                unique = false;
                break;
            }
        }
        return unique;
    }
}
