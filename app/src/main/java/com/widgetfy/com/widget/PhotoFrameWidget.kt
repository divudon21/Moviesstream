package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.widgetfy.com.MainActivity
import com.widgetfy.com.R

class PhotoFrameWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_photo)
            val intent = Intent(context, MainActivity::class.java).apply { putExtra("open_photo", true) }
            val pi = PendingIntent.getActivity(context, 13, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_photo_title, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
