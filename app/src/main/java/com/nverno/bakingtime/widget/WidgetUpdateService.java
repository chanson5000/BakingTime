package com.nverno.bakingtime.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class WidgetUpdateService extends IntentService {

    private static final String ACTION_UPDATE_RECIPE_WIDGETS
            = "com.nverno.bakingtime.action.update_recipe_widgets";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleUpdateRecipeWidget();
            }
        }
    }

    private void handleUpdateRecipeWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, RecipeIngredientsWidget.class));

        // Update all widgets
        RecipeIngredientsWidget.updateRecipeIngredientsWidgets(this,
                appWidgetManager, appWidgetIds);

    }
}
