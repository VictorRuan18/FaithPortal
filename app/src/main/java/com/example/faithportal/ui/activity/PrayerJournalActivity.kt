package com.example.faithportal.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.faithportal.R
import com.example.faithportal.ui.fragment.PrayerJournalFragment

class PrayerJournalActivity : AppCompatActivity() {
    val TAG: String = "PrayerJournalActivity"

    fun createFragment(): Fragment {
        return PrayerJournalFragment()
    }

    @LayoutRes
    fun getLayoutResId(): Int {
        return R.layout.activity_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}