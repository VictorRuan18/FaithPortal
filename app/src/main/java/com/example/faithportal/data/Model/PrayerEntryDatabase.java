package com.example.faithportal.data.Model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PrayerEntry.class}, version = 1, exportSchema = false)
public abstract class PrayerEntryDatabase extends RoomDatabase {

    private static PrayerEntryDatabase instance;

    public abstract PrayerEntryDao prayerEntryDao();

    // Singleton instance to avoid multiple database connections
    public static synchronized PrayerEntryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PrayerEntryDatabase.class, "prayer_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
