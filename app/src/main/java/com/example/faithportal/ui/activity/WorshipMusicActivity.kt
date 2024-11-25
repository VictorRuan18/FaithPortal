package com.example.faithportal.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.faithportal.R
import com.example.faithportal.ui.fragment.PrayerJournalFragment
import com.example.faithportal.ui.fragment.WorshipMusicFragment

class WorshipMusicActivity : AppCompatActivity() {
    val TAG: String = "WorshipMusicActivity"
    var mWorshipMusicFragment: WorshipMusicFragment = recoverFragment()

    fun createFragment(): Fragment {
        return WorshipMusicFragment()
    }

    fun getFragmentForTest(): WorshipMusicFragment {
        recoverFragment()
        return mWorshipMusicFragment
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

    private fun recoverFragment(): WorshipMusicFragment {
        val fm = supportFragmentManager
        if (fm.fragments.size == 0) {
            mWorshipMusicFragment = WorshipMusicFragment()
        } else {
            for (fragment in fm.fragments) {
                if (fragment is WorshipMusicFragment) {
                    mWorshipMusicFragment = fragment
                }
            }
        }
        return mWorshipMusicFragment
    }
}