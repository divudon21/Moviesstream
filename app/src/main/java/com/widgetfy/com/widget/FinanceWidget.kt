package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import kotlin.random.Random

class FinanceWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_finance)
            val saved = Random.nextInt(35000, 60000)
            views.setTextViewText(R.id.tv_finance_amount, "₹ ${"%,d".format(saved)}")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
