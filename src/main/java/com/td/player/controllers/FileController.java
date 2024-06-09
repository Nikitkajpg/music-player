package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Util;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.ArrayList;
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

    private ArrayList<String> directoriesLines = new ArrayList<>();
    private ArrayList<String> levelsLines = new ArrayList<>();
    private ArrayList<String> playlistsLines = new ArrayList<>();

    public FileController(Controller controller) {
        this.controller = controller;
        initFiles();

        readFile(directoriesLines, directoriesFile);
        readFile(levelsLines, levelsFile);
        readFile(playlistsLines, playlistFile);

        fillDirectories();
        fillPlaylists();
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

    private void readFile(ArrayList<String> lines, File file) {
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillDirectories() {
        for (String directoryPath : directoriesLines) {
            Directory directory = new Directory(controller.getDirectoryManager().getDirectories().size() + 1, Util.getDirectoryName(directoryPath), directoryPath);
            controller.getDirectoryManager().add(directory);
            addTracksToDirectory(directory);
        }
    }

    /**
     * mediaPath переделывается для корректной передачи в {@link javafx.scene.media.Media}
     */
    private void addTracksToDirectory(Directory directory) {
        File[] trackFileList = new File(directory.getPath()).listFiles(file -> file.getName().endsWith("mp3"));

        for (File trackFile : Objects.requireNonNull(trackFileList)) {
            String mediaPath = "file:///" + trackFile.getAbsolutePath().
                    replace("\\", "/").
                    replace(" ", "%20");
            directory.addTrack(new Track(Util.getNextTrackId(controller.getDirectoryManager().getDirectories()), "", "", trackFile, mediaPath, getLevel(trackFile.getAbsolutePath())));
        }
    }

    private int getLevel(String trackPath) {
        for (String line : levelsLines) {
            String[] splitLine = line.split("::");
            if (splitLine[0].equals(trackPath)) {
                return Integer.parseInt(splitLine[1]);
            }
        }
        return 5;
    }

    private void fillPlaylists() {
        controller.getPlaylistManager().createDefaultPlaylist();
        Playlist playlist = null;
        for (String line : playlistsLines) {
            if (line.startsWith("::") && line.endsWith("::")) {
                line = line.replace("::", "");
                playlist = new Playlist(controller.getPlaylistManager().getPlaylists().size() + 1, line);
                controller.getPlaylistManager().add(playlist);
            } else {
                for (Directory directory : controller.getDirectoryManager().getDirectories()) {
                    for (Track track : directory.getTracks()) {
                        if (track.getFileName().equals(line)) {
                            Objects.requireNonNull(playlist).addTrack(track);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Directory selection");
        File directoryFile = directoryChooser.showDialog(Player.stage);
        String directoryPath = directoryFile.getAbsolutePath();
        if (controller.getDirectoryManager().pathIsUnique(directoryPath)) {
            Directory directory = new Directory(controller.getDirectoryManager().getDirectories().size() + 1, Util.getDirectoryName(directoryPath), directoryPath);
            addTracksToDirectory(directory);
            controller.getDirectoryManager().add(directory);
        }
        controller.getPlaylistManager().updateDefaultPlaylist();
        controller.getViewController().updateView();
    }

    public void writeToFile() {
        writeDirectories();
        writePlaylists();
        writeLevels();
    }

    private void writeDirectories() {
        try {
            FileWriter fileWriter = new FileWriter(directoriesFile);
            for (Directory directory : controller.getDirectoryManager().getDirectories()) {
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
            for (Playlist playlist : controller.getPlaylistManager().getPlaylists()) {
                if (!playlist.getName().equals("All tracks")) {
                    fileWriter.write("::" + playlist.getName() + "::" + "\n");
                    for (Track track : playlist.getTracks()) {
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
            for (Directory directory : controller.getDirectoryManager().getDirectories()) {
                for (Track track : directory.getTracks()) {
                    fileWriter.write(track.getFileName() + "::" + track.getLevel() + "\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
