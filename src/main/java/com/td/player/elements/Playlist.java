package com.td.player.elements;

import com.td.player.managers.MusicManager;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class Playlist {
    private String name;
    private ArrayList<Music> musicArray = new ArrayList<>();

    /**
     * Конструктор для создания нового объекта.
     *
     * @param name название плейлиста
     */
    public Playlist(String name) {
        this.name = name;
    }

    /**
     * Метод добавляет музыку в {@link #musicArray}
     *
     * @param music объект {@link Music}
     */
    public void add(Music music) {
        musicArray.add(music);
    }

    /**
     * Метод добавляет музыку в {@link #musicArray} по названию файла музыки.
     *
     * @param name название файла музыки
     */
    public void add(String name, MusicManager musicManager) {
        for (Music music : musicManager.getMusicArray()) {
            if (music.getFileName().equals(name)) {
                musicArray.add(music);
                break;
            }
        }
    }

    public void delete(Music music) {
        musicArray.remove(music);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Music> getMusicArray() {
        return musicArray;
    }

    /**
     * Метод для получения следующей песни в плейлисте.
     * Если музыка последняя в списке, возвращается первая музыка
     *
     * @param currentMusic текущая музыка
     * @return следующая музыка
     */
    public Music getNext(Music currentMusic) {
        for (int i = 0; i < musicArray.size(); i++) {
            if (currentMusic.equals(musicArray.get(i))) {
                if (i + 1 < musicArray.size()) {
                    return musicArray.get(i + 1);
                }
            }
        }
        return musicArray.get(0);
    }

    /**
     * Метод для получения предыдущей песни в плейлисте.
     * Если музыка первая в списке, возвращается последняя музыка
     *
     * @param currentMusic текущая музыка
     * @return предыдущая музыка
     */
    public Music getPrevious(Music currentMusic) {
        for (int i = 0; i < musicArray.size(); i++) {
            if (currentMusic.equals(musicArray.get(i))) {
                if (i - 1 >= 0) {
                    return musicArray.get(i - 1);
                }
            }
        }
        return musicArray.get(musicArray.size() - 1);
    }
}
