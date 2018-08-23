package com.chesschecker.moves;

import com.chesschecker.bitboard.BitBoard;
import com.chesschecker.util.Column;

/**
 * A board move is defined as a move that starts and ends on a valid square of a 8x8 chess board
 * This comes from rule 2.1 of https://www.fide.com/fide/handbook.html?id=171&view=article
 */
public class BoardMove implements Move{
    int startRow;
    int startCol;
    int endRow;
    int endCol;


    public BoardMove(final int startrow, final int startcol, final int endrow, final int endcol) {
        super();
        this.startRow = startrow;
        this.startCol = startcol;
        this.endRow = endrow;
        this.endCol = endcol;
    }

    private static boolean isValidRow(final int row) {
        if (0 <= row) {
            if (8 > row) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isValidCol(final int row) {
        if (0 <= row) {
            if (8 > row) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValid(final BitBoard friendly, final BitBoard foe) {
        if (BoardMove.isValidRow(this.startRow)) {
            if (BoardMove.isValidRow(this.endRow)) {
                if (BoardMove.isValidCol(this.startCol)) {
                    if (BoardMove.isValidCol(this.endCol)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(0);
        final String str = Column.columnNumberToLetter(this.endCol);
        result.append(str);
        result.append(this.endRow+1);
        return result.toString();
    }
}
