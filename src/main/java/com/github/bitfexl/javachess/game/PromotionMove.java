package com.github.bitfexl.javachess.game;

import com.github.bitfexl.javachess.pieces.Piece;

public class PromotionMove extends Move {
    private final Piece piece;

    /**
     * Init a promotion move.
     * @param move The move (must qualify for promotion).
     * @param piece The piece to promote to.
     * @throws IllegalArgumentException move does not qualify for promotion.
     */
    public PromotionMove(Move move, Piece piece) {
        super(move.getFromFile(), move.getFromRank(), move.getToFile(), move.getToRank());

        if (!qualifiedPromotion(piece.getColor())) {
            throw new IllegalArgumentException("No promotion move.");
        }

        this.piece = piece;
    }

    /**
     * The piece to promote to.
     */
    public Piece getPiece() {
        return piece;
    }
}
