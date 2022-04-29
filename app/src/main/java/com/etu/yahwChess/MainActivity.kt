package com.etu.yahwChess

import android.annotation.SuppressLint
import android.icu.text.RelativeDateTimeFormatter
import android.os.Bundle
import android.view.FrameMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.etu.yahwChess.misc.Coordinates


class MainActivity : AppCompatActivity() {

    private lateinit var boardLayout: ViewGroup
    private lateinit var piece: ImageView

    private var xDelta = 0
    private var yDelta = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardLayout = findViewById(R.id.activity_main)
        piece = findViewById(R.id.bQueen)

        // returns True if the listener has
        // consumed the event, otherwise False.
        piece.setOnTouchListener(onTouchListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { view, event ->
            // position information
            // about the event by the user
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            // detecting user actions on moving
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = view.layoutParams as FrameLayout.LayoutParams
                    xDelta = x - lParams.leftMargin
                    yDelta = y - lParams.topMargin
                }
                MotionEvent.ACTION_MOVE -> {
                    // based on x and y coordinates (when moving image)
                    // and image is placed with it.
                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta
                    layoutParams.rightMargin = 0
                    layoutParams.bottomMargin = 0
                    view.layoutParams = layoutParams
                }
            }
            // reflect the changes on screen
            boardLayout.invalidate()
            true
        }
    }

    //error if click outside the board cos fun do nothing for now
    fun onClick(view: View) {
        TODO("when(view.getId())" +
                "R.id.R## -> position = new Coordinates(0, 0)" +

                "R.id.R## -> { position.setX(1)" +
                "              position.setY(0) }" +

                "R.id.R## -> { position.setX(2)" +
                             "position.seY(0)" )
    }


}