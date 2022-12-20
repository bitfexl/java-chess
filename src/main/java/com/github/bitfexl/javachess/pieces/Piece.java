package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.Board;
import com.github.bitfexl.javachess.Color;
import com.github.bitfexl.javachess.Move;
import com.github.bitfexl.javachess.RelativeCoordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * A chess piece. Should be immutable/stateless.
 */
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
     * Check needs to be checked (use checkOwnColor, checkCheck).
     * @param board The board to get the moves for.
     * @param file The file (1-8) of the piece.
     * @param rank The rank (1-8) of the piece.
     */
    public List<Move> getValidMoves(Board board, int file, int rank) {
        List<Move> moves = getMoves(getPossibleMoves(), file, rank);
        moves = checkOwnColor(moves, board);
        moves = checkCheck(moves, board);
        return moves;
    }

    /**
     * Checks if a move is valid.
     * @param possibleMoves The possible moves to check.
     * @param file The file (1-8) of the piece.
     * @param rank The rank (1-8) of the piece.
     * @return All moves that are in bounds of the board.
     */
    protected List<Move> getMoves(List<RelativeCoordinates> possibleMoves, int file, int rank) {
        Board.checkInBoundsException(file);
        Board.checkInBoundsException(rank);

        List<Move> moves = new ArrayList<>();

        for (RelativeCoordinates c : possibleMoves) {
            try {
                Move move = c.toMove(file, rank);
                moves.add(move);
            } catch (IllegalArgumentException ex) {
                // move out of bounds of board, do not add
            }
        }

        return moves;
    }

    /**
     * Checks if a move is valid.
     * @param moves The moves to check.
     * @param board The board to check.
     * @return All moves for which the resulting square is not occupied by a piece of same color.
     */
    protected List<Move> checkOwnColor(List<Move> moves, Board board) {
        List<Move> passedMoves = new ArrayList<>();

        for (Move move : moves) {
            if (board.get(move.getToFile(), move.getToRank()).getColor() != getColor()) {
                passedMoves.add(move);
            }
        }

        return passedMoves;
    }

    /**
     * Checks if a move is valid.
     * @param moves The moves to check.
     * @param board The board to check.
     * @return All moves for which the resulting position is not a check.
     */
    protected List<Move> checkCheck(List<Move> moves, Board board) {
        Board copy = new Board();
        board.copyTo(copy);

        // todo: cant detect check without detecting check

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
