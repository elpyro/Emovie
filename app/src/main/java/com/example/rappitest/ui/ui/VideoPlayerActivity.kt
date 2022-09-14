package com.example.rappitest.ui.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rappitest.databinding.ActivityVideoPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class VideoPlayerActivity : AppCompatActivity() {
    // id of the video which we are playing.
    private lateinit var binding: ActivityVideoPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val youTubePlayerView = binding.videoPlayer

        // here we are adding observer to our youtubeplayerview.
       // lifecycle.addObserver(youTubePlayerView)

        // below method will provides us the youtube player ui controller such
        // as to play and pause a video to forward a video and many more features.

        // below line is to enter full screen mode.
        youTubePlayerView.enterFullScreen()
        youTubePlayerView.toggleFullScreen()


        Toast.makeText(this, "El Back-End no proporciona inf. para el trailer \n ¡Video de demostación!",Toast.LENGTH_LONG).show()
        //TODO EL BACK-END NO PROPORCIONA EL TRAILER DE VIDEO, SE COLOCO UNO DE YOUTUBE DE EJEMPLO
        var videoYouTubeId = "9bwcu6rewSY"

        // adding listener for our youtube player view.
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video into the YouTube Player
                youTubePlayer.loadVideo(videoYouTubeId, 0F)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                // this method is called if video has ended,
                super.onStateChange(youTubePlayer, state)
            }
        })
    }
}