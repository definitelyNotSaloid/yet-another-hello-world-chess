package com.etu.yahwChess

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.pieces.RookPiece
import com.etu.yahwChess.view.MainMenu


class MainActivity : AppCompatActivity() {

    private lateinit var boardLayout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardLayout = findViewById<GridLayout>(R.id.board)
        val game = CurrentGame(boardLayout as GridLayout)

        game.boardContainer[Vector2dInt(0, 0)] = RookPiece(game.boardContainer, Player.BLACK)
        game.boardContainer[Vector2dInt(1, 1)] = RookPiece(game.boardContainer, Player.WHITE)
        game.boardContainer[Vector2dInt(2, 2)] = RookPiece(game.boardContainer, Player.BLACK)
        game.boardContainer[Vector2dInt(3, 3)] = RookPiece(game.boardContainer, Player.WHITE)


        //back to menu
        val backButton: ImageButton = findViewById(R.id.back_button1)

        backButton.setOnClickListener {
            openMenuActivity()
        }
    }

    // open menu activity window
    private fun openMenuActivity() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }
}