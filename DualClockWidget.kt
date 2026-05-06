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
        for (id in appWidgetIds) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_layout)
                val now = Date()

                val tf = SimpleDateFormat("HH:mm", Locale.getDefault())
                val df = SimpleDateFormat("EEE d MMM", Locale("ru"))

                tf.timeZone = TimeZone.getTimeZone("GMT+4")
                df.timeZone = TimeZone.getTimeZone("GMT+4")
                views.setTextViewText(
                    R.id.city1_block,
                    "Астрахань\n${tf.format(now)}\n${df.format(now)}"
                )

                tf.timeZone = TimeZone.getTimeZone("GMT+5")
                df.timeZone = TimeZone.getTimeZone("GMT+5")
                views.setTextViewText(
                    R.id.city2_block,
                    "Когалым\n${tf.format(now)}\n${df.format(now)}"
                )

                appWidgetManager.updateAppWidget(id, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
