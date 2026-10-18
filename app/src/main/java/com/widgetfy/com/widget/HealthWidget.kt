package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R

class HealthWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_health)
            views.setTextViewText(R.id.tv_health_sleep, "${(6..8).random()}.${(0..9).random()}h")
            views.setTextViewText(R.id.tv_health_water, "${(5..8).random()}/8")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
