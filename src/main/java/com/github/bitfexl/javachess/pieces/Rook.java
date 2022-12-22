package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.game.Color;
import com.github.bitfexl.javachess.game.RelativeCoordinates;

import java.util.List;

public class Rook extends Piece {
    private final List<RelativeCoordinates> MOVES;

    {
        RelativeCoordinates[] moves = new RelativeCoordinates[7*4];
        for (int i=0; i<7; i++) {
            int c = 1 + i;
            moves[i*4] = new RelativeCoordinates(c, 0);
            moves[i*4+1] = new RelativeCoordinates(-c, 0);
            moves[i*4+2] = new RelativeCoordinates(0, c);
            moves[i*4+3] = new RelativeCoordinates(0, -c);
        }
        MOVES = List.of(moves);
    }

    public Rook(Color color) {
        super(color);
    }

    @Override
    protected List<RelativeCoordinates> getPossibleMoves() {
        return MOVES;
    }
}
