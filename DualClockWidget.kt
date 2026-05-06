package com.dualclock.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews

class DualClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.e("DUALCLOCK", "onUpdate called, ids: ${appWidgetIds.toList()}")
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.test_text, "onUpdate OK ${appWidgetIds.size}")
            appWidgetManager.updateAppWidget(id, views)
        }
    }

    override fun onEnabled(context: Context) {
        Log.e("DUALCLOCK", "onEnabled called")
    }
}
