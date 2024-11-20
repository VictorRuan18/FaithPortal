import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.faithportal.data.repository.BibleRepository
import com.example.faithportal.model.BibleVerse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28], manifest = Config.NONE)
class TestBibleVerse {

    private lateinit var repository: BibleRepository

    @Before
    fun setUp() {
        repository = BibleRepository() // Ensure correct initialization
    }

    @Test
    fun testFetchRandomBibleVerse() = runBlocking {
        val latch = CountDownLatch(1)
        var fetchedVerse: BibleVerse? = null

        // Observe the LiveData
        val observer = Observer<BibleVerse> { bibleVerse ->
            if (bibleVerse != null) {
                fetchedVerse = bibleVerse
                latch.countDown()
            }
        }

        try {
            repository.bibleVerse.observeForever(observer)

            // Fetch the random Bible verse
            repository.fetchRandomBibleVerse()

            // Allow pending tasks to execute
            ShadowLooper.idleMainLooper()

            // Wait for LiveData update
            val awaitSuccess = latch.await(10, TimeUnit.SECONDS)
            assertTrue("Timeout waiting for Bible verse", awaitSuccess)
            assertNotNull("Bible verse should not be null", fetchedVerse)
        } finally {
            // Remove observer to prevent memory leaks
            repository.bibleVerse.removeObserver(observer)
        }
    }

    @Test
    fun testFetchBibleVerse() = runBlocking {
        val latch = CountDownLatch(1)
        var fetchedVerse: BibleVerse? = null

        // Mock the observer
        val observer = Observer<BibleVerse> { bibleVerse ->
            if (bibleVerse != null) {
                fetchedVerse = bibleVerse
                latch.countDown()
            }
        }

        try {
            repository.bibleVerse.observeForever(observer)

            // Fetch a specific Bible verse
            repository.fetchBibleVerse("en-web", "john", 3, 16)

            // Allow pending tasks to execute
            ShadowLooper.idleMainLooper()

            // Wait for LiveData update
            val awaitSuccess = latch.await(10, TimeUnit.SECONDS)
            assertTrue("Timeout waiting for Bible verse", awaitSuccess)
            assertNotNull("Bible verse should not be null", fetchedVerse)
        } finally {
            // Remove observer to prevent memory leaks
            repository.bibleVerse.removeObserver(observer)
        }
    }
    @Test
    fun testFetchBibleVerseWithNumInBook() = runBlocking {
        val latch = CountDownLatch(1)
        var fetchedVerse: BibleVerse? = null

        // Mock the observer
        val observer = Observer<BibleVerse> { bibleVerse ->
            fetchedVerse = bibleVerse
            latch.countDown()
        }

        try {
            repository.bibleVerse.observeForever(observer)

            repository.fetchBibleVerse("en-web", "2john", 1, 1)

            // Allow pending tasks to execute
            ShadowLooper.idleMainLooper()

            // Wait for LiveData update
            val awaitSuccess = latch.await(10, TimeUnit.SECONDS)
            assertTrue("Timeout waiting for Bible verse", awaitSuccess)
            assertNotNull("Bible verse should not be null", fetchedVerse)
        } finally {
            // Remove observer to prevent memory leaks
            repository.bibleVerse.removeObserver(observer)
        }
    }
}
