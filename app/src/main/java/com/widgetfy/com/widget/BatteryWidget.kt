package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews
import com.widgetfy.com.R

class BatteryWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) updateBattery(context, appWidgetManager, id)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(android.content.ComponentName(context, BatteryWidget::class.java))
            for (id in ids) updateBattery(context, mgr, id, intent)
        }
    }

    companion object {
        fun updateBattery(context: Context, mgr: AppWidgetManager, id: Int, batteryIntent: Intent? = null) {
            val views = RemoteViews(context.packageName, R.layout.widget_battery)
            val intent = batteryIntent ?: context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 50) ?: 50
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
            val pct = (level * 100 / scale.toFloat()).toInt()
            val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
            val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) ?: 0
            val health = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD) ?: BatteryManager.BATTERY_HEALTH_GOOD

            views.setTextViewText(R.id.tv_battery_percent, "$pct%")
            views.setProgressBar(R.id.progress_battery, 100, pct, false)
            views.setTextViewText(R.id.tv_battery_status, when {
                status == BatteryManager.BATTERY_STATUS_FULL -> "Full"
                isCharging && plugged == BatteryManager.BATTERY_PLUGGED_AC -> "Charging AC"
                isCharging && plugged == BatteryManager.BATTERY_PLUGGED_USB -> "Charging USB"
                isCharging -> "Charging"
                else -> "Discharging"
            })
            views.setTextViewText(R.id.tv_battery_info, when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> "Healthy • ${if(isCharging) "Fast charging" else "Good"}"
                else -> "Battery health: $health"
            })
            mgr.updateAppWidget(id, views)
        }
    }
}
