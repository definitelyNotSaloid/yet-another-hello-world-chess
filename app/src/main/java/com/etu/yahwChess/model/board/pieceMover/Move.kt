package com.etu.yahwChess.model.board.pieceMover

import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.pieces.Piece


// it will have to be implemented properly at some point, but for now, we dont really care about this class

sealed class Move {
    abstract fun applyTo(board: BoardContainer)
}

// just a normal piece move. Eg rook f6 takes bishop.
// For stuff like en passage, pawn push with promotion and castle implement other classes.
class SinglePieceMove(
    val from: Vector2dInt,
    val to: Vector2dInt
) : Move() {
    override fun applyTo(board: BoardContainer) {
        require(board[from] != null)
        { "there is no piece on board at $from" }

        require(board[from]!!.canMoveTo(to))
        { "move cant be applied to board (invalid move)" }

        board.move(from, to)
    }


    val captured : Piece? = null

    init {
        require(from.withinRectangle(Vector2dInt(0,0), Vector2dInt(7,7))
                && to.withinRectangle(Vector2dInt(0,0), Vector2dInt(7,7)))
        { "coordinates are out of board borders" }
    }
}