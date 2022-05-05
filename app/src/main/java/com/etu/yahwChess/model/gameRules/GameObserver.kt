package com.etu.yahwChess.model.gameRules

import android.graphics.Color
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.GameState
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt

// this guy checks if any player is winning each turn



class GameObserver(val game: CurrentGame) {
    fun softVictoryCheck() {
        TODO("implement")

        // TODO it should only check if given turn is a winning one, without parsing whole board
    }

    fun hardVictoryCheck() : GameState {
        var foundBlack = false
        var foundWhite = false

        for (i in 0..7) {
            for (j in 0..7) {
                val pos = Vector2dInt(i,j)
                if (game.boardContainer[pos]?.color == Player.BLACK)
                    foundBlack = true
                else if (game.boardContainer[pos]?.color == Player.WHITE)
                    foundWhite = true
            }
        }

        if (!foundBlack)
            return GameState.WHITE_WINS

        if (!foundWhite)
            return GameState.BLACK_WINS

        return GameState.PLAYING

    }
}