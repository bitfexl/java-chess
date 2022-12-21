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
        moves = moves.stream().filter(m -> board.get(m.getToFile(), m.getToRank()) == null).toList();

        // diagonal capture
        List<Move> allMoves = getDiagonalMoves(board, file, rank);
        allMoves.addAll(moves);
        moves = allMoves;

        moves = checkLineOfSight(moves, board);
        moves = checkOwnColor(moves, board);
        return moves;
    }

    private List<Move> getDiagonalMoves(Board board, int file, int rank) {
        List<Move> moves = new ArrayList<>();

        if (getColor() == Color.WHITE && rank+1 <= 8) {
            if (file-1 > 0) {
                Piece enemy = board.get(file-1, rank+1);
                if (enemy != null && enemy.getColor() != getColor()) {
                    moves.add(new Move(file, rank, file-1, rank+1));
                }
            }

            if (file+1 <= 8) {
                Piece enemy = board.get(file+1, rank+1);
                if (enemy != null && enemy.getColor() != getColor()) {
                    moves.add(new Move(file, rank, file+1, rank+1));
                }
            }
        } else if (getColor() == Color.BLACK && rank-1 > 0) {
            if (file-1 > 0) {
                Piece enemy = board.get(file-1, rank-1);
                if (enemy != null && enemy.getColor() != getColor()) {
                    moves.add(new Move(file, rank, file-1, rank-1));
                }
            }

            if (file+1 <= 8) {
                Piece enemy = board.get(file+1, rank-1);
                if (enemy != null && enemy.getColor() != getColor()) {
                    moves.add(new Move(file, rank, file+1, rank-1));
                }
            }
        }

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
