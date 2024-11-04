package com.example.faithportal.data.repository

import android.util.Log
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faithportal.model.BibleVerse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.Locale
import javax.net.ssl.*
import kotlin.random.Random

class BibleRepository {
    private val bibleApi: BibleApiService

    init {
        val client = getUnsafeOkHttpClient()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/gh/wldeh/bible-api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bibleApi = retrofit.create(BibleApiService::class.java)
    }

    private val _bibleVerse = MutableLiveData<BibleVerse>()
    val bibleVerse: LiveData<BibleVerse> get() = _bibleVerse

    suspend fun fetchBibleVerse(version: String, book: String, chapter: Int, verse: Int) {
        try {
            val response = bibleApi.getBibleVerse(version, book, chapter, verse)
            response.book = book.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            response.chapter = chapter;
            response.verse = verse;
            Log.d("BibleRepository", "API Response: $response")
            _bibleVerse.postValue(response)
            Log.d("BibleRepository", "Fetched verse: $response")
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

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}