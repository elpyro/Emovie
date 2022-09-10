package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.rappitest.R
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView


/*
Created by Devendra Chavan
*/

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var backgroundImg: ShimmerTextView =findViewById(R.id.shimmer_logo)
        val shimmer = Shimmer()
        shimmer.start<ShimmerTextView>(backgroundImg)

        val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.slide)
        backgroundImg.startAnimation(sideAnimation)
        Handler().postDelayed({
            startActivity(Intent(this, Home::class.java))
            finish()
        },1000)

    }
}