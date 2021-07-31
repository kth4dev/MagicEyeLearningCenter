package com.magiceye.learningcenter.ui.mycourse


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.helper.CollectionName
import kotlinx.android.synthetic.main.activity_video_view.*


class VideoViewActivity : AppCompatActivity() {


    private lateinit var filterUrl: String

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L


    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            playPlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    private fun playPlayer() {
        if (filterUrl != "") {
            initializePlayer(filterUrl)
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        filterUrl = intent.getStringExtra(CollectionName.bVideoUrl).toString()
        if (savedInstanceState!=null){
            playbackPosition = savedInstanceState.getLong("playbackPosition")
            currentWindow=savedInstanceState.getInt("currentWindow")
            playWhenReady=savedInstanceState.getBoolean("playWhenReady")
        }
        initRotation()
        playPlayer()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("playbackPosition",playbackPosition)
        outState.putInt("currentWindow",currentWindow)
        outState.putBoolean("playWhenReady",playWhenReady)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("MyTag", "onRestoreInstanceState")
        playbackPosition = savedInstanceState.getLong("playbackPosition")
        currentWindow=savedInstanceState.getInt("currentWindow")
        playWhenReady=savedInstanceState.getBoolean("playWhenReady")
    }
    private fun initRotation() {
        iv_rotation.setOnClickListener {
            requestedOrientation =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }

        }
    }

    private fun playbackStateListener() = object : Player.EventListener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> pb_player.visibility = View.VISIBLE
                ExoPlayer.STATE_BUFFERING -> pb_player.visibility = View.VISIBLE
                ExoPlayer.STATE_READY -> pb_player.visibility = View.GONE
                ExoPlayer.STATE_ENDED -> pb_player.visibility = View.GONE
            }

        }
    }

    private fun initializePlayer(url: String) {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.fromUri(Uri.parse(url))
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.addListener(playbackStateListener())
                exoPlayer.prepare()
                video_view.player = exoPlayer
            }
    }


    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            removeListener(playbackStateListener())
            release()
        }
        player = null
    }

}


