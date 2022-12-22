package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.game.Color;
import com.github.bitfexl.javachess.game.RelativeCoordinates;

import java.util.List;

public class King extends Piece {
    final List<RelativeCoordinates> MOVES;

    {
        MOVES = List.of(
                new RelativeCoordinates(1, 1),
                new RelativeCoordinates(-1, -1),
                new RelativeCoordinates(-1, 1),
                new RelativeCoordinates(1, -1),
                new RelativeCoordinates(0, 1),
                new RelativeCoordinates(0, -1),
                new RelativeCoordinates(1, 0),
                new RelativeCoordinates(-1, 0)

        );
    }

    public King(Color color) {
        super(color);
    }

    @Override
    protected List<RelativeCoordinates> getPossibleMoves() {
        return MOVES;
    }
    // todo: castle
}
