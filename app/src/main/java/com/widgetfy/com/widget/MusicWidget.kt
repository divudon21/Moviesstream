package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.widgetfy.com.MainActivity
import com.widgetfy.com.R

class MusicWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_music)
            val songs = listOf("Lofi Beats" to "Chillhop", "Kesariya" to "Pritam", "Apna Bana Le" to "Arijit")
            val (t,a) = songs.random()
            views.setTextViewText(R.id.tv_music_title, t)
            views.setTextViewText(R.id.tv_music_artist, "$a • 2:34/4:12")
            val intent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 20, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_music_title, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
