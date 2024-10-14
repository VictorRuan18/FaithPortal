package com.example.faithportal.Model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prayer_entries")
public class PrayerEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String date;


   public PrayerEntry(String data, String content) {
        this.date = date;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

   public int getId() {
        return id;
   }
   public void setDate(String date)
    {
          this.date = date;
    }
    public void setContent(String content)
    {
          this.content = content;
    }
}
