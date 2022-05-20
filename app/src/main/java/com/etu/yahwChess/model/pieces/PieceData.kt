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

object BishopData : PieceData() {
    override val textureResId: Int
        get() = R.mipmap.ic_bishop
}

object QueenData : PieceData() {
    override val textureResId: Int
        get() = R.mipmap.ic_queen
}

object KnightData : PieceData() {
    override val textureResId: Int
        get() = R.mipmap.ic_knight
}

object KingData : PieceData() {
    override val textureResId: Int
        get() = R.mipmap.ic_king
}

object PawnData : PieceData() {
    override val textureResId: Int
        get() = R.mipmap.ic_pawn
}