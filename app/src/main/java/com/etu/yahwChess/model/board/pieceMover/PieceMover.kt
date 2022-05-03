package com.etu.yahwChess.model.board.pieceMover

import android.util.Log
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.pieces.Piece

class PieceMover(val boardContainer: BoardContainer) {
    private var piece: Piece? = null
    private var piecePos = Vector2dInt(-1, -1)

    fun clickedAt(pos: Vector2dInt) {
        if (piece == null) {
            piece = boardContainer[pos]
            if (piece == null) {
                piecePos = Vector2dInt(-1, -1)
            }
            else {
                Log.println(Log.INFO, "PieceMover", "taken piece at $pos")
                piecePos = pos
            }
        }
        else {
            if (piecePos==pos) {
                piece = null
                piecePos = Vector2dInt(-1, -1)
                Log.println(Log.INFO, "PieceMover", "released piece by clicking on it again")
            }
            else {
                // TODO something on piece capture (eg show captured pieces)

                if (!piece!!.canMoveTo(pos)) {
                    Log.println(Log.INFO, "PieceMover", "released piece by clicking on invalid cell")
                }
                else {
                    boardContainer[pos] = piece
                    boardContainer[piecePos] = null
                    Log.println(Log.INFO, "PieceMover", "released piece at $pos")
                }

                piecePos = Vector2dInt(-1, -1)
                piece = null
            }
        }
    }
}