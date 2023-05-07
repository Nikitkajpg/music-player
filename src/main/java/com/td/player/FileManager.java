package com.td.player;

import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
    private boolean empty;
    private ArrayList<String> pathArray, playlistArrayList;

    private File dataDir, directoryListFile, playlistFile;
    private DirectoryChooser directoryChooser;

    private ArrayList<Music> musicArrayList;

    public FileManager() {
        String programPath = System.getProperty("user.dir");
        dataDir = new File(programPath + "\\data");
        directoryListFile = new File(programPath + "\\data\\directories.cfg");
        playlistFile = new File(programPath + "\\data\\playlist.dat");
        pathArray = new ArrayList<>();
        directoryChooser = new DirectoryChooser();
        musicArrayList = new ArrayList<>();
        playlistArrayList = new ArrayList<>();

        createDirectories();
    }

    // при запуске программы создаются все необходимые папки и файлы
    private void createDirectories() {
        try {
            if (dataDir.mkdir()) {
                System.out.println("dir not exists");
            } else {
                System.out.println("dir exists");
            }

            if (directoryListFile.createNewFile()) {
                System.out.println("directory file not exists");
            } else {
                System.out.println("directory file exists");
                checkIfDirectoryListFileIsEmpty();
            }

            if (playlistFile.createNewFile()) {
                System.out.println("playlist file not exist");
            } else {
                System.out.println("playlist file exists");
                checkIfPlaylistFileIsEmpty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfPlaylistFileIsEmpty() {
        try {
            FileReader fileReader = new FileReader(playlistFile);
            Scanner scanner = new Scanner(fileReader);
            if (!scanner.hasNextLine()) {
                System.out.println("playlist file is empty");
                empty = true;

                try {
                    FileWriter fileWriter = new FileWriter(playlistFile);
                    for (int i = 0; i < musicArrayList.size(); i++) {
                        fileWriter.write(i + "::" + musicArrayList.get(i).getFileName() + "::All music" + "\n");
                        //todo добавление в начало файла строки с низваниями плейлистов
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    empty = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("playlist file is not empty");
                empty = false;

                // todo прочитать файл и вывести плейлисты на экран

                while (scanner.hasNextLine()) {
                    playlistArrayList.add(scanner.nextLine());
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfDirectoryListFileIsEmpty() {
        try {
            FileReader fileReader = new FileReader(directoryListFile);
            Scanner scanner = new Scanner(fileReader);
            if (!scanner.hasNextLine()) {
                System.out.println("directory file is empty");
                empty = true;
            } else {
                System.out.println("directory file is not empty");
                empty = false;

                while (scanner.hasNextLine()) {
                    updateArray(scanner.nextLine()); //todo исправить массив создание песен
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectDirectory() {
        directoryChooser.setTitle("Select directory");
        if (!empty) {
            directoryChooser.setInitialDirectory(new File(pathArray.get(0)));
        }
        File directoryFromChooser = directoryChooser.showDialog(Player.stage);
        if (directoryFromChooser != null) {
            writeFile(directoryFromChooser.getAbsolutePath());
        } else {
            System.out.println("chosen file is null");
        }
//        System.out.println(pathArray);
    }

    private void writeFile(String path) {
        try {
            FileWriter fileWriter = new FileWriter(directoryListFile, true);
            if (pathIsUnique(path)) {
                fileWriter.write(path + "\n");
                updateArray(path);
//                pathArray.add(path);
            } else {
                System.out.println("path in file already exists");
            }
            fileWriter.flush();
            fileWriter.close();
            empty = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean pathIsUnique(String path) {
        boolean unique = true;
        for (String s : pathArray) {
            if (path.equals(s)) {
                unique = false;
                break;
            }
        }
        return unique;
    }

    private void updateArray(String path) {
        pathArray.add(path);
        System.out.println("final: " + pathArray);

        musicArrayList.clear();
        for (String pathToDirectory : pathArray) {
            File directoryWithMusic = new File(pathToDirectory);
            File[] listOfMusicFiles = directoryWithMusic.listFiles(file -> file.getName().endsWith(".mp3"));
            for (File listOfMusicFile : listOfMusicFiles) {
                String mediaPath = "file:///" + listOfMusicFile.getAbsolutePath().
                        replace("\\", "/").
                        replace(" ", "%20");
                musicArrayList.add(new Music(new Media(mediaPath), 0, listOfMusicFile.getName()));
            }
        }
    }

    public ArrayList<Music> getMusicList() {
        return musicArrayList;
    }

    public ArrayList<String> getPathArray() {
        return pathArray;
    }

    public ArrayList<String> getPlaylistArrayList() {
        return playlistArrayList;
    }
}
