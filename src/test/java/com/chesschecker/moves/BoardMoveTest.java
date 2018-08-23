package com.chesschecker.moves;

import com.chesschecker.bitboard.BitBoard;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("ALL")
public class BoardMoveTest {

    @Test
    public void isValid_1() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, 0, 0, 0);
        Assert.assertEquals(sut.isValid(empty, empty), true);
    }

    @Test
    public void isValid_2() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(-1, 0, 0, 0);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }


    @Test
    public void isValid_3() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(8, 0, 0, 0);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void isValid_4() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, 0, -1, 0);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void isValid_5() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, 0, 9, 0);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void isValid_6() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, -1, 0, 0);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void isValid_7() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, 9, 0, 0);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void isValid_8() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, 0, 0, -1);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void isValid_9() {
        final BitBoard empty = new BitBoard();
        final BoardMove sut = new BoardMove(0, 0, 0, 9);
        Assert.assertFalse("BoardMove on invalid row should not be valid", sut.isValid(empty, empty));
    }

    @Test
    public void toString_test() {
        final BoardMove sut = new BoardMove(0, 0, 0, 0);
        Assert.assertEquals("a1", sut.toString());
    }
}