package com.td.player;

import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
    private boolean empty;
    private ArrayList<String> pathArray;

    private File dataDir, directoryListFile;
    private DirectoryChooser directoryChooser;

    public FileManager() {
        String programPath = System.getProperty("user.dir");
        dataDir = new File(programPath + "\\data");
        directoryListFile = new File(programPath + "\\data\\directories.cfg");
        pathArray = new ArrayList<>();
        directoryChooser = new DirectoryChooser();

        createDirectories();
    }

    private void createDirectories() {
        try {
            if (dataDir.mkdir()) {
                System.out.println("dir not exists");
            } else {
                System.out.println("dir exists");
            }

            if (directoryListFile.createNewFile()) {
                System.out.println("file not exists");
            } else {
                System.out.println("file exists");
                checkIfFileIsEmpty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfFileIsEmpty() {
        try {
            FileReader fileReader = new FileReader(directoryListFile);
            Scanner scanner = new Scanner(fileReader);
            if (!scanner.hasNextLine()) {
                System.out.println("file is empty");
                empty = true;
            } else {
                System.out.println("file is not empty");
                empty = false;

                while (scanner.hasNextLine()) {
//                    pathArray.add(scanner.nextLine());
                    updateArray(scanner.nextLine());
                }
//                System.out.println(pathArray);
            }
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    }
}
