package com.nverno.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String recipeName, String ingredients, int appWidgetId) {

        String recipeNameText;
        String ingredientsText;

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget);

        if (recipeName != null && ingredients != null) {
            recipeNameText = recipeName;
            ingredientsText = ingredients;

            // Set the text views
            views.setTextViewText(R.id.widget_text_ingredients_list, ingredientsText);
            views.setTextViewText(R.id.widget_text_recipe_name, recipeNameText);

            // Set the visibility
            views.setViewVisibility(R.id.widget_text_static_no_recipe_selected, View.GONE);
            views.setViewVisibility(R.id.widget_text_recipe_name, View.VISIBLE);
            views.setViewVisibility(R.id.widget_text_static_ingredients, View.VISIBLE);
            views.setViewVisibility(R.id.widget_text_ingredients_list, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_text_recipe_name, View.GONE);
            views.setViewVisibility(R.id.widget_text_static_ingredients, View.GONE);
            views.setViewVisibility(R.id.widget_text_ingredients_list, View.GONE);
            views.setViewVisibility(R.id.widget_text_static_no_recipe_selected, View.VISIBLE);
        }

        // The pending intent
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

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

        WidgetUpdateService.startActionUpdateRecipeWidgets(context);
    }

    public static void updateRecipeIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                      String recipeName, String ingredients,
                                                      int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeName, ingredients, appWidgetId);
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

