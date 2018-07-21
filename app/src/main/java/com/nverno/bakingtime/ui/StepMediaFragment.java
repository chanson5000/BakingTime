package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModel;
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
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepMediaFragment extends Fragment {

    private FragmentActivity mFragmentActivity;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mViewVideoPlayer;
    private TextView mTxtNoMedia;

    private static String EXO_CURRENT_POS = "EXO_CURRENT_POS";
    private static String EXO_PLAY_WHEN_READY = "EXO_PLAY_WHEN_READY";

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

        initViewModel();
    }

    private void initViewModel() {
        RecipeViewModel recipeViewModel
                = ViewModelProviders.of(mFragmentActivity).get(RecipeViewModel.class);

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

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                    trackSelector,
                    loadControl);

            mViewVideoPlayer.setPlayer(mExoPlayer);

            // Prepare the media resource.
            String userAgent
                    = Util.getUserAgent(mFragmentActivity, "RecipeStepMedia");

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(mFragmentActivity, userAgent),
                    new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);

            if (mExoPlayerCurrentPosition != null) {
                mExoPlayer.seekTo(mExoPlayerCurrentPosition);
            }

            if (mExoPlayerPlayWhenReady != null) {
                mExoPlayer.setPlayWhenReady(mExoPlayerPlayWhenReady);
            }

//            mInstanceStateViewModel.getmExoPlayerCurrentPosition().observe(this, currentPosition -> {
//                if (currentPosition != null) {
//                    mExoPlayer.seekTo(currentPosition);
//                }
//            });

//            mInstanceStateViewModel.getmExoPlayerPlayWhenReady().observe(this, playWhenReady -> {
//                if (playWhenReady != null) {
//                    mExoPlayer.setPlayWhenReady(playWhenReady);
//                } else {
//                    mExoPlayer.setPlayWhenReady(false);
//                }
//            });

        }
    }

    public void onResume() {
        super.onResume();

    }

    private void releasePlayer() {

//        mInstanceStateViewModel.setExoPlayerCurrentPosition(mExoPlayer.getCurrentPosition());
//        mInstanceStateViewModel.setmExoPlayerPlayWhenReady(mExoPlayer.getPlayWhenReady());

        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong(EXO_CURRENT_POS, mExoPlayer.getCurrentPosition());
        savedInstanceState.putBoolean(EXO_PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && mExoPlayer != null) {
            releasePlayer();
        }
    }
}
