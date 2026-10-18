package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import kotlin.random.Random

class SavingsWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_savings)
            val saved = Random.nextInt(45, 85)
            views.setTextViewText(R.id.tv_savings_amount, "₹ ${saved},000")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
