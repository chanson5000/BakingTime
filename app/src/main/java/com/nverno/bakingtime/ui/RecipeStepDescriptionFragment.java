package com.nverno.bakingtime.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.adapter.OnClickHandler;
import com.nverno.bakingtime.adapter.RecipeStepsAdapter;
import com.nverno.bakingtime.model.Step;

public class RecipeStepDescriptionFragment extends Fragment
        implements OnClickHandler {

    private OnClickHandler mCallback;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        try {
            mCallback = (OnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeStepClickListener");
        }

    }

    public RecipeStepDescriptionFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_steps_fragment, container, false);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setHasFixedSize(true);

        RecipeStepsAdapter mStepsAdapter = new RecipeStepsAdapter(mCallback);

        return rootView;
    }

    public void onClick(Object object) {

    }

}
