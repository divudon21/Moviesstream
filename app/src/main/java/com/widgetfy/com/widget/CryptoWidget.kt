package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.widgetfy.com.R
import kotlin.random.Random

class CryptoWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_crypto)
            val btc = Random.nextInt(55, 62)
            val change = Random.nextDouble(1.0, 4.0)
            views.setTextViewText(R.id.tv_crypto_btc, "BTC ₹${btc},24,000")
            views.setTextViewText(R.id.tv_crypto_change, "▲ +${"%.1f".format(change)}% Today")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
