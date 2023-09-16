package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.Objects;

/**
 * Класс для управления файлами: создание, чтение и запись
 */
@SuppressWarnings("FieldMayBeFinal")
public class FileController {
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private File directoriesFile;
    private File playlistFile;
    private File levelsFile;

    /**
     * Конструктор создает файлы и заполняет все необходимые массивы
     * <p>Создается плейлист по умолчанию, содержащий все песни
     */
    public FileController(Controller controller) {
        directoryManager = controller.getDirectoryManager();
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        createFiles();
        fillDirectoryArray();
        fillMusicArray();
        fillMusicWithLevels();
        playlistManager.createDefaultPlaylist(musicManager);
        fillPlaylistArray();
    }

    private void createFiles() {
        File dataDir = new File(System.getProperty("user.dir") + "\\data");
        directoriesFile = new File(System.getProperty("user.dir") + "\\data\\directories.ini");
        playlistFile = new File(System.getProperty("user.dir") + "\\data\\playlist.ini");
        levelsFile = new File(System.getProperty("user.dir") + "\\data\\levels.ini");

        try {
            System.out.println("Data dir created: " + dataDir.mkdir());
            System.out.println("Directories file created: " + directoriesFile.createNewFile());
            System.out.println("Playlist file created: " + playlistFile.createNewFile());
            System.out.println("Levels file created: " + levelsFile.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillDirectoryArray() {
        try {
            String path;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(directoriesFile)));

            while ((path = bufferedReader.readLine()) != null) {
                directoryManager.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод выполняется при запуске программы
     */
    private void fillMusicArray() {
        for (Directory directory : directoryManager.getDirectoryArray()) {
            fill(directory.getPath());
        }
    }

    /**
     * Метод выполняется при выборе папки с музыкой
     *
     * @param path путь к папке с музыкой
     */
    private void fillMusicArray(String path) {
        fill(path);
    }

    /**
     * Метод заполняет массив песен.
     * <p>Поле fileList получает список музыкальных файлов в папке.
     * Пробел в пути заменяется "%20" для корректной передачи в {@link javafx.scene.media.Media}
     *
     * @param path путь к папке с музыкой
     */
    private void fill(String path) {
        File[] fileList = new File(path).listFiles(file -> file.getName().endsWith("mp3"));

        for (File musicFile : Objects.requireNonNull(fileList)) {
            String mediaPath = "file:///" + musicFile.getAbsolutePath().
                    replace("\\", "/").
                    replace(" ", "%20");
            musicManager.add("", "", musicFile, mediaPath);
        }
    }

    private void fillMusicWithLevels() {
        try {
            String level;
            int count = 0;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(levelsFile)));

            while ((level = bufferedReader.readLine()) != null) {
                musicManager.getMusicArray().get(count).setLevel(Integer.parseInt(level));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillPlaylistArray() {
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(playlistFile)));
            String name = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("::") && line.endsWith("::")) {
                    line = line.replace("::", "");
                    playlistManager.add(line);
                    name = line;
                } else {
                    for (Music music : musicManager.getMusicArray()) {
                        if (music.getFileName().equals(line)) {
                            playlistManager.addMusicToPlaylist(name, music);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод добавляет выбранную папку в массив папок {@link DirectoryManager#add(String)}
     */
    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Directory selection");
        File directory = directoryChooser.showDialog(Player.stage);
        String path = directory.getAbsolutePath();
        if (directoryManager.pathIsUnique(path)) {
            directoryManager.add(path);
            fillMusicArray(path);
        }
    }

    /**
     * Метод записывает все данные в {@link #directoriesFile}, {@link #playlistFile}, {@link #levelsFile}
     */
    public void writeAllInf() {
        writeDir();
        writePlaylist();
        writeLevels();
    }

    private void writeDir() {
        try {
            FileWriter fileWriter = new FileWriter(directoriesFile);
            for (Directory directory : directoryManager.getDirectoryArray()) {
                fileWriter.write(directory.getPath() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePlaylist() {
        try {
            FileWriter fileWriter = new FileWriter(playlistFile);
            for (Playlist playlist : playlistManager.getPlaylistArray()) {
                if (!playlist.getName().equals("All music")) {
                    fileWriter.write("::" + playlist.getName() + "::" + "\n");
                    for (Music music : playlist.getMusicArray()) {
                        fileWriter.write(music.getFileName() + "\n");
                    }
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLevels() {
        try {
            FileWriter fileWriter = new FileWriter(levelsFile);
            for (Music music : musicManager.getMusicArray()) {
                fileWriter.write(music.getLevel() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
