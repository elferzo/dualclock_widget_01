package com.dualclock.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class ClockService : Service() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            tick()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        registerReceiver(receiver, filter)
        tick() // update immediately on start
        return START_STICKY
    }

    private fun tick() {
        val mgr = AppWidgetManager.getInstance(this)
        val ids = mgr.getAppWidgetIds(ComponentName(this, DualClockWidget::class.java))
        if (ids.isEmpty()) { stopSelf(); return }
        val views = RemoteViews(packageName, R.layout.widget_layout)
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        views.setTextViewText(R.id.test_text, "TEST $time")
        mgr.updateAppWidget(ids, views)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
