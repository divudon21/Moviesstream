package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.widgetfy.com.R

class SearchWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_search)
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra("query", "")
            }
            // Fallback to Google search URL
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            val pi = try {
                PendingIntent.getActivity(context, 14, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } catch (e: Exception) {
                PendingIntent.getActivity(context, 14, webIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
            views.setOnClickPendingIntent(R.id.widget_search_root, pi)
            // Also make whole layout clickable via fallback - we use search hint id not exist, use safe
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
