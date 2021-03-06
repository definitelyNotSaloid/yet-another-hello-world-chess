package com.etu.yahwChess.misc

import android.media.tv.TvInputService
import android.util.Log
import android.widget.GridLayout
import androidx.core.view.children
import com.etu.yahwChess.MainActivity
import com.etu.yahwChess.R
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.board.pieceMover.PieceMover
import com.etu.yahwChess.model.gameRules.GameObserver
import com.etu.yahwChess.model.pieces.KingPiece
import com.etu.yahwChess.model.pieces.PawnPiece
import com.etu.yahwChess.model.pieces.Piece
import com.etu.yahwChess.model.pieces.RookPiece
import com.etu.yahwChess.serialization.SerializableGameData
import com.etu.yahwChess.view.misc.BoardViewHelper
import com.etu.yahwChess.view.board.piece.CellView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class CurrentGame(val boardView : GridLayout, val mainActivity: MainActivity) {
    val boardContainer = BoardContainer(this)
    val boardViewHelper = BoardViewHelper(boardView)
    val pieceMover = PieceMover(this)
    val gameObserver = GameObserver(this)
    var turn: Player = Player.WHITE
        private set

    var state: GameState = GameState.PLAYING
    private set

    val json: Json by lazy { Json{
        prettyPrint = true
    } }

    init {
        for (child in boardView.children) {
            child as CellView
            child.game = this
        }
    }

    // TODO move somewhere else (?)
    // this is a proxy class after all
    fun passTurn() {
        for (i in 0..7) {
            for (j in 0..7) {
                val piece = boardContainer[Vector2dInt(i,j)]
                if (piece?.color == turn && piece is PawnPiece) {
                    piece.resetEnPassage()
                }

                else if (piece?.color != turn && piece is KingPiece && gameObserver.targetedBy(piece.position).any {it.color == turn}) {
                    boardViewHelper.getCellView(vectorToCellIndex(piece.position))
                        .setBackgroundResource(R.mipmap.king_in_danger)
                }
            }
        }


        turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE


        state = gameObserver.hardVictoryCheck()
        when (state) {        // TODO replace with soft check when it will be implemented
            GameState.BLACK_WINS -> {
                Log.println(Log.INFO, "GameObserver", "Registered BLACK victory")
            }
            GameState.WHITE_WINS -> {
                Log.println(Log.INFO, "GameObserver", "Registered WHITE victory")
            }
            GameState.DRAW -> Log.println(Log.INFO, "GameObserver", "Registered DRAW")
            else -> Log.println(Log.INFO, "GameObserver", "Game continues")

            // TODO some victory/draw visualisation
        }
        mainActivity.showWinnerAlert(boardView, state)

        Log.println(Log.INFO, "CurrentGame", "passed turn. Its $turn turn now")
    }

    fun serialize() : String {          // returns encoded SerializableGameData
        val data = SerializableGameData(
            boardContainer.serialize(),
            turn,
            state
        )

        return json.encodeToString(data)
    }

    fun loadData(data: SerializableGameData) {
        boardContainer.loadFromSerialized(data.boardListSerialized)
        turn = data.turn
        state = data.state

        for (i in 0..7) {
            for (j in 0..7) {
                boardContainer[Vector2dInt(i,j)]?.linkBoard(boardContainer)
            }
        }
    }
}
