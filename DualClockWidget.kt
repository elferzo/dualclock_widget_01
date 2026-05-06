package com.dualclock.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
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

            // Astrakhan UTC+4
            timeFormat.timeZone = TimeZone.getTimeZone("GMT+4")
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+4")
            views.setTextViewText(R.id.city1_name, "Астрахань")
            views.setTextViewText(R.id.city1_time, timeFormat.format(now))
            views.setTextViewText(R.id.city1_date, dateFormat.format(now))

            // Kogalym UTC+5
            timeFormat.timeZone = TimeZone.getTimeZone("GMT+5")
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+5")
            views.setTextViewText(R.id.city2_name, "Когалым")
            views.setTextViewText(R.id.city2_time, timeFormat.format(now))
            views.setTextViewText(R.id.city2_date, dateFormat.format(now))

            // Click opens Samsung clock app
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setPackage("com.sec.android.app.clockpackage")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
