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

class TodoWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_todo)
            val notes = WidgetPrefs.getNotes(context)
            if (notes.isNotEmpty()) {
                if (notes.size > 0) views.setTextViewText(R.id.tv_todo_1, "☐ ${notes[0]}")
                if (notes.size > 1) views.setTextViewText(R.id.tv_todo_2, "☐ ${notes[1]}")
                if (notes.size > 2) views.setTextViewText(R.id.tv_todo_3, "☑ ${notes[2]}")
            }
            val intent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 12, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_todo_1, pi)
            views.setOnClickPendingIntent(R.id.tv_todo_2, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
