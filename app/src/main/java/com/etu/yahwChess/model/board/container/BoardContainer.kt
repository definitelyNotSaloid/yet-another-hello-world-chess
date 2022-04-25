package com.etu.yahwChess.model.board.container

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.etu.yahwChess.R
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.pieces.Piece
import kotlinx.android.synthetic.main.activity_main.*

// Its just a data container
// Any logic besides moving stuff should be implemented in PieceMover class
class BoardContainer {
    operator fun get(pos: Vector2dInt) : Piece? {
        TODO("implement")
    }
    operator fun set(pos: Vector2dInt, piece: Piece?) {     // force-set (or clear) cell content
        TODO()
    }

    fun move(from: Vector2dInt, to: Vector2dInt) {      // if at to-pos there was some piece, it will be replaced
        TODO()
    }
    fun tryPlaceAt(piece: Piece, pos: Vector2dInt) : Boolean {      // returns true and places piece if cell was empty, returns false and does nothing otherwise
        TODO()
    }

    fun testFun(){
    }
}