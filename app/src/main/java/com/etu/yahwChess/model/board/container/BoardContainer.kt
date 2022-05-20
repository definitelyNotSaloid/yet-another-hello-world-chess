package com.etu.yahwChess.model.board.container

import android.view.View
import android.widget.ImageView
import com.etu.yahwChess.R
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.misc.vectorToCellIndex
import com.etu.yahwChess.model.pieces.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Its just a data container
// Any logic besides moving stuff should be implemented in PieceMover class
class BoardContainer(val game: CurrentGame) {
    // Important: upper left corner is [0][0], lower right is [7][7]
    // and A1 is lower left, so A1 = [7][0]
    private val grid =  arrayListOf<ArrayList<Piece?>>(
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
        arrayListOf(null, null, null, null, null, null, null, null),
    )

    operator fun get(pos: Vector2dInt) : Piece? {
        return grid[pos.y][pos.x]
    }
    operator fun set(pos: Vector2dInt, piece: Piece?) {     // force-set (or clear) cell content
        grid[pos.y][pos.x] = piece
        game.boardViewHelper.getCellView(vectorToCellIndex(pos)).piece = piece
    }

    fun emplacePiecesDefault() {
        for (x in 0..7) {
            this[Vector2dInt(x,1)] = PawnPiece(this, Player.BLACK)
            this[Vector2dInt(x,6)] = PawnPiece(this, Player.WHITE)
        }

        this[Vector2dInt(0,0)] = RookPiece(this, Player.BLACK)
        this[Vector2dInt(7,0)] = RookPiece(this, Player.BLACK)
        this[Vector2dInt(0,7)] = RookPiece(this, Player.WHITE)
        this[Vector2dInt(7,7)] = RookPiece(this, Player.WHITE)

        this[Vector2dInt(1,0)] = KnightPiece(this, Player.BLACK)
        this[Vector2dInt(6,0)] = KnightPiece(this, Player.BLACK)
        this[Vector2dInt(1,7)] = KnightPiece(this, Player.WHITE)
        this[Vector2dInt(6,7)] = KnightPiece(this, Player.WHITE)

        this[Vector2dInt(2,0)] = BishopPiece(this, Player.BLACK)
        this[Vector2dInt(5,0)] = BishopPiece(this, Player.BLACK)
        this[Vector2dInt(2,7)] = BishopPiece(this, Player.WHITE)
        this[Vector2dInt(5,7)] = BishopPiece(this, Player.WHITE)

        this[Vector2dInt(3, 0)] = QueenPiece(this, Player.BLACK)
        this[Vector2dInt(3, 7)] = QueenPiece(this, Player.WHITE)

        this[Vector2dInt(4, 0)] = KingPiece(this, Player.BLACK)
        this[Vector2dInt(4, 7)] = KingPiece(this, Player.WHITE)
    }

    fun loadFromSerialized(serialized: String) {
        val list = game.json.decodeFromString<List<Piece?>>(serialized)

        require(list.size == 64)

        for (i in 0..63) {
                grid[i/8][i%8] = list[i]
                game.boardViewHelper.getCellView(vectorToCellIndex(Vector2dInt(i%8, i/8))).piece = grid[i/8][i%8]
        }
    }

    fun serialize() : String {
        return game.json.encodeToString(this.grid.flatten())
    }
}