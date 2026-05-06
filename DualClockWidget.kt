package com.dualclock.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class DualClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEE d MMM", Locale("ru"))
            val now = Date()

            timeFormat.timeZone = TimeZone.getTimeZone("GMT+4")
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+4")
            views.setTextViewText(R.id.city1_name, "Астрахань")
            views.setTextViewText(R.id.city1_time, timeFormat.format(now))
            views.setTextViewText(R.id.city1_date, dateFormat.format(now))

            timeFormat.timeZone = TimeZone.getTimeZone("GMT+5")
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+5")
            views.setTextViewText(R.id.city2_name, "Когалым")
            views.setTextViewText(R.id.city2_time, timeFormat.format(now))
            views.setTextViewText(R.id.city2_date, dateFormat.format(now))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
