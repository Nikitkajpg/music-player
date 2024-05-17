package com.td.player.util;

import com.td.player.elements.Track;

import java.util.ArrayList;

/**
 * Класс является частью "системы предпочтений", отвечает за подсчет времени прослушивания песни.
 * <p>{@link #flags} - массив временных отметок
 * <p>{@link #countFlags} - подсчет количества временных отметок
 */
@SuppressWarnings("FieldMayBeFinal")
public class TrackTimer {
    private static long totalTime;
    private static ArrayList<Long> flags = new ArrayList<>();
    private static int countFlags = 0;

    /**
     * Метод для установки временной отметки.
     * <p>Если включена новая песня, рассчитывается время слушания предыдущей песни и установка приоритета
     *
     * @param turningNewSong флаг включения новой песни
     */
    public static void setFlag(boolean turningNewSong, Track previousTrack) {
        if (turningNewSong) {
            long trackTime = (long) previousTrack.getMediaPlayer().getMedia().getDuration().toMillis();
            calculateTotalTime();
            if (totalTime > 0) {
                setLevel(trackTime, previousTrack);
            }
            resetFlags();
        }
        flags.add(System.currentTimeMillis());
        incrementN();
    }

    private static void incrementN() {
        if (countFlags + 1 != 40) {
            countFlags++;
        } else {
            countFlags = 0;
            flags.clear();
        }
    }

    /**
     * Метод для подсчета общего времени прослушивания музыки.
     * <p>Подсчет осуществляется следующим образом: от конечной метки отнимается начальная,
     * и от этого времени отнимается время нахождения песни на паузе
     * <p>Если после включения паузы была включена другая песня, количество временных меток становится нечетным.
     * В таком случае последняя временная метка удаляется из-за ненадобности
     */
    private static void calculateTotalTime() {
        flags.add(System.currentTimeMillis());
        if (flags.size() % 2 != 0) {
            flags.remove(flags.size() - 1);
        }
        for (int i = flags.size() - 2; i >= 0; i -= 2) {
            totalTime = totalTime + flags.get(i + 1) - flags.get(i);
        }
    }

    /**
     * Метод для определения уровня (приоритета) песни.
     * <p>Если песня была прослушана меньше 20%, уровень уменьшается.
     * <p>Если песня была прослушана больше 80%, уровень увеличивается.
     * <p>В других случаях уровень песни не изменяется.
     */
    private static void setLevel(long totalTrackTime, Track previousTrack) {
        int percent = (int) ((int) (totalTime * 100) / totalTrackTime);
        if (percent < 21) {
            previousTrack.downgradeLevel();
        } else if (percent > 79) {
            previousTrack.upgradeLevel();
        }
    }

    private static void resetFlags() {
        flags.clear();
        countFlags = 0;
        totalTime = 0;
    }
}
