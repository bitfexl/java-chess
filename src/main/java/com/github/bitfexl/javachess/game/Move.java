package com.github.bitfexl.javachess.game;

/**
 * A immutable move on the chess board.
 */
public class Move {
    private final int fromFile;
    private final int fromRank;
    private final int toFile;
    private final int toRank;

    public Move(int fromFile, int fromRank, int toFile, int toRank) {
        Board.checkInBoundsException(fromFile);
        Board.checkInBoundsException(fromRank);
        Board.checkInBoundsException(toFile);
        Board.checkInBoundsException(toRank);
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.toFile = toFile;
        this.toRank = toRank;
    }

    public int getFromFile() {
        return fromFile;
    }

    public int getFromRank() {
        return fromRank;
    }

    public int getToFile() {
        return toFile;
    }

    public int getToRank() {
        return toRank;
    }

    @Override
    public String toString() {
        final String files = "abcdefgh";
        return "" + files.charAt(fromFile - 1) + fromRank + files.charAt(toFile - 1) + toRank;
    }
}
