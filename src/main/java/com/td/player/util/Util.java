package com.td.player.util;

import com.td.player.elements.Element;
import com.td.player.elements.Music;
import com.td.player.managers.MusicManager;

import java.io.File;
import java.util.ArrayList;

public class Util {
    public static <T extends Element> int setId(ArrayList<T> elementArray) {
        int id = 0;
        if (elementArray.size() != 0) {
            id = elementArray.get(elementArray.size() - 1).getId() + 1;
        }
        return id;
    }

    public static boolean isUnique(File musicFile, MusicManager musicManager) {
        boolean unique = true;
        for (Music music : musicManager.getMusicArray()) {
            if (musicFile.getName().equals(music.getFileName())) {
                unique = false;
            }
        }
        return unique;
    }
}
