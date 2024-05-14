package com.td.player.util;

public class Util {
    public static String getDirectoryName(String directoryPath) {
        String[] strings = directoryPath.split("\\\\");
        String name = directoryPath;
        if (strings.length > 3) {
            name = strings[0] + "\\" + strings[1] + "\\...\\" + strings[strings.length - 1];
        }
        return name;
    }
}
