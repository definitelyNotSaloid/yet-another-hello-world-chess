package com.etu.yahwChess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.KeyEvent
import android.widget.GridLayout
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

    private lateinit var game: CurrentGame

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardLayout = findViewById<GridLayout>(R.id.board)
        game = CurrentGame(boardLayout as GridLayout)

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
        } else {
            game.boardContainer.emplacePiecesDefault()
        }

    }

    override fun onBackPressed() {
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
                val output =
                    applicationContext?.openFileOutput("savedData.json", Context.MODE_PRIVATE)
                output?.write(game.serialize().toByteArray())

                openMenuActivity()
                finish()
            }
            //don't save the game
            .setNegativeButton("Don't save") { _, _ ->
                Toast.makeText(
                    applicationContext,
                    "GAME NOT SAVED", Toast.LENGTH_SHORT
                ).show()

                openMenuActivity()
                finish()
            }
        dialogBuilder.show()
    }

    // open menu activity window
    private fun openMenuActivity() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }

    fun showWinnerAlert(view: View, state: GameState) {
        val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.dialog_animation))

        when (state) {
            GameState.BLACK_WINS -> {
                dialogBuilder
                    .setTitle("GAME OVER!")
                    .setMessage("Team BLACK won")
                    .setPositiveButton("MENU") { _, _ ->
                        run {
                            openMenuActivity()
                            finish()
                        }
                    }
                    .show()
            }
            GameState.WHITE_WINS -> {
                dialogBuilder
                    .setTitle("GAME OVER!")
                    .setMessage("Team WHITE won")
                    .setPositiveButton("MENU") { _, _ ->
                        run {
                            openMenuActivity()
                            finish()
                        }
                    }
                    .show()
            }
            GameState.DRAW -> {
                dialogBuilder
                    .setTitle("GAME OVER!")
                    .setMessage("DRAW")
                    .setPositiveButton("MENU") { _, _ ->
                        run {
                            openMenuActivity()
                            finish()
                        }
                    }
                    .show()
            }
        }
    }
}