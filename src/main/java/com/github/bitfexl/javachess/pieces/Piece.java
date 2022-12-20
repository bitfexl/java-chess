package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.Board;
import com.github.bitfexl.javachess.Color;
import com.github.bitfexl.javachess.Move;
import com.github.bitfexl.javachess.RelativeCoordinates;

import java.util.List;

public abstract class Piece {
    private final Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Get the possible moves for a board.
     * Features a default implementation for Pieces
     * which use getPossibleMoves().
     * Can be overridden if more control is needed.
     * Check needs to be checked (use checkCheck).
     * @param board The board to get the moves for.
     */
    public void getValidMoves(Board board) {
        // todo
        getPossibleMoves();
    }

    /**
     * Checks if a move is valid.
     * @param moves The moves to check.
     * @return All moves for which the resulting position is not a check.
     */
    protected List<Move> checkCheck(List<Move> moves) {
        // todo
        return moves;
    }

    /**
     * A list of possible moves for the piece.
     * Only use if moves are equal for black and white
     * and no other rules apply.
     * Override getValidMoves if more control is needed
     * (this method remains unused in that case).
     * @return A list of relative moves (in all directions).
     */
    protected abstract List<RelativeCoordinates> getPossibleMoves();
}
