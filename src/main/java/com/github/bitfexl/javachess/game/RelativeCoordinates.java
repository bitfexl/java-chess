package com.github.bitfexl.javachess.game;

/**
 * Immutable relative coordinates to calculate
 * the possible moves for a given piece.
 */
public class RelativeCoordinates {
    private final int xDelta;
    private final int yDelta;

    /**
     * Relative coordinates for possible moves.
     * @param xDelta The delta on the rank.
     * @param yDelta The delta on the file.
     */
    public RelativeCoordinates(int xDelta, int yDelta) {
        this.xDelta = xDelta;
        this.yDelta = yDelta;
    }

    public Move toMove(Coordinates from) {
        return new Move(from, new Coordinates(from.getFile() + xDelta, from.getRank() + yDelta));
    }

    public Move toMove(int fromFile, int fromRank) {
        return new Move(fromFile, fromRank, fromFile + xDelta, fromRank + yDelta);
    }

    public int xDelta() {
        return xDelta;
    }

    public int yDelta() {
        return yDelta;
    }
}
