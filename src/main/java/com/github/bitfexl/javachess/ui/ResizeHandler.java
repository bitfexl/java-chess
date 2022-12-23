package com.github.bitfexl.javachess.ui;

import javax.swing.*;
import java.awt.*;

public class ResizeHandler {
    private final JFrame jFrame;

    private final int initialWidth;

    private final int initialHeight;

    private int xStep;

    private int yStep;

    private boolean remainSideRatio;

    /**
     * Init a new resize handler.
     * Needs to be set as a component listener of the specified frame.
     * @param jFrame The frame to resize.
     * @param xStep The pixels in x direction to add/remove per resize.
     * @param yStep The pixels in y direction to add/remove per resize.
     * @param remainSideRatio Always add x and y or none.
     */
    public ResizeHandler(JFrame jFrame, int xStep, int yStep, boolean remainSideRatio) {
        this.jFrame = jFrame;
        this.initialWidth = jFrame.getWidth();
        this.initialHeight = jFrame.getHeight();
        this.xStep = xStep;
        this.yStep = yStep;
        this.remainSideRatio = remainSideRatio;
    }

    /**
     * Resizes the component to fit the resize configuration.
     */
    public void resize() {
        int width = jFrame.getWidth() - (Math.abs(jFrame.getWidth() - initialWidth) % xStep);
        int height;

        if (remainSideRatio) {
            height = width + (initialHeight - initialWidth);
        } else {
            height = jFrame.getHeight() - (Math.abs(jFrame.getHeight() - initialHeight) % yStep);
        }

        jFrame.setSize(new Dimension(width, height));
    }

    public JFrame getFrame() {
        return jFrame;
    }

    public int getXStep() {
        return xStep;
    }

    public void setXStep(int xStep) {
        this.xStep = xStep;
    }

    public int getYStep() {
        return yStep;
    }

    public void setYStep(int yStep) {
        this.yStep = yStep;
    }

    public boolean isRemainSideRatio() {
        return remainSideRatio;
    }

    public void setRemainSideRatio(boolean remainSideRatio) {
        this.remainSideRatio = remainSideRatio;
    }
}
