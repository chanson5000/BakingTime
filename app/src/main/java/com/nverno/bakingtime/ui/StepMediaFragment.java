package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepMediaFragment extends Fragment {

    private FragmentActivity mFragmentActivity;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mViewVideoPlayer;
    private TextView mTxtNoMedia;

    private static final String EXO_CURRENT_POS = "EXO_CURRENT_POS";
    private static final String EXO_PLAY_WHEN_READY = "EXO_PLAY_WHEN_READY";

    private Long mExoPlayerCurrentPosition;
    private Boolean mExoPlayerPlayWhenReady;

    private RecipeViewModel recipeViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeViewModel = ViewModelProviders.of(mFragmentActivity).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_media,
                container, false);

        mViewVideoPlayer = rootView.findViewById(R.id.recipe_step_media);
        mTxtNoMedia = rootView.findViewById(R.id.recipe_no_media);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mExoPlayerCurrentPosition = savedInstanceState.getLong(EXO_CURRENT_POS);
            mExoPlayerPlayWhenReady = savedInstanceState.getBoolean(EXO_PLAY_WHEN_READY);
        }
    }

    private void setupPlayer() {
        recipeViewModel.getSelectedRecipeStep().observe(this, step -> {
            if (step != null) {
                if (mExoPlayer != null) {
                    releasePlayer();
                }
                if (!step.getVideoURL().isEmpty()) {
                    playerVisible();
                    initializePlayer(Uri.parse(step.getVideoURL()));
                    loadPlayerState();
                    resetPlayerState();
                } else if (!step.getThumbnailURL().isEmpty()) {
                    playerVisible();
                    initializePlayer(Uri.parse(step.getThumbnailURL()));
                    loadPlayerState();
                    resetPlayerState();
                } else {
                    noMedia();
                    resetPlayerState();
                }
            }
        });
    }

    private void playerVisible() {
        mTxtNoMedia.setVisibility(View.GONE);
        mViewVideoPlayer.setVisibility(View.VISIBLE);
    }

    private void noMedia() {
        mViewVideoPlayer.setVisibility(View.GONE);
        mTxtNoMedia.setVisibility(View.VISIBLE);
    }

    private SimpleExoPlayer createExoPlayerInstance(FragmentActivity fragmentActivity) {
        return ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(fragmentActivity),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
    }

    private MediaSource createMediaSourceFromUri(Uri mediaUri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-recipe-step-media"))
                .createMediaSource(mediaUri);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            mExoPlayer = createExoPlayerInstance(mFragmentActivity);
            mViewVideoPlayer.setPlayer(mExoPlayer);
            mExoPlayer.prepare(createMediaSourceFromUri(mediaUri), true, false);
        }
    }

    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setupPlayer();
        }
    }

    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            setupPlayer();
        }
    }

    private void savePlayerState() {
        if (mExoPlayer != null) {
            mExoPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
    }

    private void loadPlayerState() {
        if (mExoPlayerCurrentPosition != null && mExoPlayerPlayWhenReady != null) {
            mExoPlayer.seekTo(mExoPlayerCurrentPosition);
            mExoPlayer.setPlayWhenReady(mExoPlayerPlayWhenReady);
        }
    }

    private void resetPlayerState() {
        mExoPlayerCurrentPosition = null;
        mExoPlayerPlayWhenReady = null;
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    // For the savedInstanceState...
    // I want to be able to pull current ExoPlayer values before onPause or onStop is called.
    // We are now pulling the state from the actual ExoPlayer rather than the member variables.
    // Before, the member variables weren't being pulled because the ExoPlayer state was null.
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mExoPlayer != null) {
            savedInstanceState.putLong(EXO_CURRENT_POS, mExoPlayer.getCurrentPosition());
            savedInstanceState.putBoolean(EXO_PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
            // If was was doing it the other way I believe could have just removed the ExoPlayer null
            // check and set the saved instance state with the member variables if they had values.
            // Using this as a backup now in case the ExoPlayer is null but we still have valid
            // member variables.
        } else if (mExoPlayerCurrentPosition != null && mExoPlayerPlayWhenReady != null) {
            savedInstanceState.putLong(EXO_CURRENT_POS, mExoPlayerCurrentPosition);
            savedInstanceState.putBoolean(EXO_PLAY_WHEN_READY, mExoPlayerPlayWhenReady);
        }
    }

    // In earlier APIs onPause is called after onSavedInstanceState. Later APIs there is no
    // guarantee if it is called before or after onPause.
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && mExoPlayer != null) {
            savePlayerState();
            releasePlayer();
        }
    }

    // In API 24+ onStop is called after saved instance state.
    // So hold off onStop until later on higher API levels when saving state to release the player
    // as late as possible while not interfering with the savedInstanceState operations.
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && mExoPlayer != null) {
            savePlayerState();
            releasePlayer();
        }
    }
}
