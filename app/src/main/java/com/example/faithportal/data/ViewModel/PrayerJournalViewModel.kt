package com.example.faithportal.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.faithportal.data.Model.PrayerEntry
import com.example.faithportal.data.Model.PrayerEntryDao
import com.example.faithportal.data.Model.PrayerEntryDatabase

class PrayerJournalViewModel(application: Application) : AndroidViewModel(application) {
    private val prayerEntryDao: PrayerEntryDao

    // Get all prayer entries
    val allPrayerEntries: LiveData<List<PrayerEntry>>

    init {
        val database = PrayerEntryDatabase.getInstance(application)
        prayerEntryDao = database.prayerEntryDao()
        allPrayerEntries = prayerEntryDao.allPrayerEntries
    }

    // Insert new prayer entry
    fun insert(prayerEntry: PrayerEntry?) {
        Thread { prayerEntryDao.insert(prayerEntry) }.start()
    }

    // Update a prayer entry
    fun update(prayerEntry: PrayerEntry?) {
        Thread { prayerEntryDao.update(prayerEntry) }.start()
    }

    // Delete a prayer entry
    fun delete(prayerEntry: PrayerEntry?) {
        Thread { prayerEntryDao.delete(prayerEntry) }.start()
    }
}
