package com.example.faithportal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

class AppOptionsFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_app_options, container, false)

        val btnNewGame: Button = v.findViewById(R.id.button_Fragment1)
        btnNewGame.setOnClickListener(this)
        val btnAudio: Button = v.findViewById(R.id.button_Fragment2)
        btnAudio.setOnClickListener(this)
        val btnVideo: Button = v.findViewById(R.id.button_Fragment3)
        btnVideo.setOnClickListener(this)
        val btnImage: Button = v.findViewById(R.id.button_Fragment4)
        btnImage.setOnClickListener(this)

        return v
    }

    override fun onClick(view: View) {
        val activity = requireActivity()
        when (view.id) {
            R.id.button_Fragment1 -> startActivity(
                Intent(
                    activity.applicationContext,
                    FirstFragment::class.java
                )
            )
            R.id.button_Fragment2 -> startActivity(
                Intent(
                    activity.applicationContext,
                    SecondFragment::class.java
                )
            )
            R.id.button_Fragment3 -> startActivity(
                Intent(
                    activity.applicationContext,
                    PrayerJournalFragment::class.java
                )
            )
            R.id.button_Fragment4 -> startActivity(
                Intent(
                    activity.applicationContext,
                    FourthFragment::class.java
                )
            )
        }
    }
}