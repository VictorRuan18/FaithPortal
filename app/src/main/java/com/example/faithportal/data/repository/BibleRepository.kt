package com.example.faithportal.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faithportal.model.BibleVerse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
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

    suspend fun fetchBibleVerse(version: String, book: String, chapter: Int, verse: Int) {
        try {
            val response = bibleApi.getBibleVerse(version, book, chapter, verse)
            _bibleVerse.postValue(response)
        } catch (e: HttpException) {
            Log.e("BibleRepository", "HTTP error: ${e.code()}")
        } catch (e: Exception) {
            Log.e("BibleRepository", "Error: ${e.message}")
        }
    }

    suspend fun fetchRandomBibleVerse() {
        val books = listOf("genesis", "exodus", "leviticus", "numbers", "deuteronomy") // Add more books as needed
        val book = books.random()
        val chapter = Random.nextInt(1, 50) // Adjust the range as needed
        val verse = Random.nextInt(1, 30) // Adjust the range as needed

        withContext(Dispatchers.IO) {
            fetchBibleVerse("en-asv", book, chapter, verse) // Use a default version like "en-asv"
        }
    }
}