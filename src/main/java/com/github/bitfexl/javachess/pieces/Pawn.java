package com.github.bitfexl.javachess.pieces;

import com.github.bitfexl.javachess.Board;
import com.github.bitfexl.javachess.Color;
import com.github.bitfexl.javachess.Move;
import com.github.bitfexl.javachess.RelativeCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public List<Move> getValidMoves(Board board, int file, int rank) {
        List<RelativeCoordinates> possibleMoves = new ArrayList<>(getPossibleMoves());

        // first move
        if (getColor() == Color.WHITE && rank == 2) {
            possibleMoves.add(new RelativeCoordinates(0, 2));
        } else if (getColor() == Color.BLACK && rank == 7) {
            possibleMoves.add(new RelativeCoordinates(0, -2));
        }

        List<Move> moves = getMoves(possibleMoves, file, rank);

        // no forward capture
        // todo: exception because toRank out of range (?)
        moves = moves.stream().filter(m -> board.get(m.getToFile(), m.getToRank()) == null).toList();

        // todo: diagonal capture

        moves = checkLineOfSight(moves, board);
        moves = checkOwnColor(moves, board);
        moves = checkCheck(moves, board);
        return moves;
    }

    @Override
    protected List<RelativeCoordinates> getPossibleMoves() {
        if (getColor() == Color.WHITE) {
            return List.of(new RelativeCoordinates(0, 1));
        } else if (getColor() == Color.BLACK) {
            return List.of(new RelativeCoordinates(0, -1));
        }

        return List.of(); // should never be reached
    }
}
