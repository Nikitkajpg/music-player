package com.td.player;

import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManager {
    private MusicController musicController;

    private File directoriesFile/*, playlistFile // todo playlist*/;

    private ArrayList<String> pathToDirectoriesArray = new ArrayList<>();

    public FileManager(MusicController musicController) {
        this.musicController = musicController;
        createFiles();
    }

    private void createFiles() {
        File dataDir = new File(System.getProperty("user.dir") + "\\data");
        directoriesFile = new File(System.getProperty("user.dir") + "\\data\\directories.cfg");
//        playlistFile = new File(System.getProperty("user.dir") + "\\data\\playlist.dat"); // todo playlist

        try {
            System.out.println("Data dir created: " + dataDir.mkdir());
            System.out.println("Directories file created: " + directoriesFile.createNewFile());
//            System.out.println("Playlist file created: " + playlistFile.createNewFile()); // todo playlist
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPathToDirectoriesArray() {
        pathToDirectoriesArray = getLineArrayList(directoriesFile);
        return pathToDirectoriesArray;
    }

    private ArrayList<String> getLineArrayList(File file) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));

            while ((line = bufferedReader.readLine()) != null) {
                arrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void fillMusicArray() {
        for (String path : pathToDirectoriesArray) {
            fill(path);
        }
    }

    public void fillMusicArray(String path) {
        fill(path);
    }

    private void fill(String path) {
        File musicDirectory = new File(path);
        File[] listOfMusicFiles = musicDirectory.listFiles(file -> file.getName().endsWith(".mp3"));

        for (File musicFile : Objects.requireNonNull(listOfMusicFiles)) {
            String mediaPath = "file:///" + musicFile.getAbsolutePath().
                    replace("\\", "/").
                    replace(" ", "%20");
            musicController.add(mediaPath, musicFile);
        }
    }

    public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Directory selection");
        File directory = directoryChooser.showDialog(Player.stage);
        String path = directory.getAbsolutePath();
        if (isUnique(path, pathToDirectoriesArray)) {
            pathToDirectoriesArray.add(path);
        } else {
            System.out.println("Path already exists"); // todo output
        }
        fillMusicArray(path);
    }

    private boolean isUnique(String line, List<String> list) {
        boolean unique = true;
        for (String s : list) {
            if (line.equals(s)) {
                unique = false;
                break;
            }
        }
        return unique;
    }

    public void writeAllInf() {
        writeDirs();
    }

    private void writeDirs() {
        try {
            FileWriter fileWriter = new FileWriter(directoriesFile);
            for (String path : pathToDirectoriesArray) {
                fileWriter.write(path + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
