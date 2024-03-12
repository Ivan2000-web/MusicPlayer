package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //Variables
    var startTime = 0.0
    var finalTTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0 // first time for playing

    //Handler
    var handler: Handler = Handler()

    //Media Player
    var mediaPlayer = MediaPlayer()
    lateinit var time_txt: TextView
    lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val play_btn : Button = findViewById(R.id.play_btn)
        val pause_btn : Button = findViewById(R.id.pause_btn)
        val forward_btn : Button = findViewById(R.id.forward_btn)
        val back_btn : Button = findViewById(R.id.back_btn)

        val title_text : TextView = findViewById(R.id.song_title)
        time_txt = findViewById(R.id.time_left_text)

        seekBar = findViewById(R.id.seek_bar)

        // Media Player
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.club_music_50
        )

        seekBar.isClickable = false

        //Adding functionalities for the buttons
        //Start button
        play_btn.setOnClickListener() {
            mediaPlayer.start()

            finalTTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                seekBar.max = finalTTime.toInt()
                oneTimeOnly = 1
            }

            time_txt.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime, 100)
        }

        //Setting the music title
        title_text.text = "" + resources.getResourceEntryName(R.raw.club_music_50)

        //Pause button
        pause_btn.setOnClickListener(){
            mediaPlayer.pause()
        }

        //Forward button 10 sec
        forward_btn.setOnClickListener() {
            val temp = startTime
            if((temp + forwardTime) <= finalTTime) {
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else {
                Toast.makeText(this, "Can not Jump forward", Toast.LENGTH_LONG).show()
            }
        }

        //Back button 10 sec
        back_btn.setOnClickListener() {
            val temp = startTime.toInt()
            if((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else {
                Toast.makeText(this, "Can not Jump backward", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Creating the Runnable
    //Update media playback time and seekBar progress at a specified time interval.
    val UpdateSongTime: Runnable = object  : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            time_txt.text = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))
            )

                seekBar.progress = startTime.toInt()
                handler.postDelayed(this, 100)
        }
    }

}