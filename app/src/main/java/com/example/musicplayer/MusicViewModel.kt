package com.example.musicplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {
    private val _trackName = MutableLiveData<String>()
    val trackName: LiveData<String> get() = _trackName

    fun setTrackName(name: String) {
        _trackName.value = name
    }
}
