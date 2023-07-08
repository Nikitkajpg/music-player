package com.td.player;

import javafx.scene.media.Media;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class MusicController {
    private ArrayList<Music> musicArrayList = new ArrayList<>();

    public void add(String mediaPath, File file) {
        //todo: переделать создание id
        // сделать создание level
        musicArrayList.add(new Music(musicArrayList.size(), new Media(mediaPath), file, 0));
    }

    public ArrayList<Music> getMusicArray() {
        return musicArrayList;
    }

    public void deleteAll(String path) {
        musicArrayList.removeIf(music -> music.getFile().getAbsolutePath().contains(path));
    }
}
