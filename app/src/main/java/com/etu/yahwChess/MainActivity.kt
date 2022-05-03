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
import com.etu.yahwChess.model.pieces.RookPiece
import com.etu.yahwChess.model.pieces.TestPiece
import com.etu.yahwChess.view.board.piece.CellView


class MainActivity : AppCompatActivity() {

    private lateinit var boardLayout: ViewGroup

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardLayout = findViewById<GridLayout>(R.id.board)
        val game = CurrentGame(boardLayout as GridLayout)

        game.boardContainer[Vector2dInt(0, 0)] = RookPiece(game.boardContainer)
        game.boardContainer[Vector2dInt(3, 3)] = RookPiece(game.boardContainer)
    }
}