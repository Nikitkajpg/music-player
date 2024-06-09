package com.td.player.managers;

import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Track;

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
    public void delete(int id) {
        directories.removeIf(directory -> directory.getId() == id);
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

    public Track getTrackById(int id) {
        for (Directory directory : directories) {
            for (Track track : directory.getTracks()) {
                if (track.getId() == id) {
                    return track;
                }
            }
        }
        return null;
    }

    public boolean noTracks() {
        boolean empty = true;
        for (Directory directory : directories) {
            if (directory.getTracks().size() != 0) {
                empty = false;
                break;
            }
        }
        return empty;
    }
}
