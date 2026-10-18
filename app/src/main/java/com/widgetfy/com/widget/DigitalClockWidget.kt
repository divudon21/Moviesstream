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
import java.util.*

class DigitalClockWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) update(context, appWidgetManager, id)
    }

    companion object {
        fun update(context: Context, mgr: AppWidgetManager, id: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_digital_clock)
            views.setTextViewText(R.id.tv_time, TimeUtils.currentTime())
            views.setTextViewText(R.id.tv_date, TimeUtils.currentDate())
            views.setTextViewText(R.id.tv_seconds, "${TimeUtils.amPm()} • ${TimeZone.getDefault().displayName}")

            val intent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_time, pi)

            mgr.updateAppWidget(id, views)
        }
    }
}
