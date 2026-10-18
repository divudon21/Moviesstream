package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R

class VpnWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_vpn)
            val states = listOf("● Connected" to "Singapore • 42ms", "○ Disconnected" to "Tap to connect")
            val (s, loc) = states.random()
            views.setTextViewText(R.id.tv_vpn_status, s)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
