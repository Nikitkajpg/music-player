package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.elements.Directory;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class FileController {
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private File directoriesFile;

    public FileController(DirectoryManager directoryManager, MusicManager musicManager) {
        this.directoryManager = directoryManager;
        this.musicManager = musicManager;
        createFiles();
        fillDirectoryArray();
        fillMusicArray();
    }

    private void createFiles() {
        File dataDir = new File(System.getProperty("user.dir") + "\\data");
        directoriesFile = new File(System.getProperty("user.dir") + "\\data\\directories.ini");
        File playlistFile = new File(System.getProperty("user.dir") + "\\data\\playlist.ini");

        try {
            System.out.println("Data dir created: " + dataDir.mkdir());
            System.out.println("Directories file created: " + directoriesFile.createNewFile());
            System.out.println("Playlist file created: " + playlistFile.createNewFile());
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

    private void fillMusicArray() { // выполняется при запуске программы
        for (Directory directory : directoryManager.getDirectoryArray()) {
            fill(directory.getPath());
        }
    }

    private void fillMusicArray(String path) { // выполняется при выборе папки с музыкой
        fill(path);
    }

    private void fill(String path) {
        File[] fileList = new File(path).listFiles(file -> file.getName().endsWith("mp3"));

        for (File musicFile : Objects.requireNonNull(fileList)) {
            String mediaPath = "file:///" + musicFile.getAbsolutePath().
                    replace("\\", "/").
                    replace(" ", "%20");
            musicManager.add("", "", musicFile, mediaPath);
        }
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Directory selection");
        File directory = directoryChooser.showDialog(Player.stage);
        String path = directory.getAbsolutePath();
        if (directoryManager.pathIsUnique(path)) {
            directoryManager.add(path);
        } else {
            System.out.println("Path already exists"); // todo output
        }
        fillMusicArray(path);
    }

    public void writeAllInf() {
        writeDirs();
    }

    private void writeDirs() {
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
}
