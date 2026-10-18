package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.widgetfy.com.MainActivity
import com.widgetfy.com.R
import com.widgetfy.com.util.WidgetPrefs

class StepCounterWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_steps)
            val steps = WidgetPrefs.getSteps(context)
            views.setTextViewText(R.id.tv_steps_count, "%,d".format(steps))
            views.setProgressBar(R.id.progress_steps, 10000, steps, false)
            val intent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 15, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_steps_count, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
