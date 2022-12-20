package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.Color;
import com.github.bitfexl.javachess.RelativeCoordinates;

import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    protected List<RelativeCoordinates> getPossibleMoves() {
        return null;
    }
}
