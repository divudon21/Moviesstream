package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.widgetfy.com.MainActivity
import com.widgetfy.com.R
import com.widgetfy.com.util.TimeUtils

class FlipClockWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_flip_clock)
            views.setTextViewText(R.id.tv_flip_hour, TimeUtils.hour())
            views.setTextViewText(R.id.tv_flip_minute, TimeUtils.minute())
            views.setTextViewText(R.id.tv_flip_ampm, TimeUtils.amPm())
            views.setTextViewText(R.id.tv_flip_date, TimeUtils.currentDayMonth())
            val intent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_flip_hour, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
