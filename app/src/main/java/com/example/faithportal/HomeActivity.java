package com.example.faithportal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Button prayerJournalButton;
    private Button buttonFragment1;
    private Button buttonFragment2;
    private Button buttonFragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure this is called first
        Log.d(TAG, "onCreate");

        prayerJournalButton = findViewById(R.id.button_prayerJournal);
        buttonFragment1 = findViewById(R.id.button_Fragment1);
        buttonFragment2 = findViewById(R.id.button_Fragment2);
        buttonFragment4 = findViewById(R.id.button_Fragment4);

        prayerJournalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButtons();
                replaceFragment(new PrayerJournalFragment());
            }
        });
    }

    private void hideButtons() {
        buttonFragment1.setVisibility(View.GONE);
        buttonFragment2.setVisibility(View.GONE);
        prayerJournalButton.setVisibility(View.GONE);
        buttonFragment4.setVisibility(View.GONE);
    }

    private void showButtons() {
        buttonFragment1.setVisibility(View.VISIBLE);
        buttonFragment2.setVisibility(View.VISIBLE);
        prayerJournalButton.setVisibility(View.VISIBLE);
        buttonFragment4.setVisibility(View.VISIBLE);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            showButtons();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}