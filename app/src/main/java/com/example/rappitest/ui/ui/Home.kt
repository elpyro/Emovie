package com.example.rappitest.ui.ui

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rappitest.R


class Home : AppCompatActivity() {
    var canExitApp =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }


    override fun onBackPressed() {
        //super.onBackPressed();
        if (!canExitApp) {
            canExitApp = true
            Toast.makeText(this, getString(R.string.presioneDosVeces), Toast.LENGTH_SHORT)
                .show()
            Handler().postDelayed(Runnable { canExitApp = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }
}