package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.game.*;

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
     * The piece id. Should be color_piecename, all lowercase.
     * Default implementation returns color_class.getSimpleName()
     * @return The unique piece id as a string.
     */
    public String getId() {
        return (color.toString() + "_" + getClass().getSimpleName()).toLowerCase();
    }

    /**
     * Get the possible moves for a board.
     * Features a default implementation for Pieces
     * which use getPossibleMoves().
     * Can be overridden if more control is needed.
     * Check needs to be checked using getTrueValidMoves().
     * @param board The board to get the moves for.
     * @param coordinates The coordinates of the piece.
     */
    public List<Move> getValidMoves(Board board, Coordinates coordinates) {
        List<Move> moves = getMoves(getPossibleMoves(), coordinates);
        moves = checkLineOfSight(moves, board);
        moves = checkOwnColor(moves, board);
        return moves;
    }

    /**
     * Same as getValidMoves() but also checks checks.
     * Uses getValidMoves() internally.
     */
    public List<Move> getTrueValidMoves(Board board, Coordinates coordinates) {
        List<Move> moves = getValidMoves(board, coordinates);
        moves = checkCheck(moves, board);
        return moves;
    }

    /**
     * Checks if a move is valid.
     * @param possibleMoves The possible moves to check.
     * @param coordinates The coordinates of the piece.
     * @return All moves that are in bounds of the board.
     */
    protected List<Move> getMoves(List<RelativeCoordinates> possibleMoves, Coordinates coordinates) {
        List<Move> moves = new ArrayList<>();

        for (RelativeCoordinates c : possibleMoves) {
            try {
                Move move = c.toMove(coordinates);
                moves.add(move);
            } catch (IllegalArgumentException ex) {
                // move out of bounds of board, do not add
            }
        }

        return moves;
    }

    protected List<Move> checkLineOfSight(List<Move> moves, Board board) {
        List<Move> passedMoves = new ArrayList<>();

        nextMove: for (Move move : moves) {
            int fileDelta = move.getToFile() - move.getFromFile();
            int rankDelta = move.getToRank() - move.getFromRank();
            int fileStep = (int)Math.signum(fileDelta);
            int rankStep = (int)Math.signum(rankDelta);

            for (int f=move.getFromFile()+fileStep, r=move.getFromRank()+rankStep; f != move.getToFile() || r != move.getToRank(); f+=fileStep, r+=rankStep) {
                if (board.get(f, r) != null) {
                    continue nextMove;
                }
            }

            passedMoves.add(move);
        }

        return passedMoves;
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
            Piece attackedPiece = board.get(move.getToFile(), move.getToRank());
            if (attackedPiece == null || attackedPiece.getColor() != getColor()) {
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
        List<Move> passedMoves = new ArrayList<>();

        Board copy = new Board();
        board.copyTo(copy);

        for (Move move : moves) {
            copy.move(move);
            if (!copy.isInCheck(getColor())) {
                passedMoves.add(move);
            }
            copy.undo();
        }

        return passedMoves;
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
