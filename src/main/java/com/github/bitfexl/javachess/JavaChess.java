package com.github.bitfexl.javachess;

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

    private List<Move> moves;

    private final Board board = new Board();

    private ChessPanel chessPanel;

    private Color nextPlayer;

    public void run() {
        JFrame window = new JFrame("Test");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chessPanel = new ChessPanel();
        chessPanel.setOnClick(this::onClick);
        chessPanel.getChessBoard().reset();
        chessPanel.setBlackPov(false);
        chessPanel.setOverlay(new TextOverlay("Hello, World!"));

        nextPlayer = Color.WHITE;

        chessPanel.setPreferredSize(new Dimension(400, 400));
        window.add(chessPanel);

        window.pack();
        window.setVisible(true);
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
            moves = clickedPiece.getTrueValidMoves(board, file, rank);
        }

        displayMoves();

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

    private void updateGui() {
        board.copyTo(chessPanel.getChessBoard());
        chessPanel.repaint();
    }
}
