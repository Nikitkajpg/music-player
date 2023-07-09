package com.td.player;

import javafx.collections.ObservableMap;
import javafx.scene.media.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
public class MusicController {
    private ArrayList<Music> musicArrayList = new ArrayList<>();

    public void add(String mediaPath, File file) {
        //todo: переделать создание id
        // сделать создание level

        Music music = new Music(musicArrayList.size(), new Media(mediaPath), file, 0);
        /*music.getMediaPlayer().setOnReady(() -> {
                ObservableMap<String, Object> metadata = music.getMedia().getMetadata();
                for (Map.Entry<String, Object> item : metadata.entrySet()) {
                    if (item.getKey().equals("artist")) {
                        music.setArtist(item.getValue().toString());
                    } else if (item.getKey().equals("title")) {
                        music.setTitle(item.getValue().toString());
                    }
                }

                if (music.getTitle() == null) {
                    music.setTitle(music.getFile().getName().replace(".mp3", ""));
                }
                if (music.getArtist() == null) {
                    music.setArtist("(No data)");
                }
            });*/
        musicArrayList.add(music);
    }

    public ArrayList<Music> getMusicArray() {
        return musicArrayList;
    }

    public void deleteByPath(String path) {
        musicArrayList.removeIf(music -> music.getFile().getAbsolutePath().contains(path));
    }

    public Music getMusicByName(String name) {
        for (Music music : musicArrayList) {
            if (music.getFile().getName().equals(name)) {
                return music;
            }
        }
        return null;
    }
}
