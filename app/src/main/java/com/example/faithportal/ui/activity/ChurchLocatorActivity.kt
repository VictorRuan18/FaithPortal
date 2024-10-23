package com.example.faithportal.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.faithportal.R
import com.example.faithportal.ui.fragment.BibleVerseFragment
import com.example.faithportal.ui.fragment.ChurchLocatorFragment

class ChurchLocatorActivity : AppCompatActivity() {
    val TAG: String = "ChurchLocatorActivity"

    fun createFragment(): Fragment {
        return ChurchLocatorFragment()
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