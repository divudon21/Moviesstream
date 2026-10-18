package com.widgetfy.com.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.widgetfy.com.MainActivity
import com.widgetfy.com.R
import kotlin.random.Random

class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_weather)
            val temps = listOf("24°C","26°C","28°C","30°C","32°C","27°C","29°C")
            val cities = listOf("Ahmedabad","Mumbai","Delhi","Bangalore","Jaipur")
            val descs = listOf("Sunny","Partly Cloudy","Clear","Haze","Light Rain")
            val temp = temps.random()
            val city = cities.random()
            val desc = descs.random()
            views.setTextViewText(R.id.tv_weather_temp, temp)
            views.setTextViewText(R.id.tv_weather_city, city)
            views.setTextViewText(R.id.tv_weather_icon, desc)
            views.setTextViewText(R.id.tv_weather_desc, "$desc • H:${Random.nextInt(30,35)}° L:${Random.nextInt(20,24)}°")

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://weather.com"))
            val pi = PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.tv_weather_temp, pi)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
