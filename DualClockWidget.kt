package com.dualclock.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class DualClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) updateWidget(context, appWidgetManager, id)
        scheduleNextUpdate(context)
    }

    override fun onEnabled(context: Context) = scheduleNextUpdate(context)

    override fun onDisabled(context: Context) {
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .cancel(getPendingIntent(context))
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_UPDATE) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(ComponentName(context, DualClockWidget::class.java))
            for (id in ids) updateWidget(context, mgr, id)
            scheduleNextUpdate(context)
        }
    }

    companion object {
        const val ACTION_UPDATE = "com.dualclock.widget.UPDATE"

        fun updateWidget(context: Context, mgr: AppWidgetManager, id: Int) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_layout)
                val now = Date()
                val tf = SimpleDateFormat("HH:mm", Locale.getDefault())

                tf.timeZone = TimeZone.getTimeZone("GMT+4")
                views.setTextViewText(R.id.city1_block, "Астрахань\n${tf.format(now)}")

                tf.timeZone = TimeZone.getTimeZone("GMT+5")
                views.setTextViewText(R.id.city2_block, "Когалым\n${tf.format(now)}")

                mgr.updateAppWidget(id, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun scheduleNextUpdate(context: Context) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 1)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .setExactAndAllowWhileIdle(AlarmManager.RTC, cal.timeInMillis, getPendingIntent(context))
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, DualClockWidget::class.java).apply { action = ACTION_UPDATE }
            return PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
