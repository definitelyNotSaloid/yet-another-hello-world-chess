package com.etu.yahwChess

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.model.pieces.TestPiece
import com.etu.yahwChess.view.board.piece.CellView


class MainActivity : AppCompatActivity() {

    private lateinit var boardLayout: ViewGroup

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardLayout = findViewById<GridLayout>(R.id.board)
        val boardContainer = BoardContainer()
        val game = CurrentGame(boardContainer, boardLayout as GridLayout)
        boardContainer.game = game

        boardContainer[Vector2dInt(0,0)] = TestPiece(boardContainer)


//        piece = findViewById(R.id.bQueen)
//
//        // returns True if the listener has
//        // consumed the event, otherwise False.
//        piece.setOnTouchListener(onTouchListener())
    }

    @SuppressLint("ClickableViewAccessibility")
//    private fun onTouchListener(): View.OnTouchListener {
//        return View.OnTouchListener { view, event ->
//            // position information
//            // about the event by the user
//            val x = event.rawX.toInt()
//            val y = event.rawY.toInt()
//            // detecting user actions on moving
//            when (event.action and MotionEvent.ACTION_MASK) {
//                MotionEvent.ACTION_DOWN -> {
//                    val lParams = view.layoutParams as FrameLayout.LayoutParams
//                    xDelta = x - lParams.leftMargin
//                    yDelta = y - lParams.topMargin
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    // based on x and y coordinates (when moving image)
//                    // and image is placed with it.
//                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
//                    layoutParams.leftMargin = x - xDelta
//                    layoutParams.topMargin = y - yDelta
//                    layoutParams.rightMargin = 0
//                    layoutParams.bottomMargin = 0
//                    view.layoutParams = layoutParams
//                }
//            }
//            // reflect the changes on screen
//            boardLayout.invalidate()
//            true
//        }
//    }

    //error if click outside the board cos fun do nothing for now
}