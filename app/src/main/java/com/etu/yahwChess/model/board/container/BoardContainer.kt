package com.etu.yahwChess.model.board.container

import android.view.View
import android.widget.ImageView
import com.etu.yahwChess.R
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.misc.vectorToCellIndex
import com.etu.yahwChess.model.pieces.Piece
import kotlinx.android.synthetic.main.activity_main.*

// Its just a data container
// Any logic besides moving stuff should be implemented in PieceMover class
class BoardContainer {
    lateinit var game: CurrentGame

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

    fun move(from: Vector2dInt, to: Vector2dInt) {      // if at to-pos there was some piece, it will be replaced
        TODO()
    }
    fun tryPlaceAt(piece: Piece, pos: Vector2dInt) : Boolean {      // returns true and places piece if cell was empty, returns false and does nothing otherwise
        TODO()
    }
}