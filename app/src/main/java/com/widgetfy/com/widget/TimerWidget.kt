package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R

class TimerWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_timer)
            // Liquid Glass timer - static 25:00 demo, real app would use service
            views.setTextViewText(R.id.tv_timer_time, "25:00")
            views.setTextViewText(R.id.tv_timer_label, "Pomodoro • Focus")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
