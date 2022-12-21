package com.github.bitfexl.javachess.ui;

import com.github.bitfexl.javachess.Board;
import com.github.bitfexl.javachess.Move;
import com.github.bitfexl.javachess.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ChessPanel extends JPanel implements MouseListener {
    enum Marker {
        MOVE, CAPTURE, CHECK;
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Test");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ChessPanel chessPanel = new ChessPanel();
        chessPanel.getChessBoard().reset();
        chessPanel.setBlackPov(false);

        chessPanel.setOnClick((file, rank) -> {
            chessPanel.clearMarkers();

            for (Move move : chessPanel.getChessBoard().get(file, rank).getValidMoves(chessPanel.getChessBoard(), file, rank)) {
                chessPanel.setMarker(move.getToFile(), move.getToRank(), Marker.MOVE);
            }

            chessPanel.repaint();
        });

        chessPanel.setPreferredSize(new Dimension(400, 400));
        window.add(chessPanel);

        window.pack();
        window.setVisible(true);
    }

    private final Color COLOR_LIGHT = Color.LIGHT_GRAY;
    private final Color COLOR_DARK = Color.GRAY;
    private final Color COLOR_MARKING = new Color(0, 100, 0, 80);
    private final Color COLOR_CHECK = new Color(150, 0, 0, 80);

    private final String SET_NAME = "standard";

    /**
     * true: black pov, false: white pov;
     */
    private boolean blackPov = false;

    /**
     * Called when board gets clicked.
     */
    private ClickListener onClick;

    // row * 1000 + col
    private final HashMap<Integer, Marker> markings = new HashMap<>();

    private final HashMap<String, BufferedImage> pieces = new HashMap<>();

    private final Board chessBoard = new Board(true);

    public ChessPanel() {
        addMouseListener(this);
    }

    /**
     * The board which gets drawn.
     * @return The board to copy pieces to.
     */
    public Board getChessBoard() {
        return chessBoard;
    }

    public boolean isBlackPov() {
        return blackPov;
    }

    public void setBlackPov(boolean blackPov) {
        this.blackPov = blackPov;
    }

    public void setOnClick(ClickListener onClick) {
        this.onClick = onClick;
    }

    /**
     * Removes all board markings.
     */
    public void clearMarkers() {
        markings.clear();
    }

    /**
     * Set/add a marker to the board.
     * @param file The file to add the marker to (1 to 8).
     * @param rank The rank to add the marker to (1 to 8).
     * @param marker The marker type to add.
     * @throws IllegalArgumentException File or rank out of range.
     */
    public void setMarker(int file, int rank, Marker marker) {
        Board.checkInBoundsException(file);
        Board.checkInBoundsException(rank);
        int row = isBlackPov() ? rank - 1 : 8 - rank;
        int col = isBlackPov() ? 8 - file : file - 1;
        markings.put(row * 1000 + col, marker);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        final int sqWidth = getWidth() / 8;
        final int sqHeight = getHeight() / 8;
        int sqWidthRest = getWidth() % 8;
        int sqHeightReset = getHeight() % 8;

        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(COLOR_LIGHT);

        for (int r=0; r<8; r++) {
            for (int c=0; c<8; c++) {
                int sqX = sqWidth * c;
                int sqY = sqHeight * r;

                g2d.fillRect(sqX, sqY, sqWidth, sqHeight);

                Marker marker = markings.get(r * 1000 + c);
                if (marker != null) {
                    drawMarking(g2d, marker, sqX, sqY, sqWidth, sqHeight);
                }

                // System.out.println("sqx: " + sqX + ", sqy: " + sqY + ", sqwidth: " + sqWidth + ", sqheight: " + sqHeight);

                Piece piece = isBlackPov() ?
                        chessBoard.get(8-c, r+1) :
                        chessBoard.get(c+1, 8-r);

                if (piece != null) {
                    g2d.drawImage(getPiece(piece), sqX, sqY, sqWidth, sqHeight, null);
                }

                g2d.setColor(g2d.getColor() == COLOR_DARK ? COLOR_LIGHT : COLOR_DARK);
            }
            g2d.setColor(g2d.getColor() == COLOR_DARK ? COLOR_LIGHT : COLOR_DARK);
        }
    }

    private void drawMarking(Graphics2D g2d, Marker type, int sqX, int sqY, int sqWidth, int sqHeight) {
        final int CAPTURE_WIDTH = (int)(sqWidth * 0.06);
        final int MOVE_RADIUS = (int)(sqWidth * 0.2);

        final Color oldColor = g2d.getColor();

        if (type == Marker.CAPTURE || type == Marker.CHECK) {
            g2d.setColor(type == Marker.CHECK ? COLOR_CHECK : COLOR_MARKING);
            g2d.fillRect(sqX, sqY, sqWidth, sqHeight);
            g2d.setColor(oldColor);
            g2d.fillRect(sqX + CAPTURE_WIDTH, sqY + CAPTURE_WIDTH, sqWidth - CAPTURE_WIDTH * 2, sqHeight - CAPTURE_WIDTH * 2);
        } else if (type == Marker.MOVE) {
            g2d.setColor(COLOR_MARKING);
            g2d.fillArc(sqX + sqWidth / 2 - MOVE_RADIUS, sqY + sqHeight / 2 - MOVE_RADIUS, MOVE_RADIUS * 2, MOVE_RADIUS * 2, 0, 360);
        }

        g2d.setColor(oldColor);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int col = (int)((float)e.getX() / getWidth() * 8);
            int row = (int)((float)e.getY() / getHeight() * 8);
            int rank = isBlackPov() ? row + 1 : 8 - row;
            int file = isBlackPov() ? 8 - col : col + 1;

            if (onClick != null) {
                onClick.clicked(file, rank);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
