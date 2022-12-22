package com.github.bitfexl.javachess;

import com.github.bitfexl.javachess.game.Color;
import com.github.bitfexl.javachess.game.*;
import com.github.bitfexl.javachess.pieces.King;
import com.github.bitfexl.javachess.pieces.Pawn;
import com.github.bitfexl.javachess.pieces.Piece;
import com.github.bitfexl.javachess.ui.ChessPanel;
import com.github.bitfexl.javachess.ui.PromotionOverlay;
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

    private Piece selectedPiece;

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

        JButton btnUndo = new JButton("Undo");
        btnUndo.setMaximumSize(BTN_DIMENSION);
        btnUndo.addActionListener(e -> {
            if (board.undo() != null) {
                nextPlayer = nextPlayer.opponent();
            }
            clearGui();
        });
        buttonPanel.add(btnUndo);

        JButton btnReset = new JButton("Reset");
        btnReset.setMaximumSize(BTN_DIMENSION);
        btnReset.addActionListener(e -> {
            newGame();
        });
        buttonPanel.add(btnReset);

        JButton btnTurnBoard = new JButton("Turn Board");
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

        // move
        if (moves != null) {
            Move move = moveTo(file, rank);
            if (move != null) {
                if (!handlePromotion(move)) {
                    board.move(move);
                }
                moved = true;
                nextPlayer = nextPlayer.opponent();
            }
            moves = null;
        }

        // get possible moves
        if (!moved && clickedPiece != null && clickedPiece.getColor() == nextPlayer) {
            selectedPiece = clickedPiece;
            moves = clickedPiece.getTrueValidMoves(board, new Coordinates(file, rank));
            chessPanel.setMarker(file, rank, ChessPanel.Marker.SELECTED);
        }

        postMove();
    }

    private boolean handlePromotion(Move move) {
        if (!(selectedPiece instanceof Pawn) || !move.qualifiedPromotion(selectedPiece.getColor())) {
            return false;
        }

        chessPanel.setOverlay(new PromotionOverlay(selectedPiece.getColor()) {
            @Override
            protected void pieceSelected(Piece piece) {
                chessPanel.setOverlay(null);
                board.move(new PromotionMove(move, piece));
                postMove();
            }
        });

        return true;
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

    private void postMove() {
        displayMoves();
        displayCheck();

        if (board.isCheckMate(nextPlayer)) {
            chessPanel.setOverlay(new TextOverlay("Game Over! " + nextPlayer.opponent() + " won!"));
        } else if (board.isStaleMate(nextPlayer)) {
            chessPanel.setOverlay(new TextOverlay("Game Over! Stalemate!"));
        }

        updateGui();
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
