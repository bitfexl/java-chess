package com.github.bitfexl.javachess.ui;

import java.awt.*;

public class TextOverlay implements Overlay {
    private static final Font FONT = new Font("Consolas", Font.PLAIN, 30);

    private String text;

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
        g2d.setFont(FONT);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 10, 40);
    }
}
