package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.game.Color;
import com.github.bitfexl.javachess.game.RelativeCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    final List<RelativeCoordinates> MOVES;

    {
        List<RelativeCoordinates> moves = new ArrayList<>();
        moves.addAll(new Rook(Color.BLACK).getPossibleMoves());
        moves.addAll(new Bishop(Color.BLACK).getPossibleMoves());
        MOVES = moves.stream().toList(); // unmodifiable list
    }

    public Queen(Color color) {
        super(color);
    }

    @Override
    protected List<RelativeCoordinates> getPossibleMoves() {
        return MOVES;
    }
}
