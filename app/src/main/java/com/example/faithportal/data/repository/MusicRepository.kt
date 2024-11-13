package com.example.faithportal.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faithportal.model.SpotifyResponse
import okhttp3.*
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.random.Random

class MusicRepository {
    private val musicApi: MusicApiService
    private val clientId: String = "740763ccfa71471191d00bac370cb19d"
    private val clientSecret: String = "6bf917240ba84233adf5567419b345d1"
    private var accessToken: String? = null

    init {
        val client = getOkHttpClientWithAuth()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        musicApi = retrofit.create(MusicApiService::class.java)
    }

    private val _worshipMusic = MutableLiveData<SpotifyResponse>()
    val worshipMusic: LiveData<SpotifyResponse> get() = _worshipMusic

    suspend fun fetchWorshipMusic() {
        // Ensure you have a valid access token before making the request
        if (accessToken == null) {
            fetchAccessToken()
        }

        try {
            val response = musicApi.getWorshipMusic(
                "genre:christian", "track", 1, Random.nextInt(0, 1000)
            )
            Log.d("MusicRepository", "API Response: $response")
            _worshipMusic.postValue(response)
            Log.d("MusicRepository", "Fetched music tracks: $response")
        } catch (e: HttpException) {
            Log.e("MusicRepository", "HTTP error: ${e.code()}")
        } catch (e: Exception) {
            Log.e("MusicRepository", "Error: ${e.message}")
        }
    }

    private fun fetchAccessToken() {
        val formBody = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(formBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    accessToken = parseAccessToken(responseBody)
                    Log.d("MusicRepository", "Access token received: $accessToken")
                } else {
                    Log.e("MusicRepository", "Failed to get access token")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("MusicRepository", "Error during token fetch: ${e.message}")
            }
        })
    }

    private fun parseAccessToken(responseBody: String?): String? {
        return try {
            val jsonObject = responseBody?.let { JSONObject(it) }
            jsonObject?.getString("access_token")
        } catch (e: Exception) {
            Log.e("MusicRepository", "Failed to parse access token: ${e.message}")
            null
        }
    }

    // OkHttpClient with Interceptor to add the Authorization header
    private fun getOkHttpClientWithAuth(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // Add an interceptor to add the Authorization header
        builder.addInterceptor { chain ->
            val originalRequest = chain.request()

            val requestBuilder = if (accessToken != null) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
            } else {
                originalRequest.newBuilder()
            }

            // Proceed with the request
            chain.proceed(requestBuilder.build())
        }

        return builder.build()
    }
}
