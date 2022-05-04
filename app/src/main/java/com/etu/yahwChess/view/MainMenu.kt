package com.etu.yahwChess.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.etu.yahwChess.MainActivity
import com.etu.yahwChess.R

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val playButton: ImageButton = findViewById(R.id.imageButton)

        playButton.setOnClickListener {
            openMainActivity()
        }
    }

    // open main activity window(with the board)
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}