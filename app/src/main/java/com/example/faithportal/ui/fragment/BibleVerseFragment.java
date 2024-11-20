package com.example.faithportal.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.faithportal.R;
import com.example.faithportal.viewmodel.BibleVerseViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BibleVerseFragment extends Fragment {

    private static final String TAG = "BibleVerseFragment";
    public static final String PREFS_NAME = "SavedVersesPrefs";
    public static final String KEY_SAVED_VERSES = "saved_verses";
    public BibleVerseViewModel viewModel;
    public TextView textViewVerse;
    public Button buttonGenerateNewVerse;
    public final List<String> savedVerses = new ArrayList<>();
    public Button buttonSaveVerse;
    public Button buttonViewSavedVerses;

    public BibleVerseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bible_verse, container, false);
        textViewVerse = view.findViewById(R.id.text_view_verse);
        buttonGenerateNewVerse = view.findViewById(R.id.button_generate_new_verse);
        buttonSaveVerse = view.findViewById(R.id.button_save_verse);
        buttonViewSavedVerses = view.findViewById(R.id.button_view_saved_verses);
        loadSavedVerses();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BibleVerseViewModel.class);

        viewModel.getBibleVerse().observe(getViewLifecycleOwner(), bibleVerse -> {
            if (bibleVerse != null) {
                String verseText = getString(R.string.bible_verse_format, bibleVerse.getBook(), bibleVerse.getChapter(), bibleVerse.getVerse(), bibleVerse.getText());
                textViewVerse.setText(verseText);
            } else {
                textViewVerse.setText(R.string.no_verse_found);
            }
        });

        buttonGenerateNewVerse.setOnClickListener(v -> viewModel.getRandomBibleVerse());
        buttonSaveVerse.setOnClickListener(v -> {
            String currentVerse = textViewVerse.getText().toString();
            if (!currentVerse.isEmpty()) {
                savedVerses.add(currentVerse);
                saveVerses();
                Log.d(TAG, "Verse saved: " + currentVerse);
            }
        });

        buttonViewSavedVerses.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SavedVersesFragment(savedVerses));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        viewModel.getRandomBibleVerse();
    }

    public void saveVerses() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_SAVED_VERSES, new HashSet<>(savedVerses));
        Toast.makeText(getActivity(), "Verse saved", Toast.LENGTH_LONG).show();
        editor.apply();
    }

    public void loadSavedVerses() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> savedSet = sharedPreferences.getStringSet(KEY_SAVED_VERSES, new HashSet<>());
        savedVerses.clear();
        savedVerses.addAll(savedSet);
    }
}