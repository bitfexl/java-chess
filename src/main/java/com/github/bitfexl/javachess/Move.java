package com.github.bitfexl.javachess;

public class Move {
    private final int fromFile;
    private final int fromRank;
    private final int toFile;
    private final int toRank;

    public Move(int fromFile, int fromRank, int toFile, int toRank) {
        check(fromFile);
        check(fromRank);
        check(toFile);
        check(toRank);
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.toFile = toFile;
        this.toRank = toRank;
    }

    @Override
    public String toString() {
        final String files = "abcdefgh";
        return "" + files.charAt(fromFile - 1) + fromRank + files.charAt(toFile - 1) + toRank;
    }

    private void check(int x) {
        if (x < 1 || x > 8) {
            throw new IllegalArgumentException("Rank and File must be in range 1 to 8.");
        }
    }
}
