package com.td.player.util;

import com.td.player.elements.Music;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class MusicTimer {
    private static long totalTime;
    private static ArrayList<Long> flags = new ArrayList<>();
    private static int n = 0;

    public static void setFlag(boolean turningNewSong, Music previousMusic) {
        if (turningNewSong) {
            long musicTime = (long) previousMusic.getMediaPlayer().getMedia().getDuration().toMillis();
            calculateTotalTime();
            if (totalTime > 0) {
                setLevel(musicTime, previousMusic);
            }
            resetFlags();
        }
        flags.add(System.currentTimeMillis());
        incrementN();
    }

    private static void incrementN() {
        if (n + 1 != 40) {
            n++;
        } else {
            n = 0;
            flags.clear();
        }
    }

    private static void calculateTotalTime() {
        flags.add(System.currentTimeMillis());
        if (flags.size() % 2 != 0) {
            flags.remove(flags.size() - 1);
        }
        for (int i = flags.size() - 2; i >= 0; i-=2) {
            totalTime = totalTime + flags.get(i + 1) - flags.get(i);
        }
    }

    private static void setLevel(long musicTime, Music music) {
        int percent = (int) ((int) (totalTime * 100) / musicTime);
        if (percent < 21) {
            music.downgradeLevel();
        } else if (percent > 79) {
            music.upgradeLevel();
        }
    }

    private static void resetFlags() {
        flags.clear();
        n = 0;
        totalTime = 0;
    }
}
