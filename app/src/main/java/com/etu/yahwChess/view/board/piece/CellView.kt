package com.etu.yahwChess.view.board.piece

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.etu.yahwChess.R
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.cellIndexToVector
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.pieces.Piece

class CellView constructor(
    context: Context, attrs: AttributeSet?
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
//    private var xDelta = 0
//    private var yDelta = 0

    lateinit var board : BoardContainer

    companion object {
        // TODO piece with this filter looks almost fine, but colors are a bit too sharp. Try and play with filter to make it look better
        val REVERSE_COLOR_FILTER = floatArrayOf(
            -1.0f,  0f,     0f,     0f,     255f, // red
            0f,     -1.0f,  0f,     0f,     255f, // green
            0f,     0f,     -1.0f,  0f,     255f, // blue
            0f,     0f,     0f,     1.0f,   0f  // alpha
        )
    }

    var piece : Piece?      = null
        set(value) {
            field = value
            if (value==null) {
                setImageResource(R.mipmap.transparent_background)
            }

            else {
                setImageResource(value.pieceData.textureResId)


                if (value.color == Player.BLACK)
                    colorFilter = ColorMatrixColorFilter(REVERSE_COLOR_FILTER)
                else
                    clearColorFilter()
            }
        }


    val cellIndex : String = resources.getResourceName(id).substringAfter("com.etu.yahwChess:id/").substring(0,2)


    override fun performClick(): Boolean {
        board.game.pieceMover.clickedAt(cellIndexToVector(cellIndex))
        Log.println(Log.INFO, "CellView", "touched $cellIndex")
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
            // position information
            // about the event by the user
//            val x = event!!.rawX.toInt()
//            val y = event.rawY.toInt()
            // detecting user actions on moving
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
//                    dragHelper.notifyStartedDrag(this)
//                    val lParams = layoutParams as FrameLayout.LayoutParams
//                    xDelta = x - lParams.leftMargin
//                    yDelta = y - lParams.topMargin
                }
//                MotionEvent.ACTION_MOVE -> {
//                    // based on x and y coordinates (when moving image)
//                    // and image is placed with it.
//                    val layoutParams1 = layoutParams as FrameLayout.LayoutParams
//                    layoutParams1.leftMargin = x - xDelta
//                    layoutParams1.topMargin = y - yDelta
//                    layoutParams1.rightMargin = 0
//                    layoutParams1.bottomMargin = 0
//                    layoutParams = layoutParams1
//                }
                MotionEvent.ACTION_UP -> {
                    performClick()
                }
            }
            // reflect the changes on screen
            this.invalidate()
            return true
    }
}