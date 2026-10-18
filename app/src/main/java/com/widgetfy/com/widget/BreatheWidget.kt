package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R

class BreatheWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_breathe)
            val phase = listOf("Inhale • 4s", "Hold • 4s", "Exhale • 4s").random()
            views.setTextViewText(R.id.tv_breathe_text, phase)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
