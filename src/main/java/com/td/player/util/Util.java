package com.td.player.util;

import com.td.player.controllers.Controller;
import com.td.player.controllers.view.TrackView;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Util {
    public static TrackView currentTrackView;

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

    public static String getTime(int seconds) {
        String time;
        if (seconds / 3600 < 1) {
            time = String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);
        } else {
            time = String.format("%2d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        }
        return time;
    }

    public static int getNextTrackId(ArrayList<Directory> directories) {
        int trackAmount = 0;
        for (Directory directory : directories) {
            trackAmount += directory.getTracks().size();
        }
        return trackAmount + 1;
    }

    public static String getSingleText(String single) {
        String[] strings = single.split("\n");
        if (strings[0].length() > 30) {
            strings[0] = strings[0].substring(0, 30) + "...";
        }
        if (strings[1].length() > 30) {
            strings[1] = strings[1].substring(0, 30) + "...";
        }
        return strings[0] + "\n" + strings[1];
    }

    public static String getNumberOfTracks(String id, Controller controller) {
        for (Playlist playlist : controller.getPlaylistManager().getPlaylists()) {
            if (Integer.parseInt(id) == playlist.getId()) {
                return ": " + playlist.getTracks().size() + " tracks";
            }
        }
        return ": " + -1 + " tracks";
    }

    public static String getNumberOfTracks(ParentElement parentElement) {
        return ": " + parentElement.getTracks().size() + " tracks";
    }
}
