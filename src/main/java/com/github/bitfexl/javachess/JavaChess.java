package com.github.bitfexl.javachess;

import com.github.bitfexl.javachess.game.Board;
import com.github.bitfexl.javachess.game.Color;
import com.github.bitfexl.javachess.game.Coordinates;
import com.github.bitfexl.javachess.game.Move;
import com.github.bitfexl.javachess.pieces.King;
import com.github.bitfexl.javachess.pieces.Piece;
import com.github.bitfexl.javachess.ui.ChessPanel;
import com.github.bitfexl.javachess.ui.TextOverlay;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class JavaChess {
    public static void main(String[] args) {
        new JavaChess().run();
    }

    private final Dimension BTN_DIMENSION = new Dimension(100, 20);

    private List<Move> moves;

    private final Board board = new Board();

    private ChessPanel chessPanel;

    private com.github.bitfexl.javachess.game.Color nextPlayer;

    public void run() {
        JFrame window = new JFrame("Test");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        Button btnUndo = new Button("Undo");
        btnUndo.setMaximumSize(BTN_DIMENSION);
        btnUndo.addActionListener(e -> {
            if (board.undo() != null) {
                nextPlayer = nextPlayer.opponent();
            }
            clearGui();
        });
        buttonPanel.add(btnUndo);

        Button btnReset = new Button("Reset");
        btnReset.setMaximumSize(BTN_DIMENSION);
        btnReset.addActionListener(e -> {
            newGame();
        });
        buttonPanel.add(btnReset);

        Button btnTurnBoard = new Button("Turn Board");
        btnTurnBoard.setMaximumSize(BTN_DIMENSION);
        btnTurnBoard.addActionListener(e -> {
            chessPanel.setBlackPov(!chessPanel.isBlackPov());
        });
        buttonPanel.add(btnTurnBoard);

        chessPanel = new ChessPanel();
        chessPanel.setOnClick(this::onClick);
        chessPanel.setPreferredSize(new Dimension(400, 400));

        newGame();

        window.add(buttonPanel);
        window.add(chessPanel);

        window.pack();
        window.setVisible(true);
    }

    private void newGame() {
        board.reset();
        nextPlayer = Color.WHITE;
        chessPanel.setBlackPov(false);
        clearGui();
    }

    private void onClick(int file, int rank) {
        Piece clickedPiece = board.get(file, rank);

        chessPanel.clearMarkers();

        boolean moved = false;

        if (moves != null) {
            // move
            Move move = moveTo(file, rank);
            if (move != null) {
                board.move(move);
                moved = true;
                nextPlayer = nextPlayer.opponent();
            }
            moves = null;
        }

        // get possible moves
        if (!moved && clickedPiece != null && clickedPiece.getColor() == nextPlayer) {
            moves = clickedPiece.getTrueValidMoves(board, new Coordinates(file, rank));
            chessPanel.setMarker(file, rank, ChessPanel.Marker.SELECTED);
        }

        displayMoves();
        displayCheck();

        if (board.isCheckMate(nextPlayer)) {
            chessPanel.setOverlay(new TextOverlay("Game Over! " + nextPlayer.opponent() + " won!"));
        } else if (board.isStaleMate(nextPlayer)) {
            chessPanel.setOverlay(new TextOverlay("Game Over! Stalemate!"));
        }

        updateGui();
    }

    private Move moveTo(int file, int rank) {
        if (moves == null) {
            return null;
        }

        for (Move move : moves) {
            if (move.getToFile() == file && move.getToRank() == rank) {
                return move;
            }
        }

        return null;
    }

    private void displayCheck() {
        if (board.isInCheck(Color.WHITE)) {
            Coordinates whiteKing = board.getCoordinates(King.class, Color.WHITE).iterator().next();
            chessPanel.setMarker(whiteKing.getFile(), whiteKing.getRank(), ChessPanel.Marker.CHECK);
        }

        if (board.isInCheck(Color.BLACK)) {
            Coordinates whiteKing = board.getCoordinates(King.class, Color.BLACK).iterator().next();
            chessPanel.setMarker(whiteKing.getFile(), whiteKing.getRank(), ChessPanel.Marker.CHECK);
        }

        // NoSuchElementException should not be thrown,
        // because you should not be able to do a move that
        // would allow your opponent to capture your king.
    }

    private void displayMoves() {
        for (Move move : Optional.ofNullable(moves).orElse(List.of())) {
            ChessPanel.Marker marker;
            if (board.get(move.getToFile(), move.getToRank()) != null) {
                marker = ChessPanel.Marker.CAPTURE;
            } else {
                marker = ChessPanel.Marker.MOVE;
            }
            chessPanel.setMarker(move.getToFile(), move.getToRank(), marker);
        }
    }

    private void clearGui() {
        chessPanel.clearMarkers();
        chessPanel.setOverlay(null);
        updateGui();
    }

    private void updateGui() {
        board.copyTo(chessPanel.getChessBoard());
        chessPanel.repaint();
    }
}
