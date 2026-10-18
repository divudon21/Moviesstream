package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import kotlin.random.Random

class ScreenTimeWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_screentime)
            val h = Random.nextInt(2, 8); val m = Random.nextInt(0, 59)
            views.setTextViewText(R.id.tv_screentime_hours, "${h}h ${m}m")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
