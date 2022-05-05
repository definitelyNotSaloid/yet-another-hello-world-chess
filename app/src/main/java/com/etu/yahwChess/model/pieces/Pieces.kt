package com.etu.yahwChess.model.pieces

import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer
import java.lang.RuntimeException

// btw we dont care about le tru chessers and call pawn piece as well
abstract class Piece(val board: BoardContainer, val color: Player) {
    private var prevRegisteredPos : Vector2dInt = Vector2dInt.OUT_OF_BOUNDS

    abstract val pieceData : PieceData

    init {
        val a = position            // updating position through getter
    }

    val position : Vector2dInt
        get() {
            if (prevRegisteredPos!=Vector2dInt.OUT_OF_BOUNDS
                && board[prevRegisteredPos] == this) {
                return prevRegisteredPos
            }

            for (i in 0..7) {
                for (j in 0..7){
                    if (board[Vector2dInt(i,j)] == this) {
                        prevRegisteredPos = Vector2dInt(i,j)
                        return prevRegisteredPos
                    }
                }
            }
            prevRegisteredPos = Vector2dInt.OUT_OF_BOUNDS
            return prevRegisteredPos
        }

    abstract fun possibleMoves() : Sequence<Vector2dInt>

    abstract fun canMoveTo(pos: Vector2dInt) : Boolean
}

// debug only
// all moves are possible
class TestPiece(board: BoardContainer, color: Player) : Piece(board, color) {
    override val pieceData: PieceData
        get() = TODO("Not yet implemented")

    override fun possibleMoves(): Sequence<Vector2dInt> {
        TODO("Not yet implemented")
    }

    override fun canMoveTo(pos: Vector2dInt): Boolean {
        TODO("Not yet implemented")
    }
}

class RookPiece(board: BoardContainer, color: Player) : Piece(board, color) {
    override val pieceData: PieceData
        get() = RookData

    override fun possibleMoves(): Sequence<Vector2dInt> {
        if (position == Vector2dInt.OUT_OF_BOUNDS)
            return sequence {  }

        return sequence {
            val upperLeft = Vector2dInt(0,0)
            val lowerRight = Vector2dInt(7,7)
            val startingPos = position
            var curPos : Vector2dInt

            // technically, its possible to avoid unnecessary comparisons here
            // TODO?

            for (direction in sequenceOf(
                Vector2dInt.EAST,
                Vector2dInt.WEST,
                Vector2dInt.NORTH,
                Vector2dInt.SOUTH)) {
                    curPos = startingPos + direction
                    while (curPos.withinRectangle(upperLeft, lowerRight)) {
                        yield(curPos)
                        curPos+=direction

                        if (board[curPos]!=null) {        // cant go past some piece
                            if (board[curPos]?.color != color)      // can jump at cell with piece of opposite color
                                yield(curPos)

                            break
                        }
                    }
            }
        }
    }

    override fun canMoveTo(pos: Vector2dInt): Boolean {
        if (this.position == Vector2dInt.OUT_OF_BOUNDS)
            return false

        if (pos.x != this.position.x && pos.y != this.position.y)
            return false

        if (this.position == pos)           // staying on the same cell is not a valid move
            return false

        val checkDirection = when {
            pos.x < this.position.x -> Vector2dInt.WEST
            pos.x > this.position.x -> Vector2dInt.EAST
            pos.y < this.position.y -> Vector2dInt.NORTH
            pos.y > this.position.y -> Vector2dInt.SOUTH

            else -> throw RuntimeException("this should be impossible")
        }

        var curPos = position + checkDirection
        while (pos.x != curPos.x) {
            if (board[curPos] != null)
                return false
            curPos+=checkDirection
        }

        // checking cell at destination
        if (board[curPos]?.color == this.color)
            return false

        return true
    }
}