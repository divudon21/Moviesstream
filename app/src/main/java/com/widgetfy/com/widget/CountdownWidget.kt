package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CountdownWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_countdown)
            val diwali = Calendar.getInstance().apply { set(2024, Calendar.OCTOBER, 31) }
            val now = Calendar.getInstance()
            val diff = diwali.timeInMillis - now.timeInMillis
            val days = TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(0)
            views.setTextViewText(R.id.tv_countdown_days, "%02d".format(days))
            views.setTextViewText(R.id.tv_countdown_label, "days to Diwali")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
