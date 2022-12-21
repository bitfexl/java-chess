package com.github.bitfexl.javachess;

public enum Color {
    BLACK, WHITE;

    public Color opponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}
