package com.etu.yahwChess.view.board.piece

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import java.net.URI

class PieceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    init {
        this.setBackgroundColor(Color.WHITE)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                active = !active
                true
            }
            else -> false
        }
    }
    var active : Boolean = false
        set(value) {
            field = value
            if (value)
                this.setBackgroundColor(Color.BLACK)
            else
                this.setBackgroundColor(Color.WHITE)
        }
}