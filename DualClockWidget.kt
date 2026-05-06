package com.dualclock.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent

class DualClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        context.startService(Intent(context, ClockService::class.java))
    }

    override fun onEnabled(context: Context) {
        context.startService(Intent(context, ClockService::class.java))
    }

    override fun onDisabled(context: Context) {
        context.stopService(Intent(context, ClockService::class.java))
    }
}
