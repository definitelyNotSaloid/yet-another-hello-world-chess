package com.etu.yahwChess.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.etu.yahwChess.R
import com.etu.yahwChess.MainActivity

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        var gameStart: Boolean = false

        val newGameButton: ImageButton = findViewById(R.id.newGameBtn)
        //NEW GAME
        newGameButton.setOnClickListener() {
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game_start", gameStart)
            startActivity(intent)
            finish()
        }

        val loadGameButton: ImageButton = findViewById(R.id.loadGameBtn)
        //LOAD GAME
        loadGameButton.setOnClickListener() {
            gameStart = true
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game_start", gameStart)
            startActivity(intent)
            finish()
        }
    }


    /*// open main activity window(with the board)
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }*/
}