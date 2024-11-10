package com.example.faithportal.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faithportal.R;
import com.example.faithportal.ui.SavedVersesAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class SavedVersesFragment extends Fragment {

    private static final String PREFS_NAME = "SavedVersesPrefs";
    private static final String KEY_SAVED_VERSES = "saved_verses";
    private List<String> savedVerses = new ArrayList<>();

    public SavedVersesFragment(List<String> savedVerses) {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_verses, container, false);
        loadSavedVerses();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_saved_verses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SavedVersesAdapter(savedVerses));
        return view;
    }

    private void loadSavedVerses() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> savedSet = sharedPreferences.getStringSet(KEY_SAVED_VERSES, new HashSet<>());
        savedVerses.clear();
        savedVerses.addAll(savedSet);
    }
}