package com.etu.yahwChess.model.board.pieceMover

import android.text.BoringLayout
import android.util.Log
import android.widget.ImageView
import com.etu.yahwChess.R
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.misc.vectorToCellIndex
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.pieces.Piece
import com.etu.yahwChess.model.pieces.PieceData

class PieceMover(val game: CurrentGame) {
    private var piece: Piece? = null
    private var piecePos = Vector2dInt.OUT_OF_BOUNDS

    private val boardContainer = game.boardContainer
    private lateinit var firstPieceTouchedPos: Vector2dInt
    private var firstPieceTouched: Piece? = null

    var lastMove: Move? = null
        private set

    fun clickedAt(pos: Vector2dInt) {
        if (piece == null) {
            // if picked current players piece
            if (boardContainer[pos]?.color == game.turn) {
                piece = boardContainer[pos]
                piecePos = pos

                firstPieceTouchedPos = piecePos
                firstPieceTouched = piece

                setBackgroundToPossibleMoves(piece, true)
                setBackgroundToTouchedPiece(firstPieceTouchedPos, true)
                setBackgroundToPossibleEatenPawn(piece)

                Log.println(Log.INFO, "PieceMover", "taken piece at $pos")
            }
        } else {
            // Drop piece by doubleclicking
            if (piecePos == pos) {

                setBackgroundToPossibleMoves(piece, false)
                setBackgroundToTouchedPiece(firstPieceTouchedPos, false)

                piece = null
                piecePos = Vector2dInt.OUT_OF_BOUNDS
                Log.println(Log.INFO, "PieceMover", "released piece by clicking on it again")
                return
            }

            // Pick new piece of the same color
            if (boardContainer[pos]?.color == piece!!.color) {
                piecePos = pos
                piece = boardContainer[pos]!!           // assert null just in case

                setBackgroundToPossibleMoves(firstPieceTouched, false)
                setBackgroundToPossibleMoves(piece, true)
                setBackgroundToPossibleEatenPawn(piece)

                setBackgroundToTouchedPiece(firstPieceTouchedPos, false)
                setBackgroundToTouchedPiece(piecePos, true)

                firstPieceTouchedPos = piecePos
                firstPieceTouched = piece

                Log.println(
                    Log.INFO,
                    "PieceMover",
                    "picked new piece at $pos by clicking on piece of same color"
                )
                return
            }

            // Release piece by clicking on VALID cell
            // Turn ends here
            if (piece!!.canMoveTo(pos)) {
                boardContainer[pos] = piece
                boardContainer[piecePos] = null

                for (i in 0..7) {
                    for (j in 0..7) {
                        game.boardViewHelper.getCellView(vectorToCellIndex(Vector2dInt(i, j)))
                            .setBackgroundResource(R.mipmap.transparent_background)
                    }
                }

                Log.println(Log.INFO, "PieceMover", "released piece at $pos")

                lastMove = SinglePieceMove(piecePos, pos)
                game.passTurn()

                piece = null
                piecePos = Vector2dInt.OUT_OF_BOUNDS
                return
            }

            // Default case - invalid cell
            setBackgroundToPossibleMoves(piece, false)
            setBackgroundToTouchedPiece(firstPieceTouchedPos, false)

            Log.println(Log.INFO, "PieceMover", "released piece by clicking on invalid cell")
            piecePos = Vector2dInt(-1, -1)
            piece = null

        }
    }

    //highlight possible moves
    private fun setBackgroundToPossibleMoves(piece: Piece?, show: Boolean) {
        if (show) {
            piece!!.possibleMoves().forEach {
                game.boardViewHelper.getCellView(vectorToCellIndex(it))
                    .setBackgroundResource(R.mipmap.possible_moves)
            }
        } else {
            piece!!.possibleMoves().forEach {
                game.boardViewHelper.getCellView(vectorToCellIndex(it))
                    .setBackgroundResource(R.mipmap.transparent_background)
            }
        }
    }

    //highlight clicked pawn
    private fun setBackgroundToTouchedPiece(piecePos: Vector2dInt, show: Boolean) {
        if (show) {
            game.boardViewHelper.getCellView(vectorToCellIndex(piecePos))
                .setBackgroundResource(R.mipmap.selection_background)
        } else {
            game.boardViewHelper.getCellView(vectorToCellIndex(piecePos))
                .setBackgroundResource(R.mipmap.transparent_background)
        }
    }

    //highlight a pawn that can be eaten
    private fun setBackgroundToPossibleEatenPawn(piece: Piece?) {
            piece!!.possibleMoves().forEach {
                if (boardContainer[it]?.color != piece.color && boardContainer[it] != null) {
                    game.boardViewHelper.getCellView(vectorToCellIndex(it))
                        .setBackgroundResource(R.mipmap.eat)
                }
            }
    }
}