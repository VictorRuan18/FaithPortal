package com.example.faithportal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faithportal.data.repository.BibleRepository
import kotlinx.coroutines.launch

class BibleVerseViewModel : ViewModel() {
    private val repository = BibleRepository()
    val bibleVerse = repository.bibleVerse

    fun getRandomBibleVerse() {
        viewModelScope.launch {
            repository.fetchRandomBibleVerse()
        }
    }
}