package com.github.bitfexl.javachess.ui;

import java.awt.*;

/**
 * An overlay for the chess panel.
 * Can also implement ClickListener
 * to listen for click events while
 * the overlay is active.
 */
public interface Overlay {
    /**
     * Render the overlay (on top of rendered game board).
     * @param panel The panel to render the overlay on.
     * @param g2d The graphics to draw on.
     */
    void render(ChessPanel panel, Graphics2D g2d);
}
