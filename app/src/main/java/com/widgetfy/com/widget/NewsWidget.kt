package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R

class NewsWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val headlines = listOf(
            listOf("India wins test match vs England", "ISRO launch successful", "Market hits new high"),
            listOf("AI revolution in 2024", "Chandrayaan update", "Festival sales soar"),
            listOf("New metro line opened", "Climate summit concludes", "Tech stocks rally")
        )
        val pick = headlines.random()
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_news)
            views.setTextViewText(R.id.tv_news_1, "• ${pick[0]}")
            views.setTextViewText(R.id.tv_news_2, "• ${pick[1]}")
            views.setTextViewText(R.id.tv_news_3, "• ${pick[2]}")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
