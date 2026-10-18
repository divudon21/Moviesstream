package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import com.widgetfy.com.util.TimeUtils

class WorldClockWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_world_clock)
            val (delhi, london, ny) = TimeUtils.worldTimes()
            views.setTextViewText(R.id.tv_world_delhi, delhi)
            views.setTextViewText(R.id.tv_world_london, london)
            views.setTextViewText(R.id.tv_world_ny, ny)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
