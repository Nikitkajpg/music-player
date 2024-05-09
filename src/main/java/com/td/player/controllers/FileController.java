package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.managers.DirectoryManager;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.Objects;

/**
 * Класс для управления файлами: создание, чтение и запись
 */
@SuppressWarnings("FieldMayBeFinal")
public class FileController {
    private Controller controller;

    private File directoriesFile;
    private File playlistFile;
    private File levelsFile;

    /**
     * Конструктор создает файлы и заполняет все необходимые массивы
     * <p>Создается плейлист по умолчанию, содержащий все песни
     */
    public FileController(Controller controller) {
        this.controller = controller;
        initFiles();

        fillDirectoryArray();
        fillTrackArray();
        fillTrackWithLevels();
        fillPlaylistArray();
    }

    private void initFiles() {
        directoriesFile = initFile("\\data\\directories.ini");
        playlistFile = initFile("\\data\\playlist.ini");
        levelsFile = initFile("\\data\\levels.ini");

        createFiles();
    }

    private void createFiles() {
        try {
            System.out.println("Data dir created: " + initFile("\\data").mkdir());
            System.out.println("Directories file created: " + directoriesFile.createNewFile());
            System.out.println("Playlist file created: " + playlistFile.createNewFile());
            System.out.println("Levels file created: " + levelsFile.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File initFile(String pathEnd) {
        return new File(System.getProperty("user.dir") + pathEnd);
    }

    private void fillDirectoryArray() {
        try {
            String path;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(directoriesFile)));

            while ((path = bufferedReader.readLine()) != null) {
                controller.getDirectoryManager().add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод выполняется при запуске программы
     */
    private void fillTrackArray() {
        for (Directory directory : controller.getDirectoryManager().getDirectoryArray()) {
            fillCommon(directory.getPath());
        }
    }

    /**
     * Метод выполняется при выборе папки с музыкой
     */
    private void fillTrackArray(String pathToTrackDirectory) {
        fillCommon(pathToTrackDirectory);
    }

    /**
     * Метод заполняет массив песен.
     * <p>Поле fileList получает список музыкальных файлов в папке.
     * Пробел в пути заменяется "%20" для корректной передачи в {@link javafx.scene.media.Media}
     */
    private void fillCommon(String pathToTrackDirectory) {
        File[] fileList = new File(pathToTrackDirectory).listFiles(file -> file.getName().endsWith("mp3"));

        for (File trackFile : Objects.requireNonNull(fileList)) {
            String mediaPath = "file:///" + trackFile.getAbsolutePath().
                    replace("\\", "/").
                    replace(" ", "%20");
            controller.getTrackManager().add("", "", trackFile, mediaPath);
        }
    }

    private void fillTrackWithLevels() {
        String level;
        int count = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(levelsFile)));
            while ((level = bufferedReader.readLine()) != null) {
                controller.getTrackManager().getTrackArray().get(count).setLevel(Integer.parseInt(level));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.getPlaylistManager().createDefaultPlaylist();
    }

    private void fillPlaylistArray() {
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(playlistFile)));
            String playlistName = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("::") && line.endsWith("::")) {
                    line = line.replace("::", "");
                    controller.getPlaylistManager().add(line);
                    playlistName = line;
                } else {
                    for (Track track : controller.getTrackManager().getTrackArray()) {
                        if (track.getFileName().equals(line)) {
                            controller.getPlaylistManager().addTrackToPlaylist(playlistName, track);
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
        if (controller.getDirectoryManager().pathIsUnique(path)) {
            controller.getDirectoryManager().add(path);
            fillTrackArray(path);
        }
        controller.getPlaylistManager().updateDefaultPlaylist();
        controller.getViewController().showLists();
    }

    /**
     * Метод записывает все данные в {@link #directoriesFile}, {@link #playlistFile}, {@link #levelsFile}
     */
    public void writeAllInf() {
        writeDirectories();
        writePlaylists();
        writeLevels();
    }

    private void writeDirectories() {
        try {
            FileWriter fileWriter = new FileWriter(directoriesFile);
            for (Directory directory : controller.getDirectoryManager().getDirectoryArray()) {
                fileWriter.write(directory.getPath() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePlaylists() {
        try {
            FileWriter fileWriter = new FileWriter(playlistFile);
            for (Playlist playlist : controller.getPlaylistManager().getPlaylistArray()) {
                if (!playlist.getName().equals("All tracks")) {
                    fileWriter.write("::" + playlist.getName() + "::" + "\n");
                    for (Track track : playlist.getTrackArray()) {
                        fileWriter.write(track.getFileName() + "\n");
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
            for (Track track : controller.getTrackManager().getTrackArray()) {
                fileWriter.write(track.getLevel() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
