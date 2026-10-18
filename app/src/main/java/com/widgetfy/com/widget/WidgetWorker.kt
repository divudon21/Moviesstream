package com.widgetfy.com.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class WidgetWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val context = applicationContext
        val mgr = AppWidgetManager.getInstance(context)
        val allWidgets = listOf(
            DigitalClockWidget::class.java,
            AnalogClockWidget::class.java,
            FlipClockWidget::class.java,
            WeatherWidget::class.java,
            CalendarWidget::class.java,
            QuoteWidget::class.java,
            WorldClockWidget::class.java,
            StepCounterWidget::class.java,
            MusicWidget::class.java,
            CountdownWidget::class.java,
            FinanceWidget::class.java,
            HealthWidget::class.java,
            NewsWidget::class.java,
            CryptoWidget::class.java,
            HabitWidget::class.java,
            ReminderWidget::class.java
        )
        for (cls in allWidgets) {
            val ids = mgr.getAppWidgetIds(ComponentName(context, cls))
            if (ids.isNotEmpty()) {
                context.sendBroadcast(Intent(context, cls).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                })
            }
        }
        return Result.success()
    }
}
