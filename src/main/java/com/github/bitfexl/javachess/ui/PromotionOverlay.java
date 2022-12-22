package com.github.bitfexl.javachess.ui;

import com.github.bitfexl.javachess.pieces.*;

import java.awt.*;
import java.util.List;

public abstract class PromotionOverlay implements Overlay {
    private final Color BG_COLOR = new Color(255, 255, 255, 20);

    private final List<Piece> pieces;

    // internally used values
    private int width;
    private int pWidth;
    private int padding;
    private int xOrigin;
    private int yOrigin;

    /**
     * Init a new promotion overlay with queen, knight, rook, bishop.
     * @param color The color of the player which is promoting.
     */
    protected PromotionOverlay(com.github.bitfexl.javachess.game.Color color) {
        this(List.of(new Queen(color), new Knight(color), new Rook(color), new Bishop(color)));
    }

    /**
     * Init a new promotion dialog.
     * @param pieces The pieces to chose from.
     */
    protected PromotionOverlay(List<Piece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public void render(ChessPanel panel, Graphics2D g2d) {
        calculateValues(panel);

        g2d.setColor(BG_COLOR);
        for (int i=0; i<pieces.size(); i++) {
            g2d.fillRect(
                    xOrigin + pWidth*i,
                    yOrigin,
                    pWidth,
                    pWidth
            );
            g2d.drawImage(
                    panel.getPiece(pieces.get(i)),
                    xOrigin + pWidth*i + padding,
                    yOrigin + padding,
                    pWidth - padding*2,
                    pWidth - padding*2,
                    null
            );
        }
    }

    @Override
    public void clicked(ChessPanel panel, int x, int y) {
        calculateValues(panel);

        if (!isBetween(y, yOrigin, yOrigin + pWidth)) {
            return;
        }

        for (int i=0; i<pieces.size(); i++) {
            if (isBetween(x, xOrigin + pWidth*i, xOrigin + pWidth*(i+1))) {
                pieceSelected(pieces.get(i));
                return;
            }
        }
    }

    /**
     * Called when the player chooses a piece.
     * Exit the dialog on call.
     * @param piece The chosen piece.
     */
    protected abstract void pieceSelected(Piece piece);

    private void calculateValues(ChessPanel panel) {
        width = (int)(panel.getWidth() * 0.7);
        pWidth = width / pieces.size();
        padding = (int)(panel.getWidth() * 0.02);
        xOrigin = (panel.getWidth() - width) / 2;
        yOrigin = (panel.getHeight() - pWidth) / 2;
    }

    private boolean isBetween(int x, int min, int max) {
        return x >= min && x <= max;
    }
}
