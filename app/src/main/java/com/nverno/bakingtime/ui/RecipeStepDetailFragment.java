package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class RecipeStepDetailFragment extends Fragment {

    private RecipeViewModel recipeViewModel;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView mTextRecipeStepInstruction;
    private ImageView mImageRecipeStep;
    private TextView mNoMedia;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            recipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        }
    }

    public RecipeStepDetailFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_step_detail_fragment,
                container,
                false);

        mPlayerView = rootView.findViewById(R.id.recipe_step_media);
        mTextRecipeStepInstruction = rootView.findViewById(R.id.recipe_step_instructions);
        mImageRecipeStep = rootView.findViewById(R.id.recipe_step_image);
        mNoMedia = rootView.findViewById(R.id.recipe_no_media);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recipeViewModel.getSelectedRecipeStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                if (step != null) {
//                    if (!step.getVideoURL().isEmpty()) {
//                        playerVisible();
//                        initializePlayer(Uri.parse(step.getVideoURL()));
//                    } else if (!step.getThumbnailURL().isEmpty()) {
//                        imageVisible();
//                        Picasso.with(getContext()).load(step.getThumbnailURL())
//                                .into(mImageRecipeStep);
//                    } else {
//                        noMedia();
//                    }

                    mNoMedia.setText(step.getDescription());
                }
            }
        });
    }

    private void playerVisible() {
        mImageRecipeStep.setVisibility(View.INVISIBLE);
        mNoMedia.setVisibility(View.INVISIBLE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void imageVisible() {
        mPlayerView.setVisibility(View.INVISIBLE);
        mNoMedia.setVisibility(View.INVISIBLE);
        mImageRecipeStep.setVisibility(View.VISIBLE);
    }

    private void noMedia() {
        mPlayerView.setVisibility(View.INVISIBLE);
        mImageRecipeStep.setVisibility(View.INVISIBLE);
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
            String userAgent = Util.getUserAgent(getActivity(), "RecipeStepMedia");

            if (getActivity() != null) {
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                        new DefaultDataSourceFactory(getActivity(), userAgent),
                        new DefaultExtractorsFactory(), null, null);

                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        }
    }

}
