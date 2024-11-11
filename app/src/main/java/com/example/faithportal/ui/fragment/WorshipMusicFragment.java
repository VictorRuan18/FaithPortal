package com.example.faithportal.ui.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.faithportal.R;
import com.example.faithportal.data.ViewModel.WorshipMusicViewModel;
import com.example.faithportal.model.SimplifiedArtistObject;
import com.example.faithportal.model.TrackObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class WorshipMusicFragment extends Fragment {
    private static final String TAG = "WorshipMusicFragment";

    private WorshipMusicViewModel viewModel;
    private ImageView albumImage;
    private TextView titleText;
    private Button buttonSpotify;
    private Button buttonNewMusic;
    private Button buttonPlay;
    private Button buttonPause;
    private MediaPlayer mediaPlayer;

    public WorshipMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worship_music, container, false);

        albumImage = view.findViewById(R.id.imageView);
        titleText = view.findViewById(R.id.text_view_title);
        titleText.setText("Press the \'New Random Music\' button to begin");
        buttonPlay = view.findViewById(R.id.button_playSample);
        buttonPause = view.findViewById(R.id.button_pauseSample);
        buttonPlay.setEnabled(false);
        buttonPause.setEnabled(false);
        buttonSpotify = view.findViewById(R.id.button_spotifyLink);
        buttonNewMusic = view.findViewById(R.id.button_newMusicButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WorshipMusicViewModel.class);

        viewModel.getWorshipMusic().observe(getViewLifecycleOwner(), worshipMusic -> {
            if (worshipMusic != null) {
                TrackObject item = worshipMusic.getTracks().getItems().get(0);

                // Set image from api
                String imageUrl = item.getAlbum().getImages().get(0).getUrl();
                Picasso.get().load(imageUrl).into(albumImage);

                // Set title from api
                List<SimplifiedArtistObject> artists = item.getArtists();
                StringBuilder musicTitle = new StringBuilder();
                musicTitle.append(item.getName());
                musicTitle.append(" - ");
                for (int i = 0; i < artists.size(); i++) {
                    musicTitle.append(artists.get(i).getName());
                    if (i != artists.size() - 1) {
                        musicTitle.append(", ");
                    }
                }
                titleText.setText(musicTitle.toString());

                // Set preview if it exists
                String previewUrl = item.getPreview_url();
                if (previewUrl != null) {
                    buttonPlay.setEnabled(true);
                    buttonPlay.setOnClickListener(v -> playAudio(previewUrl));
                    buttonPause.setOnClickListener(v -> pauseAudio());
                } else {
                    buttonPlay.setEnabled(false);
                    buttonPause.setEnabled(false);
                    Toast.makeText(getActivity(), "This song has no sample", Toast.LENGTH_LONG).show();
                }

                // Set a link to the music from api
                String linkUrl = item.getExternal_urls().getSpotify();
                buttonSpotify.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))));
            } else {
                titleText.setText("No songs found");
            }
        });

        buttonNewMusic.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                pauseAudio();
            }
            viewModel.retrieveWorshipMusic();
        });

        viewModel.retrieveWorshipMusic();
    }

    private void playAudio(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        buttonPause.setEnabled(true);
        buttonPlay.setEnabled(false);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                buttonPlay.setEnabled(true);
                buttonPause.setEnabled(false);
                Toast.makeText(getActivity(), "Sample finished playing", Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Sample Started playing", Toast.LENGTH_LONG).show();
    }

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(getActivity(), "Sample paused", Toast.LENGTH_LONG).show();
            buttonPause.setEnabled(false);
            buttonPlay.setEnabled(true);
        } else {
            Toast.makeText(getActivity(), "Sample has not played", Toast.LENGTH_LONG).show();
        }
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
        if (mediaPlayer != null) {
            pauseAudio();
        }
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