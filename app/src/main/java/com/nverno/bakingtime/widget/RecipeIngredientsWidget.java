package com.nverno.bakingtime.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.ui.MainActivity;
import com.nverno.bakingtime.ui.RecipeDetailActivity;
import com.nverno.bakingtime.util.WidgetHelper;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    private static final String RECIPE_ID = "RECIPE_ID";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget);

        // The pending intent
        Intent intent;

        Recipe recipe = WidgetHelper.getInstance().getCurrentRecipe();

        // If we have recipe data from the helper. (Recipe was selected in the activity UI)
        if (recipe != null) {
            // Set the text views
            views.setTextViewText(R.id.widget_text_ingredients_list, recipe.getIngredientsString());
            views.setTextViewText(R.id.widget_text_recipe_name, recipe.getName());

            // Set the visibility
            views.setViewVisibility(R.id.widget_text_static_no_recipe_selected, View.GONE);
            views.setViewVisibility(R.id.widget_text_recipe_name, View.VISIBLE);
            views.setViewVisibility(R.id.widget_text_static_ingredients, View.VISIBLE);
            views.setViewVisibility(R.id.widget_text_ingredients_list, View.VISIBLE);

            // Set our intent information for the activity.
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RECIPE_ID, recipe.getId());
        } else {
            views.setViewVisibility(R.id.widget_text_recipe_name, View.GONE);
            views.setViewVisibility(R.id.widget_text_static_ingredients, View.GONE);
            views.setViewVisibility(R.id.widget_text_ingredients_list, View.GONE);
            views.setViewVisibility(R.id.widget_text_static_no_recipe_selected, View.VISIBLE);

            intent = new Intent(context, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // This  commented code is the "vanilla" way to create a pending intent.
        // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
        // intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // This way adds a back stack in case the widget has launched the activity on its own
        // without an already existing back stack.
        PendingIntent pendingIntent =
                TaskStackBuilder.create(context).addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // Triggers every time the widget is created and on every update interval.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // Always expect multiple instances exist and update them

        WidgetUpdateService.updateWidget(context);
    }

    public static void updateRecipeIngredientsWidgets(Context context,
                                                      AppWidgetManager appWidgetManager,
                                                      int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
