package com.example.faithportal.data.Model;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PrayerEntryDao {

    @Insert
    void insert(PrayerEntry prayerEntry);

    @Update
    void update(PrayerEntry prayerEntry);

    @Delete
    void delete(PrayerEntry prayerEntry);

    @Query("SELECT * FROM prayer_entries ORDER BY id DESC")
    LiveData<List<PrayerEntry>> getAllPrayerEntries();
}