package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.TimeUnit

// MainActivity class
class MainActivity : AppCompatActivity() {

    // Initializing the ViewModel
    private lateinit var viewModel: MusicViewModel

    // Array of songs
    private val songs = arrayOf(R.raw.club_music_50, R.raw.rock_music_60)
    // Index to keep track of the current song
    private var currentSongIndex = 0

    // Variables to manage the song timing and progression
    private var startTime = 0.0
    private var finalTTime = 0.0
    private val forwardTime = 10000
    private val backwardTime = 10000
    private var oneTimeOnly = 0

    // Handler to manage delayed tasks
    private val handler = Handler()

    // MediaPlayer to play the songs
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var time_txt: TextView
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate the ViewModel
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)

        val title_text : TextView = findViewById(R.id.song_title)
        viewModel.trackName.observe(this, Observer { name ->
            title_text.text = name
        })

        val play_btn : Button = findViewById(R.id.play_btn)
        val pause_btn : Button = findViewById(R.id.pause_btn)
        val forward_btn : Button = findViewById(R.id.forward_btn)
        val back_btn : Button = findViewById(R.id.back_btn)
        val next_btn: Button = findViewById(R.id.next_btn)
        val previous_btn: Button = findViewById(R.id.previous_btn)

        time_txt = findViewById(R.id.time_left_text)
        seekBar = findViewById(R.id.seek_bar)

        // Initialize the MediaPlayer with the first song
        initializeMediaPlayer()

        seekBar.isClickable = false

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

            handler.postDelayed(updateSongTime(), 100)
        }

        pause_btn.setOnClickListener(){
            mediaPlayer.pause()
        }

        forward_btn.setOnClickListener() {
            val temp = startTime
            if((temp + forwardTime) <= finalTTime) {
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else {
                Toast.makeText(this, "Can not Jump forward", Toast.LENGTH_LONG).show()
            }
        }

        back_btn.setOnClickListener() {
            val temp = startTime.toInt()
            if((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else {
                Toast.makeText(this, "Can not Jump backward", Toast.LENGTH_LONG).show()
            }
        }

        next_btn.setOnClickListener() {
            changeSong(1)
        }

        previous_btn.setOnClickListener() {
            changeSong(-1)
        }
    }

    // Function to initialize the MediaPlayer and set the track name in the ViewModel
    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex])
        viewModel.setTrackName("" + resources.getResourceEntryName(songs[currentSongIndex]))
    }

    // Function to change the current song and update the MediaPlayer
    private fun changeSong(direction: Int) {
        currentSongIndex += direction
        if (currentSongIndex < 0) {
            currentSongIndex = songs.size - 1
        } else if (currentSongIndex >= songs.size) {
            currentSongIndex = 0
        }
        mediaPlayer.stop()
        mediaPlayer.release()
        initializeMediaPlayer()
        mediaPlayer.start()
    }

    // Function to return a Runnable that updates the song time and SeekBar progress
    private fun updateSongTime(): Runnable = object : Runnable {
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
