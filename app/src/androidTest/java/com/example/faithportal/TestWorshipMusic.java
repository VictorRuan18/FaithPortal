package com.example.faithportal;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.faithportal.ui.activity.WorshipMusicActivity;
import com.example.faithportal.ui.fragment.WorshipMusicFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class TestWorshipMusic extends ActivityTestRule<WorshipMusicActivity> {
    private WorshipMusicActivity mWorshipMusicActivity;
    private WorshipMusicFragment mWorshipMusicFragment;
    private TextView titleText;
    private Button newMusicButton;
    private Button playSample;
    private Button spotifyLink;

    public TestWorshipMusic() {
        super(WorshipMusicActivity.class);

        launchActivity(getActivityIntent());
        mWorshipMusicActivity = getActivity();
        mWorshipMusicFragment = mWorshipMusicActivity.getFragmentForTest();

        // Wait for the Activity to become idle so we don't have null Fragment references.
        getInstrumentation().waitForIdleSync();

        if (mWorshipMusicFragment != null) {
            View fragmentView = mWorshipMusicFragment.getView();
            if (fragmentView != null) {
                titleText = fragmentView.findViewById(R.id.text_view_title);
                newMusicButton = fragmentView.findViewById(R.id.button_newMusicButton);
                playSample = fragmentView.findViewById(R.id.button_playSample);
                spotifyLink = fragmentView.findViewById(R.id.button_spotifyLink);
            }
        }
    }

    @Test
    public void testPreconditions() {
        assertNotNull(mWorshipMusicActivity);
        assertNotNull(mWorshipMusicFragment);
        assertNotNull(titleText);
        assertNotNull(newMusicButton);
        assertNotNull(playSample);
        assertNotNull(spotifyLink);
    }

    @Test
    public void initialTestUI() {
        Espresso.onView(ViewMatchers.withId(R.id.text_view_title))
                .check(ViewAssertions.matches(ViewMatchers.withText("Press the 'New Random Music' button to begin")));

        Espresso.onView(ViewMatchers.withId(R.id.button_newMusicButton))
                .check(ViewAssertions.matches(ViewMatchers.withText("New Random Music")));

        Espresso.onView(ViewMatchers.withId(R.id.button_playSample))
                .check(ViewAssertions.matches(ViewMatchers.withText("Play")));

        Espresso.onView(ViewMatchers.withId(R.id.button_spotifyLink))
                .check(ViewAssertions.matches(ViewMatchers.withText("Spotify")));
    }

    @Test
    public void postButtonTestUI() {
        Espresso.onView(ViewMatchers.withId(R.id.text_view_title))
                .check(ViewAssertions.matches(ViewMatchers.withText("Press the 'New Random Music' button to begin")));

        Espresso.onView(ViewMatchers.withId(R.id.button_newMusicButton))
                .perform(ViewActions.click());

        final String[] musicTitle = {""};
        Espresso.onView(ViewMatchers.withId(R.id.text_view_title))
                .check((view, noViewFoundException) -> {
                    if (noViewFoundException != null) throw noViewFoundException;
                    TextView textView = (TextView) view;
                    musicTitle[0] = textView.getText().toString();
                });

        assertNotEquals(musicTitle[0], "Press the 'New Random Music' button to begin");
    }
}
