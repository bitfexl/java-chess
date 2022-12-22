package com.github.bitfexl.javachess.ui;

import java.awt.*;

/**
 * An overlay for the chess panel.
 * Receives all click events while dialog is active.
 */
public interface Overlay {
    /**
     * Render the overlay (on top of rendered game board).
     * @param panel The panel to render the overlay on.
     * @param g2d The graphics to draw on.
     */
    void render(ChessPanel panel, Graphics2D g2d);

    /**
     * Called when the ChessPanel is clicked.
     * @param panel The clicked panel.
     * @param x The x coordinate.
     * @param y The y coordinate (swing coordinates).
     */
    void clicked(ChessPanel panel, int x, int y);
}
