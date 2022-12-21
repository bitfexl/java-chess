package com.github.bitfexl.javachess.ui;

public interface ClickListener {
    /**
     * Event called when board is clicked (left mouse button).
     * @param file The clicked file.
     * @param rank The clicked rank.
     */
    void clicked(int file, int rank);
}
