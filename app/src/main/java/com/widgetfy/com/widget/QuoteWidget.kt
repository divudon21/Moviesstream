package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import com.widgetfy.com.util.QuoteProvider

class QuoteWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_quote)
            val (quote, author) = QuoteProvider.ofDay()
            views.setTextViewText(R.id.tv_quote_text, quote)
            views.setTextViewText(R.id.tv_quote_author, "— $author")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
