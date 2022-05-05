package com.etu.yahwChess.model.board.pieceMover

import android.util.Log
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.pieces.Piece

class PieceMover(val boardContainer: BoardContainer,val game: CurrentGame) {
    private var piece: Piece? = null
    private var piecePos = Vector2dInt.OUT_OF_BOUNDS

    var lastMove : Move? = null
        private set

    fun clickedAt(pos: Vector2dInt) {
        if (piece == null) {
            // if picked current players piece
            if (boardContainer[pos]?.color == game.turn) {
                piece = boardContainer[pos]
                piecePos = pos
                Log.println(Log.INFO, "PieceMover", "taken piece at $pos")
            }
        }
        else {
            // Drop piece by doubleckicking
            if (piecePos==pos) {
                piece = null
                piecePos = Vector2dInt.OUT_OF_BOUNDS
                Log.println(Log.INFO, "PieceMover", "released piece by clicking on it again")
                return
            }

            // Pick new piece of the same color
            if (boardContainer[pos]?.color == piece!!.color) {
                piecePos = pos
                piece = boardContainer[pos]!!           // assert null just in case
                Log.println(Log.INFO, "PieceMover", "picked new piece at $pos by clicking on piece of same color")
                return
            }

            // Release piece by clicking on VALID cell
            // Turn ends here
            if (piece!!.canMoveTo(pos)) {
                boardContainer[pos] = piece
                boardContainer[piecePos] = null
                Log.println(Log.INFO, "PieceMover", "released piece at $pos")

                lastMove = SinglePieceMove(piecePos, pos)
                game.passTurn()

                piece = null
                piecePos = Vector2dInt.OUT_OF_BOUNDS
                return
            }

            // Default case - invalid cell
            Log.println(Log.INFO, "PieceMover", "released piece by clicking on invalid cell")
            piecePos = Vector2dInt(-1, -1)
            piece = null

        }
    }
}