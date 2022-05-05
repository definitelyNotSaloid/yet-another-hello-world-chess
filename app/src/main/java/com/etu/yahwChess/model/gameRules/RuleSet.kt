package com.etu.yahwChess.model.gameRules

import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.misc.VectorPair

// why?
// implementing complex mechanics
// (eg pawn's en passant capture and promotion, castling, or dlc stuff like 3-check)
// is, well, a bit hard with piece class alone


// default
// TODO make RuleSet interface
class RuleSet() {
    // TODO private val board
    // RuleSet should be connected with board and/or piece mover to check for possible moves
    fun moveForceAllowed(from: Vector2dInt, to: Vector2dInt) : Sequence<VectorPair> {
        TODO("idk how to make it work honestly")
    }
    fun forceProhibited() : Sequence<VectorPair> {
        TODO("yeah, its gonna be fun")
    }
}