package com.nverno.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.util.IngredientStringHelper;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String recipeName,
                                String ingredients, int appWidgetId) {

        String recipeNameText;
        String ingredientsText;

        if (recipeName != null) {
            recipeNameText = recipeName;
        } else {
            recipeNameText = context.getString(R.string.no_recipe_selected);
        }

        if (ingredients != null) {
            ingredientsText = ingredients;
        } else {
            ingredientsText = "";
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        views.setTextViewText(R.id.ingredients_widget_text, ingredientsText);
        views.setTextViewText(R.id.ingredients_widget_recipe_name, recipeNameText);

        // The pending intent
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.ingredients_widget_text, pendingIntent);

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

