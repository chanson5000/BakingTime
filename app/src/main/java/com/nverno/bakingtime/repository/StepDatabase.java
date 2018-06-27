package com.nverno.bakingtime.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.nverno.bakingtime.model.Step;

@Database(entities = {Step.class}, version = 1, exportSchema = false)
public abstract class StepDatabase extends RoomDatabase {

    private static final String LOG_TAG = RecipeDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "steps";
    private static StepDatabase sInstance;

    public static StepDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new Popular Movies database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        StepDatabase.class, StepDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting Popular Movies database instance");
        return sInstance;
    }

    public abstract StepDao StepDao();

}
