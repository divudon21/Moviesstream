package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.RemoteViews
import com.widgetfy.com.R
import com.widgetfy.com.util.TimeUtils

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_calendar)
            views.setTextViewText(R.id.tv_cal_month, TimeUtils.currentMonthYear())
            views.setTextViewText(R.id.tv_cal_days, TimeUtils.calendarDays())
            views.setTextViewText(R.id.tv_cal_event, "• ${TimeUtils.currentDate()} • Tap to view calendar")

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = CalendarContract.CONTENT_URI.buildUpon().appendPath("time").build()
            }
            val pi = try {
                PendingIntent.getActivity(context, 11, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } catch (e: Exception) {
                PendingIntent.getActivity(context, 11, Intent(context, com.widgetfy.com.MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
            views.setOnClickPendingIntent(R.id.tv_cal_event, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
