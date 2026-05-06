package com.dualclock.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class DualClockWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_layout)
                val now = Date()

                val tf = SimpleDateFormat("HH:mm", Locale.getDefault())
                val df = SimpleDateFormat("EEE d MMM", Locale("ru"))

                tf.timeZone = TimeZone.getTimeZone("GMT+4")
                df.timeZone = TimeZone.getTimeZone("GMT+4")
                views.setTextViewText(R.id.city1_name, "Астрахань")
                views.setTextViewText(R.id.city1_time, tf.format(now))
                views.setTextViewText(R.id.city1_date, df.format(now))

                tf.timeZone = TimeZone.getTimeZone("GMT+5")
                df.timeZone = TimeZone.getTimeZone("GMT+5")
                views.setTextViewText(R.id.city2_name, "Когалым")
                views.setTextViewText(R.id.city2_time, tf.format(now))
                views.setTextViewText(R.id.city2_date, df.format(now))

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
