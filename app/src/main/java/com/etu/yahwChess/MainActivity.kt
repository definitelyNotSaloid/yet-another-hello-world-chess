package com.etu.yahwChess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.misc.Vector2dInt
import com.etu.yahwChess.model.pieces.RookPiece
import com.etu.yahwChess.serialization.SerializableGameData
import com.etu.yahwChess.view.MainMenu
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream


class MainActivity : AppCompatActivity() {

    private lateinit var boardLayout: ViewGroup

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardLayout = findViewById<GridLayout>(R.id.board)

        val game = CurrentGame(boardLayout as GridLayout)

        val bundle: Bundle = intent.extras!!
        val gameStart = bundle.getBoolean("game_start")

        if (gameStart) {
            try {
                val input = applicationContext?.openFileInput("savedData.json")
                if (input != null) {
                    val data = game.json.decodeFromStream<SerializableGameData>(input)

                    game.loadData(data)
                }
            } catch (e: Exception) {
                // i miss silent fails
                game.boardContainer[Vector2dInt(0, 0)] =
                    RookPiece(game.boardContainer, Player.BLACK)
                game.boardContainer[Vector2dInt(1, 1)] =
                    RookPiece(game.boardContainer, Player.WHITE)
                game.boardContainer[Vector2dInt(2, 2)] =
                    RookPiece(game.boardContainer, Player.BLACK)
                game.boardContainer[Vector2dInt(3, 3)] =
                    RookPiece(game.boardContainer, Player.WHITE)
            }
        }
        else {

            game.boardContainer[Vector2dInt(0, 0)] =
                RookPiece(game.boardContainer, Player.BLACK)
            game.boardContainer[Vector2dInt(0, 7)] =
                RookPiece(game.boardContainer, Player.WHITE)
            game.boardContainer[Vector2dInt(7, 0)] =
                RookPiece(game.boardContainer, Player.BLACK)
            game.boardContainer[Vector2dInt(7, 7)] =
                RookPiece(game.boardContainer, Player.WHITE)
        }


        //back to menu
        val backButton: ImageButton = findViewById(R.id.back_button1)

        backButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.dialog_animation))
            dialogBuilder.setTitle("Back to menu")
                .setMessage("Do you want to save your game?")

                    //save the game
                .setPositiveButton("Save") { dialogBuilder, which ->
                    Toast.makeText(
                        applicationContext,
                        "GAME SAVED", Toast.LENGTH_SHORT
                    ).show()
                    val output = applicationContext?.openFileOutput("savedData.json", Context.MODE_PRIVATE)
                    output?.write(game.serialize().toByteArray())

                    openMenuActivity()
                }
                    //don't save the game
                .setNegativeButton("Don't save") { dialogBuilder, which ->
                    Toast.makeText(
                        applicationContext,
                        "GAME NOT SAVED", Toast.LENGTH_SHORT
                    ).show()

                    openMenuActivity()
                }
            dialogBuilder.show()
        }
    }

    // open menu activity window
    private fun openMenuActivity() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }
}