package com.github.bitfexl.javachess.game;

import java.util.Objects;

/**
 * Coordinates on a check board.
 */
public class Coordinates {
    private final int file;
    private final int rank;

    /**
     * Init new coordinates.
     * @param file The file (1-8).
     * @param rank The rank (1-8).
     * @throws IllegalArgumentException File or rank out of range.
     */
    public Coordinates(int file, int rank) {
        Board.checkInBoundsException(file);
        Board.checkInBoundsException(rank);
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return file == that.file && rank == that.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }
}
