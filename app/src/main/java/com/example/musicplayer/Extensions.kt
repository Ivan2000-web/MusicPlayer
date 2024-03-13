package com.example.musicplayer

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}