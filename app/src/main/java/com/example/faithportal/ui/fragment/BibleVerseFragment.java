package com.example.faithportal.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.faithportal.R;
import com.example.faithportal.viewmodel.BibleVerseViewModel;

public class BibleVerseFragment extends Fragment {

    private static final String TAG = "BibleVerseFragment";
    private BibleVerseViewModel viewModel;
    private TextView textViewVerse;

    public BibleVerseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bible_verse, container, false);
        textViewVerse = view.findViewById(R.id.text_view_verse);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BibleVerseViewModel.class);

        viewModel.getBibleVerse().observe(getViewLifecycleOwner(), bibleVerse -> {
            if (bibleVerse != null) {
                String verseText = getString(R.string.bible_verse_format, bibleVerse.getBook(), bibleVerse.getChapter(), bibleVerse.getVerse(),bibleVerse.getText());
                textViewVerse.setText(verseText);
            } else {
                textViewVerse.setText(R.string.no_verse_found);
            }
        });
        viewModel.getRandomBibleVerse();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}