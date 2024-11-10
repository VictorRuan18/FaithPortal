package com.example.faithportal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faithportal.R;

import java.util.List;

public class SavedVersesAdapter extends RecyclerView.Adapter<SavedVersesAdapter.SavedVerseViewHolder> {

    private final List<String> savedVerses;

    public SavedVersesAdapter(List<String> savedVerses) {
        this.savedVerses = savedVerses;
    }

    @NonNull
    @Override
    public SavedVerseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_verse, parent, false);
        return new SavedVerseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedVerseViewHolder holder, int position) {
        String verse = savedVerses.get(position);
        holder.textViewSavedVerse.setText(verse);
    }

    @Override
    public int getItemCount() {
        return savedVerses.size();
    }

    public static class SavedVerseViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSavedVerse;

        public SavedVerseViewHolder(View itemView) {
            super(itemView);
            textViewSavedVerse = itemView.findViewById(R.id.text_view_saved_verse);
        }
    }
}