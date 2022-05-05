package com.etu.yahwChess.misc

import android.util.Log
import android.widget.GridLayout
import androidx.core.view.children
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.board.pieceMover.PieceMover
import com.etu.yahwChess.model.gameRules.GameObserver
import com.etu.yahwChess.view.misc.BoardViewHelper
import com.etu.yahwChess.view.board.piece.CellView

class CurrentGame(val boardView : GridLayout) {
    val boardContainer = BoardContainer(this)
    val boardViewHelper = BoardViewHelper(boardView)
    val pieceMover = PieceMover(boardContainer, this)
    val gameObserver = GameObserver(this)

    var turn: Player        = Player.WHITE
        private set

    init {
        for (child in boardView.children) {
            child as CellView
            child.board = boardContainer
        }
    }

    fun passTurn() {
        when (gameObserver.hardVictoryCheck()) {        // TODO replace with soft check when it will be implemented
            GameState.BLACK_WINS -> Log.println(Log.INFO, "GameObserver", "Registered BLACK victory")
            GameState.WHITE_WINS -> Log.println(Log.INFO, "GameObserver", "Registered WHITE victory")
            GameState.DRAW -> Log.println(Log.INFO, "GameObserver", "Registered DRAW")
            else -> Log.println(Log.INFO, "GameObserver", "Game continues")

            // TODO some victory/draw visualisation
        }


        turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
        Log.println(Log.INFO, "CurrentGame", "passed turn. Its $turn turn now")
    }
}
