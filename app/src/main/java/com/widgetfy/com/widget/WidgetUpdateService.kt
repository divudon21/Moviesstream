package com.widgetfy.com.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder

class WidgetUpdateService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mgr = AppWidgetManager.getInstance(this)

        // Update all clock-based widgets every minute
        val clocks = listOf(
            DigitalClockWidget::class.java,
            AnalogClockWidget::class.java,
            FlipClockWidget::class.java,
            WorldClockWidget::class.java,
            CalendarWidget::class.java,
            QuoteWidget::class.java,
            MusicWidget::class.java,
            CountdownWidget::class.java,
            FinanceWidget::class.java,
            HealthWidget::class.java,
            NewsWidget::class.java,
            CryptoWidget::class.java,
            HabitWidget::class.java,
            ReminderWidget::class.java
        )
        for (cls in clocks) {
            val ids = mgr.getAppWidgetIds(ComponentName(this, cls))
            if (ids.isNotEmpty()) {
                val update = Intent(this, cls).apply { action = AppWidgetManager.ACTION_APPWIDGET_UPDATE; putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids) }
                sendBroadcast(update)
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }
}
