package com.example.faithportal.data.repository

import com.example.faithportal.model.BibleVerse
import retrofit2.http.GET
import retrofit2.http.Path

interface BibleApiService {
    @GET("bibles/en-niv/books/{book}/chapters/{chapter}/verses/{verse}.json")
    suspend fun getBibleVerse(
        @Path("book") book: String,
        @Path("chapter") chapter: Int,
        @Path("verse") verse: Int
    ): BibleVerse
}