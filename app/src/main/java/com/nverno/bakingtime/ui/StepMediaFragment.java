package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepMediaFragment extends Fragment {

    private RecipeViewModel recipeViewModel;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView mNoMedia;

    private FragmentActivity mFragmentActivity;

    public StepMediaFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            mFragmentActivity = getActivity();
            recipeViewModel = ViewModelProviders.of(mFragmentActivity)
                    .get(RecipeViewModel.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step_media,
                container, false);

        mPlayerView = rootView.findViewById(R.id.recipe_step_media);
        mNoMedia = rootView.findViewById(R.id.recipe_no_media);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
    }

    private void initViewModel() {
        recipeViewModel.getSelectedRecipeStep().observe(mFragmentActivity, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
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
            }
        });
    }

    private void playerVisible() {
        mNoMedia.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void noMedia() {
        mPlayerView.setVisibility(View.GONE);
        mNoMedia.setVisibility(View.VISIBLE);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                    trackSelector,
                    loadControl);

            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the media resource.
            String userAgent = Util.getUserAgent(mFragmentActivity, "RecipeStepMedia");

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(mFragmentActivity, userAgent),
                    new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    // Optional
//    @Override
//    public void onPause() {
//        super.onPause();
//        releasePlayer();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        releasePlayer();
//    }
}
