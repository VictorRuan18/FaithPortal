package com.example.faithportal.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.example.faithportal.R
import com.example.faithportal.ui.activity.BibleVerseActivity
import com.example.faithportal.ui.activity.ChurchLocatorActivity
import com.example.faithportal.ui.activity.PrayerJournalActivity
import com.example.faithportal.ui.activity.WorshipMusicActivity

class AppOptionsFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_app_options, container, false)

        val btnBibleVerse: Button = v.findViewById(R.id.button_bibleVerse)
        btnBibleVerse.setOnClickListener(this)
        val btnWorshipMusic: Button = v.findViewById(R.id.button_worshipMusic)
        btnWorshipMusic.setOnClickListener(this)
        val btnPrayerJournal: Button = v.findViewById(R.id.button_prayerJournal)
        btnPrayerJournal.setOnClickListener(this)
        val btnChurchLocator: Button = v.findViewById(R.id.button_churchLocator)
        btnChurchLocator.setOnClickListener(this)

        return v
    }

    override fun onClick(view: View) {
        val activity = requireActivity()
        when (view.id) {
            R.id.button_bibleVerse -> startActivity(
                Intent(
                    activity.applicationContext,
                    BibleVerseActivity::class.java
                )
            )
            R.id.button_worshipMusic -> startActivity(
                Intent(
                    activity.applicationContext,
                    WorshipMusicActivity::class.java
                )
            )
            R.id.button_prayerJournal -> startActivity(
                Intent(
                    activity.applicationContext,
                    PrayerJournalActivity::class.java
                )
            )
            R.id.button_churchLocator -> startActivity(
                Intent(
                    activity.applicationContext,
                    ChurchLocatorActivity::class.java
                )
            )
        }
    }
}