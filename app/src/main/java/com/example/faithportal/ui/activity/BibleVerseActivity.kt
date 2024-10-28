package com.example.faithportal.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.faithportal.R
import com.example.faithportal.ui.fragment.BibleVerseFragment
import com.example.faithportal.ui.fragment.PrayerJournalFragment

class BibleVerseActivity : AppCompatActivity() {
    val TAG: String = "BibleVerseActivity"

    fun createFragment(): Fragment {
        return BibleVerseFragment()
    }

    @LayoutRes
    fun getLayoutResId(): Int {
        return R.layout.activity_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = BibleVerseFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}