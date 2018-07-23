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
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mFragmentActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        initViewModel();
    }

    private void initViewModel() {
        RecipeViewModel recipeViewModel = ViewModelProviders.of(mFragmentActivity).get(RecipeViewModel.class);

        recipeViewModel.getSelectedRecipeStep().observe(this, step -> {
            if (step != null) {
                if (mExoPlayer != null) {
                    releasePlayer();
                }
                if (!step.getVideoURL().isEmpty()) {
                    playerVisible();
                    initializePlayer(Uri.parse(step.getVideoURL()));
                } else if (!step.getThumbnailURL().isEmpty()) {
                    playerVisible();
                    initializePlayer(Uri.parse(step.getThumbnailURL()));
                } else {
                    noMedia();
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

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mFragmentActivity),
                    trackSelector,
                    loadControl);

            mViewVideoPlayer.setPlayer(mExoPlayer);

            // Prepare the media resource.
//            String userAgent
//                    = Util.getUserAgent(mFragmentActivity, "RecipeStepMedia");

//            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
//                    new DefaultDataSourceFactory(mFragmentActivity, userAgent),
//                    new DefaultExtractorsFactory(), null, null);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("RecipeStepMedia"))
                    .createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource, true, false);

            if (mExoPlayerCurrentPosition != null) {
                mExoPlayer.seekTo(mExoPlayerCurrentPosition);
            }

            if (mExoPlayerPlayWhenReady != null) {
                mExoPlayer.setPlayWhenReady(mExoPlayerPlayWhenReady);
            }
        }
    }

    // This code was suggested to hide UI elements but I do not want to use it quite yet
    // as this causes issues with my current solution for hiding the action bar on rotate.
    // Leaving commented here for future consideration.
//    private void hideSystemUi() {
//        mViewVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }

    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initViewModel();
        }
    }

    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            // No use for this quite yet, bookmarking future consideration.
//            hideSystemUi();
            initViewModel();
        }
    }

    private void releasePlayer() {
        mExoPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
        mExoPlayerPlayWhenReady = mExoPlayer.getPlayWhenReady();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (mExoPlayer != null) {
            savedInstanceState.putLong(EXO_CURRENT_POS, mExoPlayer.getCurrentPosition());
            savedInstanceState.putBoolean(EXO_PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
        }
    }

    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && mExoPlayer != null) {
            releasePlayer();
        }
    }

    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && mExoPlayer != null) {
            releasePlayer();
        }
    }
}
