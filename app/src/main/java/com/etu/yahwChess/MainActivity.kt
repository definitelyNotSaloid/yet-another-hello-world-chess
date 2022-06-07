package com.etu.yahwChess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.etu.yahwChess.misc.CurrentGame
import com.etu.yahwChess.misc.GameState
import com.etu.yahwChess.misc.Player
import com.etu.yahwChess.model.board.container.BoardContainer
import com.etu.yahwChess.serialization.SerializableGameData
import com.etu.yahwChess.view.MainMenu
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream


@Suppress("OPT_IN_IS_NOT_ENABLED")
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
                game.boardContainer.emplacePiecesDefault()
            }
        }
        else {

            game.boardContainer.emplacePiecesDefault()
        }


        //back to menu
        val backButton: ImageButton = findViewById(R.id.back_button1)

        backButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.dialog_animation))
            dialogBuilder
                .setTitle("Back to menu")
                .setMessage("Do you want to save your game?")

                    //save the game
                .setPositiveButton("Save") { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        "GAME SAVED", Toast.LENGTH_SHORT
                    ).show()
                    val output = applicationContext?.openFileOutput("savedData.json", Context.MODE_PRIVATE)
                    output?.write(game.serialize().toByteArray())

                    openMenuActivity()
                }
                    //don't save the game
                .setNegativeButton("Don't save") { _, _ ->
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

    fun showWinnerAlert(state: GameState) {

        when (state) {
            GameState.BLACK_WINS -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("GAME OVER!")
                    .setMessage("Team BLACK won")
                    .setPositiveButton("MENU") {_, _ -> openMenuActivity()}
                    .show()
            }
            GameState.WHITE_WINS -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("GAME OVER!")
                    .setMessage("Team WHITE won")
                    .setPositiveButton("MENU") { _, _ -> openMenuActivity()}
                    .show()
            }
            GameState.DRAW-> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("GAME OVER!")
                    .setMessage("DRAW")
                    .setPositiveButton("MENU") { _, _ -> openMenuActivity()}
                    .show()
            }
        }
    }
}