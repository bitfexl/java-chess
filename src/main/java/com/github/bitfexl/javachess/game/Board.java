package com.github.bitfexl.javachess.game;

import com.github.bitfexl.javachess.pieces.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Board {
    /**
     * Checks if a number is between 1 and 8.
     * @param x The number/file/rank to check.
     * @return true: in bounds, false: out of bounds;
     */
    public static boolean checkInBounds(int x) {
        return x >= 1 && x <= 8;
    }

    /**
     * Checks if a number is between 1 and 8.
     * @param x The number/file/rank to check.
     * @throws IllegalArgumentException if number is not in bounds.
     */
    public static void checkInBoundsException(int x) {
        if (!checkInBounds(x)) {
            throw new IllegalArgumentException("Rank and File must be in range 1 to 8.");
        }
    }

    private final Map<Coordinates, Piece> board = new HashMap<>();

    private Stack<Move> moveStack = new Stack<>();

    // moveStack size after move and captured piece, if any
    private Map<Integer, Piece> capturedPieces = new HashMap<>();

    public Board() {
        reset();
    }

    public Board(boolean empty) {
        if (!empty) {
            reset();
        }
    }

    /**
     * Checks if a player currently is in check.
     * @param color The player to check.
     * @return true: in check or checkmate, false: not in check;
     */
    public boolean isInCheck(Color color) {
        for (Coordinates coordinates : board.keySet()) {
            Piece piece = get(coordinates);
            if (piece.getColor() == color) {
                continue;
            }

            for (Move move : piece.getValidMoves(this, coordinates)) {
                Piece king = get(move.getToFile(), move.getToRank());
                if (king instanceof King && king.getColor() == color) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Play a move. Does not get played
     * if the piece at the starting pos in null.
     * Does not check for check or check mate.
     * @param move The move to play.
     */
    public void move(Move move) {
        Piece piece = get(move.getFromFile(), move.getFromRank());
        if (piece == null) {
            return;
        }

        Piece captured = set(move.getToFile(), move.getToRank(), piece);
        set(move.getFromFile(), move.getFromRank(), null);

        moveStack.push(move);
        capturedPieces.put(moveStack.size(), captured);

        // todo: special moves (promotion, castle)
    }

    /**
     * Undo the last move.
     * @return The move which has been undone or null -> initial position reached.
     */
    public Move undo() {
        if (moveStack.isEmpty()) {
            return null;
        }

        Piece capturedPiece = capturedPieces.get(moveStack.size());
        Move move = moveStack.pop();

        set(move.getFromFile(), move.getFromRank(), get(move.getToFile(), move.getToRank()));
        set(move.getToFile(), move.getToRank(), capturedPiece);

        return move;
    }

    /**
     * Copy the current board. Pieces and moves remain the same.
     * @param other The board to copy to.
     */
    public void copyTo(Board other) {
        other.board.clear();
        other.board.putAll(board);

        other.moveStack.clear();
        other.moveStack.addAll(moveStack);

        other.capturedPieces.clear();
        other.capturedPieces.putAll(capturedPieces);
    }

    /**
     * An array of all played moves.
     * @return The already played moves on this board.
     */
    public Move[] getMoves() {
        Move[] moves = new Move[moveStack.size()];
        moveStack.copyInto(moves);
        return moves;
    }

    /**
     * Get a piece.
     * @param file The file (1-8).
     * @param rank The rank (1-8).
     * @return The piece or null.
     */
    public Piece get(int file, int rank) {
        return get(new Coordinates(file, rank));
    }

    /**
     * Get a piece.
     * @param coordinates The coordinates of the piece.
     * @return The piece or null.
     */
    public Piece get(Coordinates coordinates) {
        return board.get(coordinates);
    }

    /**
     * Place a piece. Does not affect history.
     * @param file The file (1-8).
     * @param rank The rank (1-8).
     * @param piece The piece.
     * @return The piece that was there before or null.
     */
    public Piece set(int file, int rank, Piece piece) {
        return set(new Coordinates(file, rank), piece);
    }

    /**
     * Place a piece. Does not affect history.
     * @param coordinates The coordinates of the piece.
     * @param piece The piece.
     * @return The piece that was there before or null.
     */
    public Piece set(Coordinates coordinates, Piece piece) {
        Piece old = get(coordinates);
        if (piece == null) {
            board.remove(coordinates);
        } else {
            board.put(coordinates, piece);
        }
        return old;
    }

    /**
     * Set the board up (initial position).
     * Also resets move history.
     */
    public void reset() {
        board.clear();

        set(1, 1, new Rook(Color.WHITE));
        set(2, 1, new Knight(Color.WHITE));
        set(3, 1, new Bishop(Color.WHITE));
        set(4, 1, new Queen(Color.WHITE));
        set(5, 1, new King(Color.WHITE));
        set(6, 1, new Bishop(Color.WHITE));
        set(7, 1, new Knight(Color.WHITE));
        set(8, 1, new Rook(Color.WHITE));

        for (int i=1; i<=8; i++) {
            set(i, 2, new Pawn(Color.WHITE));
            set(i, 7, new Pawn(Color.BLACK));
        }

        set(1, 8, new Rook(Color.BLACK));
        set(2, 8, new Knight(Color.BLACK));
        set(3, 8, new Bishop(Color.BLACK));
        set(4, 8, new Queen(Color.BLACK));
        set(5, 8, new King(Color.BLACK));
        set(6, 8, new Bishop(Color.BLACK));
        set(7, 8, new Knight(Color.BLACK));
        set(8, 8, new Rook(Color.BLACK));

        moveStack.clear();
        capturedPieces.clear();
    }
}
