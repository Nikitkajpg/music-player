package com.td.player;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class FileManager {
    private VBox musicListVBox, fileListVBox;

    private ArrayList<String> pathArray = new ArrayList<>();
    private ArrayList<String> playlistArrayList = new ArrayList<>();
    private ArrayList<Music> musicArrayList = new ArrayList<>();

    private File dataDir, directoriesFile, playlistFile;
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    private String directoriesFileName = "directories.cfg";
    private String playlistFileName = "playlist.dat";

    public FileManager(VBox musicListVBox, VBox fileListVBox) {
        this.fileListVBox = fileListVBox;
        this.musicListVBox = musicListVBox;
        String programPath = System.getProperty("user.dir");
        dataDir = new File(programPath + "\\data");
        directoriesFile = new File(programPath + "\\data\\" + directoriesFileName);
        playlistFile = new File(programPath + "\\data\\" + playlistFileName);

        createDirectories();
    }

    private void createDirectories() {
        try {
            if (dataDir.mkdir()) {
                System.out.println("dir not exists");
            } else {
                System.out.println("dir exists");
            }

            createFile(directoriesFile);
            createFile(playlistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile(File file) throws IOException {
        if (file.createNewFile()) {
            System.out.println(file.getName() + " file not exists");
        } else {
            System.out.println(file.getName() + " file exists");
            if (file.length() == 0) {
                System.out.println(file.getName() + " is empty");
                if (file.getName().equals(playlistFileName)) {
                    writeFavorite();
                }
            } else {
                System.out.println(file.getName() + " file is not empty");
                String line;
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file)));

                while ((line = bufferedReader.readLine()) != null) {
                    if (file.getName().equals(playlistFileName)) {
                        playlistArrayList.add(line);
                    } else if (file.getName().equals(directoriesFileName)) {
                        updateArray(line);
                    }
                }
            }
        }
    }

    private void writeFavorite() {
        try {
            FileWriter fileWriter = new FileWriter(playlistFile);
            fileWriter.write("Favorite\n");
            for (int i = 0; i < musicArrayList.size(); i++) {
                fileWriter.write(i + "::" + musicArrayList.get(i).getFileName() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectDirectory() {
        directoryChooser.setTitle("Select directory");
        if (directoriesFile.length() != 0) {
            directoryChooser.setInitialDirectory(new File(pathArray.get(0)));
        }
        File directoryFromChooser = directoryChooser.showDialog(Player.stage);
        if (directoryFromChooser != null) {
            writeFile(directoryFromChooser.getAbsolutePath());
        } else {
            System.out.println("chosen file is null");
        }
    }

    private void writeFile(String path) {
        try {
            FileWriter fileWriter = new FileWriter(directoriesFile, true);
            if (pathIsUnique(path)) {
                fileWriter.write(path + "\n");
                fileListVBox.getChildren().add(new Label(path));
                updateArray(path);
            } else {
                System.out.println("path in file already exists");
            }
            fileWriter.flush();
            fileWriter.close();
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
        File directoryWithMusic = new File(path);
        File[] listOfMusicFiles = directoryWithMusic.listFiles(file -> file.getName().endsWith(".mp3"));
        for (File musicFile : Objects.requireNonNull(listOfMusicFiles)) {
            String mediaPath = "file:///" + musicFile.getAbsolutePath().
                    replace("\\", "/").
                    replace(" ", "%20");
            musicArrayList.add(new Music(new Media(mediaPath), 0, musicFile.getName()));
            musicListVBox.getChildren().add(new Label(musicFile.getName().replace(".mp3", "")));
        }
    }

    public ArrayList<String> getPathArray() {
        return pathArray;
    }

    public ArrayList<String> getPlaylistArrayList() {
        return playlistArrayList;
    }
}
