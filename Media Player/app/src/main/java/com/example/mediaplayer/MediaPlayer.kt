package com.example.mediaplayer

import android.widget.MediaController
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_media_player.*

class MediaPlayer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        videoView.setMediaController(MediaController(this,true))
        val path = "android.resource://$packageName/${R.raw.video}"
        videoView.setVideoURI(Uri.parse(path))
    }
}
