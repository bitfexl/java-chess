package com.github.bitfexl.javachess.ui;

import com.github.bitfexl.javachess.Board;
import com.github.bitfexl.javachess.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ChessPanel extends JPanel {

    public static void main(String[] args) {
        JFrame window = new JFrame("Test");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(400, 400));

        ChessPanel chessPanel = new ChessPanel();
        window.add(chessPanel);

        window.pack();
        window.setVisible(true);
    }

    private final Color COLOR_LIGHT = Color.LIGHT_GRAY;
    private final Color COLOR_DARK = Color.GRAY;

    private final String SET_NAME = "standard";

    private final HashMap<String, BufferedImage> pieces = new HashMap<>();

    private final Board chessBoard = new Board(true);

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int sqWidth = getWidth() / 8;
        int sqHeight = getHeight() / 8;

        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(COLOR_LIGHT);

        for (int r=0; r<8; r++) {
            for (int c=0; c<8; c++) {
                g2d.fillRect(sqWidth * c, sqHeight * r, sqWidth * (c+1), sqHeight * (r+1));
                g2d.setColor(g2d.getColor() == COLOR_DARK ? COLOR_LIGHT : COLOR_DARK);
            }
            g2d.setColor(g2d.getColor() == COLOR_DARK ? COLOR_LIGHT : COLOR_DARK);
        }
    }

    /**
     * The board which gets drawn.
     * @return The board to copy pieces to.
     */
    public Board getChessBoard() {
        return chessBoard;
    }

    private BufferedImage getPiece(Piece piece) {
        if (!pieces.containsKey(piece.getId())) {
            pieces.put(piece.getId(), loadPiece(piece));
        }
        return pieces.get(piece.getId());
    }

    private BufferedImage loadPiece(Piece piece) {
        String resourceName = "/assets/pieces/" + SET_NAME + "/" + piece.getId() + ".png";
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(resourceName)));
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Piece set '" + SET_NAME + "' does not exist or is corrupted.", ex);
        } catch (IOException ex) {
            // todo: set unable to load flag or something
            throw new RuntimeException(ex);
        }
    }
}
