package com.example.faithportal.ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.faithportal.ui.fragment.AppOptionsFragment
import com.example.faithportal.ui.fragment.PrayerJournalFragment

class PrayerJournalActivity : AppCompatActivity() {

    fun createFragment(): Fragment {
        return PrayerJournalFragment()
    }
}