package com.example.faithportal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faithportal.data.repository.BibleRepository
import kotlinx.coroutines.launch

class BibleVerseViewModel : ViewModel() {
    private val repository = BibleRepository()
    val bibleVerse = repository.bibleVerse

    fun getBibleVerse(book: String, chapter: Int, verse: Int) {
        viewModelScope.launch {
            repository.fetchBibleVerse(book, chapter, verse)
        }
    }

    fun getRandomBibleVerse() {
        viewModelScope.launch {
            repository.fetchRandomBibleVerse()
        }
    }
}