package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.game.Board;
import com.github.bitfexl.javachess.game.Color;
import com.github.bitfexl.javachess.game.Move;
import com.github.bitfexl.javachess.game.RelativeCoordinates;

import java.util.List;

public class Knight extends Piece {
    private final List<RelativeCoordinates> MOVES = List.of(
            new RelativeCoordinates(2, 1),
            new RelativeCoordinates(1, 2),
            new RelativeCoordinates(-2, 1),
            new RelativeCoordinates(-1, 2),
            new RelativeCoordinates(-2, -1),
            new RelativeCoordinates(-1, -2),
            new RelativeCoordinates(2, -1),
            new RelativeCoordinates(1, -2)
    );

    public Knight(Color color) {
        super(color);
    }

    @Override
    public List<Move> getValidMoves(Board board, int file, int rank) {
        List<Move> moves = getMoves(getPossibleMoves(), file, rank);
        // no line of sight checking
        moves = checkOwnColor(moves, board);
        return moves;
    }

    @Override
    protected List<RelativeCoordinates> getPossibleMoves() {
        return MOVES;
    }
}
