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
            val formattedBook = bookNameMapping[book] ?: book.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            response.book = formattedBook
            response.chapter = chapter
            response.verse = verse
            Log.d("BibleRepository", "API Response: $response")
            _bibleVerse.postValue(response)
            Log.d("BibleRepository", "Fetched verse: $response")
        } catch (e: HttpException) {
            Log.e("BibleRepository", "HTTP error: ${e.code()}")
        } catch (e: Exception) {
            Log.e("BibleRepository", "Error: ${e.message}")
        }
    }

    val books = listOf(
        "genesis", "exodus", "leviticus", "numbers", "deuteronomy", "joshua", "judges", "ruth", "1samuel", "2samuel",
        "1kings", "2kings", "1chronicles", "2chronicles", "ezra", "nehemiah", "esther", "job", "psalms", "proverbs",
        "ecclesiastes", "songofsongs", "isaiah", "jeremiah", "lamentations", "ezekiel", "daniel", "hosea", "joel",
        "amos", "obadiah", "jonah", "micah", "nahum", "habakkuk", "zephaniah", "haggai", "zechariah", "malachi",
        "matthew", "mark", "luke", "john", "acts", "romans", "1corinthians", "2corinthians", "galatians", "ephesians",
        "philippians", "colossians", "1thessalonians", "2thessalonians", "1timothy", "2timothy", "titus", "philemon",
        "hebrews", "james", "1peter", "2peter", "1john", "2john", "3john", "jude", "revelation"
    )

    val chapters = mapOf(
        "genesis" to 50, "exodus" to 40, "leviticus" to 27, "numbers" to 36, "deuteronomy" to 34,
        "joshua" to 24, "judges" to 21, "ruth" to 4, "1samuel" to 31, "2samuel" to 24,
        "1kings" to 22, "2kings" to 25, "1chronicles" to 29, "2chronicles" to 36, "ezra" to 10,
        "nehemiah" to 13, "esther" to 10, "job" to 42, "psalms" to 150, "proverbs" to 31,
        "ecclesiastes" to 12, "songofsongs" to 8, "isaiah" to 66, "jeremiah" to 52, "lamentations" to 5,
        "ezekiel" to 48, "daniel" to 12, "hosea" to 14, "joel" to 3, "amos" to 9,
        "obadiah" to 1, "jonah" to 4, "micah" to 7, "nahum" to 3, "habakkuk" to 3,
        "zephaniah" to 3, "haggai" to 2, "zechariah" to 14, "malachi" to 4,
        "matthew" to 28, "mark" to 16, "luke" to 24, "john" to 21, "acts" to 28,
        "romans" to 16, "1corinthians" to 16, "2corinthians" to 13, "galatians" to 6, "ephesians" to 6,
        "philippians" to 4, "colossians" to 4, "1thessalonians" to 5, "2thessalonians" to 3, "1timothy" to 6,
        "2timothy" to 4, "titus" to 3, "philemon" to 1, "hebrews" to 13, "james" to 5,
        "1peter" to 5, "2peter" to 3, "1john" to 5, "2john" to 1, "3john" to 1,
        "jude" to 1, "revelation" to 22
    )

    val verses = mapOf(
        "genesis" to listOf(31, 25, 24, 26, 32, 22, 24, 22, 29, 32, 32, 20, 18, 24, 21, 16, 27, 33, 38, 18, 34, 24, 20, 67, 34, 35, 46, 22, 35, 43, 55, 32, 20, 31, 29, 43, 36, 30, 23, 23, 57, 38, 34, 34, 28, 34, 31, 22, 33, 26),
        "exodus" to listOf(22, 25, 22, 31, 23, 30, 25, 32, 35, 29, 10, 51, 22, 31, 27, 36, 16, 27, 25, 26, 36, 31, 33, 18, 40, 37, 21, 43, 46, 38, 18, 35, 23, 35, 35, 38, 29, 31, 43, 38),
        "leviticus" to listOf(17, 16, 17, 35, 19, 30, 38, 36, 24, 20, 47, 8, 59, 57, 33, 34, 16, 30, 37, 27, 24, 33, 44, 23, 55, 46, 34),
        "numbers" to listOf(54, 34, 51, 49, 31, 27, 89, 26, 23, 36, 35, 16, 33, 45, 41, 50, 13, 32, 22, 29, 35, 41, 30, 25, 18, 65, 23, 31, 40, 16, 54, 42, 56, 29, 34, 13),
        "deuteronomy" to listOf(46, 37, 29, 49, 33, 25, 26, 20, 29, 22, 32, 32, 18, 29, 23, 22, 20, 22, 21, 20, 23, 30, 25, 22, 19, 19, 26, 68, 29, 20, 30, 52, 29, 12),
        "joshua" to listOf(18, 24, 17, 24, 15, 27, 26, 35, 27, 43, 23, 24, 33, 15, 63, 10, 18, 28, 51, 9, 45, 34, 16, 33),
        "judges" to listOf(36, 23, 31, 24, 31, 40, 25, 35, 57, 18, 40, 15, 25, 20, 20, 31, 13, 31, 30, 48, 25),
        "ruth" to listOf(22, 23, 18, 22),
        "1samuel" to listOf(28, 36, 21, 22, 12, 21, 17, 22, 27, 27, 15, 25, 23, 52, 35, 23, 58, 30, 24, 42, 15, 23, 29, 22, 44, 25, 12, 25, 11, 31, 13),
        "2samuel" to listOf(27, 32, 39, 12, 25, 23, 29, 18, 13, 19, 27, 31, 39, 33, 37, 23, 29, 33, 43, 26, 22, 51, 39, 25),
        "1kings" to listOf(53, 46, 28, 34, 18, 38, 51, 66, 28, 29, 43, 33, 34, 31, 34, 34, 24, 46, 21, 43, 29, 53),
        "2kings" to listOf(18, 25, 27, 44, 27, 33, 20, 29, 37, 36, 21, 21, 25, 29, 38, 20, 41, 37, 37, 21, 26, 20, 37, 20, 30),
        "1chronicles" to listOf(54, 55, 24, 43, 26, 81, 40, 40, 44, 14, 47, 40, 14, 17, 29, 43, 27, 17, 19, 8, 30, 19, 32, 31, 31, 32, 34, 21, 30),
        "2chronicles" to listOf(17, 18, 17, 22, 14, 42, 22, 18, 31, 19, 23, 16, 22, 15, 19, 14, 19, 34, 11, 37, 20, 12, 21, 27, 28, 23, 9, 27, 36, 27, 21, 33, 25, 33, 27, 23),
        "ezra" to listOf(11, 70, 13, 24, 17, 22, 28, 36, 15, 44),
        "nehemiah" to listOf(11, 20, 32, 23, 19, 19, 73, 18, 38, 39, 36, 47, 31),
        "esther" to listOf(22, 23, 15, 17, 14, 14, 10, 17, 32, 3),
        "job" to listOf(22, 13, 26, 21, 27, 30, 21, 22, 35, 22, 20, 25, 28, 22, 35, 22, 16, 21, 29, 29, 34, 30, 17, 25, 6, 14, 23, 28, 25, 31, 40, 22, 33, 37, 16, 33, 24, 41, 30, 24, 34, 17),
        "psalms" to listOf(6, 12, 8, 8, 12, 10, 17, 9, 20, 18, 7, 8, 6, 7, 5, 11, 15, 50, 14, 9, 13, 31, 6, 10, 22, 12, 14, 9, 11, 12, 24, 11, 22, 22, 28, 12, 40, 22, 13, 17, 13, 11, 5, 26, 17, 11, 9, 14, 20, 23, 19, 9, 6, 7, 23, 13, 11, 11, 17, 12, 8, 12, 11, 10, 13, 20, 7, 35, 36, 5, 24, 20, 28, 23, 10, 12, 20, 72, 13, 19, 16, 8, 18, 12, 13, 17, 7, 18, 52, 17, 16, 15, 5, 23, 11, 13, 12, 9, 9, 5, 8, 28, 22, 35, 45, 48, 43, 13, 31, 7, 10, 10, 9, 8, 18, 19, 2, 29, 176, 7, 8, 9, 4, 8, 5, 6, 5, 6, 8, 8, 3, 18, 3, 3, 21, 26, 9, 8, 24, 13, 10, 7, 12, 15, 21, 10, 20, 14, 9, 6),
        "proverbs" to listOf(33, 22, 35, 27, 23, 35, 27, 36, 18, 32, 31, 28, 25, 35, 33, 33, 28, 24, 29, 30, 31, 29, 35, 34, 28, 28, 27, 28, 27, 33, 31),
        "ecclesiastes" to listOf(18, 26, 22, 16, 20, 12, 29, 17, 18, 20, 10, 14),
        "songofsongs" to listOf(17, 17, 11, 16, 16, 13, 13, 14),
        "isaiah" to listOf(
            31, 22, 26, 6, 30, 13, 25, 22, 21, 34, 16, 6, 22, 32, 9, 14, 14, 7, 25, 6,
            17, 25, 18, 23, 12, 21, 13, 29, 24, 33, 9, 20, 24, 17, 10, 22, 38, 22, 8,
            31, 29, 25, 28, 28, 25, 13, 15, 22, 26, 11, 23, 15, 12, 17, 13, 12, 21,
            14, 30, 16, 12, 25, 24
        ),
        "jeremiah" to listOf(
            19, 37, 25, 31, 31, 30, 34, 22, 26, 25, 23, 17, 27, 22, 21, 21, 27, 23, 15, 18,
            14, 30, 40, 10, 38, 24, 22, 17, 32, 24, 40, 44, 26, 22, 19, 32, 21, 28, 18, 16,
            18, 22, 13, 30, 5, 28, 7, 47, 39, 46, 64, 34
        ),
        "lamentations" to listOf(22, 22, 66, 22, 22),
        "ezekiel" to listOf(
            28, 10, 27, 17, 17, 14, 27, 18, 11, 22, 25, 28, 23, 23, 8, 63, 24, 32, 14, 49,
            32, 31, 49, 27, 17, 21, 36, 26, 21, 26, 18, 32, 33, 31, 15, 38, 28, 23, 29, 49,
            26, 20, 27, 31, 25, 24, 23, 35
        ),
        "daniel" to listOf(21, 49, 30, 37, 31, 28, 28, 27, 27, 21, 45, 13),
        "hosea" to listOf(11, 23, 5, 19, 15, 11, 16, 14, 17, 15, 12, 14, 16, 9),
        "joel" to listOf(20, 32, 21),
        "amos" to listOf(15, 16, 15, 13, 27, 14, 17, 14, 15),
        "obadiah" to listOf(21),
        "jonah" to listOf(17, 10, 10, 11),
        "micah" to listOf(16, 13, 12, 13, 15, 16, 20),
        "nahum" to listOf(15, 13, 19),
        "habakkuk" to listOf(17, 20, 19),
        "zephaniah" to listOf(18, 15, 20),
        "haggai" to listOf(15, 23),
        "zechariah" to listOf(21, 13, 10, 14, 11, 15, 14, 23, 17, 12, 17, 14, 9, 21),
        "malachi" to listOf(14, 17, 18, 6),
        "matthew" to listOf(
            25, 23, 17, 25, 48, 34, 29, 34, 38, 42, 30, 50, 58, 36, 39, 28, 27, 35, 30, 34,
            46, 46, 39, 51, 46, 75, 66, 20
        ),
        "mark" to listOf(45, 28, 35, 41, 43, 56, 37, 38, 50, 52, 33, 44, 37, 72, 47, 20),
        "luke" to listOf(
            80, 52, 38, 44, 39, 49, 50, 56, 62, 42, 54, 59, 35, 35, 32, 31, 37, 43, 48, 47,
            38, 71, 56, 53
        ),
        "john" to listOf(51, 25, 36, 54, 47, 71, 53, 59, 41, 42, 57, 50, 38, 31, 27, 33, 26, 40, 42, 31, 25),
        "acts" to listOf(
            26, 47, 26, 37, 42, 15, 60, 40, 43, 48, 30, 25, 52, 28, 41, 40, 34, 28, 41, 38,
            40, 30, 35, 27, 27, 32, 44, 31
        ),
        "romans" to listOf(32, 29, 31, 25, 21, 23, 25, 39, 33, 21, 36, 21, 14, 23, 33, 27),
        "1corinthians" to listOf(31, 16, 23, 21, 13, 20, 40, 13, 27, 33, 34, 31, 13, 40, 58, 24),
        "2corinthians" to listOf(24, 17, 18, 18, 21, 18, 16, 24, 15, 18, 33, 21, 14),
        "galatians" to listOf(24, 21, 29, 31, 26, 18),
        "ephesians" to listOf(23, 22, 21, 32, 33, 24),
        "philippians" to listOf(30, 30, 21, 23),
        "colossians" to listOf(29, 23, 25, 18),
        "1thessalonians" to listOf(10, 20, 13, 18, 28),
        "2thessalonians" to listOf(12, 17, 18),
        "1timothy" to listOf(20, 15, 16, 16, 25, 21),
        "2timothy" to listOf(18, 26, 17, 22),
        "titus" to listOf(16, 15, 15),
        "philemon" to listOf(25),
        "hebrews" to listOf(14, 18, 19, 16, 14, 20, 28, 13, 28, 39, 40, 29, 25),
        "james" to listOf(27, 26, 18, 17, 20),
        "1peter" to listOf(25, 25, 22, 19, 14),
        "2peter" to listOf(21, 22, 18),
        "1john" to listOf(10, 29, 24, 21, 21),
        "2john" to listOf(13),
        "3john" to listOf(14),
        "jude" to listOf(25),
        "revelation" to listOf(20, 29, 22, 11, 14, 17, 17, 13, 21, 11, 19, 17, 18, 20, 8, 21, 18, 24, 21, 15, 27, 21)
    )

    val bookNameMapping = mapOf(
        "genesis" to "Genesis",
        "exodus" to "Exodus",
        "leviticus" to "Leviticus",
        "numbers" to "Numbers",
        "deuteronomy" to "Deuteronomy",
        "joshua" to "Joshua",
        "judges" to "Judges",
        "ruth" to "Ruth",
        "1samuel" to "1 Samuel",
        "2samuel" to "2 Samuel",
        "1kings" to "1 Kings",
        "2kings" to "2 Kings",
        "1chronicles" to "1 Chronicles",
        "2chronicles" to "2 Chronicles",
        "ezra" to "Ezra",
        "nehemiah" to "Nehemiah",
        "esther" to "Esther",
        "job" to "Job",
        "psalms" to "Psalms",
        "proverbs" to "Proverbs",
        "ecclesiastes" to "Ecclesiastes",
        "songofsongs" to "Song of Songs",
        "isaiah" to "Isaiah",
        "jeremiah" to "Jeremiah",
        "lamentations" to "Lamentations",
        "ezekiel" to "Ezekiel",
        "daniel" to "Daniel",
        "hosea" to "Hosea",
        "joel" to "Joel",
        "amos" to "Amos",
        "obadiah" to "Obadiah",
        "jonah" to "Jonah",
        "micah" to "Micah",
        "nahum" to "Nahum",
        "habakkuk" to "Habakkuk",
        "zephaniah" to "Zephaniah",
        "haggai" to "Haggai",
        "zechariah" to "Zechariah",
        "malachi" to "Malachi",
        "matthew" to "Matthew",
        "mark" to "Mark",
        "luke" to "Luke",
        "john" to "John",
        "acts" to "Acts",
        "romans" to "Romans",
        "1corinthians" to "1 Corinthians",
        "2corinthians" to "2 Corinthians",
        "galatians" to "Galatians",
        "ephesians" to "Ephesians",
        "philippians" to "Philippians",
        "colossians" to "Colossians",
        "1thessalonians" to "1 Thessalonians",
        "2thessalonians" to "2 Thessalonians",
        "1timothy" to "1 Timothy",
        "2timothy" to "2 Timothy",
        "titus" to "Titus",
        "philemon" to "Philemon",
        "hebrews" to "Hebrews",
        "james" to "James",
        "1peter" to "1 Peter",
        "2peter" to "2 Peter",
        "1john" to "1 John",
        "2john" to "2 John",
        "3john" to "3 John",
        "jude" to "Jude",
        "revelation" to "Revelation"
    )

    suspend fun fetchRandomBibleVerse() {
        val book = books.random()
        val maxChapter = chapters[book] ?: 1
        var chapter = Random.nextInt(1, maxChapter + 1)
        var maxVerse = verses[book]?.get(chapter - 1) ?: 1
        var verse = Random.nextInt(1, maxVerse + 1)

        Log.d("BibleRepository", "Fetching random verse: $book $chapter:$verse")
        withContext(Dispatchers.IO) {
            fetchBibleVerse("en-web", book, chapter, verse)
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