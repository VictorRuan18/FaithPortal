package com.example.faithportal.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.faithportal.R;
import com.example.faithportal.data.ViewModel.WorshipMusicViewModel;
import com.example.faithportal.model.SimplifiedArtistObject;
import com.example.faithportal.model.TrackObject;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WorshipMusicFragment extends Fragment {
    private static final String TAG = "WorshipMusicFragment";

    private WorshipMusicViewModel viewModel;
    private ImageView musicVideo;
    private TextView titleText;
    private Button buttonSpotify;
    private Button buttonNewMusic;

    public WorshipMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worship_music, container, false);

        musicVideo = view.findViewById(R.id.imageView);
        titleText = view.findViewById(R.id.text_view_title);
        buttonSpotify = view.findViewById(R.id.button_spotifyLink);
        buttonNewMusic = view.findViewById(R.id.button_newMusicButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WorshipMusicViewModel.class);

        AtomicReference<String> linkUrl = new AtomicReference<>("");
        AtomicReference<String> imageUrl = new AtomicReference<>("");

        viewModel.getWorshipMusic().observe(getViewLifecycleOwner(), worshipMusic -> {
            if (worshipMusic != null) {
                List<TrackObject> items = worshipMusic.getTracks().getItems();
                List<SimplifiedArtistObject> artists = items.get(0).getArtists();
                StringBuilder musicTitle = new StringBuilder();
                musicTitle.append(items.get(0).getName());
                musicTitle.append(" - ");
                for (int i = 0; i < artists.size(); i++) {
                    musicTitle.append(artists.get(i).getName());
                    if (i != artists.size() - 1) {
                        musicTitle.append(", ");
                    }
                }
                titleText.setText(musicTitle.toString());
                linkUrl.set(items.get(0).getExternal_urls().getSpotify());
                imageUrl.set(items.get(0).getAlbum().getImages().get(0).getUrl());
                Log.d(TAG, String.valueOf(imageUrl));
            } else {
                titleText.setText("No songs found");
            }
        });


        buttonNewMusic.setOnClickListener(v -> viewModel.retrieveWorshipMusic());

        buttonSpotify.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(linkUrl)))));

        viewModel.retrieveWorshipMusic();
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