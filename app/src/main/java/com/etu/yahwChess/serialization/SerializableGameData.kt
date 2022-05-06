package com.etu.yahwChess.serialization

import com.etu.yahwChess.misc.GameState
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.model.pieces.Piece
import com.etu.yahwChess.model.pieces.PieceData
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
class SerializableGameData(
    val boardListSerialized: String,
    val turn: Player,
    val state: GameState)
