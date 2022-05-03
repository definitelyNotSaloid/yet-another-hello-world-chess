package com.etu.yahwChess.model.pieces

import android.graphics.drawable.Drawable
import com.etu.yahwChess.R

sealed class PieceData {
    abstract val textureResId : Int
}

object RookData : PieceData() {
    override val textureResId: Int
        get() = R.mipmap.ic_rook
}