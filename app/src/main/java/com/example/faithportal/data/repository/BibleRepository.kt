package com.example.faithportal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faithportal.model.BibleVerse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class BibleRepository {
    private val bibleApi: BibleApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/gh/wldeh/bible-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bibleApi = retrofit.create(BibleApiService::class.java)
    }

    private val _bibleVerse = MutableLiveData<BibleVerse>()
    val bibleVerse: LiveData<BibleVerse> get() = _bibleVerse

    suspend fun fetchBibleVerse(book: String, chapter: Int, verse: Int) {
        val response = bibleApi.getBibleVerse(book, chapter, verse)
        _bibleVerse.postValue(response)
    }

    suspend fun fetchRandomBibleVerse() {
        val books = listOf("genesis", "exodus", "leviticus", "numbers", "deuteronomy") // Add more books as needed
        val book = books.random()
        val chapter = Random.nextInt(1, 50) // Adjust the range as needed
        val verse = Random.nextInt(1, 30) // Adjust the range as needed

        withContext(Dispatchers.IO) {
            fetchBibleVerse(book, chapter, verse)
        }
    }
}