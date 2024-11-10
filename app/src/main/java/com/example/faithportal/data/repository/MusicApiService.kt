package com.example.faithportal.data.repository


import com.example.faithportal.model.SpotifyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("/v1/search")
    suspend fun getWorshipMusic(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): SpotifyResponse
}