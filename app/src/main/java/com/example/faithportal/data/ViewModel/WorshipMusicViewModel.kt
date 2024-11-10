package com.example.faithportal.data.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faithportal.data.repository.MusicRepository
import kotlinx.coroutines.launch

class WorshipMusicViewModel() : ViewModel() {
    private val repository = MusicRepository()
    val worshipMusic = repository.worshipMusic

    fun retrieveWorshipMusic() {
        viewModelScope.launch {
            repository.fetchWorshipMusic()
        }
    }
}