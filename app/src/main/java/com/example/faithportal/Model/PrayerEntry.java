package com.example.faithportal.Model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prayer_entries")
public class PrayerEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String date;

    public PrayerEntry(String date, String content) {
        this.date = date;
        this.content = content;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
