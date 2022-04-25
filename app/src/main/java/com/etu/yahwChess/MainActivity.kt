package com.etu.yahwChess

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.etu.yahwChess.view.board.piece.PieceView
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnTouchListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private var dx = 0f
    private var dy = 0f

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {

        if (view is PieceView) {
            return when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    view?.active=!view?.active
                    true
                }
                else -> false
            }
        }

        return true
    }
}

