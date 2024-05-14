package com.td.player.util;

import com.td.player.elements.Track;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Util {
    public static String getDirectoryName(String directoryPath) {
        String[] strings = directoryPath.split("\\\\");
        String name = directoryPath;
        if (strings.length > 3) {
            name = strings[0] + "\\" + strings[1] + "\\...\\" + strings[strings.length - 1];
        }
        return name;
    }

    public static void openDirectoryInExplorer(String directoryPath) {
        File directory = new File(directoryPath);
        try {
            Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFile(Track track) {
        File directory = new File(track.getAbsolutePath());
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            if (isWindows) {
                ProcessBuilder pb = new ProcessBuilder("explorer.exe", "/select," + directory.toURI());
                pb.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
