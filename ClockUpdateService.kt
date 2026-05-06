package com.dualclock.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class ClockUpdateService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            val mgr = AppWidgetManager.getInstance(this@ClockUpdateService)
            val ids = mgr.getAppWidgetIds(
                ComponentName(this@ClockUpdateService, DualClockWidget::class.java)
            )
            for (id in ids) DualClockWidget.update(this@ClockUpdateService, mgr, id)
            handler.postDelayed(this, 60_000L)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
