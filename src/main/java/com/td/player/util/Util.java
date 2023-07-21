package com.td.player.util;

import com.td.player.elements.Music;
import com.td.player.managers.MusicManager;

import java.io.File;

public class Util {
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
