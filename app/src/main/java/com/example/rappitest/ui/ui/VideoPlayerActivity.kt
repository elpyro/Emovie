package com.example.rappitest.ui.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rappitest.databinding.ActivityVideoPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class VideoPlayerActivity : AppCompatActivity() {
    // id of the video which we are playing.
    private lateinit var binding: ActivityVideoPlayerBinding
    lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Muestra un video de youtube. Back end no proporciono trailer
        vistaVideo()

    }

    private fun vistaVideo() {
        youTubePlayerView = binding.videoPlayer

        // below line is to enter full screen mode.
        youTubePlayerView.enterFullScreen()
        youTubePlayerView.toggleFullScreen()


        //EL BACK-END NO PROPORCIONA EL TRAILER DE VIDEO, SE COLOCO UNO DE YOUTUBE DE EJEMPLO
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

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
    }
}