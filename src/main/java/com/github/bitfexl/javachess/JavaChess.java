package com.github.bitfexl.javachess;

import com.github.bitfexl.javachess.pieces.Piece;
import com.github.bitfexl.javachess.ui.ChessPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JavaChess {
    public static void main(String[] args) {
        new JavaChess().run();
    }

    private Piece selectedPiece;

    private List<Move> moves;

    private final Board board = new Board();

    private ChessPanel chessPanel;

    public void run() {
        JFrame window = new JFrame("Test");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chessPanel = new ChessPanel();
        chessPanel.setOnClick(this::onClick);
        chessPanel.getChessBoard().reset();
        chessPanel.setBlackPov(false);

        chessPanel.setPreferredSize(new Dimension(400, 400));
        window.add(chessPanel);

        window.pack();
        window.setVisible(true);
    }

    private void onClick(int file, int rank) {
        Piece clickedPiece = board.get(file, rank);

        chessPanel.clearMarkers();


        if (selectedPiece != null) {
            // move
            Move move = moveTo(file, rank);
            if (move != null) {
                board.move(move);
                selectedPiece = null;
                updateGui();
                return;
            }
        }

        if (clickedPiece != null) {
            // display possible moves
            moves = clickedPiece.getTrueValidMoves(board, file, rank);
            for (Move move : moves) {
                ChessPanel.Marker marker;
                if (board.get(move.getToFile(), move.getToRank()) != null) {
                    marker = ChessPanel.Marker.CAPTURE;
                } else {
                    marker = ChessPanel.Marker.MOVE;
                }
                chessPanel.setMarker(move.getToFile(), move.getToRank(), marker);
            }
        }

        selectedPiece = clickedPiece;
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

    private void updateGui() {
        board.copyTo(chessPanel.getChessBoard());
        chessPanel.repaint();
    }
}
