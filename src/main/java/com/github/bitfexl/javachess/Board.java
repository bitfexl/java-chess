package com.github.bitfexl.javachess;

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
        return x < 1 || x > 8;
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

    private Piece[][] board;

    {
        board = new Piece[8][];
        for (int i=0; i<8; i++) {
            board[i] = new Piece[8];
        }
    }

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
     * Get a piece.
     * @param file The file (1-8).
     * @param rank The rank (1-8).
     * @return The piece or null.
     */
    public Piece get(int file, int rank) {
        checkInBoundsException(file);
        checkInBoundsException(rank);
        return board[rank-1][file-1];
    }

    /**
     * Place a piece. Does not affect history.
     * @param file The file (1-8).
     * @param rank The rank (1-8).
     * @param piece The piece.
     * @return The piece that was there before or null.
     */
    public Piece set(int file, int rank, Piece piece) {
        checkInBoundsException(file);
        checkInBoundsException(rank);
        Piece old = get(file, rank);
        board[rank-1][file-1] = piece;
        return old;
    }

    /**
     * Set the board up (initial position).
     * Also resets move history.
     */
    public void reset() {
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
