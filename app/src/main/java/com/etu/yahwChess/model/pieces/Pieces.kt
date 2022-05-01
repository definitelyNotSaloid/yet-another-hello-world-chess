package com.etu.yahwChess.model.pieces

import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer

// btw we dont care about le tru chessers and call pawn piece as well
abstract class Piece(val board: BoardContainer) {
    init {
        //TODO("put this piece on the board. cant be done until BoardContainer is implemented")
    }

    val position : Vector2dInt
        get() {
            for (i in 0..7) {
                for (j in 0..7){
                    if (board[Vector2dInt(i,j)] == this)
                        return Vector2dInt(i,j)
                }
            }

            return Vector2dInt(-1,-1)
        }

    abstract fun possibleMoves() : Sequence<Vector2dInt>
    // is it really needed? Useful for pieces like knight or pawn, but imagine using this method with queen
    // and yeah, is it relative or absolute?
    // First is easier to override (independent of game, board and material world)
    // Second, on the other hand, is easier for processing

    abstract fun canMoveTo(pos: Vector2dInt) : Boolean
    // and yet again, relative or absolute?
}

// debug only
// all moves are possible
class TestPiece(board: BoardContainer) : Piece(board) {
    override fun possibleMoves(): Sequence<Vector2dInt> {
        TODO("Not yet implemented")
    }

    override fun canMoveTo(pos: Vector2dInt): Boolean {
        TODO("Not yet implemented")
    }
}