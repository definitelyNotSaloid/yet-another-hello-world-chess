package com.etu.yahwChess.model.gameRules

import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.misc.VectorPair

// why?
// implementing complex mechanics
// (eg pawn's en passant capture and promotion, castling, or dlc stuff like 3-check)
// is, well, a bit hard with piece class alone

// migrate this somewhere else
enum class PlayerSide {
    NONE,
    WHITE,
    BLACK
}

// default
// TODO make RuleSet interface
class RuleSet() {
    // TODO private val board
    // RuleSet should be connected with board and/or piece mover to check for possible moves
    open fun moveForceAllowed(from: Vector2dInt, to: Vector2dInt) : Sequence<VectorPair> {
        TODO("idk how to make it work honestly")
    }
    open fun forceProhibited() : Sequence<VectorPair> {
        TODO("yeah, its gonna be fun")
    }

    // think about better name
    open fun invokeEventAfterMoveIfNeeded(from: Vector2dInt, to: Vector2dInt) {
        // stuff like pawn promotion

        TODO("implement")
    }

    open fun checkForVictory() : PlayerSide {
        TODO("implement")
    }
}