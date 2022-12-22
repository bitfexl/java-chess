package com.github.bitfexl.javachess.ui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class TextOverlay implements Overlay {
    private static final Font FONT = new Font("Consolas", Font.PLAIN, 30);

    private String text;

    /**
     * Bounds calculated by getFontForWidth().
     */
    private Rectangle2D fontBounds;

    public TextOverlay(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(ChessPanel panel, Graphics2D g2d) {
        final int width = panel.getWidth();
        final int height = panel.getHeight();

        g2d.setFont(getFontForWidth(width, g2d.getFontRenderContext()));

        int x = (int)(width - fontBounds.getWidth()) / 2;
        int y = (int)(height + fontBounds.getHeight()) / 2;
        int padding = (int)fontBounds.getHeight() / 4;

        g2d.setColor(new Color(255, 255, 255, 20));
        g2d.fillRect(x - padding, y-(int)fontBounds.getHeight(), (int)fontBounds.getWidth() + padding * 2, (int)fontBounds.getHeight());
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x, y - padding);
    }

    private Font getFontForWidth(int width, FontRenderContext fontRenderContext) {
        Font newFont = null;
        for (int d=0; d<20; d++) {
            newFont = FONT.deriveFont((float)FONT.getSize() + d);
            fontBounds = newFont.getStringBounds(text, fontRenderContext);
            if (fontBounds.getWidth() >= width * 0.7) {
                return newFont;
            }
        }
        return newFont;
    }
}
