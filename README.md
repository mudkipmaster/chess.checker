# chess.checker
[![Build Status](https://travis-ci.com/mudkipmaster/chess.checker.svg?branch=master)](https://travis-ci.com/mudkipmaster/chess.checker)

The repository contains a valid move generator designed to follow the following input and output scheme:

```
The input for the above board configuration should look something like this:
WHITE: Rf1, Kg1, Pf2, Ph2, Pg3
BLACK: Kb8, Ne8, Pa7, Pb7, Pc7, Ra5
PIECE TO MOVE: Rf1

Given the above input description, your program should produce the following output:
LEGAL MOVES FOR Rf1: e1, d1, c1, b1, a1
```

A code coverage report can be found as an HTML document here (you will have to download the repository to view this:
https://github.com/mudkipmaster/chess.checker/blob/1ba78a6a4b72bb21b0a96385f3e7deabe5a467c4/inspectionresults/coverage/index.html

A static analysis report can be found here:
https://github.com/mudkipmaster/chess.checker/blob/1ba78a6a4b72bb21b0a96385f3e7deabe5a467c4/inspectionresults/staticanalysis/index.html

All tests can be found in the `src/test` directory. They are Junit tests. I think these can be run using my Gradle build config, but I'm honestly not sure how to do that. Instead, I just use Intellij

See the bottom of this README for information about the status of this program.

## Architecture
The goal of this project was not performance but rather to be highly "dependable". To do this, move validation was done using a set subtraction system wherein each piece’s valid moves are a subset of every possible valid move. The goal of the architecture was to reduce code reuse and eliminate unnecessary complexity while maintaining modularity. The following figure shows the general structure of the code.

![Image of DataFlow](https://github.com/mudkipmaster/chess.checker/blob/66e21967e9ca18356845e2fde1eb0a4d216e978e/inspectionresults/Dataflow.png)

## Move Validation
The following figure sets up this hieracy.

![Image of Class Hierarchy](https://github.com/mudkipmaster/chess.checker/blob/ba3636c97f0209adf60ac8316bb1a6ae6ad8633e/inspectionresults/ClassHeirarchy.png)


The thought was that if move validation was treated as near a mathematical problem as possible, it would be more easily tested and verified. Rules for the pieces were taken from [here](https://www.fide.com/fide/handbook.html?id=171&amp;view=article)
The test suit for this section of the program is actually reasonable simple. For each piece, a list of moves were generated that verified that each condition that made it different from its parent was tested. For instance, A RookMove was defined as being able to make all Queen moves that are not Bishop moves. In the code this looks like this:

```
if (this.isValidBoardMove()) {
    if (this.isValidColoredMove(friendly)) {
        if (this.isValidSlideMove(friendly, for)) {
            if (this.isValidQueenMove()) {
                if (this.isValidBishopMove()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
}
```

We verify that this piece is valid if and only if by verifying that a valid can be made in valid by setting the return value of any of its parent class' validation functions to false. Then we are left only to validate the moves that would be valid queen moves but not valid Rook moves. In this test class this looks like this (at the same time we test that the `toString()` method also returns the correct thing):
```
{3, 4, 6, 7, empty, empty, "Rh7", false},
{3, 4, 0, 7, empty, empty, "Rh1", false},
{3, 4, 7, 0, empty, empty, "Ra8", false},
{3, 4, 0, 1, empty, empty, "Rb1", false},
```
### Static Analysis
One of the rules that was set in the static analysis tool in Intellij was a warning for if statements with negate conditions. Initially I had several but, like the code snippet above shows, I reversed them. The resulting code is easier to read and less prone to mistakes. There are 6 remaining warning for this section. They are mostly warnings of violation of the Law of Demeter. The Law of Demeter states that 
> Each unit should have only limited knowledge about other units: only units "closely" related to the current unit.

Intellij says I should refactor these methods and, given more time, I would definitely try. You can see the full result of the inspections [here](https://github.com/mudkipmaster/chess.checker/blob/1ba78a6a4b72bb21b0a96385f3e7deabe5a467c4/inspectionresults/staticanalysis/index.html)

### Code Coverage
There are 821 tests in the move validation suit. The goal was to obtain 100% line coverage. To ensure that line coverage was a meaningful metric, one of the static analysis rules that was added was the requirement that every if statement have a corresponding else. With this rule, it was necessary to exercise both positive and negative branches of each condition to obtain 100% line coverage. You can see the full result of the coverage analysis [here](https://github.com/mudkipmaster/chess.checker/blob/1ba78a6a4b72bb21b0a96385f3e7deabe5a467c4/inspectionresults/coverage/index.html)

## Move Generation
While theoretically we could start with a pregenerated list of all possible moves for all possible positions on the chess board, this would be a prohibitively massive set. Instead, we choose to generate a set of moves for each input piece from its starting position to every square on the board.
```
for (int i = 0; 8 &gt; i; i++) {
   for (int j = 0; 8 &gt; j; j++) {
       //no inspection NestedTryStatement
       try {
           switch (PieceAbbreviation.fromAbbreviation(type)) {
               case KING:
                   result.add(new KingMove(row, column, i, j));
                   result.add(new CastlingKingMove(row, column, i, j));
                   result.add(new CastlingKingMove(row, column, i, j));
                   break;
               case QUEEN:
                   result.add(new QueenMove(row, column, i, j));
                   break;
               case ROOK:
                   result.add(new RookMove(row, column, i, j));
                   break;
               case BISHOP:
                   result.add(new BishopMove(row, column, i, j));
                   break;
               case KNIGHT:
                   result.add(new KnightMove(row, column, i, j));
                   break;
               case PAWN:
                   result.add(new PawnMove(row, column, i, j));
                   result.add(new PawnCaptureMove(row, column, i, j));
                   result.add(new Pawn37DMove(row, column, i, j));
                   break;
               default:
                   //Don't add moves that don't have a valid piece type
                   break;
```

As you can see, we define a few special types of moves to handle the intricacies of chess. Pawns can make 3 types of moves, and the King can make both regular moves and special Castling moves. As such, having a piece of this position will add more than one move to the move set.

## Black box testing

Early on, to generate some test input data, I wrote a little script to generate sample input for this program from real game data. You can read more about this in the README in the repository:
https://github.com/mudkipmaster/ChessGameToMoveList

While this does not exhaustively test moves, it does give us at least one legal move for each board state. We generate 4,536 tests from 1000 games which achieves 75% coverage of the code. While this may not be the most efficient method of testing, it does allow a lot of tests to be generated quickly (though this test only counts as one in our total test count). Fun fact: Castling moves appeared 58 times in our test data.

## Black Move Support

In order to reduce the number of execution paths for move validation, it was decided that the validation would only work for white moves. In order to support black moves, we need to orient the board such that it appears to the move validator to be a white move. To do this, we introduce the idea of "Oriented" or the board state from the white perspective. When we parse the board state from the input, we use the following factory:

```
if (whiteIn.containsAll(moveIn)) {
    return new WhiteBoardState(whiteIn, blackIn, moveIn);
} else {
    if (blackIn.containsAll(moveIn)) {
        return new BlackBoardState(whiteIn, blackIn, moveIn);
    } else {// The piece that is supposed to be moved is not actually a piece. Or
        // the piece list contains pieces that are both black and white
        // Default to white, and return no pieces
    return new WhiteBoardState();
}       
```

WhiteBoardState has the following `getOrientedWhite()` method:
```
public final Set<String> getOrientedWhite() {
    return this.white;
}
```

While BlackBoardState has the following `getOrientedWhite()` method, which serves to flip the black positions so that they appear as they would from the white perspective:
```
public final Set<String> getOrientedWhite() {
    return PieceList.flipRows(this.black);
}
```

This handles the majority of the change in perspective. From there we need only to flip the resulting `MoveList` (which is done in BlackBoardState's `getValidMoves()`. This resulting list is called the `MoveList`'s "Evil twin".

## Check

Check was the entire reason that this piece of software needed to generate all possible moves for a side, rather than just the moves for a given piece. To know whether a piece can move, even with that piece isn't the king, we need to know all the moves the other side can make and whether the King is attacked. Check is defined as:

>The king is said to be 'in check' if it is attacked by one or more of the opponent's pieces, even if such pieces are constrained from moving to the square occupied by the king because they would then leave or place their own king in check. No piece can be moved that will either expose the king of the same color to check or leave that king in check. 

To do this we the list of all moves the opponent (Black or White depending on the move) can make next turn. We generate a bitboard that represents the occupancy of friendly pieces for each pseudo valid move, removing the piece from its current location and placing it at its end position. We use this (after mirroring it) as the foe bitboard in the calculation of the black moves which results in a bitboard of all possible opponent moves.

Actually checking for check is easy, we simply need to check whether the square that the friendly King is on at the end of its turn is attacked by the opponent. Based on this check, and moves are removed from the set of pseudo valid moves to obtain moves that are valid

Unfortunately because the moves the opponent can make next turn are dependent on the current move, we must calculate opponent moves for each possible pseudo valid move. This is computationally inefficient, and it should be possible to do this more efficiently.

## Issues

While technically all of our black box tests pass, due to the nature of our testing protocol (simply verifying that the ground truth move is within the list of valid moves we could also pass all of our tests simply by returning the list of all possible moves for a give piece. So while for the example shown in the beginning we return the correct results, there are a few issues outstanding:

- As previously stated, Castling appears relatively infrequently. While Castling moves will return for a King that is in the correct location on the board, no checks are currently done to ensure that the Rook is also in place, its castling move is uninhibited, and that none of the spaces this King must traverse are in check. As such a Castling move may appear in the results and not actually be valid. This problem lingers because it is the only type of move whose validity is dependent on board state rather than just peace state. It will probably be handled as part of the final filtering for check, but this isn't finalized

Overall I'm not sure the solution I came up with was the best one. I hadn't written in Java since high school, and I think that I could have organized my code differently if I better grasped how the language handles object orientation. I also might have developed more than I needed. This program really generates all valid moves for a side rather than focusing on just the peice in question. I did this because it made it easy to obtain the attacks and verify check, but there might have been a more elegant solution. Also my set method comes at the expense of performance. My inital list of moves is quite large even for relatively simple boards, but the use of Java Streams (my first experience) actually means that it runs pretty quickly. I could probably further optimize my use of Streams to improve performance.
